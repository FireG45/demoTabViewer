package ru.fireg45.demotabviewer.tab;

import org.herac.tuxguitar.graphics.control.TGFactoryImpl;
import org.herac.tuxguitar.io.base.TGSongWriter;
import org.herac.tuxguitar.io.base.TGSongWriterHandle;
import org.herac.tuxguitar.io.gtp.GP5InputStream;
import org.herac.tuxguitar.io.gtp.GP5OutputStream;
import org.herac.tuxguitar.io.gtp.GTPSettings;
import org.herac.tuxguitar.song.factory.TGFactory;
import org.herac.tuxguitar.song.models.*;
import org.herac.tuxguitar.song.models.effects.TGEffectHarmonic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.fireg45.demotabviewer.model.Tabulature;
import ru.fireg45.demotabviewer.services.FileService;
import ru.fireg45.demotabviewer.services.TabulatureService;
import ru.fireg45.demotabviewer.tab.dto.NoteChangesDTO;
import ru.fireg45.demotabviewer.tab.dto.TabChangesDTO;

import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TabEditor {

    private final TabulatureService tabulatureService;
    private final FileService fileService;
    private final TabReader tabReader;
    private final TGFactory factory = new TGFactoryImpl();

    @Autowired
    public TabEditor(TabulatureService tabulatureService, FileService fileService, TabReader tabReader) {
        this.tabulatureService = tabulatureService;
        this.fileService = fileService;
        this.tabReader = tabReader;
    }

    public TGSong readSong(int id) throws IOException {
        Optional<Tabulature> tabulatureOptional = tabulatureService.findById(id);
        if (tabulatureOptional.isPresent()) {
            Tabulature tabulature = tabulatureOptional.get();
            InputStream stream = fileService.download(tabulature);

            return tabReader.readSong(tabulature.getFilepath(), stream);
        }
        return null;
    }

    private void applyEffectChanges(TGNote note, NoteChangesDTO changes) {
        TGNoteEffect effect = note.getEffect();
        addEffects(changes, effect);
        removeEffects(changes, effect);
    }

    private void removeEffects(NoteChangesDTO changes, TGNoteEffect effect) {
        for (String removedEffect : changes.getRemovedEffects()) {
            switch (removedEffect.charAt(0)) {
                case 'p':
                    effect.setPalmMute(false);
                    break;
                case 't':
                    effect.setTapping(false);
                    break;
                case 's':
                    effect.setSlapping(false);
                    break;
                case 'P':
                    effect.setPopping(false);
                    break;
                case 'h':
                    effect.setHarmonic(null);
                    break;
                case '>':
                    effect.setAccentuatedNote(false);
                    break;
                case 'L':
                    effect.setLetRing(false);
                    break;
                case '^':
                    effect.setHeavyAccentuatedNote(false);
                    break;
                case 'v':
                    effect.setVibrato(false);
                    break;
                case 'x':
                    effect.setDeadNote(false);
                    break;
                case 'g':
                    effect.setGhostNote(false);
                    break;
                case 'H':
                    effect.setHammer(false);
                    break;
                case 'S':
                    effect.setSlide(false);
                    break;
            }
        }
    }

    private void addEffects(NoteChangesDTO changes, TGNoteEffect effect) {
        for (String addedEffect : changes.getAddedEffects()) {
            switch (addedEffect.charAt(0)) {
                case 'p':
                    effect.setPalmMute(true);
                    break;
                case 't':
                    effect.setTapping(true);
                    break;
                case 's':
                    effect.setSlapping(true);
                    break;
                case 'P':
                    effect.setPopping(true);
                    break;
                case 'h':
                    var harm = factory.newEffectHarmonic();
                    harm.setType(TGEffectHarmonic.TYPE_ARTIFICIAL);
                    effect.setHarmonic(harm);
                    break;
                case '>':
                    effect.setAccentuatedNote(true);
                    break;
                case 'L':
                    effect.setLetRing(true);
                    break;
                case '^':
                    effect.setHeavyAccentuatedNote(true);
                    break;
                case 'v':
                    effect.setVibrato(true);
                    break;
                case 'x':
                    effect.setDeadNote(true);
                    break;
                case 'g':
                    effect.setGhostNote(true);
                    break;
                case 'H':
                    effect.setHammer(true);
                    break;
                case 'S':
                    effect.setSlide(true);
                    break;
            }
        }
    }

    private void applyChanges(NoteChangesDTO changes, TGSong song, int track) {
        TGTrack tgTrack = song.getTrack(track);
        TGMeasure measure = tgTrack.getMeasure(changes.getStaveId());
        TGBeat beat = measure.getBeat(changes.getBeatId());
        int noteId = 0;
        TGNote note = null;
        for (int i = 0; i < beat.countVoices() && noteId != changes.getNoteId(); i++) {
            TGVoice voice = beat.getVoice(i);
            for (TGNote n : voice.getNotes()) {
                noteId++;
                if (noteId == changes.getNoteId()) {
                    note = n;
                    break;
                }
            }
        }
        if (note != null) {
            if (Objects.equals(changes.getOldFret(), "x")) {
                note.getEffect().setDeadNote(false);
            } else {
                if (changes.getNewFret().charAt(0) == 'x')
                    note.getEffect().setDeadNote(false);
                else
                    note.setValue(Integer.parseInt(changes.getNewFret()));
            }
            applyEffectChanges(note, changes);
        }
    }

    public void updateSong(int id, int track, TabChangesDTO tabChanges) throws IOException {
        TGSong song = readSong(id);
        TGFactory factory = new TGFactoryImpl();
        for (var change : tabChanges.getNoteChanges()) {
            applyChanges(change, song, track);
        }

        TGTrack tgTrack = song.getTrack(track);
        for (var change : tabChanges.getStaveChanges()) {
            TGMeasure measure = tgTrack.getMeasure(change.staveId);
            measure.getHeader().getTempo().setValue(change.newTempo);
        }

        TGSongWriter tgSongWriter = new GP5OutputStream(new GTPSettings());
        TGSongWriterHandle tgSongWriterHandle = new TGSongWriterHandle();

        String filename = "/tmp/t_" + System.currentTimeMillis() + song.getName() + song.getAuthor() + ".gp5";
        File targetFile = new File(filename);
        try (OutputStream out = new FileOutputStream(targetFile)) {
            tgSongWriterHandle.setOutputStream(out);
            tgSongWriterHandle.setSong(song);
            tgSongWriterHandle.setFactory(factory);
            tgSongWriterHandle.setFormat(GP5InputStream.FILE_FORMAT);
            tgSongWriter.write(tgSongWriterHandle);
        }

        try (InputStream in = new FileInputStream(filename)) {
            Optional<Tabulature> tabulatureOptional = tabulatureService.findById(id);
            String[] split = filename.split("/");
            filename = split[split.length - 1];
            String newFilepath = fileService.upload(in, filename);
            if (tabulatureOptional.isPresent()) {
                Tabulature tabulature = tabulatureOptional.get();
                tabulature.setFilepath(newFilepath);
                tabulatureService.save(tabulature);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        targetFile.deleteOnExit();
    }
}
