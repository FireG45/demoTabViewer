package ru.fireg45.demotabviewer.util.tabs;

import org.herac.tuxguitar.graphics.control.TGFactoryImpl;
import org.herac.tuxguitar.io.base.TGFileFormatException;
import org.herac.tuxguitar.io.gtp.GP5InputStream;
import org.herac.tuxguitar.io.gtp.GTPInputStream;
import org.herac.tuxguitar.io.gtp.GTPSettings;
import org.herac.tuxguitar.song.factory.TGFactory;
import org.herac.tuxguitar.song.models.*;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class TabReaderImpl implements TabReader {
    @Override
    public TGSong readSong(String filename) throws IOException, TGFileFormatException {
        GTPInputStream gtpInputStream = new GP5InputStream(new GTPSettings());
        TGFactory factory = new TGFactoryImpl();
        gtpInputStream.init(factory);
        gtpInputStream.setStream(new FileInputStream(filename));
        return gtpInputStream.readSong();
    }

    @Override
    public List<TGMeasure> getMeasuresList(TGSong song, int track) {
        Iterator<TGMeasure> measureIterator = song.getTrack(track).getMeasures();
        List<TGMeasure> measures = new ArrayList<>();
        measureIterator.forEachRemaining(measures::add);
        return measures;
    }

    @Override
    public String readVoice(TGVoice voice) {
        return null;
    }

    @Override
    public String readEffect(TGNote note) {
        return null;
    }

    @Override
    public NoteDTO readBeat(TGBeat beat) {
        TGVoice voice = beat.getVoice(0);
        for (int i = 0; i < voice.countNotes(); i++) {

        }
        return null;
    }

    @Override
    public MeasureDTO readMeasure(TGMeasure measure) {
        List<TGBeat> beats = measure.getBeats();
        int noteCount = 0;
        List<NoteDTO> notes = new ArrayList<>();
        for (TGBeat beat : beats) {
            TGVoice voice = beat.getVoice(0);
            for (TGNote note : voice.getNotes()) {
                notes.add(new NoteDTO(note.getString(), note.getValue()));
            }
        }

        return new MeasureDTO(notes);
    }

    @Override
    public StringBuilder initStringBuilder(TGSong song, int track) {
        return null;
    }

    @Override
    public TabDTO read(int track, String filename) throws TGFileFormatException, IOException {
        TGSong song = readSong(filename);
        List<TGMeasure> measures = getMeasuresList(song, track);
        TabDTO tabDTO = new TabDTO();
        tabDTO.measures = new MeasureDTO[measures.size()];
        for (int i = 0; i < measures.size(); i++) {
            tabDTO.measures[i] = readMeasure(measures.get(i));
        }
        return tabDTO;
    }

    @Override
    public List<String> getTrackNames(Iterator<TGTrack> tracks) {
        return null;
    }
}
