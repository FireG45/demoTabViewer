package ru.fireg45.demotabviewer.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.fireg45.demotabviewer.model.Tabulature;

@Service
public interface FileService {
    public String upload(MultipartFile file)  throws Exception ;
    public MultipartFile download(Tabulature tabulature);
}
