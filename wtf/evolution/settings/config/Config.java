/* Decompiler 4ms, total 333ms, lines 62 */
package wtf.evolution.settings.config;

import com.google.gson.JsonObject;
import java.io.File;
import java.util.Iterator;
import wtf.evolution.Main;
import wtf.evolution.module.Module;

public final class Config implements ConfigUpdater {
   private final String name;
   private final File file;

   public Config(String name) {
      this.name = name;
      this.file = new File(ConfigManager.configDirectory, name + ".json");
      if (!this.file.exists()) {
         try {
            this.file.createNewFile();
         } catch (Exception var3) {
         }
      }

   }

   public File getFile() {
      return this.file;
   }

   public String getName() {
      return this.name;
   }

   public JsonObject save() {
      JsonObject jsonObject = new JsonObject();
      JsonObject modulesObject = new JsonObject();
      new JsonObject();
      Iterator var4 = Main.m.m.iterator();

      while(var4.hasNext()) {
         Module module = (Module)var4.next();
         modulesObject.add(module.name, module.save());
      }

      jsonObject.add("Features", modulesObject);
      return jsonObject;
   }

   public void load(JsonObject object) {
      if (object.has("Features")) {
         JsonObject modulesObject = object.getAsJsonObject("Features");
         Iterator var3 = Main.m.m.iterator();

         while(var3.hasNext()) {
            Module module = (Module)var3.next();
            module.setState(false);
            module.load(modulesObject.getAsJsonObject(module.name));
         }
      }

   }
}
