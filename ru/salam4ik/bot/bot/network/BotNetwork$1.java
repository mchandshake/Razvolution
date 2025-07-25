/* Decompiler 2ms, total 256ms, lines 17 */
package ru.salam4ik.bot.bot.network;

import io.netty.channel.nio.NioEventLoopGroup;
import net.minecraft.util.LazyLoadBase;

final class BotNetwork$1 extends LazyLoadBase<NioEventLoopGroup> {
   protected NioEventLoopGroup load() {
      return new NioEventLoopGroup();
   }

   // $FF: synthetic method
   // $FF: bridge method
   protected Object load() {
      return this.load();
   }
}
