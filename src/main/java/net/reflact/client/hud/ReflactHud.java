package net.reflact.client.hud;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import net.reflact.client.ClientData;
import com.mojang.blaze3d.systems.RenderSystem;

public class ReflactHud implements HudRenderCallback {
    private static final Identifier HEALTH_BG = Identifier.of("reflact", "hud/health_bg");
    private static final Identifier HEALTH = Identifier.of("reflact", "hud/health");
    private static final Identifier MANA_BG = Identifier.of("reflact", "hud/mana_bg");
    private static final Identifier MANA = Identifier.of("reflact", "hud/mana");
    private static final Identifier HOTBAR = Identifier.of("reflact", "hud/hotbar");

    public static void initialize() {
        HudRenderCallback.EVENT.register(new ReflactHud());
    }

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

        // Hotbar Background (Placeholder for texture)
        // context.fill(centerX - hotbarWidth / 2, bottomY, centerX + hotbarWidth / 2, bottomY + hotbarHeight, 0x80000000);

        // Position bars above hotbar
        int barY = bottomY - 12;
        
        // Draw Health Bar (Left)
        int hpX = centerX - barWidth - 5;
        float hpPercent = (float) (ClientData.currentHealth / ClientData.maxHealth);
        
        // Health Background (Black)
        context.fill(hpX, barY, hpX + barWidth, barY + barHeight, 0xFF000000);
        // Health Foreground (Red)
        int hpFill = (int) (barWidth * hpPercent);
        if (hpFill > 0) {
             context.fill(hpX, barY, hpX + hpFill, barY + barHeight, 0xFFFF0000);
        }

        // Draw Mana Bar (Right)
        int manaX = centerX + 5;
        float manaPercent = (float) (ClientData.currentMana / ClientData.maxMana);

        // Mana Background (Black)
        context.fill(manaX, barY, manaX + barWidth, barY + barHeight, 0xFF000000);
        // Mana Foreground (Blue)
        int manaFill = (int) (barWidth * manaPercent);
        if (manaFill > 0) {
            context.fill(manaX, barY, manaX + manaFill, barY + barHeight, 0xFF0000FF);
        }
        
        // Draw Text Overlays
        context.drawText(client.textRenderer, (int)ClientData.currentHealth + "/" + (int)ClientData.maxHealth, hpX + 5, barY + 1, 0xFFFFFF, true);
        context.drawText(client.textRenderer, (int)ClientData.currentMana + "/" + (int)ClientData.maxMana, manaX + 5, barY + 1, 0xFFFFFF, true);
    }
}