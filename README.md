# xfLoader

A homemade datapack loader for Minecraft Java Edition 26.3.
Built with Java Agent + ASM bytecode injection.
No Fabric, no NeoForge - hooks directly into vanilla.

## Modules

- **loader** - the actual loader (`xfLoader.jar`). Attach via `-javaagent`.
- **installer** - auto-installer (`xfLoader-install.jar`). Detects the
  official launcher, downloads the loader, patches `26.3.json` to add
  `-javaagent`.

## Features

- Auto-load datapacks from `.minecraft/versions/26.3/datapack/`
- Built-in datapacks under `xf_datapacks/` inside the jar
- Injects sources into `PackRepository` before world load
- Executes `/reload` on player join so datapacks take effect
- `/xfloader` and `/xfloader debug <bool>` commands
- Config file at `.minecraft/versions/26.3/config/xfloader.json`

## Installation

### Easy way (installer)

1. Download `xfLoader-install.jar` from the latest release.
2. Make sure you have started Minecraft 26.3 with the official launcher
   at least once (so `versions/26.3/26.3.json` exists).
3. Run `java -jar xfLoader-install.jar`.
4. Launch 26.3 from the official launcher.

### Manual way

1. Download `xfLoader.jar`.
2. Put it in `.minecraft/versions/26.3/`.
3. Edit `.minecraft/versions/26.3/26.3.json`, add to `arguments.jvm`:
   `-javaagent:/absolute/path/to/xfLoader.jar`
4. Launch 26.3.

## Build

Requires JDK 17+, Gradle wrapper included.

Run build.sh with bash, or `./gradlew clean build`.
Output goes to `build/libs/`:
- `xfLoader.jar`
- `xfLoader-install.jar`

## License

CC BY-NC-SA 4.0 - Attribution, NonCommercial, ShareAlike.
Commercial use is prohibited.

## Author

xiaofangii2 - https://github.com/xiaofangii2
