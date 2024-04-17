package ru.fireg45.demotabviewer.services;

import io.minio.MinioClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.fireg45.demotabviewer.minio.MinioUtil;
import ru.fireg45.demotabviewer.model.Tabulature;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MinioFileServiceTest {

    @Mock
    private MinioUtil minioUtil;

    @InjectMocks
    private MinioFileService minioFileService;

    private final MinioClient minioClient = MinioClient.builder().endpoint("http://localhost:9000").build();


    @Test
    void uploadMultipartFile_Success() throws Exception {
        // Arrange
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt", "text/plain", "Test data".getBytes());

        String filename =
                multipartFile.getName().hashCode() + new Date().hashCode() + multipartFile.getOriginalFilename();
        // Mocking MinioUtil behavior
        doReturn(true).when(minioUtil).minioUpload(eq(multipartFile), anyString(), eq(null));
        doReturn(minioClient).when(minioUtil).getMinioClient();

        // Act
        String result = minioFileService.upload(multipartFile);

        // Assert
        assertNotNull(result);
        verify(minioUtil, times(1)).minioUpload(eq(multipartFile), anyString(), eq(null));
    }

    @Test
    void uploadInputStream_Success() throws Exception {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("Test data".getBytes());

        // Mocking MinioUtil behavior
        doReturn(true).when(minioUtil).minioUpload(eq(inputStream), anyString(), eq(null));
        doReturn(minioClient).when(minioUtil).getMinioClient();


        // Act
        String result = minioFileService.upload(inputStream, "test.txt");

        // Assert
        assertNotNull(result);
        verify(minioUtil, times(1)).minioUpload(eq(inputStream), anyString(), eq(null));
    }

    @Test
    void delete_Success() {
        String filename = "test.txt";
        doNothing().when(minioUtil).deleteFile(eq(null), eq(filename));
        doReturn(minioClient).when(minioUtil).getMinioClient();
        minioFileService.delete(filename);
        verify(minioUtil, times(1)).deleteFile(eq(null), eq(filename));
    }


    @Test
    void download_Success() throws IOException {
        // Arrange
        Tabulature tabulature = new Tabulature();
        tabulature.setFilepath("test.txt");
        InputStream inputStream = new ByteArrayInputStream("Test data".getBytes());

        doReturn(minioClient).when(minioUtil).getMinioClient();
        doReturn(inputStream).when(minioUtil).downloadFile(null, tabulature.getFilepath());

        // Act
        InputStream result = minioFileService.download(tabulature);

        // Assert
        assertNotNull(result);
        // Additional assertions can be added based on the behavior of the download method
    }
}
