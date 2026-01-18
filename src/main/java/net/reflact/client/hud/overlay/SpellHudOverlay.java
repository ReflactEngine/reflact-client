package net.reflact.client.hud.overlay;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.reflact.client.ClientData;
import net.reflact.client.ReflactClient;
import net.reflact.client.input.KeyInputHandler;

public class SpellHudOverlay implements ReflactOverlay {
    @Override
    public void render(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        int x = getX();
        int y = getY();
        int spellSize = 20;
        int spellGap = 5;
        
        int renderedCount = 0;
        for (int i = 0; i < 10; i++) {
             String spell = getSpell(i + 1);
             if (spell == null || spell.isEmpty()) continue;
             
             KeyBinding key = KeyInputHandler.getSpellKey(i);
             String keyText = "?";
             if (key != null) {
                 keyText = key.getBoundKeyLocalizedText().getString().toUpperCase();
                 // Simplistic cleanup for display
                 if (keyText.startsWith("KEY.KEYBOARD.")) keyText = keyText.replace("KEY.KEYBOARD.", "");
                 if (keyText.length() > 2) keyText = keyText.substring(0, 1); 
             }

             int renderX = x + (renderedCount * (spellSize + spellGap));
             renderSpellIcon(context, client.textRenderer, spell, keyText, renderX, y, spellSize);
             renderedCount++;
        }
    }
    
    private String getSpell(int slot) {
        return switch (slot) {
            case 1 -> ReflactClient.CONFIG.spellSlot1();
            case 2 -> ReflactClient.CONFIG.spellSlot2();
            case 3 -> ReflactClient.CONFIG.spellSlot3();
            case 4 -> ReflactClient.CONFIG.spellSlot4();
            case 5 -> ReflactClient.CONFIG.spellSlot5();
            case 6 -> ReflactClient.CONFIG.spellSlot6();
            case 7 -> ReflactClient.CONFIG.spellSlot7();
            case 8 -> ReflactClient.CONFIG.spellSlot8();
            case 9 -> ReflactClient.CONFIG.spellSlot9();
            case 10 -> ReflactClient.CONFIG.spellSlot10();
            default -> "";
        };
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
