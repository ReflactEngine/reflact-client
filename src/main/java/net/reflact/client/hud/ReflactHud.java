package net.reflact.client.hud;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.util.Identifier;
import net.reflact.client.ClientData;

public class ReflactHud implements HudRenderCallback {
    private static final Identifier HEALTH_BG = Identifier.of("reflact", "textures/gui/hud/health_bg.png");
    private static final Identifier HEALTH = Identifier.of("reflact", "textures/gui/hud/health.png");
    private static final Identifier MANA_BG = Identifier.of("reflact", "textures/gui/hud/mana_bg.png");
    private static final Identifier MANA = Identifier.of("reflact", "textures/gui/hud/mana.png");
    private static final Identifier HOTBAR = Identifier.of("reflact", "textures/gui/hud/hotbar.png");

    @Override
    public void onHudRender(DrawContext context, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.options.hudHidden) return;

        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();

        // Dimensions
        int barWidth = 100;
        int barHeight = 10;
        int hotbarWidth = 182;
        int hotbarHeight = 22;

        int centerX = width / 2;
        int bottomY = height - hotbarHeight; // Align with bottom of screen

        // Draw Hotbar
        int hotbarX = centerX - (hotbarWidth / 2);
        context.drawTexture(RenderPipelines.GUI_TEXTURED, HOTBAR, hotbarX, bottomY, 0, 0, hotbarWidth, hotbarHeight, hotbarWidth, hotbarHeight);

        // Position bars above hotbar
        int barY = bottomY - 12;
        
        // Draw Health Bar (Left)
        int hpX = centerX - barWidth - 5;
        float hpPercent = (float) (ClientData.currentHealth / ClientData.maxHealth);
        
        // Health Background
        context.drawTexture(RenderPipelines.GUI_TEXTURED, HEALTH_BG, hpX, barY, 0, 0, barWidth, barHeight, barWidth, barHeight);
        // Health Foreground (clipped)
        int hpFill = (int) (barWidth * hpPercent);
        if (hpFill > 0) {
             context.drawTexture(RenderPipelines.GUI_TEXTURED, HEALTH, hpX, barY, 0, 0, hpFill, barHeight, barWidth, barHeight);
        }

        // Draw Mana Bar (Right)
        int manaX = centerX + 5;
        float manaPercent = (float) (ClientData.currentMana / ClientData.maxMana);

        // Mana Background
        context.drawTexture(RenderPipelines.GUI_TEXTURED, MANA_BG, manaX, barY, 0, 0, barWidth, barHeight, barWidth, barHeight);
        // Mana Foreground (clipped)
        int manaFill = (int) (barWidth * manaPercent);
        if (manaFill > 0) {
            context.drawTexture(RenderPipelines.GUI_TEXTURED, MANA, manaX, barY, 0, 0, manaFill, barHeight, barWidth, barHeight);
        }
        
        // Draw Text Overlays
        context.drawText(client.textRenderer, (int)ClientData.currentHealth + "/" + (int)ClientData.maxHealth, hpX + 5, barY + 1, 0xFFFFFF, true);
        context.drawText(client.textRenderer, (int)ClientData.currentMana + "/" + (int)ClientData.maxMana, manaX + 5, barY + 1, 0xFFFFFF, true);
    }

    private void drawBar(DrawContext context, int x, int y, int width, int height, float percent, int color) {
        // Background
        context.fill(x, y, x + width, y + height, 0xFF333333);
        // Foreground
        int filledWidth = (int) (width * percent);
        context.fill(x, y, x + filledWidth, y + height, color);
    }
}
