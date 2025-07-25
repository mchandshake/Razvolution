/* Decompiler 5ms, total 321ms, lines 49 */
package wtf.evolution.helpers.math;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathHelper {
   public static String format(long millis) {
      long hours = millis / 3600000L;
      long minutes = millis % 3600000L / 60000L;
      long seconds = millis % 360000L % 60000L / 1000L;
      return String.format("%02d:%02d:%02d", hours, minutes, seconds);
   }

   public static float clamp(float value, float min, float max) {
      if (value < min) {
         return min;
      } else {
         return value > max ? max : value;
      }
   }

   public static double interpolate(double current, double old, double scale) {
      return old + (current - old) * scale;
   }

   public static float interpolate(float current, float old, double scale) {
      return (float)interpolate((double)current, (double)old, scale);
   }

   public static int interpolate(int current, int old, double scale) {
      return (int)interpolate((double)current, (double)old, scale);
   }

   public static double round(double num, double increment) {
      double v = (double)Math.round(num / increment) * increment;
      BigDecimal bd = new BigDecimal(v);
      bd = bd.setScale(2, RoundingMode.HALF_UP);
      return bd.doubleValue();
   }

   public static int getRandomNumberBetween(int min, int max) {
      return (int)(Math.random() * (double)(max - min + 1) + (double)min);
   }

   public static double getRandomNumberBetween(double min, double max) {
      return Math.random() * (max - min) + min;
   }
}
