package ru.fireg45.demotabviewer.util.tabs.dto;

import java.util.List;

public record BeatDTO(String duration, boolean palmMute, boolean ghostNote, List<NoteDTO> noteDTOS,
                      List<String> effects) {

}
