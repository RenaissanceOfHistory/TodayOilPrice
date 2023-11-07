package today.oil;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @create: 2023/10/31
 * @Description:
 * @FileName: FareApp
 */
@MapperScan(basePackages = "today.oil.mapper")
@EnableDiscoveryClient
@SpringBootApplication
public class FareApp {
    public static void main(String[] args) {
        SpringApplication.run(FareApp.class, args);
    }
}