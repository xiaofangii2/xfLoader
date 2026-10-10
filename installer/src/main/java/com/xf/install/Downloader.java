package com.xf.install;

import java.io.*;
import java.net.*;
import java.nio.file.*;

public class Downloader {
    public static void download(String url, Path out) throws IOException {
        Files.createDirectories(out.getParent());
        System.out.println("下载: " + url);
        System.out.println("  到: " + out);

        try (InputStream in = new URL(url).openStream()) {
            Files.copy(in, out, StandardCopyOption.REPLACE_EXISTING);
        }
        System.out.println("  完成 (" + Files.size(out) + " bytes)");
    }
}
