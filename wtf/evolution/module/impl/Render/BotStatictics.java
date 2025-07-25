/* Decompiler 7ms, total 547ms, lines 65 */
package wtf.evolution.module.impl.Render;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ru.salam4ik.bot.bot.Bot;
import ru.salam4ik.bot.bot.network.BotPlayClient;
import wtf.evolution.Main;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventDisplay;
import wtf.evolution.helpers.animation.Counter;
import wtf.evolution.helpers.font.Fonts;
import wtf.evolution.helpers.math.MathHelper;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;

@ModuleInfo(
   name = "BotStatictics",
   type = Category.Render
)
public class BotStatictics extends Module {
   public Counter timer = new Counter();
   public double proxy;
   public double balance;

   @EventTarget
   public void onRender(EventDisplay e) {
      this.proxy = (double)Main.proxy.proxies.size();
      this.balance = Main.balance;
      if (this.timer.hasReached(30000.0D)) {
         this.balance();
      }

      List<String> botsname = (List)Bot.bots.stream().map((n) -> {
         return n.getBot().getName();
      }).collect(Collectors.toList());
      int count = (int)this.mc.getConnection().getPlayerInfoMap().stream().filter((name) -> {
         return botsname.contains(name.getGameProfile().getName());
      }).count();
      Fonts.pix.drawCenteredStringWithOutline("[ evolution§b.lua§f ]", (double)((float)e.sr.getScaledWidth() / 2.0F), (double)(e.sr.getScaledHeight() - 70), -1);
      Fonts.pix.drawCenteredStringWithOutline("Server brand: §b" + this.mc.player.getServerBrand() + "§f  |  User name: §b" + this.mc.getSession().getUsername(), (double)((float)e.sr.getScaledWidth() / 2.0F), (double)(e.sr.getScaledHeight() - 65), -1);
      Fonts.pix.drawCenteredStringWithOutline("Bots total: §b" + Bot.bots.size() + "§f  |  On this server: §b" + count + "§f  |  Proxy: §b" + (int)this.proxy, (double)((float)e.sr.getScaledWidth() / 2.0F), (double)(e.sr.getScaledHeight() - 60), -1);
      Fonts.pix.drawCenteredStringWithOutline("Balance: §b" + (int)this.balance + "§f  |  Last captcha: §b" + BotPlayClient.lastSolved, (double)((float)e.sr.getScaledWidth() / 2.0F), (double)(e.sr.getScaledHeight() - 55), -1);
      List<Long> longs = (List)Bot.bots.stream().map(Bot::getTime).collect(Collectors.toList());
      long average = longs.stream().mapToLong(Long::longValue).sum() / (long)longs.size();
      Fonts.pix.drawCenteredStringWithOutline("Average time: §b" + MathHelper.format(average), (double)((float)e.sr.getScaledWidth() / 2.0F), (double)(e.sr.getScaledHeight() - 50), -1);
   }

   public void balance() {
      (new Thread(() -> {
         Document site = null;

         try {
            site = Jsoup.connect("http://api.captcha.guru/res.php?action=getbalance&key=" + Main.apiKey).get();
            Main.balance = Double.parseDouble(site.text());
         } catch (NumberFormatException | IOException var2) {
         }

      })).start();
   }
}
