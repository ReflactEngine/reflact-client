package net.reflact.client.hud.overlay;

import net.minecraft.client.gui.DrawContext;

public interface ReflactOverlay {
    void render(DrawContext context, float tickDelta);
    int getX();
    int getY();
    void setX(int x);
    void setY(int y);
    int getWidth();
    int getHeight();
    
    // Resize support
    default void setWidth(int width) {}
    default void setHeight(int height) {}
    
    boolean isEnabled();
    String getName();
}
