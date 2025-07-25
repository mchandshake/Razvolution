/* Decompiler 3ms, total 278ms, lines 39 */
package ru.salam4ik.bot.bot.network;

import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.login.INetHandlerLoginClient;
import net.minecraft.network.login.server.SPacketDisconnect;
import net.minecraft.network.login.server.SPacketEnableCompression;
import net.minecraft.network.login.server.SPacketEncryptionRequest;
import net.minecraft.network.login.server.SPacketLoginSuccess;
import net.minecraft.util.text.ITextComponent;

public final class BotLoginClient implements INetHandlerLoginClient {
   private final BotNetwork networkManager;

   public BotLoginClient(BotNetwork botNetwork) {
      this.networkManager = botNetwork;
   }

   public void handleLoginSuccess(SPacketLoginSuccess sPacketLoginSuccess) {
      this.networkManager.setConnectionState(EnumConnectionState.PLAY);
      this.networkManager.setNetHandler(new BotPlayClient(this.networkManager, sPacketLoginSuccess.getProfile()));
   }

   public void handleEncryptionRequest(SPacketEncryptionRequest sPacketEncryptionRequest) {
   }

   public void handleEnableCompression(SPacketEnableCompression sPacketEnableCompression) {
      if (!this.networkManager.isLocalChannel()) {
         this.networkManager.setCompressionThreshold(sPacketEnableCompression.getCompressionThreshold());
      }

   }

   public void handleDisconnect(SPacketDisconnect sPacketDisconnect) {
   }

   public void onDisconnect(ITextComponent iTextComponent) {
   }
}
