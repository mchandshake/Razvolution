/* Decompiler 2ms, total 304ms, lines 42 */
package wtf.evolution.module.impl.Movement;

import net.minecraft.client.entity.EntityPlayerSP;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventNoSlow;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;

@ModuleInfo(
   name = "NoSlow",
   type = Category.Movement
)
public class NoSlow extends Module {
   @EventTarget
   public void onEating(EventNoSlow e) {
      EntityPlayerSP var10000;
      if (this.mc.player.onGround) {
         if (this.mc.player.ticksExisted % 2 == 0) {
            var10000 = this.mc.player;
            var10000.motionX *= 0.4D;
            var10000 = this.mc.player;
            var10000.motionZ *= 0.4D;
         }

         if (this.mc.player.ticksExisted % 4 == 0) {
            var10000 = this.mc.player;
            var10000.motionX *= 1.2D;
            var10000 = this.mc.player;
            var10000.motionZ *= 1.2D;
         }
      } else {
         var10000 = this.mc.player;
         var10000.motionX *= 0.97D;
         var10000 = this.mc.player;
         var10000.motionZ *= 0.97D;
      }

      e.cancel();
   }
}
