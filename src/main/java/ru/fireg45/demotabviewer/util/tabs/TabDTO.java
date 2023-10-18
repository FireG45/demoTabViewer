package ru.fireg45.demotabviewer.util.tabs;

import java.util.List;

record NoteDTO(int string, int fret) {
}

record MeasureDTO(List<NoteDTO> noteDTOS) {}

public class TabDTO {
    public String title;
    public String author;
    public MeasureDTO[] measures;
}
