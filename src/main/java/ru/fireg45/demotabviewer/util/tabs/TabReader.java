package ru.fireg45.demotabviewer.util.tabs;

import org.herac.tuxguitar.io.base.TGFileFormatException;

import org.herac.tuxguitar.song.models.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public interface TabReader {
    TGSong readSong(String filename) throws IOException, TGFileFormatException;

    List<TGMeasure> getMeasuresList(TGSong song, int track);

    String readVoice(TGVoice voice);

    String readEffect(TGNote note);

    NoteDTO readBeat(TGBeat beat);

    MeasureDTO readMeasure(TGMeasure measure);

    StringBuilder initStringBuilder(TGSong song, int track);

    TabDTO read(int track, String filename) throws TGFileFormatException, IOException;

    List<String> getTrackNames(Iterator<TGTrack> tracks);
}
