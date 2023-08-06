package ru.fireg45.demotabviewer.util;

import org.herac.tuxguitar.io.base.TGFileFormatException;
import org.herac.tuxguitar.io.gtp.GP5InputStream;
import org.herac.tuxguitar.io.gtp.GTPInputStream;
import org.herac.tuxguitar.io.gtp.GTPSettings;
import org.herac.tuxguitar.song.factory.TGFactory;
import org.herac.tuxguitar.song.models.*;
import org.herac.tuxguitar.song.models.effects.TGEffectBend;

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
            string.append(points.stream().max((p1, p2) -> p1.getPosition() - p2.getPosition()).get().getValue());
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
        if (song.getTrack(track).getMeasure(0).getClef() == CLEF_BASS) {
            beatsString.append("clef=bass strings=4\n");
        }
        if (song.getTrack(track).isPercussionTrack()) {
            beatsString.append("clef=percussion \n");
        }
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
