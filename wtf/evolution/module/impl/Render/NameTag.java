/* Decompiler 58ms, total 499ms, lines 315 */
package wtf.evolution.module.impl.Render;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventDisplay;
import wtf.evolution.event.events.impl.EventRender;
import wtf.evolution.helpers.font.Fonts;
import wtf.evolution.helpers.math.MathHelper;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.helpers.render.RoundedUtil;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.Setting;
import wtf.evolution.settings.options.BooleanSetting;
import wtf.evolution.settings.options.ModeSetting;
import wtf.evolution.settings.options.SliderSetting;

@ModuleInfo(
   name = "NameTag",
   type = Category.Render
)
public class NameTag extends Module {
   public static Map<EntityPlayer, double[]> entityPositions = new HashMap();
   public BooleanSetting armor = new BooleanSetting("Show Armor", true);
   public BooleanSetting potion = new BooleanSetting("Show Potions", true);
   public BooleanSetting backGround = new BooleanSetting("NameTags Background", true);
   public ModeSetting backGroundMode = new ModeSetting("Background Mode", "Default", new String[]{"Default", "Shader"});
   public BooleanSetting offand;
   public SliderSetting opacity = (new SliderSetting("Background Opacity", 150.0F, 0.0F, 255.0F, 5.0F)).setHidden(() -> {
      return !this.backGround.get();
   });
   public SliderSetting size = new SliderSetting("NameTags Size", 1.0F, 0.5F, 2.0F, 0.1F);

   public NameTag() {
      this.addSettings(new Setting[]{this.size, this.backGround, this.backGroundMode, this.opacity, this.armor, this.potion});
   }

   public static TextFormatting getHealthColor(float health) {
      if (health <= 4.0F) {
         return TextFormatting.RED;
      } else if (health <= 8.0F) {
         return TextFormatting.GOLD;
      } else if (health <= 12.0F) {
         return TextFormatting.YELLOW;
      } else {
         return health <= 16.0F ? TextFormatting.DARK_GREEN : TextFormatting.GREEN;
      }
   }

   @EventTarget
   public void onRender3D(EventRender event) {
      this.updatePositions();
   }

   @EventTarget
   public void onRender2D(EventDisplay event) {
      ScaledResolution sr = new ScaledResolution(this.mc);
      GlStateManager.pushMatrix();
      Iterator var3 = entityPositions.keySet().iterator();

      while(true) {
         while(true) {
            EntityPlayer entity;
            do {
               if (!var3.hasNext()) {
                  GlStateManager.popMatrix();
                  return;
               }

               entity = (EntityPlayer)var3.next();
            } while(entity == this.mc.player);

            GlStateManager.pushMatrix();
            double[] array = (double[])entityPositions.get(entity);
            if (array[3] >= 0.0D && array[3] < 1.0D) {
               double scaleFactor = (double)ScaledResolution.getScaleFactor();
               GlStateManager.translate(array[0] / scaleFactor, array[1] / scaleFactor, 0.0D);
               this.scale();
               String string = entity.getName();
               String stringHP = MathHelper.round((double)entity.getHealth(), 1.0D) + " ";
               String string2 = "" + stringHP;
               float width = (float)(Fonts.RUB16.getStringWidth(string2 + " " + string) + 6);
               GlStateManager.translate(0.0D, -10.0D, 0.0D);
               if (this.backGround.get()) {
                  if (this.backGroundMode.is("Default")) {
                     RenderUtil.drawBlurredShadow(-width / 2.0F - 2.0F, -10.0F, width + 3.0F, 11.0F, 20, new Color(0, 0, 0, 255));
                     RoundedUtil.drawRound(-width / 2.0F - 2.0F, -10.0F, width + 3.0F, 11.0F, 1.0F, new Color(0, 0, 0, (int)this.opacity.get()));
                  } else if (this.backGroundMode.is("Shader")) {
                     RenderUtil.drawBlurredShadow(-width / 2.0F - 2.0F, -10.0F, width + 3.0F, 11.0F, 15, new Color(0, 0, 0, (int)this.opacity.get()));
                  }
               }

               Fonts.RUB16.drawStringWithShadow(string + " " + getHealthColor(entity.getHealth()) + string2, (double)(-width / 2.0F + 2.0F), -6.5D, -1);
               ItemStack heldItemStack = entity.getHeldItem(EnumHand.OFF_HAND);
               ArrayList<ItemStack> list = new ArrayList();
               Iterator var14 = entity.getArmorInventoryList().iterator();

               while(var14.hasNext()) {
                  ItemStack i = (ItemStack)var14.next();
                  list.add(i);
               }

               int n10 = -(list.size() * 9) - 8;
               if (this.armor.get()) {
                  GlStateManager.pushMatrix();
                  GlStateManager.translate(0.0F, -2.0F, 0.0F);
                  this.mc.getRenderItem().renderItemIntoGUI(heldItemStack, (double)(n10 + 70), -28.0D);
                  this.mc.getRenderItem().renderItemOverlays(this.mc.fontRenderer, heldItemStack, (double)(n10 + 70), -28.0D);

                  for(Iterator var25 = list.iterator(); var25.hasNext(); n10 = (int)((double)n10 + 13.5D)) {
                     ItemStack itemStack = (ItemStack)var25.next();
                     RenderHelper.enableGUIStandardItemLighting();
                     this.mc.getRenderItem().renderItemIntoGUI(itemStack, (double)(n10 + 6), -28.0D);
                     this.mc.getRenderItem().renderItemOverlays(this.mc.fontRenderer, itemStack, (double)(n10 + 5), -28.0D);
                     n10 += 3;
                     RenderHelper.disableStandardItemLighting();
                     int n11 = 7;
                     int getEnchantmentLevel = EnchantmentHelper.getEnchantmentLevel((Enchantment)Objects.requireNonNull(Enchantment.getEnchantmentByID(16)), itemStack);
                     int getEnchantmentLevel2 = EnchantmentHelper.getEnchantmentLevel((Enchantment)Objects.requireNonNull(Enchantment.getEnchantmentByID(20)), itemStack);
                     int getEnchantmentLevel3 = EnchantmentHelper.getEnchantmentLevel((Enchantment)Objects.requireNonNull(Enchantment.getEnchantmentByID(19)), itemStack);
                     if (getEnchantmentLevel > 0) {
                        this.drawEnchantTag("S" + this.getColor(getEnchantmentLevel) + getEnchantmentLevel, n10, n11);
                        n11 += 8;
                     }

                     if (getEnchantmentLevel2 > 0) {
                        this.drawEnchantTag("F" + this.getColor(getEnchantmentLevel2) + getEnchantmentLevel2, n10, n11);
                        n11 += 8;
                     }

                     if (getEnchantmentLevel3 > 0) {
                        this.drawEnchantTag("Kb" + this.getColor(getEnchantmentLevel3) + getEnchantmentLevel3, n10, n11);
                     } else {
                        int getEnchantmentLevel7;
                        int getEnchantmentLevel8;
                        int getEnchantmentLevel9;
                        if (itemStack.getItem() instanceof ItemArmor) {
                           getEnchantmentLevel7 = EnchantmentHelper.getEnchantmentLevel((Enchantment)Objects.requireNonNull(Enchantment.getEnchantmentByID(0)), itemStack);
                           getEnchantmentLevel8 = EnchantmentHelper.getEnchantmentLevel((Enchantment)Objects.requireNonNull(Enchantment.getEnchantmentByID(7)), itemStack);
                           getEnchantmentLevel9 = EnchantmentHelper.getEnchantmentLevel((Enchantment)Objects.requireNonNull(Enchantment.getEnchantmentByID(34)), itemStack);
                           if (getEnchantmentLevel7 > 0) {
                              this.drawEnchantTag("P" + this.getColor(getEnchantmentLevel7) + getEnchantmentLevel7, n10, n11);
                              n11 += 8;
                           }

                           if (getEnchantmentLevel8 > 0) {
                              this.drawEnchantTag("Th" + this.getColor(getEnchantmentLevel8) + getEnchantmentLevel8, n10, n11);
                              n11 += 8;
                           }

                           if (getEnchantmentLevel9 > 0) {
                              this.drawEnchantTag("U" + this.getColor(getEnchantmentLevel9) + getEnchantmentLevel9, n10, n11);
                           }
                        } else if (itemStack.getItem() instanceof ItemBow) {
                           getEnchantmentLevel7 = EnchantmentHelper.getEnchantmentLevel((Enchantment)Objects.requireNonNull(Enchantment.getEnchantmentByID(48)), itemStack);
                           getEnchantmentLevel8 = EnchantmentHelper.getEnchantmentLevel((Enchantment)Objects.requireNonNull(Enchantment.getEnchantmentByID(49)), itemStack);
                           getEnchantmentLevel9 = EnchantmentHelper.getEnchantmentLevel((Enchantment)Objects.requireNonNull(Enchantment.getEnchantmentByID(50)), itemStack);
                           if (getEnchantmentLevel7 > 0) {
                              this.drawEnchantTag("P" + this.getColor(getEnchantmentLevel7) + getEnchantmentLevel7, n10, n11);
                              n11 += 8;
                           }

                           if (getEnchantmentLevel8 > 0) {
                              this.drawEnchantTag("P" + this.getColor(getEnchantmentLevel8) + getEnchantmentLevel8, n10, n11);
                              n11 += 8;
                           }

                           if (getEnchantmentLevel9 > 0) {
                              this.drawEnchantTag("F" + this.getColor(getEnchantmentLevel9) + getEnchantmentLevel9, n10, n11);
                           }
                        }
                     }
                  }

                  GlStateManager.popMatrix();
                  if (this.potion.get()) {
                     this.drawPotionEffect(entity);
                  }
               }

               GlStateManager.popMatrix();
            } else {
               GlStateManager.popMatrix();
            }
         }
      }
   }

   private void drawPotionEffect(EntityPlayer entity) {
      float tagwidth = 0.0F;
      int stringY = -25;
      if (entity.getTotalArmorValue() > 0 || !entity.getHeldItemMainhand().isEmpty() && !entity.getHeldItemOffhand().isEmpty()) {
         stringY -= 37;
      }

      Iterator var4 = entity.getActivePotionEffects().iterator();

      while(var4.hasNext()) {
         PotionEffect potionEffect = (PotionEffect)var4.next();
         Potion potion = potionEffect.getPotion();
         boolean potRanOut = (double)potionEffect.getDuration() != 0.0D;
         String power = "";
         if (entity.isPotionActive(potion) && potRanOut) {
            if (potionEffect.getAmplifier() == 1) {
               power = "II";
            } else if (potionEffect.getAmplifier() == 2) {
               power = "III";
            } else if (potionEffect.getAmplifier() == 3) {
               power = "IV";
            }

            GlStateManager.pushMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            float var10000 = tagwidth - (float)Fonts.RUB16.getStringWidth(I18n.format(potion.getName(), new Object[0]) + " " + power + TextFormatting.GRAY + " " + Potion.getPotionDurationString(potionEffect, 1.0F)) / 2.0F;
            stringY -= 10;
            GlStateManager.popMatrix();
         }
      }

   }

   private void drawEnchantTag(String text, int n, int n2) {
      GlStateManager.pushMatrix();
      GlStateManager.disableDepth();
      n2 -= 7;
      Fonts.RUB16.drawStringWithShadow(text, (float)(n + 6), (float)(-35 - n2), -1);
      GlStateManager.enableDepth();
      GlStateManager.popMatrix();
   }

   private String getColor(int n) {
      if (n != 1) {
         if (n == 2) {
            return "";
         }

         if (n == 3) {
            return "";
         }

         if (n == 4) {
            return "";
         }

         if (n >= 5) {
            return "";
         }
      }

      return "";
   }

   private void updatePositions() {
      entityPositions.clear();
      float pTicks = this.mc.timer.renderPartialTicks;
      Iterator var2 = this.mc.world.playerEntities.iterator();

      while(var2.hasNext()) {
         EntityPlayer o = (EntityPlayer)var2.next();
         if (o != this.mc.player) {
            double x = o.lastTickPosX + (o.posX - o.lastTickPosX) * (double)pTicks - this.mc.getRenderManager().viewerPosX;
            double y = o.lastTickPosY + (o.posY - o.lastTickPosY) * (double)pTicks - this.mc.getRenderManager().viewerPosY;
            double z = o.lastTickPosZ + (o.posZ - o.lastTickPosZ) * (double)pTicks - this.mc.getRenderManager().viewerPosZ;
            if (((double[])Objects.requireNonNull(this.convertTo2D(x, y += (double)o.height + 0.2D, z)))[2] >= 0.0D && ((double[])Objects.requireNonNull(this.convertTo2D(x, y, z)))[2] < 2.0D) {
               entityPositions.put(o, new double[]{((double[])Objects.requireNonNull(this.convertTo2D(x, y, z)))[0], ((double[])Objects.requireNonNull(this.convertTo2D(x, y, z)))[1], Math.abs(((double[])Objects.requireNonNull(this.convertTo2D(x, y + 1.0D, z)))[1] - ((double[])Objects.requireNonNull(this.convertTo2D(x, y, z)))[1]), ((double[])Objects.requireNonNull(this.convertTo2D(x, y, z)))[2]});
            }
         }
      }

   }

   private double[] convertTo2D(double x, double y, double z) {
      FloatBuffer screenCords = BufferUtils.createFloatBuffer(3);
      IntBuffer viewport = BufferUtils.createIntBuffer(16);
      FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
      FloatBuffer projection = BufferUtils.createFloatBuffer(16);
      GL11.glGetFloat(2982, modelView);
      GL11.glGetFloat(2983, projection);
      GL11.glGetInteger(2978, viewport);
      boolean result = GLU.gluProject((float)x, (float)y, (float)z, modelView, projection, viewport, screenCords);
      return result ? new double[]{(double)screenCords.get(0), (double)((float)Display.getHeight() - screenCords.get(1)), (double)screenCords.get(2)} : null;
   }

   private void scale() {
      float n = this.mc.gameSettings.smoothCamera ? 2.0F : this.size.get();
      GlStateManager.scale(n, n, n);
   }
}
