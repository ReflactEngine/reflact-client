package net.reflact.client.hud;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.reflact.client.ClientData;

public class ReflactHud implements HudRenderCallback {
    @Override
    public void onHudRender(DrawContext context, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.options.hudHidden) return;

        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();

        // Constants
        int barWidth = 100;
        int barHeight = 10;
        int centerX = width / 2;
        int bottomY = height - 40; // Just above hotbar

        // Draw Health Bar (Left)
        int hpX = centerX - barWidth - 2;
        float hpPercent = (float) (ClientData.currentHealth / ClientData.maxHealth);
        drawBar(context, hpX, bottomY, barWidth, barHeight, hpPercent, 0xFFFF0000); // Red

        // Draw Mana Bar (Right)
        int manaX = centerX + 2;
        float manaPercent = (float) (ClientData.currentMana / ClientData.maxMana);
        drawBar(context, manaX, bottomY, barWidth, barHeight, manaPercent, 0xFF0000FF); // Blue
        
        // Draw Text Overlays
        context.drawText(client.textRenderer, (int)ClientData.currentHealth + "/" + (int)ClientData.maxHealth, hpX + 5, bottomY + 1, 0xFFFFFF, true);
        context.drawText(client.textRenderer, (int)ClientData.currentMana + "/" + (int)ClientData.maxMana, manaX + 5, bottomY + 1, 0xFFFFFF, true);
    }

    private void drawBar(DrawContext context, int x, int y, int width, int height, float percent, int color) {
        // Background
        context.fill(x, y, x + width, y + height, 0xFF333333);
        // Foreground
        int filledWidth = (int) (width * percent);
        context.fill(x, y, x + filledWidth, y + height, color);
    }
}
