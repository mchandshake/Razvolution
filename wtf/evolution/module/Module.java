/* Decompiler 24ms, total 356ms, lines 194 */
package wtf.evolution.module;

import com.google.gson.JsonObject;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import wtf.evolution.Main;
import wtf.evolution.editor.DragManager;
import wtf.evolution.event.EventManager;
import wtf.evolution.helpers.MusicHelper;
import wtf.evolution.helpers.animation.Animation;
import wtf.evolution.helpers.animation.impl.EaseInOutQuad;
import wtf.evolution.helpers.render.Translate;
import wtf.evolution.module.impl.Render.ClickGui;
import wtf.evolution.module.impl.Render.ClientSound;
import wtf.evolution.notifications.NotificationType;
import wtf.evolution.settings.Config;
import wtf.evolution.settings.Setting;
import wtf.evolution.settings.options.BooleanSetting;
import wtf.evolution.settings.options.ColorSetting;
import wtf.evolution.settings.options.ListSetting;
import wtf.evolution.settings.options.ModeSetting;
import wtf.evolution.settings.options.SliderSetting;

public class Module extends Config {
   public ModuleInfo info = (ModuleInfo)this.getClass().getAnnotation(ModuleInfo.class);
   public String name;
   public int bind;
   public boolean state;
   private String displayName;
   public Animation animation = new EaseInOutQuad(250, 1.0D);
   public Translate a = new Translate(0.0F, 0.0F);
   public Category category;
   public boolean opened = true;
   public boolean isRender = true;
   public Minecraft mc = Minecraft.getMinecraft();

   public Module() {
      this.name = this.info.name();
      this.bind = 0;
      this.category = this.info.type();
      this.displayName = this.name;
      this.state = false;
      System.out.println("Module: " + this.name + " loaded.");
   }

   public void onEnable() {
      if (Main.m.getModule(ClientSound.class).state && !(this instanceof ClickGui)) {
         MusicHelper.playSound("skidproject/enable.wav", 1.0F);
      }

      Main.c.saveConfig("default");
      DragManager.saveDragData();
      Main.notify.call(this.name, "module was enabled", NotificationType.INFO);
      EventManager.register(this);
   }

   public void onDisable() {
      if (Main.m.getModule(ClientSound.class).state && !(this instanceof ClickGui)) {
         MusicHelper.playSound("skidproject/disable.wav", 1.0F);
      }

      Main.c.saveConfig("default");
      DragManager.saveDragData();
      Main.notify.call(this.name, "module was disabled", NotificationType.INFO);
      EventManager.unregister(this);
   }

   public JsonObject save() {
      JsonObject object = new JsonObject();
      if (this.state) {
         object.addProperty("state", this.state);
      }

      if (this.bind > 0) {
         object.addProperty("keyIndex", this.bind);
      }

      JsonObject propertiesObject = new JsonObject();
      Iterator var3 = this.getSettings().iterator();

      while(true) {
         while(var3.hasNext()) {
            Setting set = (Setting)var3.next();
            if (set instanceof BooleanSetting) {
               propertiesObject.addProperty(set.name, ((BooleanSetting)set).get());
            } else if (set instanceof ModeSetting) {
               propertiesObject.addProperty(set.name, ((ModeSetting)set).currentMode);
            } else if (set instanceof SliderSetting) {
               propertiesObject.addProperty(set.name, ((SliderSetting)set).current);
            } else if (set instanceof ColorSetting) {
               propertiesObject.addProperty(set.name, ((ColorSetting)set).get());
            } else if (set instanceof ListSetting) {
               String out = "";

               String s;
               for(Iterator var6 = ((ListSetting)set).selected.iterator(); var6.hasNext(); out = out + s + ",") {
                  s = (String)var6.next();
               }

               propertiesObject.addProperty(set.name, out);
            }
         }

         object.add("Settings", propertiesObject);
         return object;
      }
   }

   public void setSuffix(String suffix) {
      this.displayName = this.name + (suffix.length() > 0 ? " " + ChatFormatting.GRAY + suffix : "");
   }

   public String getDisplayName() {
      return this.displayName;
   }

   public void load(JsonObject object) {
      if (object != null) {
         if (object.has("state")) {
            this.setState(object.get("state").getAsBoolean());
         }

         if (object.has("keyIndex")) {
            this.bind = object.get("keyIndex").getAsInt();
         }

         Iterator var2 = this.getSettings().iterator();

         while(true) {
            while(true) {
               Setting set;
               JsonObject propertiesObject;
               do {
                  do {
                     do {
                        if (!var2.hasNext()) {
                           return;
                        }

                        set = (Setting)var2.next();
                        propertiesObject = object.getAsJsonObject("Settings");
                     } while(set == null);
                  } while(propertiesObject == null);
               } while(!propertiesObject.has(set.name));

               if (set instanceof BooleanSetting) {
                  ((BooleanSetting)set).set(propertiesObject.get(set.name).getAsBoolean());
               } else if (set instanceof ModeSetting) {
                  ((ModeSetting)set).currentMode = propertiesObject.get(set.name).getAsString();
               } else if (set instanceof SliderSetting) {
                  ((SliderSetting)set).current = propertiesObject.get(set.name).getAsFloat();
               } else if (set instanceof ColorSetting) {
                  ((ColorSetting)set).color = propertiesObject.get(set.name).getAsInt();
               } else if (set instanceof ListSetting) {
                  String[] split = propertiesObject.get(set.name).getAsString().split(",");
                  ((ListSetting)set).selected = new ArrayList();
                  String[] var6 = split;
                  int var7 = split.length;

                  for(int var8 = 0; var8 < var7; ++var8) {
                     String s = var6[var8];
                     if (((ListSetting)set).list.contains(s)) {
                        ((ListSetting)set).selected.add(s);
                     }
                  }
               }
            }
         }
      }
   }

   public void toggle() {
      this.state = !this.state;
      if (this.state) {
         this.onEnable();
      } else {
         this.onDisable();
      }

   }

   public void setState(boolean state) {
      this.state = state;
      if (state) {
         this.onEnable();
      } else {
         this.onDisable();
      }

   }
}
