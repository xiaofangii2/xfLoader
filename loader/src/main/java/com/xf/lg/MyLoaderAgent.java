package com.xf.lg;

import java.lang.instrument.Instrumentation;
import java.nio.file.*;

public class MyLoaderAgent {
    public static void main(String[] args) {}

    public static void premain(String args, Instrumentation inst) {
        Log.ok("agent 挂载");

        inst.addTransformer(new MyTransformer(), true);
        inst.addTransformer(new PlayerJoinTransformer(), true);
        Log.ok("transformers 已装 (2 个)");

        try {
            Path gameDir = Path.of(System.getProperty("user.dir"));

            // 1. 先建 config/ 目录 + 默认配置
            Path cfgDir = gameDir.resolve("config");
            Files.createDirectories(cfgDir);
            Path cfgFile = cfgDir.resolve("xfloader.json");
            if (!Files.exists(cfgFile)) {
                Files.writeString(cfgFile, "{\n  \"debug\": false\n}\n");
                Log.ok("已创建默认配置: " + cfgFile);
            } else {
                Log.i("配置文件已存在: " + cfgFile);
            }

            // 2. 读配置
            Config.load(gameDir);

            // 3. 建 datapack 目录 + 扫描
            Path dir = gameDir.resolve("datapack");
            Files.createDirectories(dir);
            Log.ok("datapack 目录: " + dir);
            DatapackCache.scan(dir);

        } catch (Exception e) {
            Log.fail("初始化失败: " + e);
            e.printStackTrace();
        }

        try {
            Class.forName("net.minecraft.server.players.PlayerList");
            Log.ok("PlayerList 预加载完成");
        } catch (Throwable t) {
            Log.fail("预加载 PlayerList 失败: " + t);
        }
    }
}
