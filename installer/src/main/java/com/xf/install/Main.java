package com.xf.install;

import java.nio.file.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== xfLoader-install ===");
        System.out.println("平台: " + Platform.current());

        Path mcDir = Platform.minecraftDir();
        System.out.println("官方启动器目录: " + mcDir);

        if (!Files.isDirectory(mcDir)) {
            System.out.println("找不到 .minecraft 目录, 请先用官方启动器启动一次。");
            return;
        }

        Installer.installInteractive(mcDir);
    }
}
