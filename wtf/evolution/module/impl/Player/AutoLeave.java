/* Decompiler 4ms, total 390ms, lines 52 */
package wtf.evolution.module.impl.Player;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import wtf.evolution.Main;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventMotion;
import wtf.evolution.helpers.ChatUtil;
import wtf.evolution.helpers.math.MathHelper;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.BooleanSetting;
import wtf.evolution.settings.options.ModeSetting;
import wtf.evolution.settings.options.SliderSetting;

@ModuleInfo(
   name = "AutoLeave",
   type = Category.Player
)
public class AutoLeave extends Module {
   private final ModeSetting mode = (new ModeSetting("Mode", "/spawn", new String[]{"/spawn", "/home", "/hub", "/lobby"})).call(this);
   private final SliderSetting radius = (new SliderSetting("Radius", 30.0F, 1.0F, 100.0F, 5.0F)).call(this);
   private final BooleanSetting ignoreFriends = new BooleanSetting("Ignore Friends", true);

   @EventTarget
   public void onUpdate(EventMotion e) {
      Iterator var2 = this.mc.world.playerEntities.iterator();

      while(true) {
         EntityPlayer player;
         do {
            do {
               if (!var2.hasNext()) {
                  return;
               }

               player = (EntityPlayer)var2.next();
            } while(player == this.mc.player);
         } while(this.ignoreFriends.get() && Main.f.friends.contains(player.getName()));

         double distance = (double)this.mc.player.getDistance(player);
         if (distance <= (double)this.radius.get()) {
            ChatUtil.print(ChatFormatting.YELLOW + "[AutoLeave]: " + ChatFormatting.WHITE + "замечен игрок " + ChatFormatting.GREEN + player.getName() + ChatFormatting.WHITE + " в радиусе " + ChatFormatting.GREEN + MathHelper.round(distance, 1.0D));
            this.mc.player.sendChatMessage(this.mode.get());
            this.toggle();
         }
      }
   }
}
