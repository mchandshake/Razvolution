/* Decompiler 20ms, total 379ms, lines 124 */
package wtf.evolution.module.impl.Render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventRender;
import wtf.evolution.event.events.impl.EventUpdate;
import wtf.evolution.event.events.impl.JumpEvent;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.ColorSetting;
import wtf.evolution.settings.options.ModeSetting;
import wtf.evolution.settings.options.SliderSetting;

@ModuleInfo(
   name = "JumpCircle",
   type = Category.Render
)
public class JumpCircle extends Module {
   public ArrayList<wtf.evolution.module.impl.Render.JumpCircle.Circle> circles = new ArrayList();
   public ModeSetting mode = (new ModeSetting("Mode", "Akrien", new String[]{"Celestial", "Akrien"})).call(this);
   public SliderSetting speed = (new SliderSetting("Speed", 1.0F, 0.0F, 10.0F, 1.0F)).call(this);
   public ColorSetting rgb = (new ColorSetting("Color", -1)).call(this);

   @EventTarget
   public void jump(JumpEvent e) {
      this.circles.add(new wtf.evolution.module.impl.Render.JumpCircle.Circle(this, this.mc.player.posX, this.mc.player.posY + 0.1D, this.mc.player.posZ));
   }

   @EventTarget
   public void render(EventUpdate e) {
      this.circles.removeIf((circle) -> {
         return circle.age > 1000.0D;
      });
   }

   @EventTarget
   public void onRender(EventRender e) {
      wtf.evolution.module.impl.Render.JumpCircle.Circle c;
      for(Iterator var2 = this.circles.iterator(); var2.hasNext(); c.age += (double)this.speed.get()) {
         c = (wtf.evolution.module.impl.Render.JumpCircle.Circle)var2.next();
         this.renderCircle(c, e.pt);
      }

   }

   public void renderCircle(wtf.evolution.module.impl.Render.JumpCircle.Circle c, float partialTicks) {
      if (c.age < 1000.0D) {
         double ix = -(this.mc.player.lastTickPosX + (this.mc.player.posX - this.mc.player.lastTickPosX) * (double)partialTicks);
         double iy = -(this.mc.player.lastTickPosY + (this.mc.player.posY - this.mc.player.lastTickPosY) * (double)partialTicks);
         double iz = -(this.mc.player.lastTickPosZ + (this.mc.player.posZ - this.mc.player.lastTickPosZ) * (double)partialTicks);
         GlStateManager.pushMatrix();
         GL11.glDepthMask(false);
         GlStateManager.enableDepth();
         GlStateManager.translate(ix, iy, iz);
         GlStateManager.enableBlend();
         GL11.glBlendFunc(770, 771);
         GlStateManager.disableTexture2D();
         GL11.glDisable(2884);
         GL11.glShadeModel(7425);
         GL11.glDisable(3008);
         GlStateManager.alphaFunc(516, 0.0F);
         int i;
         double x;
         double z;
         int color;
         if (this.mode.is("Celestial")) {
            GL11.glBegin(8);

            for(i = 0; i <= 360; ++i) {
               x = Math.cos((double)i * 3.141592653589793D / 180.0D);
               z = Math.sin((double)i * 3.141592653589793D / 180.0D);
               color = this.rgb.get();
               GL11.glColor4d((double)((float)(new Color(color)).getRed() / 255.0F), (double)((float)(new Color(color)).getGreen() / 255.0F), (double)((float)(new Color(color)).getBlue() / 255.0F), 0.0D);
               GL11.glVertex3d(c.x + x * c.age / 1000.0D * 1.5D, c.y, c.z + z * c.age / 1000.0D * 1.5D);
               GL11.glColor4d((double)((float)(new Color(color)).getRed() / 255.0F), (double)((float)(new Color(color)).getGreen() / 255.0F), (double)((float)(new Color(color)).getBlue() / 255.0F), 0.5D - c.age / 2000.0D);
               GL11.glVertex3d(c.x + x * (c.age / 1000.0D), c.y, c.z + z * (c.age / 1000.0D));
            }

            GL11.glEnd();
            GL11.glBegin(8);

            for(i = 0; i <= 360; ++i) {
               x = Math.cos((double)i * 3.141592653589793D / 180.0D);
               z = Math.sin((double)i * 3.141592653589793D / 180.0D);
               color = this.rgb.get();
               GL11.glColor4d((double)((float)(new Color(color)).getRed() / 255.0F), (double)((float)(new Color(color)).getGreen() / 255.0F), (double)((float)(new Color(color)).getBlue() / 255.0F), 0.9D - c.age / 1100.0D);
               GL11.glVertex3d(c.x + x * (c.age / 1000.0D), c.y, c.z + z * (c.age / 1000.0D));
               GL11.glColor4d((double)((float)(new Color(color)).getRed() / 255.0F), (double)((float)(new Color(color)).getGreen() / 255.0F), (double)((float)(new Color(color)).getBlue() / 255.0F), 0.0D);
               GL11.glVertex3d(c.x + x * c.age / 1000.0D * 0.5D, c.y, c.z + z * c.age / 1000.0D * 0.5D);
            }

            GL11.glEnd();
         } else {
            GL11.glBegin(8);

            for(i = 0; i <= 360; ++i) {
               x = Math.cos((double)i * 3.141592653589793D / 180.0D);
               z = Math.sin((double)i * 3.141592653589793D / 180.0D);
               color = this.rgb.get();
               GL11.glColor4d((double)((float)(new Color(color)).getRed() / 255.0F), (double)((float)(new Color(color)).getGreen() / 255.0F), (double)((float)(new Color(color)).getBlue() / 255.0F), 0.9D - c.age / 1100.0D);
               GL11.glVertex3d(c.x + x * (c.age / 1000.0D), c.y, c.z + z * (c.age / 1000.0D));
               GL11.glColor4d((double)((float)(new Color(color)).getRed() / 255.0F), (double)((float)(new Color(color)).getGreen() / 255.0F), (double)((float)(new Color(color)).getBlue() / 255.0F), 0.0D);
               GL11.glVertex3d(c.x + x * c.age / 1000.0D * 0.5D, c.y, c.z + z * c.age / 1000.0D * 0.5D);
            }

            GL11.glEnd();
         }

         GL11.glEnable(3008);
         GlStateManager.enableTexture2D();
         GlStateManager.disableBlend();
         GL11.glEnable(2884);
         GlStateManager.resetColor();
         GL11.glDepthMask(true);
         GlStateManager.popMatrix();
      }
   }
}
