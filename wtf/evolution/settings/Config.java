/* Decompiler 4ms, total 441ms, lines 24 */
package wtf.evolution.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Config {
   private final ArrayList<Setting> settingList = new ArrayList();

   public final void addSettings(Setting... options) {
      this.settingList.addAll(Arrays.asList(options));
   }

   public final List<Setting> getSettingsForGUI() {
      return (List)this.settingList.stream().filter((setting) -> {
         return !(Boolean)setting.hidden.get();
      }).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
   }

   public final List<Setting> getSettings() {
      return this.settingList;
   }
}
