/* Decompiler 5ms, total 266ms, lines 29 */
package wtf.evolution.module.impl.Movement;

import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventMotion;
import wtf.evolution.helpers.MovementUtil;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;

@ModuleInfo(
   name = "Strafe",
   type = Category.Movement
)
public class Strafe extends Module {
   public double boost;

   @EventTarget
   public void onUpdate(EventMotion e) {
      if (!this.mc.player.onGround && MovementUtil.getPlayerMotion() <= 0.2199999988079071D) {
         MovementUtil.setSpeed(this.mc.player.isSneaking() ? (float)MovementUtil.getPlayerMotion() : 0.22F);
      }

      if (!this.mc.player.onGround && this.mc.player.motionY == -0.4448259643949201D) {
         MovementUtil.setSpeed((float)MovementUtil.getPlayerMotion());
      }

   }
}
