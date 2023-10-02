package ru.fireg45.demotabviewer.util.tabs;

import org.herac.tuxguitar.graphics.control.TGFactoryImpl;
import org.herac.tuxguitar.io.base.TGFileFormat;
import org.herac.tuxguitar.io.base.TGFileFormatException;

import org.herac.tuxguitar.io.base.TGSongReader;
import org.herac.tuxguitar.io.base.TGSongReaderHandle;
import org.herac.tuxguitar.io.gtp.*;
import org.herac.tuxguitar.io.tg.TGSongReaderImpl;
import org.herac.tuxguitar.song.factory.TGFactory;
import org.herac.tuxguitar.song.models.*;
import org.herac.tuxguitar.song.models.effects.TGEffectBend;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import static org.herac.tuxguitar.song.models.TGMeasure.CLEF_BASS;

public interface TabReader {
    TGSong readSong(String filename) throws IOException, TGFileFormatException;

    List<TGMeasure> getMeasuresList(TGSong song, int track);

    String readVoice(TGVoice voice);

    String readEffect(TGNote note);

    BeatDTO readBeat(TGBeat beat);

    BeatDTO[] readMeasure(TGMeasure measure);

    StringBuilder initStringBuilder(TGSong song, int track);

    TabDTO read(int track, String filename) throws TGFileFormatException, IOException;

    List<String> getTrackNames(Iterator<TGTrack> tracks);
}
