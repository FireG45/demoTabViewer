package ru.fireg45.demotabviewer.tab.dto;

import lombok.Data;

import java.util.List;

@Data
public class TabChangesDTO {
    public List<NoteChangesDTO> noteChanges;
    public List<StaveChangesDTO> staveChanges;
}
