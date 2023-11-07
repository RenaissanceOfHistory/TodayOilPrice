package today.oil.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @create: 2023/10/31
 * @Description:
 * @FileName: WhiteListConfig
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "user")
public class WhiteListConfig {
    private List<String> whiteList;
}
