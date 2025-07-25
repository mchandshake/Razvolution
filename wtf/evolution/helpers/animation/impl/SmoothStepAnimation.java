/* Decompiler 1ms, total 254ms, lines 20 */
package wtf.evolution.helpers.animation.impl;

import wtf.evolution.helpers.animation.Animation;
import wtf.evolution.helpers.animation.Direction;

public class SmoothStepAnimation extends Animation {
   public SmoothStepAnimation(int ms, double endPoint) {
      super(ms, endPoint);
   }

   public SmoothStepAnimation(int ms, double endPoint, Direction direction) {
      super(ms, endPoint, direction);
   }

   protected double getEquation(double x) {
      double x1 = x / (double)this.duration;
      return -2.0D * Math.pow(x1, 3.0D) + 3.0D * Math.pow(x1, 2.0D);
   }
}
