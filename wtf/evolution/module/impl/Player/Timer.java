/* Decompiler 5ms, total 317ms, lines 65 */
package wtf.evolution.module.impl.Player;

import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.network.play.client.CPacketPlayer.PositionRotation;
import net.minecraft.util.math.MathHelper;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventDisplay;
import wtf.evolution.event.events.impl.EventMotion;
import wtf.evolution.event.events.impl.EventPacket;
import wtf.evolution.helpers.MovementUtil;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.BooleanSetting;
import wtf.evolution.settings.options.ColorSetting;
import wtf.evolution.settings.options.SliderSetting;

@ModuleInfo(
   name = "Timer",
   type = Category.Player
)
public class Timer extends Module {
   public float ticks = 0.0F;
   public boolean active;
   public float animWidth;
   public SliderSetting timer = (new SliderSetting("Timer", 1.0F, 0.1F, 15.0F, 0.1F)).call(this);
   public BooleanSetting smart = (new BooleanSetting("Smart", false)).call(this);
   public ColorSetting color = (new ColorSetting("Color", -1)).setHidden(() -> {
      return !this.smart.get();
   }).call(this);

   @EventTarget
   public void onSend(EventPacket eventSendPacket) {
      if ((eventSendPacket.getPacket() instanceof Position || eventSendPacket.getPacket() instanceof PositionRotation) && this.ticks <= 25.0F && !this.active && MovementUtil.isMoving()) {
         this.ticks = (float)((double)this.ticks + 0.6D);
      }

   }

   @EventTarget
   public void onPreUpdate(EventMotion e) {
      if (this.smart.get()) {
         if (!this.active) {
            this.mc.timer.timerSpeed = this.timer.get();
         } else {
            this.mc.timer.timerSpeed = 1.0F;
         }
      } else {
         this.mc.timer.timerSpeed = this.timer.get();
      }

      this.ticks = MathHelper.clamp(this.ticks, 0.0F, 100.0F);
   }

   @EventTarget
   public void onRender(EventDisplay e) {
   }

   public void onDisable() {
      super.onDisable();
      this.active = true;
      this.mc.timer.timerSpeed = 1.0F;
   }
}
