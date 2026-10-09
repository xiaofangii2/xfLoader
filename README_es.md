# xfLoader

[English](README.md) | [简体中文](README_zh-CN.md) | [繁體中文](README_zh-TW.md) | [日本語](README_ja.md) | [한국어](README_ko.md) | [Deutsch](README_de.md) | [Français](README_fr.md) | [Español](README_es.md) | [Русский](README_ru.md) | [Português](README_pt.md)

Un cargador de paquetes de datos casero para Minecraft Java Edition.
Implementado con Java Agent + inyección de bytecode ASM.
Sin Fabric, sin NeoForge - se engancha directamente al vanilla.

## Características

- Carga automática desde la raíz de versión:
  coloca archivos .zip en .minecraft/versions/<versión>/datapack/;
  se inyectan en el sistema de paquetes de datos al iniciar y se
  aplican a todos los mundos.
- Paquetes integrados: los paquetes en xf_datapacks/ dentro del
  jar se extraen a datapack/ al iniciar y se cargan junto con los
  externos.
- Inyección temprana: las fuentes se añaden a PackRepository
  antes de cargar el mundo, evitando avisos tardíos.
- Comando /xfloader: marcador de posición.
  /xfloader debug <true|false> alterna los registros de depuración.

## Versiones compatibles

- Minecraft Java 26.3

## Uso

1. Coloca xiaofang.jar en .minecraft/versions/<versión>/.
2. Añade a los argumentos JVM del launcher:
   -javaagent:/ruta/absoluta/xiaofang.jar
3. Inicia el juego y entra a cualquier mundo.

## Compilar

Ejecutar build.sh con bash. Requiere JDK 17+ y ASM 9.8+ en libs/.

## Licencia

CC BY-NC-SA 4.0 - Atribución · No comercial · Compartir igual.
El uso comercial está prohibido.

## Autor

xiaofangii2 - https://github.com/xiaofangii2
