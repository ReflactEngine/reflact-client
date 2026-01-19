package net.reflact.client.hud.overlay;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.reflact.client.ReflactClient;

public class HotbarOverlay implements ReflactOverlay {
    private static final Identifier TEXTURE = Identifier.of("reflact", "textures/gui/sprites/hud/hotbar.png");

    @Override
    public void render(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null) return;

        int x = getX();
        int y = getY();
        int width = getWidth();
        int height = getHeight();

        // Draw Background (Polished: Translucent Black with Border)
        // Main background
        context.fill(x, y, x + width, y + height, 0xAA000000);
        // Top/Bottom borders
        context.fill(x, y, x + width, y + 1, 0xFF555555);
        context.fill(x, y + height - 1, x + width, y + height, 0xFF555555);
        // Left/Right borders
        context.fill(x, y, x + 1, y + height, 0xFF555555);
        context.fill(x + width - 1, y, x + width, y + height, 0xFF555555);

        // Draw Slots
        for (int i = 0; i < 9; i++) {
            ItemStack stack = player.getInventory().getStack(i);
            int itemX = x + 3 + i * 20; 
            int itemY = y + 3; 
            
            // Slot background (slightly lighter)
            context.fill(itemX - 1, itemY - 1, itemX + 17, itemY + 17, 0x44FFFFFF);
            
            context.drawItem(stack, itemX, itemY);
            context.drawStackOverlay(client.textRenderer, stack, itemX, itemY);
            
            // Draw selection
            if (player.getInventory().getSelectedSlot() == i) {
                 // Selection Border (White, thicker)
                 context.fill(itemX - 2, itemY - 2, itemX + 18, itemY - 1, 0xFFFFFFFF); // Top
                 context.fill(itemX - 2, itemY + 17, itemX + 18, itemY + 18, 0xFFFFFFFF); // Bottom
                 context.fill(itemX - 2, itemY - 1, itemX - 1, itemY + 17, 0xFFFFFFFF); // Left
                 context.fill(itemX + 17, itemY - 1, itemX + 18, itemY + 17, 0xFFFFFFFF); // Right
            }
        }
    }

    @Override
    public int getX() { 
        int cfg = ReflactClient.CONFIG.hotbarX();
        if (cfg == 0) {
            MinecraftClient client = MinecraftClient.getInstance();
            return client.getWindow().getScaledWidth() / 2 - 91; 
        }
        return cfg;
    }
    
    @Override
    public int getY() { 
        int cfg = ReflactClient.CONFIG.hotbarY();
        if (cfg == 0) {
            MinecraftClient client = MinecraftClient.getInstance();
            return client.getWindow().getScaledHeight() - 22; 
        }
        return cfg;
    }

    @Override public void setX(int x) { ReflactClient.CONFIG.hotbarX(x); }
    @Override public void setY(int y) { ReflactClient.CONFIG.hotbarY(y); }
    @Override public int getWidth() { return 182; }
    @Override public int getHeight() { return 22; }
    @Override public boolean isEnabled() { return ReflactClient.CONFIG.showHotbar(); }
    @Override public String getName() { return "Hotbar"; }
}
