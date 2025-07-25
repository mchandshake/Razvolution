/* Decompiler 27ms, total 1183ms, lines 278 */
package ru.salam4ik.bot.bot.network;

import com.google.common.collect.Queues;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.CodecException;
import io.netty.handler.timeout.TimeoutException;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.channels.ClosedChannelException;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.annotation.Nullable;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NettyCompressionDecoder;
import net.minecraft.network.NettyCompressionEncoder;
import net.minecraft.network.Packet;
import net.minecraft.network.ThreadQuickExitException;
import net.minecraft.util.ITickable;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.text.ITextComponent;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import ru.salam4ik.bot.bot.network.BotNetwork.1;
import ru.salam4ik.bot.bot.network.BotNetwork.2;
import ru.salam4ik.bot.bot.network.BotNetwork.3;
import wtf.evolution.bot.ProxyS.Proxy;

public class BotNetwork extends SimpleChannelInboundHandler<Packet<?>> {
   private boolean disconnected;
   private Channel channel;
   public String password;
   public static final AttributeKey<EnumConnectionState> PROTOCOL_ATTRIBUTE_KEY = AttributeKey.valueOf("protocol");
   public static final LazyLoadBase<NioEventLoopGroup> CLIENT_NIO_EVENTLOOP = new 1();
   private INetHandler packetListener;
   public boolean connection = false;
   public static final LazyLoadBase<EpollEventLoopGroup> CLIENT_EPOLL_EVENTLOOP = new 2();
   private final Queue<ru.salam4ik.bot.bot.network.BotNetwork.InboundHandlerTuplePacketListener> outboundPacketsQueue = Queues.newConcurrentLinkedQueue();
   private final EnumPacketDirection direction;
   private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
   public Proxy p;

   public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
      super.channelActive(channelHandlerContext);
      this.channel = channelHandlerContext.channel();

      try {
         this.setConnectionState(EnumConnectionState.HANDSHAKING);
      } catch (Throwable var3) {
      }

   }

   public static BotNetwork createNetworkManagerAndConnect(InetAddress inetAddress, int n, Proxy proxy) {
      BotNetwork botNetwork = new BotNetwork(EnumPacketDirection.CLIENTBOUND, proxy);
      ((Bootstrap)((Bootstrap)((Bootstrap)(new Bootstrap()).group((EventLoopGroup)CLIENT_NIO_EVENTLOOP.getValue())).handler(new 3(proxy, botNetwork))).channel(NioSocketChannel.class)).connect(inetAddress, n).syncUninterruptibly();
      return botNetwork;
   }

   private void dispatchPacket(Packet<?> packet, @Nullable GenericFutureListener<? extends Future<? super Void>>[] genericFutureListenerArray) {
      EnumConnectionState enumConnectionState = EnumConnectionState.getFromPacket(packet);
      EnumConnectionState enumConnectionState2 = (EnumConnectionState)this.channel.attr(PROTOCOL_ATTRIBUTE_KEY).get();
      if (enumConnectionState2 != enumConnectionState) {
         this.channel.config().setAutoRead(false);
      }

      if (this.channel.eventLoop().inEventLoop()) {
         if (enumConnectionState != enumConnectionState2) {
            this.setConnectionState(enumConnectionState);
         }

         ChannelFuture channelFuture = this.channel.writeAndFlush(packet);
         if (genericFutureListenerArray != null) {
            channelFuture.addListeners(genericFutureListenerArray);
         }

         channelFuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
      } else {
         this.channel.eventLoop().execute(() -> {
            if (enumConnectionState != enumConnectionState2) {
               this.setConnectionState(enumConnectionState);
            }

            ChannelFuture channelFuture = this.channel.writeAndFlush(packet);
            if (genericFutureListenerArray != null) {
               channelFuture.addListeners(genericFutureListenerArray);
            }

            channelFuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
         });
      }

   }

   public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
   }

   protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet<?> packet) throws Exception {
      if (this.channel.isOpen()) {
         try {
            packet.processPacket(this.packetListener);
         } catch (ThreadQuickExitException var4) {
         }
      }

   }

   public void sendPacket(Packet<?> packet) {
      if (this.isChannelOpen()) {
         this.flushOutboundQueue();
         this.dispatchPacket(packet, (GenericFutureListener[])null);
      } else {
         this.readWriteLock.writeLock().lock();

         try {
            this.outboundPacketsQueue.add(new ru.salam4ik.bot.bot.network.BotNetwork.InboundHandlerTuplePacketListener(packet, new GenericFutureListener[0]));
         } finally {
            this.readWriteLock.writeLock().unlock();
         }
      }

   }

   @SafeVarargs
   public final void sendPacket(Packet<?> packet, GenericFutureListener<? extends Future<? super Void>> genericFutureListener, GenericFutureListener<? extends Future<? super Void>>... genericFutureListenerArray) {
      if (this.isChannelOpen()) {
         this.flushOutboundQueue();
         this.dispatchPacket(packet, (GenericFutureListener[])ArrayUtils.add(genericFutureListenerArray, 0, genericFutureListener));
      } else {
         this.readWriteLock.writeLock().lock();

         try {
            this.outboundPacketsQueue.add(new ru.salam4ik.bot.bot.network.BotNetwork.InboundHandlerTuplePacketListener(packet, (GenericFutureListener[])ArrayUtils.add(genericFutureListenerArray, 0, genericFutureListener)));
         } finally {
            this.readWriteLock.writeLock().unlock();
         }
      }

   }

   public BotNetwork(EnumPacketDirection enumPacketDirection, Proxy p) {
      this.direction = enumPacketDirection;
      this.p = p;
   }

   public void setConnectionState(EnumConnectionState enumConnectionState) {
      this.channel.attr(PROTOCOL_ATTRIBUTE_KEY).set(enumConnectionState);
      this.channel.config().setAutoRead(true);
   }

   public INetHandler getNetHandler() {
      return this.packetListener;
   }

   public void setCompressionThreshold(int n) {
      if (n >= 0) {
         if (this.channel.pipeline().get("decompress") instanceof NettyCompressionDecoder) {
            ((NettyCompressionDecoder)this.channel.pipeline().get("decompress")).setCompressionThreshold(n);
         } else {
            this.channel.pipeline().addBefore("decoder", "decompress", new NettyCompressionDecoder(n));
         }

         if (this.channel.pipeline().get("compress") instanceof NettyCompressionEncoder) {
            ((NettyCompressionEncoder)this.channel.pipeline().get("compress")).setCompressionThreshold(n);
         } else {
            this.channel.pipeline().addBefore("encoder", "compress", new NettyCompressionEncoder(n));
         }
      } else {
         if (this.channel.pipeline().get("decompress") instanceof NettyCompressionDecoder) {
            this.channel.pipeline().remove("decompress");
         }

         if (this.channel.pipeline().get("compress") instanceof NettyCompressionEncoder) {
            this.channel.pipeline().remove("compress");
         }
      }

   }

   public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
      if (!(throwable instanceof CodecException) && !(throwable instanceof SocketException) && !(throwable instanceof TimeoutException) && !(throwable instanceof ClosedChannelException)) {
      }

   }

   public boolean isLocalChannel() {
      return this.channel instanceof LocalChannel || this.channel instanceof LocalServerChannel;
   }

   public void disableAutoRead() {
      this.channel.config().setAutoRead(false);
   }

   public void tick() {
      this.flushOutboundQueue();
      if (this.packetListener instanceof ITickable) {
         ((ITickable)this.packetListener).update();
      }

      if (this.channel != null) {
         this.channel.flush();
      }

   }

   public void handleDisconnection() {
      if (this.channel != null && !this.channel.isOpen() && !this.disconnected) {
         this.disconnected = true;
         this.getNetHandler().onDisconnect((ITextComponent)null);
      }

   }

   public void setNetHandler(INetHandler iNetHandler) {
      Validate.notNull(iNetHandler, "packetListener", new Object[0]);
      this.packetListener = iNetHandler;
   }

   public boolean isChannelOpen() {
      return this.channel != null && this.channel.isOpen();
   }

   public void closeChannel() {
      if (this.channel.isOpen()) {
         try {
            try {
               this.channel.close().sync();
            } catch (Exception var2) {
               this.channel.close();
            }
         } catch (Throwable var3) {
         }
      }

   }

   public boolean hasNoChannel() {
      return this.channel == null;
   }

   private void flushOutboundQueue() {
      if (this.channel != null && this.channel.isOpen()) {
         this.readWriteLock.readLock().lock();

         try {
            while(!this.outboundPacketsQueue.isEmpty()) {
               ru.salam4ik.bot.bot.network.BotNetwork.InboundHandlerTuplePacketListener inboundHandlerTuplePacketListener = (ru.salam4ik.bot.bot.network.BotNetwork.InboundHandlerTuplePacketListener)this.outboundPacketsQueue.poll();
               this.dispatchPacket(ru.salam4ik.bot.bot.network.BotNetwork.InboundHandlerTuplePacketListener.access$000(inboundHandlerTuplePacketListener), ru.salam4ik.bot.bot.network.BotNetwork.InboundHandlerTuplePacketListener.access$100(inboundHandlerTuplePacketListener));
            }
         } finally {
            this.readWriteLock.readLock().unlock();
         }
      }

   }

   // $FF: synthetic method
   // $FF: bridge method
   protected void channelRead0(ChannelHandlerContext var1, Object var2) throws Exception {
      this.channelRead0(var1, (Packet)var2);
   }
}
