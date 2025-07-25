/* Decompiler 6ms, total 291ms, lines 61 */
package wtf.evolution.helpers.render;

public final class Translate {
   private double x;
   private double y;

   public Translate(float x, float y) {
      this.x = (double)x;
      this.y = (double)y;
   }

   public final void interpolate(double targetX, double targetY, double smoothing) {
      this.x = this.animate(targetX, this.x, smoothing);
      this.y = this.animate(targetY, this.y, smoothing);
   }

   public void animate(double newX, double newY) {
      this.x = this.animate(this.x, newX, 1.0D);
      this.y = this.animate(this.y, newY, 1.0D);
   }

   public double animate(double target, double current, double speed) {
      boolean larger = target > current;
      if (speed < 0.0D) {
         speed = 0.0D;
      } else if (speed > 1.0D) {
         speed = 1.0D;
      }

      double dif = Math.max(target, current) - Math.min(target, current);
      double factor = dif * speed;
      if (factor < 0.1D) {
         factor = 0.1D;
      }

      if (larger) {
         current += factor;
      } else {
         current -= factor;
      }

      return current;
   }

   public double getX() {
      return (double)Math.round(this.x);
   }

   public void setX(double x) {
      this.x = x;
   }

   public double getY() {
      return (double)Math.round(this.y);
   }

   public void setY(double y) {
      this.y = y;
   }
}
