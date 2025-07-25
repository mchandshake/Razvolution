/* Decompiler 4ms, total 359ms, lines 40 */
package wtf.evolution;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordEventHandlers.Builder;
import wtf.evolution.Discord.1;

public class Discord {
   private boolean running = true;
   private static long created = 0L;

   public void start() {
      created = System.currentTimeMillis();
      DiscordEventHandlers handlers = (new Builder()).setReadyEventHandler((user) -> {
         update("discord.gg/evolution", "discord.gg/evolution");
      }).build();
      DiscordRPC.discordInitialize("1018825698007318619", handlers, true);
      (new 1(this, "Discord RPC Callback")).start();
   }

   public void shutdown() {
      this.running = false;
      DiscordRPC.discordShutdown();
   }

   public static void update(String firstLine, String secondLine) {
      net.arikia.dev.drpc.DiscordRichPresence.Builder b = new net.arikia.dev.drpc.DiscordRichPresence.Builder(secondLine);
      b.setBigImage("https://c.tenor.com/sVNO62-MYV0AAAAC/zxc-cat.gif", "botting.");
      b.setSmallImage("https://c.tenor.com/elj6q-hfJnIAAAAd/cat-%D0%BB%D0%B0%D0%B4%D0%BD%D0%BE.gif", "made by salam4ik.");
      b.setDetails(firstLine);
      b.setStartTimestamps(created);
      DiscordRPC.discordUpdatePresence(b.build());
   }

   // $FF: synthetic method
   static boolean access$000(Discord x0) {
      return x0.running;
   }
}
