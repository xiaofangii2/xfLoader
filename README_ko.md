# xfLoader

[English](README.md) | [简体中文](README_zh-CN.md) | [繁體中文](README_zh-TW.md) | [日本語](README_ja.md) | [한국어](README_ko.md) | [Deutsch](README_de.md) | [Français](README_fr.md) | [Español](README_es.md) | [Русский](README_ru.md) | [Português](README_pt.md)

Minecraft Java Edition용 자체 제작 데이터팩 로더입니다.
Java Agent + ASM 바이트코드 주입으로 구현되었으며,
Fabric / NeoForge 없이 바닐라에 직접 연결됩니다.

## 기능

- 버전 루트의 데이터팩 자동 로드:
  .minecraft/versions/<버전>/datapack/ 에 zip을 넣으면
  시작 시 게임 데이터팩 시스템에 자동 주입되어
  모든 월드에 적용됩니다.
- 내장 데이터팩: jar 내부의 xf_datapacks/ 데이터팩은
  jar와 함께 배포되며, 시작 시 datapack/ 으로 풀려
  외부 데이터팩과 함께 로드됩니다.
- 조기 주입: 월드 로드 전에 PackRepository에 소스를 추가하여
  로드 지연 경고를 방지합니다.
- 명령어 /xfloader: 자리 표시자.
  /xfloader debug <true|false> 로 디버그 로그 전환.

## 지원 버전

- Minecraft Java 26.3

## 사용 방법

1. xiaofang.jar 를 .minecraft/versions/<버전>/ 에 넣습니다.
2. 런처 JVM 인수에 추가:
   -javaagent:/절대경로/xiaofang.jar
3. 게임을 실행하고 아무 월드에 들어갑니다.

## 빌드

bash 로 build.sh 를 실행합니다. JDK 17+ 와 libs/ 안의 ASM 9.8+ 가 필요합니다.

## 라이선스

CC BY-NC-SA 4.0 - 저작자표시 · 비영리 · 동일조건변경허락.
상업적 이용은 금지됩니다.

## 저자

xiaofangii2 - https://github.com/xiaofangii2
