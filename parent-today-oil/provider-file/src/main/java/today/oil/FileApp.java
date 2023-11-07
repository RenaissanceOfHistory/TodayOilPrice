package today.oil;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @create: 2023/11/4
 * @Description:
 * @FileName: FileApp
 */
@EnableDiscoveryClient
@SpringBootApplication
public class FileApp {
    public static void main(String[] args) {
        SpringApplication.run(FileApp.class, args);
    }
}