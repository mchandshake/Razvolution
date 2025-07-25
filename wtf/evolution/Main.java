/* Decompiler 9ms, total 305ms, lines 137 */
package wtf.evolution;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import ru.salam4ik.bot.bot.BotStarter;
import wtf.evolution.altmanager.AltManager;
import wtf.evolution.altmanager.Session;
import wtf.evolution.bot.ProxyS;
import wtf.evolution.click.Screen;
import wtf.evolution.command.CommandManager;
import wtf.evolution.editor.Drag;
import wtf.evolution.editor.DragManager;
import wtf.evolution.helpers.ChangeLog;
import wtf.evolution.helpers.FriendManager;
import wtf.evolution.helpers.file.ClickGuiSave;
import wtf.evolution.helpers.render.TPS;
import wtf.evolution.module.Manager;
import wtf.evolution.module.Module;
import wtf.evolution.module.impl.Render.FeatureList;
import wtf.evolution.notifications.NotificationManager;
import wtf.evolution.settings.config.ConfigManager;

public class Main {
   public static Manager m;
   public static Screen s;
   public static boolean protectedd = false;
   public static Robot imageRobot;
   public static ProxyS proxy = new ProxyS();
   public static HashMap<String, String> nickHash = new HashMap();
   public static String apiKey = "c45f86bf4f16d61878944756f89e8ae2";
   public static NotificationManager notify;
   public static ConfigManager c;
   public static AltManager alt;
   public static FriendManager f;
   public static double balance;
   public static CommandManager d = new CommandManager();
   public static String username;
   public static ChangeLog changeLog = new ChangeLog();
   public static int uid;
   public static String till;

   public void load() {
      (new Discord()).start();
      proxy.start();
      username = "discord.gg/evolution";
      uid = 1337;
      till = "discord.gg/evolution";
      alt = new AltManager();

      try {
         imageRobot = new Robot();
      } catch (AWTException var2) {
         throw new RuntimeException(var2);
      }

      notify = new NotificationManager();
      m = new Manager();
      s = new Screen();
      c = new ConfigManager();
      DragManager.loadDragData();
      c.loadConfig("default");
      FeatureList.modules.addAll(m.m);
      new TPS();
      f = new FriendManager();
      ClickGuiSave.load();
      BotStarter.init();
   }

   public void unload() {
      c.saveConfig("default");
      DragManager.saveDragData();

      try {
         if (AltManager.file.exists()) {
            AltManager.file.delete();
         }

         FileWriter fr = new FileWriter(AltManager.file);
         Throwable var2 = null;

         try {
            Iterator var3 = AltManager.sessions.iterator();

            while(var3.hasNext()) {
               Session s = (Session)var3.next();
               fr.write(s.nick + "\n");
            }
         } catch (Throwable var13) {
            var2 = var13;
            throw var13;
         } finally {
            if (fr != null) {
               if (var2 != null) {
                  try {
                     fr.close();
                  } catch (Throwable var12) {
                     var2.addSuppressed(var12);
                  }
               } else {
                  fr.close();
               }
            }

         }
      } catch (Exception var15) {
      }

   }

   public static BufferedImage getScreenImage() {
      return imageRobot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
   }

   public static Drag createDrag(Module module, String name, float x, float y) {
      DragManager.draggables.put(name, new Drag(module, name, x, y));
      return (Drag)DragManager.draggables.get(name);
   }

   public static void keyEvent(int key) {
      Iterator var1 = Main.m.m.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         if (m.bind == key) {
            m.toggle();
         }
      }

   }
}
