package net.reflact.client.map

import net.minecraft.client.gui.DrawContext
import net.minecraft.util.Identifier

object ReflactMapRenderer {

    // Zoom Levels logic similar to Wynntils
    const val MIN_ZOOM = 0.2f
    const val MAX_ZOOM = 10f
    const val ZOOM_LEVELS = 100

    // Simple zoom calculation without GUI scale complexity for now
    fun getZoomRenderScaleFromLevel(zoomLevel: Float): Float {
        val t = (zoomLevel - 1) / (ZOOM_LEVELS - 1).toFloat()
        return MIN_ZOOM + t * (MAX_ZOOM - MIN_ZOOM)
    }

    fun renderMapTexture(
        context: DrawContext,
        texture: Identifier,
        centerX: Float,
        centerZ: Float,
        textureX: Float,
        textureZ: Float,
        width: Float,
        height: Float,
        scale: Float,
        textureWidth: Int,
        textureHeight: Int
    ) {

        val halfWidth = width / 2f
        val halfHeight = height / 2f

        // renderX/Y is top-left
        val x = (centerX - halfWidth).toInt()
        val y = (centerZ - halfHeight).toInt()
        val w = width.toInt()
        val h = height.toInt()

        context.drawTexturedQuad(
            texture,
            x, x + w,
            y, y + h,
            0f, 1f,
            0f, 1f
        )
    }

    // Helper to get render position
    fun getRenderX(worldX: Float, mapCenterX: Float, centerX: Float, currentZoom: Float): Float {
        val distanceX = worldX - mapCenterX
        return centerX + distanceX * currentZoom
    }

    fun getRenderZ(worldZ: Float, mapCenterZ: Float, centerZ: Float, currentZoom: Float): Float {
        val distanceZ = worldZ - mapCenterZ
        return centerZ + distanceZ * currentZoom
    }
}
