package kr.re.etri.did.idmngserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.properties")
public class ServerProperties {
    private String certPath;
    private String urServerUrl;
    private String serverPrivSeed;
}
