package com.xf.lg;

import java.nio.file.*;

public class LoaderCore {
    public static void init() {
        Log.ok("xfLoader 初始化");
        try {
            Path gameDir = Path.of(System.getProperty("user.dir"));
            Config.load(gameDir);
            Path dir = gameDir.resolve("datapack");
            Files.createDirectories(dir);
            Log.ok("datapack 目录: " + dir);
            DatapackCache.scan(dir);
        } catch (Exception e) {
            Log.fail("初始化失败: " + e);
            e.printStackTrace();
        }

        // 针对 Forge 的 LootModifierManager 崩溃做兜底
        try {
            Class<?> handler = Class.forName("net.minecraftforge.common.ForgeInternalHandler");
            handler.getMethod("getLootModifierManager").invoke(null);
            Log.ok("成功提前初始化 LootModifierManager");
        } catch (Throwable ignored) {
            // 不是 Forge 环境，或者找不到这个方法，直接跳过
        }
    }

    public static void onPreload() {
        try {
            Class.forName("net.minecraft.server.players.PlayerList");
            Class.forName("net.minecraft.client.gui.screens.TitleScreen");
            Class.forName("net.minecraft.client.gui.screens.PauseScreen");
            Log.ok("预加载完成");
        } catch (Throwable t) {
            Log.fail("预加载失败: " + t);
        }
    }
}
