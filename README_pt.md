# xfLoader

[English](README.md) | [简体中文](README_zh-CN.md) | [繁體中文](README_zh-TW.md) | [日本語](README_ja.md) | [한국어](README_ko.md) | [Deutsch](README_de.md) | [Français](README_fr.md) | [Español](README_es.md) | [Русский](README_ru.md) | [Português](README_pt.md)

Um carregador de datapacks caseiro para Minecraft Java Edition.
Implementado com Java Agent + injeção de bytecode ASM.
Sem Fabric, sem NeoForge - conecta-se diretamente ao vanilla.

## Recursos

- Carregamento automático da raiz de versão:
  coloque arquivos .zip em .minecraft/versions/<versão>/datapack/;
  eles são injetados no sistema de datapacks na inicialização e
  valem para todos os mundos.
- Datapacks embutidos: datapacks em xf_datapacks/ dentro do jar
  são extraídos para datapack/ na inicialização e carregados junto
  com os externos.
- Injeção precoce: as fontes são adicionadas ao PackRepository
  antes do carregamento do mundo, evitando avisos tardios.
- Comando /xfloader: placeholder.
  /xfloader debug <true|false> alterna os logs de depuração.

## Versões suportadas

- Minecraft Java 26.3

## Uso

1. Coloque xiaofang.jar em .minecraft/versions/<versão>/.
2. Adicione aos argumentos JVM do launcher:
   -javaagent:/caminho/absoluto/xiaofang.jar
3. Inicie o jogo e entre em qualquer mundo.

## Compilar

Execute build.sh com bash. Requer JDK 17+ e ASM 9.8+ em libs/.

## Licença

CC BY-NC-SA 4.0 - Atribuição · Não comercial · Compartilha igual.
Uso comercial é proibido.

## Autor

xiaofangii2 - https://github.com/xiaofangii2
