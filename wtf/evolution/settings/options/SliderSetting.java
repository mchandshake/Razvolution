/* Decompiler 2ms, total 356ms, lines 37 */
package wtf.evolution.settings.options;

import java.util.function.Supplier;
import wtf.evolution.module.Module;
import wtf.evolution.settings.Setting;

public class SliderSetting extends Setting {
   public float current;
   public float minimum;
   public float maximum;
   public float increment;
   public float sliderWidth;
   public boolean sliding;

   public SliderSetting(String name, float current, float minimum, float maximum, float increment) {
      this.name = name;
      this.minimum = minimum;
      this.current = current;
      this.maximum = maximum;
      this.increment = increment;
   }

   public float get() {
      return this.current;
   }

   public SliderSetting setHidden(Supplier<Boolean> hidden) {
      this.hidden = hidden;
      return this;
   }

   public SliderSetting call(Module module) {
      module.addSettings(new Setting[]{this});
      return this;
   }
}
