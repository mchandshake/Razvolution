/* Decompiler 7ms, total 327ms, lines 78 */
package wtf.evolution.clickgui.panels.components;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import wtf.evolution.clickgui.panels.Panel;
import wtf.evolution.helpers.font.Fonts;
import wtf.evolution.module.Module;
import wtf.evolution.module.impl.Render.ClickGui;
import wtf.evolution.settings.Setting;
import wtf.evolution.settings.options.BooleanSetting;

public class ModuleComponent extends Component {
   public Module module;
   public float x;
   public float y;
   public float width;
   public float height;
   public List<Component> components = new ArrayList();
   public float offset;
   public Panel p;
   public boolean opened = true;

   public ModuleComponent(Module module, Panel p, float x, float y, float width, float height) {
      super(module, x, y, width, height);
      this.p = p;
      this.module = module;
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      int yOffset = 16;
      Iterator var8 = module.getSettings().iterator();

      while(var8.hasNext()) {
         Setting c = (Setting)var8.next();
         if (c instanceof BooleanSetting) {
            this.components.add(new BooleanComponent(module, (BooleanSetting)c, x, y + (float)yOffset, width, height));
            yOffset += 16;
         }
      }

      this.opened = true;
   }

   public void render(int mouseX, int mouseY) {
      if (this.module.state) {
         Fonts.RUB14.drawCenteredBlurredString(this.module.name, (double)(this.x + 62.5F), (double)(this.y + 6.0F), 13, new Color(ClickGui.getColor()), this.module.state ? ClickGui.getColor() : -1);
      } else {
         Fonts.RUB14.drawCenteredString(this.module.name, this.x + 62.5F, this.y + 6.0F, -1);
      }

      this.components.forEach((component) -> {
         if (this.opened) {
            component.render(mouseX, mouseY);
            this.height += 16.0F;
         }

      });
      super.render(mouseX, mouseY);
   }

   public void setY(float y) {
      this.y = y;
   }

   public void click(int mouseX, int mouseY, int button) {
      super.click(mouseX, mouseY, button);
      if (button == 1) {
         this.opened = !this.opened;
      } else {
         this.module.toggle();
      }

   }
}
