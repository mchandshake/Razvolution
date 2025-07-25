/* Decompiler 1ms, total 309ms, lines 16 */
package wtf.evolution.module.impl.Render;

import java.awt.Color;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.ColorSetting;

@ModuleInfo(
   name = "WorldColor",
   type = Category.Render
)
public class WorldColor extends Module {
   public ColorSetting color = (new ColorSetting("Color", (new Color(255, 255, 255)).getRGB())).call(this);
}
