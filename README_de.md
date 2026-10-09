# xfLoader

[English](README.md) | [简体中文](README_zh-CN.md) | [繁體中文](README_zh-TW.md) | [日本語](README_ja.md) | [한국어](README_ko.md) | [Deutsch](README_de.md) | [Français](README_fr.md) | [Español](README_es.md) | [Русский](README_ru.md) | [Português](README_pt.md)

Ein selbstgebauter Datenpaket-Loader für Minecraft Java Edition.
Implementiert mit Java Agent + ASM-Bytecode-Injektion.
Kein Fabric, kein NeoForge - hängt sich direkt ins Vanilla-Spiel.

## Funktionen

- Automatisches Laden aus dem Versionsstamm:
  Lege .zip-Dateien in .minecraft/versions/<Version>/datapack/ ab;
  sie werden beim Start ins Datenpaket-System injiziert und gelten
  für alle Welten.
- Eingebaute Datenpakete: Datenpakete unter xf_datapacks/ im Jar
  werden beim Start nach datapack/ entpackt und zusammen mit
  externen geladen.
- Frühe Injektion: Quellen werden vor dem Weltladen in
  PackRepository eingefügt, um späte Warnungen zu vermeiden.
- Befehl /xfloader: Platzhalter.
  /xfloader debug <true|false> schaltet Debug-Logs um.

## Unterstützte Versionen

- Minecraft Java 26.3

## Verwendung

1. xiaofang.jar nach .minecraft/versions/<Version>/ legen.
2. In den JVM-Argumenten des Launchers hinzufügen:
   -javaagent:/absoluter/pfad/xiaofang.jar
3. Spiel starten und eine beliebige Welt betreten.

## Build

build.sh mit bash ausführen. Benötigt JDK 17+ und ASM 9.8+ in libs/.

## Lizenz

CC BY-NC-SA 4.0 - Namensnennung · Nicht kommerziell ·
Weitergabe unter gleichen Bedingungen.
Kommerzielle Nutzung ist untersagt.

## Autor

xiaofangii2 - https://github.com/xiaofangii2
