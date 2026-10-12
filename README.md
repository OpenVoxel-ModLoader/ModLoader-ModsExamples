# OpenVoxel ModLoader — example client mods

Example mods for [ModLoader-Client](https://github.com/OpenVoxel-ModLoader/ModLoader-Client).
Each folder is a complete mod; the JARs are also attached to the Releases of this repo.

| Mod | Shows |
|-----|-------|
| `hello-mod` | Lifecycle hooks, chat messages with `&` colors, a `/hello` command, translations (`assets/hello/lang/*.json`). |
| `hud-clock` | Drawing on the HUD every frame (`onFrame`) with the engine's `GLUIRenderer`: clock, coordinates and FPS. |
| `block-counter` | Listening to engine events (`ClickEvent`) through `VoxelEngine.getEventManager()` and reading world blocks. |

## Install

Copy a JAR into `ModLoader-Client/instance/mods` and start the game with the loader.

## Build

```
build-all.bat   /   ./build-all.sh        # every mod -> dist/
hello-mod\build.bat                        # one mod
```

Requirements: JDK 11+, the game JAR (`OPENVOXEL_GAME_JAR`, downloaded by the official launcher)
and the loader JAR (`OPENVOXEL_MODLOADER_JAR`, defaults to `../ModLoader-Client/dist/ModLoader-v1.0.0-vgame-0.0.8-client.jar`).
When `../ModLoader-Client/instance/mods` exists the JARs are copied there too.

## Anatomy of a mod

```
my-mod/
  build.bat / build.sh          calls ../common-build.(bat|sh) <dir> <jar>
  src/.../MyMod.java            implements com.happyuky7.openvoxelmoding.loader.ClientMod
  resources/openvoxel.mod.properties
  resources/assets/<modid>/lang/en_us.json   optional translations
```

`openvoxel.mod.properties`:

```properties
id=hello
name=Hello Mod
version=1.0.0
main=com.happyuky7.examples.hello.HelloMod
```

## API summary (`ModContext`)

| Method | Purpose |
|--------|---------|
| `chat(String)` | Print a line in the chat (supports `&a` / `&#rrggbb` colors). |
| `registerCommand(name, description, handler)` | Client `/command`; listed by `/chelp` and Tab-completed. |
| `translate(key, args...)` / `language()` | Translations from the loader, the mod's `assets/<id>/lang` and `instance/lang`. |
| `getEngine(VoxelEngine.class)` | The engine once `onGameReady` fired (world, camera, renderer, event manager). |
| `getInstanceDirectory()` / `getConfigDirectory()` | Where to store files. |
| `isCreative()` | Current game mode. |

`ClientMod` hooks: `onLoad` (JVM start), `onGameReady` (engine created), `onFrame` (each frame, main thread), `onShutdown`.
Full docs: https://openvoxel-modloader.github.io/
