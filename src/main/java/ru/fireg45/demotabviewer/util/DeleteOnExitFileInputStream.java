package ru.fireg45.demotabviewer.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DeleteOnExitFileInputStream extends FileInputStream {
    File file;

    public DeleteOnExitFileInputStream(String s) throws FileNotFoundException {
        this(new File(s));
    }

    public DeleteOnExitFileInputStream(File file) throws FileNotFoundException {
        super(file);
        this.file = file;
    }

    public void close() throws IOException {
        try {
            super.close();
        } finally {
            if (file != null) {
                file.delete();
                file = null;
            }
        }
    }
}
