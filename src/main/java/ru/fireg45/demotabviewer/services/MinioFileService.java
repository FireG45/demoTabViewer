package ru.fireg45.demotabviewer.services;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.fireg45.demotabviewer.minio.MinioUtil;
import ru.fireg45.demotabviewer.model.Tabulature;
import ru.fireg45.demotabviewer.model.User;
import ru.fireg45.demotabviewer.util.DeleteOnExitFileInputStream;

import java.io.*;
import java.util.Date;
import java.util.Optional;

@Primary
@Service
public class MinioFileService implements FileService {

    private final MinioUtil minioUtil;

    @Value("${s3.default-bucket}")
    private String defaultBucket;

    @Autowired
    public MinioFileService(MinioUtil minioUtil) {
        this.minioUtil = minioUtil;
    }

    private String getBucket() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        String bucketName = String.valueOf(userEmail.hashCode());
        if (!minioUtil.bucketExists(bucketName)) minioUtil.createBucketName(bucketName);
        return bucketName;
    }

    @Override
    public String upload(MultipartFile file) throws Exception {

        MinioClient minioClient = minioUtil.getMinioClient();
        String fileName = file.getName().hashCode() + new Date().hashCode() + file.getOriginalFilename();
        if (minioClient != null && minioUtil.minioUpload(file, fileName, getBucket())) {
            return fileName;
        }

        return null;
    }

    @Override
    public String upload(InputStream inputStream, String filename) throws Exception {
        MinioClient minioClient = minioUtil.getMinioClient();
        String fileName = filename.hashCode() + new Date().hashCode() + filename;
        if (minioClient != null && minioUtil.minioUpload(inputStream, fileName, getBucket())) {
            return fileName;
        }

        return null;
    }

    @Override
    public void delete(String filepath) {
        MinioClient minioClient = minioUtil.getMinioClient();
        if (minioClient != null) {
            minioUtil.deleteFile(getBucket(),filepath);
        }
    }

    @Override
    public InputStream download(Tabulature tabulature) throws IOException {
        MinioClient minioClient = minioUtil.getMinioClient();
        if (minioClient == null) {
            return null;
        }

        InputStream stream = minioUtil.downloadFile(
                String.valueOf(tabulature.getUser().getEmail().hashCode()),
                tabulature.getFilepath()
        );

        byte[] buffer = stream.readAllBytes();

        File targetFile = new File("/tmp/t_" + System.currentTimeMillis() + tabulature.getFilepath());
        targetFile.deleteOnExit();
        try (OutputStream outStream = new FileOutputStream(targetFile)) {
            outStream.write(buffer);
        }

        return new DeleteOnExitFileInputStream(targetFile);
    }
}
