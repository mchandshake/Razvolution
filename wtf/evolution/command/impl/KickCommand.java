/* Decompiler 4ms, total 349ms, lines 44 */
package wtf.evolution.command.impl;

import com.mojang.authlib.GameProfile;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.CPacketLoginStart;
import net.minecraft.server.network.NetHandlerHandshakeTCP;
import wtf.evolution.command.Command;
import wtf.evolution.command.CommandInfo;
import wtf.evolution.helpers.ChatUtil;

@CommandInfo(
   name = "kick"
)
public class KickCommand extends Command {
   public void execute(String[] args) {
      super.execute(args);
      (new Thread(() -> {
         String ip = ((InetSocketAddress)this.mc.player.connection.getNetworkManager().getRemoteAddress()).getAddress().getHostAddress();
         String ip2 = ((InetSocketAddress)this.mc.player.connection.getNetworkManager().getRemoteAddress()).getAddress().getHostName();
         int port = ((InetSocketAddress)this.mc.player.connection.getNetworkManager().getRemoteAddress()).getPort();
         InetAddress inetaddress = null;

         try {
            inetaddress = InetAddress.getByName(ip);
         } catch (UnknownHostException var7) {
         }

         GuiConnecting.networkManager = NetworkManager.createNetworkManagerAndConnect(inetaddress, port, Minecraft.getMinecraft().gameSettings.isUsingNativeTransport());
         GuiConnecting.networkManager.setNetHandler(new NetHandlerHandshakeTCP(this.mc.player.getServer(), GuiConnecting.networkManager));
         GuiConnecting.networkManager.sendPacket(new C00Handshake(ip2, port, EnumConnectionState.LOGIN));
         GuiConnecting.networkManager.sendPacket(new CPacketLoginStart(new GameProfile((UUID)null, args[1])));
         ChatUtil.print("Kicked " + args[1]);
      })).start();
   }
}
