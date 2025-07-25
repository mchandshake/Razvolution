/* Decompiler 107ms, total 503ms, lines 247 */
package wtf.evolution.click;

import java.awt.Color;
import java.io.IOException;
import java.util.Iterator;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.opengl.GL11;
import wtf.evolution.Main;
import wtf.evolution.helpers.font.Fonts;
import wtf.evolution.helpers.math.MathHelper;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.helpers.render.RoundedUtil;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.settings.Setting;
import wtf.evolution.settings.options.BooleanSetting;
import wtf.evolution.settings.options.ModeSetting;
import wtf.evolution.settings.options.SliderSetting;

public class Skid extends GuiScreen {
   public Category selected;
   public int x;
   public int y;
   public int prevMouseX;
   public int prevMouseY;
   public boolean drag;
   public Module selMod;
   public double preX;
   public double rotX;

   public Skid() {
      this.selected = Category.Movement;
      this.x = 150;
      this.y = 150;
      this.prevMouseX = 0;
      this.prevMouseY = 0;
      this.selMod = (Module)Main.m.m.get(0);
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.mouseClicked(mouseX, mouseY, mouseButton);
      if (mouseButton == 0 && !this.drag && this.isHovered((float)this.x, (float)this.y, mouseX, mouseY, 250, 10)) {
         this.prevMouseX = this.x - mouseX;
         this.prevMouseY = this.y - mouseY;
         this.drag = true;
      }

      int y = this.y + 20;
      Category[] var5 = Category.values();
      int yMod = var5.length;

      for(int var7 = 0; var7 < yMod; ++var7) {
         Category c = var5[var7];
         if (this.isHovered((float)this.x + 7.5F, (float)y, mouseX, mouseY, 20, 20)) {
            this.selected = c;
         }

         y += 30;
      }

      int x = this.x + 40;
      yMod = this.y + 50;

      label104:
      for(Iterator var15 = Main.m.getModulesFromCategory(this.selected).iterator(); var15.hasNext(); x += 30) {
         Module m = (Module)var15.next();
         if (this.isHovered((float)(x - 2), (float)(this.y + 15), mouseX, mouseY, Fonts.REG16.getStringWidth(m.name) + 4, 7)) {
            this.selMod = m;
         }

         if (m == this.selMod) {
            if (this.isHovered((float)(this.x + 40), (float)(this.y + 35), mouseX, mouseY, 10, 10)) {
               this.selMod.toggle();
            }

            Iterator var9 = this.selMod.getSettings().iterator();

            while(true) {
               ModeSetting mS;
               do {
                  Setting s;
                  do {
                     if (!var9.hasNext()) {
                        continue label104;
                     }

                     s = (Setting)var9.next();
                     if (s instanceof BooleanSetting) {
                        BooleanSetting b = (BooleanSetting)s;
                        if (this.isHovered((float)(this.x + 40), (float)yMod, mouseX, mouseY, 10, 10)) {
                           b.set(!b.get());
                        }

                        yMod += 15;
                     }

                     if (s instanceof SliderSetting) {
                        SliderSetting sl = (SliderSetting)s;
                        if (this.isHovered((float)(this.x + 40), (float)(yMod + 7), mouseX, mouseY, 100, 3)) {
                           sl.sliding = true;
                        }

                        yMod += 15;
                     }
                  } while(!(s instanceof ModeSetting));

                  mS = (ModeSetting)s;
                  if (mouseButton == 1 && this.isHovered((float)(this.x + 40), (float)(yMod + 10), mouseX, mouseY, 100, 10 + (mS.opened ? mS.modes.size() * 8 + 2 : 0))) {
                     mS.opened = !mS.opened;
                  }
               } while(!mS.opened);

               for(Iterator var12 = mS.modes.iterator(); var12.hasNext(); yMod += 8) {
                  String mode = (String)var12.next();
                  if (this.isHovered((float)(this.x + 40), (float)(yMod + 20), mouseX, mouseY, 100, 10)) {
                     mS.currentMode = mode;
                  }
               }
            }
         }
      }

   }

   protected void mouseReleased(int mouseX, int mouseY, int state) {
      super.mouseReleased(mouseX, mouseY, state);
      if (this.drag) {
         this.drag = false;
      }

      Iterator var4 = this.selMod.getSettings().iterator();

      while(var4.hasNext()) {
         Setting s = (Setting)var4.next();
         if (s instanceof SliderSetting) {
            SliderSetting sl = (SliderSetting)s;
            sl.sliding = false;
         }
      }

   }

   public boolean isHovered(float x, float y, int mouseX, int mouseY, int width, int height) {
      return (float)mouseX >= x && (float)mouseX <= x + (float)width && (float)mouseY >= y && (float)mouseY <= y + (float)height;
   }

   public void drag(int mouseX, int mouseY) {
      if (this.drag) {
         this.x = mouseX + this.prevMouseX;
         this.y = mouseY + this.prevMouseY;
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      super.drawScreen(mouseX, mouseY, partialTicks);
      this.drag(mouseX, mouseY);
      this.rotX = MathHelper.interpolate(this.drag ? (this.preX - (double)mouseX) * 20.0D : 0.0D, this.rotX, 0.01D);
      GL11.glPushMatrix();
      GL11.glTranslated((double)((float)this.x + 125.0F), (double)((float)this.y + 5.0F), 0.0D);
      GL11.glRotated(-this.rotX, 0.0D, 0.0D, 1.0D);
      GL11.glTranslated((double)((float)(-this.x) - 125.0F), (double)((float)(-this.y) - 5.0F), 0.0D);
      RenderUtil.drawRectWH((float)this.x, (float)this.y, 250.0F, 230.0F, (new Color(21, 21, 21, 255)).getRGB());
      RenderUtil.drawRectWH((float)this.x, (float)this.y, 35.0F, 230.0F, (new Color(15, 15, 15, 255)).getRGB());
      RenderUtil.drawRectWH((float)this.x, (float)this.y, 250.0F, 10.0F, (new Color(10, 10, 10, 255)).getRGB());
      Fonts.REG16.drawString("skidproject - " + this.selected.name() + " - " + this.selMod.name, (float)this.x + 2.5F, (float)(this.y + 2), (new Color(255, 255, 255, 255)).getRGB());
      int y = this.y + 20;
      Category[] var5 = Category.values();
      int yMod = var5.length;

      for(int var7 = 0; var7 < yMod; ++var7) {
         Category var10000 = var5[var7];
         RenderUtil.drawRectWH((float)this.x + 7.5F, (float)y, 20.0F, 20.0F, (new Color(0, 0, 0)).getRGB());
         y += 30;
      }

      int x = this.x + 40;
      yMod = this.y + 50;

      label83:
      for(Iterator var15 = Main.m.getModulesFromCategory(this.selected).iterator(); var15.hasNext(); x += 30) {
         Module m = (Module)var15.next();
         if (m == this.selMod) {
            RenderUtil.drawRectWH((float)(x - 2), (float)(this.y + 25), (float)(Fonts.REG16.getStringWidth(m.name) + 4), 1.0F, (new Color(100, 100, 255)).getRGB());
         }

         Fonts.REG16.drawString(m.name, (float)x, (float)(this.y + 15), (new Color(255, 255, 255)).getRGB());
         if (m == this.selMod) {
            RenderUtil.drawRectWH((float)(this.x + 40), (float)(this.y + 35), 10.0F, 10.0F, m.state ? (new Color(100, 100, 255)).getRGB() : (new Color(5, 5, 5)).getRGB());
            Fonts.REG16.drawString("Activate", (float)(this.x + 55), (float)(this.y + 37), -1);
            Iterator var9 = this.selMod.getSettings().iterator();

            while(true) {
               Setting s;
               do {
                  if (!var9.hasNext()) {
                     continue label83;
                  }

                  s = (Setting)var9.next();
                  if (s instanceof BooleanSetting) {
                     BooleanSetting b = (BooleanSetting)s;
                     RenderUtil.drawRectWH((float)(this.x + 40), (float)yMod, 10.0F, 10.0F, b.get() ? (new Color(100, 100, 255)).getRGB() : (new Color(5, 5, 5)).getRGB());
                     Fonts.REG16.drawString(b.name, (float)(this.x + 55), (float)(yMod + 2), -1);
                     yMod += 15;
                  }

                  if (s instanceof SliderSetting) {
                     SliderSetting b = (SliderSetting)s;
                     if (b.sliding) {
                        b.current = net.minecraft.util.math.MathHelper.clamp((float)((double)(mouseX - this.x - 140) * (double)(b.maximum - b.minimum) / 100.0D + (double)b.maximum), b.minimum, b.maximum);
                     }

                     float amountWidth = (b.current - b.minimum) / (b.maximum - b.minimum);
                     RoundedUtil.drawRound((float)(this.x + 40), (float)(yMod + 7), 100.0F, 3.0F, 0.0F, new Color(56, 56, 56));
                     RoundedUtil.drawRound((float)(this.x + 40), (float)(yMod + 7), amountWidth * 100.0F, 3.0F, 0.0F, new Color(255, 56, 56));
                     Fonts.REG16.drawString(String.valueOf(b.current), (float)(this.x + 140 - Fonts.REG16.getStringWidth(String.valueOf(b.current))), (float)(yMod - 1), Color.GRAY.getRGB());
                     Fonts.REG16.drawString(b.name, (float)(this.x + 40), (float)(yMod - 1), Color.GRAY.getRGB());
                     yMod += 15;
                  }
               } while(!(s instanceof ModeSetting));

               ModeSetting b = (ModeSetting)s;
               RoundedUtil.drawRound((float)(this.x + 40), (float)(yMod + 10), 100.0F, (float)(10 + (b.opened ? b.modes.size() * 8 + 2 : 0)), 2.0F, new Color(27, 27, 27));
               Fonts.REG16.drawCenteredString(b.currentMode, (float)(this.x + 90), (float)(yMod + 12), -1);
               Fonts.REG16.drawString(b.name, (float)(this.x + 40), (float)(yMod + 2), Color.GRAY.getRGB());
               if (b.opened) {
                  for(Iterator var18 = b.modes.iterator(); var18.hasNext(); yMod += 8) {
                     String mode = (String)var18.next();
                     if (mode == b.currentMode) {
                        RoundedUtil.drawRound((float)(this.x + 40), (float)(yMod + 20), 100.0F, 8.0F, 2.0F, new Color(39, 39, 39));
                     }

                     Fonts.REG16.drawCenteredString(mode, (float)(this.x + 90), (float)(yMod + 21), -1);
                  }
               }

               yMod += 15;
            }
         }
      }

      GL11.glPopMatrix();
      this.preX = (double)mouseX;
   }
}
