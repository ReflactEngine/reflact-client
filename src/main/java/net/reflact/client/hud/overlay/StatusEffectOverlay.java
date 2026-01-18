package net.reflact.client.hud.overlay;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.reflact.client.ReflactClient;
import java.util.Collection;
import net.minecraft.entity.effect.StatusEffectInstance;

public class StatusEffectOverlay implements ReflactOverlay {
    @Override
    public void render(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        int x = getX();
        int y = getY();
        int effectOffset = 0;
        Collection<StatusEffectInstance> effects = client.player.getStatusEffects();
        if (!effects.isEmpty()) {
             for (StatusEffectInstance effect : effects) {
                 net.minecraft.text.Text name = effect.getEffectType().value().getName();
                 String duration = net.minecraft.util.StringHelper.formatTicks(effect.getDuration(), client.world.getTickManager().getTickRate());
                 String amplifier = effect.getAmplifier() > 0 ? " " + (effect.getAmplifier() + 1) : "";
                 String text = name.getString() + amplifier + " (" + duration + ")";
                 int color = effect.getEffectType().value().isBeneficial() ? 0xFF55FF55 : 0xFFFF5555;
                 context.drawText(client.textRenderer, text, x, y + effectOffset, color, true);
                 effectOffset += 10;
             }
        }
    }
    @Override public int getX() { return ReflactClient.CONFIG.statusEffectHudX(); }
    @Override public int getY() { return ReflactClient.CONFIG.statusEffectHudY(); }
    @Override public void setX(int x) { ReflactClient.CONFIG.statusEffectHudX(x); }
    @Override public void setY(int y) { ReflactClient.CONFIG.statusEffectHudY(y); }
    @Override public int getWidth() { return 100; }
    @Override public int getHeight() { return 100; } // Dynamic
    @Override public boolean isEnabled() { return ReflactClient.CONFIG.showStatusEffectHud(); }
    @Override public String getName() { return "Status Effects"; }
}
