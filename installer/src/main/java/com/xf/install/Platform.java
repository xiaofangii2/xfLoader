package com.xf.install;

import java.nio.file.*;

public class Platform {
    public static String current() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) return "windows";
        if (os.contains("mac")) return "macos";
        return "linux";
    }

    public static Path minecraftDir() {
        String os = System.getProperty("os.name").toLowerCase();
        String home = System.getProperty("user.home");

        if (os.contains("win")) {
            String appdata = System.getenv("APPDATA");
            return Paths.get(appdata, ".minecraft");
        }
        if (os.contains("mac")) {
            return Paths.get(home, "Library", "Application Support", "minecraft");
        }
        return Paths.get(home, ".minecraft");
    }
}
