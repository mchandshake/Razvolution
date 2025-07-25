/* Decompiler 9ms, total 361ms, lines 58 */
package wtf.evolution.module.impl.Render;

import java.awt.Color;
import java.util.Iterator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventDisplay;
import wtf.evolution.helpers.render.ColorUtil;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.ColorSetting;
import wtf.evolution.settings.options.ModeSetting;

@ModuleInfo(
   name = "TriangleESP",
   type = Category.Render
)
public class TriangleESP extends Module {
   public ModeSetting mode = (new ModeSetting("Mode", "Static", new String[]{"Static", "Fade", "Astolfo", "Rainbow"})).call(this);
   public ColorSetting color = (new ColorSetting("Color", -1)).setHidden(() -> {
      return !this.mode.is("Static") && !this.mode.is("Fade");
   }).call(this);

   @EventTarget
   public void onRender(EventDisplay event) {
      int i = 0;
      Iterator var3 = this.mc.world.loadedEntityList.iterator();

      while(var3.hasNext()) {
         Entity entity = (Entity)var3.next();
         i += 50;
         if (entity != this.mc.player && entity instanceof EntityPlayer) {
            double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)this.mc.timer.renderPartialTicks - this.mc.getRenderManager().renderPosX;
            double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)this.mc.timer.renderPartialTicks - this.mc.getRenderManager().renderPosZ;
            double cos = Math.cos((double)this.mc.player.rotationYaw * 0.017453292519943295D);
            double sin = Math.sin((double)this.mc.player.rotationYaw * 0.017453292519943295D);
            double rotY = -(z * cos - x * sin);
            double rotX = -(x * cos + z * sin);
            float angle = (float)(Math.atan2(rotY, rotX) * 180.0D / 3.141592653589793D);
            double xPos = 50.0D * Math.cos(Math.toRadians((double)angle)) + (double)((float)event.sr.getScaledWidth() / 2.0F);
            double y = 50.0D * Math.sin(Math.toRadians((double)angle)) + (double)((float)event.sr.getScaledHeight() / 2.0F);
            GlStateManager.pushMatrix();
            GlStateManager.translate(xPos, y, 0.0D);
            GlStateManager.rotate(angle + 180.0F + 90.0F, 0.0F, 0.0F, 1.0F);
            Color c = new Color(ColorUtil.applyColor(this.mode.get(), i, this.color.get(), 255).getRed(), ColorUtil.applyColor(this.mode.get(), i, this.color.get(), 255).getGreen(), ColorUtil.applyColor(this.mode.get(), i, this.color.get(), 255).getBlue(), Math.max((int)(255.0F - this.mc.player.getDistance(entity) * 5.0F), 15));
            RenderUtil.drawBlurredShadow(0.0F, -5.0F, 10.0F, 10.0F, 15, c);
            RenderUtil.drawTriangle(0.0F, 0.0F, 5.0F, 7.0F, c.getRGB(), c.darker().getRGB());
            GlStateManager.popMatrix();
         }
      }

   }
}
