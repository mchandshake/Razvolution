/* Decompiler 2ms, total 258ms, lines 28 */
package wtf.evolution.module.impl.Movement;

import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventMotion;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.ModeSetting;

@ModuleInfo(
   name = "NoClip",
   type = Category.Movement
)
public class NoClip extends Module {
   public ModeSetting mode = new ModeSetting("Mode", "SunRise", new String[]{"SunRise"});

   @EventTarget
   public void onMotion(EventMotion e) {
      if (this.mc.player.collidedHorizontally) {
         this.mc.player.onGround = true;
         if (!this.mc.gameSettings.keyBindSneak.isKeyDown()) {
            this.mc.player.motionY = 0.0D;
         }
      }

   }
}
