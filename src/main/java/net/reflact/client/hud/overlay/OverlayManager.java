package net.reflact.client.hud.overlay;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.reflact.client.ReflactClient;

import java.util.ArrayList;
import java.util.List;

public class OverlayManager {

  public static final OverlayManager INSTANCE = new OverlayManager();
  private final List<ReflactOverlay> overlays = new ArrayList<>();

  // Added this getter so HudEditorScreen can access the list
  public List<ReflactOverlay> getOverlays() {
    return overlays;
  }

  public void register(ReflactOverlay overlay) {
    overlays.add(overlay);
  }

  public void onHudRender(DrawContext context, RenderTickCounter tickCounter) {
    MinecraftClient client = MinecraftClient.getInstance();
    if (client.player == null || client.options.hudHidden || !ReflactClient.CONFIG.showHud())
      return;

    // Don't render if we are in the editor screen to avoid duplication
    if (client.currentScreen instanceof net.reflact.client.gui.HudEditorScreen)
      return;

    float tickDelta = tickCounter.getTickProgress(true);

    for (ReflactOverlay overlay : overlays) {
      if (overlay.isEnabled()) {
        overlay.render(context, tickDelta);
      }
    }
  }

  public void saveConfig() {
    ReflactClient.CONFIG.save();
  }
}
