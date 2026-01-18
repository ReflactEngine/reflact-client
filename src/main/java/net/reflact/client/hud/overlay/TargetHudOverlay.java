package net.reflact.client.hud.overlay;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.reflact.client.ReflactClient;
import net.minecraft.entity.LivingEntity;

public class TargetHudOverlay implements ReflactOverlay {
    @Override
    public void render(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.targetedEntity instanceof LivingEntity livingTarget) {
             int x = getX();
             int y = getY();
             context.drawText(client.textRenderer, livingTarget.getDisplayName(), x, y, 0xFFFFFFFF, true);
             float health = livingTarget.getHealth();
             float maxHealth = livingTarget.getMaxHealth();
             float percent = health / maxHealth;
             int targetBarWidth = 100;
             int targetBarHeight = 8;
             int targetBarY = y + 12;
             context.fill(x, targetBarY, x + targetBarWidth, targetBarY + targetBarHeight, 0xFF000000);
             int fill = (int)(targetBarWidth * percent);
             if (fill > 0) context.fill(x, targetBarY, x + fill, targetBarY + targetBarHeight, 0xFFFF0000);
             String targetHpText = String.format("%.0f/%.0f", health, maxHealth);
             context.drawText(client.textRenderer, targetHpText, x + 2, targetBarY, 0xFFFFFFFF, true);
        }
    }
    @Override public int getX() { return ReflactClient.CONFIG.targetHudX(); }
    @Override public int getY() { return ReflactClient.CONFIG.targetHudY(); }
    @Override public void setX(int x) { ReflactClient.CONFIG.targetHudX(x); }
    @Override public void setY(int y) { ReflactClient.CONFIG.targetHudY(y); }
    @Override public int getWidth() { return 100; }
    @Override public int getHeight() { return 30; }
    @Override public boolean isEnabled() { return ReflactClient.CONFIG.showTargetHud(); }
    @Override public String getName() { return "Target HUD"; }
}
