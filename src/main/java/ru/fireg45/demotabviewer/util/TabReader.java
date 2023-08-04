package ru.fireg45.demotabviewer.util;

import org.herac.tuxguitar.io.base.TGFileFormatException;
import org.herac.tuxguitar.io.gtp.GP5InputStream;
import org.herac.tuxguitar.io.gtp.GTPInputStream;
import org.herac.tuxguitar.io.gtp.GTPSettings;
import org.herac.tuxguitar.song.factory.TGFactory;
import org.herac.tuxguitar.song.models.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.herac.tuxguitar.song.models.TGMeasure.CLEF_BASS;

public class TabReader {

    public static TGSong readSong(String filename) throws IOException, TGFileFormatException {
        GTPInputStream inputStream = new GP5InputStream(new GTPSettings());
        inputStream.init(new TGFactory(), new FileInputStream(filename));
        return inputStream.readSong();
    }

    public static List<TGMeasure> getMeasuresList(TGSong song, int track) {
        Iterator<TGMeasure> measureIterator = song.getTrack(track).getMeasures();
        List<TGMeasure> measures = new ArrayList<>();
        measureIterator.forEachRemaining(measures::add);
        return measures;
    }

    private static String readMeasure(TGMeasure measure) {
        StringBuilder measureString = new StringBuilder();
        List<TGBeat> beats = measure.getBeats();
        for (TGBeat beat : beats) {
            for (int j = 0; j < beat.countVoices(); j++) {
                StringBuilder stringBuilder = new StringBuilder();
                int notes = 0;
                for (int k = 0; k < beat.getVoice(j).countNotes(); k++) {
                    if (k > 0) stringBuilder.append(".");
                    TGNote note = beat.getVoice(j).getNote(k);
                    stringBuilder.append(note.getValue()).append("/").append(note.getString());
                    notes++;
                }
                if (notes > 1) {
                    measureString.append("(").append(stringBuilder).append(")").append(" ");
                } else {
                    measureString.append(stringBuilder).append(" ");
                }
            }
        }
        return measureString.toString();
    }

    private static StringBuilder initStringBuilder(TGSong song, int track) {
        StringBuilder beatsString = new StringBuilder();
        beatsString.append("\ntabstave notation=true time=4/4 ");
        if (song.getTrack(track).getMeasure(0).getClef() == CLEF_BASS) {
            beatsString.append("clef=bass strings=4\n");
        }
        if (song.getTrack(track).isPercussionTrack()) {
            beatsString.append("clef=percussion \n");
        }
        beatsString.append("\nnotes ");

        return beatsString;
    }

    public static List<String> read(int track, String filename) throws TGFileFormatException, IOException {
        List<String> tabs = new ArrayList<>();
        TGSong song = readSong(filename);
        List<TGMeasure> measures = getMeasuresList(song, track);
        StringBuilder beatsString = initStringBuilder(song, track);
        for (int l = 0; l < measures.size(); l++) {
            beatsString.append(readMeasure(measures.get(l)));
            beatsString.append(" | ");
            if (l != 0 && l % 4 == 0) {
                tabs.add(beatsString.toString());
                beatsString = initStringBuilder(song, track);
            }
        }
        return tabs;
    }

}
