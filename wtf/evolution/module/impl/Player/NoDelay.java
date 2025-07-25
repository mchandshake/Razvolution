/* Decompiler 2ms, total 336ms, lines 37 */
package wtf.evolution.module.impl.Player;

import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventUpdate;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.ListSetting;

@ModuleInfo(
   name = "NoDelay",
   type = Category.Player
)
public class NoDelay extends Module {
   public ListSetting delay = (new ListSetting("NoDelay", new String[]{"Jump", "LeftClick", "Block", "RightClick"})).call(this);

   @EventTarget
   public void onMotion(EventUpdate e) {
      if (this.delay.selected.contains("Jump")) {
         this.mc.player.jumpTicks = 0;
      }

      if (this.delay.selected.contains("LeftClick")) {
         this.mc.leftClickCounter = 0;
      }

      if (this.delay.selected.contains("Block")) {
         this.mc.playerController.blockHitDelay = 0;
      }

      if (this.delay.selected.contains("RightClick")) {
         this.mc.rightClickDelayTimer = 0;
      }

   }
}
