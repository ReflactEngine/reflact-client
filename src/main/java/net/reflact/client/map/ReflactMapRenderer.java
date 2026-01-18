package net.reflact.client.map;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class ReflactMapRenderer {

    // Zoom Levels logic similar to Wynntils
    public static final float MIN_ZOOM = 0.2f;
    public static final float MAX_ZOOM = 10f;
    public static final int ZOOM_LEVELS = 100;
    
    // Simple zoom calculation without GUI scale complexity for now
    public static float getZoomRenderScaleFromLevel(float zoomLevel) {
        float t = (zoomLevel - 1) / (float) (ZOOM_LEVELS - 1);
        return MIN_ZOOM + t * (MAX_ZOOM - MIN_ZOOM);
    }

    public static void renderMapTexture(
            DrawContext context,
            Identifier texture,
            float centerX,
            float centerZ,
            float textureX,
            float textureZ,
            float width,
            float height,
            float scale,
            int textureWidth,
            int textureHeight) {
            
        // Placeholder render to fix compilation issues with DrawContext/VertexFormat
        // In a real implementation, we would use proper BufferBuilder logic here.
        float halfWidth = width / 2f;
        float halfHeight = height / 2f;
        
        context.fill(
            (int)(centerX - halfWidth), 
            (int)(centerZ - halfHeight), 
            (int)(centerX + halfWidth), 
            (int)(centerZ + halfHeight), 
            0xFF555555 // Grey placeholder
        );
        
        // Draw grid lines to show it's a map
        int gridStep = 50;
        for (float x = centerX - halfWidth; x < centerX + halfWidth; x += gridStep) {
             context.fill((int)x, (int)(centerZ - halfHeight), (int)x + 1, (int)(centerZ + halfHeight), 0xFF888888);
        }
        for (float y = centerZ - halfHeight; y < centerZ + halfHeight; y += gridStep) {
             context.fill((int)(centerX - halfWidth), (int)y, (int)(centerX + halfWidth), (int)y + 1, 0xFF888888);
        }
    }
    
    // Helper to get render position
    public static float getRenderX(float worldX, float mapCenterX, float centerX, float currentZoom) {
        float distanceX = worldX - mapCenterX;
        return centerX + distanceX * currentZoom;
    }

    public static float getRenderZ(float worldZ, float mapCenterZ, float centerZ, float currentZoom) {
        float distanceZ = worldZ - mapCenterZ;
        return centerZ + distanceZ * currentZoom;
    }
}
