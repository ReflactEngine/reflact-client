package net.reflact.client.particle;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class DamageIndicatorManager {
  private static final List<Indicator> indicators = new ArrayList<>();
  private static final Random random = new Random();

  public static void add(double x, double y, double z, int amount, boolean crit) {
    indicators.add(new Indicator(x, y, z, amount, crit));
  }

  public static void render(MatrixStack matrices, VertexConsumerProvider consumers, Camera camera) {
    if (indicators.isEmpty()) return;
    
    TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
    Vec3d camPos = new Vec3d(MinecraftClient.getInstance().player.getX(), MinecraftClient.getInstance().player.getY(), MinecraftClient.getInstance().player.getZ()); // camera.getPos();
    Quaternionf camRot = camera.getRotation();
    float tickDelta = MinecraftClient.getInstance().getRenderTickCounter().getTickProgress(true);

    Iterator<Indicator> iterator = indicators.iterator();
    while (iterator.hasNext()) {
      Indicator indicator = iterator.next();
      indicator.update();

      if (indicator.isExpired()) {
        iterator.remove();
        continue;
      }

      double x = MathHelper.lerp(tickDelta, indicator.prevX, indicator.x);
      double y = MathHelper.lerp(tickDelta, indicator.prevY, indicator.y);
      double z = MathHelper.lerp(tickDelta, indicator.prevZ, indicator.z);

      matrices.push();
      matrices.translate(x - camPos.x, y - camPos.y, z - camPos.z);
      matrices.multiply(camRot);
      matrices.scale(-0.025f, -0.025f, 0.025f);

      // Scale up for crit
      if (indicator.crit) {
        matrices.scale(1.5f, 1.5f, 1.5f);
      }

      String text = String.valueOf(indicator.amount);
      int color = indicator.crit ? 0xFFFF5555 : 0xFFFFFFFF; // Red for crit, White normal
      int width = textRenderer.getWidth(text);

      // Draw text centered
      Matrix4f matrix = matrices.peek().getPositionMatrix();
      textRenderer.draw(text, -width / 2f, 0, color, false, matrix, consumers, TextRenderer.TextLayerType.SEE_THROUGH,
          0, 0xF000F0);
      textRenderer.draw(text, -width / 2f, 0, color, false, matrix, consumers, TextRenderer.TextLayerType.NORMAL, 0,
          0xF000F0);

      matrices.pop();
    }
  }

  private static class Indicator {
    double x, y, z;
    double prevX, prevY, prevZ;
    double vy;
    int amount;
    boolean crit;
    int age;
    int maxAge = 40;

    public Indicator(double x, double y, double z, int amount, boolean crit) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.prevX = x;
      this.prevY = y;
      this.prevZ = z;
      this.amount = amount;
      this.crit = crit;
      this.vy = 0.1 + random.nextDouble() * 0.1;
    }

    public void update() {
      prevX = x;
      prevY = y;
      prevZ = z;

      age++;
      y += vy;
      vy *= 0.9; // Drag
    }

    public boolean isExpired() {
      return age >= maxAge;
    }
  }
}
