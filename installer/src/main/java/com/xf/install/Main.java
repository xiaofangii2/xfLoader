package com.xf.install;

import java.nio.file.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== xfLoader-install ===");
        System.out.println("平台: " + Platform.current());

        Path mcDir = Platform.minecraftDir();
        System.out.println("官方启动器目录: " + mcDir);

        if (!Files.isDirectory(mcDir)) {
            System.out.println("找不到 .minecraft 目录, 请先用官方启动器启动一次 26.3");
            return;
        }

        Path versionDir = mcDir.resolve("versions/26.3");
        Path versionJson = versionDir.resolve("26.3.json");

        if (!Files.exists(versionJson)) {
            System.out.println("找不到 " + versionJson);
            System.out.println("请先用官方启动器下载 26.3, 再运行本安装器");
            return;
        }

        Installer.install(mcDir, versionJson);
    }
}
