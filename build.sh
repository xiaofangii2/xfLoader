#!/data/data/com.termux/files/usr/bin/bash
cd ~/myloader

TARGET="$1"

if [ "$TARGET" = "-loader" ]; then
    ./gradlew :loader:clean :loader:build --console=plain --no-daemon || exit 1
elif [ "$TARGET" = "-installer" ]; then
    ./gradlew :installer:clean :installer:build --console=plain --no-daemon || exit 1
elif [ "$TARGET" = "-forge" ]; then
    ./gradlew :forge:clean :forge:build --console=plain --no-daemon || exit 1
else
    ./gradlew clean build --console=plain --no-daemon || exit 1
fi

mkdir -p build/libs

if [ "$TARGET" = "-loader" ]; then
    cp loader/build/libs/xfLoader-agent.jar build/libs/
    cp loader/build/libs/xfLoader-fabric.jar build/libs/
elif [ "$TARGET" = "-installer" ]; then
    cp installer/build/libs/xfLoader-install.jar build/libs/
elif [ "$TARGET" = "-forge" ]; then
    cp forge/build/libs/xfLoader-forge.jar build/libs/
else
    cp loader/build/libs/xfLoader-agent.jar build/libs/
    cp loader/build/libs/xfLoader-fabric.jar build/libs/
    cp installer/build/libs/xfLoader-install.jar build/libs/
    cp forge/build/libs/xfLoader-forge.jar build/libs/
fi

# 自动分发
# Fabric 版
mkdir -p "/storage/emulated/0/FCL/.minecraft/versions/26.3/mods"
cp loader/build/libs/xfLoader-fabric.jar "/storage/emulated/0/FCL/.minecraft/versions/26.3/mods/"

# Agent 版
cp loader/build/libs/xfLoader-agent.jar "/storage/emulated/0/FCL/.minecraft/versions/26.3 xf/xiaofang.jar"

# Forge 版
mkdir -p "/storage/emulated/0/FCL/.minecraft/versions/26.3-Forge/mods"
cp forge/build/libs/xfLoader-forge.jar "/storage/emulated/0/FCL/.minecraft/versions/26.3-Forge/mods/"
