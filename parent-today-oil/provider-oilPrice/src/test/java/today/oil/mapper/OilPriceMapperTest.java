package today.oil.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@SpringBootTest
class OilPriceMapperTest {

    @Autowired
    private OilPriceMapper oilPriceMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${sidecar.ip}")
    private String ip;

    @Value("${sidecar.port}")
    private Integer port;

    @SuppressWarnings("unchecked")
    @Test
    void insertProvince() {
        final String URL = "http://" + ip + ":" + port + "/oils/all";
        log.info(URL);
        Map<Object, Object> map = restTemplate.getForEntity(URL, Map.class).getBody();
        Optional.ofNullable(map)
                .ifPresent(objectMap -> {
                    objectMap.forEach((o, o2) -> System.out.println(o + "\t\t" + o2));
                    oilPriceMapper.insertProvince((List<String>) map.get("data"));
                });
    }

    @SuppressWarnings("unchecked")
    @Test
    void insertProvinceOilPrice() {
        final String URL = "http://" + ip + ":" + port + "/oils/query/北京";
        log.info(URL);
        Map<Object, Object> map = restTemplate.getForEntity(URL, Map.class).getBody();
        Optional.ofNullable(map)
                .ifPresent(objectMap -> {
                    List<Map<String, String>> provinceList = (List<Map<String, String>>) objectMap.get("data");
                    provinceList.forEach(pro -> {
                        try {
                            pro.put("label_89", "" + Double.parseDouble(pro.get("label_89")));
                            pro.put("label_92", "" + Double.parseDouble(pro.get("label_92")));
                            pro.put("label_95", "" + Double.parseDouble(pro.get("label_95")));
                            pro.put("label_0", "" + Double.parseDouble(pro.get("label_0")));
                        } catch (NumberFormatException ignored) {
                            pro.put("label_89", "0");
                            pro.put("label_92", "0");
                            pro.put("label_95", "0");
                            pro.put("label_0", "0");
                        }
                        System.out.println(pro);
                    });
                    System.out.println(oilPriceMapper.insertProvinceOilPrice("北京", provinceList));
                });
    }

    @SuppressWarnings("unchecked")
    @Test
    void insertLabelOilPrice() {
        final String URL = "http://" + ip + ":" + port + "/oils/query/label/0";
        log.info(URL);
        Map map = restTemplate.getForEntity(URL, Map.class).getBody();
        Optional.ofNullable(map)
                .ifPresent(m -> {
                    m.forEach((o, o2) -> System.out.println(o + "\t\t" + o2));
                    List<Map<String, String>> labelList = (List<Map<String, String>>) m.get("data");
                    labelList.forEach(label -> {
                        try {
                            label.put("price", "" + Double.parseDouble(label.get("price")));
                        } catch (NumberFormatException ignored) {
                            label.put("price", "0");
                        }
                        System.out.println(label);
                    });
                    System.out.println(oilPriceMapper.insertLabelOilPrice("0", labelList));
                });
    }

    @Test
    void selectProvinceName() {
        System.out.println(oilPriceMapper.selectProvinceName());
    }

    @Test
    void CTProvince() {
        int ctProvince = oilPriceMapper.CTProvince();
        log.info("info = {}", ctProvince);
    }

    @Test
    void CTProvinceOilPrice() {
        int ctProvinceOilPrice = oilPriceMapper.CTProvinceOilPrice("北京");
        log.info("info = {}", ctProvinceOilPrice);
    }

    @Test
    void CTLabel() {
        int ctLabel = oilPriceMapper.CTLabel("0");
        log.info("info = {}", ctLabel);
    }

    @Test
    void selectByLabel() {
        List<Map<Object, Object>> mapList = oilPriceMapper.selectByLabel("0", new Date());
        mapList.forEach(System.out::println);
    }

    @Test
    void selectByProvince() {
        List<Map<Object, Object>> mapList = oilPriceMapper.selectByProvince("北京", new Date());
        mapList.forEach(System.out::println);
    }
}