/* Decompiler 2ms, total 381ms, lines 17 */
package ru.salam4ik.bot.bot.network;

import io.netty.channel.epoll.EpollEventLoopGroup;
import net.minecraft.util.LazyLoadBase;

final class BotNetwork$2 extends LazyLoadBase<EpollEventLoopGroup> {
   protected EpollEventLoopGroup load() {
      return new EpollEventLoopGroup();
   }

   // $FF: synthetic method
   // $FF: bridge method
   protected Object load() {
      return this.load();
   }
}
