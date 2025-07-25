/* Decompiler 3ms, total 453ms, lines 38 */
package ru.salam4ik.bot.bot;

import com.mojang.authlib.GameProfile;
import java.net.InetAddress;
import java.util.UUID;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.CPacketLoginStart;
import ru.salam4ik.bot.bot.network.BotLoginClient;
import ru.salam4ik.bot.bot.network.BotNetwork;
import wtf.evolution.bot.ProxyS.Proxy;

public class Session {
   public String user;
   public Proxy proxy;

   public Session(String user, Proxy proxy) {
      this.user = user;
      this.proxy = proxy;
   }

   public void join() {
      (new Thread(() -> {
         GameProfile gameProfile = new GameProfile(UUID.randomUUID(), this.user);

         try {
            BotNetwork botNetwork = BotNetwork.createNetworkManagerAndConnect(InetAddress.getByName(GuiConnecting.ip), GuiConnecting.port, this.proxy);
            botNetwork.setNetHandler(new BotLoginClient(botNetwork));
            botNetwork.sendPacket(new C00Handshake(GuiConnecting.ip, GuiConnecting.port, EnumConnectionState.LOGIN));
            botNetwork.sendPacket(new CPacketLoginStart(gameProfile));
         } catch (Exception var3) {
         }

      })).start();
   }
}
