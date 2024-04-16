package ru.fireg45.demotabviewer.tab.dto;

import lombok.Data;

import java.util.List;

@Data
public class NoteChangesDTO {
    private int noteId;
    private int beatId;
    private String oldFret;
    private String newFret;
    private List<String> addedEffects;
    private List<String> removedEffects;
    private int staveId;
}
