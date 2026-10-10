package com.xf.lg;

import java.nio.file.*;
import java.util.regex.*;

public class Config {

    public static Path CONFIG_FILE;
    public static boolean DEBUG = false;

    public static void load(Path gameDir) {
        try {
            Path cfgDir = gameDir.resolve("config");
            Files.createDirectories(cfgDir);
            CONFIG_FILE = cfgDir.resolve("xfloader.json");

            if (!Files.exists(CONFIG_FILE)) {
                Files.writeString(CONFIG_FILE, "{\n  \"debug\": false\n}\n");
                Log.i("已生成默认配置: " + CONFIG_FILE);
            }

            String content = Files.readString(CONFIG_FILE);
            Matcher m = Pattern.compile("\"debug\"\\s*:\\s*(true|false)").matcher(content);
            if (m.find()) {
                DEBUG = Boolean.parseBoolean(m.group(1));
                Log.DEBUG = DEBUG;
                Log.i("从配置读到 debug = " + DEBUG);
            }
        } catch (Exception e) {
            Log.fail("读配置失败: " + e);
        }
    }

    public static void save() {
        try {
            Files.writeString(CONFIG_FILE, "{\n  \"debug\": " + DEBUG + "\n}\n");
            Log.ok("配置已保存: debug = " + DEBUG);
        } catch (Exception e) {
            Log.fail("保存失败: " + e);
        }
    }
}
