package ru.fireg45.demotabviewer.util.tabs;

import org.herac.tuxguitar.io.base.TGFileFormatException;

import org.herac.tuxguitar.song.models.*;
import ru.fireg45.demotabviewer.util.tabs.dto.BeatDTO;
import ru.fireg45.demotabviewer.util.tabs.dto.MeasureDTO;
import ru.fireg45.demotabviewer.util.tabs.dto.NoteDTO;
import ru.fireg45.demotabviewer.util.tabs.dto.TabDTO;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public interface TabReader {
    TGSong readSong(String filename) throws IOException, TGFileFormatException;

    List<TGMeasure> getMeasuresList(TGSong song, int track);

    List<String> readEffects(TGNoteEffect effect, int fret);

    BeatDTO readBeat(TGBeat beat, List<String> slidesAndTies, List<Integer> subList,
                     List<List<Integer>> pmIndexes, List<List<Integer>> lrIndexes, List<Integer> lrsubList,
                     int i);

    MeasureDTO readMeasure(TGMeasure measure);

    TabDTO read(int track, String filename) throws TGFileFormatException, IOException;
}
