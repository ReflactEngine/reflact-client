package net.reflact.client.hud.overlay;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.reflact.client.ReflactClient;

public class ArmorHudOverlay implements ReflactOverlay {
    @Override
    public void render(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        int x = getX();
        int y = getY();
        int offset = 0;
        for (net.minecraft.entity.EquipmentSlot slot : net.minecraft.entity.EquipmentSlot.values()) {
            if (slot.getType() != net.minecraft.entity.EquipmentSlot.Type.HUMANOID_ARMOR) continue;
            net.minecraft.item.ItemStack stack = client.player.getEquippedStack(slot);
            if (stack.isEmpty()) continue;
            context.drawItem(stack, x, y - offset);
            context.drawStackOverlay(client.textRenderer, stack, x, y - offset);
            offset += 20;
        }
    }
    @Override public int getX() { return ReflactClient.CONFIG.armorHudX(); }
    @Override public int getY() { return ReflactClient.CONFIG.armorHudY(); }
    @Override public void setX(int x) { ReflactClient.CONFIG.armorHudX(x); }
    @Override public void setY(int y) { ReflactClient.CONFIG.armorHudY(y); }
    @Override public int getWidth() { return 16; }
    @Override public int getHeight() { return 80; } // 4 items * 20
    @Override public boolean isEnabled() { return ReflactClient.CONFIG.showArmorHud(); }
    @Override public String getName() { return "Armor HUD"; }
}
