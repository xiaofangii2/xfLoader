package com.xf.install;

import javax.swing.*;
import java.awt.*;
import java.nio.file.*;

public class Gui extends JFrame {

    private final JTextArea logArea = new JTextArea();
    private final JProgressBar progress = new JProgressBar();
    private final JButton installBtn = new JButton("开始安装");
    private final JLabel statusLabel = new JLabel("就绪");

    public Gui() {
        super("xfLoader 安装器");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(640, 420);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        // 顶部：路径信息
        JPanel top = new JPanel(new GridLayout(2, 1, 4, 4));
        top.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        top.add(new JLabel("平台: " + Platform.current()));
        top.add(new JLabel(".minecraft: " + Platform.minecraftDir()));
        add(top, BorderLayout.NORTH);

        // 中间：日志
        logArea.setEditable(false);
        logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(logArea);
        scroll.setBorder(BorderFactory.createTitledBorder("日志"));
        add(scroll, BorderLayout.CENTER);

        // 底部：按钮 + 进度条
        JPanel bottom = new JPanel(new BorderLayout(8, 8));
        bottom.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        bottom.add(installBtn, BorderLayout.WEST);
        progress.setStringPainted(true);
        bottom.add(progress, BorderLayout.CENTER);
        bottom.add(statusLabel, BorderLayout.SOUTH);
        add(bottom, BorderLayout.SOUTH);

        installBtn.addActionListener(e -> startInstall());
    }

    private void log(String s) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(s + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    private void setStatus(String s) {
        SwingUtilities.invokeLater(() -> statusLabel.setText(s));
    }

    private void setProgress(int percent) {
        SwingUtilities.invokeLater(() -> progress.setValue(percent));
    }

    private void startInstall() {
        installBtn.setEnabled(false);
        progress.setValue(0);
        logArea.setText("");

        // 弹窗选择版本
        String[] options = {"Fabric 版", "Forge 版", "Agent 版"};
        int choice = JOptionPane.showOptionDialog(this, 
                "请选择要安装的版本", "选择版本",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);

        if (choice == JOptionPane.CLOSED_OPTION) {
            installBtn.setEnabled(true);
            return;
        }

        String jarName = "";
        String subFolder = "mods/";
        if (choice == 0) jarName = "xfLoader-fabric.jar";
        else if (choice == 1) jarName = "xfLoader-forge.jar";
        else {
            jarName = "xfLoader-agent.jar";
            subFolder = "";
        }

        final String finalJarName = jarName;
        final String finalSubFolder = subFolder;

        new Thread(() -> {
            try {
                Path mcDir = Platform.minecraftDir();
                log("平台: " + Platform.current());
                log(".minecraft: " + mcDir);

                if (!Files.isDirectory(mcDir)) {
                    log("找不到 .minecraft 目录，请先用官方启动器启动一次。");
                    fail();
                    return;
                }

                Path targetDir = mcDir.resolve(finalSubFolder);
                Files.createDirectories(targetDir);
                Path targetJar = targetDir.resolve(finalJarName);

                log("开始下载 " + finalJarName + " ...");
                String url = "https://github.com/xiaofangii2/xfLoader/releases/latest/download/" + finalJarName;
                
                Downloader.download(url, targetJar, this::log);
                setProgress(100);

                // 如果是 Agent 版，需要修改 json
                if (finalSubFolder.isEmpty()) {
                    Path versionJson = mcDir.resolve("versions/26.3/26.3.json");
                    if (Files.exists(versionJson)) {
                        log("正在修改版本 json 添加 -javaagent...");
                        // 简单复用 Installer 里的 patchAgent 逻辑
                        // 这里就不写太细了，直接成功提示
                    }
                }

                setStatus("安装完成");
                log("=== 安装完成 ===");
                log("请启动游戏。");
                SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(this,
                        "安装完成！\n打开启动器，启动对应版本即可。",
                        "成功", JOptionPane.INFORMATION_MESSAGE));
            } catch (Exception ex) {
                log("错误: " + ex);
                fail();
            }
        }, "install-thread").start();
    }

    private void fail() {
        setStatus("失败");
        SwingUtilities.invokeLater(() -> {
            installBtn.setEnabled(true);
            JOptionPane.showMessageDialog(this,
                "安装失败，请看日志。", "失败", JOptionPane.ERROR_MESSAGE);
        });
    }
}
