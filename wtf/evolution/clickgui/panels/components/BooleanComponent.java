/* Decompiler 1ms, total 391ms, lines 31 */
package wtf.evolution.clickgui.panels.components;

import java.awt.Color;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.module.Module;
import wtf.evolution.settings.options.BooleanSetting;

public class BooleanComponent extends Component {
   public float x;
   public float y;
   public float width;
   public float height;
   public BooleanSetting setting;
   public Module module;

   public BooleanComponent(Module module, BooleanSetting s, float x, float y, float width, float height) {
      super(module, x, y, width, height);
      this.module = module;
      this.setting = s;
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
   }

   public void render(int mouseX, int mouseY) {
      super.render(mouseX, mouseY);
      RenderUtil.drawRectWH(this.x, this.y, this.width, this.height, (new Color(5, 5, 5, 150)).getRGB());
   }
}
