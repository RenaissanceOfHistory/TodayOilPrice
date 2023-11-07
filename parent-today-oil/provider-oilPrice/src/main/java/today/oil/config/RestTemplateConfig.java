package today.oil.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/** RestTemplate配置类，调用其它语言服务器
 * @create: 2023/10/31
 * @Description:
 * @FileName: RestTemplateConfig
 */
@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
