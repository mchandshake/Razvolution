/* Decompiler 13ms, total 419ms, lines 104 */
package wtf.evolution.module.impl.Movement;

import java.util.Iterator;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventMotion;
import wtf.evolution.helpers.MovementUtil;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.ModeSetting;

@ModuleInfo(
   name = "Speed",
   type = Category.Movement
)
public class Speed extends Module {
   public ModeSetting mode = (new ModeSetting("Mode", "Matrix", new String[]{"Matrix", "Sunrise", "Matrix Boost"})).call(this);

   @EventTarget
   public void onUpdate(EventMotion e) {
      this.setSuffix(this.mode.get());
      if (this.mode.is("Matrix")) {
         if (MovementUtil.isMoving()) {
            if (this.mc.player.onGround) {
               this.mc.player.jump();
            }

            if (this.mc.player.motionY == -0.4448259643949201D && !this.mc.world.getCollisionBoxes(this.mc.player, this.mc.player.getEntityBoundingBox().offset(0.0D, -1.0D, 0.0D).expand(0.0D, 0.2D, 0.0D)).isEmpty()) {
               EntityPlayerSP var10000 = this.mc.player;
               var10000.motionX *= 2.0D;
               var10000 = this.mc.player;
               var10000.motionZ *= 2.0D;
            }
         }
      } else if (this.mode.is("Sunrise")) {
         if (this.mc.player.onGround) {
            MovementUtil.setSpeed((float)MovementUtil.getPlayerMotion() + 0.4F);
         }

         if (!this.mc.player.onGround && this.mc.player.isInWater()) {
            MovementUtil.setSpeed((float)MovementUtil.getPlayerMotion() + 0.4F);
         }

         MovementUtil.setSpeed((float)MovementUtil.getPlayerMotion());
      } else if (this.mode.is("Matrix Boost") && MovementUtil.isMoving()) {
         if (this.mc.player.onGround) {
            this.mc.player.jump();
         }

         if (this.mc.player.fallDistance > 0.0F) {
            MovementUtil.setSpeed(1.0F);
         }
      }

   }

   private void disabler() {
      int elytra = this.getSlotIDFromItem(Items.ELYTRA);
      if (this.mc.player.inventory.getItemStack().getItem() != Items.ELYTRA) {
         this.mc.playerController.windowClick(0, elytra < 9 ? elytra + 36 : elytra, 1, ClickType.PICKUP, this.mc.player);
      }

      this.mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, this.mc.player);
      this.mc.player.connection.sendPacket(new CPacketEntityAction(this.mc.player, Action.START_FALL_FLYING));
      this.mc.player.connection.sendPacket(new CPacketEntityAction(this.mc.player, Action.START_FALL_FLYING));
      this.mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, this.mc.player);
      this.mc.playerController.windowClick(0, elytra < 9 ? elytra + 36 : elytra, 1, ClickType.PICKUP, this.mc.player);
   }

   public int getSlotIDFromItem(Item item) {
      Iterator var2 = this.mc.player.getArmorInventoryList().iterator();

      while(var2.hasNext()) {
         ItemStack stack = (ItemStack)var2.next();
         if (stack.getItem() == item) {
            return -2;
         }
      }

      int slot = -1;

      for(int i = 0; i < 36; ++i) {
         ItemStack s = this.mc.player.inventory.getStackInSlot(i);
         if (s.getItem() == item) {
            slot = i;
            break;
         }
      }

      if (slot < 9 && slot != -1) {
         slot += 36;
      }

      return slot;
   }
}
