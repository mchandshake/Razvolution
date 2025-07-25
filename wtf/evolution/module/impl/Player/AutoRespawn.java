/* Decompiler 2ms, total 381ms, lines 22 */
package wtf.evolution.module.impl.Player;

import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventUpdate;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;

@ModuleInfo(
   name = "AutoRespawn",
   type = Category.Player
)
public class AutoRespawn extends Module {
   @EventTarget
   public void update(EventUpdate e) {
      if (this.mc.player != null && this.mc.world != null && this.mc.player.deathTime > 0) {
         this.mc.player.respawnPlayer();
      }

   }
}
