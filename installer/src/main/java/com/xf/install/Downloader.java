package com.xf.install;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.function.Consumer;

public class Downloader {
    public static void download(String url, Path out) throws IOException {
        download(url, out, System.out::println);
    }

    public static void download(String url, Path out, Consumer<String> log) throws IOException {
        Files.createDirectories(out.getParent());
        log.accept("  下载: " + url);

        URLConnection conn = new URL(url).openConnection();
        conn.setRequestProperty("User-Agent", "xfLoader-install");

        long total = conn.getContentLengthLong();
        try (InputStream in = conn.getInputStream();
             OutputStream os = Files.newOutputStream(out,
                 StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {

            byte[] buf = new byte[8192];
            long read = 0;
            int n;
            while ((n = in.read(buf)) != -1) {
                os.write(buf, 0, n);
                read += n;
                if (total > 0 && read % (1024 * 1024) < 8192) {
                    log.accept(String.format("  已下载 %.1f / %.1f MB",
                        read / 1048576.0, total / 1048576.0));
                }
            }
        }
        log.accept("  完成: " + Files.size(out) + " bytes");
    }
}
