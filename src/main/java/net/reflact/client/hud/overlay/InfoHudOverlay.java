package net.reflact.client.hud.overlay;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.reflact.client.ReflactClient;

public class InfoHudOverlay implements ReflactOverlay {
    @Override
    public void render(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        int x = getX();
        int y = getY();
        int infoColor = ReflactClient.CONFIG.infoHudColor().rgb();
        String coordsText = String.format("XYZ: %.1f / %.1f / %.1f", client.player.getX(), client.player.getY(), client.player.getZ());
        String biomeText = "Biome: " + client.world.getBiome(client.player.getBlockPos()).getKey().map(key -> key.getValue().toString()).orElse("Unknown");
        context.drawText(client.textRenderer, coordsText, x, y, infoColor, true);
        context.drawText(client.textRenderer, biomeText, x, y + 10, infoColor, true);
    }
    @Override public int getX() { return ReflactClient.CONFIG.infoHudX(); }
    @Override public int getY() { return ReflactClient.CONFIG.infoHudY(); }
    @Override public void setX(int x) { ReflactClient.CONFIG.infoHudX(x); }
    @Override public void setY(int y) { ReflactClient.CONFIG.infoHudY(y); }
    @Override public int getWidth() { return 150; } // Approximate
    @Override public int getHeight() { return 20; }
    @Override public boolean isEnabled() { return ReflactClient.CONFIG.showInfoHud(); }
    @Override public String getName() { return "Info HUD"; }
}
