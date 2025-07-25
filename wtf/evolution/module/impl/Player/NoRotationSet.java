/* Decompiler 1ms, total 282ms, lines 25 */
package wtf.evolution.module.impl.Player;

import net.minecraft.network.play.server.SPacketPlayerPosLook;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventPacket;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;

@ModuleInfo(
   name = "NoRotationSet",
   type = Category.Player
)
public class NoRotationSet extends Module {
   @EventTarget
   public void onRotate(EventPacket e) {
      if (e.getPacket() instanceof SPacketPlayerPosLook) {
         SPacketPlayerPosLook packet = (SPacketPlayerPosLook)e.getPacket();
         packet.yaw = this.mc.player.rotationYaw;
         packet.pitch = this.mc.player.rotationPitch;
      }

   }
}
