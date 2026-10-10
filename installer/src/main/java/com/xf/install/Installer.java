package com.xf.install;

import com.google.gson.*;
import java.nio.file.*;

public class Installer {

    // GitHub Release 最新版的 asset, 文件名必须和上传的一致
    private static final String LOADER_URL =
        "https://github.com/xiaofangii2/xfLoader/releases/latest/download/xfLoader.jar";

    public static void install(Path mcDir, Path versionJson) {
        try {
            Path loaderJar = mcDir.resolve("xiaofang.jar");
            Downloader.download(LOADER_URL, loaderJar);

            Path backup = versionJson.resolveSibling(versionJson.getFileName() + ".bak");
            if (!Files.exists(backup)) {
                Files.copy(versionJson, backup);
                System.out.println("已备份: " + backup);
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = Files.readString(versionJson);
            JsonObject root = JsonParser.parseString(json).getAsJsonObject();

            JsonObject args = root.getAsJsonObject("arguments");
            if (args == null) {
                args = new JsonObject();
                root.add("arguments", args);
            }
            JsonArray jvm = args.getAsJsonArray("jvm");
            if (jvm == null) {
                jvm = new JsonArray();
                args.add("jvm", jvm);
            }

            String agentArg = "-javaagent:" + loaderJar.toAbsolutePath();
            boolean exists = false;
            for (JsonElement el : jvm) {
                if (el.isJsonPrimitive() && el.getAsString().startsWith("-javaagent:")) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                jvm.add(agentArg);
                System.out.println("已添加: " + agentArg);
            } else {
                System.out.println("已有 -javaagent, 跳过");
            }

            Files.writeString(versionJson, gson.toJson(root));
            System.out.println("已写入: " + versionJson);

            System.out.println();
            System.out.println("=== 安装完成 ===");
            System.out.println("现在打开官方启动器, 启动 26.3 即可");
        } catch (Exception e) {
            System.err.println("安装失败: " + e);
            e.printStackTrace();
        }
    }
}
