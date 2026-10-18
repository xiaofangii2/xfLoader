package com.xf.install;

import com.google.gson.*;
import java.nio.file.*;
import java.util.Scanner;

public class Installer {

    private static final String BASE_URL = "https://github.com/xiaofangii2/xfLoader/releases/latest/download/";

    public static void installInteractive(Path mcDir) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请选择要安装的加载器版本：");
        System.out.println("1. Fabric 版 (放入 mods 文件夹)");
        System.out.println("2. Forge 版 (放入 mods 文件夹)");
        System.out.println("3. Agent 版 (直接修改版本文件)");
        System.out.print("请输入数字 (1-3): ");
        
        String choice = scanner.nextLine();
        String jarName = "";
        String subFolder = "mods/";

        switch (choice) {
            case "1" -> jarName = "xfLoader-fabric.jar";
            case "2" -> jarName = "xfLoader-forge.jar";
            case "3" -> {
                jarName = "xfLoader-agent.jar";
                subFolder = "";
            }
            default -> {
                System.out.println("输入错误，默认安装 Fabric 版。");
                jarName = "xfLoader-fabric.jar";
            }
        }

        try {
            Path targetDir = mcDir.resolve(subFolder);
            Files.createDirectories(targetDir);
            Path targetJar = targetDir.resolve(jarName);
            
            System.out.println("正在下载 " + jarName + " ...");
            Downloader.download(BASE_URL + jarName, targetJar, System.out::println);
            
            if (choice.equals("3")) {
                patchAgent(mcDir, targetJar);
            } else {
                System.out.println("安装完成！请启动游戏。");
            }
        } catch (Exception e) {
            System.err.println("安装失败: " + e);
        }
    }

    private static void patchAgent(Path mcDir, Path jar) throws Exception {
        Path versionJson = mcDir.resolve("versions/26.3/26.3.json");
        if (!Files.exists(versionJson)) {
            System.out.println("未找到 26.3.json，请先用官方启动器启动一次。");
            return;
        }
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject root = JsonParser.parseString(Files.readString(versionJson)).getAsJsonObject();
        JsonArray jvm = root.getAsJsonObject("arguments").getAsJsonArray("jvm");
        
        String agentArg = "-javaagent:" + jar.toAbsolutePath();
        for (JsonElement el : jvm) {
            if (el.getAsString().contains("-javaagent:")) {
                System.out.println("已有 -javaagent 参数，跳过。");
                return;
            }
        }
        
        jvm.add(agentArg);
        Files.writeString(versionJson, gson.toJson(root));
        System.out.println("Agent 注入成功！");
    }
}
