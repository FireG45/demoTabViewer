package ru.fireg45.demotabviewer.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.fireg45.demotabviewer.model.Tabulature;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Service
public interface FileService {
    String upload(MultipartFile file) throws Exception;
    String upload(InputStream inputStream, String filename) throws Exception;
    void delete(String filepath);
    InputStream download(Tabulature tabulature) throws IOException;
}
