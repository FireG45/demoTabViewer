package ru.fireg45.demotabviewer.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.fireg45.demotabviewer.model.Tabulature;
import ru.fireg45.demotabviewer.util.exceptions.FileUploadException;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

@Service
public class FilesystemFileService implements FileService {

    @Value("${upload.defaultFilePath}")
    private String path;

    @Override
    public String upload(MultipartFile file) throws Exception {
        String name = generateFilename(file) + file.getOriginalFilename();

        if (!file.isEmpty()) {
            byte[] bytes = file.getBytes();
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(path + name));
            stream.write(bytes);
            stream.close();
        } else {
            throw new FileUploadException("Error! File is Empty");
        }

        return path + name;
    }

    private String generateFilename(MultipartFile file) {
        return String.valueOf(file.hashCode() + System.currentTimeMillis());
    }

    @Override
    public MultipartFile download(Tabulature tabulature) {
        return null;
    }
}
