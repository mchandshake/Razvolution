/* Decompiler 3ms, total 327ms, lines 39 */
package wtf.evolution.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import wtf.evolution.Main;
import wtf.evolution.command.Command;
import wtf.evolution.command.CommandInfo;
import wtf.evolution.helpers.ChatUtil;
import wtf.evolution.module.Module;

@CommandInfo(
   name = "toggle"
)
public class ToggleCommand extends Command {
   public void execute(String[] args) {
      super.execute(args);
      Main.m.getModule(args[1]).toggle();
   }

   public void onError() {
      super.onError();
      ChatUtil.print(ChatFormatting.RED + "Ошибка в команде -> toggle <имя модуля>");
   }

   public List<String> getSuggestions(String[] args) {
      List<String> modules = new ArrayList();
      Iterator var3 = Main.m.m.iterator();

      while(var3.hasNext()) {
         Module m = (Module)var3.next();
         modules.add(m.name);
      }

      return modules;
   }
}
