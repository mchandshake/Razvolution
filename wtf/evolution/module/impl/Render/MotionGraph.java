/* Decompiler 9ms, total 354ms, lines 69 */
package wtf.evolution.module.impl.Render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import org.lwjgl.opengl.GL11;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventDisplay;
import wtf.evolution.event.events.impl.EventMotion;
import wtf.evolution.helpers.math.MathHelper;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;

@ModuleInfo(
   name = "MotionGraph",
   type = Category.Render
)
public class MotionGraph extends Module {
   public ArrayList<Double> height = new ArrayList();
   public double maxHeight;
   public double chlen;
   public double chlen2;
   public double avg;

   @EventTarget
   public void onUpdate(EventMotion e) {
      this.chlen = MathHelper.interpolate(Math.hypot(this.mc.player.motionX, this.mc.player.motionZ) * 75.0D * (double)this.mc.timer.timerSpeed, this.chlen, 0.30000001192092896D);
      this.height.add(this.chlen);
      if (this.height.size() >= 120) {
         this.height.remove(0);
      }

      this.avg = this.height.stream().mapToDouble(Double::doubleValue).sum() / (double)this.height.size();
      this.maxHeight = (Double)Collections.max(this.height);
      this.chlen2 = MathHelper.interpolate(this.maxHeight, this.chlen2, 0.1D);
   }

   @EventTarget
   public void onRender(EventDisplay e) {
      RenderUtil.bloom(() -> {
         RenderUtil.drawRectWH((float)e.sr.getScaledWidth() / 2.0F - 60.0F, (float)((double)(e.sr.getScaledHeight() - 50) - this.maxHeight), (float)this.height.size(), (float)this.maxHeight, (new Color(21, 21, 21, 100)).getRGB());
      }, 5.0F, 2.0F, Color.BLACK.getRGB());
      RenderUtil.blur(() -> {
         RenderUtil.drawRectWH((float)e.sr.getScaledWidth() / 2.0F - 60.0F, (float)((double)(e.sr.getScaledHeight() - 50) - this.maxHeight), (float)this.height.size(), (float)this.maxHeight, (new Color(21, 21, 21, 100)).getRGB());
      }, 10.0F);
      RenderUtil.drawRectWH((float)e.sr.getScaledWidth() / 2.0F - 60.0F, (float)((double)(e.sr.getScaledHeight() - 50) - this.avg), (float)this.height.size(), 0.5F, (new Color(255, 255, 255, 255)).getRGB());
      RenderUtil.drawRectWH((float)e.sr.getScaledWidth() / 2.0F - 60.0F, (float)((double)(e.sr.getScaledHeight() - 50) - this.maxHeight), (float)this.height.size(), (float)this.maxHeight, (new Color(21, 21, 21, 100)).getRGB());
      GL11.glPushMatrix();
      RenderUtil.start();
      GL11.glDisable(3008);
      GL11.glEnable(2848);
      GL11.glLineWidth(1.0F);
      GL11.glBegin(3);

      for(int i = 0; i < this.height.size(); ++i) {
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glVertex2d((double)((float)e.sr.getScaledWidth() / 2.0F - 60.0F + 0.5F + (float)i), (double)(e.sr.getScaledHeight() - 50) - (Double)this.height.get(i));
      }

      GL11.glEnd();
      GL11.glDisable(2848);
      GL11.glEnable(3008);
      RenderUtil.stop();
      GL11.glPopMatrix();
   }
}
