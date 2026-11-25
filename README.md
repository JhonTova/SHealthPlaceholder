<div align="center">

# SHealthPlaceholder

### Placeholders de vida, de verdad completos.

La vida real de un jugador — incluyendo absorción y efectos — lista para tus HUD, tab y scoreboards.

[![build](https://github.com/JhonTova/SHealthPlaceholder/actions/workflows/build.yml/badge.svg)](https://github.com/JhonTova/SHealthPlaceholder/actions/workflows/build.yml) [![release](https://img.shields.io/github/v/release/JhonTova/SHealthPlaceholder?label=release)](https://github.com/JhonTova/SHealthPlaceholder/releases/latest) ![Minecraft](https://img.shields.io/badge/Minecraft-1.21.11%2B-62B47A) ![Java](https://img.shields.io/badge/Java-21-E76F00)

</div>

---

## Características

- **Placeholders de vida** precisos para usar en cualquier plugin con PlaceholderAPI.
- Tiene en cuenta la **absorción** (corazones amarillos) y los **efectos de poción**.
- Ideal para **scoreboards, tab, holograms** y HUDs personalizados.
- Ligero y sin configuración complicada.

## Requisitos

Requiere **[SiriusCore](https://github.com/JhonTova/SiriusCore)** y **PlaceholderAPI**.

## Instalación

`SiriusCore.jar` + `SHealthPlaceholder.jar` en `plugins/` (con PlaceholderAPI). Spigot / Paper / Purpur 1.21.11+.

## Descarga

El `.jar` de cada versión está en [Releases](https://github.com/JhonTova/SHealthPlaceholder/releases/latest).

## Compilar desde el código

Requiere Java 21 y Maven. SiriusCore no está en un repositorio Maven público, así que
primero se instala en local la versión que declara el `pom.xml`:

```bash
git clone --branch v1.1.1 https://github.com/JhonTova/SiriusCore.git
mvn -f SiriusCore/pom.xml install -DskipTests
mvn package        # en este repositorio; el .jar queda en target/
```

Cada push y pull request se compila en GitHub Actions. Al subir una tag `v*`, el `.jar`
se adjunta solo a la release.
