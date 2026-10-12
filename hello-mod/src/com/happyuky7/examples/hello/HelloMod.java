package com.happyuky7.examples.hello;

import com.happyuky7.openvoxelmoding.loader.ClientMod;
import com.happyuky7.openvoxelmoding.loader.ModContext;
import net.openvoxel.client.ClientMain;
import net.openvoxel.client.SessionInfo;

/** Smallest useful mod: a chat message on startup, a command and translated strings. */
public final class HelloMod implements ClientMod {
    private boolean greeted;

    @Override
    public void onLoad(ModContext context) {
        context.log("HelloMod loaded, language " + context.language());
        context.registerCommand("hello", context.translate("hello.command"), args -> {
            SessionInfo session = ClientMain.getSession();
            String name = args.length > 0 ? String.join(" ", args) : (session != null && session.username != null ? session.username : "player");
            context.chat(context.translate("hello.greeting", name, context.isCreative() ? "creative" : "survival"));
        });
    }

    @Override
    public void onFrame(ModContext context) {
        // The chat only exists inside a world; greet once the first time we see one.
        net.openvoxel.engine.VoxelEngine engine = context.getEngine(net.openvoxel.engine.VoxelEngine.class);
        if (!greeted && engine != null && engine.isWorldActive()) {
            greeted = true;
            context.chat(context.translate("hello.ready"));
        }
        if (engine != null && !engine.isWorldActive()) greeted = false;
    }
}
