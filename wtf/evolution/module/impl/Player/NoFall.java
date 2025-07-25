/* Decompiler 2ms, total 321ms, lines 28 */
package wtf.evolution.module.impl.Player;

import net.minecraft.network.play.client.CPacketPlayer.Position;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventUpdate;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.ModeSetting;

@ModuleInfo(
   name = "NoFall",
   type = Category.Player
)
public class NoFall extends Module {
   public ModeSetting mode = (new ModeSetting("Mode", "Vanilla", new String[]{"Vanilla"})).call(this);

   @EventTarget
   public void onUpdate(EventUpdate e) {
      if (this.mode.get().equals("Vanilla") && this.mc.player.fallDistance >= 3.0F) {
         this.mc.player.connection.sendPacket(new Position(this.mc.player.posX, this.mc.player.posY, this.mc.player.posZ, true));
         this.mc.player.connection.sendPacket(new Position(this.mc.player.posX, this.mc.player.posY, this.mc.player.posZ, false));
         this.mc.player.fallDistance = 0.0F;
      }

   }
}
