package ru.fireg45.demotabviewer.util.tabs;

import org.herac.tuxguitar.graphics.control.TGFactoryImpl;
import org.herac.tuxguitar.io.base.TGFileFormatException;
import org.herac.tuxguitar.io.gtp.GP1InputStream;
import org.herac.tuxguitar.io.gtp.*;
import org.herac.tuxguitar.io.gtp.GTPInputStream;
import org.herac.tuxguitar.io.gtp.GTPSettings;
import org.herac.tuxguitar.song.factory.TGFactory;
import org.herac.tuxguitar.song.models.*;
import org.herac.tuxguitar.song.models.effects.TGEffectBend;
import org.springframework.stereotype.Service;
import ru.fireg45.demotabviewer.util.tabs.dto.BeatDTO;
import ru.fireg45.demotabviewer.util.tabs.dto.MeasureDTO;
import ru.fireg45.demotabviewer.util.tabs.dto.NoteDTO;
import ru.fireg45.demotabviewer.util.tabs.dto.TabDTO;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Service
public class TabReaderImpl implements TabReader {
    @Override
    public TGSong readSong(String filename) throws IOException, TGFileFormatException {
        GTPInputStream gtpInputStream;
        String extension = filename.substring(filename.lastIndexOf('.'));
        System.out.println(extension);
        switch (extension) {
            case ".gp" -> gtpInputStream = new GP1InputStream(new GTPSettings());
            case ".gp2" -> gtpInputStream = new GP2InputStream(new GTPSettings());
            case ".gp3" -> gtpInputStream = new GP3InputStream(new GTPSettings());
            case ".gp4" -> gtpInputStream = new GP4InputStream(new GTPSettings());
            default -> gtpInputStream = new GP5InputStream(new GTPSettings());
        }
        TGFactory factory = new TGFactoryImpl();
        gtpInputStream.init(factory);
        gtpInputStream.setStream(new FileInputStream(filename));
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
    public String readVoice(TGVoice voice) {
        return null;
    }

    @Override
    public String readEffect(TGNote note) {
        return null;
    }

    @Override
    public NoteDTO readBeat(TGBeat beat) {
        return null;
    }

    public String readDuration(TGDuration duration) {
        List<Integer> thDurations = List.of(
                TGDuration.WHOLE, TGDuration.HALF, TGDuration.QUARTER, TGDuration.EIGHTH, TGDuration.SIXTEENTH,
                        TGDuration.THIRTY_SECOND, TGDuration.SIXTY_FOURTH, (int)TGDuration.QUARTER_TIME);
        List<String> vexDurations = List.of( "w", "h", "q", "8", "16", "32", "64", "128");
        return vexDurations.get(thDurations.indexOf(duration.getValue()));
    }

    public List<String> readEffects(TGNoteEffect effect) {
        List<String> effects = new ArrayList<>();
        if (!effect.hasAnyEffect()) return effects;
        else {
            if (effect.isVibrato()) {
                effects.add("v");
            }
            if (effect.isBend()) {
                TGEffectBend bend = effect.getBend();
                List<TGEffectBend.BendPoint> points = bend.getPoints();
                Collections.reverse(points);
                TGEffectBend.BendPoint maxPoint = Collections.max(points,
                        Comparator.comparingInt(TGEffectBend.BendPoint::getValue));
                String[] bendValues = new String[] { "1/2", "Full"};
                int maxVal = maxPoint.getValue() % 2;
                if (maxVal > 1) {
                    if (maxPoint == points.get(0)) {
                        effects.add("b " + "UP:" + bendValues[maxVal / 2 - 1]);
                    } else {
                        effects.add("b " + "UP:" + bendValues[maxPoint.getValue() / 2 - 1] + " "
                                + "DOWN:" + bendValues[maxPoint.getValue() / 2 - 1]);
                    }
                }
            }
            if (effect.isPalmMute()) {
                effects.add("p");
            }
        }
        return effects;
    }

    @Override
    public MeasureDTO readMeasure(TGMeasure measure) {
        List<TGBeat> beats = measure.getBeats();
        List<BeatDTO> beatDTOS = new ArrayList<>();
        TGTempo tempo = measure.getTempo();
        TGTimeSignature timeSignature = measure.getTimeSignature();
        for (TGBeat beat : beats) {
            TGVoice voice = beat.getVoice(0);
            List<NoteDTO> notes = new ArrayList<>();
            List<String> effects = new ArrayList<>();
            boolean palmMute = false;
            for (TGNote note : voice.getNotes()) {
                TGNoteEffect effect =  note.getEffect();

                notes.add(new NoteDTO(String.valueOf(note.getString()), effect.isDeadNote() ? "x" :  String.valueOf(note.getValue())));
                effects.addAll(readEffects(effect));
                palmMute |= effect.isPalmMute();
            }
            String duration = readDuration(voice.getDuration());
            beatDTOS.add(new BeatDTO(duration, palmMute, notes, effects));
        }
        return new MeasureDTO(tempo.getValue(),
                timeSignature.getNumerator() + "/" + timeSignature.getDenominator().getValue(), beatDTOS);
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

        tabDTO.stringCount = song.getTrack(track).stringCount();
        tabDTO.trackNames = new String[song.countTracks()];
        tabDTO.tunings = new String[song.countTracks()];
        List<TGTrack> tracks = song.getTrackList().stream().toList();
        for (int j = 0; j < tracks.size(); j++) {
            tabDTO.trackNames[j] = tracks.get(j).getName();
            tabDTO.tunings[j] = tracks.get(j).getTuning();
        }

        int mSize = measures.size();
        tabDTO.measures = new MeasureDTO[mSize];
        for (int i = 0; i < mSize; i++) {
            tabDTO.measures[i] = readMeasure(measures.get(i));
        }

        return tabDTO;
    }

    @Override
    public List<String> getTrackNames(Iterator<TGTrack> tracks) {
        return null;
    }
}
