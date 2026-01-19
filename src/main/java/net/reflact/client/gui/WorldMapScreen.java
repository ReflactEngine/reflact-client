package net.reflact.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.reflact.client.map.MapTextureManager;
import net.reflact.client.map.ReflactMapRenderer;
import org.lwjgl.glfw.GLFW;
import net.minecraft.client.gui.Click;
import net.minecraft.client.input.KeyInput;

public class WorldMapScreen extends Screen {

    // Zoom constants
    private static final float MIN_ZOOM_LEVEL = 1f;
    private static final float MAX_ZOOM_LEVEL = 100f;
    
    // Map state
    private float zoomLevel = 60f;
    private float mapCenterX;
    private float mapCenterZ;
    private boolean isDragging = false;
    
    // Rendering state
    private float centerX;
    private float centerZ;
    private float mapWidth;
    private float mapHeight;
    private float zoomRenderScale;

    // We reuse the MapTextureManager. 
    private final MapTextureManager mapManager = MapTextureManager.INSTANCE;
    
    private static final Identifier POINTER_TEXTURE = Identifier.of("reflact", "textures/map/map_pointers.png");

    public WorldMapScreen() {
        super(Text.of("World Map"));
    }

    @Override
    protected void init() {
        super.init();
        // Default to player position
        if (client.player != null) {
            this.mapCenterX = (float) client.player.getX();
            this.mapCenterZ = (float) client.player.getZ();
        }
        
        // Full screen with margin
        float margin = 20;
        this.mapWidth = width - margin * 2;
        this.mapHeight = height - margin * 2;
        this.centerX = width / 2f;
        this.centerZ = height / 2f;
        
        updateZoomScale();
    }
    
    private void updateZoomScale() {
        this.zoomRenderScale = ReflactMapRenderer.getZoomRenderScaleFromLevel(zoomLevel);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Fix: Removed renderBackground(context, mouseX, mouseY, delta) which was causing blur issues
        // We render our own background manually below.
        
        // Update map data (scans chunks)
        mapManager.update(client);
        
        // Render Map Background (The void)
        context.fill(0, 0, width, height, 0xFF000000);
                     
        // Render Map Content
        // The MapTextureManager holds a texture centered on the *Player*.
        // We need to render it relative to the *Map Center* (Camera).
        
        if (client.player != null) {
            float playerX = (float) client.player.getX();
            float playerZ = (float) client.player.getZ();
            
            // Calculate where the player is relative to the map center (camera)
            // If camera is at (0,0) and player is at (10,10), relative is (10,10).
            // Screen position = ScreenCenter + Relative * Zoom
            
            float relPlayerX = playerX - mapCenterX;
            float relPlayerZ = playerZ - mapCenterZ;
            
            float screenPlayerX = centerX + relPlayerX * zoomRenderScale;
            float screenPlayerZ = centerZ + relPlayerZ * zoomRenderScale;
            
            // The texture itself is 128x128 blocks centered on the player.
            // So we draw the texture at screenPlayerX/Z with size = 128 * zoomRenderScale
            
            float renderedSize = 128 * zoomRenderScale; // 128 blocks * zoom
            
            ReflactMapRenderer.renderMapTexture(
                context,
                mapManager.getTextureId(),
                screenPlayerX,
                screenPlayerZ,
                0, 0,
                renderedSize,
                renderedSize,
                zoomRenderScale,
                128, 128
            );
            
            // Render Waypoints
            for (net.reflact.client.ClientData.Waypoint wp : net.reflact.client.ClientData.waypoints) {
                 float wpRelX = wp.x - mapCenterX;
                 float wpRelZ = wp.z - mapCenterZ;
                 float wpScreenX = centerX + wpRelX * zoomRenderScale;
                 float wpScreenZ = centerZ + wpRelZ * zoomRenderScale;
                 
                 context.fill((int)wpScreenX - 2, (int)wpScreenZ - 2, (int)wpScreenX + 2, (int)wpScreenZ + 2, wp.color);
                 context.drawTextWithShadow(textRenderer, wp.name, (int)wpScreenX + 4, (int)wpScreenZ - 4, wp.color);
            }
            
            // Render Player Pointer
            renderCursor(context, screenPlayerX, screenPlayerZ, client.player.getYaw());
        }
        
        // Draw Border
        int bx = (int)(centerX - mapWidth/2 - 1);
        int by = (int)(centerZ - mapHeight/2 - 1);
        int bw = (int)mapWidth + 2;
        int bh = (int)mapHeight + 2;
        int borderColor = 0xFFFFFFFF;
        
        context.fill(bx, by, bx + bw, by + 1, borderColor); // Top
        context.fill(bx, by + bh - 1, bx + bw, by + bh, borderColor); // Bottom
        context.fill(bx, by + 1, bx + 1, by + bh - 1, borderColor); // Left
        context.fill(bx + bw - 1, by + 1, bx + bw, by + bh - 1, borderColor); // Right
        
        // UI Text
        context.drawCenteredTextWithShadow(textRenderer, "World Map (" + (int)mapCenterX + ", " + (int)mapCenterZ + ")", width / 2, 10, 0xFFFFFF);
        context.drawCenteredTextWithShadow(textRenderer, "Zoom: " + (int)zoomLevel, width / 2, height - 15, 0xAAAAAA);
        
        super.render(context, mouseX, mouseY, delta);
    }
    
    private void renderCursor(DrawContext context, float x, float y, float yaw) {
        context.getMatrices().pushMatrix();
        context.getMatrices().translate(x, y);
        context.getMatrices().rotate((float) Math.toRadians(yaw + 180));
        
        int arrowColor = 0xFFAAAAAA;
        // Simple pixel arrow centered at 0,0
        context.fill(-1, -4, 1, -2, arrowColor);
        context.fill(-2, -2, 2, 0, arrowColor);
        context.fill(-3, 0, 3, 2, arrowColor);
        context.fill(-4, 2, 4, 4, arrowColor);
        
        context.getMatrices().popMatrix();
    }
    
    @Override
    public boolean mouseDragged(Click click, double deltaX, double deltaY) {
        if (click.button() == 0) { // Left click drag
            // Move map center opposite to drag
            mapCenterX -= deltaX / zoomRenderScale;
            mapCenterZ -= deltaY / zoomRenderScale;
            return true;
        }
        return super.mouseDragged(click, deltaX, deltaY);
    }
    
    // Removed old signature method if it exists to avoid confusion, using correct fabric/yarn mapping
    // mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) is the standard
    
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (verticalAmount != 0) {
            zoomLevel += verticalAmount * 5;
            zoomLevel = MathHelper.clamp(zoomLevel, MIN_ZOOM_LEVEL, MAX_ZOOM_LEVEL);
            updateZoomScale();
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }
    
    @Override
    public boolean keyPressed(KeyInput input) {
        if (input.key() == GLFW.GLFW_KEY_M || input.key() == GLFW.GLFW_KEY_ESCAPE) {
            this.close();
            return true;
        }
        return super.keyPressed(input);
    }
    
    @Override
    public void close() {
        // mapManager.close(); // Don't close singleton
        super.close();
    }
}
