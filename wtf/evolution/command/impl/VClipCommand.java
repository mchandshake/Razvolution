/* Decompiler 2ms, total 437ms, lines 23 */
package wtf.evolution.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import wtf.evolution.command.Command;
import wtf.evolution.command.CommandInfo;
import wtf.evolution.helpers.ChatUtil;

@CommandInfo(
   name = "vclip"
)
public class VClipCommand extends Command {
   public void execute(String[] args) {
      super.execute(args);
      this.mc.player.setPosition(this.mc.player.posX, this.mc.player.posY + Double.parseDouble(args[1]), this.mc.player.posZ);
      ChatUtil.print("Clipped to " + args[1] + " blocks.");
   }

   public void onError() {
      super.onError();
      ChatUtil.print(ChatFormatting.RED + "Ошибка в команде -> vclip <значение>");
   }
}
