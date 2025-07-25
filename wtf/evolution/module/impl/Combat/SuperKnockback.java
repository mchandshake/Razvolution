/* Decompiler 2ms, total 300ms, lines 31 */
package wtf.evolution.module.impl.Combat;

import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketUseEntity.Action;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventPacket;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;

@ModuleInfo(
   name = "SuperKnockBack",
   type = Category.Combat
)
public class SuperKnockback extends Module {
   @EventTarget
   public void onSendPacket(EventPacket event) {
      if (event.getPacket() instanceof CPacketUseEntity) {
         CPacketUseEntity packet = (CPacketUseEntity)event.getPacket();
         if (packet.getAction() == Action.ATTACK) {
            this.mc.player.setSprinting(false);
            this.mc.player.connection.sendPacket(new CPacketEntityAction(this.mc.player, net.minecraft.network.play.client.CPacketEntityAction.Action.STOP_SPRINTING));
            this.mc.player.setSprinting(true);
            this.mc.player.connection.sendPacket(new CPacketEntityAction(this.mc.player, net.minecraft.network.play.client.CPacketEntityAction.Action.START_SPRINTING));
         }
      }

   }
}
