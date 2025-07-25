/* Decompiler 3ms, total 369ms, lines 43 */
package wtf.evolution.settings.options;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import wtf.evolution.helpers.animation.Animation;
import wtf.evolution.helpers.animation.impl.EaseInOutQuad;
import wtf.evolution.module.Module;
import wtf.evolution.settings.Setting;

public class ModeSetting extends Setting {
   public final List<String> modes;
   public String currentMode;
   public Animation animation = new EaseInOutQuad(200, 1.0D);
   public int index;
   public boolean opened;

   public ModeSetting(String name, String currentMode, String... options) {
      this.name = name;
      this.modes = Arrays.asList(options);
      this.index = this.modes.indexOf(currentMode);
      this.currentMode = (String)this.modes.get(this.index);
   }

   public String get() {
      return this.currentMode;
   }

   public boolean is(String mode) {
      return this.currentMode.equalsIgnoreCase(mode);
   }

   public ModeSetting setHidden(Supplier<Boolean> hidden) {
      this.hidden = hidden;
      return this;
   }

   public ModeSetting call(Module module) {
      module.addSettings(new Setting[]{this});
      return this;
   }
}
