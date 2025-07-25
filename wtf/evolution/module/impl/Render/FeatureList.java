/* Decompiler 58ms, total 444ms, lines 207 */
package wtf.evolution.module.impl.Render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventDisplay;
import wtf.evolution.helpers.ScaleUtil;
import wtf.evolution.helpers.font.FontRenderer;
import wtf.evolution.helpers.font.Fonts;
import wtf.evolution.helpers.render.ColorUtil;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.helpers.render.Translate;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.Setting;
import wtf.evolution.settings.options.BooleanSetting;
import wtf.evolution.settings.options.ColorSetting;
import wtf.evolution.settings.options.ModeSetting;
import wtf.evolution.settings.options.SliderSetting;

@ModuleInfo(
   name = "ArrayList",
   type = Category.Render
)
public class FeatureList extends Module {
   public static ArrayList<Module> modules = new ArrayList();
   public static ModeSetting mode = new ModeSetting("Color Mode", "Fade", new String[]{"Fade", "Rainbow", "Astolfo", "Static"});
   public static ModeSetting font = new ModeSetting("Font", "Rubik", new String[]{"Rubik", "Tenacity"});
   public static SliderSetting speed = (new SliderSetting("Color Speed", 5.0F, 1.0F, 9.9F, 1.0F)).setHidden(() -> {
      return mode.get().equalsIgnoreCase("Static");
   });
   public static SliderSetting animationSpeed = new SliderSetting("Animation Speed", 50.0F, 0.0F, 100.0F, 1.0F);
   public BooleanSetting hide = new BooleanSetting("Hide Render", true);
   public BooleanSetting glow = new BooleanSetting("Text Glow", true);
   public SliderSetting glowRadius = (new SliderSetting("Glow Radius", 15.0F, 5.0F, 80.0F, 5.0F)).setHidden(() -> {
      return !this.glow.get();
   });
   public SliderSetting glowAlpha = (new SliderSetting("Glow Alpha", 150.0F, 0.0F, 255.0F, 1.0F)).setHidden(() -> {
      return !this.glow.get();
   });
   public BooleanSetting shadow = new BooleanSetting("Shadow", true);
   public BooleanSetting rightLine = new BooleanSetting("Right Line", true);
   public BooleanSetting rightLineGlow = (new BooleanSetting("Right Line Glow", false)).setHidden(() -> {
      return !this.rightLine.get();
   });
   public SliderSetting rightLineRadius = (new SliderSetting("Right Line Radius", 15.0F, 0.0F, 25.0F, 1.0F)).setHidden(() -> {
      return !this.rightLine.get() && !this.rightLineGlow.get();
   });
   public SliderSetting rightLineAlpha = (new SliderSetting("Right Line Alpha", 150.0F, 0.0F, 255.0F, 1.0F)).setHidden(() -> {
      return !this.rightLine.get() && !this.rightLineGlow.get();
   });
   public BooleanSetting lower = new BooleanSetting("to Lower Case", true);
   public ColorSetting s = (new ColorSetting("Color", (new Color(123, 157, 245)).getRGB())).setHidden(() -> {
      return !mode.is("Static") && !mode.is("Fade");
   });

   public FeatureList() {
      this.addSettings(new Setting[]{this.hide, this.lower, this.glow, this.glowRadius, this.glowAlpha, this.shadow, this.rightLine, this.rightLineGlow, this.rightLineRadius, this.rightLineAlpha, mode, font, speed, animationSpeed, this.s});
   }

   public FontRenderer getFont() {
      if (font.is("Rubik")) {
         return Fonts.RUB14;
      } else {
         return font.is("Tenacity") ? Fonts.BOLD14 : null;
      }
   }

   @EventTarget
   public void onDisplay(EventDisplay e) {
      ScaleUtil.scale_pre();
      modules.sort((f1, f2) -> {
         return this.getFont().getStringWidth(this.lower.get() ? f1.getDisplayName().toLowerCase() : f1.getDisplayName()) > this.getFont().getStringWidth(this.lower.get() ? f2.getDisplayName().toLowerCase() : f2.getDisplayName()) ? -1 : 1;
      });
      int count = this.mc.player.getActivePotionEffects().size() > 0 ? (ScaledResolution.getScaleFactor() > 2 ? 6 : 3) : 0;
      int x = true;
      int y = false;
      Iterator var5 = modules.iterator();

      while(true) {
         Module m;
         float width;
         Translate translate;
         int color;
         do {
            if (!var5.hasNext()) {
               count = this.mc.player.getActivePotionEffects().size() > 0 ? (ScaledResolution.getScaleFactor() > 2 ? 6 : 3) : 0;
               var5 = modules.iterator();

               while(true) {
                  do {
                     if (!var5.hasNext()) {
                        count = this.mc.player.getActivePotionEffects().size() > 0 ? (ScaledResolution.getScaleFactor() > 2 ? 6 : 3) : 0;
                        var5 = modules.iterator();

                        while(true) {
                           do {
                              if (!var5.hasNext()) {
                                 ScaleUtil.scale_post();
                                 return;
                              }

                              m = (Module)var5.next();
                           } while(this.hide.get() && m.category == Category.Render);

                           width = (float)(ScaleUtil.calc(e.sr.getScaledWidth()) - this.getFont().getStringWidth(this.lower.get() ? m.getDisplayName().toLowerCase() : m.getDisplayName()) - 3);
                           translate = m.a;
                           if (m.isRender) {
                              color = this.getColor(count);
                              GL11.glPushMatrix();
                              GL11.glTranslated((double)(-2 - (this.rightLine.get() ? 1 : 0)), 2.0D, 0.0D);
                              if (this.rightLine.get() && this.rightLineGlow.get()) {
                                 RenderUtil.drawBlurredShadow((float)Math.round((float)translate.getX() - width + (float)ScaleUtil.calc(e.sr.getScaledWidth())), (float)Math.round((float)translate.getY()), 3.0F, 9.0F, (int)this.rightLineRadius.get(), new Color((new Color(color)).getRed(), (new Color(color)).getGreen(), (new Color(color)).getBlue(), (int)this.rightLineAlpha.get()));
                              }

                              if (this.glow.get()) {
                                 RenderUtil.drawBlurredShadow((float)Math.round((float)translate.getX()), (float)Math.round((float)translate.getY()) - 0.5F, width - (float)ScaleUtil.calc(e.sr.getScaledWidth()) + (float)(this.getFont().getStringWidth(this.lower.get() ? m.getDisplayName().toLowerCase() : m.getDisplayName()) * 2) + 6.0F, 9.0F, (int)this.glowRadius.get(), new Color((new Color(color)).getRed(), (new Color(color)).getGreen(), (new Color(color)).getBlue(), (int)this.glowAlpha.get()));
                              }

                              GL11.glPopMatrix();
                              ++count;
                           }
                        }
                     }

                     m = (Module)var5.next();
                  } while(this.hide.get() && m.category == Category.Render);

                  width = (float)(ScaleUtil.calc(e.sr.getScaledWidth()) - this.getFont().getStringWidth(this.lower.get() ? m.getDisplayName().toLowerCase() : m.getDisplayName()) - 3);
                  translate = m.a;
                  if (m.isRender) {
                     color = this.getColor(count);
                     GL11.glPushMatrix();
                     GL11.glTranslated((double)(-2 - (this.rightLine.get() ? 1 : 0)), 2.0D, 0.0D);
                     RenderUtil.drawRectWH((float)translate.getX(), (float)translate.getY(), width - (float)ScaleUtil.calc(e.sr.getScaledWidth()) + (float)(this.getFont().getStringWidth(this.lower.get() ? m.getDisplayName().toLowerCase() : m.getDisplayName()) * 2) + 6.0F, 9.0F, (new Color(10, 10, 10, 150)).getRGB());
                     if (this.rightLine.get()) {
                        RenderUtil.drawRectWH((float)translate.getX() - width + (float)ScaleUtil.calc(e.sr.getScaledWidth()), (float)translate.getY(), 1.0F, 9.0F, color);
                     }

                     this.getFont().drawString(this.lower.get() ? m.getDisplayName().toLowerCase() : m.getDisplayName(), (float)translate.getX() + 1.5F, (float)translate.getY() + 3.0F - (float)(this.getFont() == Fonts.REG14 ? 1 : 0), color);
                     GL11.glPopMatrix();
                     ++count;
                  }
               }
            }

            m = (Module)var5.next();
         } while(this.hide.get() && m.category == Category.Render);

         width = (float)(ScaleUtil.calc(e.sr.getScaledWidth()) - this.getFont().getStringWidth(this.lower.get() ? m.getDisplayName().toLowerCase() : m.getDisplayName()) - 3);
         translate = m.a;
         color = count * (this.getFont().getFontHeight() + 4);
         translate.interpolate(m.state ? (double)width : (double)ScaleUtil.calc(e.sr.getScaledWidth()) + 3.0D, (double)color, (double)(animationSpeed.get() / 5.0F * this.mc.deltaTime()));
         if (m.state && translate.getX() <= (double)ScaleUtil.calc(e.sr.getScaledWidth() + 3)) {
            m.isRender = true;
         }

         if (translate.getX() >= (double)(ScaleUtil.calc(e.sr.getScaledWidth()) + 3)) {
            m.isRender = false;
         }

         if (m.isRender) {
            GL11.glPushMatrix();
            GL11.glTranslated((double)(-2 - (this.rightLine.get() ? 1 : 0)), 2.0D, 0.0D);
            if (this.shadow.get()) {
               RenderUtil.drawBlurredShadow((float)translate.getX(), (float)translate.getY(), width - (float)ScaleUtil.calc(e.sr.getScaledWidth()) + (float)(this.getFont().getStringWidth(this.lower.get() ? m.getDisplayName().toLowerCase() : m.getDisplayName()) * 2) + 6.0F, 9.0F, 15, new Color(10, 10, 10, 200));
            }

            GL11.glPopMatrix();
            ++count;
         }
      }
   }

   public int getColor(int index) {
      if (mode.get().equalsIgnoreCase("Fade")) {
         return ColorUtil.fade(10 - (int)speed.get(), index * 50, new Color(this.s.get()), 1.0F).getRGB();
      } else if (mode.get().equalsIgnoreCase("Rainbow")) {
         return ColorUtil.rainbow(10 - (int)speed.get(), index * 40, 0.7F, 1.0F, 1.0F).getRGB();
      } else if (mode.get().equalsIgnoreCase("Astolfo")) {
         return astolfo(1.0F, (float)(index * 25), 0.5F, 10.0F - speed.get()).getRGB();
      } else {
         return mode.get().equalsIgnoreCase("Static") ? this.s.get() : -1;
      }
   }

   public static Color astolfo(float yDist, float yTotal, float saturation, float speedt) {
      float speed = 1800.0F;

      float hue;
      for(hue = (float)(System.currentTimeMillis() % (long)((int)speed)) + (yTotal - yDist) * speedt; hue > speed; hue -= speed) {
      }

      hue /= speed;
      if ((double)hue > 0.5D) {
         hue = 0.5F - (hue - 0.5F);
      }

      hue += 0.5F;
      return Color.getHSBColor(hue, saturation, 1.0F);
   }
}
