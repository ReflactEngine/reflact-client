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
        
        // Draw Background
        context.drawTexturedQuad(BG_TEXTURE, x, x + width, y, y + height, 0f, 1f, 0f, 1f);
        
        // Draw Progress
        int progressWidth = (int) (width * manaPercent);
        if (progressWidth > 0) {
            float u2 = (float) progressWidth / width;
            context.drawTexturedQuad(PROGRESS_TEXTURE, x, x + progressWidth, y, y + height, 0f, u2, 0f, 1f);
        }
        
        context.drawText(client.textRenderer, "MP " + (int)ClientData.currentMana + "/" + (int)ClientData.maxMana, x + 5, y + (height - 8) / 2, 0xFFFFFF, true);
    }
    @Override public int getX() { return ReflactClient.CONFIG.manaBarX(); }
    @Override public int getY() { return ReflactClient.CONFIG.manaBarY(); }
    @Override public void setX(int x) { ReflactClient.CONFIG.manaBarX(x); }
    @Override public void setY(int y) { ReflactClient.CONFIG.manaBarY(y); }
    @Override public int getWidth() { return 100; }
    @Override public int getHeight() { return 10; }
    @Override public boolean isEnabled() { return true; }
    @Override public String getName() { return "Mana Bar"; }
}
