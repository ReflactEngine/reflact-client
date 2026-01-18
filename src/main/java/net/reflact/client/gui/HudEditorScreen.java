package net.reflact.client.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.reflact.client.hud.overlay.OverlayManager;
import net.reflact.client.hud.overlay.ReflactOverlay;

public class HudEditorScreen extends Screen {

  private ReflactOverlay selectedOverlay;
  private int dragOffsetX, dragOffsetY;

  public HudEditorScreen() {
    super(Text.of("HUD Editor"));
  }

  @Override
  public void render(DrawContext context, int mouseX, int mouseY, float delta) {
    this.renderBackground(context, mouseX, mouseY, delta);

    for (ReflactOverlay overlay : OverlayManager.INSTANCE.getOverlays()) {
      if (overlay.isEnabled()) {
        overlay.render(context, delta);

        // Draw border
        int x = overlay.getX();
        int y = overlay.getY();
        int w = overlay.getWidth();
        int h = overlay.getHeight();

        int color = (overlay == selectedOverlay) ? 0xFF00FF00 : 0xFFFFFFFF; // Green if selected, White otherwise

        // Fixed: DrawContext doesn't have drawBorder, we use our helper method
        this.drawBorder(context, x - 1, y - 1, w + 2, h + 2, color);

        if (overlay == selectedOverlay) {
          context.drawText(textRenderer, overlay.getName(), x, y - 10, 0xFF00FF00, true);
        }
      }
    }

    context.drawCenteredTextWithShadow(textRenderer, "HUD Editor - Drag to move, ESC to save & exit", width / 2, 10,
        0xFFFFFF);

    super.render(context, mouseX, mouseY, delta);
  }

  // @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (button == 0) { // Left click
      for (ReflactOverlay overlay : OverlayManager.INSTANCE.getOverlays()) {
        if (overlay.isEnabled()) {
          int x = overlay.getX();
          int y = overlay.getY();
          int w = overlay.getWidth();
          int h = overlay.getHeight();

          if (mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h) {
            selectedOverlay = overlay;
            dragOffsetX = (int) mouseX - x;
            dragOffsetY = (int) mouseY - y;
            return true;
          }
        }
      }
      selectedOverlay = null;
    }
    return false;
  }

  // @Override
  public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
    if (selectedOverlay != null && button == 0) {
      selectedOverlay.setX((int) mouseX - dragOffsetX);
      selectedOverlay.setY((int) mouseY - dragOffsetY);
      return true;
    }
    return false;
  }

  @Override
  public void close() {
    OverlayManager.INSTANCE.saveConfig();
    super.close();
  }

  // Helper method for drawing borders
  private void drawBorder(DrawContext context, int x, int y, int width, int height, int color) {
    context.fill(x, y, x + width, y + 1, color); // Top
    context.fill(x, y + height - 1, x + width, y + height, color); // Bottom
    context.fill(x, y + 1, x + 1, y + height - 1, color); // Left
    context.fill(x + width - 1, y + 1, x + width, y + height - 1, color); // Right
  }
}
