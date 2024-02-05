package ru.fireg45.demotabviewer.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.fireg45.demotabviewer.model.Tabulature;
import ru.fireg45.demotabviewer.util.exceptions.FileUploadException;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

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

    @Override
    public void delete(String filepath) {
        Path path = FileSystems.getDefault().getPath(filepath);
        try {
            Files.delete(path);
        } catch (NoSuchFileException x) {
            System.err.format("%s: no such" + " file or directory%n", path);
        } catch (IOException x) {
            System.err.println(x.getLocalizedMessage());
        }
    }

    private String generateFilename(MultipartFile file) {
        return String.valueOf(file.hashCode() + System.currentTimeMillis());
    }

    @Override
    public MultipartFile download(Tabulature tabulature) {
        return null;
    }
}
