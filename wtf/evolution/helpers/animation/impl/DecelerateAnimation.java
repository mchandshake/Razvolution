/* Decompiler 2ms, total 248ms, lines 20 */
package wtf.evolution.helpers.animation.impl;

import wtf.evolution.helpers.animation.Animation;
import wtf.evolution.helpers.animation.Direction;

public class DecelerateAnimation extends Animation {
   public DecelerateAnimation(int ms, double endPoint) {
      super(ms, endPoint);
   }

   public DecelerateAnimation(int ms, double endPoint, Direction direction) {
      super(ms, endPoint, direction);
   }

   protected double getEquation(double x) {
      double x1 = x / (double)this.duration;
      return 1.0D - (x1 - 1.0D) * (x1 - 1.0D);
   }
}
