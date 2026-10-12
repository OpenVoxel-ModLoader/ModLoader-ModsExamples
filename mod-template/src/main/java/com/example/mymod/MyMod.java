package com.example.mymod;

import com.happyuky7.openvoxelmoding.loader.ClientMod;
import com.happyuky7.openvoxelmoding.loader.ModConfig;
import com.happyuky7.openvoxelmoding.loader.ModContext;

/** Template: command, translation, persistent config and a per-frame hook. Copy and rename. */
public final class MyMod implements ClientMod {
    private ModConfig config;
    private int frames;

    @Override
    public void onLoad(ModContext ctx) {
        config = ctx.config("mymod");                       // instance/config/mymod.properties
        int launches = config.getInt("launches", 0) + 1;
        config.set("launches", launches).save();
        ctx.log("My Mod loaded, launch #" + launches);

        ctx.registerCommand("hello", ctx.translate("mymod.command"), args -> {
            String name = args.length > 0 ? String.join(" ", args) : "player";
            ctx.chat(ctx.translate("mymod.hello", name));
            ctx.playSound("pop");
        });
    }

    @Override
    public void onGameReady(ModContext ctx) {
        // Engine is available here: ctx.engine().getEventManager().register(ClickEvent.class, e -> ...)
        ctx.registerRecipe("golden_apple", 1, new String[]{"GGG", "GAG", "GGG"}, "G", "gold_ingot", "A", "apple");
    }

    @Override
    public void onFrame(ModContext ctx) {
        if (!ctx.isInWorld()) return;
        if (++frames % 1200 == 0) ctx.chat("&7You are at " + ctx.getPlayerPosition()); // every ~20 s
    }

    @Override
    public void onShutdown(ModContext ctx) { config.save(); }
}
