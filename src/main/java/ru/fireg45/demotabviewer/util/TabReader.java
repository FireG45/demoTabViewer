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

    public static List<String> read(int track) throws IOException, TGFileFormatException {
        List<String> tabs = new ArrayList<>();
        GTPInputStream inputStream = new GP5InputStream(new GTPSettings());
        TGFactory factory = new TGFactory();
        String testTabFilepath = "/home/fireg/IdeaProjects/demoTabViewer/src/main/resources/static/test/tab.gp5";
        inputStream.init(factory, new FileInputStream(testTabFilepath));
        TGSong song = inputStream.readSong();
        Iterator<TGMeasure> measureIterator = song.getTrack(track).getMeasures();
        List<TGMeasure> measures = new ArrayList<>();
        int i = 0;
        for (TGMeasure measure = measureIterator.next(); measureIterator.hasNext(); measure = measureIterator.next()) {
            measures.add(measure);
        }
        StringBuilder beatsString = new StringBuilder();
        beatsString.append("options scale=1 width = 1910\n");
        beatsString.append("\ntabstave notation=true time=4/4 ");
        if (song.getTrack(track).getMeasure(0).getClef() == CLEF_BASS) {
            beatsString.append("clef=bass strings=4\n");
        }
        if (song.getTrack(track).isPercussionTrack()) {
            beatsString.append("clef=percussion \n");
        }
        beatsString.append("\nnotes ");
        for (int l = 0; l < measures.size(); l++) {
            List<TGBeat> beats = measures.get(l).getBeats();
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
                        beatsString.append("(").append(stringBuilder).append(")").append(" ");
                    } else {
                        beatsString.append(stringBuilder).append(" ");
                    }
                }
            }
            beatsString.append(" | ");
            if (l % 4 ==0) {
                tabs.add(beatsString.toString());
                beatsString = new StringBuilder();
                beatsString.append("options scale=1 width = 1910\n");
                beatsString.append("\ntabstave notation=true time=4/4 ");
                if (song.getTrack(track).getMeasure(0).getClef() == CLEF_BASS) {
                    beatsString.append("clef=bass strings=4\n");
                }
                if (song.getTrack(track).isPercussionTrack()) {
                    beatsString.append("clef=percussion \n");
                }
                beatsString.append("\nnotes ");
            }
        }
        return tabs;
    }
}
