package ru.fireg45.demotabviewer.tab;

import org.herac.tuxguitar.graphics.control.TGFactoryImpl;
import org.herac.tuxguitar.io.base.TGFileFormatException;

import org.herac.tuxguitar.io.gtp.*;
import org.herac.tuxguitar.song.factory.TGFactory;
import org.herac.tuxguitar.song.models.*;
import org.herac.tuxguitar.song.models.effects.TGEffectBend;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

@Deprecated
public class OldTabReader {

    public static TGSong readSong(String filename) throws IOException, TGFileFormatException {
        GTPInputStream gtpInputStream = new GP5InputStream(new GTPSettings());
        TGFactory factory = new TGFactoryImpl();
        gtpInputStream.init(factory);
        gtpInputStream.setStream(new FileInputStream(filename));
        return gtpInputStream.readSong();
    }

    public static List<TGMeasure> getMeasuresList(TGSong song, int track) {
        Iterator<TGMeasure> measureIterator = song.getTrack(track).getMeasures();
        List<TGMeasure> measures = new ArrayList<>();
        measureIterator.forEachRemaining(measures::add);
        return measures;
    }

    private static String readVoice(TGVoice voice) {
        StringBuilder voiceString = new StringBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        for (int k = 0; k < voice.countNotes(); k++) {
            if (k > 0) stringBuilder.append(".");
            TGNote note = voice.getNote(k);
            stringBuilder.append(note.getEffect().isDeadNote() ? "X" : note.getValue()).append(readEffect(note))
                    .append("/").append(note.getString());
        }
        if (voice.countNotes() > 1) {
            voiceString.append("(").append(stringBuilder).append(")").append(" ");
        } else {
            voiceString.append(stringBuilder).append(" ");
        }
        return voiceString.toString();
    }

    private static String readEffect(TGNote note) {
        TGNoteEffect effect = note.getEffect();
        StringBuilder string = new StringBuilder();
        if (effect.isBend()) {
            string.append("b");
            List<TGEffectBend.BendPoint> points = effect.getBend().getPoints();
            string.append(points.stream().max(Comparator.comparingInt(TGEffectBend.BendPoint::getPosition)).get().getValue());
        }
        if (effect.isVibrato()) string.append("v");
        return string.toString();
    }

    private static String readBeat(TGBeat beat) {
        if (beat.isRestBeat()) return " ## ";
        StringBuilder beatString = new StringBuilder();
        for (int j = 0; j < beat.countVoices(); j++) {
            beatString.append(readVoice(beat.getVoice(j)));
        }
        return beatString.toString();
    }

    private static String readMeasure(TGMeasure measure) {
        StringBuilder measureString = new StringBuilder();
        List<TGBeat> beats = measure.getBeats();
        for (TGBeat beat : beats) {
            measureString.append(readBeat(beat));
        }
        return measureString.toString();
    }

    private static StringBuilder initStringBuilder(TGSong song, int track) {
        StringBuilder beatsString = new StringBuilder();
        beatsString.append("\ntabstave notation=true time=4/4 ");
        beatsString.append("strings=").append(song.getTrack(track).stringCount()).append("\n");
        beatsString.append("\nnotes ");

        return beatsString;
    }

    public static TGSong read(int track, List<String> tabs, String filename) throws TGFileFormatException, IOException {
        TGSong song = readSong(filename);
        List<TGMeasure> measures = getMeasuresList(song, track);
        StringBuilder beatsString = initStringBuilder(song, track);
        String[] repeatChar = new String[] {"|", "=|:"};
        for (int l = 0; l < measures.size(); l++) {
            beatsString.append(readMeasure(measures.get(l)));

            beatsString.append(" " + "|" + " ");
            if (l != 0 && l % 4 == 0) {
                tabs.add(beatsString.toString());
                beatsString = initStringBuilder(song, track);
            }
        }
        return song;
    }

    public static List<String> getTrackNames(Iterator<TGTrack> tracks) {
        List<String> trackNames = new ArrayList<>();
        tracks.forEachRemaining(tgTrack -> trackNames.add(tgTrack.getName()));
        return trackNames;
    }

}