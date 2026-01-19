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
            
        float halfWidth = width / 2f;
        float halfHeight = height / 2f;

        // Render the dynamic map texture
        // We use drawTexture with custom UVs if needed, or just the whole texture if it matches the view
        // For simplicity, we assume the texture covers the area we want to draw.
        // In a real scrolling map, we'd adjust UVs based on player position relative to the texture center.
        
        // renderX/Y is top-left
        int x = (int)(centerX - halfWidth);
        int y = (int)(centerZ - halfHeight);
        int w = (int)width;
        int h = (int)height;
        
        // Draw the full texture stretched to the box (placeholder logic)
        // Ideally we want to draw a subsection if we are zooming.
        // But MapTextureManager updates the texture to be CENTERED on the player.
        // So we can just draw the whole texture.
        
        context.drawTexturedQuad(
            texture,
            x, x + w, 
            y, y + h, 
            0f, 1f, 
            0f, 1f
        );
        
        // Draw grid lines to show it's a map (Optional, maybe remove later)
        /*
        int gridStep = 50;
        for (float gx = centerX - halfWidth; gx < centerX + halfWidth; gx += gridStep) {
             context.fill((int)gx, (int)(centerZ - halfHeight), (int)gx + 1, (int)(centerZ + halfHeight), 0x44888888);
        }
        for (float gy = centerZ - halfHeight; gy < centerZ + halfHeight; gy += gridStep) {
             context.fill((int)(centerX - halfWidth), (int)gy, (int)(centerX + halfWidth), (int)gy + 1, 0x44888888);
        }
        */
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
