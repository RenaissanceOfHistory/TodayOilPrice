package today.oil;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @create: 2023/11/1
 * @Description:
 * @FileName: OilPriceApp
 */
@EnableScheduling
@MapperScan(basePackages = "today.oil.mapper")
@EnableDiscoveryClient
@SpringBootApplication
public class OilPriceApp {
    public static void main(String[] args) {
        SpringApplication.run(OilPriceApp.class, args);
    }
}