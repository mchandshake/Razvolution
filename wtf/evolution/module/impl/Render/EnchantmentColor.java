/* Decompiler 1ms, total 421ms, lines 16 */
package wtf.evolution.module.impl.Render;

import java.awt.Color;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.ColorSetting;

@ModuleInfo(
   name = "EnchantmentColor",
   type = Category.Render
)
public class EnchantmentColor extends Module {
   public ColorSetting enchantColor = (new ColorSetting("Color", (new Color(120, 210, 210)).getRGB())).call(this);
}
