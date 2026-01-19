package net.reflact.client.hud.overlay;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.reflact.client.ClientData;
import net.reflact.client.ReflactClient;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class ManaBarOverlay implements ReflactOverlay {
    private static final Identifier BG_TEXTURE = Identifier.of("reflact", "textures/gui/sprites/hud/mana_bg.png");
    private static final Identifier PROGRESS_TEXTURE = Identifier.of("reflact", "textures/gui/sprites/hud/mana.png");

    @Override
    public void render(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        int x = getX();
        int y = getY();
        int width = getWidth();
        int height = getHeight();

        float manaPercent = (float) (ClientData.currentMana / ClientData.maxMana);
        
        // Draw Polished Bar
        // 1. Background (Dark Gray)
        context.fill(x, y, x + width, y + height, 0xFF333333);
        
        // 2. Background Border (Black)
        int border = 1;
        context.fill(x - border, y - border, x + width + border, y, 0xFF000000); // Top
        context.fill(x - border, y + height, x + width + border, y + height + border, 0xFF000000); // Bottom
        context.fill(x - border, y, x, y + height, 0xFF000000); // Left
        context.fill(x + width, y, x + width + border, y + height, 0xFF000000); // Right
        
        // 3. Progress
        int progressWidth = (int) (width * manaPercent);
        if (progressWidth > 0) {
             int color = ReflactClient.CONFIG.manaColor().rgb();
             color = color | 0xFF000000;
             
             // Main fill
             context.fill(x, y, x + progressWidth, y + height, color);
             
             // Highlight (Top half lighter)
             int r = (color >> 16) & 0xFF;
             int g = (color >> 8) & 0xFF;
             int b = color & 0xFF;
             int lighter = 0xFF000000 | 
                          (Math.min(255, r + 40) << 16) | 
                          (Math.min(255, g + 40) << 8) | 
                          Math.min(255, b + 40);
                          
            context.fill(x, y, x + progressWidth, y + height / 2, lighter);
        }
        
        // 4. Text (Shadowed, Centered)
        String text = "MP " + (int)ClientData.currentMana + "/" + (int)ClientData.maxMana;
        int textWidth = client.textRenderer.getWidth(text);
        context.drawText(client.textRenderer, text, x + (width - textWidth) / 2, y + (height - 8) / 2, 0xFFFFFF, true);
    }
    @Override public int getX() { return ReflactClient.CONFIG.manaBarX(); }
    @Override public int getY() { return ReflactClient.CONFIG.manaBarY(); }
    @Override public void setX(int x) { ReflactClient.CONFIG.manaBarX(x); }
    @Override public void setY(int y) { ReflactClient.CONFIG.manaBarY(y); }
    // Resizable state
    private int width = 100;
    private int height = 10;

    @Override public int getWidth() { return width; }
    @Override public int getHeight() { return height; }
    
    @Override public void setWidth(int w) { this.width = w; }
    @Override public void setHeight(int h) { this.height = h; }

    @Override public boolean isEnabled() { return true; }
    @Override public String getName() { return "Mana Bar"; }
}
