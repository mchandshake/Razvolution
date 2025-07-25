/* Decompiler 3ms, total 283ms, lines 45 */
package wtf.evolution.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lwjgl.input.Keyboard;
import wtf.evolution.Main;
import wtf.evolution.command.Command;
import wtf.evolution.command.CommandInfo;
import wtf.evolution.helpers.ChatUtil;
import wtf.evolution.module.Module;

@CommandInfo(
   name = "bind"
)
public class BindCommand extends Command {
   public void execute(String[] args) {
      super.execute(args);
      Main.m.getModule(args[1]).bind = Keyboard.getKeyIndex(args[2].toUpperCase());
      ChatUtil.print("Клавиша " + args[2] + " назначена на " + args[1]);
   }

   public void onError() {
      super.onError();
      ChatUtil.print(ChatFormatting.RED + "Ошибка в команде -> bind <имя модуля> <клавиша>");
   }

   public List<String> getSuggestions(String[] args) {
      if (args.length > 1) {
         return null;
      } else {
         List<String> modules = new ArrayList();
         Iterator var3 = Main.m.m.iterator();

         while(var3.hasNext()) {
            Module m = (Module)var3.next();
            modules.add(m.name);
         }

         return modules;
      }
   }
}
