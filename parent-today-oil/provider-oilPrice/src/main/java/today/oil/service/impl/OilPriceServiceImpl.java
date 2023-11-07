package today.oil.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import today.oil.mapper.OilPriceMapper;
import today.oil.service.OilPriceService;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @create: 2023/11/1
 * @Description:
 * @FileName: OilPriceServiceImpl
 */
@Slf4j
@Service
public class OilPriceServiceImpl implements OilPriceService {

    @Value("${sidecar.ip}")
    private String ip;

    @Value("${sidecar.port}")
    private Integer port;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private OilPriceMapper oilPriceMapper;

    /** 标号 */
    private static final String[] labels = {"89", "92", "95", "98", "0"};

    @Override
    public List queryAllProvinces() {
        oilPriceMapper.CTProvince();

        final String KEY = "provinceList";
        log.info("从缓存查询省份集合");
        List provinceList = redisTemplate.opsForList().range(KEY, 0, -1);

        if (Objects.isNull(provinceList) || provinceList.isEmpty()) {
            log.info("省份集合为空");
            synchronized (this) {
                log.info("再次查询省份集合");
                provinceList = redisTemplate.opsForList().range(KEY, 0, -1);
                if (Objects.isNull(provinceList) || provinceList.isEmpty()) {
                    provinceList = queryAllProvinces(KEY);
                }
            }
        }
        return provinceList;
    }

    private List queryAllProvinces(final String key) {
        log.info("从数据库查询省份集合");
        List<String> provinceList = oilPriceMapper.selectProvinceName();
        if (Objects.isNull(provinceList) || provinceList.isEmpty()) crawlProvinces();

        provinceList = oilPriceMapper.selectProvinceName();
        if (Objects.nonNull(provinceList) && !provinceList.isEmpty()) {
            log.info("设置省份集合缓存");
            redisTemplate.opsForList().rightPushAll(key, provinceList.toArray());
            redisTemplate.expire(key, 24, TimeUnit.HOURS);
        }
        return provinceList;
    }

    @Override
    public List queryOilPriceByProvince(String province) {
        if (!hasProvince(province)) {
            log.info("不存在省份名：province({})", province);
            return null;
        }

        oilPriceMapper.CTProvinceOilPrice(province);
        final String KEY = "oilPriceList::" + province;
        log.info("从缓存查询油价信息集合：province({})", province);
        List oilPriceList = redisTemplate.opsForList().range(KEY, 0, -1);

        if (Objects.isNull(oilPriceList) || oilPriceList.isEmpty()) {
            log.info("油价信息集合为空：province({})", province);
            synchronized (this) {
                log.info("再次查询油价信息集合：province({})", province);
                oilPriceList = redisTemplate.opsForList().range(KEY, 0, -1);
                if (Objects.isNull(oilPriceList) || oilPriceList.isEmpty()) {
                    oilPriceList = queryOilPriceByProvince(KEY, province);
                }
            }
        }
        return oilPriceList;
    }

    /** 判断是否存在province
     * @Description:
     * @date: 2023/11/2
     * @param province 省份名
     * @return: boolean
     */
    private boolean hasProvince(final String province) {
        List provinceName = queryAllProvinces();
        return Objects.nonNull(provinceName) && provinceName.contains(province);
    }

    /** 查询province油价信息
     * @Description:
     * @date: 2023/11/2
     * @param key Redis Key
     * @param province 省份名
     * @return: java.util.List
     */
    private List queryOilPriceByProvince(final String key, String province) {
        log.info("从数据库查询油价信息集合: province({})", province);
        List oilPriceList = oilPriceMapper.selectByProvince(province, new Date());

        // 数据库不存在信息时将执行爬虫
        if (Objects.isNull(oilPriceList) || oilPriceList.isEmpty()) {
            try {
                crawlByProvince(province);
                log.info("爬虫请求成功：province({})", province);
            } catch (Exception ex) {
                log.error("爬虫请求失败", ex);
            }
        }

        // 爬虫执行后从数据库查询当前日期或之前日期的数据
        for (int i = 0; i < 100; i++) {
            // 首次查询存在数据时，避免二次查询
            if (Objects.isNull(oilPriceList) || oilPriceList.isEmpty()) {
                log.info("从数据库查询油价信息集合：province({})", province);
                Date date = Date.from(LocalDate.now().minusDays(i).atStartOfDay(ZoneId.systemDefault()).toInstant());
                oilPriceList = oilPriceMapper.selectByProvince(province, date);
            }
            // 如果存在数据，设置缓存
            if (Objects.nonNull(oilPriceList) && !oilPriceList.isEmpty()) {
                log.info("油价信息查询成功，设置缓存：province({})", province);
                redisTemplate.opsForList().rightPushAll(key, oilPriceList.toArray());
                redisTemplate.expire(key, 24, TimeUnit.HOURS);
                break;
            }
        }
        return oilPriceList;
    }

    /** 爬取province油价信息
     * @Description:
     * @date: 2023/11/2
     * @param province 省份名
     * @return: void
     */
    private void crawlByProvince(String province) {
        log.info("爬取油价信息：province({})", province);
        List<Map<String, String>> oilPriceList = getData("/oils/query/province/" + province);
        if (Objects.nonNull(oilPriceList) && !oilPriceList.isEmpty()) {
            log.info("油价信息预处理：province({})", province);
            // 对缺失数值补0
            oilPriceList.forEach(oilPrice -> {
                for (String label : labels) {
                    try {
                        Double.parseDouble(oilPrice.getOrDefault("label_" + label, ""));
                    } catch (NumberFormatException ignored) {
                        oilPrice.put("label_" + label, "0");
                    }
                }
            });
            log.info("预处理完成：{}", oilPriceList);
            log.info("数据库添加油价信息：province({})", province);
            oilPriceMapper.insertProvinceOilPrice(province, oilPriceList);
        }
    }

    @Override
    public List queryOilPriceByLabel(String label) {
        if (!hasLabel(label)) {
            log.info("不存在标号名：label({})", label);
            return null;
        }

        oilPriceMapper.CTLabel(label);
        final String KEY = "oilPriceList::label::" + label;
        log.info("从缓存查询油价信息集合：label({})", label);
        List labelList = redisTemplate.opsForList().range(KEY, 0, -1);

        if (Objects.isNull(labelList) || labelList.isEmpty()) {
            log.info("油价信息集合为空：label({})", label);
            synchronized (this) {
                log.info("再次查询油价信息集合：label({})", label);
                labelList = redisTemplate.opsForList().range(KEY, 0, -1);
                if (Objects.isNull(labelList) || labelList.isEmpty()) {
                    labelList = queryOilPriceByLabel(KEY, label);
                }
            }
        }
        return labelList;
    }

    /** 判断是否存在label
     * @Description:
     * @date: 2023/11/2
     * @param label 标号
     * @return: boolean
     */
    private boolean hasLabel(final String label) {
        for (String lb : labels) {
            if (lb.equals(label)) return true;
        }
        return false;
    }

    /** 查询label油价信息
     * @Description:
     * @date: 2023/11/2
     * @param key Redis Key
     * @param label 标号
     * @return: java.util.List
     */
    private List queryOilPriceByLabel(final String key, String label) {
        log.info("从数据库查询油价信息集合: label({})", label);
        List labelList = oilPriceMapper.selectByLabel(label, new Date());

        if (Objects.isNull(labelList) || labelList.isEmpty()) {
            try {
                crawlByLabel(label);
                log.info("爬虫请求成功：label({})", label);
            } catch (Exception ex) {
                log.error("爬虫请求失败", ex);
            }
        }

        //noinspection LoopConditionNotUpdatedInsideLoop
        for (int i = 0; i < 100; i++) {
            if (Objects.isNull(labelList) || labelList.isEmpty()) {
                log.info("从数据库查询油价信息集合: label({})", label);
                Date date = Date.from(LocalDate.now().minusDays(i).atStartOfDay(ZoneId.systemDefault()).toInstant());
                labelList = oilPriceMapper.selectByLabel(label, date);
            }
            if (Objects.nonNull(labelList) && !labelList.isEmpty()) {
                log.info("油价信息查询成功，设置缓存：label({})", label);
                redisTemplate.opsForList().rightPushAll(key, labelList.toArray());
                redisTemplate.expire(key, 24, TimeUnit.HOURS);
                break;
            }
        }
        return labelList;
    }

    /** 爬取label油价信息
     * @Description:
     * @date: 2023/11/2
     * @param label 标号
     * @return: void
     */
    private void crawlByLabel(String label) {
        log.info("爬取油价信息：label({})", label);
        List<Map<String, String>> labelList = getData("/oils/query/label/" + label);
        if (Objects.nonNull(labelList) && !labelList.isEmpty()) {
            log.info("油价信息预处理：label({})", label);
            labelList.forEach(item -> {
                try {
                    Double.parseDouble(item.getOrDefault("price", ""));
                } catch (NumberFormatException ignored) {
                    item.put("price", "0");
                }
            });
            log.info("预处理完成：{}", labelList);
            log.info("数据库添加油价信息：label({})", label);
            oilPriceMapper.insertLabelOilPrice(label, labelList);
        }
    }

    /** 获取URL全路径
     * @Description:
     * @date: 2023/11/2
     * @param suffix 请求路径
     * @return: java.lang.String
     */
    private String getURL(String suffix) {
        return "http://" + ip + ":" + port + suffix;
    }

    /** 获取响应的data
     * @Description:
     * @date: 2023/11/2
     * @param suffix 请求路径
     * @return: java.util.List<java.util.Map<java.lang.String,java.lang.String>>
     */
    @SuppressWarnings("unchecked")
    private List<Map<String, String>> getData(String suffix) {
        log.info("请求Python服务");
        Map map = restTemplate.getForEntity(getURL(suffix), Map.class).getBody();
        if (Objects.isNull(map)) return null;

        log.info("map: {}", map);
        return (List<Map<String, String>>) map.get("data");
    }

    @SuppressWarnings("unchecked")
    private void crawlProvinces() {
        log.info("爬取省份集合");
        Map map = restTemplate.getForEntity(getURL("/oils/all"), Map.class).getBody();
        Optional.ofNullable(map)
                .ifPresent(map1 -> oilPriceMapper.insertProvince((List<String>) map1.get("data")));
    }

    /** 定时爬虫任务
     * @Description:
     * @date: 2023/11/2
     * @param
     * @return: void
     */
    @SuppressWarnings("unchecked")
    @Scheduled(cron = "0 0 0 * * ?") // 每日0时
    public void crawlOilPriceInfo() {
        log.info("执行定时爬取油价信息任务");
        final SecureRandom random = new SecureRandom();
        List provinceList = queryAllProvinces();
        log.info("provinceList：{}", provinceList);
        if (Objects.nonNull(provinceList)) {
            provinceList.forEach(province -> {
                try {
                    log.info("爬取油价信息：province({})", province);
                    queryOilPriceByProvince("" + province);

                    int timeout = random.nextInt(25) + 5;
                    log.info("设置随机超时：{}sec\n", timeout);
                    TimeUnit.SECONDS.sleep(timeout);
                } catch (InterruptedException e) {
                    log.error("", e);
                }
            });
        }

        log.info("爬取标号油价信息：{}", Arrays.toString(labels));
        for (String label : labels) {
            try {
                queryOilPriceByLabel(label);
                int timeout = random.nextInt(25) + 5;
                log.info("设置随机超时：{}sec\n", timeout);
                TimeUnit.SECONDS.sleep(timeout);
            } catch (InterruptedException e) {
                log.error("", e);
            }
        }
    }
}
