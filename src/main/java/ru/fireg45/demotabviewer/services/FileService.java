package ru.fireg45.demotabviewer.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.fireg45.demotabviewer.model.Tabulature;

@Service
public interface FileService {
    public String upload(MultipartFile file) throws Exception ;
    public void delete(String filepath);
    public MultipartFile download(Tabulature tabulature);
}
