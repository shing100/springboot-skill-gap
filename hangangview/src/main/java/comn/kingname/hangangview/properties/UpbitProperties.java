package comn.kingname.hangangview.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "upbit")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class UpbitProperties {
    private String accessKey;
    private String secretKey;
    private String apiUrl;
}
