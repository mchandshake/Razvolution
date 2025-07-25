/* Decompiler 2ms, total 254ms, lines 19 */
package wtf.evolution.helpers.render;

import wtf.evolution.helpers.render.Simulation.Vector2;
import wtf.evolution.helpers.render.cape.Mth;

public class Simulation$Point {
   public Vector2 position = new Vector2(0.0F, 0.0F);
   public Vector2 prevPosition = new Vector2(0.0F, 0.0F);
   public boolean locked;

   public float getLerpX(float delta) {
      return Mth.lerp(delta, this.prevPosition.x, this.position.x);
   }

   public float getLerpY(float delta) {
      return Mth.lerp(delta, this.prevPosition.y, this.position.y);
   }
}
