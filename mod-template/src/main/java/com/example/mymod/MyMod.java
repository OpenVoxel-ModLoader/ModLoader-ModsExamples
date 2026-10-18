package com.example.mymod;

import com.happyuky7.openvoxelmoding.loader.ClientMod;
import com.happyuky7.openvoxelmoding.loader.ModConfig;
import com.happyuky7.openvoxelmoding.loader.ModContext;

/** Template: native block, command, translation, recipe, persistent config and a per-frame hook. Copy and rename. */
public final class MyMod implements ClientMod {
    private ModConfig config;
    private long lastLog;

    @Override
    public void onLoad(ModContext ctx) {
        config = ctx.config("mymod");                       // instance/config/mymod.properties
        int launches = config.getInt("launches", 0) + 1;
        config.set("launches", launches).save();
        ctx.log("My Mod loaded, launch #" + launches);

        // A real engine block (Fabric/Forge-style registration). Must be registered in onLoad.
        // Install the same jar on a ModLoader server (mods/ + agent/) to use it in multiplayer.
        ctx.nativeBlock("mymod:example_block")
           .name("Example Block")
           .texture("assets/mymod/textures/example_block.png")
           .hardness(1.5f).tool("pickaxe", false)
           .light(6)
           .register();
        ctx.registerRecipe("mymod:example_block", 4, new String[]{"SS", "SS"}, "S", "stone");

        if (ctx.isServer()) return;                          // on a dedicated server only blocks matter

        ctx.registerCommand("hello", ctx.translate("mymod.command"), args -> {
            String name = args.length > 0 ? String.join(" ", args) : "player";
            ctx.chat(ctx.translate("mymod.hello", name));
            ctx.playSound("pop");
        });
        ctx.registerCommand("where", "show your coordinates", args -> {
            if (!ctx.isInWorld()) return;
            ctx.chat("&7You are at &e" + format(ctx.getPlayerPosition()));
        });
    }

    private static String format(org.joml.Vector3d p) { return String.format(java.util.Locale.ROOT, "%.1f, %.1f, %.1f", p.x, p.y, p.z); }

    @Override
    public void onGameReady(ModContext ctx) {
        // Engine is available here: ctx.engine().getEventManager().register(ClickEvent.class, e -> ...)
        ctx.registerRecipe("golden_apple", 1, new String[]{"GGG", "GAG", "GGG"}, "G", "gold_ingot", "A", "apple");
        ctx.chat("&7Try &e/give mymod:example_block 16");
    }

    @Override
    public void onFrame(ModContext ctx) {
        if (!ctx.isInWorld()) return;
        // onFrame runs every rendered frame (hundreds per second): use time, never frame counts,
        // and keep chat for things the player asked for. Here: write the position to the log once a minute.
        long now = System.currentTimeMillis();
        if (now - lastLog < 60_000) return;
        lastLog = now;
        ctx.log("Player at " + format(ctx.getPlayerPosition()));
    }

    @Override
    public void onShutdown(ModContext ctx) { config.save(); }
}
