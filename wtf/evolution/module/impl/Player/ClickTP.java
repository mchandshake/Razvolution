/* Decompiler 5ms, total 369ms, lines 42 */
package wtf.evolution.module.impl.Player;

import java.awt.Color;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.util.math.RayTraceResult;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventRender;
import wtf.evolution.event.events.impl.MouseEvent;
import wtf.evolution.helpers.Castt;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;

@ModuleInfo(
   name = "ClickTP",
   type = Category.Player
)
public class ClickTP extends Module {
   @EventTarget
   public void onUpdate(MouseEvent e) {
      if (e.button == 1) {
         RayTraceResult r = Castt.rayTrace(500.0D, this.mc.player.rotationYaw, this.mc.player.rotationPitch);
         this.mc.player.connection.sendPacket(new Position(this.mc.player.posX, this.mc.player.posY, this.mc.player.posZ, false));

         for(int i = 0; i < 2; ++i) {
            this.mc.player.connection.sendPacket(new Position((double)((float)r.getBlockPos().getX() + 0.5F), (double)(r.getBlockPos().getY() + 1), (double)((float)r.getBlockPos().getZ() + 0.5F), true));
         }
      }

   }

   @EventTarget
   public void onRender(EventRender e) {
      RayTraceResult r = Castt.rayTrace(150.0D, this.mc.player.rotationYaw, this.mc.player.rotationPitch);
      if (r != null) {
         RenderUtil.blockEsp(r.getBlockPos(), Color.WHITE);
      }

   }
}
