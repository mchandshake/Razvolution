/* Decompiler 7ms, total 409ms, lines 80 */
package wtf.evolution.module.impl.Player;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import wtf.evolution.Main;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventMotion;
import wtf.evolution.event.events.impl.EventPacket;
import wtf.evolution.event.events.impl.EventRender;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.notifications.NotificationType;
import wtf.evolution.settings.options.BooleanSetting;
import wtf.evolution.settings.options.SliderSetting;

@ModuleInfo(
   name = "NoCom",
   type = Category.Player
)
public class NoCom extends Module {
   public int distance = 100;
   public int xyiec = 0;
   public SliderSetting boost = (new SliderSetting("Boost", 7.0F, 1.0F, 10.0F, 1.0F)).call(this);
   public SliderSetting distance1 = (new SliderSetting("Distance", 16.0F, 1.0F, 32.0F, 1.0F)).call(this);
   public BooleanSetting render = (new BooleanSetting("Render checked block", true)).call(this);
   public ArrayList<BlockPos> p = new ArrayList();

   @EventTarget
   public void onUpdate(EventMotion e) {
      for(int i = 0; (float)i < this.boost.get(); ++i) {
         ++this.xyiec;
         this.setSuffix(String.valueOf(this.xyiec));
         this.distance = (int)((float)this.distance + this.distance1.get());
         double x = this.mc.player.posX + Math.cos(Math.toRadians((double)this.distance)) * (double)this.distance / (double)this.distance1.get();
         double z = this.mc.player.posZ + Math.sin(Math.toRadians((double)this.distance)) * (double)this.distance / (double)this.distance1.get();
         this.p.add(new BlockPos(x, this.mc.player.posY - 1.0D, z));
         this.mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, new BlockPos(x, 255.0D, z), this.mc.player.getHorizontalFacing()));
      }

   }

   @EventTarget
   public void onPacket(EventPacket e) {
      if (e.getPacket() instanceof SPacketBlockChange) {
         SPacketBlockChange packet = (SPacketBlockChange)e.getPacket();
         this.mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString("Founded: " + packet.getBlockPosition().getX() + " " + packet.getBlockPosition().getZ()));
         Main.notify.call("NoCom", "Founded: " + packet.getBlockPosition().getX() + " " + packet.getBlockPosition().getZ(), NotificationType.INFO);
      }

   }

   @EventTarget
   public void onRender(EventRender e) {
      if (this.render.get()) {
         Iterator var2 = this.p.iterator();

         while(var2.hasNext()) {
            BlockPos pos = (BlockPos)var2.next();
            RenderUtil.blockEsp(pos, new Color(255, 255, 255, 100));
         }

      }
   }

   public void onDisable() {
      super.onDisable();
      this.xyiec = 0;
      this.distance = 100;
      this.p.clear();
   }
}
