package net.reflact.client.hud.overlay;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.reflact.client.ClientData;
import net.reflact.client.ReflactClient;

public class HealthBarOverlay implements ReflactOverlay {
    @Override
    public void render(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        ClientData.currentHealth = client.player.getHealth();
        ClientData.maxHealth = client.player.getMaxHealth();

        int x = getX();
        int y = getY();
        int width = getWidth();
        int height = getHeight();

        float hpPercent = (float) (ClientData.currentHealth / ClientData.maxHealth);
        int hpColor = ReflactClient.CONFIG.healthColor().rgb();
        if (hpPercent < 0.25f && System.currentTimeMillis() % 1000 < 500) {
            hpColor = 0xFFFFFFFF;
        }

        context.fill(x - 1, y - 1, x + width + 1, y + height + 1, 0xFFFFFFFF);
        context.fill(x, y, x + width, y + height, 0xFF000000);
        int hpFill = (int) (width * hpPercent);
        if (hpFill > 0) context.fill(x, y, x + hpFill, y + height, hpColor);
        context.drawText(client.textRenderer, "HP " + (int)ClientData.currentHealth + "/" + (int)ClientData.maxHealth, x + 5, y + 1, 0xFFFFFF, true);
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
