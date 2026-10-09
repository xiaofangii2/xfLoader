# xfLoader

[English](README.md) | [简体中文](README_zh-CN.md) | [繁體中文](README_zh-TW.md) | [日本語](README_ja.md) | [한국어](README_ko.md) | [Deutsch](README_de.md) | [Français](README_fr.md) | [Español](README_es.md) | [Русский](README_ru.md) | [Português](README_pt.md)

Самодельный загрузчик датапаков для Minecraft Java Edition.
Реализован с помощью Java Agent + ASM-инъекции байт-кода.
Без Fabric, без NeoForge - напрямую встраивается в ванильную игру.

## Возможности

- Автозагрузка датапаков из корня версии:
  положите .zip в .minecraft/versions/<версия>/datapack/, и они
  будут автоматически внедрены в систему датапаков при запуске
  и действовать во всех мирах.
- Встроенные датапаки: датапаки в xf_datapacks/ внутри jar
  распаковываются в datapack/ при запуске и загружаются вместе
  с внешними.
- Ранняя инъекция: источники добавляются в PackRepository
  до загрузки мира, что предотвращает поздние предупреждения.
- Команда /xfloader: заглушка.
  /xfloader debug <true|false> переключает отладочные логи.

## Поддерживаемые версии

- Minecraft Java 26.3

## Использование

1. Поместите xiaofang.jar в .minecraft/versions/<версия>/.
2. Добавьте в JVM-аргументы лаунчера:
   -javaagent:/абсолютный/путь/xiaofang.jar
3. Запустите игру и войдите в любой мир.

## Сборка

Запустить build.sh через bash. Требуется JDK 17+ и ASM 9.8+ в libs/.

## Лицензия

CC BY-NC-SA 4.0 - С указанием авторства · Некоммерческая ·
С сохранением условий.
Коммерческое использование запрещено.

## Автор

xiaofangii2 - https://github.com/xiaofangii2
