/* Decompiler 5ms, total 310ms, lines 68 */
package wtf.evolution.helpers.render;

public class Simulation$Vector2 {
   public float x;
   public float y;

   public Simulation$Vector2(float x, float y) {
      this.x = x;
      this.y = y;
   }

   public Simulation$Vector2 clone() {
      return new Simulation$Vector2(this.x, this.y);
   }

   public void copy(Simulation$Vector2 vec) {
      this.x = vec.x;
      this.y = vec.y;
   }

   public Simulation$Vector2 add(Simulation$Vector2 vec) {
      this.x += vec.x;
      this.y += vec.y;
      return this;
   }

   public Simulation$Vector2 subtract(Simulation$Vector2 vec) {
      this.x -= vec.x;
      this.y -= vec.y;
      return this;
   }

   public Simulation$Vector2 div(float amount) {
      this.x /= amount;
      this.y /= amount;
      return this;
   }

   public Simulation$Vector2 mul(float amount) {
      this.x *= amount;
      this.y *= amount;
      return this;
   }

   public Simulation$Vector2 normalize() {
      float f = (float)Math.sqrt((double)(this.x * this.x + this.y * this.y));
      if (f < 1.0E-4F) {
         this.x = 0.0F;
         this.y = 0.0F;
      } else {
         this.x /= f;
         this.y /= f;
      }

      return this;
   }

   public String toString() {
      return "Vector2 [x=" + this.x + ", y=" + this.y + "]";
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Object clone() throws CloneNotSupportedException {
      return this.clone();
   }
}
