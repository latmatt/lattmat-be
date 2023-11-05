package solution.com.lattmat.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "lattmatapp")
@Getter
@Setter
@ToString
public class AppConfig {
    private String redirectUrl;
}

