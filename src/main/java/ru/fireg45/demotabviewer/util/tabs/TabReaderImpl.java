package ru.fireg45.demotabviewer.util.tabs;

import org.herac.tuxguitar.io.base.TGFileFormatException;
import org.herac.tuxguitar.song.models.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

@Service
public class TabReaderImpl implements TabReader {
    @Override
    public TGSong readSong(String filename) throws IOException, TGFileFormatException {
        return null;
    }

    @Override
    public List<TGMeasure> getMeasuresList(TGSong song, int track) {
        return null;
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
    public BeatDTO readBeat(TGBeat beat) {
        TGVoice voice = beat.getVoice(0);
        for (int i = 0; i < voice.countNotes(); i++) {

        }
        return null;
    }

    @Override
    public BeatDTO[] readMeasure(TGMeasure measure) {
        return null;
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
        for (int l = 0; l < measures.size(); l++) {
        }
        return null;
    }

    @Override
    public List<String> getTrackNames(Iterator<TGTrack> tracks) {
        return null;
    }
}
