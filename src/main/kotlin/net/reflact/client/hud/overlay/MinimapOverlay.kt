package net.reflact.client.hud.overlay

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Identifier
import net.reflact.client.ReflactClient
import net.reflact.client.map.MapTextureManager
import net.reflact.client.map.ReflactMapRenderer
import org.joml.Quaternionf
import kotlin.math.abs

class MinimapOverlay : ReflactOverlay {

    private val mapManager = MapTextureManager.INSTANCE

    override fun render(context: DrawContext, tickDelta: Float) {
        val client = MinecraftClient.getInstance()
        if (client.player == null) return

        val x = x
        val y = y
        val size = ReflactClient.CONFIG.radarSize() // Configurable size
        val centerX = x + size / 2
        val centerY = y + size / 2

        // 1. Render Map Background (The World)
        context.enableScissor(x, y, x + size, y + size)

        // Background color
        context.fill(x, y, x + size, y + size, 0xFF000000.toInt())

        // Update and Render Map Texture
        mapManager.update(client)

        ReflactMapRenderer.renderMapTexture(
            context,
            mapManager.textureId,
            centerX.toFloat(), centerY.toFloat(),
            0f, 0f, // Texture offsets not fully used yet
            size.toFloat(), size.toFloat(),
            1.0f,
            128, 128 // Texture size from Manager
        )

        // Entities (Radar)
        // Range should match the Map Texture Radius
        // Map covers MAP_SIZE blocks. Radius = MAP_SIZE / 2.
        val rangeEntities = MapTextureManager.MAP_SIZE / 2.0f

        for (entity in client.world!!.entities) {
            if (entity === client.player) continue
            if (entity !is LivingEntity) continue

            val dx = entity.x - client.player!!.x
            val dz = entity.z - client.player!!.z

            if (abs(dx) < rangeEntities && abs(dz) < rangeEntities) {
                var mapX = (centerX + (dx / rangeEntities) * (size / 2)).toInt()
                var mapY = (centerY + (dz / rangeEntities) * (size / 2)).toInt()

                // Clamp
                mapX = x.coerceAtLeast((x + size).coerceAtMost(mapX))
                mapY = y.coerceAtLeast((y + size).coerceAtMost(mapY))

                val color = if (entity is PlayerEntity) 0xFF00FF00.toInt() else 0xFFFF0000.toInt() // Green / Red
                context.fill(mapX - 1, mapY - 1, mapX + 2, mapY + 2, color)
            }
        }

        context.disableScissor()

        // 2. Render Border Texture (Overlay)
        // Draw a simple border manually
        val borderColor = 0xFFCCCCCC.toInt()
        context.fill(x - 1, y - 1, x + size + 1, y, borderColor) // Top
        context.fill(x - 1, y + size, x + size + 1, y + size + 1, borderColor) // Bottom
        context.fill(x - 1, y, x, y + size, borderColor) // Left
        context.fill(x + size, y, x + size + 1, y + size, borderColor) // Right

        // 3. Render Player Pointer (Textured)
        context.matrices.pushMatrix()
        context.matrices.translate(centerX.toFloat(), centerY.toFloat())
        context.matrices.rotate(Math.toRadians((client.player!!.yaw + 180).toDouble()).toFloat())

        val pSize = 16
        context.drawTexturedQuad(POINTER_TEXTURE, -pSize / 2, pSize / 2, -pSize / 2, pSize / 2, 0f, 1f, 0f, 1f)

        context.matrices.popMatrix()
    }

    override var x: Int
        get() = ReflactClient.CONFIG.radarX()
        set(value) { ReflactClient.CONFIG.radarX(value) }
    override var y: Int
        get() = ReflactClient.CONFIG.radarY()
        set(value) { ReflactClient.CONFIG.radarY(value) }
    override var width: Int
        get() = ReflactClient.CONFIG.radarSize()
        set(value) { ReflactClient.CONFIG.radarSize(value) }
    override var height: Int
        get() = ReflactClient.CONFIG.radarSize()
        set(value) { ReflactClient.CONFIG.radarSize(value) } // Keep square

    override val isEnabled: Boolean get() = ReflactClient.CONFIG.showEntityRadar()
    override val name: String get() = "Minimap"

    companion object {
        private val BORDER_TEXTURE = Identifier.of("reflact", "textures/map/wynn_map_textures.png")
        private val POINTER_TEXTURE = Identifier.of("reflact", "textures/map/map_pointers.png")
    }
}
