# xfLoader

[English](README.md) | [简体中文](README_zh-CN.md) | [繁體中文](README_zh-TW.md) | [日本語](README_ja.md) | [한국어](README_ko.md) | [Deutsch](README_de.md) | [Français](README_fr.md) | [Español](README_es.md) | [Русский](README_ru.md) | [Português](README_pt.md)

自制 Minecraft Java 版数据包加载器。
基于 Java Agent + ASM 字节码注入实现，无需 Fabric / NeoForge，
直接给原版客户端挂载。

## 功能

- 版本根目录数据包自动加载：
  在 .minecraft/versions/<版本>/datapack/ 放 zip，
  启动时自动注入到游戏数据包系统，所有世界生效。
- 内置数据包：jar 内 xf_datapacks/ 下的数据包随 jar 分发，
  启动时解压到 datapack/，与外部数据包一起加载。
- 早期注入：在世界加载之前把数据包塞进 PackRepository，
  避免进入世界时加载过晚。
- 命令 /xfloader：占位命令，
  /xfloader debug <true|false> 切换调试日志。

## 支持版本

- Minecraft Java 26.3

## 使用方式

1. 把 xiaofang.jar 放到 .minecraft/versions/<版本名>/。
2. 启动器 JVM 参数里加：
   -javaagent:/绝对路径/xiaofang.jar
3. 启动游戏，进任意世界即可。

## 构建

用 bash 运行 build.sh，需要 JDK 17+ 和 libs/ 下的 ASM 9.8+。

## 协议

CC BY-NC-SA 4.0 — 署名 · 非商业性使用 · 相同方式共享。
禁止商用。

## 作者

xiaofangii2 - https://github.com/xiaofangii2
