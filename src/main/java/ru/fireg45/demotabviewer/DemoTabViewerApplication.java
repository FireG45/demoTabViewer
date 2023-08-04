package ru.fireg45.demotabviewer;

import org.herac.tuxguitar.io.base.TGFileFormatException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.fireg45.demotabviewer.util.TabReader;

import java.io.IOException;

@SpringBootApplication
public class DemoTabViewerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoTabViewerApplication.class, args);
    }

}
