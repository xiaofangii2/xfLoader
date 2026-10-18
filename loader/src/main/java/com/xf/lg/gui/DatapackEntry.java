package com.xf.lg.gui;

import java.nio.file.Path;

public class DatapackEntry {
    public final String name;
    public final String description;
    public final Path path;
    public final boolean builtin;

    public DatapackEntry(String name, String description, Path path, boolean builtin) {
        this.name = name;
        this.description = description;
        this.path = path;
        this.builtin = builtin;
    }
}
