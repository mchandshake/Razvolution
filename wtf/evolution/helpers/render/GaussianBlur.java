/* Decompiler 5ms, total 276ms, lines 58 */
package wtf.evolution.helpers.render;

import java.nio.FloatBuffer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

public class GaussianBlur {
   public static ShaderUtil blurShader = new ShaderUtil("skidproject/shaders/gaussian.frag");
   public static Framebuffer framebuffer = new Framebuffer(1, 1, false);

   public static void setupUniforms(float dir1, float dir2, float radius) {
      blurShader.setUniformi("textureIn", new int[]{0});
      blurShader.setUniformf("texelSize", new float[]{1.0F / (float)RenderUtil.mc.displayWidth, 1.0F / (float)RenderUtil.mc.displayHeight});
      blurShader.setUniformf("direction", new float[]{dir1, dir2});
      blurShader.setUniformf("radius", new float[]{radius});
      FloatBuffer weightBuffer = BufferUtils.createFloatBuffer(256);

      for(int i = 0; (float)i <= radius; ++i) {
         weightBuffer.put(calculateGaussianValue((float)i, radius / 2.0F));
      }

      weightBuffer.rewind();
      GL20.glUniform1(blurShader.getUniform("weights"), weightBuffer);
   }

   public static void renderBlur(float radius) {
      GlStateManager.enableBlend();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      OpenGlHelper.glBlendFunc(770, 771, 1, 0);
      framebuffer = RenderUtil.createFrameBuffer(framebuffer);
      framebuffer.framebufferClear();
      framebuffer.bindFramebuffer(true);
      blurShader.init();
      setupUniforms(1.0F, 0.0F, radius);
      RenderUtil.bindTexture(RenderUtil.mc.getFramebuffer().framebufferTexture);
      ShaderUtil.drawQuads();
      framebuffer.unbindFramebuffer();
      blurShader.unload();
      RenderUtil.mc.getFramebuffer().bindFramebuffer(true);
      blurShader.init();
      setupUniforms(0.0F, 1.0F, radius);
      RenderUtil.bindTexture(framebuffer.framebufferTexture);
      ShaderUtil.drawQuads();
      blurShader.unload();
      RenderUtil.resetColor();
      GlStateManager.bindTexture(0);
   }

   public static float calculateGaussianValue(float x, float sigma) {
      double PI = 3.141592653D;
      double output = 1.0D / Math.sqrt(2.0D * PI * (double)(sigma * sigma));
      return (float)(output * Math.exp((double)(-(x * x)) / (2.0D * (double)(sigma * sigma))));
   }
}
