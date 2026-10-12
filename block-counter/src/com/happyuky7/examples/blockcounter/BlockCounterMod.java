package com.happyuky7.examples.blockcounter;

import com.happyuky7.openvoxelmoding.loader.ClientMod;
import com.happyuky7.openvoxelmoding.loader.ModContext;
import net.openvoxel.engine.VoxelEngine;
import net.openvoxel.engine.events.player.ClickEvent;
import net.openvoxel.engine.world.block.BlockType;

import java.util.EnumMap;
import java.util.Map;

/** Listens to engine click events and counts which blocks the player breaks. */
public final class BlockCounterMod implements ClientMod {
    private final Map<BlockType, Integer> broken = new EnumMap<>(BlockType.class);

    @Override
    public void onGameReady(ModContext context) {
        VoxelEngine engine = context.getEngine(VoxelEngine.class);
        engine.getEventManager().register(ClickEvent.class, event -> {
            if (event.type != ClickEvent.ClickType.LEFT || engine.getWorld() == null) return;
            try {
                BlockType type = engine.getWorld().getBlock(event.worldX, event.worldY, event.worldZ).getType();
                if (type == null || type == BlockType.AIR) return;
                broken.merge(type, 1, Integer::sum);
            } catch (Exception ignored) { }
        });
        context.registerCommand("broken", "show how many blocks you broke (example mod)", args -> {
            if (broken.isEmpty()) { context.chat("&7No blocks broken yet."); return; }
            context.chat("&e--- Blocks broken ---");
            broken.forEach((type, count) -> context.chat("&a" + type.name().toLowerCase() + "&7: " + count));
        });
    }
}
