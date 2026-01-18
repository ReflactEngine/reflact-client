package net.reflact.client.hud.overlay;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.reflact.client.ReflactClient;

public class MinimapOverlay implements ReflactOverlay {
    
    private static final Identifier BORDER_TEXTURE = Identifier.of("reflact", "textures/map/wynn_map_textures.png");
    private static final Identifier POINTER_TEXTURE = Identifier.of("reflact", "textures/map/map_pointers.png");

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
        // We use a stencil or scissor to clip content to the circle/square.
        // For simplicity, we just render the square content first.
        
        context.enableScissor(x, y, x + size, y + size);
        
        // Background color
        context.fill(x, y, x + size, y + size, 0xFF000000); 

        // Optimised Block Renderer
        // Only render every 2nd or 4th pixel to reduce lag ("jank")
        int range = 24; 
        int steps = 2; // pixel step
        int blockSize = size / (range * 2);
        if (blockSize < 1) blockSize = 1;

        int pX = client.player.getBlockX();
        int pZ = client.player.getBlockZ();
        
        // Render blocks
        for (int dx = -range; dx <= range; dx++) {
            for (int dz = -range; dz <= range; dz++) {
                int worldX = pX + dx;
                int worldZ = pZ + dz;
                
                // Get highest block - use HEIGHTMAP for speed
                int worldY = client.world.getTopY(net.minecraft.world.Heightmap.Type.MOTION_BLOCKING, worldX, worldZ);
                net.minecraft.util.math.BlockPos pos = new net.minecraft.util.math.BlockPos(worldX, worldY - 1, worldZ);
                net.minecraft.block.BlockState state = client.world.getBlockState(pos);
                
                int color = state.getMapColor(client.world, pos).color;
                if (color == 0) continue; 
                
                int mapPixelX = centerX + (dx * blockSize);
                int mapPixelY = centerY + (dz * blockSize);
                
                // Only draw if within bounds
                if (mapPixelX >= x && mapPixelX < x + size && mapPixelY >= y && mapPixelY < y + size) {
                     color = color | 0xFF000000;
                     context.fill(mapPixelX, mapPixelY, mapPixelX + blockSize, mapPixelY + blockSize, color);
                }
            }
        }
        
        // Entities (Radar)
        float rangeEntities = 32.0f; 
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

        // 3. Render Player Pointer
        // Center of map
        // Texture: map_pointers.png. Assuming 16x16, drawing top-left 8x8.
        float u1 = 0f;
        float u2 = 0.5f;
        float v1 = 0f;
        float v2 = 0.5f;
        context.drawTexturedQuad(POINTER_TEXTURE, centerX - 4, centerX + 4, centerY - 4, centerY + 4, u1, u2, v1, v2); 
    }
    @Override public int getX() { return ReflactClient.CONFIG.radarX(); }
    @Override public int getY() { return ReflactClient.CONFIG.radarY(); }
    @Override public void setX(int x) { ReflactClient.CONFIG.radarX(x); }
    @Override public void setY(int y) { ReflactClient.CONFIG.radarY(y); }
    @Override public int getWidth() { return ReflactClient.CONFIG.radarSize(); }
    @Override public int getHeight() { return ReflactClient.CONFIG.radarSize(); }
    @Override public boolean isEnabled() { return ReflactClient.CONFIG.showEntityRadar(); }
    @Override public String getName() { return "Minimap"; }
}
