package ru.fireg45.demotabviewer.util.tabs.dto;

import lombok.Getter;
import lombok.Setter;
import ru.fireg45.demotabviewer.model.Tabulature;

import java.util.List;

@Setter
@Getter
public class TabListDTO {
    private List<Tabulature> tabs;
    private int pageCount;
    private int page;

    public TabListDTO(List<Tabulature> tabs, int pageCount, int page) {
        this.tabs = tabs;
        this.pageCount = pageCount;
        this.page = page;
    }
}
