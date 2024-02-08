package ru.fireg45.demotabviewer.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.fireg45.demotabviewer.model.Tabulature;

import java.io.FileNotFoundException;
import java.io.InputStream;

@Service
public interface FileService {
    public String upload(MultipartFile file) throws Exception ;
    public void delete(String filepath);
    public InputStream download(Tabulature tabulature) throws FileNotFoundException;
}
