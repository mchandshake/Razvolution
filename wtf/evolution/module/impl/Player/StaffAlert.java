/* Decompiler 8ms, total 1057ms, lines 72 */
package wtf.evolution.module.impl.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.network.play.server.SPacketPlayerListItem.Action;
import wtf.evolution.Main;
import wtf.evolution.editor.Drag;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventPlayer;
import wtf.evolution.helpers.render.Translate;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.notifications.NotificationType;

@ModuleInfo(
   name = "StaffAlert",
   type = Category.Player
)
public class StaffAlert extends Module {
   public ArrayList<NetworkPlayerInfo> staff = new ArrayList();
   public Translate translate = new Translate(0.0F, 0.0F);
   public Drag drag = Main.createDrag(this, "staff", 20.0F, 60.0F);

   public void onDisable() {
      super.onDisable();
      this.staff.clear();
   }

   @EventTarget
   public void onPower(EventPlayer eventPlayer) {
      if (eventPlayer.getAction() == Action.ADD_PLAYER && this.check(eventPlayer.getPlayerData().getDisplayName().getUnformattedText().toLowerCase())) {
         (new Thread(() -> {
            try {
               TimeUnit.SECONDS.sleep(1L);
            } catch (InterruptedException var3) {
               throw new RuntimeException(var3);
            }

            if (this.check(this.mc.player.connection.getPlayerInfo(eventPlayer.getPlayerData().getProfile().getId()).getDisplayName().getUnformattedText().toLowerCase())) {
               Main.notify.call("Staff Alert", eventPlayer.getPlayerData().getDisplayName().getFormattedText() + " joined!", NotificationType.INFO);
            }

         })).start();
      }

      if (eventPlayer.getAction() == Action.REMOVE_PLAYER) {
         Iterator var2 = this.staff.iterator();

         while(var2.hasNext()) {
            NetworkPlayerInfo info = (NetworkPlayerInfo)var2.next();
            if (info.getGameProfile().getId().equals(eventPlayer.getPlayerData().getProfile().getId())) {
               if (this.mc.player.connection.getPlayerInfo(eventPlayer.getPlayerData().getProfile().getId()).getGameProfile().getName() == null) {
                  this.staff.remove(info);
                  Main.notify.call("Staff Alert", eventPlayer.getPlayerData().getDisplayName().getFormattedText() + " leaved!", NotificationType.INFO);
               } else {
                  Main.notify.call("Staff Alert", eventPlayer.getPlayerData().getDisplayName().getFormattedText() + " spectator!", NotificationType.INFO);
               }
               break;
            }
         }
      }

   }

   public boolean check(String name) {
      return name.contains("helper") || name.contains("moder") || name.contains("admin") || name.contains("owner") || name.contains("curator") || name.contains("хелпер") || name.contains("модер") || name.contains("админ") || name.contains("куратор");
   }
}
