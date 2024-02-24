'use strict'

import MIDIFile from "./MIDIFile";
import {WebAudioFontPlayer} from 'webaudiofont';

var ex = null;

export default class MidiWebPlayer {
    constructor(path) {
        ex = this;
        ex.path = path;
        ex.audioContext = null;
        ex.player = null;
        ex.reverberator = null;
        ex.equalizer = null;
        ex.songStart = 0;
        ex.input = null;
        ex.currentSongTime = 0;
        ex.nextStepTime = 0;
        ex.nextPositionTime = 0;
        ex.loadedsong = null;

        ex.speed = 0.9;
        ex.isPlaying = false;
        ex.isStarted = false;

        ex.play = function () {
            console.log(ex.speed)
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

        ex.startPlay = function (song) {
            ex.currentSongTime = 0;
            ex.songStart = ex.audioContext.currentTime;
            ex.nextStepTime = ex.audioContext.currentTime;
            let stepDuration = 44 / 1000;
            ex.tick(song, stepDuration);
        }

        ex.tick = function (song, stepDuration) {
            try {
                if (ex.audioContext.currentTime > ex.nextStepTime - stepDuration) {
                    ex.sendNotes(song, ex.songStart, ex.currentSongTime, ex.currentSongTime + stepDuration, ex.audioContext, ex.input, ex.player);
                    ex.currentSongTime = ex.currentSongTime + stepDuration;
                    ex.nextStepTime = ex.nextStepTime + stepDuration;
                    if (ex.currentSongTime > song.duration) {
                        ex.currentSongTime = ex.currentSongTime - song.duration;
                        ex.sendNotes(song, ex.songStart, 0, ex.currentSongTime, ex.audioContext, ex.input, ex.player);
                        ex.songStart = ex.songStart + song.duration;
                    }
                }
                if (ex.nextPositionTime < ex.audioContext.currentTime) {
                    ex.nextPositionTime = ex.audioContext.currentTime + 3;
                }
                if (ex.isPlaying && ex.isStarted) {
                    window.requestAnimationFrame(function (t) {
                        ex.tick(song, stepDuration);
                    });
                }
            } catch (e) {
                console.log(e)
            }
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
                        var v = track.volume / 7;
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
                        var v = beat.volume / 2;
                        ex.player.queueWaveTable(ex.audioContext, ex.input, window[instr], when, beat.n, duration, v);
                    }
                }
            }
        }

        ex.startLoad = function (song) {
            console.log(song);
            var AudioContextFunc = window.AudioContext || window.webkitAudioContext;
            ex.audioContext = new AudioContextFunc();
            ex.player = new WebAudioFontPlayer();

            ex.equalizer = ex.player.createChannel(ex.audioContext);
            ex.reverberator = ex.player.createReverberator(ex.audioContext);
            ex.input = ex.equalizer.input;
            ex.equalizer.output.connect(ex.reverberator.input);
            ex.reverberator.output.connect(ex.audioContext.destination);

            for (var i = 0; i < song.tracks.length; i++) {
                var nn = ex.player.loader.findInstrument(song.tracks[i].program);
                var info = ex.player.loader.instrumentInfo(nn);
                song.tracks[i].info = info;
                song.tracks[i].id = nn;
                ex.player.loader.startLoad(ex.audioContext, info.url, info.variable);
            }
            for (var i = 0; i < song.beats.length; i++) {
                var nn = ex.player.loader.findDrum(song.beats[i].n);
                var info = ex.player.loader.drumInfo(nn);
                song.beats[i].info = info;
                song.beats[i].id = nn;
                ex.player.loader.startLoad(ex.audioContext, info.url, info.variable);
            }
            var self = ex;
            ex.player.loader.waitLoad(function () {
                self.resetEqlualizer();
            });
            ex.loadedsong = song
        }

        ex.resetEqlualizer = function () {
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
        }

        ex.handleMidi = function () {
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
        }
    }
}