/* Decompiler 44ms, total 1263ms, lines 298 */
package wtf.evolution.module.impl.Combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventUpdate;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.Setting;
import wtf.evolution.settings.options.BooleanSetting;
import wtf.evolution.settings.options.SliderSetting;

@ModuleInfo(
   name = "AutoTotem",
   type = Category.Combat
)
public class AutoTotem extends Module {
   public static SliderSetting health = new SliderSetting("Health", 6.0F, 0.0F, 20.0F, 0.5F);
   public static BooleanSetting checkCrystal = new BooleanSetting("Check Crystal", true);
   public static SliderSetting CrystalDistance = (new SliderSetting("CrystalDistance", 8.0F, 0.0F, 15.0F, 0.5F)).setHidden(() -> {
      return !checkCrystal.get();
   });
   public static BooleanSetting fall = new BooleanSetting("Check Falling", true);
   public static SliderSetting fallDistance = (new SliderSetting("FallDistance", 8.0F, 1.0F, 30.0F, 0.5F)).setHidden(() -> {
      return !fall.get();
   });
   public static BooleanSetting obsidian = new BooleanSetting("Check Obsidian", false);
   public static SliderSetting obsidianDistance = (new SliderSetting("ObsidianDistance", 8.0F, 1.0F, 15.0F, 0.5F)).setHidden(() -> {
      return !obsidian.get();
   });
   public static BooleanSetting checkTnt = new BooleanSetting("Check TNT", false);
   public static SliderSetting TnTDistance = (new SliderSetting("TnTDistance", 8.0F, 0.0F, 15.0F, 0.5F)).setHidden(() -> {
      return !checkTnt.get();
   });
   public static BooleanSetting absorptionHP = new BooleanSetting("+ Absorption", true);
   public static int swapBack = -1;
   public static long delay;

   public AutoTotem() {
      this.addSettings(new Setting[]{health, checkCrystal, CrystalDistance, fall, fallDistance, obsidian, obsidianDistance, checkTnt, TnTDistance, absorptionHP});
   }

   @EventTarget
   public void onEvent(EventUpdate event) {
      float hp = this.mc.player.getHealth();
      if (absorptionHP.get()) {
         hp += this.mc.player.getAbsorptionAmount();
      }

      int totem = this.getSlotIDFromItem(Items.TOTEM_OF_UNDYING);
      int stackSizeHand = this.mc.player.getHeldItemOffhand().getCount();
      boolean handNotNull = !(this.mc.player.getHeldItemOffhand().getItem() instanceof ItemAir);
      boolean handTotem = this.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING;
      boolean totemCheck = hp <= health.get() || this.checkTNT() || this.checkCrystal() || this.checkFall() || this.checkObsidian();
      if (System.currentTimeMillis() >= delay) {
         if (totemCheck) {
            if (totem >= 0 && !handTotem) {
               this.mc.playerController.windowClick(0, totem, 1, ClickType.PICKUP, this.mc.player);
               this.mc.playerController.windowClick(0, 45, 1, ClickType.PICKUP, this.mc.player);
               if (handNotNull) {
                  this.mc.playerController.windowClick(0, totem, 0, ClickType.PICKUP, this.mc.player);
                  if (swapBack == -1) {
                     swapBack = totem;
                  }
               }

               delay = System.currentTimeMillis() + 300L;
            }

         } else if (swapBack >= 0) {
            this.mc.playerController.windowClick(0, swapBack, 0, ClickType.PICKUP, this.mc.player);
            this.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, this.mc.player);
            if (handNotNull) {
               this.mc.playerController.windowClick(0, swapBack, 0, ClickType.PICKUP, this.mc.player);
            }

            swapBack = -1;
            delay = System.currentTimeMillis() + 300L;
         } else {
            int totemSlot = this.getTotemSlot();
            if (totemSlot < 9 && totemSlot != -1) {
               totemSlot += 36;
            }

            if (absorptionHP.get()) {
               float var10000 = hp + this.mc.player.getAbsorptionAmount();
            }

            int prevCurrentItem = this.mc.player.inventory.currentItem;
            int currentItem = this.findNearestCurrentItem();
            ItemStack prevHeldItem = this.mc.player.getHeldItemOffhand();
            boolean totemInHand = this.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING;
            ItemStack itemstack;
            if (totemCheck) {
               if (totemSlot >= 0 && !totemInHand) {
                  this.mc.playerController.windowClick(0, totemSlot, currentItem, ClickType.SWAP, this.mc.player);
                  this.mc.getConnection().sendPacket(new CPacketHeldItemChange(currentItem));
                  this.mc.player.inventory.currentItem = currentItem;
                  itemstack = this.mc.player.getHeldItem(EnumHand.OFF_HAND);
                  this.mc.player.setHeldItem(EnumHand.OFF_HAND, this.mc.player.getHeldItem(EnumHand.MAIN_HAND));
                  this.mc.player.setHeldItem(EnumHand.MAIN_HAND, itemstack);
                  this.mc.getConnection().sendPacket(new CPacketPlayerDigging(Action.SWAP_HELD_ITEMS, BlockPos.ORIGIN, EnumFacing.DOWN));
                  this.mc.getConnection().sendPacket(new CPacketHeldItemChange(prevCurrentItem));
                  this.mc.player.inventory.currentItem = prevCurrentItem;
                  this.mc.playerController.windowClick(0, totemSlot, currentItem, ClickType.SWAP, this.mc.player);
                  if (swapBack == -1) {
                     swapBack = totemSlot;
                  }

                  return;
               }

               if (totemInHand) {
                  return;
               }
            }

            if (swapBack >= 0) {
               this.mc.getConnection().sendPacket(new CPacketHeldItemChange(currentItem));
               this.mc.player.inventory.currentItem = currentItem;
               itemstack = this.mc.player.getHeldItem(EnumHand.OFF_HAND);
               this.mc.player.setHeldItem(EnumHand.OFF_HAND, this.mc.player.getHeldItem(EnumHand.MAIN_HAND));
               this.mc.player.setHeldItem(EnumHand.MAIN_HAND, itemstack);
               this.mc.getConnection().sendPacket(new CPacketPlayerDigging(Action.SWAP_HELD_ITEMS, BlockPos.ORIGIN, EnumFacing.DOWN));
               this.mc.playerController.windowClick(0, swapBack, currentItem, ClickType.SWAP, this.mc.player);
               this.mc.getConnection().sendPacket(new CPacketPlayerDigging(Action.SWAP_HELD_ITEMS, BlockPos.ORIGIN, EnumFacing.DOWN));
               itemstack = this.mc.player.getHeldItem(EnumHand.OFF_HAND);
               this.mc.player.setHeldItem(EnumHand.OFF_HAND, this.mc.player.getHeldItem(EnumHand.MAIN_HAND));
               this.mc.player.setHeldItem(EnumHand.MAIN_HAND, itemstack);
               this.mc.getConnection().sendPacket(new CPacketHeldItemChange(prevCurrentItem));
               this.mc.player.inventory.currentItem = prevCurrentItem;
               swapBack = -1;
            }

         }
      }
   }

   public int getSlotIDFromItem(Item item) {
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

   public int getTotemSlot() {
      for(int i = 0; i < 36; ++i) {
         ItemStack stack = this.mc.player.inventory.getStackInSlot(i);
         if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
            return i;
         }
      }

      return -1;
   }

   public int findNearestCurrentItem() {
      int currentItem = this.mc.player.inventory.currentItem;
      if (currentItem == 8) {
         return 7;
      } else {
         return currentItem == 0 ? 1 : currentItem - 1;
      }
   }

   private boolean checkCrystal() {
      if (!checkCrystal.get()) {
         return false;
      } else {
         Iterator var1 = this.mc.world.loadedEntityList.iterator();

         Entity entity;
         do {
            if (!var1.hasNext()) {
               return false;
            }

            entity = (Entity)var1.next();
         } while(!(entity instanceof EntityEnderCrystal) || this.mc.player.getDistance(entity) > CrystalDistance.get());

         return true;
      }
   }

   private boolean checkTNT() {
      if (!checkTnt.get()) {
         return false;
      } else {
         Iterator var1 = this.mc.world.loadedEntityList.iterator();

         Entity entity;
         do {
            if (!var1.hasNext()) {
               return false;
            }

            entity = (Entity)var1.next();
            if (entity instanceof EntityTNTPrimed && this.mc.player.getDistance(entity) <= TnTDistance.get()) {
               return true;
            }
         } while(!(entity instanceof EntityMinecartTNT) || this.mc.player.getDistance(entity) > TnTDistance.get());

         return true;
      }
   }

   private boolean IsValidBlockPos(BlockPos pos) {
      IBlockState state = this.mc.world.getBlockState(pos);
      return state.getBlock() instanceof BlockObsidian;
   }

   private boolean checkObsidian() {
      if (!obsidian.get()) {
         return false;
      } else {
         BlockPos pos = (BlockPos)getSphere(this.getPlayerPosLocal(), obsidianDistance.get(), 6, false, true, 0).stream().filter(this::IsValidBlockPos).min(Comparator.comparing((blockPos) -> {
            return getDistanceOfEntityToBlock(this.mc.player, blockPos);
         })).orElse((Object)null);
         return pos != null;
      }
   }

   public static double getDistanceOfEntityToBlock(Entity entity, BlockPos blockPos) {
      return getDistance(entity.posX, entity.posY, entity.posZ, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ());
   }

   public static double getDistance(double n, double n2, double n3, double n4, double n5, double n6) {
      double n7 = n - n4;
      double n8 = n2 - n5;
      double n9 = n3 - n6;
      return (double)MathHelper.sqrt(n7 * n7 + n8 * n8 + n9 * n9);
   }

   public BlockPos getPlayerPosLocal() {
      return this.mc.player == null ? BlockPos.ORIGIN : new BlockPos(Math.floor(this.mc.player.posX), Math.floor(this.mc.player.posY), Math.floor(this.mc.player.posZ));
   }

   public static List<BlockPos> getSphere(BlockPos blockPos, float n, int n2, boolean b, boolean b2, int n3) {
      ArrayList<BlockPos> list = new ArrayList();
      int x = blockPos.getX();
      int y = blockPos.getY();
      int z = blockPos.getZ();

      for(int n4 = x - (int)n; (float)n4 <= (float)x + n; ++n4) {
         for(int n5 = z - (int)n; (float)n5 <= (float)z + n; ++n5) {
            for(int n6 = b2 ? y - (int)n : y; (float)n6 < (b2 ? (float)y + n : (float)(y + n2)); ++n6) {
               double n7 = (double)((x - n4) * (x - n4) + (z - n5) * (z - n5) + (b2 ? (y - n6) * (y - n6) : 0));
               if (n7 < (double)(n * n) && (!b || n7 >= (double)((n - 1.0F) * (n - 1.0F)))) {
                  list.add(new BlockPos(n4, n6 + n3, n5));
               }
            }
         }
      }

      return list;
   }

   private boolean checkFall() {
      if (!fall.get()) {
         return false;
      } else {
         return this.mc.player.fallDistance > fallDistance.get();
      }
   }
}
