/* Decompiler 1ms, total 326ms, lines 21 */
package wtf.evolution;

import net.arikia.dev.drpc.DiscordRPC;

class Discord$1 extends Thread {
   // $FF: synthetic field
   final Discord this$0;

   Discord$1(Discord this$0, String arg0) {
      super(arg0);
      this.this$0 = this$0;
   }

   public void run() {
      while(Discord.access$000(this.this$0)) {
         DiscordRPC.discordRunCallbacks();
      }

   }
}
