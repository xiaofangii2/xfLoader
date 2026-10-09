package com.xf.lg;

import java.lang.instrument.Instrumentation;
import java.nio.file.*;

public class MyLoaderAgent {
    public static void premain(String args, Instrumentation inst) {
        Log.ok("agent 挂载");
        try {
            Path dir = Path.of(System.getProperty("user.dir"), "datapack");
            Files.createDirectories(dir);
            Log.ok("datapack 目录: " + dir);
            DatapackCache.scan(dir);
        } catch (Exception e) {
            Log.fail("初始化失败: " + e);
            e.printStackTrace();
        }
        inst.addTransformer(new MyTransformer(), true);
        Log.ok("transformer 已装");
    }
}
