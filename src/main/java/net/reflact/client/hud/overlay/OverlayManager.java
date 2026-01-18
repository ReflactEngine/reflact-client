package net.reflact.client.hud.overlay;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.reflact.client.ReflactClient;

import java.util.ArrayList;
import java.util.List;

public class OverlayManager implements HudRenderCallback {

    public static final OverlayManager INSTANCE = new OverlayManager();
    private final List<ReflactOverlay> overlays = new ArrayList<>();

    public void register(ReflactOverlay overlay) {
        overlays.add(overlay);
    }

    public List<ReflactOverlay> getOverlays() {
        return overlays;
    }

    @Override
    public void onHudRender(DrawContext context, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.options.hudHidden || !ReflactClient.CONFIG.showHud()) return;
        // Don't render if we are in the editor screen, as the editor handles rendering there
        if (client.currentScreen instanceof net.reflact.client.gui.HudEditorScreen) return;

        for (ReflactOverlay overlay : overlays) {
            if (overlay.isEnabled()) {
                overlay.render(context, tickCounter.getTickDelta(true));
            }
        }
    }

    public void saveConfig() {
        ReflactClient.CONFIG.save();
    }
}
