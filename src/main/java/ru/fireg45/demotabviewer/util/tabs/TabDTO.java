package ru.fireg45.demotabviewer.util.tabs;

record BeatDTO(int string, int fret) {
}

record MeasureDTO(BeatDTO[] beatDTOS) {}

public class TabDTO {
    public MeasureDTO[] measures;
}
