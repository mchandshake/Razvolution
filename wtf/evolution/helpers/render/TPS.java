/* Decompiler 5ms, total 271ms, lines 57 */
package wtf.evolution.helpers.render;

import java.util.Arrays;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.util.math.MathHelper;
import wtf.evolution.event.EventManager;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventPacket;

public class TPS {
   private static float[] tickRates = new float[20];
   private int nextIndex = 0;
   private long timeLastTimeUpdate;

   public TPS() {
      this.nextIndex = 0;
      this.timeLastTimeUpdate = -1L;
      Arrays.fill(tickRates, 0.0F);
      EventManager.register(this);
   }

   public static float getTPSServer() {
      float numTicks = 0.0F;
      float sumTickRates = 0.0F;
      float[] var2 = tickRates;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         float tickRate = var2[var4];
         if (tickRate > 0.0F) {
            sumTickRates += tickRate;
            ++numTicks;
         }
      }

      return MathHelper.clamp(sumTickRates / numTicks, 0.0F, 20.0F);
   }

   private void onTimeUpdate() {
      if (this.timeLastTimeUpdate != -1L) {
         float timeElapsed = (float)(System.currentTimeMillis() - this.timeLastTimeUpdate) / 1000.0F;
         tickRates[this.nextIndex % tickRates.length] = MathHelper.clamp(20.0F / timeElapsed, 0.0F, 20.0F);
         ++this.nextIndex;
      }

      this.timeLastTimeUpdate = System.currentTimeMillis();
   }

   @EventTarget
   public void onReceivePacket(EventPacket event) {
      if (event.getPacket() instanceof SPacketTimeUpdate) {
         this.onTimeUpdate();
      }

   }
}
