/* Decompiler 2ms, total 264ms, lines 23 */
package wtf.evolution.bot;

import java.net.InetSocketAddress;
import wtf.evolution.bot.ProxyS.ProxyType;

public class ProxyS$Proxy {
   private final ProxyType type;
   private final InetSocketAddress address;

   public ProxyS$Proxy(ProxyType type, InetSocketAddress address) {
      this.type = type;
      this.address = address;
   }

   public ProxyType getType() {
      return this.type;
   }

   public InetSocketAddress getAddress() {
      return this.address;
   }
}
