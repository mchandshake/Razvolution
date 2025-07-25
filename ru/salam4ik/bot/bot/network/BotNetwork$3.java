/* Decompiler 3ms, total 305ms, lines 61 */
package ru.salam4ik.bot.bot.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.handler.proxy.Socks4ProxyHandler;
import io.netty.handler.proxy.Socks5ProxyHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.NettyPacketDecoder;
import net.minecraft.network.NettyPacketEncoder;
import net.minecraft.network.NettyVarint21FrameDecoder;
import net.minecraft.network.NettyVarint21FrameEncoder;
import wtf.evolution.bot.ProxyS;
import wtf.evolution.bot.ProxyS.Proxy;
import wtf.evolution.bot.ProxyS.ProxyType;

final class BotNetwork$3 extends ChannelInitializer {
   // $FF: synthetic field
   final Proxy val$proxy;
   // $FF: synthetic field
   final BotNetwork val$botNetwork;

   BotNetwork$3(Proxy var1, BotNetwork var2) {
      this.val$proxy = var1;
      this.val$botNetwork = var2;
   }

   protected void initChannel(Channel channel) {
      try {
         channel.config().setOption(ChannelOption.TCP_NODELAY, true);
      } catch (ChannelException var3) {
      }

      if (this.val$proxy != null) {
         Socks5ProxyHandler socks5;
         if (!ProxyS.isPrivate) {
            if (this.val$proxy.getType() == ProxyType.SOCKS5) {
               socks5 = new Socks5ProxyHandler(this.val$proxy.getAddress());
               socks5.setConnectTimeoutMillis(9500L);
               channel.pipeline().addLast(new ChannelHandler[]{socks5});
            }

            if (this.val$proxy.getType() == ProxyType.SOCKS4) {
               Socks4ProxyHandler socks4 = new Socks4ProxyHandler(this.val$proxy.getAddress());
               socks4.setConnectTimeoutMillis(9500L);
               channel.pipeline().addLast(new ChannelHandler[]{socks4});
            }
         } else if (this.val$proxy.getType() == ProxyType.SOCKS5) {
            socks5 = new Socks5ProxyHandler(this.val$proxy.getAddress(), "i700rp1020412", "z2zbuyfgtv");
            socks5.setConnectTimeoutMillis(9500L);
            channel.pipeline().addLast(new ChannelHandler[]{socks5});
         }
      }

      channel.pipeline().addLast("timeout", new ReadTimeoutHandler(30)).addLast("splitter", new NettyVarint21FrameDecoder()).addLast("decoder", new NettyPacketDecoder(EnumPacketDirection.CLIENTBOUND)).addLast("prepender", new NettyVarint21FrameEncoder()).addLast("encoder", new NettyPacketEncoder(EnumPacketDirection.SERVERBOUND)).addLast("packet_handler", this.val$botNetwork);
   }
}
