/* Decompiler 17ms, total 424ms, lines 107 */
package wtf.evolution.module.impl.Render;

import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import wtf.evolution.Main;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventRender;
import wtf.evolution.helpers.render.AntiAliasing;
import wtf.evolution.helpers.render.ColorUtil;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.BooleanSetting;
import wtf.evolution.settings.options.ColorSetting;
import wtf.evolution.settings.options.ModeSetting;
import wtf.evolution.settings.options.SliderSetting;

@ModuleInfo(
   name = "ChinaHat",
   type = Category.Render
)
public class ChinaHat extends Module {
   public BooleanSetting outline = (new BooleanSetting("Outline", true)).call(this);
   public SliderSetting lineWidth = (new SliderSetting("Width", 1.0F, 0.0F, 5.0F, 0.1F)).setHidden(() -> {
      return !this.outline.get();
   }).call(this);
   public ModeSetting mode = (new ModeSetting("Mode", "Static", new String[]{"Static", "Fade", "Astolfo", "Rainbow"})).call(this);
   public ColorSetting color = (new ColorSetting("Color", -1)).setHidden(() -> {
      return !this.mode.is("Static") && !this.mode.is("Fade");
   }).call(this);

   @EventTarget
   public void onRender(EventRender e) {
      if (this.mc.gameSettings.thirdPersonView != 0) {
         double ix = -(this.mc.player.lastTickPosX + (this.mc.player.posX - this.mc.player.lastTickPosX) * (double)e.pt);
         double iy = -(this.mc.player.lastTickPosY + (this.mc.player.posY - this.mc.player.lastTickPosY) * (double)e.pt);
         double iz = -(this.mc.player.lastTickPosZ + (this.mc.player.posZ - this.mc.player.lastTickPosZ) * (double)e.pt);
         float x = (float)(this.mc.player.lastTickPosX + (this.mc.player.posX - this.mc.player.lastTickPosX) * (double)e.pt);
         float y = (float)(this.mc.player.lastTickPosY + (this.mc.player.posY - this.mc.player.lastTickPosY) * (double)e.pt) + this.mc.player.height - (this.mc.player.isSneaking() ? (Main.m.getModule(CustomModel.class).state ? 0.02F : 0.2F) : 0.0F) + (Main.m.getModule(CustomModel.class).state ? 0.3F : 0.0F);
         float z = (float)(this.mc.player.lastTickPosZ + (this.mc.player.posZ - this.mc.player.lastTickPosZ) * (double)e.pt);
         GlStateManager.pushMatrix();
         GL11.glDepthMask(false);
         GlStateManager.enableDepth();
         GL11.glRotatef(-this.mc.player.rotationYaw, 0.0F, 1.0F, 0.0F);
         GlStateManager.translate(ix, iy, iz);
         GlStateManager.enableBlend();
         GL11.glBlendFunc(770, 771);
         GlStateManager.disableTexture2D();
         GL11.glDisable(2884);
         GL11.glShadeModel(7425);
         GL11.glDisable(3008);
         AntiAliasing.hook(true, false, false);
         GlStateManager.alphaFunc(516, 0.0F);
         GL11.glBegin(6);
         Color c1 = this.getColor(1);
         GL11.glColor4f((float)c1.getRed() / 255.0F, (float)c1.getGreen() / 255.0F, (float)c1.getBlue() / 255.0F, 0.39215687F);
         GL11.glVertex3f(x, y + 0.23F, z);

         for(int i = 0; i <= 360; ++i) {
            double x1 = Math.cos((double)i * 3.141592653589793D / 180.0D) * 0.55D;
            double z1 = Math.sin((double)i * 3.141592653589793D / 180.0D) * 0.55D;
            Color c = this.getColor(i);
            GL11.glColor4f((float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F, 0.39215687F);
            GL11.glVertex3d((double)x + x1, (double)y, (double)z + z1);
         }

         GL11.glEnd();
         if (this.outline.get()) {
            GL11.glLineWidth(this.lineWidth.get());
            GL11.glBegin(2);

            for(int i = 0; i <= 360; ++i) {
               double x1 = Math.cos((double)i * 3.141592653589793D / 180.0D) * 0.55D;
               double z1 = Math.sin((double)i * 3.141592653589793D / 180.0D) * 0.55D;
               Color c = this.getColor(i);
               GL11.glColor4f((float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F, 1.0F);
               GL11.glVertex3d((double)x + x1, (double)y, (double)z + z1);
            }

            GL11.glEnd();
         }

         AntiAliasing.unhook(true, false, false);
         GL11.glEnable(3008);
         GlStateManager.enableTexture2D();
         GlStateManager.disableBlend();
         GL11.glEnable(2884);
         GlStateManager.resetColor();
         GL11.glDepthMask(true);
         GlStateManager.popMatrix();
      }
   }

   public Color getColor(int index) {
      if (this.mode.is("Static")) {
         return new Color(this.color.get());
      } else if (this.mode.is("Fade")) {
         return ColorUtil.fade(10, index * 2, new Color(this.color.get()), 0.5F);
      } else if (this.mode.is("Astolfo")) {
         return FeatureList.astolfo(1.0F, (float)index / 2.0F, 0.5F, 10.0F);
      } else {
         return this.mode.is("Rainbow") ? ColorUtil.rainbow(10, index, 0.5F, 1.0F, 0.5F) : Color.WHITE;
      }
   }
}
