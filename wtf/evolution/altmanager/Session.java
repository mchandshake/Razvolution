/* Decompiler 1ms, total 251ms, lines 18 */
package wtf.evolution.altmanager;

import net.minecraft.client.Minecraft;

public class Session {
   public String nick;
   public String password;

   public Session(String nick) {
      this.nick = nick;
      this.session();
   }

   public void session() {
      Minecraft.getMinecraft().session = new net.minecraft.util.Session(this.nick, "", "", "mojang");
   }
}
