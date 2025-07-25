/* Decompiler 55ms, total 557ms, lines 249 */
package wtf.evolution.command.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import ru.salam4ik.bot.bot.Bot;
import ru.salam4ik.bot.bot.BotStarter;
import wtf.evolution.Main;
import wtf.evolution.command.Command;
import wtf.evolution.command.CommandInfo;
import wtf.evolution.helpers.ChatUtil;
import wtf.evolution.helpers.math.MathHelper;

@CommandInfo(
   name = "bot"
)
public class BotCommand extends Command {
   public static boolean log = true;
   public static boolean follow;
   public static boolean mimic;
   public static boolean movingStrafe;
   public static boolean movingRandom;
   public static String text;
   public static boolean look;
   public static boolean aura;
   public static boolean armor;
   public static boolean sword;
   public static String target;
   public int puk = 0;

   public String get(int key) {
      ArrayList<String> a = new ArrayList(Main.nickHash.keySet());
      return (String)a.get((int)MathHelper.clamp((float)key, 0.0F, (float)(a.size() - 1)));
   }

   public void execute(String[] args) {
      super.execute(args);
      if (args[1].equalsIgnoreCase("join")) {
         (new Thread(() -> {
            for(int i = 0; i < Integer.parseInt(args[2]); ++i) {
               if (args.length > 3) {
                  try {
                     Thread.sleep(Long.parseLong(args[3]));
                  } catch (InterruptedException var4) {
                     throw new RuntimeException(var4);
                  }
               }

               ++this.puk;
               BotStarter.run("Evolution" + RandomUtils.nextInt(1111, 9999), true, RandomStringUtils.randomAlphabetic(6).toLowerCase());
            }

         })).start();
         ChatUtil.print("Запущено " + args[2] + " ботов /////// discord.gg/evolution");
      }

      Iterator var2;
      Bot bot;
      if (args[1].equalsIgnoreCase("jump")) {
         var2 = Bot.bots.iterator();

         while(var2.hasNext()) {
            bot = (Bot)var2.next();
            bot.getBot().jump();
         }

         ChatUtil.print("Все боты прыгнули //// discord.gg/evolution");
      }

      if (args[1].equalsIgnoreCase("aura")) {
         aura = !aura;
         if (!aura) {
            for(var2 = Bot.bots.iterator(); var2.hasNext(); bot.getConnection().forward = false) {
               bot = (Bot)var2.next();
            }
         }

         target = args[2];
         ChatUtil.print("Аура " + (aura ? "включена" : "выключена"));
      }

      if (args[1].equalsIgnoreCase("armor")) {
         armor = !armor;
         ChatUtil.print("Авто-надевание " + (armor ? "включено" : "выключено"));
      }

      if (args[1].equalsIgnoreCase("sword")) {
         sword = !sword;
         ChatUtil.print("Авто-меч " + (sword ? "включен" : "выключен"));
      }

      if (args[1].equalsIgnoreCase("mimic")) {
         mimic = !mimic;
         ChatUtil.print("Mimic: " + mimic);
      }

      if (args[1].equalsIgnoreCase("strafe")) {
         movingStrafe = !movingStrafe;
         ChatUtil.print("movingStrafe: " + movingStrafe);
      }

      if (args[1].equalsIgnoreCase("random")) {
         movingRandom = !movingRandom;
         if (!movingRandom) {
            for(var2 = Bot.bots.iterator(); var2.hasNext(); bot.getConnection().forward = false) {
               bot = (Bot)var2.next();
            }
         }

         ChatUtil.print("movingRandom: " + movingRandom);
      }

      if (args[1].equalsIgnoreCase("follow")) {
         follow = !follow;
         if (!follow) {
            for(var2 = Bot.bots.iterator(); var2.hasNext(); bot.getConnection().forward = false) {
               bot = (Bot)var2.next();
            }
         }

         ChatUtil.print("discord.gg/evolution / Follow: " + follow);
      }

      if (args[1].equalsIgnoreCase("click")) {
         var2 = Bot.bots.iterator();

         while(var2.hasNext()) {
            bot = (Bot)var2.next();
            bot.getController().windowClick(bot.getBot().openContainer.windowId, Integer.parseInt(args[2]), 0, ClickType.PICKUP, bot.getBot());
         }

         ChatUtil.print("Все боты кликнули на " + args[2]);
      }

      if (args[1].equalsIgnoreCase("swap")) {
         var2 = Bot.bots.iterator();

         while(var2.hasNext()) {
            bot = (Bot)var2.next();
            bot.getConnection().sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
         }

         ChatUtil.print("Все боты использовали предмет");
      }

      if (args[1].equalsIgnoreCase("slot")) {
         var2 = Bot.bots.iterator();

         while(var2.hasNext()) {
            bot = (Bot)var2.next();
            bot.getConnection().sendPacket(new CPacketHeldItemChange(Integer.parseInt(args[2])));
         }

         ChatUtil.print("Все боты выбрали слот " + args[2]);
      }

      if (args[1].equalsIgnoreCase("kick")) {
         var2 = Bot.bots.iterator();

         while(var2.hasNext()) {
            bot = (Bot)var2.next();
            bot.getNetManager().closeChannel();
            Bot.bots.remove(bot);
         }

         ChatUtil.print("Все боты кикнуты / discord.gg/evolution");
      } else if (args[1].equalsIgnoreCase("look")) {
         look = !look;
         if (look) {
            for(var2 = Bot.bots.iterator(); var2.hasNext(); bot.getBot().moveForward = 0.0F) {
               bot = (Bot)var2.next();
            }
         }

         ChatUtil.print("Look: " + look);
      }

      if (args[1].equalsIgnoreCase("message")) {
         (new Thread(() -> {
            Iterator var1 = Bot.bots.iterator();

            while(true) {
               Bot bot;
               do {
                  if (!var1.hasNext()) {
                     return;
                  }

                  bot = (Bot)var1.next();
               } while(args.length <= 2);

               StringBuilder text = new StringBuilder();

               for(int i = 2; i < args.length; ++i) {
                  text.append(args[i]).append(" ");
               }

               bot.getConnection().sendPacket(new CPacketChatMessage(text.substring(0, text.length() - 1)));
            }
         })).start();
      }

      if (args[1].equalsIgnoreCase("text") && args.length > 2) {
         StringBuilder text = new StringBuilder();

         for(int i = 2; i < args.length; ++i) {
            text.append(args[i]).append(" ");
         }

         BotCommand.text = text.substring(0, text.length() - 1);
      }

      if (args[1].equalsIgnoreCase("invclear")) {
         var2 = Bot.bots.iterator();

         while(var2.hasNext()) {
            bot = (Bot)var2.next();

            for(int o = 0; o < 46; ++o) {
               bot.getController().windowClick(bot.getBot().inventoryContainer.windowId, o, 1, ClickType.THROW, bot.getBot());
            }
         }

         ChatUtil.print("Все боты очистили инвентарь");
      }

   }

   public static String getRandomString(int length) {
      return RandomStringUtils.random(length, "0123456789");
   }

   public List<String> getSuggestions(String[] args) {
      if (args.length == 1) {
         return Arrays.asList("join", "kick", "follow", "strafe", "random", "click", "swap", "look", "message", "invclear", "slot");
      } else {
         return args.length > 1 && args[1].equalsIgnoreCase("slot") ? Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8") : null;
      }
   }
}
