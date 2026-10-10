#!/data/data/com.termux/files/usr/bin/bash
cd ~/myloader

./gradlew clean build --console=plain || exit 1

mkdir -p build/libs
cp loader/build/libs/xfLoader.jar build/libs/
cp installer/build/libs/xfLoader-install.jar build/libs/
