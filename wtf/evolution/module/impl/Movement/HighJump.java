/* Decompiler 3ms, total 279ms, lines 49 */
package wtf.evolution.module.impl.Movement;

import java.util.concurrent.TimeUnit;
import net.minecraft.network.play.client.CPacketPlayer;
import wtf.evolution.helpers.MovementUtil;
import wtf.evolution.helpers.math.MathHelper;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.SliderSetting;

@ModuleInfo(
   name = "HighJump",
   type = Category.Movement
)
public class HighJump extends Module {
   public SliderSetting value = (new SliderSetting("Height", 5.0F, 1.0F, 10.0F, 0.1F)).call(this);

   public void onEnable() {
      super.onEnable();
      MovementUtil.setSpeed(0.0F);
      this.mc.player.connection.sendPacket(new CPacketPlayer(true));
      if (this.mc.player.onGround) {
         this.mc.player.jump();
      }

      (new Thread(() -> {
         double jump = (double)this.value.get();
         this.mc.player.motionY = jump;

         try {
            TimeUnit.MILLISECONDS.sleep((long)MathHelper.getRandomNumberBetween(200, 250));
         } catch (InterruptedException var5) {
            var5.printStackTrace();
         }

         this.mc.player.motionY = jump - (0.098D + 0.01D * (jump * 2.0D - 2.0D));

         try {
            TimeUnit.MILLISECONDS.sleep(50L);
         } catch (InterruptedException var4) {
            throw new RuntimeException(var4);
         }

         this.toggle();
      })).start();
   }
}
