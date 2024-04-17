package ru.fireg45.demotabviewer.services;

import org.herac.tuxguitar.song.factory.TGFactory;
import org.herac.tuxguitar.song.models.TGSong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import ru.fireg45.demotabviewer.model.Tabulature;
import ru.fireg45.demotabviewer.tab.TabReader;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MidiServiceTest {

    @Mock
    private TabReader tabReader;

    @Mock
    private FileService fileService;

    @InjectMocks
    private MidiService midiService;

    private final TGFactory factory = new TGFactory();

    @BeforeEach
    void setUp() {
    }

    @Test
    void convertToMidi_SuccessfullyConverts() throws IOException {
        // Arrange
        Tabulature tabulature = new Tabulature("Title", "Author", "filepath", null, 0);
        TGSong mockedSong = factory.newSong();
        when(tabReader.readSong(anyString(), any())).thenReturn(mockedSong);
        InputStream mockedStream = mock(InputStream.class);
        when(fileService.download(any())).thenReturn(mockedStream);

        // Act
        Resource resource = midiService.convertToMidi(tabulature);

        // Assert
        assertNotNull(resource);
        assertTrue(resource.exists());
        assertTrue(resource.isReadable());
    }

    @Test
    void convertToMidi_FileNotFoundExceptionThrown() throws IOException {
        // Arrange
        Tabulature tabulature = new Tabulature("Title", "Author", "example/filepath", null, 0);
        // Act & Assert
        assertThrows(FileNotFoundException.class, () -> midiService.convertToMidi(tabulature));
    }
}
