# mod-template

Starting point for a client mod with Gradle. Copy the folder, rename the package/ids and run:

```
export OPENVOXEL_GAME_JAR=/path/to/openvoxel-0.0.8-Debug.jar          # or ../../ModLoader-Client/game/openvoxel.jar
export OPENVOXEL_MODLOADER_JAR=/path/to/ModLoader-v1.0.0-vgame-0.0.8-client.jar
gradle jar        # or use the wrapper from ModLoader-Client: ../../ModLoader-Client/gradlew -p . jar
```

The jar lands in `../../ModLoader-Client/instance/mods` when that folder exists, otherwise in `build/libs`.
No Gradle? `../common-build.sh mod-template my-mod.jar` works too (sources under `src/main/java`, resources under `src/main/resources`).
