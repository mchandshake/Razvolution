/* Decompiler 4ms, total 356ms, lines 57 */
package wtf.evolution.helpers.file;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import net.minecraft.client.Minecraft;
import wtf.evolution.Main;

public class ClickGuiSave {
   public static File file;

   public static void save() {
      if (!file.exists()) {
         try {
            file.createNewFile();
         } catch (IOException var2) {
            throw new RuntimeException(var2);
         }
      }

      try {
         Files.write(file.toPath(), (Main.s.x + ":" + Main.s.y).getBytes(), new OpenOption[0]);
      } catch (IOException var1) {
         throw new RuntimeException(var1);
      }
   }

   public static void load() {
      if (!file.exists()) {
         System.out.println("No clickgui data found");
      } else {
         String[] data;
         try {
            data = (new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8)).split(":");
         } catch (IOException var3) {
            var3.printStackTrace();
            System.out.println("Failed to load clickgui");
            return;
         }

         try {
            Main.s.x = Integer.parseInt(data[0]);
            Main.s.y = Integer.parseInt(data[1]);
         } catch (Exception var2) {
            System.out.println("Failed to set gui position");
         }

      }
   }

   static {
      file = new File(Minecraft.getMinecraft().gameDir + "\\evolution\\clickgui.cfg");
   }
}
