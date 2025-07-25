/* Decompiler 29ms, total 435ms, lines 140 */
package wtf.evolution.module.impl.Player;

import java.util.Iterator;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import wtf.evolution.Main;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.ChatEvent;
import wtf.evolution.event.events.impl.EventMotion;
import wtf.evolution.helpers.animation.Counter;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.notifications.NotificationType;

@ModuleInfo(
   name = "BotSolver",
   type = Category.Player
)
public class BotSolver extends Module {
   public Counter c = new Counter();

   @EventTarget
   public void onUpdate(EventMotion e) {
      int i = 0;
      ContainerChest chest = (ContainerChest)this.mc.player.openContainer;
      Iterator var4 = this.mc.player.openContainer.getInventory().iterator();

      while(var4.hasNext()) {
         ItemStack stack = (ItemStack)var4.next();
         ++i;
         if (stack.getDisplayName().length() < 3 && this.c.hasReached(400.0D)) {
            System.out.println(i);
            this.mc.playerController.windowClick(this.mc.player.openContainer.windowId, i - 1, 0, ClickType.PICKUP, this.mc.player);
            this.c.reset();
         }

         if (chest.getLowerChestInventory().getDisplayName().getUnformattedText().toLowerCase().contains("зелье") && PotionUtils.getColor(stack) == 255 && this.c.hasReached(400.0D)) {
            System.out.println(i);
            this.mc.playerController.windowClick(this.mc.player.openContainer.windowId, i - 1, 0, ClickType.PICKUP, this.mc.player);
            this.c.reset();
         }
      }

   }

   @EventTarget
   public void solve(ChatEvent e) {
      this.getInfinitySolve(this.mc.player, e.message);
   }

   public void getInfinitySolve(EntityPlayerSP bot, String message) {
      String[] solve1 = new String[]{"①", "②", "③", "④", "⑤", "⑥", "⑦", "⑧", "⑨", "⑩", "⑪", "⑫", "⑬", "⑭", "⑮", "⑯", "⑰", "⑱", "⑲", "⑳"};
      String[] solve2 = new String[]{"⑴", "⑵", "⑶", "⑷", "⑸", "⑹", "⑺", "⑻", "⑼", "⑽", "⑾", "⑿", "⒀", "⒁", "⒂", "⒃", "⒄", "⒅", "⒆", "⒇"};
      String[] solve3 = new String[]{"⒈", "⒉", "⒊", "⒋", "⒌", "⒍", "⒎", "⒏", "⒐", "⒑", "⒒", "⒓", "⒔", "⒕", "⒖", "⒗", "⒘", "⒙", "⒚", "⒛"};
      String[] solve4 = new String[]{"один", "два", "три", "четыре", "пять", "шесть", "семь", "восемь", "девять", "десять", "одиннадцать", "двенадцать", "тринадцать", "четырнадцать", "пятнадцать", "шестнадцать", "семнадцать", "восемнадцать", "девятнадцать"};
      String[] solve5 = new String[]{"⓫", "⓬", "⓭", "⓮", "⓯", "⓰", "⓱", "⓲", "⓳", "⓴"};
      if (message.contains("Решите пример: ")) {
         String text = message.replaceAll("Решите пример: ", "");
         text = text.replaceAll("\\.", "");
         text = text.replaceAll("!", "");
         text = text.replaceAll(" ", "");
         text = text.replaceAll("_", "");
         text = text.replaceAll("-", "");
         text = text.replaceAll("_", "");
         String[] split1 = text.split("\\+");
         int solve = 0;
         String[] split = solve1;
         int var12 = solve1.length;

         int var13;
         String s;
         for(var13 = 0; var13 < var12; ++var13) {
            s = split[var13];
            ++solve;
            if (split1[1].equalsIgnoreCase(s)) {
               text = text.replaceAll(s, String.valueOf(solve));
            }
         }

         solve = 0;
         split = solve2;
         var12 = solve2.length;

         for(var13 = 0; var13 < var12; ++var13) {
            s = split[var13];
            ++solve;
            if (split1[1].equalsIgnoreCase(s)) {
               text = text.replaceAll(s, String.valueOf(solve));
            }
         }

         solve = 0;
         split = solve3;
         var12 = solve3.length;

         for(var13 = 0; var13 < var12; ++var13) {
            s = split[var13];
            ++solve;
            if (split1[1].equalsIgnoreCase(s)) {
               text = text.replaceAll(s, String.valueOf(solve));
            }
         }

         solve = 0;
         split = solve4;
         var12 = solve4.length;

         for(var13 = 0; var13 < var12; ++var13) {
            s = split[var13];
            ++solve;
            if (split1[1].equalsIgnoreCase(s)) {
               text = text.replaceAll(s, String.valueOf(solve));
            }
         }

         solve = 10;
         split = solve5;
         var12 = solve5.length;

         for(var13 = 0; var13 < var12; ++var13) {
            s = split[var13];
            ++solve;
            if (split1[1].equalsIgnoreCase(s)) {
               text = text.replaceAll(s, String.valueOf(solve));
            }
         }

         Main.notify.call("Solved", bot.getName() + " " + text, NotificationType.INFO);
         split = text.split("\\+");
         System.out.println(message);
         bot.sendChatMessage(String.valueOf(Integer.parseInt(split[0]) + Integer.parseInt(split[1])));
      }

   }
}
