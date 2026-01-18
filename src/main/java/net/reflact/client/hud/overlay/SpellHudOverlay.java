package net.reflact.client.hud.overlay;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.reflact.client.ClientData;
import net.reflact.client.ReflactClient;

public class SpellHudOverlay implements ReflactOverlay {
    @Override
    public void render(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        int x = getX();
        int y = getY();
        int spellSize = 20;
        int spellGap = 5;
        renderSpellIcon(context, client.textRenderer, "fireball", "R", x, y, spellSize);
        renderSpellIcon(context, client.textRenderer, "heal", "G", x + spellSize + spellGap, y, spellSize);
    }
    private void renderSpellIcon(DrawContext context, net.minecraft.client.font.TextRenderer textRenderer, String spellName, String keyText, int x, int y, int size) {
        long lastCast = ClientData.spellLastCast.getOrDefault(spellName, 0L);
        long cooldown = ClientData.spellCooldowns.getOrDefault(spellName, 0L);
        long timePassed = System.currentTimeMillis() - lastCast;
        float progress = (float) timePassed / cooldown;
        boolean onCooldown = timePassed < cooldown;
        context.fill(x, y, x + size, y + size, 0x80000000);
        int borderColor = onCooldown ? 0xFFFF0000 : 0xFFFFFFFF;
        context.fill(x - 1, y - 1, x + size + 1, y, borderColor);
        context.fill(x - 1, y + size, x + size + 1, y + size + 1, borderColor);
        context.fill(x - 1, y, x, y + size, borderColor);
        context.fill(x + size, y, x + size + 1, y + size, borderColor);
        String iconLetter = spellName.substring(0, 1).toUpperCase();
        context.drawText(textRenderer, iconLetter, x + 7, y + 6, 0xFFCCCCCC, true);
        if (onCooldown) {
            int cooldownHeight = (int) (size * (1.0f - progress));
            context.fill(x, y + size - cooldownHeight, x + size, y + size, 0x80FFFFFF);
            String secondsLeft = String.format("%.1f", (cooldown - timePassed) / 1000.0f);
            context.drawText(textRenderer, secondsLeft, x + 2, y + 6, 0xFFFF5555, true);
        } else {
             context.drawText(textRenderer, keyText, x + size - 6, y + size - 6, 0xFFFFFF00, true);
        }
    }
    @Override public int getX() { return ReflactClient.CONFIG.spellHudX(); }
    @Override public int getY() { return ReflactClient.CONFIG.spellHudY(); }
    @Override public void setX(int x) { ReflactClient.CONFIG.spellHudX(x); }
    @Override public void setY(int y) { ReflactClient.CONFIG.spellHudY(y); }
    @Override public int getWidth() { return 45; }
    @Override public int getHeight() { return 20; }
    @Override public boolean isEnabled() { return ReflactClient.CONFIG.showSpellHud(); }
    @Override public String getName() { return "Spells"; }
}
