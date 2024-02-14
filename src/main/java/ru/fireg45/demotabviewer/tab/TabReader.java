package ru.fireg45.demotabviewer.tab;

import org.herac.tuxguitar.io.base.TGFileFormatException;

import org.herac.tuxguitar.song.models.*;
import ru.fireg45.demotabviewer.tab.dto.BeatDTO;
import ru.fireg45.demotabviewer.tab.dto.MeasureDTO;
import ru.fireg45.demotabviewer.tab.dto.TabDTO;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface TabReader {
    TGSong readSong(String filename, InputStream stream) throws IOException, TGFileFormatException;

    List<TGMeasure> getMeasuresList(TGSong song, int track);

    List<String> readEffects(TGNoteEffect effect, int fret);

    BeatDTO readBeat(TGBeat beat, List<String> slidesAndTies, List<Integer> subList,
                     List<List<Integer>> pmIndexes, List<List<Integer>> lrIndexes, List<Integer> lrsubList,
                     int i);

    MeasureDTO readMeasure(TGMeasure measure);

    TabDTO read(int track, String filename, InputStream stream) throws TGFileFormatException, IOException;
}
