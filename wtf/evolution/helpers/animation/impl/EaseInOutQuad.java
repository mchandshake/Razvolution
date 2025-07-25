/* Decompiler 1ms, total 265ms, lines 20 */
package wtf.evolution.helpers.animation.impl;

import wtf.evolution.helpers.animation.Animation;
import wtf.evolution.helpers.animation.Direction;

public class EaseInOutQuad extends Animation {
   public EaseInOutQuad(int ms, double endPoint) {
      super(ms, endPoint);
   }

   public EaseInOutQuad(int ms, double endPoint, Direction direction) {
      super(ms, endPoint, direction);
   }

   protected double getEquation(double x1) {
      double x = x1 / (double)this.duration;
      return x < 0.5D ? 2.0D * Math.pow(x, 2.0D) : 1.0D - Math.pow(-2.0D * x + 2.0D, 2.0D) / 2.0D;
   }
}
