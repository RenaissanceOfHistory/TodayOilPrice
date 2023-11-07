package today.oil.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import today.oil.mapper.OilPriceMapper;
import today.oil.service.impl.OilPriceServiceImpl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest
class OilPriceServiceTest {

    @Autowired
    private OilPriceServiceImpl oilPriceService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private OilPriceMapper oilPriceMapper;

    @Test
    void crawl() {
        oilPriceService.crawlOilPriceInfo();
    }

    @Test
    void test01() {
        Set<String> keySet = redisTemplate.keys("*");
        Optional.ofNullable(keySet)
                .ifPresent(set -> {
                    set.forEach(key -> {
                        System.out.println(key);
                        redisTemplate.delete(key);
                    });
                });
        System.out.println(redisTemplate.keys("*"));
    }

    @SuppressWarnings("unchecked")
    @Test
    void test02() {
        long toMillis = TimeUnit.DAYS.toMillis(1);
        System.out.println("toMillis = " + toMillis);
        for (int i = 0; i < 3; i++) {
            Date date = new Date(System.currentTimeMillis() - toMillis * i);
            System.out.println(date);
            List oilPriceList = oilPriceMapper.selectByProvince("云南", date);
            System.out.println(oilPriceList);
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    void dropAllProvincesTable() {
        List provinceList = oilPriceService.queryAllProvinces();
        Optional.ofNullable(provinceList)
                .ifPresent(list ->
                        list.forEach(province ->
                                System.out.println(oilPriceMapper.dropTableByProvince((String) province))));
    }

    @Test
    void dropLabelTable() {
        final String[] labels = {"89", "92", "95", "98", "0"};
        for (String label : labels) {
            System.out.println(oilPriceMapper.dropTableByLabel(label));
        }
    }
}