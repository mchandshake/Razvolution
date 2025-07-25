/* Decompiler 3ms, total 247ms, lines 44 */
package wtf.evolution.helpers;

public class BestColor {
   private final int red;
   private final int green;
   private final int blue;
   private final int alpha;

   public BestColor(int red, int green, int blue, int alpha) {
      this.red = red;
      this.green = green;
      this.blue = blue;
      this.alpha = alpha;
   }

   public BestColor(int red, int green, int blue) {
      this(red, green, blue, 255);
   }

   public BestColor(int color) {
      this(color >> 16 & 255, color >> 8 & 255, color & 255, color >> 24 & 255);
   }

   public int getRed() {
      return this.red;
   }

   public int getGreen() {
      return this.green;
   }

   public int getBlue() {
      return this.blue;
   }

   public int getAlpha() {
      return this.alpha;
   }

   public int getRGB() {
      return this.alpha << 24 | this.red << 16 | this.green << 8 | this.blue;
   }
}
