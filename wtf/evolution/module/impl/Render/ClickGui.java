/* Decompiler 2ms, total 323ms, lines 34 */
package wtf.evolution.module.impl.Render;

import wtf.evolution.Main;
import wtf.evolution.helpers.file.ClickGuiSave;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.Setting;
import wtf.evolution.settings.options.ColorSetting;

@ModuleInfo(
   name = "ClickGui",
   type = Category.Render
)
public class ClickGui extends Module {
   public ColorSetting color = new ColorSetting("Click Gui Color", -1);

   public ClickGui() {
      this.bind = 54;
      this.addSettings(new Setting[]{this.color});
   }

   public void onEnable() {
      super.onEnable();
      this.mc.displayGuiScreen(Main.s);
      ClickGuiSave.save();
      this.toggle();
   }

   public static int getColor() {
      return ((ClickGui)Main.m.getModule(ClickGui.class)).color.get();
   }
}
