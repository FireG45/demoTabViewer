package ru.fireg45.demotabviewer.util;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Tuple<T1, T2> {
    private T1 value1;
    private T2 value2;

    public Tuple(T1 value1, T2 value2) {
        this.value1 = value1;
        this.value2 = value2;
    }
}
