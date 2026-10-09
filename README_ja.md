# xfLoader

[English](README.md) | [简体中文](README_zh-CN.md) | [繁體中文](README_zh-TW.md) | [日本語](README_ja.md) | [한국어](README_ko.md) | [Deutsch](README_de.md) | [Français](README_fr.md) | [Español](README_es.md) | [Русский](README_ru.md) | [Português](README_pt.md)

Minecraft Java 版向けの自作データパックローダー。
Java Agent + ASM バイトコード注入で実装。
Fabric / NeoForge 不要でバニラに直接フックします。

## 機能

- バージョンルートのデータパックを自動ロード：
  .minecraft/versions/<バージョン>/datapack/ に zip を置くと、
  起動時にゲームのデータパックシステムへ自動注入され、
  全ワールドで有効。
- 内蔵データパック：jar 内の xf_datapacks/ にあるデータパックは
  jar と一緒に配布され、起動時に datapack/ に展開されて
  外部データパックと一緒にロードされる。
- 早期注入：ワールドロード前に PackRepository にソースを追加し、
  ロード遅延による警告を回避。
- コマンド /xfloader：プレースホルダ。
  /xfloader debug <true|false> でデバッグログ切替。

## 対応バージョン

- Minecraft Java 26.3

## 使い方

1. xiaofang.jar を .minecraft/versions/<バージョン>/ に配置。
2. ランチャーの JVM 引数に追加：
   -javaagent:/絶対パス/xiaofang.jar
3. ゲームを起動し、任意のワールドへ入る。

## ビルド

bash で build.sh を実行。JDK 17+ と libs/ 内の ASM 9.8+ が必要。

## ライセンス

CC BY-NC-SA 4.0 - 表示 · 非営利 · 継承。
商用利用は禁止です。

## 作者

xiaofangii2 - https://github.com/xiaofangii2
