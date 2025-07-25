/* Decompiler 9ms, total 281ms, lines 74 */
package wtf.evolution.helpers.render;

import java.awt.Color;
import java.nio.FloatBuffer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

public class Bloom {
   public static ShaderUtil gaussianBloom = new ShaderUtil("skidproject/shaders/bloom.frag");
   public static Framebuffer framebuffer = new Framebuffer(1, 1, false);
   public static float[] weights = new float[41];

   public static void renderBlur(int sourceTexture, float radius, float offset, Color color) {
      framebuffer = RenderUtil.createFrameBuffer(framebuffer);
      GlStateManager.enableAlpha();
      GlStateManager.alphaFunc(516, 0.0F);
      GlStateManager.enableBlend();
      OpenGlHelper.glBlendFunc(770, 771, 1, 0);
      FloatBuffer weightBuffer = BufferUtils.createFloatBuffer(256);

      for(int i = 0; (float)i < radius; ++i) {
         weightBuffer.put(calculateGaussianValue((float)i, radius));
      }

      weightBuffer.rewind();
      setAlphaLimit(0.0F);
      framebuffer.framebufferClear();
      framebuffer.bindFramebuffer(true);
      gaussianBloom.init();
      setupUniforms(radius, offset, 0.0F, weightBuffer, color);
      RenderUtil.bindTexture(sourceTexture);
      ShaderUtil.drawQuads();
      gaussianBloom.unload();
      framebuffer.unbindFramebuffer();
      RenderUtil.mc.getFramebuffer().bindFramebuffer(true);
      gaussianBloom.init();
      setupUniforms(radius, 0.0F, offset, weightBuffer, color);
      GL13.glActiveTexture(34000);
      RenderUtil.bindTexture(sourceTexture);
      GL13.glActiveTexture(33984);
      RenderUtil.bindTexture(framebuffer.framebufferTexture);
      ShaderUtil.drawQuads();
      gaussianBloom.unload();
      GlStateManager.alphaFunc(516, 0.1F);
      GlStateManager.enableAlpha();
      GlStateManager.bindTexture(0);
   }

   public static void setAlphaLimit(float limit) {
      GlStateManager.enableAlpha();
      GlStateManager.alphaFunc(516, (float)((double)limit * 0.01D));
   }

   public static float calculateGaussianValue(float x, float sigma) {
      double PI = 3.141592653D;
      double output = 1.0D / Math.sqrt(2.0D * PI * (double)(sigma * sigma));
      return (float)(output * Math.exp((double)(-(x * x)) / (2.0D * (double)(sigma * sigma))));
   }

   public static void setupUniforms(float radius, float directionX, float directionY, FloatBuffer weights, Color color) {
      gaussianBloom.setUniformi("inTexture", new int[]{0});
      gaussianBloom.setUniformi("textureToCheck", new int[]{16});
      gaussianBloom.setUniformf("radius", new float[]{radius});
      gaussianBloom.setUniformf("color", new float[]{(float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F});
      gaussianBloom.setUniformf("texelSize", new float[]{1.0F / (float)RenderUtil.mc.displayWidth, 1.0F / (float)RenderUtil.mc.displayHeight});
      gaussianBloom.setUniformf("direction", new float[]{directionX, directionY});
      GL20.glUniform1(gaussianBloom.getUniform("weights"), weights);
   }
}
