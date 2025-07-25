/* Decompiler 2ms, total 792ms, lines 27 */
package ru.salam4ik.bot.bot.network;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.Packet;

class BotNetwork$InboundHandlerTuplePacketListener {
   private final GenericFutureListener<? extends Future<? super Void>>[] listener;
   private final Packet<?> packet;

   @SafeVarargs
   public BotNetwork$InboundHandlerTuplePacketListener(Packet<?> packet, GenericFutureListener<? extends Future<? super Void>>... genericFutureListenerArray) {
      this.packet = packet;
      this.listener = genericFutureListenerArray;
   }

   // $FF: synthetic method
   static Packet access$000(BotNetwork$InboundHandlerTuplePacketListener x0) {
      return x0.packet;
   }

   // $FF: synthetic method
   static GenericFutureListener[] access$100(BotNetwork$InboundHandlerTuplePacketListener x0) {
      return x0.listener;
   }
}
