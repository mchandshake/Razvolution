/* Decompiler 93ms, total 977ms, lines 364 */
package ru.salam4ik.bot.bot;

import com.mojang.authlib.GameProfile;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.vecmath.Vector2f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.CPacketLoginStart;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import ru.salam4ik.bot.bot.entity.BotPlayer;
import ru.salam4ik.bot.bot.network.BotLoginClient;
import ru.salam4ik.bot.bot.network.BotNetwork;
import wtf.evolution.Main;
import wtf.evolution.bot.ProxyS.Proxy;
import wtf.evolution.command.impl.BotCommand;
import wtf.evolution.helpers.math.MathHelper;
import wtf.evolution.notifications.NotificationType;

public class BotStarter {
   public static final Random random = new Random((long)((double)System.currentTimeMillis() * Math.random() + (double)System.nanoTime()));
   public static int integer;
   public static ExecutorService exec = Executors.newCachedThreadPool();
   public static Minecraft mc = Minecraft.getMinecraft();

   public static void init() {
      exec.submit(() -> {
         while(true) {
            Iterator var0 = Bot.bots.iterator();

            label168:
            while(var0.hasNext()) {
               Bot bot = (Bot)var0.next();
               if (bot.getBot().getHealth() <= 0.0F) {
                  bot.getBot().respawnPlayer();
               }

               bot.getController().updateController();

               try {
                  bot.getWorld().tick();
                  bot.getWorld().updateEntities();
                  Vector2f vector2f;
                  if (BotCommand.follow) {
                     vector2f = getBlockAngles(mc.player.posX, mc.player.posY + 1.0D, mc.player.posZ, bot.getBot().posX, bot.getBot().posY, bot.getBot().posZ);
                     bot.getBot().rotationYaw = normalizeYaw(vector2f.y);
                     bot.getBot().rotationPitch = normalizePitch(vector2f.x);
                     bot.getConnection().forward = true;
                  }

                  if (BotCommand.look) {
                     vector2f = getBlockAngles(mc.player.posX, mc.player.posY + 1.0D, mc.player.posZ, bot.getBot().posX, bot.getBot().posY, bot.getBot().posZ);
                     bot.getBot().rotationYaw = normalizeYaw(vector2f.y);
                     bot.getBot().rotationPitch = normalizePitch(vector2f.x);
                  }

                  if (BotCommand.mimic) {
                     bot.getConnection().forward = mc.gameSettings.keyBindForward.pressed;
                     bot.getConnection().backward = mc.gameSettings.keyBindBack.pressed;
                     bot.getConnection().left = mc.gameSettings.keyBindLeft.pressed;
                     bot.getConnection().right = mc.gameSettings.keyBindRight.pressed;
                     bot.getConnection().jump = mc.gameSettings.keyBindJump.pressed;
                     bot.getConnection().sneak = mc.gameSettings.keyBindSneak.pressed;
                     bot.getBot().rotationYaw = mc.player.rotationYaw;
                     bot.getBot().rotationPitch = mc.player.rotationPitch;
                  }

                  if (BotCommand.aura) {
                     Entity e = getFromName(BotCommand.target);
                     if (e != null && e != bot.getBot()) {
                        Vector2f vector2fx = getBlockAngles(e.posX, e.posY + 0.5D, e.posZ, bot.getBot().posX, bot.getBot().posY, bot.getBot().posZ);
                        bot.getBot().rotationYaw = normalizeYaw(vector2fx.y) + (float)MathHelper.getRandomNumberBetween(-3, 3);
                        bot.getBot().rotationPitch = normalizePitch(vector2fx.x) + (float)MathHelper.getRandomNumberBetween(-1, 1);
                        bot.getConnection().forward = bot.getBot().getDistance(e) > 2.0F;
                        if ((double)bot.getBot().getDistance(e) <= 3.2D && (double)bot.getBot().getCooledAttackStrength(1.0F) >= 0.93D) {
                           bot.getController().attackEntity(bot.getBot(), e);
                           bot.getBot().swingArm(EnumHand.MAIN_HAND);
                           bot.getBot().resetCooldown();
                        }
                     }
                  }

                  if (BotCommand.movingStrafe) {
                     float dst = mc.player.getDistance(bot.getBot());
                     double maxDst = 3.0D;
                     float forward = 0.0F;
                     if ((double)dst <= maxDst - 0.3D) {
                        forward = -1.0F;
                     } else if ((double)dst > maxDst) {
                        forward = 1.0F;
                     }

                     strafe(bot.getBot(), getBlockAngles(mc.player.posX, mc.player.posY + 1.0D, mc.player.posZ, bot.getBot().posX, bot.getBot().posY, bot.getBot().posZ).y, 0.24D, (double)forward, 1.0D);
                  }

                  if (BotCommand.movingRandom) {
                     BotPlayer var10000 = bot.getBot();
                     var10000.rotationYaw += (float)MathHelper.getRandomNumberBetween(-20, 20);
                     bot.getConnection().forward = true;
                  }

                  int i = 0;
                  Iterator var16 = bot.getBot().openContainer.getInventory().iterator();

                  while(var16.hasNext()) {
                     ItemStack stack = (ItemStack)var16.next();
                     ++i;
                     if ((stack.getDisplayName().toLowerCase().contains("нажми") || stack.getDisplayName().toLowerCase().contains("click") || stack.getDisplayName().toLowerCase().contains("сюда") || stack.getDisplayName().toLowerCase().contains("here")) && bot.c.hasReached(400.0D)) {
                        System.out.println(i);
                        bot.getConnection().getController().windowClick(bot.getBot().openContainer.windowId, i - 1, 0, ClickType.PICKUP, bot.getBot());
                        Main.notify.call(bot.getBot().getName(), "Clicked " + (i - 1), NotificationType.INFO);
                        bot.c.reset();
                     }

                     if (bot.getBot().currentContainerName.toLowerCase().contains("зелье") && PotionUtils.getColor(stack) == 255 && bot.c.hasReached(400.0D)) {
                        System.out.println(i);
                        bot.getConnection().getController().windowClick(bot.getBot().openContainer.windowId, i - 1, 0, ClickType.PICKUP, bot.getBot());
                        Main.notify.call(bot.getBot().getName(), "Clicked " + (i - 1), NotificationType.INFO);
                        bot.c.reset();
                     }
                  }

                  if (BotCommand.sword && hasSword(bot)) {
                     bot.getBot().inventory.currentItem = getSword(bot);
                  }

                  if (BotCommand.armor) {
                     InventoryPlayer inventory = bot.getBot().inventory;
                     int[] ArmorSlots = new int[4];
                     int[] ArmorValues = new int[4];

                     int slot;
                     ItemStack stackx;
                     ItemArmor item;
                     for(slot = 0; slot < 4; ++slot) {
                        ArmorSlots[slot] = -1;
                        stackx = inventory.armorItemInSlot(slot);
                        if (!isNullOrEmpty(stackx) && stackx.getItem() instanceof ItemArmor) {
                           item = (ItemArmor)stackx.getItem();
                           ArmorValues[slot] = getArmorValue(item, stackx, bot);
                        }
                     }

                     int j;
                     for(slot = 0; slot < 36; ++slot) {
                        stackx = inventory.getStackInSlot(slot);
                        if (!isNullOrEmpty(stackx) && stackx.getItem() instanceof ItemArmor) {
                           item = (ItemArmor)stackx.getItem();
                           j = item.armorType.getIndex();
                           int armorValue = getArmorValue(item, stackx, bot);
                           if (armorValue > ArmorValues[j]) {
                              ArmorSlots[j] = slot;
                              ArmorValues[j] = armorValue;
                           }
                        }
                     }

                     ArrayList<Integer> types = new ArrayList(Arrays.asList(0, 1, 2, 3));
                     Collections.shuffle(types);
                     Iterator var21 = types.iterator();

                     int i1;
                     ItemStack oldArmor;
                     do {
                        do {
                           if (!var21.hasNext()) {
                              continue label168;
                           }

                           i1 = (Integer)var21.next();
                           j = ArmorSlots[i1];
                        } while(j == -1);

                        oldArmor = inventory.armorItemInSlot(i1);
                     } while(!isNullOrEmpty(oldArmor) && inventory.getFirstEmptyStack() == -1);

                     if (j < 9) {
                        j += 36;
                     }

                     if (bot.c.hasReached(200.0D)) {
                        if (!isNullOrEmpty(oldArmor)) {
                           bot.getController().windowClick(0, 8 - i1, 0, ClickType.QUICK_MOVE, bot.getBot());
                        }

                        bot.getController().windowClick(0, j, 0, ClickType.QUICK_MOVE, bot.getBot());
                        bot.c.reset();
                     }
                  }
               } catch (Exception var11) {
                  bot.getNetManager().closeChannel();
                  Bot.bots.remove(bot);
               }
            }

            Thread.sleep(50L);
         }
      });
   }

   public static boolean hasSword(Bot bot) {
      Iterator var1 = bot.getBot().inventory.mainInventory.iterator();

      ItemStack stack;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         stack = (ItemStack)var1.next();
      } while(!(stack.getItem() instanceof ItemSword));

      return true;
   }

   public static int getSword(Bot bot) {
      int slot = -1;

      for(int i = 0; i < 9; ++i) {
         ItemStack stack = bot.getBot().inventory.getStackInSlot(i);
         if (stack.getItem() instanceof ItemSword) {
            slot = i;
         }
      }

      return slot;
   }

   public static int getArmorValue(ItemArmor item, ItemStack stack, Bot bot) {
      int armorPoints = item.damageReduceAmount;
      int prtPoints = false;
      int armorToughness = (int)item.toughness;
      int armorType = item.getArmorMaterial().getDamageReductionAmount(EntityEquipmentSlot.LEGS);
      Enchantment protection = Enchantments.PROTECTION;
      int prtLvl = EnchantmentHelper.getEnchantmentLevel(protection, stack);
      DamageSource dmgSource = DamageSource.causePlayerDamage(bot.getBot());
      int prtPoints = protection.calcModifierDamage(prtLvl, dmgSource);
      return armorPoints * 5 + prtPoints * 3 + armorToughness + armorType;
   }

   public static boolean isNullOrEmpty(ItemStack stack) {
      return stack == null || stack.isEmpty();
   }

   public static Entity getFromName(String name) {
      Iterator var1 = mc.world.loadedEntityList.iterator();

      Entity entity;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         entity = (Entity)var1.next();
      } while(!entity.getName().equals(name));

      return entity;
   }

   public static void strafe(BotPlayer player, float yaw, double speed, double forward, double direction) {
      if (forward != 0.0D) {
         if (direction > 0.0D) {
            yaw += (float)(forward > 0.0D ? -45 : 45);
         } else if (direction < 0.0D) {
            yaw += (float)(forward > 0.0D ? 45 : -45);
         }

         direction = 0.0D;
         if (forward > 0.0D) {
            forward = 1.0D;
         } else if (forward < 0.0D) {
            forward = -1.0D;
         }
      }

      double cos = Math.cos(Math.toRadians((double)(yaw + 90.0F)));
      double sin = Math.sin(Math.toRadians((double)(yaw + 90.0F)));
      double x = forward * speed * cos + direction * speed * sin;
      double z = forward * speed * sin - direction * speed * cos;
      player.motionX = x;
      player.motionZ = z;
   }

   public static float normalizeYaw(float f) {
      while(f > 360.0F) {
         f -= 360.0F;
      }

      while(f < 0.0F) {
         f += 360.0F;
      }

      return f;
   }

   public static float normalizePitch(float f) {
      while(f > 90.0F) {
         f -= 90.0F;
      }

      while(f < -90.0F) {
         f += 90.0F;
      }

      return f;
   }

   public static void run(String string, boolean use, String password) {
      (new Thread(() -> {
         Proxy proxy = use ? Main.proxy.getProxy() : null;
         GameProfile gameProfile = new GameProfile(UUID.randomUUID(), string);

         try {
            BotNetwork botNetwork = BotNetwork.createNetworkManagerAndConnect(InetAddress.getByName(GuiConnecting.ip), GuiConnecting.port, proxy);
            botNetwork.setNetHandler(new BotLoginClient(botNetwork));
            botNetwork.sendPacket(new C00Handshake(GuiConnecting.ip, GuiConnecting.port, EnumConnectionState.LOGIN));
            Thread.sleep(400L);
            botNetwork.sendPacket(new CPacketLoginStart(gameProfile));
            botNetwork.password = password;
         } catch (Exception var6) {
         }

      })).start();
   }

   public static Vector2f getBlockAngles(double d, double d2, double d3, double d4, double d5, double d6) {
      double d7 = d - d4;
      double d8 = d3 - d6;
      double d9 = d2 - d5 - 1.0D;
      double d10 = (double)net.minecraft.util.math.MathHelper.sqrt(d7 * d7 + d8 * d8);
      float f = (float)Math.toDegrees(-Math.atan(d7 / d8));
      float f2 = (float)(-Math.toDegrees(Math.atan(d9 / d10)));
      double d11 = Math.toDegrees(Math.atan(d8 / d7));
      if (d8 < 0.0D && d7 < 0.0D) {
         f = (float)(90.0D + d11);
      } else if (d8 < 0.0D && d7 > 0.0D) {
         f = (float)(-90.0D + d11);
      }

      return new Vector2f(f2, normalizeYaw(f));
   }
}
