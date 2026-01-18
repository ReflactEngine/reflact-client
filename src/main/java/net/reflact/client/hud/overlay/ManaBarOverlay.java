package net.reflact.client.hud.overlay;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.reflact.client.ClientData;
import net.reflact.client.ReflactClient;

public class ManaBarOverlay implements ReflactOverlay {
    @Override
    public void render(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        int x = getX();
        int y = getY();
        int width = getWidth();
        int height = getHeight();

        float manaPercent = (float) (ClientData.currentMana / ClientData.maxMana);
        int manaColor = ReflactClient.CONFIG.manaColor().rgb();

        context.fill(x - 1, y - 1, x + width + 1, y + height + 1, 0xFFFFFFFF);
        context.fill(x, y, x + width, y + height, 0xFF000000);
        int manaFill = (int) (width * manaPercent);
        if (manaFill > 0) context.fill(x, y, x + manaFill, y + height, manaColor);
        context.drawText(client.textRenderer, "MP " + (int)ClientData.currentMana + "/" + (int)ClientData.maxMana, x + 5, y + 1, 0xFFFFFF, true);
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
