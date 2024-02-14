package ru.fireg45.demotabviewer.tab.dto;

import java.util.List;

public record MeasureDTO(int tempo, String timeSignature, List<BeatDTO> beatDTOS, List<List<Integer>> pmIndexes,
                         List<String> slidesAndTies, List<List<Integer>> lrIndexes) {
}
