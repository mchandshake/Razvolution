/* Decompiler 2ms, total 684ms, lines 42 */
package wtf.evolution.helpers.render;

import org.lwjgl.opengl.GL11;

public class AntiAliasing {
   public static void hook(boolean line, boolean polygon, boolean point) {
      if (line) {
         GL11.glEnable(2848);
         GL11.glHint(3154, 4354);
      }

      if (polygon) {
         GL11.glEnable(2881);
         GL11.glHint(3155, 4354);
      }

      if (point) {
         GL11.glEnable(2832);
         GL11.glHint(3153, 4354);
      }

   }

   public static void unhook(boolean line, boolean polygon, boolean point) {
      if (line) {
         GL11.glHint(3154, 4352);
         GL11.glDisable(2848);
      }

      if (polygon) {
         GL11.glHint(3155, 4352);
         GL11.glDisable(2881);
      }

      if (point) {
         GL11.glHint(3153, 4352);
         GL11.glDisable(2832);
      }

   }
}
