package ru.fireg45.demotabviewer.tab.dto;

import lombok.Data;

@Data
public class StaveChangesDTO {
    public int staveId;
    public int oldTempo;
    public int newTempo;
}
