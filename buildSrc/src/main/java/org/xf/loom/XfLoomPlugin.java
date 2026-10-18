package org.xf.loom;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XfLoomPlugin implements Plugin<Project> {

    private static final String MANIFEST_URL =
        "https://piston-meta.mojang.com/mc/game/version_manifest_v2.json";

    @Override
    public void apply(Project project) {
        XfLoomExtension ext = project.getExtensions()
            .create("xfLoom", XfLoomExtension.class);

        project.afterEvaluate(p -> {
            String version = ext.minecraftVersion;
            if (version == null || version.isEmpty()) {
                p.getLogger().warn("[xf-loom] 未设置 xfLoom.minecraftVersion, 跳过");
                return;
            }

            try {
                // 固定到项目根目录的 libs/minecraft.jar
                Path rootDir = p.getRootProject().getProjectDir().toPath();
                Path libsDir = rootDir.resolve("libs");
                Files.createDirectories(libsDir);
                Path jar = libsDir.resolve("minecraft.jar");

                p.getLogger().lifecycle("[xf-loom] 目标文件: " + jar);

                if (Files.exists(jar)) {
                    long size = Files.size(jar);
                    p.getLogger().lifecycle("[xf-loom] 文件已存在 (" + size + " bytes), 跳过下载");
                } else {
                    p.getLogger().lifecycle("[xf-loom] 文件不存在, 开始下载 " + version + " ...");
                    downloadMinecraft(version, jar, p);
                    long size = Files.size(jar);
                    p.getLogger().lifecycle("[xf-loom] 下载完成 (" + size + " bytes)");
                }

                p.getDependencies().add("compileOnly", p.files(jar));
                p.getLogger().lifecycle("[xf-loom] 已添加 compileOnly: " + jar);

            } catch (Exception e) {
                throw new RuntimeException("[xf-loom] 失败", e);
            }
        });
    }

    private void downloadMinecraft(String version, Path out, Project p) throws Exception {
        String manifest = httpGet(MANIFEST_URL);
        String versionUrl = findVersionUrl(manifest, version);
        if (versionUrl == null) throw new RuntimeException("找不到版本: " + version);
        p.getLogger().lifecycle("[xf-loom] 版本 json: " + versionUrl);

        String versionJson = httpGet(versionUrl);
        String clientUrl = findClientUrl(versionJson);
        if (clientUrl == null) throw new RuntimeException("版本 json 里找不到 client 下载地址");
        p.getLogger().lifecycle("[xf-loom] client jar: " + clientUrl);

        downloadFile(clientUrl, out, p);
    }

    private String findVersionUrl(String manifest, String version) {
        Pattern pat = Pattern.compile(
            "\\{[^{}]*\"id\"\\s*:\\s*\"" + Pattern.quote(version) + "\"[^{}]*\\}");
        Matcher m = pat.matcher(manifest);
        if (!m.find()) return null;
        Matcher u = Pattern.compile("\"url\"\\s*:\\s*\"([^\"]+)\"").matcher(m.group());
        return u.find() ? u.group(1) : null;
    }

    private String findClientUrl(String versionJson) {
        Matcher m = Pattern.compile(
            "\"client\"\\s*:\\s*\\{[^{}]*\"url\"\\s*:\\s*\"([^\"]+)\"")
            .matcher(versionJson);
        return m.find() ? m.group(1) : null;
    }

    private String httpGet(String urlStr) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setRequestProperty("User-Agent", "xf-loom/1.0");
        try (InputStream in = conn.getInputStream()) {
            return new String(in.readAllBytes());
        }
    }

    private void downloadFile(String urlStr, Path out, Project p) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setRequestProperty("User-Agent", "xf-loom/1.0");

        long total = conn.getContentLengthLong();
        try (InputStream in = conn.getInputStream();
             OutputStream os = Files.newOutputStream(out)) {
            byte[] buf = new byte[8192];
            long read = 0;
            int n;
            long lastLog = 0;
            while ((n = in.read(buf)) != -1) {
                os.write(buf, 0, n);
                read += n;
                if (read - lastLog > 10 * 1024 * 1024) {
                    p.getLogger().lifecycle(String.format(
                        "[xf-loom]   已下载 %.1f / %.1f MB",
                        read / 1048576.0,
                        total > 0 ? total / 1048576.0 : -1));
                    lastLog = read;
                }
            }
        }
    }
}
