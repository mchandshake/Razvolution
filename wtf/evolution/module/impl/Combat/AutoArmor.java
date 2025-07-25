/* Decompiler 15ms, total 361ms, lines 118 */
package wtf.evolution.module.impl.Combat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventUpdate;
import wtf.evolution.helpers.animation.Counter;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.BooleanSetting;
import wtf.evolution.settings.options.SliderSetting;

@ModuleInfo(
   name = "AutoArmor",
   type = Category.Combat
)
public class AutoArmor extends Module {
   public BooleanSetting openInventory = (new BooleanSetting("openInventory", true)).call(this);
   public SliderSetting delay = (new SliderSetting("Delay", 1.0F, 0.1F, 10.0F, 0.1F)).call(this);
   private final Counter timerUtils = new Counter();

   @EventTarget
   public void onUpdate(EventUpdate e) {
      if (this.mc.currentScreen instanceof GuiInventory || !this.openInventory.get()) {
         if (!(this.mc.currentScreen instanceof GuiContainer) || this.mc.currentScreen instanceof InventoryEffectRenderer) {
            InventoryPlayer inventory = this.mc.player.inventory;
            int[] ArmorSlots = new int[4];
            int[] ArmorValues = new int[4];

            int slot;
            ItemStack stack;
            ItemArmor item;
            for(slot = 0; slot < 4; ++slot) {
               ArmorSlots[slot] = -1;
               stack = inventory.armorItemInSlot(slot);
               if (!isNullOrEmpty(stack) && stack.getItem() instanceof ItemArmor) {
                  item = (ItemArmor)stack.getItem();
                  ArmorValues[slot] = this.getArmorValue(item, stack);
               }
            }

            int j;
            for(slot = 0; slot < 36; ++slot) {
               stack = inventory.getStackInSlot(slot);
               if (!isNullOrEmpty(stack) && stack.getItem() instanceof ItemArmor) {
                  item = (ItemArmor)stack.getItem();
                  j = item.armorType.getIndex();
                  int armorValue = this.getArmorValue(item, stack);
                  if (armorValue > ArmorValues[j]) {
                     ArmorSlots[j] = slot;
                     ArmorValues[j] = armorValue;
                  }
               }
            }

            ArrayList<Integer> types = new ArrayList(Arrays.asList(0, 1, 2, 3));
            Collections.shuffle(types);
            Iterator var11 = types.iterator();

            while(var11.hasNext()) {
               int i = (Integer)var11.next();
               j = ArmorSlots[i];
               if (j != -1) {
                  ItemStack oldArmor = inventory.armorItemInSlot(i);
                  if (isNullOrEmpty(oldArmor) || inventory.getFirstEmptyStack() != -1) {
                     if (j < 9) {
                        j += 36;
                     }

                     if (this.timerUtils.hasReached((double)(this.delay.get() * 100.0F))) {
                        if (!isNullOrEmpty(oldArmor)) {
                           this.mc.playerController.windowClick(0, 8 - i, 0, ClickType.QUICK_MOVE, this.mc.player);
                        }

                        this.mc.playerController.windowClick(0, j, 0, ClickType.QUICK_MOVE, this.mc.player);
                        this.timerUtils.reset();
                     }
                     break;
                  }
               }
            }

         }
      }
   }

   private int getArmorValue(ItemArmor item, ItemStack stack) {
      int armorPoints = item.damageReduceAmount;
      int prtPoints = false;
      int armorToughness = (int)item.toughness;
      int armorType = item.getArmorMaterial().getDamageReductionAmount(EntityEquipmentSlot.LEGS);
      Enchantment protection = Enchantments.PROTECTION;
      int prtLvl = EnchantmentHelper.getEnchantmentLevel(protection, stack);
      DamageSource dmgSource = DamageSource.causePlayerDamage(this.mc.player);
      int prtPoints = protection.calcModifierDamage(prtLvl, dmgSource);
      return armorPoints * 5 + prtPoints * 3 + armorToughness + armorType;
   }

   public static boolean isNullOrEmpty(ItemStack stack) {
      return stack == null || stack.isEmpty();
   }
}
