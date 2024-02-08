package ru.fireg45.demotabviewer.minio.config;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "s3")
public class StorageProperty {

    @Value("${s3.url}")
    private String url;
    @Value("${s3.username}")
    private String accessKey;
    @Value("${s3.password}")
    private String secretKey;
}