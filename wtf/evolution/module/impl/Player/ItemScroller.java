/* Decompiler 1ms, total 281ms, lines 15 */
package wtf.evolution.module.impl.Player;

import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.SliderSetting;

@ModuleInfo(
   name = "ItemScroller",
   type = Category.Player
)
public class ItemScroller extends Module {
   public SliderSetting delay = (new SliderSetting("Delay", 10.0F, 0.0F, 500.0F, 1.0F)).call(this);
}
