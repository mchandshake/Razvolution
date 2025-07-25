/* Decompiler 4ms, total 2341ms, lines 54 */
package ru.salam4ik.bot.bot;

import io.netty.util.internal.ConcurrentSet;
import java.util.Set;
import ru.salam4ik.bot.bot.entity.BotController;
import ru.salam4ik.bot.bot.entity.BotPlayer;
import ru.salam4ik.bot.bot.network.BotNetwork;
import ru.salam4ik.bot.bot.network.BotPlayClient;
import ru.salam4ik.bot.bot.world.BotWorld;
import wtf.evolution.helpers.animation.Counter;

public class Bot {
   private final BotController controller;
   private final BotNetwork netManager;
   private final BotPlayer bot;
   public static Set<Bot> bots = new ConcurrentSet();
   private final BotWorld world;
   public Counter var_5cc = new Counter();
   private final BotPlayClient connection;
   public long systemTime = System.currentTimeMillis();

   public BotController getController() {
      return this.controller;
   }

   public Bot(BotNetwork var1, BotPlayClient var2, BotController var3, BotPlayer var4, BotWorld var5) {
      this.netManager = var1;
      this.connection = var2;
      this.controller = var3;
      this.bot = var4;
      this.world = var5;
   }

   public long getTime() {
      return System.currentTimeMillis() - this.systemTime;
   }

   public BotPlayClient getConnection() {
      return this.connection;
   }

   public BotWorld getWorld() {
      return this.world;
   }

   public BotNetwork getNetManager() {
      return this.netManager;
   }

   public BotPlayer getBot() {
      return this.bot;
   }
}
