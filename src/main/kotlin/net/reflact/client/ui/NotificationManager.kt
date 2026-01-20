package net.reflact.client.ui

import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text
import java.util.ArrayList

object NotificationManager {
    private val notifications: MutableList<Notification> = ArrayList()

    fun add(text: Text, color: Int) {
        notifications.add(Notification(text, color))
    }

    fun render(context: DrawContext, textRenderer: TextRenderer, screenWidth: Int, screenHeight: Int) {
        if (notifications.isEmpty()) return

        var y = screenHeight / 4 // Start at top quarter
        val centerX = screenWidth / 2

        val iterator = notifications.iterator()
        while (iterator.hasNext()) {
            val notification = iterator.next()
            notification.update()

            if (notification.isExpired) {
                iterator.remove()
                continue
            }

            // Alpha logic omitted in Java version, omitted here too for simplicity

            val width = textRenderer.getWidth(notification.text) + 10
            val height = 12
            val x = centerX - width / 2

            // Background
            context.fill(x, y, x + width, y + height, 0x80000000.toInt())

            // Border
            val borderColor = 0xFFFFFFFF.toInt()
            context.fill(x, y, x + width, y + 1, borderColor) // Top
            context.fill(x, y + height - 1, x + width, y + height, borderColor) // Bottom
            context.fill(x, y, x + 1, y + height, borderColor) // Left
            context.fill(x + width - 1, y, x + width, y + height, borderColor) // Right

            // Text
            context.drawText(
                textRenderer,
                notification.text,
                centerX - textRenderer.getWidth(notification.text) / 2,
                y + 2,
                notification.color,
                true
            )

            y += height + 2 // Stack down
        }
    }

    private class Notification(var text: Text, var color: Int) {
        var startTime: Long = System.currentTimeMillis()
        var duration: Long = 3000 // 3 seconds

        fun update() {
            // Animation logic could go here
        }

        val isExpired: Boolean
            get() = System.currentTimeMillis() - startTime > duration

        val alpha: Float
            get() {
                val age = System.currentTimeMillis() - startTime
                if (age > duration - 500) {
                    return (duration - age) / 500f
                }
                return 1.0f
            }
    }
}
