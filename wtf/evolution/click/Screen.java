/* Decompiler 793ms, total 1327ms, lines 951 */
package wtf.evolution.click;

import java.awt.Color;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import wtf.evolution.Main;
import wtf.evolution.helpers.ScaleUtil;
import wtf.evolution.helpers.StencilUtil;
import wtf.evolution.helpers.animation.Animation;
import wtf.evolution.helpers.animation.Direction;
import wtf.evolution.helpers.animation.impl.EaseBackIn;
import wtf.evolution.helpers.animation.impl.EaseInOutQuad;
import wtf.evolution.helpers.font.Fonts;
import wtf.evolution.helpers.math.MathHelper;
import wtf.evolution.helpers.render.GaussianBlur;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.helpers.render.RoundedUtil;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.impl.Render.ClickGui;
import wtf.evolution.settings.Setting;
import wtf.evolution.settings.options.BooleanSetting;
import wtf.evolution.settings.options.ColorSetting;
import wtf.evolution.settings.options.ListSetting;
import wtf.evolution.settings.options.ModeSetting;
import wtf.evolution.settings.options.SliderSetting;

public class Screen extends GuiScreen {
   public int x;
   public int y;
   public boolean dragging = false;
   public boolean categoryOpen = false;
   public Category selectedType;
   public Category[] types;
   public Animation openAnimation;
   public Animation categoryAnimation;
   public double scrollX;
   public double scrollXA;
   public boolean bue;
   public double prevX;
   public double prevY;
   public boolean drag;
   public int prevMouseX;
   public int prevMouseY;
   public boolean binding;
   public String text;
   public Module bindingModule;
   public Category preSelected;
   public boolean animating;
   public ArrayList<String> texts;

   public Screen() {
      this.selectedType = Category.Combat;
      this.openAnimation = new EaseBackIn(250, 1.0D, 1.0F);
      this.categoryAnimation = new EaseInOutQuad(250, 1.0D);
      this.text = "";
      this.texts = new ArrayList();
      this.types = Category.values();
      this.x = 200;
      this.y = 100;
   }

   public void initGui() {
      super.initGui();
      this.openAnimation = new EaseBackIn(500, 1.0D, 1.0F);
      this.openAnimation.setDirection(Direction.FORWARDS);
   }

   public void drawScreen(int mouseX1, int mouseY1, float partialTicks) {
      super.drawScreen(mouseX1, mouseX1, partialTicks);
      int mouseX = (int)ScaleUtil.calc((float)mouseX1, (float)mouseY1)[0];
      int mouseY = (int)ScaleUtil.calc((float)mouseX1, (float)mouseY1)[1];
      if (this.openAnimation.getOutput() == 0.0D) {
         this.mc.player.closeScreen();
      }

      this.dragging(mouseX, mouseY);
      ScaledResolution sr = new ScaledResolution(this.mc);
      this.x = (int)MathHelper.clamp((float)this.x, 0.0F, (float)(ScaleUtil.calc(sr.getScaledWidth()) - 300));
      this.y = (int)MathHelper.clamp((float)this.y, 0.0F, (float)(ScaleUtil.calc(sr.getScaledHeight()) - 200));
      GaussianBlur.renderBlur(3.0F);
      this.drawDefaultBackground();
      RenderUtil.drawRect(0.0F, 0.0F, (float)this.mc.displayWidth, (float)this.mc.displayHeight, (new Color((new Color(ClickGui.getColor())).getRed(), (new Color(ClickGui.getColor())).getGreen(), (new Color(ClickGui.getColor())).getBlue(), (int)(this.openAnimation.getOutput() * 30.0D))).darker().darker().darker().getRGB());
      ScaleUtil.scale_pre();
      this.categoryAnimation.setDirection(this.categoryOpen ? Direction.FORWARDS : Direction.BACKWARDS);
      this.categoryOpen = RenderUtil.isHovered((float)mouseX, (float)mouseY, (float)(this.x - 2), (float)this.y, (float)(25.0D + this.categoryAnimation.getOutput() * 50.0D), 200.0F);
      int alpha = 255;
      RenderUtil.drawBlurredShadow((float)this.x, (float)this.y, 300.0F, 200.0F, 15, Color.BLACK);
      StencilUtil.initStencilToWrite();
      RoundedUtil.drawRound((float)this.x, (float)this.y, 300.0F, 200.0F, 7.0F, new Color(21, 21, 21, alpha));
      StencilUtil.readStencilBuffer(1);
      RoundedUtil.drawRound((float)this.x, (float)this.y, 300.0F, 200.0F, 7.0F, new Color(21, 21, 21, alpha));
      if (this.animating) {
         this.selectedType = this.preSelected;
         this.preSelected = null;
         this.animating = false;
         this.scrollX = 0.0D;
      }

      for(int i = 0; i < 15; ++i) {
         RenderUtil.drawRectWH((float)((double)this.x + this.categoryAnimation.getOutput() * 50.0D + 5.0D + (double)(i * 25)), (float)this.y, 0.5F, 200.0F, (new Color(25, 25, 25, 255)).getRGB());
         RenderUtil.drawRectWH((float)(this.x + 5), (float)(this.y + i * 25), 300.0F, 0.5F, (new Color(25, 25, 25, 255)).getRGB());
      }

      List<Module> list = Main.m.getModulesFromCategory(this.selectedType);
      List<Module> modules = (List)Main.m.getModulesFromCategory(this.selectedType).stream().filter((f1) -> {
         return list.indexOf(f1) % 2 == 0;
      }).collect(Collectors.toList());
      List<Module> modules2 = (List)Main.m.getModulesFromCategory(this.selectedType).stream().filter((f1) -> {
         return list.indexOf(f1) % 2 != 0;
      }).collect(Collectors.toList());
      int x = (int)((double)(this.x + 35) + this.categoryAnimation.getOutput() * 50.0D);
      float y = (float)((double)(this.y + 10) + this.scrollXA);

      label477:
      for(Iterator var14 = modules.iterator(); var14.hasNext(); y += 25.0F) {
         Module m = (Module)var14.next();
         float heightBoost = 0.0F;
         Iterator var17 = m.getSettingsForGUI().iterator();

         while(true) {
            Setting s;
            while(true) {
               ListSetting b;
               while(true) {
                  ModeSetting b;
                  do {
                     if (!var17.hasNext()) {
                        RoundedUtil.drawRound((float)x, y, 125.0F, (float)(20 + (m.opened ? m.getSettingsForGUI().size() * 20 : 0)) + heightBoost, 3.0F, m.state ? new Color(30, 30, 30, alpha) : new Color(27, 27, 27, alpha));
                        Fonts.REG16.drawString(m.name, (float)(x + 5), y + 7.0F, m.state ? ClickGui.getColor() : (new Color(255, 255, 255, alpha)).getRGB());
                        if (this.binding && this.bindingModule == m) {
                           Fonts.REG16.drawString("[binding..]", (float)(x + 120 - Fonts.REG16.getStringWidth("[binding..]")), y + 7.0F, (new Color(255, 255, 255, alpha)).getRGB());
                        } else if (m.bind > 1) {
                           Fonts.REG16.drawString("[" + Keyboard.getKeyName(m.bind) + "]", (float)(x + 120 - Fonts.REG16.getStringWidth("[" + Keyboard.getKeyName(m.bind) + "]")), y + 7.0F, (new Color(255, 255, 255, alpha)).getRGB());
                        }

                        if (m.opened) {
                           var17 = m.getSettingsForGUI().iterator();

                           while(true) {
                              while(true) {
                                 int i;
                                 while(true) {
                                    while(true) {
                                       while(true) {
                                          if (!var17.hasNext()) {
                                             continue label477;
                                          }

                                          s = (Setting)var17.next();
                                          if (!(s instanceof ListSetting)) {
                                             break;
                                          }

                                          b = (ListSetting)s;
                                          if (!(Boolean)b.hidden.get()) {
                                             y += 20.0F;
                                             RoundedUtil.drawRound((float)(x + 2), y + 5.0F, 121.0F, 10.0F, 3.0F, new Color(36, 36, 36, alpha));
                                             Fonts.REG16.drawString(b.name, (float)(x + 3), y - 3.0F, (new Color(73, 73, 73, alpha)).getRGB());
                                             Fonts.REG16.drawCenteredString("selected " + b.selected.size() + "/" + b.list.size(), (float)(x + 60), y + 7.0F, (new Color(255, 255, 255, alpha)).getRGB());
                                             if (!b.opened) {
                                                break;
                                             }

                                             RoundedUtil.drawRound((float)(x + 2), y + 20.0F, 121.0F, (float)(b.opened ? b.list.size() * 10 : 0), 3.0F, new Color(36, 36, 36, alpha));

                                             for(i = 0; i < b.list.size(); ++i) {
                                                Fonts.REG16.drawCenteredString((String)b.list.get(i), (float)(x + 60), y + 22.0F + (float)(i * 10), b.selected.contains(b.list.get(i)) ? ClickGui.getColor() : -1);
                                             }

                                             y += (float)(b.list.size() * 10 + 5);
                                             break;
                                          }
                                       }

                                       if (!(s instanceof BooleanSetting)) {
                                          break;
                                       }

                                       BooleanSetting b = (BooleanSetting)s;
                                       if (!(Boolean)b.hidden.get()) {
                                          y += 20.0F;
                                          b.animation.setDirection(b.get() ? Direction.FORWARDS : Direction.BACKWARDS);
                                          RoundedUtil.drawRound((float)(x + 2), y, 121.0F, 15.0F, 3.0F, new Color(36, 36, 36, alpha));
                                          RoundedUtil.drawRound((float)(x + 100), y + 5.0F, 10.0F, 5.0F, 2.0F, new Color(45, 45, 45, alpha));
                                          RenderUtil.drawBlurredShadow((float)((double)(x + 100) + b.animation.getOutput() * 5.0D), y + 5.0F, 5.0F, 5.0F, 15, b.get() ? new Color(ClickGui.getColor()) : new Color(89, 89, 89, alpha));
                                          RoundedUtil.drawRound((float)((double)(x + 100) + b.animation.getOutput() * 5.0D), y + 5.0F, 5.0F, 5.0F, 2.0F, b.get() ? new Color(ClickGui.getColor()) : new Color(89, 89, 89, alpha));
                                          Fonts.REG16.drawString(b.name.toLowerCase(), (float)(x + 5), y + 4.0F, (new Color(100, 100, 100, alpha)).getRGB());
                                          break;
                                       }
                                    }

                                    if (!(s instanceof SliderSetting)) {
                                       break;
                                    }

                                    SliderSetting b = (SliderSetting)s;
                                    if (!(Boolean)b.hidden.get()) {
                                       y += 20.0F;
                                       RoundedUtil.drawRound((float)(x + 2), y, 121.0F, 15.0F, 3.0F, new Color(36, 36, 36, alpha));
                                       if (b.sliding) {
                                          b.current = (float)MathHelper.round((double)net.minecraft.util.math.MathHelper.clamp((float)((double)(mouseX - x - 120) * (double)(b.maximum - b.minimum) / 115.0D + (double)b.maximum), b.minimum, b.maximum), (double)b.increment);
                                       }

                                       b.sliderWidth = MathHelper.interpolate((b.current - b.minimum) / (b.maximum - b.minimum) * 115.0F, b.sliderWidth, 0.3D);
                                       float amountWidth = (b.current - b.minimum) / (b.maximum - b.minimum);
                                       RoundedUtil.drawRound((float)(x + 5), y + 9.0F, 115.0F, 2.0F, 1.0F, new Color(56, 56, 56, alpha));
                                       RenderUtil.drawBlurredShadow((float)(x + 5), y + 9.0F, b.sliderWidth, 2.0F, 15, new Color(ClickGui.getColor()));
                                       RoundedUtil.drawRound((float)(x + 5), y + 9.0F, b.sliderWidth, 2.0F, 1.0F, new Color(ClickGui.getColor()));
                                       RenderUtil.drawBlurredShadow((float)(x + 3) + b.sliderWidth, y + 8.0F, 4.0F, 4.0F, 15, new Color(ClickGui.getColor()));
                                       RoundedUtil.drawRound((float)(x + 3) + b.sliderWidth, y + 8.0F, 4.0F, 4.0F, 1.5F, new Color(ClickGui.getColor()));
                                       Fonts.REG12.drawString(String.valueOf(b.current), (float)(x + 120 - Fonts.REG16.getStringWidth(String.valueOf(b.current))), y + 2.0F, (new Color(100, 100, 100, alpha)).getRGB());
                                       Fonts.REG12.drawString(b.name, (float)(x + 5), y + 2.0F, (new Color(100, 100, 100, alpha)).getRGB());
                                       break;
                                    }
                                 }

                                 if (!(s instanceof ModeSetting)) {
                                    break;
                                 }

                                 b = (ModeSetting)s;
                                 if (!(Boolean)b.hidden.get()) {
                                    y += 20.0F;
                                    RoundedUtil.drawRound((float)(x + 2), y + 5.0F, 121.0F, 10.0F, 3.0F, new Color(36, 36, 36, alpha));
                                    Fonts.REG16.drawString(b.name, (float)(x + 3), y - 3.0F, (new Color(73, 73, 73, alpha)).getRGB());
                                    Fonts.REG16.drawCenteredString(b.currentMode, (float)(x + 60), y + 7.0F, (new Color(255, 255, 255, alpha)).getRGB());
                                    if (!b.opened) {
                                       break;
                                    }

                                    RoundedUtil.drawRound((float)(x + 2), y + 20.0F, 121.0F, (float)(b.opened ? b.modes.size() * 10 : 0), 3.0F, new Color(36, 36, 36, alpha));

                                    for(i = 0; i < b.modes.size(); ++i) {
                                       Fonts.REG16.drawCenteredString((String)b.modes.get(i), (float)(x + 60), y + 22.0F + (float)(i * 10), b.currentMode.equalsIgnoreCase((String)b.modes.get(i)) ? ClickGui.getColor() : (new Color(255, 255, 255, alpha)).getRGB());
                                    }

                                    y += (float)(b.modes.size() * 10 + 5);
                                    break;
                                 }
                              }

                              if (s instanceof ColorSetting) {
                                 ColorSetting b = (ColorSetting)s;
                                 if (!(Boolean)b.hidden.get()) {
                                    y += 20.0F;
                                    double soX = (double)(mouseX - x);
                                    double soY = (double)((float)mouseY - y);
                                    soX -= 20.0D;
                                    soY -= 17.5D;
                                    double dst = Math.sqrt(soX * soX + soY * soY);
                                    double dst1 = Math.sqrt(soX * soX);
                                    float[] hsb = Color.RGBtoHSB((new Color(b.get())).getRed(), (new Color(b.get())).getGreen(), (new Color(b.get())).getBlue(), (float[])null);
                                    double poX = (double)(hsb[1] * 15.0F) * (Math.sin(Math.toRadians((double)(hsb[0] * 360.0F))) / Math.sin(Math.toRadians(90.0D)));
                                    double poY = (double)(hsb[1] * 15.0F) * (Math.sin(Math.toRadians((double)(90.0F - hsb[0] * 360.0F))) / Math.sin(Math.toRadians(90.0D)));
                                    if (dst > 15.0D) {
                                       b.slid = false;
                                    }

                                    if (b.slid) {
                                       b.color = Color.HSBtoRGB((float)(Math.atan2(soX, soY) / 6.283185307179586D - 1.0D), (float)(dst / 15.0D), 1.0F);
                                    }

                                    RoundedUtil.drawRound((float)(x + 2), y, 121.0F, 35.0F, 3.0F, new Color(36, 36, 36, alpha));
                                    Fonts.RUB16.drawString(b.name, (float)(x + 40), y + 14.0F, (new Color(255, 255, 255)).getRGB());
                                    RenderUtil.drawBlurredShadow((float)(x + 40), y + 25.0F, 20.0F, 5.0F, 10, new Color(b.get()));
                                    RenderUtil.drawRectWH((float)(x + 40), y + 25.0F, 20.0F, 5.0F, b.get());
                                    RenderUtil.drawColoredCircle((double)(x + 20), (double)y + 17.5D, 15.0D, 1.0F);
                                    drawRoundCircle((float)(x + 20), y + 17.5F, 32.0F, new Color(36, 36, 36, alpha));
                                    RoundedUtil.drawRoundCircle((float)x + (float)poX + 20.5F, y + (float)poY + 18.0F, 3.0F, new Color(0, 0, 0, 255));
                                    RoundedUtil.drawRoundCircle((float)x + (float)poX + 20.5F, y + (float)poY + 18.0F, 2.0F, new Color(b.get()));
                                    y += 20.0F;
                                 }
                              }
                           }
                        }
                        continue label477;
                     }

                     s = (Setting)var17.next();
                  } while(!m.opened);

                  if (!(s instanceof ModeSetting)) {
                     break;
                  }

                  b = (ModeSetting)s;
                  if (!(Boolean)b.hidden.get()) {
                     heightBoost += (float)(b.opened ? b.modes.size() * 10 + 5 : 0);
                     break;
                  }
               }

               if (!(s instanceof ListSetting)) {
                  break;
               }

               b = (ListSetting)s;
               if (!(Boolean)b.hidden.get()) {
                  heightBoost += (float)(b.opened ? b.list.size() * 10 + 5 : 0);
                  break;
               }
            }

            if (s instanceof ColorSetting && !(Boolean)s.hidden.get()) {
               heightBoost += 20.0F;
            }
         }
      }

      int x1 = (int)((double)(this.x + 165) + this.categoryAnimation.getOutput() * 50.0D);
      float y1 = (float)((double)(this.y + 10) + this.scrollXA);

      label391:
      for(Iterator var38 = modules2.iterator(); var38.hasNext(); y1 += 25.0F) {
         Module m1 = (Module)var38.next();
         float heightBoost1 = 0.0F;
         Iterator var42 = m1.getSettingsForGUI().iterator();

         while(true) {
            Setting s;
            ColorSetting b;
            while(true) {
               ListSetting b;
               while(true) {
                  ModeSetting b;
                  do {
                     if (!var42.hasNext()) {
                        RoundedUtil.drawRound((float)x1, y1, 125.0F, (float)(20 + (m1.opened ? m1.getSettingsForGUI().size() * 20 : 0)) + heightBoost1, 3.0F, m1.state ? new Color(30, 30, 30, alpha) : new Color(27, 27, 27, alpha));
                        Fonts.REG16.drawString(m1.name, (float)(x1 + 5), y1 + 7.0F, m1.state ? ClickGui.getColor() : (new Color(255, 255, 255, alpha)).getRGB());
                        if (this.binding && this.bindingModule == m1) {
                           Fonts.REG16.drawString("[binding..]", (float)(x1 + 120 - Fonts.REG16.getStringWidth("[binding..]")), y1 + 7.0F, (new Color(255, 255, 255, alpha)).getRGB());
                        } else if (m1.bind > 1) {
                           Fonts.REG16.drawString("[" + Keyboard.getKeyName(m1.bind) + "]", (float)(x1 + 120 - Fonts.REG16.getStringWidth("[" + Keyboard.getKeyName(m1.bind) + "]")), y1 + 7.0F, (new Color(255, 255, 255, alpha)).getRGB());
                        }

                        if (m1.opened) {
                           var42 = m1.getSettingsForGUI().iterator();

                           while(true) {
                              while(true) {
                                 while(true) {
                                    while(true) {
                                       while(true) {
                                          if (!var42.hasNext()) {
                                             continue label391;
                                          }

                                          s = (Setting)var42.next();
                                          if (!(s instanceof ListSetting)) {
                                             break;
                                          }

                                          b = (ListSetting)s;
                                          if (!(Boolean)b.hidden.get()) {
                                             y1 += 20.0F;
                                             String finalOut = "";
                                             RoundedUtil.drawRound((float)(x1 + 2), y1 + 5.0F, 121.0F, 10.0F, 3.0F, new Color(36, 36, 36, alpha));
                                             Fonts.REG16.drawString(b.name, (float)(x1 + 3), y1 - 3.0F, (new Color(73, 73, 73, alpha)).getRGB());
                                             Fonts.REG16.drawCenteredString("selected " + b.selected.size() + "/" + b.list.size(), (float)(x1 + 60), y1 + 7.0F, (new Color(255, 255, 255, alpha)).getRGB());
                                             if (!b.opened) {
                                                break;
                                             }

                                             RoundedUtil.drawRound((float)(x1 + 2), y1 + 20.0F, 121.0F, (float)(b.opened ? b.list.size() * 10 : 0), 3.0F, new Color(36, 36, 36, alpha));

                                             for(int i = 0; i < b.list.size(); ++i) {
                                                Fonts.REG16.drawCenteredString((String)b.list.get(i), (float)(x1 + 60), y1 + 22.0F + (float)(i * 10), b.selected.contains(b.list.get(i)) ? ClickGui.getColor() : -1);
                                                if (b.selected.contains(b.list.get(i))) {
                                                   finalOut = finalOut + ((String)b.list.get(i)).substring(0, 2) + " ";
                                                }
                                             }

                                             y1 += (float)(b.list.size() * 10 + 5);
                                             break;
                                          }
                                       }

                                       if (!(s instanceof BooleanSetting)) {
                                          break;
                                       }

                                       BooleanSetting b = (BooleanSetting)s;
                                       if (!(Boolean)b.hidden.get()) {
                                          y1 += 20.0F;
                                          b.animation.setDirection(b.get() ? Direction.FORWARDS : Direction.BACKWARDS);
                                          RoundedUtil.drawRound((float)(x1 + 2), y1, 121.0F, 15.0F, 3.0F, new Color(36, 36, 36, alpha));
                                          RoundedUtil.drawRound((float)(x1 + 100), y1 + 5.0F, 10.0F, 5.0F, 2.0F, new Color(45, 45, 45, alpha));
                                          RenderUtil.drawBlurredShadow((float)((double)(x1 + 100) + b.animation.getOutput() * 5.0D), y1 + 5.0F, 5.0F, 5.0F, 15, b.get() ? new Color(ClickGui.getColor()) : new Color(89, 89, 89, alpha));
                                          RoundedUtil.drawRound((float)((double)(x1 + 100) + b.animation.getOutput() * 5.0D), y1 + 5.0F, 5.0F, 5.0F, 2.0F, b.get() ? new Color(ClickGui.getColor()) : new Color(89, 89, 89, alpha));
                                          Fonts.REG16.drawString(b.name.toLowerCase(), (float)(x1 + 5), y1 + 4.0F, (new Color(100, 100, 100, alpha)).getRGB());
                                          break;
                                       }
                                    }

                                    if (!(s instanceof SliderSetting)) {
                                       break;
                                    }

                                    SliderSetting b = (SliderSetting)s;
                                    if (!(Boolean)b.hidden.get()) {
                                       y1 += 20.0F;
                                       RoundedUtil.drawRound((float)(x1 + 2), y1, 121.0F, 15.0F, 3.0F, new Color(36, 36, 36, alpha));
                                       if (b.sliding) {
                                          b.current = (float)MathHelper.round((double)net.minecraft.util.math.MathHelper.clamp((float)((double)(mouseX - x1 - 120) * (double)(b.maximum - b.minimum) / 115.0D + (double)b.maximum), b.minimum, b.maximum), (double)b.increment);
                                       }

                                       b.sliderWidth = MathHelper.interpolate((b.current - b.minimum) / (b.maximum - b.minimum) * 115.0F, b.sliderWidth, 0.3D);
                                       float amountWidth = (b.current - b.minimum) / (b.maximum - b.minimum);
                                       RoundedUtil.drawRound((float)(x1 + 5), y1 + 9.0F, 115.0F, 2.0F, 1.0F, new Color(56, 56, 56, alpha));
                                       RenderUtil.drawBlurredShadow((float)(x1 + 5), y1 + 9.0F, b.sliderWidth, 2.0F, 15, new Color(ClickGui.getColor()));
                                       RoundedUtil.drawRound((float)(x1 + 5), y1 + 9.0F, b.sliderWidth, 2.0F, 1.0F, new Color(ClickGui.getColor()));
                                       RenderUtil.drawBlurredShadow((float)(x1 + 3) + b.sliderWidth, y1 + 8.0F, 4.0F, 4.0F, 15, new Color(ClickGui.getColor()));
                                       RoundedUtil.drawRound((float)(x1 + 3) + b.sliderWidth, y1 + 8.0F, 4.0F, 4.0F, 1.5F, new Color(ClickGui.getColor()));
                                       Fonts.REG12.drawString(String.valueOf(b.current), (float)(x1 + 120 - Fonts.REG16.getStringWidth(String.valueOf(b.current))), y1 + 2.0F, (new Color(100, 100, 100, alpha)).getRGB());
                                       Fonts.REG12.drawString(b.name, (float)(x1 + 5), y1 + 2.0F, (new Color(100, 100, 100, alpha)).getRGB());
                                       break;
                                    }
                                 }

                                 if (!(s instanceof ModeSetting)) {
                                    break;
                                 }

                                 b = (ModeSetting)s;
                                 if (!(Boolean)b.hidden.get()) {
                                    y1 += 20.0F;
                                    RoundedUtil.drawRound((float)(x1 + 2), y1 + 5.0F, 121.0F, 10.0F, 3.0F, new Color(36, 36, 36, alpha));
                                    Fonts.REG16.drawString(b.name, (float)(x1 + 3), y1 - 3.0F, (new Color(73, 73, 73, alpha)).getRGB());
                                    Fonts.REG16.drawCenteredString(b.currentMode, (float)(x1 + 60), y1 + 7.0F, (new Color(255, 255, 255, alpha)).getRGB());
                                    if (!b.opened) {
                                       break;
                                    }

                                    RoundedUtil.drawRound((float)(x1 + 2), y1 + 20.0F, 121.0F, (float)(b.opened ? b.modes.size() * 10 : 0), 3.0F, new Color(36, 36, 36, alpha));

                                    for(int i = 0; i < b.modes.size(); ++i) {
                                       Fonts.REG16.drawCenteredString((String)b.modes.get(i), (float)(x1 + 60), y1 + 22.0F + (float)(i * 10), b.currentMode.equalsIgnoreCase((String)b.modes.get(i)) ? ClickGui.getColor() : (new Color(255, 255, 255, alpha)).getRGB());
                                    }

                                    y1 += (float)(b.modes.size() * 10 + 5);
                                    break;
                                 }
                              }

                              if (s instanceof ColorSetting) {
                                 b = (ColorSetting)s;
                                 if (!(Boolean)b.hidden.get()) {
                                    y1 += 20.0F;
                                    double soX = (double)(mouseX - x1);
                                    double soY = (double)((float)mouseY - y1);
                                    soX -= 20.0D;
                                    soY -= 17.5D;
                                    double dst = Math.sqrt(soX * soX + soY * soY);
                                    float[] hsb = Color.RGBtoHSB((new Color(b.get())).getRed(), (new Color(b.get())).getGreen(), (new Color(b.get())).getBlue(), (float[])null);
                                    double poX = (double)(hsb[1] * 15.0F) * (Math.sin(Math.toRadians((double)(hsb[0] * 360.0F))) / Math.sin(Math.toRadians(90.0D)));
                                    double poY = (double)(hsb[1] * 15.0F) * (Math.sin(Math.toRadians((double)(90.0F - hsb[0] * 360.0F))) / Math.sin(Math.toRadians(90.0D)));
                                    if (dst > 15.0D) {
                                       b.slid = false;
                                    }

                                    if (b.slid && this.getColor() != (new Color(36, 36, 36)).getRGB()) {
                                       b.color = Color.HSBtoRGB((float)(Math.atan2(soX, soY) / 6.283185307179586D - 1.0D), (float)(dst / 15.0D), 1.0F);
                                    }

                                    RoundedUtil.drawRound((float)(x1 + 2), y1, 121.0F, 35.0F, 3.0F, new Color(36, 36, 36, alpha));
                                    RenderUtil.drawBlurredShadow((float)(x1 + 40), y1 + 25.0F, 20.0F, 5.0F, 10, new Color(b.get()));
                                    RenderUtil.drawRectWH((float)(x1 + 40), y1 + 25.0F, 20.0F, 5.0F, b.get());
                                    Fonts.RUB16.drawString(b.name, (float)(x1 + 40), y1 + 14.0F, (new Color(255, 255, 255, alpha)).getRGB());
                                    RenderUtil.drawColoredCircle((double)(x1 + 20), (double)y1 + 17.5D, 15.0D, 1.0F);
                                    drawRoundCircle((float)(x1 + 20), y1 + 17.5F, 32.0F, new Color(36, 36, 36, alpha));
                                    RoundedUtil.drawRoundCircle((float)x1 + (float)poX + 20.5F, y1 + (float)poY + 18.0F, 3.0F, new Color(0, 0, 0, 255));
                                    RoundedUtil.drawRoundCircle((float)x1 + (float)poX + 20.5F, y1 + (float)poY + 18.0F, 2.0F, new Color(b.get()));
                                    y1 += 20.0F;
                                 }
                              }
                           }
                        }
                        continue label391;
                     }

                     s = (Setting)var42.next();
                  } while(!m1.opened);

                  if (!(s instanceof ModeSetting)) {
                     break;
                  }

                  b = (ModeSetting)s;
                  if (!(Boolean)b.hidden.get()) {
                     heightBoost1 += (float)(b.opened ? b.modes.size() * 10 + 5 : 0);
                     break;
                  }
               }

               if (!(s instanceof ListSetting)) {
                  break;
               }

               b = (ListSetting)s;
               if (!(Boolean)b.hidden.get()) {
                  heightBoost1 += (float)(b.opened ? b.list.size() * 10 + 5 : 0);
                  break;
               }
            }

            if (s instanceof ColorSetting) {
               b = (ColorSetting)s;
               if (!(Boolean)b.hidden.get()) {
                  heightBoost1 += 20.0F;
               }
            }
         }
      }

      this.scrollX = (double)MathHelper.clamp((float)this.scrollX, (float)((double)(-Math.max(y - (float)this.y, y1 - (float)this.y) - Math.max(y - (float)this.y, y1 - (float)this.y)) + this.scrollX) + 170.0F, 0.0F);
      RenderUtil.drawRectWH((float)(this.x - 2), (float)(this.y - 2), (float)(25.0D + this.categoryAnimation.getOutput() * 50.0D), 204.0F, (new Color(25, 25, 25, alpha)).getRGB());
      StencilUtil.initStencilToWrite();
      RenderUtil.drawRectWH((float)(this.x - 2), (float)this.y, (float)(25.0D + this.categoryAnimation.getOutput() * 50.0D), 200.0F, (new Color(25, 25, 25, alpha)).getRGB());
      StencilUtil.readStencilBuffer(1);
      int i = 5;
      Category[] var35 = this.types;
      int var36 = var35.length;

      for(x = 0; x < var36; ++x) {
         Category type = var35[x];
         RenderUtil.drawRectWH((float)(this.x - 1), (float)(this.y + i + 6), (float)(24.0D + this.categoryAnimation.getOutput() * 50.0D), 20.0F, this.selectedType == type ? (new Color(30, 30, 30, alpha)).getRGB() : (new Color(25, 25, 25, alpha)).getRGB());
         if (this.categoryAnimation.getOutput() > 0.05D) {
            Fonts.REG16.drawString(type.name(), (float)(this.x + 20), (float)(this.y + 14 + i), (new Color(255, 255, 255, (int)net.minecraft.util.math.MathHelper.clamp(this.categoryAnimation.getOutput() * 255.0D, 30.0D, 255.0D))).getRGB());
         }

         RenderUtil.drawTexture(new ResourceLocation("icons/" + type.name().toLowerCase() + ".png"), (double)((float)(this.x + 5)), (double)((float)(this.y + 10) + (float)i), 12.0D, 12.0D, new Color(255, 255, 255, alpha));
         i += 20;
      }

      StencilUtil.uninitStencilBuffer();
      StencilUtil.uninitStencilBuffer();
      this.prevMouseX = mouseX;
      this.prevMouseY = mouseY;
      this.scrollXA = MathHelper.interpolate(this.scrollX, this.scrollXA, 0.1D);
      this.scrollX += (double)((float)Mouse.getDWheel() / 120.0F * 30.0F);
      ScaleUtil.scale_post();
   }

   public static void drawRoundCircle(float x, float y, float radius, Color color) {
      RoundedUtil.drawRoundOutline(x - radius / 2.0F, y - radius / 2.0F, radius, radius, radius / 2.0F - 0.5F, 0.1F, new Color(0, 0, 0, 0), color);
   }

   public int getColor() {
      ByteBuffer rgb = BufferUtils.createByteBuffer(100);
      GL11.glReadPixels(Mouse.getX(), Mouse.getY(), 1, 1, 6407, 5121, rgb);
      return (new Color(rgb.get(0) & 255, rgb.get(1) & 255, rgb.get(2) & 255)).getRGB();
   }

   public void dragging(int mouseX, int mouseY) {
      if (this.drag) {
         this.x = (int)((double)mouseX + this.prevX);
         this.y = (int)((double)mouseY + this.prevY);
      }

   }

   public void onGuiClosed() {
      super.onGuiClosed();
      this.drag = false;
      Iterator var1 = Main.m.m.iterator();

      while(var1.hasNext()) {
         Module s = (Module)var1.next();
         Iterator var3 = s.getSettingsForGUI().iterator();

         while(var3.hasNext()) {
            Setting setting = (Setting)var3.next();
            if (setting instanceof SliderSetting) {
               SliderSetting sliderSetting = (SliderSetting)setting;
               sliderSetting.sliding = false;
            }

            if (setting instanceof ColorSetting) {
               ColorSetting b = (ColorSetting)setting;
               b.slid = false;
            }
         }
      }

   }

   protected void mouseReleased(int mouseX1, int mouseY1, int state) {
      super.mouseReleased(mouseX1, mouseY1, state);
      int mouseX = (int)ScaleUtil.calc((float)mouseX1, (float)mouseY1)[0];
      int mouseY = (int)ScaleUtil.calc((float)mouseX1, (float)mouseY1)[1];
      this.drag = false;
      Iterator var6 = Main.m.m.iterator();

      while(var6.hasNext()) {
         Module s = (Module)var6.next();
         Iterator var8 = s.getSettingsForGUI().iterator();

         while(var8.hasNext()) {
            Setting setting = (Setting)var8.next();
            if (setting instanceof SliderSetting) {
               SliderSetting sliderSetting = (SliderSetting)setting;
               sliderSetting.sliding = false;
            }

            if (setting instanceof ColorSetting) {
               ColorSetting b = (ColorSetting)setting;
               b.slid = false;
            }
         }
      }

   }

   protected void mouseClicked(int mouseX1, int mouseY1, int mouseButton) throws IOException {
      super.mouseClicked(mouseX1, mouseY1, mouseButton);
      int mouseX = (int)ScaleUtil.calc((float)mouseX1, (float)mouseY1)[0];
      int mouseY = (int)ScaleUtil.calc((float)mouseX1, (float)mouseY1)[1];
      if (RenderUtil.isHovered((float)mouseX, (float)mouseY, (float)this.x, (float)this.y, 300.0F, 5.0F)) {
         this.drag = true;
         this.prevX = (double)(this.x - mouseX);
         this.prevY = (double)(this.y - mouseY);
      }

      int i = 5;
      Category[] var7 = this.types;
      int var8 = var7.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         Category type = var7[var9];
         if (RenderUtil.isHovered((float)mouseX, (float)mouseY, (float)(this.x - 1), (float)(this.y + i + 6), (float)(25.0D + this.categoryAnimation.getOutput() * 50.0D), 20.0F)) {
            this.preSelected = type;
            this.animating = true;
         }

         i += 20;
      }

      if (!this.categoryOpen) {
         List<Module> list = Main.m.getModulesFromCategory(this.selectedType);
         List<Module> modules = (List)Main.m.getModulesFromCategory(this.selectedType).stream().filter((f1) -> {
            return list.indexOf(f1) % 2 == 0;
         }).collect(Collectors.toList());
         List<Module> modules2 = (List)Main.m.getModulesFromCategory(this.selectedType).stream().filter((f1) -> {
            return list.indexOf(f1) % 2 != 0;
         }).collect(Collectors.toList());
         int x = this.x + 35;
         float y = (float)((double)(this.y + 10) + this.scrollXA);

         Iterator var14;
         double soX;
         double soY;
         label316:
         for(Iterator var12 = modules.iterator(); var12.hasNext(); y += 25.0F) {
            Module m = (Module)var12.next();
            if (RenderUtil.isHovered((float)mouseX, (float)mouseY, (float)x, y, 125.0F, 20.0F)) {
               if (mouseButton == 0) {
                  m.toggle();
               }

               if (mouseButton == 2) {
                  this.bindingModule = m;
                  this.binding = true;
               }

               if (mouseButton == 1) {
                  m.opened = !m.opened;
               }
            }

            if (m.opened) {
               var14 = m.getSettingsForGUI().iterator();

               while(true) {
                  Setting s;
                  while(true) {
                     int i1;
                     while(true) {
                        while(true) {
                           if (!var14.hasNext()) {
                              continue label316;
                           }

                           s = (Setting)var14.next();
                           if (s instanceof ListSetting) {
                              ListSetting b = (ListSetting)s;
                              y += 20.0F;
                              if (RenderUtil.isHovered((float)mouseX, (float)mouseY, (float)x, y + 5.0F, 125.0F, 10.0F)) {
                                 b.opened = !b.opened;
                              }

                              if (b.opened) {
                                 RenderUtil.drawRectWH((float)x, y + 14.0F, 125.0F, 0.5F, (new Color(73, 73, 73, 255)).getRGB());

                                 for(i1 = 0; i1 < b.list.size(); ++i1) {
                                    if (RenderUtil.isHovered((float)mouseX, (float)mouseY, (float)x, y + 22.0F + (float)(i1 * 10), 125.0F, 9.0F)) {
                                       if (b.selected.contains(b.list.get(i1))) {
                                          b.selected.remove(b.list.get(i1));
                                       } else {
                                          b.selected.add(b.list.get(i1));
                                       }
                                    }
                                 }

                                 y += (float)(b.list.size() * 10 + 5);
                              }
                           }

                           if (!(s instanceof SliderSetting)) {
                              break;
                           }

                           SliderSetting sl = (SliderSetting)s;
                           if (!(Boolean)sl.hidden.get()) {
                              y += 20.0F;
                              if (RenderUtil.isHovered((float)mouseX, (float)mouseY, (float)x, y, 125.0F, 15.0F)) {
                                 sl.sliding = true;
                              }
                              break;
                           }
                        }

                        if (!(s instanceof BooleanSetting)) {
                           break;
                        }

                        BooleanSetting b = (BooleanSetting)s;
                        if (!(Boolean)b.hidden.get()) {
                           y += 20.0F;
                           if (RenderUtil.isHovered((float)mouseX, (float)mouseY, (float)x, y, 125.0F, 15.0F)) {
                              b.set(!b.get());
                           }
                           break;
                        }
                     }

                     if (!(s instanceof ModeSetting)) {
                        break;
                     }

                     ModeSetting b = (ModeSetting)s;
                     if (!(Boolean)b.hidden.get()) {
                        y += 20.0F;
                        if (RenderUtil.isHovered((float)mouseX, (float)mouseY, (float)x, y + 5.0F, 125.0F, 10.0F)) {
                           b.opened = !b.opened;
                        }

                        if (!b.opened) {
                           break;
                        }

                        RenderUtil.drawRectWH((float)x, y + 14.0F, 125.0F, 0.5F, (new Color(73, 73, 73, 255)).getRGB());

                        for(i1 = 0; i1 < b.modes.size(); ++i1) {
                           if (RenderUtil.isHovered((float)mouseX, (float)mouseY, (float)x, y + 22.0F + (float)(i1 * 10), 125.0F, 9.0F)) {
                              b.currentMode = (String)b.modes.get(i1);
                           }
                        }

                        y += (float)(b.modes.size() * 10 + 5);
                        break;
                     }
                  }

                  if (s instanceof ColorSetting) {
                     ColorSetting b = (ColorSetting)s;
                     if (!(Boolean)b.hidden.get()) {
                        y += 20.0F;
                        double soX = (double)(mouseX - x);
                        soX = (double)((float)mouseY - y);
                        soX -= 20.0D;
                        soX -= 17.5D;
                        soY = Math.sqrt(soX * soX + soX * soX);
                        if (soY <= 15.0D) {
                           b.slid = true;
                        }

                        y += 20.0F;
                     }
                  }
               }
            }
         }

         int x1 = this.x + 165;
         float y1 = (float)((double)(this.y + 10) + this.scrollXA);

         label260:
         for(var14 = modules2.iterator(); var14.hasNext(); y1 += 25.0F) {
            Module m = (Module)var14.next();
            if (RenderUtil.isHovered((float)mouseX, (float)mouseY, (float)x1, y1, 125.0F, 20.0F)) {
               if (mouseButton == 0) {
                  m.toggle();
               }

               if (mouseButton == 2) {
                  this.bindingModule = m;
                  this.binding = true;
               }

               if (mouseButton == 1) {
                  m.opened = !m.opened;
               }
            }

            if (m.opened) {
               Iterator var36 = m.getSettingsForGUI().iterator();

               while(true) {
                  Setting s;
                  while(true) {
                     int i1;
                     while(true) {
                        while(true) {
                           if (!var36.hasNext()) {
                              continue label260;
                           }

                           s = (Setting)var36.next();
                           if (s instanceof ListSetting) {
                              ListSetting b = (ListSetting)s;
                              y1 += 20.0F;
                              if (RenderUtil.isHovered((float)mouseX, (float)mouseY, (float)x1, y1 + 5.0F, 125.0F, 10.0F)) {
                                 b.opened = !b.opened;
                              }

                              if (b.opened) {
                                 RenderUtil.drawRectWH((float)x1, y1 + 14.0F, 125.0F, 0.5F, (new Color(73, 73, 73, 255)).getRGB());

                                 for(i1 = 0; i1 < b.list.size(); ++i1) {
                                    if (RenderUtil.isHovered((float)mouseX, (float)mouseY, (float)x1, y1 + 22.0F + (float)(i1 * 10), 125.0F, 9.0F)) {
                                       if (b.selected.contains(b.list.get(i1))) {
                                          b.selected.remove(b.list.get(i1));
                                       } else {
                                          b.selected.add(b.list.get(i1));
                                       }
                                    }
                                 }

                                 y1 += (float)(b.list.size() * 10 + 5);
                              }
                           }

                           if (!(s instanceof SliderSetting)) {
                              break;
                           }

                           SliderSetting sl = (SliderSetting)s;
                           if (!(Boolean)sl.hidden.get()) {
                              y1 += 20.0F;
                              if (RenderUtil.isHovered((float)mouseX, (float)mouseY, (float)x1, y1, 125.0F, 15.0F)) {
                                 sl.sliding = true;
                              }
                              break;
                           }
                        }

                        if (!(s instanceof BooleanSetting)) {
                           break;
                        }

                        BooleanSetting b = (BooleanSetting)s;
                        if (!(Boolean)b.hidden.get()) {
                           y1 += 20.0F;
                           if (RenderUtil.isHovered((float)mouseX, (float)mouseY, (float)x1, y1, 125.0F, 15.0F)) {
                              b.set(!b.get());
                           }
                           break;
                        }
                     }

                     if (!(s instanceof ModeSetting)) {
                        break;
                     }

                     ModeSetting b = (ModeSetting)s;
                     if (!(Boolean)b.hidden.get()) {
                        y1 += 20.0F;
                        if (RenderUtil.isHovered((float)mouseX, (float)mouseY, (float)x1, y1 + 5.0F, 125.0F, 10.0F)) {
                           b.opened = !b.opened;
                        }

                        if (!b.opened) {
                           break;
                        }

                        RenderUtil.drawRectWH((float)x1, y1 + 14.0F, 125.0F, 0.5F, (new Color(73, 73, 73, 255)).getRGB());

                        for(i1 = 0; i1 < b.modes.size(); ++i1) {
                           if (RenderUtil.isHovered((float)mouseX, (float)mouseY, (float)x1, y1 + 22.0F + (float)(i1 * 10), 125.0F, 9.0F)) {
                              b.currentMode = (String)b.modes.get(i1);
                           }
                        }

                        y1 += (float)(b.modes.size() * 10 + 5);
                        break;
                     }
                  }

                  if (s instanceof ColorSetting) {
                     ColorSetting b = (ColorSetting)s;
                     if (!(Boolean)b.hidden.get()) {
                        y1 += 20.0F;
                        soX = (double)(mouseX - x1);
                        soY = (double)((float)mouseY - y1);
                        soX -= 20.0D;
                        soY -= 17.5D;
                        double dst = Math.sqrt(soX * soX + soY * soY);
                        if (dst <= 15.0D) {
                           b.slid = true;
                        }

                        y1 += 20.0F;
                     }
                  }
               }
            }
         }

      }
   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
      if (this.binding) {
         if (keyCode == 1) {
            this.bindingModule.bind = 0;
            this.binding = false;
            this.bindingModule = null;
            return;
         }

         this.bindingModule.bind = keyCode;
         this.binding = false;
         this.bindingModule = null;
      }

      super.keyTyped(typedChar, keyCode);
   }
}
