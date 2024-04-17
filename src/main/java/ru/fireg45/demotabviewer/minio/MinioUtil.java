package ru.fireg45.demotabviewer.minio;

import io.minio.*;

import io.minio.errors.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import ru.fireg45.demotabviewer.minio.config.MinioClientConfig;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Component
public class MinioUtil {

    public MinioClient getMinioClient() {
        return MinioClientConfig.getMinioClient();
    }
    
    public Boolean minioUpload(MultipartFile file, String fileName, String bucketName) {
        try {
            MinioClient minioClient = getMinioClient();

            if (fileName == null && file != null && file.getOriginalFilename() != null) {
                fileName = file.getOriginalFilename().replaceAll(" ", "_");
            }

            InputStream inputStream;
            if (file != null) {
                inputStream = file.getInputStream();

                PutObjectArgs objectArgs = PutObjectArgs.builder().bucket(bucketName).object(fileName)
                        .stream(inputStream, file.getSize(), -1).contentType(file.getContentType()).build();

                minioClient.putObject(objectArgs);
                return true;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
        return false;
    }

    public Boolean minioUpload(InputStream inputStream, String fileName, String bucketName) {
        try {
            MinioClient minioClient = getMinioClient();

            if (inputStream != null) {

                PutObjectArgs objectArgs = PutObjectArgs.builder().bucket(bucketName).object(fileName)
                        .stream(inputStream, -1, 5242880).contentType("audio/x-gtp").build();

                minioClient.putObject(objectArgs);
                return true;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
        return false;
    }

    public boolean bucketExists(String bucketName) {
        try {
            return MinioClientConfig.bucketExists(bucketName);
        } catch (Exception e) {
            // e.printStackTrace();
            log.error(e.getMessage());
            return false;
        }
    }

    public InputStream getFileInputStream(String fileName, String bucketName) {
        try {
            MinioClient minioClient = getMinioClient();
            return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(fileName).build());
        } catch (Exception e) {
            // e.printStackTrace();
            log.error(e.getMessage());
        }
        return null;
    }

    public void createBucketName(String bucketName) {
        try {
            if (bucketName.isBlank()) {
                return;
            }
            MinioClient minioClient = getMinioClient();
            if (MinioClientConfig.bucketExists(bucketName)) {
                log.info("Bucket {} already exists.", bucketName);
            } else {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public InputStream downloadFile(String bucketName, String filename) {
        InputStream stream;
        MinioClient minioClient = getMinioClient();
        try {
            stream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(filename)
                    .build());
        } catch (Exception e) {
            log.error("Happened error when get list objects from minio: ", e);
            return null;
        }

        return stream;
    }

    public void deleteBucketName(String bucketName) {
        try {
            if (bucketName.isBlank()) {
                return;
            }
            MinioClient minioClient = getMinioClient();
            boolean isExist = MinioClientConfig.bucketExists(bucketName);
            if (isExist) {
                minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


    public void deleteBucketFile(String bucketName) {
        try {
            if (bucketName.isBlank()) {
                return;
            }
            MinioClient minioClient = getMinioClient();
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (isExist) {
                minioClient.deleteBucketEncryption(DeleteBucketEncryptionArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public String getPreviewFileUrl(String bucketName, String fileName) {
        try {
            MinioClient minioClient = getMinioClient();
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().bucket(bucketName).object(fileName).build());
        } catch (Exception e) {
            return "";
        }
    }

    @SneakyThrows
    public void deleteFile(String defaultBucket, String filepath) {
        MinioClient minioClient = getMinioClient();
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(defaultBucket).object(filepath).build());
    }
}