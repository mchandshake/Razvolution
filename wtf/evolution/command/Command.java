/* Decompiler 1ms, total 322ms, lines 29 */
package wtf.evolution.command;

import java.util.List;
import net.minecraft.client.Minecraft;

public class Command {
   public String command;
   public CommandInfo info = (CommandInfo)this.getClass().getAnnotation(CommandInfo.class);
   public Minecraft mc = Minecraft.getMinecraft();

   public Command() {
      this.command = this.info.name();
   }

   public void execute(String[] args) {
   }

   public void onError() {
   }

   public String getCommand() {
      return this.command;
   }

   public List<String> getSuggestions(String[] args) {
      return null;
   }
}
