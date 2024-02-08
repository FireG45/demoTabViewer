package ru.fireg45.demotabviewer.minio.config;

import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import io.minio.messages.Bucket;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Component
@Configuration
public class MinioClientConfig {

    private final StorageProperty storageProperty;

    @Getter
    private static MinioClient minioClient;

    @Autowired
    public MinioClientConfig(StorageProperty storageProperty) {
        this.storageProperty = storageProperty;
    }

    @SneakyThrows(Exception.class)
    public static boolean bucketExists(String bucketName) {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    @SneakyThrows(Exception.class)
    public static List<Bucket> getAllBuckets() {
        return minioClient.listBuckets();
    }

    @PostConstruct
    public void init() {
        try {
            minioClient = MinioClient.builder()
                    .endpoint(storageProperty.getUrl())
                    .credentials(storageProperty.getAccessKey(), storageProperty.getSecretKey())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Initiating Minio Configuration Anomalous: [{}}", e. fillInStackTrace());
        }
    }

}