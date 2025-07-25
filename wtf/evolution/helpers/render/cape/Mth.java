/* Decompiler 3ms, total 339ms, lines 30 */
package wtf.evolution.helpers.render.cape;

public class Mth {
   public static float lerp(float f, float g, float h) {
      return g + f * (h - g);
   }

   public static double lerp(double d, double e, double f) {
      return e + d * (f - e);
   }

   public static float fastInvSqrt(float f) {
      float g = 0.5F * f;
      int i = Float.floatToIntBits(f);
      i = 1597463007 - (i >> 1);
      f = Float.intBitsToFloat(i);
      f *= 1.5F - g * f * f;
      return f;
   }

   public static float fastInvCubeRoot(float f) {
      int i = Float.floatToIntBits(f);
      i = 1419967116 - i / 3;
      float g = Float.intBitsToFloat(i);
      g = 0.6666667F * g + 0.33333334F * g * g * f;
      g = 0.6666667F * g + 0.33333334F * g * g * f;
      return g;
   }
}
