package ru.fireg45.demotabviewer.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequest {
    private int tabId;
    private int value;
}
