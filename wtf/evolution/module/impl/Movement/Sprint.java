/* Decompiler 3ms, total 268ms, lines 46 */
package wtf.evolution.module.impl.Movement;

import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventMotion;
import wtf.evolution.event.events.impl.EventPacket;
import wtf.evolution.helpers.MovementUtil;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.BooleanSetting;

@ModuleInfo(
   name = "Sprint",
   type = Category.Movement
)
public class Sprint extends Module {
   public BooleanSetting allmode = (new BooleanSetting("All - Direction", false)).call(this);

   @EventTarget
   public void onUpdate(EventMotion e) {
      if (this.allmode.get()) {
         this.mc.player.setSprinting(MovementUtil.isMoving());
      } else {
         this.mc.player.setSprinting(this.mc.player.moveForward > 0.0F);
      }

   }

   @EventTarget
   public void onUpdate(EventPacket e) {
      if (e.getPacket() instanceof CPacketEntityAction) {
         CPacketEntityAction packet = (CPacketEntityAction)e.getPacket();
         if (packet.getAction() == Action.START_SPRINTING) {
            e.cancel();
         }

         if (packet.getAction() == Action.STOP_SPRINTING) {
            e.cancel();
         }
      }

   }
}
