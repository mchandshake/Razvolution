/* Decompiler 5ms, total 395ms, lines 58 */
package wtf.evolution.command.impl;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import wtf.evolution.Main;
import wtf.evolution.command.Command;
import wtf.evolution.command.CommandInfo;
import wtf.evolution.helpers.ChatUtil;

@CommandInfo(
   name = "config"
)
public class ConfigCommand extends Command {
   public void execute(String[] args) {
      super.execute(args);
      if (args[1].startsWith("load")) {
         Main.c.loadConfig(args[2]);
         ChatUtil.print("Конфигурация загружена.");
      }

      if (args[1].startsWith("save")) {
         Main.c.saveConfig(args[2]);
         ChatUtil.print("Конфигурация сохранена.");
      }

      if (args[1].startsWith("list")) {
         File file = new File(this.mc.gameDir + "\\evolution\\configs");
         File[] var3 = file.listFiles();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            File s = var3[var5];
            ChatUtil.print(s.getName());
         }
      }

   }

   public List<String> getSuggestions(String[] args) {
      try {
         File file;
         if (args[1].startsWith("load")) {
            file = new File(this.mc.gameDir + "\\evolution\\configs");
            return (List)Arrays.asList(file.listFiles()).stream().map(File::getName).collect(Collectors.toList());
         } else if (args[1].startsWith("save")) {
            file = new File(this.mc.gameDir + "\\evolution\\configs");
            return (List)Arrays.asList(file.listFiles()).stream().map(File::getName).collect(Collectors.toList());
         } else {
            return Arrays.asList("load", "save", "list");
         }
      } catch (Exception var3) {
         return null;
      }
   }
}
