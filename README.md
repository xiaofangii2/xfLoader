# xfLoader

[English](README.md) | [简体中文](README_zh-CN.md) | [繁體中文](README_zh-TW.md) | [日本語](README_ja.md) | [한국어](README_ko.md) | [Deutsch](README_de.md) | [Français](README_fr.md) | [Español](README_es.md) | [Русский](README_ru.md) | [Português](README_pt.md)

A homemade datapack loader for Minecraft Java Edition.
Built with Java Agent + ASM bytecode injection.
No Fabric, no NeoForge - hooks directly into vanilla.

## Features

- Auto-load datapacks from version root:
  drop .zip files into .minecraft/versions/<version>/datapack/,
  they are injected into the game's datapack system on startup,
  effective for all worlds.
- Built-in datapacks: datapacks under xf_datapacks/ inside the jar
  are extracted to datapack/ at startup and loaded together with
  external ones.
- Early injection: adds sources to PackRepository before world
  loading, avoiding late-load warnings.
- Command /xfloader: placeholder.
  /xfloader debug <true|false> toggles debug logging.

## Supported versions

- Minecraft Java 26.3

## Usage

1. Place xiaofang.jar into .minecraft/versions/<version>/.
2. Add to launcher JVM args:
   -javaagent:/path/to/xiaofang.jar
3. Launch the game and enter any world.

## Build

Run build.sh with bash. Requires JDK 17+ and ASM 9.8+ in libs/.

## License

CC BY-NC-SA 4.0 - Attribution, NonCommercial, ShareAlike.
Commercial use is prohibited.

## Author

xiaofangii2 - https://github.com/xiaofangii2
