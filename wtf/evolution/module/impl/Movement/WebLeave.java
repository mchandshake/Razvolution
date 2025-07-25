/* Decompiler 2ms, total 259ms, lines 31 */
package wtf.evolution.module.impl.Movement;

import net.minecraft.block.BlockWeb;
import net.minecraft.util.math.BlockPos;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventMotion;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.SliderSetting;

@ModuleInfo(
   name = "WebLeave",
   type = Category.Movement
)
public class WebLeave extends Module {
   public SliderSetting motion = (new SliderSetting("Motion", 1.0F, 0.0F, 10.0F, 0.1F)).call(this);

   @EventTarget
   public void onMotion(EventMotion e) {
      if (this.mc.player.isInWeb) {
         this.mc.player.motionY = 1.0D;
      }

      if (this.mc.world.getBlockState(new BlockPos(this.mc.player.posX, this.mc.player.posY - 0.1D, this.mc.player.posZ)).getBlock() instanceof BlockWeb) {
         this.mc.player.motionY = (double)this.motion.get();
      }

   }
}
