# xfLoader

[English](README.md) | [简体中文](README_zh-CN.md) | [繁體中文](README_zh-TW.md) | [日本語](README_ja.md) | [한국어](README_ko.md) | [Deutsch](README_de.md) | [Français](README_fr.md) | [Español](README_es.md) | [Русский](README_ru.md) | [Português](README_pt.md)

自製 Minecraft Java 版資料包載入器。
以 Java Agent + ASM 位元組碼注入實作，
無需 Fabric / NeoForge，直接掛載到原版客戶端。

## 功能

- 版本根目錄資料包自動載入：
  在 .minecraft/versions/<版本>/datapack/ 放 zip，
  啟動時自動注入遊戲資料包系統，所有世界生效。
- 內建資料包：jar 內 xf_datapacks/ 下的資料包隨 jar 分發，
  啟動時解壓到 datapack/，與外部資料包一起載入。
- 早期注入：在世界載入之前把資料包塞進 PackRepository，
  避免載入過晚。
- 指令 /xfloader：佔位指令，
  /xfloader debug <true|false> 切換除錯日誌。

## 支援版本

- Minecraft Java 26.3

## 使用方式

1. 將 xiaofang.jar 放到 .minecraft/versions/<版本名>/。
2. 啟動器 JVM 參數加入：
   -javaagent:/絕對路徑/xiaofang.jar
3. 啟動遊戲，進入任意世界即可。

## 建置

用 bash 執行 build.sh，需要 JDK 17+ 與 libs/ 下的 ASM 9.8+。

## 授權

CC BY-NC-SA 4.0 — 姓名標示 · 非商業性 · 相同方式分享。
禁止商業使用。

## 作者

xiaofangii2 - https://github.com/xiaofangii2
