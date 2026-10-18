package com.xf.lg.gui;

import com.xf.lg.DatapackCache;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class DatapackList {

    public static List<DatapackEntry> load() {
        List<DatapackEntry> list = new ArrayList<>();
        Path dpDir = DatapackCache.DATAPACK_DIR;
        if (dpDir == null || !Files.isDirectory(dpDir)) return list;

        try (var s = Files.list(dpDir)) {
            for (Path p : s.toList()) {
                if (Files.isDirectory(p)) {
                    Path meta = p.resolve("pack.mcmeta");
                    if (Files.exists(meta)) {
                        list.add(new DatapackEntry(
                            p.getFileName().toString(),
                            readMetaFromFile(meta),
                            p, true
                        ));
                    }
                } else if (p.toString().endsWith(".zip")) {
                    list.add(new DatapackEntry(
                        p.getFileName().toString(),
                        readMetaFromZip(p),
                        p, false
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    private static String readMetaFromFile(Path meta) {
        try {
            return extractDescription(Files.readString(meta, StandardCharsets.UTF_8));
        } catch (Exception e) {
            return "(读取失败)";
        }
    }

    private static String readMetaFromZip(Path zip) {
        try (ZipFile zf = new ZipFile(zip.toFile())) {
            ZipEntry entry = zf.getEntry("pack.mcmeta");
            if (entry == null) return "(无 pack.mcmeta)";
            try (InputStream in = zf.getInputStream(entry)) {
                return extractDescription(new String(in.readAllBytes(), StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            return "(读取失败)";
        }
    }

    private static String extractDescription(String json) {
        int i = json.indexOf("\"description\"");
        if (i < 0) return "(无 description)";
        int colon = json.indexOf(':', i);
        if (colon < 0) return "(格式错误)";
        int start = json.indexOf('"', colon);
        if (start < 0) return "(格式错误)";
        int end = start + 1;
        while (end < json.length()) {
            char c = json.charAt(end);
            if (c == '\\') { end += 2; continue; }
            if (c == '"') break;
            end++;
        }
        if (end >= json.length()) return "(格式错误)";
        return json.substring(start + 1, end);
    }
}
