/* Decompiler 4ms, total 265ms, lines 39 */
package wtf.evolution.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

public class ScaleUtil {
   public static float size = 2.0F;

   public static void scale_pre() {
      ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft());
      double scale = (double)ScaledResolution.getScaleFactor() / Math.pow((double)ScaledResolution.getScaleFactor(), 2.0D);
      GL11.glPushMatrix();
      GL11.glScaled(scale * (double)size, scale * (double)size, scale * (double)size);
   }

   public static void scale_post() {
      GL11.glScaled((double)size, (double)size, (double)size);
      GL11.glPopMatrix();
   }

   public static int calc(int value) {
      ScaledResolution rs = new ScaledResolution(Minecraft.getMinecraft());
      return (int)((float)(value * ScaledResolution.getScaleFactor()) / size);
   }

   public static int calc(float value) {
      ScaledResolution rs = new ScaledResolution(Minecraft.getMinecraft());
      return (int)(value * (float)ScaledResolution.getScaleFactor() / size);
   }

   public static float[] calc(float mouseX, float mouseY) {
      ScaledResolution rs = new ScaledResolution(Minecraft.getMinecraft());
      mouseX = mouseX * (float)ScaledResolution.getScaleFactor() / size;
      mouseY = mouseY * (float)ScaledResolution.getScaleFactor() / size;
      return new float[]{mouseX, mouseY};
   }
}
