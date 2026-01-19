package net.reflact.client.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.reflact.client.hud.overlay.OverlayManager;
import net.reflact.client.hud.overlay.ReflactOverlay;
import org.lwjgl.glfw.GLFW;
import net.minecraft.client.gui.Click;
import net.minecraft.client.input.KeyInput;

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
        context.drawCenteredTextWithShadow(textRenderer, "Scroll to Resize Selected Item", width / 2, 25, 0xFFFFFF);
        context.drawCenteredTextWithShadow(textRenderer, "Press ENTER or ESC to Save & Exit", width / 2, 40, 0xAAAAAA);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(Click click, boolean isRecursive) {
        if (click.button() == 0) { // Left click
            // Check for overlay selection (Topmost first)
            for (int i = OverlayManager.INSTANCE.getOverlays().size() - 1; i >= 0; i--) {
                ReflactOverlay overlay = OverlayManager.INSTANCE.getOverlays().get(i);
                if (overlay.isEnabled()) {
                    int x = overlay.getX();
                    int y = overlay.getY();
                    int w = overlay.getWidth();
                    int h = overlay.getHeight();

                    if (click.x() >= x && click.x() <= x + w && click.y() >= y && click.y() <= y + h) {
                        selectedOverlay = overlay;
                        dragOffsetX = (int) click.x() - x;
                        dragOffsetY = (int) click.y() - y;
                        isDragging = true;
                        return true; // Consume event
                    }
                }
            }
            // If clicked background, deselect
            selectedOverlay = null;
            isDragging = false;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(Click click, double deltaX, double deltaY) {
        if (isDragging && selectedOverlay != null && click.button() == 0) {
            // Update position
            int newX = (int) click.x() - dragOffsetX;
            int newY = (int) click.y() - dragOffsetY;
            
            selectedOverlay.setX(newX);
            selectedOverlay.setY(newY);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (selectedOverlay != null) {
            // Resize selected overlay
            int scaleStep = 2;
            int newW = selectedOverlay.getWidth() + (int)(verticalAmount * scaleStep);
            int newH = selectedOverlay.getHeight() + (int)(verticalAmount * scaleStep);
            
            // Basic validation
            if (newW < 10) newW = 10;
            if (newH < 10) newH = 10;
            
            selectedOverlay.setWidth(newW);
            selectedOverlay.setHeight(newH);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean mouseReleased(Click click) {
        if (click.button() == 0) {
            isDragging = false;
        }
        return super.mouseReleased(click);
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        if (selectedOverlay != null) {
            int moveAmount = 1;
            if ((input.modifiers() & GLFW.GLFW_MOD_SHIFT) != 0) moveAmount = 10;
            
            if (input.key() == GLFW.GLFW_KEY_UP) {
                selectedOverlay.setY(selectedOverlay.getY() - moveAmount);
                return true;
            }
            if (input.key() == GLFW.GLFW_KEY_DOWN) {
                selectedOverlay.setY(selectedOverlay.getY() + moveAmount);
                return true;
            }
            if (input.key() == GLFW.GLFW_KEY_LEFT) {
                selectedOverlay.setX(selectedOverlay.getX() - moveAmount);
                return true;
            }
            if (input.key() == GLFW.GLFW_KEY_RIGHT) {
                selectedOverlay.setX(selectedOverlay.getX() + moveAmount);
                return true;
            }
        }
        
        if (input.key() == GLFW.GLFW_KEY_ENTER || input.key() == GLFW.GLFW_KEY_ESCAPE) {
            this.close();
            return true;
        }
        
        return false;
    }

    @Override
    public void close() {
        OverlayManager.INSTANCE.saveConfig();
        super.close();
    }
}
