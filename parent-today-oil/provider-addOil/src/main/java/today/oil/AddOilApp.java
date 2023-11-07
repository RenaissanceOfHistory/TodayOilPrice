package today.oil;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @create: 2023/10/31
 * @Description:
 * @FileName: AddOilApp
 */
@MapperScan(basePackages = "today.oil.mapper")
@EnableDiscoveryClient
@SpringBootApplication
public class AddOilApp {
    public static void main(String[] args) {
        SpringApplication.run(AddOilApp.class, args);
    }
}