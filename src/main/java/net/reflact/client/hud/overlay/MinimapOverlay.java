package net.reflact.client.hud.overlay;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.reflact.client.ReflactClient;
import net.reflact.client.map.MapTextureManager;
import net.reflact.client.map.ReflactMapRenderer;

public class MinimapOverlay implements ReflactOverlay {
    
    private static final Identifier BORDER_TEXTURE = Identifier.of("reflact", "textures/map/wynn_map_textures.png");
    private static final Identifier POINTER_TEXTURE = Identifier.of("reflact", "textures/map/map_pointers.png");

    private final MapTextureManager mapManager = MapTextureManager.INSTANCE;

    @Override
    public void render(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        int x = getX();
        int y = getY();
        int size = ReflactClient.CONFIG.radarSize(); // Configurable size
        int centerX = x + size / 2;
        int centerY = y + size / 2;
        
        // 1. Render Map Background (The World)
        context.enableScissor(x, y, x + size, y + size);
        
        // Background color
        context.fill(x, y, x + size, y + size, 0xFF000000); 

        // Update and Render Map Texture
        mapManager.update(client);
        
        ReflactMapRenderer.renderMapTexture(
            context,
            mapManager.getTextureId(),
            centerX, centerY,
            0, 0, // Texture offsets not fully used yet
            size, size,
            1.0f,
            128, 128 // Texture size from Manager
        );
        
        // Entities (Radar)
        // Range should match the Map Texture Radius
        // Map covers MAP_SIZE blocks. Radius = MAP_SIZE / 2.
        float rangeEntities = MapTextureManager.MAP_SIZE / 2.0f; 
        
        for (Entity entity : client.world.getEntities()) {
            if (entity == client.player) continue;
            if (!(entity instanceof LivingEntity)) continue;
            
            double dx = entity.getX() - client.player.getX();
            double dz = entity.getZ() - client.player.getZ();
            
            if (Math.abs(dx) < rangeEntities && Math.abs(dz) < rangeEntities) {
                int mapX = (int) (centerX + (dx / rangeEntities) * (size / 2));
                int mapY = (int) (centerY + (dz / rangeEntities) * (size / 2));
                
                // Clamp
                mapX = Math.max(x, Math.min(x + size, mapX));
                mapY = Math.max(y, Math.min(y + size, mapY));
                
                int color = (entity instanceof PlayerEntity) ? 0xFF00FF00 : 0xFFFF0000; // Green / Red
                context.fill(mapX - 1, mapY - 1, mapX + 2, mapY + 2, color);
            }
        }

        context.disableScissor();
        
        // 2. Render Border Texture (Overlay)
        // Adjust UV to pick a specific border style from wynn_map_textures.png
        // Assuming the texture is a grid of borders. Let's try drawing the whole thing or a region.
        // Wynntils texture is likely 512x512 or similar with multiple borders.
        // For now, we draw it stretched or just a frame. 
        // Let's use drawing textured quad.
        
        // context.drawTexture(BORDER_TEXTURE, x - 10, y - 10, 0, 0, size + 20, size + 20, 512, 512); 
        // Note: UVs need to be correct. Let's assume top-left border for now.
        
        // Draw a simple border manually
        int borderColor = 0xFFCCCCCC;
        context.fill(x - 1, y - 1, x + size + 1, y, borderColor); // Top
        context.fill(x - 1, y + size, x + size + 1, y + size + 1, borderColor); // Bottom
        context.fill(x - 1, y, x, y + size, borderColor); // Left
        context.fill(x + size, y, x + size + 1, y + size, borderColor); // Right

        // 3. Render Player Pointer (Manual Shape to avoid texture issues)
        context.getMatrices().pushMatrix();
        context.getMatrices().translate(centerX, centerY);
        context.getMatrices().rotate((float) Math.toRadians(client.player.getYaw() + 180));
        
        // Draw a simple arrow using fills
        // Center is 0,0
        // Tip at (0, -4), Left at (-3, 3), Right at (3, 3), Indent at (0, 1)
        
        // We can't use fill for triangles easily without BufferBuilder.
        // Let's draw a rotated square or simple rectangle for now, or attempt a small pixel art arrow.
        // 0xFFRed
        int arrowColor = 0xFFAAAAAA; 
        
        // Tip
        context.fill(-1, -4, 1, -2, arrowColor);
        context.fill(-2, -2, 2, 0, arrowColor);
        context.fill(-3, 0, 3, 2, arrowColor);
        context.fill(-4, 2, 4, 4, arrowColor);
        
        context.getMatrices().popMatrix();
    }
    @Override public int getX() { return ReflactClient.CONFIG.radarX(); }
    @Override public int getY() { return ReflactClient.CONFIG.radarY(); }
    @Override public void setX(int x) { ReflactClient.CONFIG.radarX(x); }
    @Override public void setY(int y) { ReflactClient.CONFIG.radarY(y); }
    @Override public int getWidth() { return ReflactClient.CONFIG.radarSize(); }
    @Override public int getHeight() { return ReflactClient.CONFIG.radarSize(); }
    @Override public boolean isEnabled() { return ReflactClient.CONFIG.showEntityRadar(); }
    @Override public String getName() { return "Minimap"; }
    
    @Override public void setWidth(int width) { ReflactClient.CONFIG.radarSize(width); }
    @Override public void setHeight(int height) { ReflactClient.CONFIG.radarSize(height); } // Keep square
}
