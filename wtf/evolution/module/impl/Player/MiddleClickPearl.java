/* Decompiler 4ms, total 282ms, lines 39 */
package wtf.evolution.module.impl.Player;

import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.MouseEvent;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;

@ModuleInfo(
   name = "MiddleClickPearl",
   type = Category.Player
)
public class MiddleClickPearl extends Module {
   @EventTarget
   public void onMouse(MouseEvent e) {
      if (e.button == 2 && this.getPearl() != -1) {
         this.mc.player.connection.sendPacket(new CPacketHeldItemChange(this.getPearl()));
         this.mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
         this.mc.player.connection.sendPacket(new CPacketHeldItemChange(this.mc.player.inventory.currentItem));
      }

   }

   public int getPearl() {
      for(int i = 0; i < 9; ++i) {
         this.mc.player.inventory.getStackInSlot(i);
         if (this.mc.player.inventory.getStackInSlot(i).getItem() == Items.ENDER_PEARL) {
            return i;
         }
      }

      return -1;
   }
}
