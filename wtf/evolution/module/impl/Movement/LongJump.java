/* Decompiler 3ms, total 358ms, lines 34 */
package wtf.evolution.module.impl.Movement;

import org.lwjgl.input.Keyboard;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventMotion;
import wtf.evolution.helpers.MovementUtil;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.ModeSetting;

@ModuleInfo(
   name = "LongJump",
   type = Category.Movement
)
public class LongJump extends Module {
   public ModeSetting mode = (new ModeSetting("Mode", "Vanilla", new String[]{"Vanilla", "Air Jump"})).call(this);

   @EventTarget
   public void onUpdate(EventMotion e) {
      if (this.mode.is("Air Jump")) {
         if (!this.mc.world.getCollisionBoxes(this.mc.player, this.mc.player.getEntityBoundingBox().offset(0.0D, -1.0D, 0.0D).expand(-1.0D, 0.0D, -1.0D).expand(1.0D, 0.0D, 1.0D)).isEmpty() && Keyboard.isKeyDown(57)) {
            this.mc.player.jumpTicks = 0;
            this.mc.player.fallDistance = 0.0F;
            e.setOnGround(true);
            this.mc.player.onGround = true;
         }
      } else if (this.mode.is("Vanilla") && !this.mc.player.onGround) {
         MovementUtil.setSpeed((float)(MovementUtil.getPlayerMotion() + 0.029999999329447746D));
      }

   }
}
