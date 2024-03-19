import MIDIFile from "./MIDIFile";
import {WebAudioFontPlayer} from 'webaudiofont';

var ex = null;

export default class MidiWebPlayer {
    constructor(path, score, measures, measuresLengths, tabBeats, metronomeTrack, enableMetronome, measuresStarts,
                measuresStartNotesInds) {
        ex = this;
        ex.path = path;
        ex.score = score;
        ex.measures = measures;
        ex.measuresLengths = measuresLengths;
        ex.audioContext = new window.AudioContext();
        ex.player = null;
        ex.reverberator = null;
        ex.equalizer = null;
        ex.songStart = 0;
        ex.input = null;
        ex.currentSongTime = 0;
        ex.nextStepTime = 0;
        ex.nextPositionTime = 0;
        ex.loadedsong = null;

        ex.tabBeats = tabBeats;
        ex.metronomeTrack = metronomeTrack;
        ex.enableMetronome = enableMetronome;

        ex.measuresStarts = measuresStarts;
        ex.measuresStartNotesInds = measuresStartNotesInds;

        ex.speed = 1.0;
        ex.isPlaying = false;
        ex.isStarted = false;

        ex.lastNoteIndex = 0;

        ex.tracks = [];

        ex.loaded = false;

        ex.remapInstrument = function (nn) {
            if (315 <= nn && nn <= 332) { // overdrive guitar remap
                return 327;
            }
            if (333 <= nn && nn <= 353) { // dist guitar remap
                return 339;
            }
            if (274 <= nn && nn <= 285) { // jazz guitar remap
                return 278;
            }

            return nn;
        }

        ex.play = function () {
            if (!ex.loaded || !ex.loaded || !ex.audioContext) return;
            try {
                if (!ex.isStarted) {
                    ex.audioContext.resume().then(r => {
                        ex.isPlaying = true;
                        ex.isStarted = true;
                        ex.startPlay(ex.loadedsong);
                    });
                } else {
                    if (!ex.isPlaying) {
                        ex.audioContext.resume().then(() => {
                            ex.isPlaying = true;
                            let stepDuration = 44 / 1000;
                            ex.tick(ex.loadedsong, stepDuration);
                        });
                    }
                }
            } catch (e) {
                ex.handleMidi();
                ex.play();
            }
        }

        ex.pause = function () {
            console.log(ex.speed)
            if (ex.isPlaying) {
                ex.audioContext.suspend().then(() => {
                    ex.isPlaying = false;
                });
            }
        }

        ex.setSpeed = function (speed) {
            ex.speed = speed;
        }

        ex.getIsPlaying = function () {
            return ex.isPlaying;
        };

        ex.getContextTime = function () {
            return ex.audioContext.currentTime;
        }

        ex.startPlay = function (song) {
            ex.currentSongTime = 0;
            ex.songStart = ex.getContextTime();
            ex.nextStepTime = ex.getContextTime();
            let stepDuration = 44 / 1000;

            ex.lastMeasure = ex.score.current.state.start;
            ex.lastNoteIndex = ex.measuresStartNotesInds[ex.score.current.state.start] - 1

            let next = ex.measuresStarts[ex.score.current.state.start];
            ex.songStart = ex.songStart - (next - ex.currentSongTime);
            ex.currentSongTime = next;

            ex.tick(song, stepDuration);
        }

        ex.tick = function (song, stepDuration) {
            let currentNote = ex.getCurrNote();
            let measures = ex.score.current.state.measureObjs;
            let lastMeasure = ex.score.current.state.lastMeasure;
            let measuresLengths = ex.score.current.state.measuresLengths;


            if (currentNote === 0 && lastMeasure !== 0) {
                //let prevId = lastMeasure;
                ex.score.current.setMeasure(0)
                //ex.score.current.state.measureObjs[prevId].current.componentDidMount();
            }

            let noteCount = measuresLengths.slice(0, lastMeasure + 1).reduce((a, b) => a + b);

            if (currentNote >= noteCount) {
                lastMeasure = (lastMeasure + 1) % measures.length;
                ex.score.current.setMeasure(lastMeasure)
                ex.score.current.state.measureObjs[lastMeasure - 1]
                    .current.setNote(0);
                ex.score.current.state.measureObjs[lastMeasure - 1].current.componentDidMount();
                ex.score.current.state.measureObjs[lastMeasure].current.componentDidMount();
            } else {
                let measureId = lastMeasure;
                ex.score.current.state.measureObjs[measureId]
                    .current.setNote((currentNote - noteCount + measuresLengths[lastMeasure]) + 1);
                ex.score.current.state.measureObjs[measureId].current.componentDidMount();
            }

            ex.score.current.state.measureObjs[lastMeasure > 0 ? lastMeasure - 1 : 0].current.componentDidMount();

            if (ex.getContextTime() > ex.nextStepTime - stepDuration) {
                ex.sendNotes(song, ex.songStart, ex.currentSongTime, ex.currentSongTime + stepDuration, ex.audioContext, ex.input, ex.player);
                ex.currentSongTime = ex.currentSongTime + stepDuration;
                ex.nextStepTime = ex.nextStepTime + stepDuration;
                if (ex.currentSongTime > song.duration * (1 / ex.speed)) {
                    ex.audioContext.suspend().then(() => {
                        ex.handleMidi();
                        ex.isPlaying = false;
                        ex.isStarted = false
                    });
                }
            }
            if (ex.nextPositionTime < ex.getContextTime()) {
                ex.nextPositionTime = ex.getContextTime() + 3;
            }
            if (ex.isPlaying && ex.isStarted) {
                window.requestAnimationFrame(function (t) {
                    try {
                        ex.tick(song, stepDuration);
                    } catch (e) {

                    }
                });
            }
        }

        ex.getCurrNote = function () {
            let tabBeats = ex.tabBeats;

            let song = ex.loadedsong;
            if (song === null || song.tracks === null) {
                ex.lastNoteIndex = 0;
                return 0;
            }

            let notes = tabBeats;

            if (0.0 <= ex.currentSongTime && ex.currentSongTime <= (notes[1].when * (1 / ex.speed)))
                ex.lastNoteIndex = 0;

            let ind = ex.lastNoteIndex + 1 < notes.length ? ex.lastNoteIndex + 1 : notes.length - 1;

            let nextNoteWhen = (notes[ind].when * (1 / ex.speed));

            if (ex.currentSongTime >= nextNoteWhen && ex.lastNoteIndex < notes.length - 1) {
                ex.lastNoteIndex++;
            }

            return ex.lastNoteIndex;
        }

        ex.sendNotes = function (song, songStart, start, end, audioContext, input, player) {
            for (let t = 0; t < song.tracks.length; t++) {
                let track = song.tracks[t];
                for (var i = 0; i < track.notes.length; i++) {
                    if (track.notes[i].when * (1 / ex.speed) >= start && track.notes[i].when * (1 / ex.speed) < end) {
                        var when = ex.songStart + track.notes[i].when;
                        var duration = track.notes[i].duration;
                        if (duration > 3) {
                            duration = 3;
                        }
                        var instr = track.info.variable;
                        var v = track.volume / 2;
                        ex.player.queueWaveTable(ex.audioContext, ex.input, window[instr], when, track.notes[i].pitch, duration, v, track.notes[i].slides);
                    }
                }
            }
            for (var b = 0; b < song.beats.length; b++) {
                var beat = song.beats[b];
                for (var i = 0; i < beat.notes.length; i++) {
                    if (beat.notes[i].when * (1 / ex.speed) >= start && beat.notes[i].when * (1 / ex.speed) < end) {
                        var when = ex.songStart + beat.notes[i].when;
                        var duration = 1.5;
                        var instr = beat.info.variable;
                        var v = beat.volume / 3;
                        ex.player.queueWaveTable(ex.audioContext, ex.input, window[instr], when, beat.n, duration, v);
                    }
                }
            }
        }

        ex.startLoad = function (song) {
            console.log(song);
            ex.audioContext = new window.AudioContext();
            ex.player = new WebAudioFontPlayer();

            ex.equalizer = ex.player.createChannel(ex.audioContext);
            ex.reverberator = ex.player.createReverberator(ex.audioContext);
            ex.input = ex.equalizer.input;
            ex.equalizer.output.connect(ex.reverberator.input);
            ex.reverberator.output.connect(ex.audioContext.destination);

            let beats = ex.tabBeats;

            // let notes = []
            // for (let i = 0; i < song.tracks[1].notes.length; i++) {
            //     let note = song.tracks[1].notes[i];
            //     if (i > 0 && note.when === song.tracks[1].notes[i - 1].when) continue;
            //     notes.push({when: note.when, duration: note.duration});
            // }

            for (var i = 0; i < song.tracks.length; i++) {
                var nn = ex.remapInstrument(ex.player.loader.findInstrument(song.tracks[i].program));
                var info = ex.player.loader.instrumentInfo(nn);

                song.tracks[i].info = info;
                song.tracks[i].id = nn;
                song.tracks[i].volume = 1.0;
                ex.player.loader.startLoad(ex.audioContext, info.url, info.variable);
            }

            if (ex.enableMetronome) song.beats.push(ex.metronomeTrack);

            for (var i = 0; i < song.beats.length; i++) {
                var nn = ex.player.loader.findDrum(song.beats[i].n);
                var info = ex.player.loader.drumInfo(nn);
                song.beats[i].info = info;
                song.beats[i].id = nn;
                ex.player.loader.startLoad(ex.audioContext, info.url, info.variable);
            }

            const self = ex;
            ex.player.loader.waitLoad(function () {
                self.resetEqualizer();
            });
            ex.loadedsong = song
            ex.loaded = true;
        }

        ex.resetEqualizer = function () {
            try {
                ex.equalizer.band32.gain.setTargetAtTime(2, 0, 0.0001);
                ex.equalizer.band64.gain.setTargetAtTime(2, 0, 0.0001);
                ex.equalizer.band128.gain.setTargetAtTime(1, 0, 0.0001);
                ex.equalizer.band256.gain.setTargetAtTime(0, 0, 0.0001);
                ex.equalizer.band512.gain.setTargetAtTime(-1, 0, 0.0001);
                ex.equalizer.band1k.gain.setTargetAtTime(5, 0, 0.0001);
                ex.equalizer.band2k.gain.setTargetAtTime(4, 0, 0.0001);
                ex.equalizer.band4k.gain.setTargetAtTime(3, 0, 0.0001);
                ex.equalizer.band8k.gain.setTargetAtTime(-2, 0, 0.0001);
                ex.equalizer.band16k.gain.setTargetAtTime(2, 0, 0.0001);
            } catch (e) {
                ex.handleMidi();
            }
        }

        ex.handleMidi = function () {
            try {
                var self = ex;
                path = ex.path;
                console.log(path);
                var xmlHttpRequest = new XMLHttpRequest();
                xmlHttpRequest.open("GET", path, true);
                xmlHttpRequest.responseType = "arraybuffer";
                xmlHttpRequest.onload = function (e) {
                    var arrayBuffer = xmlHttpRequest.response;
                    var midiFile = new MIDIFile(arrayBuffer);
                    var song = midiFile.parseSong();
                    self.startLoad(song);
                };
                xmlHttpRequest.send(null);
            } catch (e) {
                ex.handleMidi();
            }
        }
    }
}