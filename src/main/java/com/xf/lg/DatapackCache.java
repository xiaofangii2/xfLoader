package com.xf.lg;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.*;
import java.util.zip.*;

public class DatapackCache {

    public static final List<Path> ZIPS = new ArrayList<>();
    public static Path DATAPACK_DIR;

    public static void scan(Path dir) {
        Log.step("扫描数据包");
        DATAPACK_DIR = dir;
        ZIPS.clear();

        // 1. jar 内置数据包 -> 直接解压到 datapack/
        try {
            extractBuiltin(dir);
        } catch (Exception e) {
            Log.fail("内置解压失败: " + e);
            e.printStackTrace();
        }

        // 2. 扫外部 zip
        if (!Files.isDirectory(dir)) {
            Log.fail("目录不存在");
            return;
        }
        try (var s = Files.list(dir)) {
            for (Path p : s.toList()) {
                if (p.toString().endsWith(".zip")) {
                    ZIPS.add(p);
                    Log.i("  [外部] " + p.getFileName());
                }
            }
        } catch (Exception e) {
            Log.fail("扫描失败: " + e);
        }
        Log.ok("完成: 外部=" + ZIPS.size());
    }

    private static void extractBuiltin(Path datapackDir) throws Exception {
        URL probe = DatapackCache.class.getResource("/xf_datapacks/");
        if (probe == null) {
            Log.i("jar 里无 xf_datapacks/");
            return;
        }
        Log.i("找到 xf_datapacks/");

        URL jarUrl = DatapackCache.class.getProtectionDomain().getCodeSource().getLocation();
        Path jarPath = Paths.get(jarUrl.toURI());

        int count = 0;
        try (ZipFile zf = new ZipFile(jarPath.toFile())) {
            var entries = zf.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName();
                if (!name.startsWith("xf_datapacks/")) continue;
                if (entry.isDirectory()) continue;

                // xf_datapacks/builtin/pack.mcmeta  ->  datapack/builtin/pack.mcmeta
                String rel = name.substring("xf_datapacks/".length());
                Path out = datapackDir.resolve(rel);
                Files.createDirectories(out.getParent());
                try (InputStream is = zf.getInputStream(entry)) {
                    Files.copy(is, out, StandardCopyOption.REPLACE_EXISTING);
                }
                Log.i("  解压: " + rel);
                count++;
            }
        }
        Log.ok("内置解压 " + count + " 个文件 -> " + datapackDir);
    }
}
