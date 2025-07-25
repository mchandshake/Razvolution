/* Decompiler 8ms, total 295ms, lines 96 */
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
import wtf.evolution.settings.Setting;
import wtf.evolution.settings.options.BooleanSetting;
import wtf.evolution.settings.options.ModeSetting;
import wtf.evolution.settings.options.SliderSetting;

@CommandInfo(
   name = "set"
)
public class SettingCommand extends Command {
   public void execute(String[] args) {
      super.execute(args);
      Setting s = null;
      Iterator var3 = Main.m.getModule(args[1]).getSettings().iterator();

      while(var3.hasNext()) {
         Setting set = (Setting)var3.next();
         if (set.name.contains(args[3])) {
            s = set;
         }
      }

      if (s == null) {
         ChatUtil.print(ChatFormatting.RED + "Нет такой настройки.");
      } else {
         if (args[2].contains("boolean")) {
            ((BooleanSetting)s).set(Boolean.parseBoolean(args[4]));
         } else if (args[2].contains("slider")) {
            ((SliderSetting)s).current = Float.parseFloat(args[4]);
         } else if (args[2].contains("mode")) {
            if (!((ModeSetting)s).modes.contains(args[4])) {
               ChatUtil.print(ChatFormatting.RED + "Нет такого мода.");
               return;
            }

            ((ModeSetting)s).currentMode = args[4];
         }

         ChatUtil.print("Установлено значение " + args[4] + " на " + args[3] + " для модуля " + args[1]);
      }
   }

   public void onError() {
      super.onError();
      ChatUtil.print(ChatFormatting.RED + "Ошибка в команде -> set <имя модуля> <тип настройки> <имя настройки> <значение>");
   }

   public List<String> getSuggestions(String[] args) {
      ArrayList types;
      Iterator var3;
      if (args.length > 1) {
         if (args.length <= 2) {
            types = new ArrayList();
            types.add("boolean");
            types.add("slider");
            types.add("mode");
            return types;
         } else if (args.length <= 3) {
            types = new ArrayList();
            var3 = Main.m.getModule(args[1]).getSettings().iterator();

            while(var3.hasNext()) {
               Setting s = (Setting)var3.next();
               if (!s.name.contains(" ")) {
                  types.add(s.name);
               }
            }

            return types;
         } else {
            return null;
         }
      } else {
         types = new ArrayList();
         var3 = Main.m.m.iterator();

         while(var3.hasNext()) {
            Module m = (Module)var3.next();
            types.add(m.name);
         }

         return types;
      }
   }
}
