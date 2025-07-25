/* Decompiler 1ms, total 514ms, lines 33 */
package wtf.evolution.module.impl.Render;

import net.minecraft.network.play.server.SPacketTimeUpdate;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventPacket;
import wtf.evolution.event.events.impl.EventUpdate;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.SliderSetting;

@ModuleInfo(
   name = "WorldTime",
   type = Category.Render
)
public class WorldTime extends Module {
   public SliderSetting time = (new SliderSetting("Time", 1000.0F, 0.0F, 24000.0F, 100.0F)).call(this);

   @EventTarget
   public void onUpdate(EventUpdate e) {
      this.setSuffix(String.valueOf(this.time.get()));
      this.mc.world.setWorldTime((long)this.time.get());
   }

   @EventTarget
   public void onPacket(EventPacket e) {
      if (e.getPacket() instanceof SPacketTimeUpdate) {
         e.cancel();
      }

   }
}
