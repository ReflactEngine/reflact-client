package net.reflact.client.hud.overlay;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.reflact.client.ClientData;
import net.reflact.client.ReflactClient;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class HealthBarOverlay implements ReflactOverlay {
    private static final Identifier BG_TEXTURE = Identifier.of("reflact", "textures/gui/sprites/hud/health_bg.png");
    private static final Identifier PROGRESS_TEXTURE = Identifier.of("reflact", "textures/gui/sprites/hud/health.png");

    @Override
    public void render(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        ClientData.currentHealth = client.player.getHealth();
        ClientData.maxHealth = client.player.getMaxHealth();

        int x = getX();
        int y = getY();
        // Use texture dimensions or keep configurable width?
        // Textures are likely designed for a specific aspect ratio.
        // Let's stick to configurable width/height but user might need to adjust config.
        int width = getWidth();
        int height = getHeight();

        float hpPercent = (float) (ClientData.currentHealth / ClientData.maxHealth);
        
        // Draw Background
        // context.drawTexture(RenderLayer::getGuiTextured, BG_TEXTURE, x, y, 0f, 0f, width, height, width, height);
        context.drawTexturedQuad(BG_TEXTURE, x, x + width, y, y + height, 0f, 1f, 0f, 1f);
        
        // Draw Progress
        int progressWidth = (int) (width * hpPercent);
        if (progressWidth > 0) {
            float u2 = (float) progressWidth / width;
            context.drawTexturedQuad(PROGRESS_TEXTURE, x, x + progressWidth, y, y + height, 0f, u2, 0f, 1f);
        }
        
        context.drawText(client.textRenderer, "HP " + (int)ClientData.currentHealth + "/" + (int)ClientData.maxHealth, x + 5, y + (height - 8) / 2, 0xFFFFFF, true);
    }
    @Override public int getX() { return ReflactClient.CONFIG.healthBarX(); }
    @Override public int getY() { return ReflactClient.CONFIG.healthBarY(); }
    @Override public void setX(int x) { ReflactClient.CONFIG.healthBarX(x); }
    @Override public void setY(int y) { ReflactClient.CONFIG.healthBarY(y); }
    @Override public int getWidth() { return 100; }
    @Override public int getHeight() { return 10; }
    @Override public boolean isEnabled() { return true; }
    @Override public String getName() { return "Health Bar"; }
}
