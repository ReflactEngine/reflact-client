package net.reflact.client.ui;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NotificationManager {
    private static final List<Notification> notifications = new ArrayList<>();

    public static void add(Text text, int color) {
        notifications.add(new Notification(text, color));
    }

    public static void render(DrawContext context, TextRenderer textRenderer, int screenWidth, int screenHeight) {
        if (notifications.isEmpty()) return;

        int y = screenHeight / 4; // Start at top quarter
        int centerX = screenWidth / 2;

        Iterator<Notification> iterator = notifications.iterator();
        while (iterator.hasNext()) {
            Notification notification = iterator.next();
            notification.update();

            if (notification.isExpired()) {
                iterator.remove();
                continue;
            }

            float alpha = notification.getAlpha();
            int color = notification.color;
            if (alpha < 1.0f) {
                // Fade out logic if we wanted to manipulate ARGB, but textRenderer usually takes full int.
                // We'll just rely on the lifecycle for now or simple alpha blending if supported.
                // For simplicity, we just stop rendering when expired.
            }

            int width = textRenderer.getWidth(notification.text) + 10;
            int height = 12;
            int x = centerX - width / 2;

            // Background
            context.fill(x, y, x + width, y + height, 0x80000000);
            
            // Border
            int borderColor = 0xFFFFFFFF;
            context.fill(x, y, x + width, y + 1, borderColor); // Top
            context.fill(x, y + height - 1, x + width, y + height, borderColor); // Bottom
            context.fill(x, y, x + 1, y + height, borderColor); // Left
            context.fill(x + width - 1, y, x + width, y + height, borderColor); // Right
            
            // Text
            context.drawText(textRenderer, notification.text, centerX - textRenderer.getWidth(notification.text) / 2, y + 2, color, true);

            y += height + 2; // Stack down
        }
    }

    private static class Notification {
        Text text;
        int color;
        long startTime;
        long duration = 3000; // 3 seconds

        public Notification(Text text, int color) {
            this.text = text;
            this.color = color;
            this.startTime = System.currentTimeMillis();
        }

        public void update() {
            // Animation logic could go here
        }

        public boolean isExpired() {
            return System.currentTimeMillis() - startTime > duration;
        }

        public float getAlpha() {
            long age = System.currentTimeMillis() - startTime;
            if (age > duration - 500) {
                return (duration - age) / 500f;
            }
            return 1.0f;
        }
    }
}
