package ru.fireg45.demotabviewer.services;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.fireg45.demotabviewer.minio.MinioUtil;
import ru.fireg45.demotabviewer.minio.config.MinioClientConfig;
import ru.fireg45.demotabviewer.model.Tabulature;
import ru.fireg45.demotabviewer.util.DeleteOnExitFileInputStream;

import java.io.*;
import java.nio.file.StandardOpenOption;
import java.util.Date;

@Primary
@Service
public class MinioFileService implements FileService {

    private final MinioUtil minioUtil;

    @Value("${s3.default-bucket}")
    private String defaultBucket;

    @Autowired
    public MinioFileService(MinioUtil minioUtil, MinioClientConfig minioClientConfig) {
        this.minioUtil = minioUtil;
    }

    @Override
    public String upload(MultipartFile file) throws Exception {
        MinioClient minioClient = MinioClientConfig.getMinioClient();
        String fileName = file.getName().hashCode() + new Date().hashCode() + file.getOriginalFilename();
        if (minioClient != null && minioUtil.minioUpload(file, fileName, defaultBucket)) {
            return fileName;
        }

        return null;
    }

    @Override
    public void delete(String filepath) {
        MinioClient minioClient = MinioClientConfig.getMinioClient();
        if (minioClient != null) {
            minioUtil.deleteFile(defaultBucket,filepath);
        }
    }

    @Override
    public InputStream download(Tabulature tabulature) throws IOException {
        MinioClient minioClient = MinioClientConfig.getMinioClient();
        if (minioClient == null) {
            return null;
        }

        InputStream stream = minioUtil.downloadFile(defaultBucket, tabulature.getFilepath());

        byte[] buffer = stream.readAllBytes();

        File targetFile = new File("/tmp/t_" + System.currentTimeMillis() + tabulature.getFilepath());
        targetFile.deleteOnExit();
        try (OutputStream outStream = new FileOutputStream(targetFile)) {
            outStream.write(buffer);
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }

        return new DeleteOnExitFileInputStream(targetFile);
    }
}
