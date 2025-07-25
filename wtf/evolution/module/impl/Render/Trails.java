/* Decompiler 24ms, total 397ms, lines 121 */
package wtf.evolution.module.impl.Render;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventRender;
import wtf.evolution.helpers.render.ColorUtil;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.Setting;
import wtf.evolution.settings.options.ColorSetting;
import wtf.evolution.settings.options.ModeSetting;
import wtf.evolution.settings.options.SliderSetting;

@ModuleInfo(
   name = "Trails",
   type = Category.Render
)
public class Trails extends Module {
   public ModeSetting mode = (new ModeSetting("Mode", "Static", new String[]{"Static", "Fade", "Astolfo", "Rainbow"})).call(this);
   public ColorSetting color = (new ColorSetting("Color", -1)).setHidden(() -> {
      return !this.mode.is("Static") && !this.mode.is("Fade");
   }).call(this);
   public SliderSetting time = new SliderSetting("Time", 500.0F, 0.0F, 1500.0F, 100.0F);
   public ArrayList<wtf.evolution.module.impl.Render.Trails.Point> p = new ArrayList();

   public Trails() {
      this.addSettings(new Setting[]{this.time});
   }

   @EventTarget
   public void onRender(EventRender e) {
      double ix = -(this.mc.player.lastTickPosX + (this.mc.player.posX - this.mc.player.lastTickPosX) * (double)e.pt);
      double iy = -(this.mc.player.lastTickPosY + (this.mc.player.posY - this.mc.player.lastTickPosY) * (double)e.pt);
      double iz = -(this.mc.player.lastTickPosZ + (this.mc.player.posZ - this.mc.player.lastTickPosZ) * (double)e.pt);
      float x = (float)(this.mc.player.lastTickPosX + (this.mc.player.posX - this.mc.player.lastTickPosX) * (double)e.pt);
      float y = (float)(this.mc.player.lastTickPosY + (this.mc.player.posY - this.mc.player.lastTickPosY) * (double)e.pt);
      float z = (float)(this.mc.player.lastTickPosZ + (this.mc.player.posZ - this.mc.player.lastTickPosZ) * (double)e.pt);
      this.p.add(new wtf.evolution.module.impl.Render.Trails.Point(this, new Vec3d((double)x, (double)y, (double)z)));
      this.p.removeIf((pointx) -> {
         return (float)pointx.time >= this.time.get();
      });
      GlStateManager.pushMatrix();
      GL11.glDepthMask(false);
      GlStateManager.translate(ix, iy, iz);
      GlStateManager.enableBlend();
      GL11.glBlendFunc(770, 771);
      GlStateManager.disableTexture2D();
      GL11.glDisable(2884);
      GL11.glShadeModel(7425);
      GL11.glDisable(3008);
      GlStateManager.alphaFunc(516, 0.0F);
      GL11.glBegin(8);
      Iterator var11 = this.p.iterator();

      wtf.evolution.module.impl.Render.Trails.Point point;
      float alpha;
      wtf.evolution.module.impl.Render.Trails.Point temp;
      int color;
      while(var11.hasNext()) {
         point = (wtf.evolution.module.impl.Render.Trails.Point)var11.next();
         if (this.p.indexOf(point) < this.p.size() - 1) {
            alpha = 100.0F * ((float)this.p.indexOf(point) / (float)this.p.size());
            temp = (wtf.evolution.module.impl.Render.Trails.Point)this.p.get(this.p.indexOf(point) + 1);
            color = RenderUtil.setAlpha(ColorUtil.applyColor(this.mode.get(), this.p.indexOf(point) * 4, this.color.get(), 255), (int)alpha);
            RenderUtil.color(color);
            GL11.glVertex3d(temp.pos.x, temp.pos.y, temp.pos.z);
            GL11.glVertex3d(temp.pos.x, temp.pos.y + (double)this.mc.player.height - 0.1D, temp.pos.z);
            ++point.time;
         }
      }

      GL11.glEnd();
      GL11.glLineWidth(2.0F);
      GL11.glBegin(3);
      var11 = this.p.iterator();

      while(var11.hasNext()) {
         point = (wtf.evolution.module.impl.Render.Trails.Point)var11.next();
         if (this.p.indexOf(point) < this.p.size() - 1) {
            alpha = 100.0F * ((float)this.p.indexOf(point) / (float)this.p.size());
            temp = (wtf.evolution.module.impl.Render.Trails.Point)this.p.get(this.p.indexOf(point) + 1);
            color = RenderUtil.setAlpha(ColorUtil.applyColor(this.mode.get(), this.p.indexOf(point) * 4, this.color.get(), 255).brighter(), (int)alpha);
            RenderUtil.color(color);
            GL11.glVertex3d(temp.pos.x, temp.pos.y, temp.pos.z);
            ++point.time;
         }
      }

      GL11.glEnd();
      GL11.glBegin(3);
      var11 = this.p.iterator();

      while(var11.hasNext()) {
         point = (wtf.evolution.module.impl.Render.Trails.Point)var11.next();
         if (this.p.indexOf(point) < this.p.size() - 1) {
            alpha = 100.0F * ((float)this.p.indexOf(point) / (float)this.p.size());
            temp = (wtf.evolution.module.impl.Render.Trails.Point)this.p.get(this.p.indexOf(point) + 1);
            color = RenderUtil.setAlpha(ColorUtil.applyColor(this.mode.get(), this.p.indexOf(point) * 4, this.color.get(), 255).brighter(), (int)alpha);
            RenderUtil.color(color);
            GL11.glVertex3d(temp.pos.x, temp.pos.y + (double)this.mc.player.height - 0.1D, temp.pos.z);
            ++point.time;
         }
      }

      GL11.glEnd();
      GL11.glEnable(3008);
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
      GL11.glEnable(2884);
      GlStateManager.resetColor();
      GL11.glDepthMask(true);
      GlStateManager.popMatrix();
   }
}
