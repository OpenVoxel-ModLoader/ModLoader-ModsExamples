package com.happyuky7.examples.hudclock;

import com.happyuky7.openvoxelmoding.loader.ClientMod;
import com.happyuky7.openvoxelmoding.loader.ModContext;
import net.openvoxel.engine.VoxelEngine;
import net.openvoxel.engine.ui.GLUIRenderer;
import net.openvoxel.engine.ui.UIManager;
import org.joml.Vector3d;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/** Draws a small HUD panel (clock, position, fps) in the top-right corner while playing. */
public final class HudClockMod implements ClientMod {
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm:ss");
    private boolean enabled = true;
    private long lastNs; private int frames, fps;

    @Override
    public void onLoad(ModContext context) {
        context.registerCommand("clock", "toggle the HUD clock (example mod)", args -> {
            enabled = !enabled;
            context.chat(enabled ? "&aHUD clock shown" : "&7HUD clock hidden");
        });
    }

    @Override
    public void onFrame(ModContext context) {
        VoxelEngine engine = context.getEngine(VoxelEngine.class);
        long now = System.nanoTime();
        frames++;
        if (now - lastNs >= 1_000_000_000L) { fps = frames; frames = 0; lastNs = now; }
        if (!enabled || engine == null || !engine.isWorldActive() || engine.getCamera() == null || UIManager.get().hasActiveGUIs()) return;
        int[] size = engine.getCamera().getAspect();
        Vector3d p = engine.getCamera().getPosition();
        String line1 = LocalTime.now().format(TIME) + "  " + fps + " fps";
        String line2 = String.format("X %.1f  Y %.1f  Z %.1f", p.x, p.y, p.z);
        float w = Math.max(GLUIRenderer.getTextWidth(line1), GLUIRenderer.getTextWidth(line2)) + 16, x = size[0] - w - 8, y = 8;
        GLUIRenderer.setup2DRendering(size[0], size[1]);
        try {
            GLUIRenderer.drawPanel(x, y, w, 50, 0f, 0f, 0f, 0.45f);
            GLUIRenderer.drawText(line1, x + 8, y + 20, 1f, 1f, 0.6f, 1f);
            GLUIRenderer.drawText(line2, x + 8, y + 42, 0.9f, 0.9f, 0.9f, 1f);
        } finally { GLUIRenderer.restore3DRendering(); }
    }
}
