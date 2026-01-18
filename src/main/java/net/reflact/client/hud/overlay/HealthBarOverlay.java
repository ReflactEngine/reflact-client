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
        // Use simple color fill if texture is problematic
        context.fill(x, y, x + width, y + height, 0x80000000); // Semi-transparent black bg
        
        // Draw Progress
        int progressWidth = (int) (width * hpPercent);
        if (progressWidth > 0) {
            // ReflactClient.CONFIG.healthColor() returns io.wispforest.owo.ui.core.Color
            // We need ARGB int. .rgb() usually returns RGB, check alpha.
            int color = ReflactClient.CONFIG.healthColor().rgb();
            // Ensure full opacity if owo color doesn't provide it, or use standard red
            color = color | 0xFF000000; 
            context.fill(x, y, x + progressWidth, y + height, color);
        }
        
        // context.drawTexturedQuad(BG_TEXTURE, x, x + width, y, y + height, 0f, 1f, 0f, 1f);
        /*
        int progressWidth = (int) (width * hpPercent);
        if (progressWidth > 0) {
            float u2 = (float) progressWidth / width;
            context.drawTexturedQuad(PROGRESS_TEXTURE, x, x + progressWidth, y, y + height, 0f, u2, 0f, 1f);
        }
        */
        
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
