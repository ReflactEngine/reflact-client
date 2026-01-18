package net.reflact.client.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.reflact.client.hud.overlay.OverlayManager;
import net.reflact.client.hud.overlay.ReflactOverlay;
import org.lwjgl.glfw.GLFW;

public class HudEditorScreen extends Screen {

    private ReflactOverlay selectedOverlay;
    private int dragOffsetX, dragOffsetY;
    
    // Track mouse state for dragging
    private boolean isDragging = false;

    public HudEditorScreen() {
        super(Text.of("HUD Editor"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Render a dark background to make it clear we are in edit mode
        context.fill(0, 0, width, height, 0x80000000);

        for (ReflactOverlay overlay : OverlayManager.INSTANCE.getOverlays()) {
            if (overlay.isEnabled()) {
                overlay.render(context, delta);

                int x = overlay.getX();
                int y = overlay.getY();
                int w = overlay.getWidth();
                int h = overlay.getHeight();

                int color = (overlay == selectedOverlay) ? 0xFF00FF00 : 0xFFFFFFFF; // Green if selected, White otherwise

                // Draw border
                context.fill(x, y, x + w, y + 1, color); // Top
                context.fill(x, y + h - 1, x + w, y + h, color); // Bottom
                context.fill(x, y + 1, x + 1, y + h - 1, color); // Left
                context.fill(x + w - 1, y + 1, x + w, y + h - 1, color); // Right

                if (overlay == selectedOverlay) {
                    context.drawText(textRenderer, overlay.getName(), x, y - 10, 0xFF00FF00, true);
                }
            }
        }

        context.drawCenteredTextWithShadow(textRenderer, "HUD Editor - Drag or use Arrow Keys (Shift for faster) to move", width / 2, 10, 0xFFFFFF);
        context.drawCenteredTextWithShadow(textRenderer, "Press ENTER or ESC to Save & Exit", width / 2, 25, 0xAAAAAA);

        super.render(context, mouseX, mouseY, delta);
    }

    // @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) { // Left click
            // Check for overlay selection (Topmost first)
            for (int i = OverlayManager.INSTANCE.getOverlays().size() - 1; i >= 0; i--) {
                ReflactOverlay overlay = OverlayManager.INSTANCE.getOverlays().get(i);
                if (overlay.isEnabled()) {
                    int x = overlay.getX();
                    int y = overlay.getY();
                    int w = overlay.getWidth();
                    int h = overlay.getHeight();

                    if (mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h) {
                        selectedOverlay = overlay;
                        dragOffsetX = (int) mouseX - x;
                        dragOffsetY = (int) mouseY - y;
                        isDragging = true;
                        return true; // Consume event
                    }
                }
            }
            // If clicked background, deselect
            selectedOverlay = null;
            isDragging = false;
        }
        return false; // super.mouseClicked(mouseX, mouseY, button);
    }

    // @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (isDragging && selectedOverlay != null && button == 0) {
            // Update position
            int newX = (int) mouseX - dragOffsetX;
            int newY = (int) mouseY - dragOffsetY;
            
            // Optional: Clamp to screen
            // newX = Math.max(0, Math.min(newX, width - selectedOverlay.getWidth()));
            // newY = Math.max(0, Math.min(newY, height - selectedOverlay.getHeight()));
            
            selectedOverlay.setX(newX);
            selectedOverlay.setY(newY);
            return true;
        }
        return false; // super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }
    
    // @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            isDragging = false;
        }
        return false; // super.mouseReleased(mouseX, mouseY, button);
    }

    // @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (selectedOverlay != null) {
            int moveAmount = 1;
            if ((modifiers & GLFW.GLFW_MOD_SHIFT) != 0) moveAmount = 10;
            
            if (keyCode == GLFW.GLFW_KEY_UP) {
                selectedOverlay.setY(selectedOverlay.getY() - moveAmount);
                return true;
            }
            if (keyCode == GLFW.GLFW_KEY_DOWN) {
                selectedOverlay.setY(selectedOverlay.getY() + moveAmount);
                return true;
            }
            if (keyCode == GLFW.GLFW_KEY_LEFT) {
                selectedOverlay.setX(selectedOverlay.getX() - moveAmount);
                return true;
            }
            if (keyCode == GLFW.GLFW_KEY_RIGHT) {
                selectedOverlay.setX(selectedOverlay.getX() + moveAmount);
                return true;
            }
        }
        
        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.close();
            return true;
        }
        
        return false; // super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void close() {
        OverlayManager.INSTANCE.saveConfig();
        super.close();
    }
}
