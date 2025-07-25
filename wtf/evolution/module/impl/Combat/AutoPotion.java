/* Decompiler 16ms, total 355ms, lines 126 */
package wtf.evolution.module.impl.Combat;

import java.util.Iterator;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumHand;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventMotion;
import wtf.evolution.helpers.animation.Counter;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.Setting;
import wtf.evolution.settings.options.BooleanSetting;
import wtf.evolution.settings.options.SliderSetting;

@ModuleInfo(
   name = "AutoPotion",
   type = Category.Combat
)
public class AutoPotion extends Module {
   public static BooleanSetting onlyGround = new BooleanSetting("onlyGround", true);
   public static BooleanSetting strenght = new BooleanSetting("Strenght", true);
   public static BooleanSetting speed = new BooleanSetting("Speed", true);
   public static BooleanSetting fire_resistance = new BooleanSetting("Fire Resistance", true);
   public static BooleanSetting hotberOnly = new BooleanSetting("Hotbar Only", true);
   public SliderSetting delay = new SliderSetting("Delay", 100.0F, 10.0F, 1000.0F, 10.0F);
   private final Counter timerUtils = new Counter();
   ItemStack held;

   public AutoPotion() {
      this.addSettings(new Setting[]{onlyGround, strenght, speed, fire_resistance, hotberOnly, this.delay});
   }

   @EventTarget
   public void onPre(EventMotion event) {
      if (this.timerUtils.hasReached((double)this.delay.get())) {
         if (strenght.get() && !this.mc.player.isPotionActive(MobEffects.STRENGTH)) {
            this.throwPotion(5);
         }

         if (speed.get() && !this.mc.player.isPotionActive(MobEffects.SPEED)) {
            this.throwPotion(1);
         }

         if (fire_resistance.get() && !this.mc.player.isPotionActive(MobEffects.FIRE_RESISTANCE)) {
            this.throwPotion(12);
         }

         this.timerUtils.reset();
      }

   }

   private int getPotionIndexInv(int id) {
      for(int i = 0; i < 45; ++i) {
         int index = i < 9 ? i + 36 : i;
         Iterator var4 = PotionUtils.getEffectsFromStack(this.mc.player.inventory.getStackInSlot(index)).iterator();

         while(var4.hasNext()) {
            PotionEffect potion = (PotionEffect)var4.next();
            if (potion.getPotion() == Potion.getPotionById(id) && this.mc.player.inventory.getStackInSlot(i).getItem() == Items.SPLASH_POTION) {
               return index;
            }
         }
      }

      return -1;
   }

   private int getPotionIndexHb(int id) {
      for(int i = 0; i < 9; ++i) {
         Iterator var3 = PotionUtils.getEffectsFromStack(this.mc.player.inventory.getStackInSlot(i)).iterator();

         while(var3.hasNext()) {
            PotionEffect potion = (PotionEffect)var3.next();
            if (potion.getPotion() == Potion.getPotionById(id) && this.mc.player.inventory.getStackInSlot(i).getItem() == Items.SPLASH_POTION) {
               return i;
            }
         }
      }

      return -1;
   }

   private void throwPotion(int potionId) {
      if (!onlyGround.get() || this.mc.player.onGround) {
         int index = true;
         int index;
         if (this.getPotionIndexHb(potionId) == -1) {
            index = this.getPotionIndexInv(potionId);
         } else {
            index = this.getPotionIndexHb(potionId);
         }

         if (index != -1) {
            if (!hotberOnly.get() && index > 9) {
               this.mc.playerController.windowClick(0, index, 0, ClickType.PICKUP, this.mc.player);
               this.mc.playerController.windowClick(0, 44, 0, ClickType.PICKUP, this.mc.player);
               this.throwPot(index);
               this.mc.playerController.windowClick(0, 44, 0, ClickType.PICKUP, this.mc.player);
            } else {
               this.throwPot(index);
            }

         }
      }
   }

   void throwPot(int index) {
      this.mc.player.connection.sendPacket(new CPacketHeldItemChange(index < 9 ? index : 8));
      this.mc.player.connection.sendPacket(new Rotation(this.mc.player.rotationYaw, 90.0F, this.mc.player.onGround));
      this.mc.player.rotationPitchHead = 90.0F;
      this.mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
      this.mc.player.connection.sendPacket(new CPacketHeldItemChange(this.mc.player.inventory.currentItem));
   }
}
