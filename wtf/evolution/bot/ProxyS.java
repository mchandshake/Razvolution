/* Decompiler 10ms, total 330ms, lines 74 */
package wtf.evolution.bot;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ProxyS {
   protected final String[] proxyList = new String[]{"https://raw.githubusercontent.com/jetkai/proxy-list/main/online-proxies/txt/proxies-socks4.txt", "https://raw.githubusercontent.com/saschazesiger/Free-Proxies/master/proxies/socks4.txt", "https://raw.githubusercontent.com/monosans/proxy-list/main/proxies/socks4.txt", "https://raw.githubusercontent.com/ShiftyTR/Proxy-List/master/socks4.txt", "https://api.proxyscrape.com/v2/?request=displayproxies&protocol=socks4", "https://raw.githubusercontent.com/mmpx12/proxy-list/master/socks4.txt", "https://www.proxy-list.download/api/v1/get?type=socks4", "https://openproxylist.xyz/socks4.txt", "https://proxyspace.pro/socks4.txt"};
   public static boolean isPrivate = false;
   public int number;
   public final List<wtf.evolution.bot.ProxyS.Proxy> proxies = new ArrayList();

   public void start() {
      this.proxies.clear();
      System.out.println("[Scraper] Starting...");

      try {
         (new Thread(() -> {
            try {
               String[] var1 = this.proxyList;
               int var2 = var1.length;

               for(int var3 = 0; var3 < var2; ++var3) {
                  String proxyMap = var1[var3];
                  Document proxyList = Jsoup.connect(proxyMap).ignoreHttpErrors(true).get();
                  String[] var6 = proxyList.text().split(" ");
                  int var7 = var6.length;

                  for(int var8 = 0; var8 < var7; ++var8) {
                     String proxy = var6[var8];
                     if (isPrivate) {
                        int port = '궜';
                        this.proxies.add(new wtf.evolution.bot.ProxyS.Proxy(wtf.evolution.bot.ProxyS.ProxyType.SOCKS5, new InetSocketAddress(proxy, port)));
                     } else {
                        String[] proxySplit = proxy.split(":");
                        if (proxySplit.length >= 2) {
                           this.proxies.add(new wtf.evolution.bot.ProxyS.Proxy(wtf.evolution.bot.ProxyS.ProxyType.SOCKS4, new InetSocketAddress(proxy.split(":")[0], Integer.parseInt(proxy.split(":")[1]))));
                        }
                     }
                  }
               }
            } catch (Throwable var12) {
               var12.printStackTrace();
            }

         })).run();
         System.out.println("[Scraper] Scraped " + this.proxies.size() + " proxy's");
      } catch (Exception var2) {
      }

   }

   public wtf.evolution.bot.ProxyS.Proxy getProxy() {
      ++this.number;
      if (this.number > this.proxies.size() - 1) {
         this.number = 0;
      }

      return (wtf.evolution.bot.ProxyS.Proxy)this.proxies.get(this.number);
   }

   public wtf.evolution.bot.ProxyS.ProxyType getProxyType(String url) {
      if (url.contains("socks4")) {
         return wtf.evolution.bot.ProxyS.ProxyType.SOCKS4;
      } else if (url.contains("socks5")) {
         return wtf.evolution.bot.ProxyS.ProxyType.SOCKS5;
      } else {
         return !url.contains("/http") && !url.contains("=http") ? null : wtf.evolution.bot.ProxyS.ProxyType.HTTP;
      }
   }
}
