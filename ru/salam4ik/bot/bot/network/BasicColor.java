/* Decompiler 12ms, total 333ms, lines 109 */
package ru.salam4ik.bot.bot.network;

import java.util.HashMap;
import java.util.Map;
import ru.salam4ik.bot.bot.network.BasicColor.1;

public class BasicColor {
   final int g;
   final int b;
   public static final BasicColor TRANSPARENT = new 1(0, 0, 0);
   final int r;
   public static Map<Integer, BasicColor> colors = new HashMap();

   private static BasicColor toCol(int n, int n2, int n3) {
      return new BasicColor(n, n2, n3);
   }

   public BasicColor(int n, int n2, int n3) {
      this.r = n;
      this.g = n2;
      this.b = n3;
   }

   public int shaded(byte by) {
      double d = 0.0D;
      switch(by) {
      case 0:
         d = 0.71D;
         break;
      case 1:
         d = 0.85D;
         break;
      case 2:
         d = 1.0D;
         break;
      case 3:
         d = 0.53D;
      }

      return -16777216 | this.toInt(this.r, d) << 16 | this.toInt(this.g, d) << 8 | this.toInt(this.b, d);
   }

   private int toInt(int n, double d) {
      return (int)Math.round((double)n * d);
   }

   static {
      colors.put(0, TRANSPARENT);
      colors.put(1, toCol(127, 178, 56));
      colors.put(2, toCol(247, 233, 163));
      colors.put(3, toCol(199, 199, 199));
      colors.put(4, toCol(255, 0, 0));
      colors.put(5, toCol(160, 160, 255));
      colors.put(6, toCol(167, 167, 167));
      colors.put(7, toCol(0, 124, 0));
      colors.put(8, toCol(255, 255, 255));
      colors.put(9, toCol(164, 168, 184));
      colors.put(10, toCol(151, 109, 77));
      colors.put(11, toCol(112, 112, 112));
      colors.put(12, toCol(64, 64, 255));
      colors.put(13, toCol(143, 119, 72));
      colors.put(14, toCol(255, 252, 245));
      colors.put(15, toCol(216, 127, 51));
      colors.put(16, toCol(178, 76, 216));
      colors.put(17, toCol(102, 153, 216));
      colors.put(18, toCol(229, 229, 51));
      colors.put(19, toCol(127, 204, 25));
      colors.put(20, toCol(242, 127, 165));
      colors.put(21, toCol(76, 76, 76));
      colors.put(22, toCol(153, 153, 153));
      colors.put(23, toCol(76, 127, 153));
      colors.put(24, toCol(127, 63, 178));
      colors.put(25, toCol(51, 76, 178));
      colors.put(26, toCol(102, 76, 51));
      colors.put(27, toCol(102, 127, 51));
      colors.put(28, toCol(153, 51, 51));
      colors.put(29, toCol(25, 25, 25));
      colors.put(30, toCol(250, 238, 77));
      colors.put(31, toCol(92, 219, 213));
      colors.put(32, toCol(74, 128, 255));
      colors.put(33, toCol(0, 217, 58));
      colors.put(34, toCol(129, 86, 49));
      colors.put(35, toCol(112, 2, 0));
      colors.put(36, toCol(209, 177, 161));
      colors.put(37, toCol(159, 82, 36));
      colors.put(38, toCol(149, 87, 108));
      colors.put(39, toCol(112, 108, 138));
      colors.put(40, toCol(186, 133, 36));
      colors.put(41, toCol(103, 117, 53));
      colors.put(42, toCol(160, 77, 78));
      colors.put(43, toCol(57, 41, 35));
      colors.put(44, toCol(135, 107, 98));
      colors.put(45, toCol(87, 92, 92));
      colors.put(46, toCol(122, 73, 88));
      colors.put(47, toCol(76, 62, 92));
      colors.put(48, toCol(76, 50, 35));
      colors.put(49, toCol(76, 82, 42));
      colors.put(50, toCol(142, 60, 46));
      colors.put(51, toCol(37, 22, 16));
      colors.put(52, toCol(189, 48, 49));
      colors.put(53, toCol(148, 63, 97));
      colors.put(54, toCol(92, 25, 29));
      colors.put(55, toCol(22, 126, 134));
      colors.put(56, toCol(58, 142, 140));
      colors.put(57, toCol(86, 44, 62));
      colors.put(58, toCol(20, 180, 133));
   }
}
