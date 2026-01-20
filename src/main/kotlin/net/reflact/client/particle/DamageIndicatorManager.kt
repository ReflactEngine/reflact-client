package net.reflact.client.particle

import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.render.Camera
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import org.joml.Matrix4f
import org.joml.Quaternionf
import java.util.Random
import java.util.ArrayList

object DamageIndicatorManager {
    private val indicators = ArrayList<Indicator>()
    private val random = Random()

    fun add(x: Double, y: Double, z: Double, amount: Int, crit: Boolean) {
        indicators.add(Indicator(x, y, z, amount, crit))
    }

    fun render(matrices: MatrixStack, consumers: VertexConsumerProvider, camera: Camera) {
        if (indicators.isEmpty()) return

        val textRenderer = MinecraftClient.getInstance().textRenderer
        val camPos = Vec3d(MinecraftClient.getInstance().player!!.x, MinecraftClient.getInstance().player!!.y, MinecraftClient.getInstance().player!!.z)
        val camRot = camera.rotation
        val tickDelta = MinecraftClient.getInstance().renderTickCounter.getTickProgress(true)

        val iterator = indicators.iterator()
        while (iterator.hasNext()) {
            val indicator = iterator.next()
            indicator.update()

            if (indicator.isExpired) {
                iterator.remove()
                continue
            }

            val x = MathHelper.lerp(tickDelta.toDouble(), indicator.prevX, indicator.x)
            val y = MathHelper.lerp(tickDelta.toDouble(), indicator.prevY, indicator.y)
            val z = MathHelper.lerp(tickDelta.toDouble(), indicator.prevZ, indicator.z)

            matrices.push()
            matrices.translate(x - camPos.x, y - camPos.y, z - camPos.z)
            matrices.multiply(camRot)
            matrices.scale(-0.025f, -0.025f, 0.025f)

            // Scale up for crit
            if (indicator.crit) {
                matrices.scale(1.5f, 1.5f, 1.5f)
            }

            val text = indicator.amount.toString()
            val color = if (indicator.crit) 0xFFFF5555.toInt() else 0xFFFFFFFF.toInt() // Red for crit, White normal
            val width = textRenderer.getWidth(text)

            // Draw text centered
            val matrix = matrices.peek().positionMatrix
            textRenderer.draw(
                text, -width / 2f, 0f, color, false, matrix, consumers, TextRenderer.TextLayerType.SEE_THROUGH,
                0, 0xF000F0
            )
            textRenderer.draw(
                text, -width / 2f, 0f, color, false, matrix, consumers, TextRenderer.TextLayerType.NORMAL, 0,
                0xF000F0
            )

            matrices.pop()
        }
    }

    private class Indicator(
        var x: Double,
        var y: Double,
        var z: Double,
        var amount: Int,
        var crit: Boolean
    ) {
        var prevX: Double = x
        var prevY: Double = y
        var prevZ: Double = z
        var vy: Double = 0.1 + random.nextDouble() * 0.1
        var age: Int = 0
        var maxAge: Int = 40

        fun update() {
            prevX = x
            prevY = y
            prevZ = z

            age++
            y += vy
            vy *= 0.9 // Drag
        }

        val isExpired: Boolean
            get() = age >= maxAge
    }
}
