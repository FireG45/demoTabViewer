var audioContext = null;
var player = null;
var reverberator = null;
var equalizer = null;
var songStart = 0;
var input = null;
var currentSongTime = 0;
var nextStepTime = 0;
var nextPositionTime = 0;
var loadedsong = null;

var path = "http://localhost:8080/tabs/midi/355"
console.log(path);
var xmlHttpRequest = new XMLHttpRequest();
xmlHttpRequest.open("GET", path, true);
xmlHttpRequest.responseType = "arraybuffer";
xmlHttpRequest.onload = function (e) {
    var arrayBuffer = xmlHttpRequest.response;
    var midiFile = new MIDIFile(arrayBuffer);
    var song = midiFile.parseSong();
    startLoad(song);
};
xmlHttpRequest.send(null);

function startLoad(song) {
    console.log(song);
    var AudioContextFunc = window.AudioContext || window.webkitAudioContext;
    audioContext = new AudioContextFunc();
    player = new WebAudioFontPlayer();

    equalizer = player.createChannel(audioContext);
    reverberator = player.createReverberator(audioContext);
    input = equalizer.input;
    equalizer.output.connect(reverberator.input);
    reverberator.output.connect(audioContext.destination);

    for (var i = 0; i < song.tracks.length; i++) {
        var nn = player.loader.findInstrument(song.tracks[i].program);
        var info = player.loader.instrumentInfo(nn);
        song.tracks[i].info = info;
        song.tracks[i].id = nn;
        player.loader.startLoad(audioContext, info.url, info.variable);
    }
    for (var i = 0; i < song.beats.length; i++) {
        var nn = player.loader.findDrum(song.beats[i].n);
        var info = player.loader.drumInfo(nn);
        song.beats[i].info = info;
        song.beats[i].id = nn;
        player.loader.startLoad(audioContext, info.url, info.variable);
    }
    player.loader.waitLoad(function () {
        console.log('buildControls');
        buildControls(song);
        resetEqlualizer();
    });
}

function startPlay(song) {
    currentSongTime = 0;
    songStart = audioContext.currentTime;
    nextStepTime = audioContext.currentTime;
    var stepDuration = 44 / 1000;
    tick(song, stepDuration);
}

function tick(song, stepDuration) {
    if (audioContext.currentTime > nextStepTime - stepDuration) {
        sendNotes(song, songStart, currentSongTime, currentSongTime + stepDuration, audioContext, input, player);
        currentSongTime = currentSongTime + stepDuration;
        nextStepTime = nextStepTime + stepDuration;
        if (currentSongTime > song.duration) {
            currentSongTime = currentSongTime - song.duration;
            sendNotes(song, songStart, 0, currentSongTime, audioContext, input, player);
            songStart = songStart + song.duration;
        }
    }
    if (nextPositionTime < audioContext.currentTime) {
        var o = document.getElementById('position');
        o.value = 100 * currentSongTime / song.duration;
        document.getElementById('tmr').innerHTML = '' + Math.round(100 * currentSongTime / song.duration) + '%';
        nextPositionTime = audioContext.currentTime + 3;
    }
    window.requestAnimationFrame(function (t) {
        tick(song, stepDuration);
    });
}

function sendNotes(song, songStart, start, end, audioContext, input, player) {
    for (var t = 0; t < song.tracks.length; t++) {
        var track = song.tracks[t];
        for (var i = 0; i < track.notes.length; i++) {
            if (track.notes[i].when >= start && track.notes[i].when < end) {
                var when = songStart + track.notes[i].when;
                var duration = track.notes[i].duration;
                if (duration > 3) {
                    duration = 3;
                }
                var instr = track.info.variable;
                var v = track.volume / 7;
                player.queueWaveTable(audioContext, input, window[instr], when, track.notes[i].pitch, duration, v, track.notes[i].slides);
            }
        }
    }
    for (var b = 0; b < song.beats.length; b++) {
        var beat = song.beats[b];
        for (var i = 0; i < beat.notes.length; i++) {
            if (beat.notes[i].when >= start && beat.notes[i].when < end) {
                var when = songStart + beat.notes[i].when;
                var duration = 1.5;
                var instr = beat.info.variable;
                var v = beat.volume / 2;
                player.queueWaveTable(audioContext, input, window[instr], when, beat.n, duration, v);
            }
        }
    }
}


function resetEqlualizer() {
    equalizer.band32.gain.setTargetAtTime(2, 0, 0.0001);
    equalizer.band64.gain.setTargetAtTime(2, 0, 0.0001);
    equalizer.band128.gain.setTargetAtTime(1, 0, 0.0001);
    equalizer.band256.gain.setTargetAtTime(0, 0, 0.0001);
    equalizer.band512.gain.setTargetAtTime(-1, 0, 0.0001);
    equalizer.band1k.gain.setTargetAtTime(5, 0, 0.0001);
    equalizer.band2k.gain.setTargetAtTime(4, 0, 0.0001);
    equalizer.band4k.gain.setTargetAtTime(3, 0, 0.0001);
    equalizer.band8k.gain.setTargetAtTime(-2, 0, 0.0001);
    equalizer.band16k.gain.setTargetAtTime(2, 0, 0.0001);
}

function setVolumeAction(i, song) {
    var vlm = document.getElementById('channel' + i);
    vlm.oninput = function (e) {
        player.cancelQueue(audioContext);
        var v = vlm.value / 100;
        if (v < 0.000001) {
            v = 0.000001;
        }
        song.tracks[i].volume = v;
    };
    var sl = document.getElementById('selins' + i);
    sl.onchange = function (e) {
        var nn = sl.value;
        var info = player.loader.instrumentInfo(nn);
        player.loader.startLoad(audioContext, info.url, info.variable);
        player.loader.waitLoad(function () {
            console.log('loaded');
            song.tracks[i].info = info;
            song.tracks[i].id = nn;
        });
    };
}

function setDrVolAction(i, song) {
    var vlm = document.getElementById('drum' + i);
    vlm.oninput = function (e) {
        player.cancelQueue(audioContext);
        var v = vlm.value / 100;
        if (v < 0.000001) {
            v = 0.000001;
        }
        song.beats[i].volume = v;
    };
    var sl = document.getElementById('seldrm' + i);
    sl.onchange = function (e) {
        var nn = sl.value;
        var info = player.loader.drumInfo(nn);
        player.loader.startLoad(audioContext, info.url, info.variable);
        player.loader.waitLoad(function () {
            console.log('loaded');
            song.beats[i].info = info;
            song.beats[i].id = nn;
        });
    };
}

function handleFileSelect(event) {
    console.log(event);
    var file = event.target.files[0];
    console.log(file);
    var fileReader = new FileReader();
    fileReader.onload = function (progressEvent) {
        console.log(progressEvent);
        var arrayBuffer = progressEvent.target.result;
        console.log(arrayBuffer);
        var midiFile = new MIDIFile(arrayBuffer);
        var song = midiFile.parseSong();
        startLoad(song);
    };
    fileReader.readAsArrayBuffer(file);
}