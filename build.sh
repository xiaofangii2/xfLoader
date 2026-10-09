#!/data/data/com.termux/files/usr/bin/bash
cd ~/myloader

START=$(date +%s%3N)

echo "> Configure project :"
echo "xiaofang-loader v1.0.0"

echo "> Task :compileJava"
rm -rf out fat
mkdir -p out
BRIG="/storage/emulated/0/FCL/.minecraft/libraries/com/mojang/brigadier/1.3.11/brigadier-1.3.11.jar"
javac -cp "libs/asm-9.8.jar:$BRIG" -d out $(find src -name "*.java") || exit 1

echo "> Task :processResources"
mkdir -p fat
cd fat
unzip -o ../libs/asm-9.8.jar -d . > /dev/null
rm -rf META-INF
cp -r ../out/com ./com
cp ../META-INF/MANIFEST.MF .
if [ -d ../xf_datapacks ]; then
    cp -r ../xf_datapacks ./xf_datapacks
    echo "  copied xf_datapacks"
fi
cd ..

echo "> Task :jar"
cd fat
jar cfm ~/myloader/xiaofang-fat.jar MANIFEST.MF com org
if [ -d xf_datapacks ]; then
    jar uf ~/myloader/xiaofang-fat.jar xf_datapacks
    echo "  added xf_datapacks to jar"
fi
cd ..

echo "> Task :copyToFcl"
cp ~/myloader/xiaofang-fat.jar "/storage/emulated/0/FCL/.minecraft/versions/26.3 xf/xiaofang.jar"

END=$(date +%s%3N)
echo ""
echo "BUILD SUCCESSFUL in $((END-START))ms"
echo "5 actionable tasks: 5 executed"
