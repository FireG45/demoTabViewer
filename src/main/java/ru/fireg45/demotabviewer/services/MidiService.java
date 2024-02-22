package ru.fireg45.demotabviewer.services;

import org.herac.tuxguitar.io.base.TGFileFormat;
import org.herac.tuxguitar.io.base.TGSongStreamContext;
import org.herac.tuxguitar.io.base.TGSongWriterHandle;
import org.herac.tuxguitar.io.midi.MidiSongWriter;
import org.herac.tuxguitar.song.managers.TGSongManager;
import org.herac.tuxguitar.song.models.TGSong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import ru.fireg45.demotabviewer.model.Tabulature;
import ru.fireg45.demotabviewer.tab.TabReader;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Path;

import static org.herac.tuxguitar.io.base.TGSongPersistenceHelper.ATTRIBUTE_FORMAT_CODE;
import static org.herac.tuxguitar.io.base.TGSongPersistenceHelper.ATTRIBUTE_MIME_TYPE;

@Service
public class MidiService {

    private final TabReader tabReader;
    private final FileService fileService;

    @Autowired
    public MidiService(TabReader tabReader, FileService fileService) {
        this.tabReader = tabReader;
        this.fileService = fileService;
    }

    public Resource convertToMidi(Tabulature tabulature) throws FileNotFoundException {

        File targetFile = new File("/tmp/t_" + System.currentTimeMillis()
                + tabulature.getFilepath() + ".mid");
        targetFile.deleteOnExit();
        try (OutputStream outStream = new FileOutputStream(targetFile)) {
            TGSong song = tabReader.readSong(tabulature.getFilepath(), fileService.download(tabulature));
            MidiSongWriter midiSongWriter = new MidiSongWriter();

            TGSongManager songManager = new TGSongManager();

            TGSongWriterHandle songWriterHandle = new TGSongWriterHandle();
            songWriterHandle.setFactory(songManager.getFactory());
            songWriterHandle.setSong(song);
            songWriterHandle.setOutputStream(outStream);
            songWriterHandle.setContext(new TGSongStreamContext());

            midiSongWriter.write(songWriterHandle);
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }

        try {
            Path file = targetFile.toPath().normalize();
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new FileNotFoundException("Could not find file");
            }
        }
        catch (MalformedURLException e) {
            throw new FileNotFoundException("Could not download file");
        }
    }

}
