/* Decompiler 2ms, total 286ms, lines 23 */
package wtf.evolution.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import wtf.evolution.command.Command;
import wtf.evolution.command.CommandInfo;
import wtf.evolution.helpers.ChatUtil;

@CommandInfo(
   name = "hclip"
)
public class HClipCommand extends Command {
   public void execute(String[] args) {
      super.execute(args);
      this.mc.player.setPosition(this.mc.player.posX - Math.sin(Math.toRadians((double)this.mc.player.rotationYaw)) * Double.parseDouble(args[1]), this.mc.player.posY, this.mc.player.posZ + Math.cos(Math.toRadians((double)this.mc.player.rotationYaw)) * Double.parseDouble(args[1]));
      ChatUtil.print("Clipped to " + args[1] + " blocks.");
   }

   public void onError() {
      super.onError();
      ChatUtil.print(ChatFormatting.RED + "Ошибка в команде -> hclip <значение>");
   }
}
