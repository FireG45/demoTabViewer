package ru.fireg45.demotabviewer.tab;

import org.herac.tuxguitar.graphics.control.TGFactoryImpl;
import org.herac.tuxguitar.io.base.TGFileFormatException;
import org.herac.tuxguitar.io.gtp.GP1InputStream;
import org.herac.tuxguitar.io.gtp.*;
import org.herac.tuxguitar.io.gtp.GTPInputStream;
import org.herac.tuxguitar.io.gtp.GTPSettings;
import org.herac.tuxguitar.song.factory.TGFactory;
import org.herac.tuxguitar.song.models.*;
import org.herac.tuxguitar.song.models.effects.TGEffectBend;
import org.herac.tuxguitar.song.models.effects.TGEffectHarmonic;
import org.springframework.stereotype.Service;
import ru.fireg45.demotabviewer.tab.dto.BeatDTO;
import ru.fireg45.demotabviewer.tab.dto.MeasureDTO;
import ru.fireg45.demotabviewer.tab.dto.NoteDTO;
import ru.fireg45.demotabviewer.tab.dto.TabDTO;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
    public class TabReaderImpl implements TabReader {
    @Override
    public TGSong readSong(String filename, InputStream stream) throws TGFileFormatException {
        GTPInputStream gtpInputStream;
        String extension = filename.substring(filename.lastIndexOf('.'));
        switch (extension) {
            case ".gp2" -> gtpInputStream = new GP2InputStream(new GTPSettings());
            case ".gp3" -> gtpInputStream = new GP3InputStream(new GTPSettings());
            case ".gp4" -> gtpInputStream = new GP4InputStream(new GTPSettings());
            case ".gp5" -> gtpInputStream = new GP5InputStream(new GTPSettings());
            default -> gtpInputStream = new GP1InputStream(new GTPSettings());
        }
        TGFactory factory = new TGFactoryImpl();
        gtpInputStream.init(factory);
        gtpInputStream.setStream(stream);
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
    public BeatDTO readBeat(TGBeat beat, List<String> slidesAndTies, List<Integer> subList,
                            List<List<Integer>> pmIndexes, List<List<Integer>> lrIndexes, List<Integer> lrsubList,
                            int i) {
        TGVoice voice = beat.getVoice(0);

        if (beat.isRestBeat()) {
            return new BeatDTO(readDuration(voice.getDuration()),
                    false, false, List.of(), List.of());
        }

        List<NoteDTO> notes = new ArrayList<>();
        List<String> effects = new ArrayList<>();
        boolean palmMute = false, ghostNote = false, letRing  = false;
        int noteInd = 0;
        for (TGNote note : voice.getNotes()) {
            TGNoteEffect effect =  note.getEffect();
            if (effect.isSlide() || effect.isHammer() || effect.isPullOff()) {
                slidesAndTies.add(readSlideTie(i, effect, noteInd));
            }
            effects.addAll(readEffects(effect, note.getValue()));
            palmMute |= effect.isPalmMute();
            letRing |= effect.isLetRing();
            ghostNote |= effect.isGhostNote();
            noteInd++;

            notes.add(new NoteDTO(String.valueOf(note.getString()),
                    effect.isDeadNote() ? "x" : String.valueOf(note.getValue())));
        }
        String duration = readDuration(voice.getDuration());
        if (palmMute) {
            subList.add(i);
        } else if (!subList.isEmpty()) {
            pmIndexes.add(subList);
        }
        if (letRing) {
            lrsubList.add(i);
        } else if (!lrsubList.isEmpty()) {
            lrIndexes.add(lrsubList);
        }
        return new BeatDTO(duration, palmMute, ghostNote, notes, effects);
    }

    private static String readSlideTie(int i, TGNoteEffect effect, int noteInd) {
        String type = "";
        if (effect.isHammer()  || effect.isPullOff()) type = "H";
        else if (effect.isSlide()) type = "S";
        return i + "|" + (i + 1) + "|" + type + "|" + noteInd;
    }

    public String readDuration(TGDuration duration) {
        List<Integer> thDurations = List.of(
                TGDuration.WHOLE, TGDuration.HALF, TGDuration.QUARTER, TGDuration.EIGHTH, TGDuration.SIXTEENTH,
                        TGDuration.THIRTY_SECOND, TGDuration.SIXTY_FOURTH, (int)TGDuration.QUARTER_TIME);
        List<String> vexDurations = List.of( "w", "h", "q", "8", "16", "32", "64", "128");
        String dotted = duration.isDotted() ? "." : "";
        String doubleDotted = duration.isDoubleDotted() ? "." : "";
        return dotted + doubleDotted + vexDurations.get(thDurations.indexOf(duration.getValue()));
    }

    @Override
    public List<String> readEffects(TGNoteEffect effect, int fret) {
        List<String> effects = new ArrayList<>();
        if (!effect.hasAnyEffect()) {
            return effects;
        } else {
            if (effect.isVibrato()) {
                effects.add("v");
            }
            if (effect.isBend()) {
                TGEffectBend bend = effect.getBend();
                 String[] bendValues =
                         new String[] { "1/4", "1/4", "1/2", "3/4", "full", "1¼", "1½", "1¾", "2", "2¼",
                                 "2½", "2¾", "3", "3¼", "3½" };
                String value = bendValues[bend.getBendValue() / 25];

                switch (bend.getBendType()) {
                    case 1 -> effects.add("b " + "UP:" + value);
                    case 2 -> effects.add("b " + "UP:" + value + " " + "DOWN:" + value);
                }
            }
            boolean[] effectsFlags = { effect.isPalmMute(), effect.isTapping(), effect.isSlapping(),
                    effect.isPopping(), effect.isHarmonic(), effect.isAccentuatedNote(), effect.isLetRing(),
                    effect.isHeavyAccentuatedNote() };
            String[] effectsSigns = { "p",  "t", "s", "P", readHarmonic(effect, fret), ">", "L", "^" };
            int effectsSize = effectsFlags.length;
            for (int i = 0; i < effectsSize; i++) {
                if (effectsFlags[i]) effects.add(effectsSigns[i]);
            }
        }
        return effects;
    }

    private static String readHarmonic(TGNoteEffect effect, int fret) {
        TGEffectHarmonic harmonic = effect.getHarmonic();
        StringBuilder effectString = new StringBuilder("h|");
        if (harmonic == null) return "";
        if (harmonic.isNatural() || harmonic.isArtificial())  effectString.append("H").append("|").append(fret);
        if (harmonic.isTapped()) effectString.append(TGEffectHarmonic.KEY_TAPPED);
        if (harmonic.isPinch()) effectString.append(TGEffectHarmonic.KEY_PINCH);
        if (harmonic.isSemi()) effectString.append(TGEffectHarmonic.KEY_SEMI);
        return effectString.toString();
    }

    @Override
    public MeasureDTO readMeasure(TGMeasure measure) {
        List<TGBeat> beats = measure.getBeats();
        List<BeatDTO> beatDTOS = new ArrayList<>();
        TGTempo tempo = measure.getTempo();
        TGTimeSignature timeSignature = measure.getTimeSignature();
        List<List<Integer>> pmIndexes = new ArrayList<>();
        List<List<Integer>> lrIndexes = new ArrayList<>();
        List<String> slidesAndTies = new ArrayList<>();
        List<Integer> subList = new ArrayList<>();
        List<Integer> lrsubList = new ArrayList<>();
        int i = 0;
        for (TGBeat beat : beats) {
            beatDTOS.add(readBeat(beat, slidesAndTies, subList, pmIndexes, lrIndexes, lrsubList, i));
            i++;
        }
        if (!subList.isEmpty()) {
            pmIndexes.add(subList);
        }
        if (!lrsubList.isEmpty()) {
            lrIndexes.add(lrsubList);
        }
        return new MeasureDTO(tempo.getValue(),timeSignature.getNumerator() + "/"
                + timeSignature.getDenominator().getValue(), beatDTOS, pmIndexes, slidesAndTies, lrIndexes);
    }

    @Override
    public TabDTO read(int track, String filename, InputStream stream) throws TGFileFormatException, IOException {
        TGSong song = readSong(filename, stream);
        List<TGMeasure> measures = getMeasuresList(song, track);
        TabDTO tabDTO = new TabDTO();

        tabDTO.stringCount = song.getTrack(track).stringCount();
        tabDTO.trackNames = new String[song.countTracks()];
        tabDTO.tunings = new String[song.countTracks()];
        tabDTO.trackPrograms = new int[song.countTracks()];
        List<TGTrack> tracks = song.getTrackList().stream().toList();
        for (int j = 0; j < tracks.size(); j++) {
            TGTrack tgTrack = tracks.get(j);
            TGChannel tgChannel = song.getChannel(tgTrack.getChannelId() - 1);
            tabDTO.trackNames[j] = tgTrack.getName() + "  |  (" + tgChannel.getName() + ")";
            tabDTO.tunings[j] = tgTrack.getTuning();
            tabDTO.trackPrograms[j] = tgChannel.getProgram();
        }

        int mSize = measures.size();
        tabDTO.measures = new MeasureDTO[mSize];
        for (int i = 0; i < mSize; i++) {
            tabDTO.measures[i] = readMeasure(measures.get(i));
        }

        return tabDTO;
    }

}
