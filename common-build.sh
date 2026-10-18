#!/usr/bin/env bash
# Usage: ./common-build.sh <mod-dir> <jar-name>
# Needs the game JAR (OPENVOXEL_GAME_JAR) and the loader JAR (OPENVOXEL_MODLOADER_JAR or ../ModLoader-Client/dist/*.jar).
set -euo pipefail
MOD="$1"; JARNAME="$2"
ROOT="$(cd "$(dirname "$0")" && pwd)"
GAME="${OPENVOXEL_GAME_JAR:-}"
for c in "$ROOT/../../../Game/versions/0.0.8-Debug/openvoxel-0.0.8-Debug.jar" "$ROOT/../ModLoader-Client/game/openvoxel.jar"; do [ -z "$GAME" ] && [ -f "$c" ] && GAME="$c"; done
[ -n "$GAME" ] || { echo "Game JAR not found; set OPENVOXEL_GAME_JAR." >&2; exit 1; }
LOADER="${OPENVOXEL_MODLOADER_JAR:-}"
if [ -z "$LOADER" ]; then for j in "$ROOT"/../ModLoader-Client/dist/ModLoader-v*-vgame-0.0.8-client.jar; do [ -f "$j" ] && LOADER="$j"; done; fi
[ -f "$LOADER" ] || { echo "Loader JAR not found; set OPENVOXEL_MODLOADER_JAR or build ../ModLoader-Client." >&2; exit 1; }
rm -rf "$MOD/build" && mkdir -p "$MOD/build" "$ROOT/dist"
find "$MOD/src" -name '*.java' > "$MOD/build/sources.txt"
javac --release 11 -encoding UTF-8 -Xlint:-options -classpath "$GAME:$LOADER" -d "$MOD/build" @"$MOD/build/sources.txt"
rm "$MOD/build/sources.txt"
[ -d "$MOD/resources" ] && cp -r "$MOD/resources/." "$MOD/build/"
[ -d "$MOD/src/main/resources" ] && cp -r "$MOD/src/main/resources/." "$MOD/build/"
jar --create --file "$ROOT/dist/$JARNAME" -C "$MOD/build" .
[ -d "$ROOT/../ModLoader-Client/instance/mods" ] && cp -f "$ROOT/dist/$JARNAME" "$ROOT/../ModLoader-Client/instance/mods/"
echo "$JARNAME -> dist (and ../ModLoader-Client/instance/mods if present)"
