/* Decompiler 4ms, total 1004ms, lines 50 */
package wtf.evolution.module.impl.Render;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventDisplay;
import wtf.evolution.event.events.impl.EventPacket;
import wtf.evolution.helpers.ScaleUtil;
import wtf.evolution.helpers.font.Fonts;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;

@ModuleInfo(
   name = "Hotbar",
   type = Category.Render
)
public class Hotbar extends Module {
   private float tps;
   private double lastTimePacketReceived;

   @EventTarget
   public void onRender(EventDisplay e) {
      ScaleUtil.scale_pre();
      double blockpersecord = Math.hypot(this.mc.player.prevPosX - this.mc.player.posX, this.mc.player.prevPosZ - this.mc.player.posZ) * 20.0D;
      double height = this.mc.currentScreen instanceof GuiChat ? 13.0D : 0.0D;
      Fonts.pix.drawStringWithShadow(String.format("bps: %.2f", blockpersecord), 1.0D, (double)(ScaleUtil.calc(e.sr.getScaledHeight()) - 3) - height, 16777215);
      Fonts.pix.drawStringWithShadow(String.format("tps: %.1f", this.getTps()), 1.0D, (double)(ScaleUtil.calc(e.sr.getScaledHeight()) - 9) - height, 16777215);
      ScaleUtil.scale_post();
   }

   public float getTps() {
      return this.tps;
   }

   @EventTarget
   public void onPacket(EventPacket e) {
      if (e.getPacket() instanceof SPacketTimeUpdate) {
         float tmp = (float)(this.calculateTps((double)System.currentTimeMillis() - this.lastTimePacketReceived) * 100.0D);
         this.tps = tmp / 100.0F;
         this.lastTimePacketReceived = (double)System.currentTimeMillis();
      }

   }

   private double calculateTps(double n) {
      return 20.0D / Math.max((n - 1000.0D) / 500.0D, 1.0D);
   }
}
