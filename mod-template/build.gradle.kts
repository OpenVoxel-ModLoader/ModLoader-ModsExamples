plugins {
    java
}

group = "com.example"
version = "0.1.0"

// Both jars are compile-only: the loader injects itself at runtime and the game is on the classpath.
val gameJar = System.getenv("OPENVOXEL_GAME_JAR") ?: "../../ModLoader-Client/game/openvoxel.jar"
val loaderJar = System.getenv("OPENVOXEL_MODLOADER_JAR") ?: "../../ModLoader-Client/dist/ModLoader-v1.0.0-vgame-0.0.8-client.jar"

repositories { mavenCentral() }

dependencies {
    compileOnly(files(gameJar))
    compileOnly(files(loaderJar))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release.set(11)
}

tasks.jar {
    archiveFileName.set("my-mod-${version}.jar")
    // Drop the jar straight into the instance when it exists next to this repo.
    val mods = file("../../ModLoader-Client/instance/mods")
    if (mods.isDirectory) destinationDirectory.set(mods)
}
