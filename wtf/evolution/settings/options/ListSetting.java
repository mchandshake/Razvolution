/* Decompiler 1ms, total 309ms, lines 30 */
package wtf.evolution.settings.options;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import wtf.evolution.module.Module;
import wtf.evolution.settings.Setting;

public class ListSetting extends Setting {
   public List<String> list;
   public boolean opened;
   public List<String> selected = new ArrayList();

   public ListSetting(String name, String... settings) {
      this.name = name;
      this.list = Arrays.asList(settings);
   }

   public ListSetting call(Module module) {
      module.addSettings(new Setting[]{this});
      return this;
   }

   public ListSetting setHidden(Supplier<Boolean> hidden) {
      this.hidden = hidden;
      return this;
   }
}
