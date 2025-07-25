/* Decompiler 16ms, total 343ms, lines 114 */
package wtf.evolution.helpers.render;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class RoundedUtil {
   public static ShaderUtil roundedShader = new ShaderUtil("roundedRect");
   public static ShaderUtil roundedOutlineShader = new ShaderUtil("skidproject/shaders/outline.frag");
   private static final ShaderUtil roundedTexturedShader = new ShaderUtil("skidproject/shaders/round.frag");
   private static final ShaderUtil roundedGradientShader = new ShaderUtil("roundedRectGradient");

   public static void drawRound(float x, float y, float width, float height, float radius, Color color) {
      drawRound(x, y, width, height, radius, false, color);
   }

   public static void drawRoundScale(float x, float y, float width, float height, float radius, Color color, float scale) {
      drawRound(x + width - width * scale, y + height / 2.0F - height / 2.0F * scale, width * scale, height * scale, radius, false, color);
   }

   public static void drawRoundCircle(float x, float y, float radius, Color color) {
      drawRound(x - radius / 2.0F, y - radius / 2.0F, radius, radius, radius / 2.0F - 0.5F, color);
   }

   public static void drawRoundCircleOut(float x, float y, float radius, float thikness, Color color, Color sidecolor) {
      drawRoundOutline(x - radius / 2.0F, y - radius / 2.0F, radius, radius, radius / 2.0F - 0.5F, thikness, color, sidecolor);
   }

   public static void drawGradientHorizontal(float x, float y, float width, float height, float radius, Color left, Color right) {
      drawGradientRound(x, y, width, height, radius, left, left, right, right);
   }

   public static void drawGradientVertical(float x, float y, float width, float height, float radius, Color top, Color bottom) {
      drawGradientRound(x, y, width, height, radius, bottom, top, bottom, top);
   }

   public static void drawGradientRound(float x, float y, float width, float height, float radius, Color bottomLeft, Color topLeft, Color bottomRight, Color topRight) {
      RenderUtil.resetColor();
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(770, 771);
      roundedGradientShader.init();
      setupRoundedRectUniforms(x, y, width, height, radius, roundedGradientShader);
      roundedGradientShader.setUniformf("color1", new float[]{(float)bottomLeft.getRed() / 255.0F, (float)bottomLeft.getGreen() / 255.0F, (float)bottomLeft.getBlue() / 255.0F, 0.3137255F});
      roundedGradientShader.setUniformf("color2", new float[]{(float)topLeft.getRed() / 255.0F, (float)topLeft.getGreen() / 255.0F, (float)topLeft.getBlue() / 255.0F, 0.3137255F});
      roundedGradientShader.setUniformf("color3", new float[]{(float)bottomRight.getRed() / 255.0F, (float)bottomRight.getGreen() / 255.0F, (float)bottomRight.getBlue() / 255.0F, 0.3137255F});
      roundedGradientShader.setUniformf("color4", new float[]{(float)topRight.getRed() / 255.0F, (float)topRight.getGreen() / 255.0F, (float)topRight.getBlue() / 255.0F, 0.3137255F});
      ShaderUtil.drawQuads(x - 1.0F, y - 1.0F, width + 2.0F, height + 2.0F);
      roundedGradientShader.unload();
      GlStateManager.disableBlend();
   }

   public static void drawGradientRoundA(float x, float y, float width, float height, float radius, Color bottomLeft, Color topLeft, Color bottomRight, Color topRight) {
      RenderUtil.resetColor();
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(770, 771);
      roundedGradientShader.init();
      setupRoundedRectUniforms(x, y, width, height, radius, roundedGradientShader);
      roundedGradientShader.setUniformf("color1", new float[]{(float)bottomLeft.getRed() / 255.0F, (float)bottomLeft.getGreen() / 255.0F, (float)bottomLeft.getBlue() / 255.0F, (float)bottomLeft.getAlpha() / 255.0F});
      roundedGradientShader.setUniformf("color2", new float[]{(float)topLeft.getRed() / 255.0F, (float)topLeft.getGreen() / 255.0F, (float)topLeft.getBlue() / 255.0F, (float)topLeft.getAlpha() / 255.0F});
      roundedGradientShader.setUniformf("color3", new float[]{(float)bottomRight.getRed() / 255.0F, (float)bottomRight.getGreen() / 255.0F, (float)bottomRight.getBlue() / 255.0F, (float)bottomRight.getAlpha() / 255.0F});
      roundedGradientShader.setUniformf("color4", new float[]{(float)topRight.getRed() / 255.0F, (float)topRight.getGreen() / 255.0F, (float)topRight.getBlue() / 255.0F, (float)topRight.getAlpha() / 255.0F});
      ShaderUtil.drawQuads(x - 1.0F, y - 1.0F, width + 2.0F, height + 2.0F);
      roundedGradientShader.unload();
      GlStateManager.disableBlend();
   }

   public static void drawRound(float x, float y, float width, float height, float radius, boolean blur, Color color) {
      RenderUtil.resetColor();
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(770, 771);
      roundedShader.init();
      setupRoundedRectUniforms(x, y, width, height, radius, roundedShader);
      roundedShader.setUniformi("blur", new int[]{blur ? 1 : 0});
      roundedShader.setUniformf("color", new float[]{(float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F});
      ShaderUtil.drawQuads(x - 1.0F, y - 1.0F, width + 2.0F, height + 2.0F);
      roundedShader.unload();
      GlStateManager.disableBlend();
   }

   public static void drawRoundOutline(float x, float y, float width, float height, float radius, float outlineThickness, Color color, Color outlineColor) {
      RenderUtil.resetColor();
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(770, 771);
      roundedOutlineShader.init();
      ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
      setupRoundedRectUniforms(x, y, width, height, radius, roundedOutlineShader);
      roundedOutlineShader.setUniformf("outlineThickness", new float[]{outlineThickness * (float)ScaledResolution.getScaleFactor()});
      roundedOutlineShader.setUniformf("color", new float[]{(float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F});
      roundedOutlineShader.setUniformf("outlineColor", new float[]{(float)outlineColor.getRed() / 255.0F, (float)outlineColor.getGreen() / 255.0F, (float)outlineColor.getBlue() / 255.0F, (float)outlineColor.getAlpha() / 255.0F});
      ShaderUtil.drawQuads(x - (2.0F + outlineThickness), y - (2.0F + outlineThickness), width + 4.0F + outlineThickness * 2.0F, height + 4.0F + outlineThickness * 2.0F);
      roundedOutlineShader.unload();
      GlStateManager.disableBlend();
   }

   public static void drawRoundTextured(float x, float y, float width, float height, float radius, float alpha) {
      RenderUtil.resetColor();
      roundedTexturedShader.init();
      roundedTexturedShader.setUniformi("textureIn", new int[]{0});
      setupRoundedRectUniforms(x, y, width, height, radius, roundedTexturedShader);
      roundedTexturedShader.setUniformf("alpha", new float[]{alpha});
      ShaderUtil.drawQuads(x - 1.0F, y - 1.0F, width + 2.0F, height + 2.0F);
      roundedTexturedShader.unload();
      GlStateManager.disableBlend();
   }

   private static void setupRoundedRectUniforms(float x, float y, float width, float height, float radius, ShaderUtil roundedTexturedShader) {
      ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
      roundedTexturedShader.setUniformf("location", new float[]{x * (float)ScaledResolution.getScaleFactor(), (float)Minecraft.getMinecraft().displayHeight - height * (float)ScaledResolution.getScaleFactor() - y * (float)ScaledResolution.getScaleFactor()});
      roundedTexturedShader.setUniformf("rectSize", new float[]{width * (float)ScaledResolution.getScaleFactor(), height * (float)ScaledResolution.getScaleFactor()});
      roundedTexturedShader.setUniformf("radius", new float[]{radius * (float)ScaledResolution.getScaleFactor()});
   }
}
