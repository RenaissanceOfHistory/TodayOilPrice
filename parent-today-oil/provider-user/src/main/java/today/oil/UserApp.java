package today.oil;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @create: 2023/10/30
 * @Description:
 * @FileName: UserApp
 */
@MapperScan(basePackages = "today.oil.mapper")
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class UserApp {
    public static void main(String[] args) {
        SpringApplication.run(UserApp.class, args);
    }
}