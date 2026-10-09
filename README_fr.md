# xfLoader

[English](README.md) | [简体中文](README_zh-CN.md) | [繁體中文](README_zh-TW.md) | [日本語](README_ja.md) | [한국어](README_ko.md) | [Deutsch](README_de.md) | [Français](README_fr.md) | [Español](README_es.md) | [Русский](README_ru.md) | [Português](README_pt.md)

Un chargeur de datapacks fait maison pour Minecraft Java Edition.
Implémenté avec Java Agent + injection de bytecode ASM.
Pas de Fabric, pas de NeoForge - se branche directement sur le vanilla.

## Fonctionnalités

- Chargement automatique depuis la racine de version :
  déposez des fichiers .zip dans .minecraft/versions/<version>/datapack/,
  ils sont injectés dans le système de datapacks au démarrage et
  s'appliquent à tous les mondes.
- Datapacks intégrés : les datapacks dans xf_datapacks/ du jar
  sont extraits vers datapack/ au démarrage et chargés avec
  les datapacks externes.
- Injection précoce : les sources sont ajoutées à PackRepository
  avant le chargement du monde, évitant les avertissements tardifs.
- Commande /xfloader : espace réservé.
  /xfloader debug <true|false> bascule les logs de debug.

## Versions prises en charge

- Minecraft Java 26.3

## Utilisation

1. Placez xiaofang.jar dans .minecraft/versions/<version>/.
2. Ajoutez aux arguments JVM du launcher :
   -javaagent:/chemin/absolu/xiaofang.jar
3. Lancez le jeu et entrez dans n'importe quel monde.

## Compilation

Exécuter build.sh avec bash. Nécessite JDK 17+ et ASM 9.8+ dans libs/.

## Licence

CC BY-NC-SA 4.0 - Attribution · Pas d'utilisation commerciale ·
Partage dans les mêmes conditions.
L'utilisation commerciale est interdite.

## Auteur

xiaofangii2 - https://github.com/xiaofangii2
