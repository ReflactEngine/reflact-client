package net.reflact.client.hud;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.reflact.client.hud.overlay.*;

public class ReflactHud {

    public static void initialize() {
        OverlayManager.INSTANCE.register(new HealthBarOverlay());
        OverlayManager.INSTANCE.register(new ManaBarOverlay());
        OverlayManager.INSTANCE.register(new HotbarOverlay());
        OverlayManager.INSTANCE.register(new InfoHudOverlay());
        OverlayManager.INSTANCE.register(new ArmorHudOverlay());
        OverlayManager.INSTANCE.register(new StatusEffectOverlay());
        OverlayManager.INSTANCE.register(new TargetHudOverlay());
        OverlayManager.INSTANCE.register(new SpellHudOverlay());
        
        // Notifications
        OverlayManager.INSTANCE.register(new net.reflact.client.hud.overlay.ReflactOverlay() {
            @Override public void render(net.minecraft.client.gui.DrawContext context, float tickDelta) {
                net.minecraft.client.MinecraftClient client = net.minecraft.client.MinecraftClient.getInstance();
                int width = client.getWindow().getScaledWidth();
                int height = client.getWindow().getScaledHeight();
                net.reflact.client.ui.NotificationManager.render(context, client.textRenderer, width, height);
            }
            @Override public int getX() { return 0; }
            @Override public int getY() { return 0; }
            @Override public void setX(int x) {}
            @Override public void setY(int y) {}
            @Override public int getWidth() { return 0; }
            @Override public int getHeight() { return 0; }
            @Override public boolean isEnabled() { return true; }
            @Override public String getName() { return "Notifications"; }
        });

        HudRenderCallback.EVENT.register(OverlayManager.INSTANCE::onHudRender);
    }
}
