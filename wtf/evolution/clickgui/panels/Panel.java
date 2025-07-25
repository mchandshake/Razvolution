/* Decompiler 15ms, total 315ms, lines 94 */
package wtf.evolution.clickgui.panels;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.gui.Gui;
import wtf.evolution.Main;
import wtf.evolution.clickgui.panels.components.Component;
import wtf.evolution.clickgui.panels.components.ModuleComponent;
import wtf.evolution.helpers.StencilUtil;
import wtf.evolution.helpers.font.Fonts;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.helpers.render.RoundedUtil;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;

public class Panel {
   public Category category;
   public float x;
   public float y;
   public ArrayList<Component> components = new ArrayList();
   public float height;

   public Panel(Category category, float x, float y) {
      this.category = category;
      this.x = x;
      this.y = y;
      int yOffset = 20;

      for(Iterator var5 = Main.m.getModulesFromCategory(category).iterator(); var5.hasNext(); yOffset += 16) {
         Module module = (Module)var5.next();
         this.components.add(new ModuleComponent(module, this, x, y + (float)yOffset, 125.0F, 16.0F));
      }

   }

   public void render(int mouseX, int mouseY) {
      Iterator var3 = this.components.iterator();

      while(var3.hasNext()) {
         Component component = (Component)var3.next();
         if (component instanceof ModuleComponent) {
            ModuleComponent moduleComponent = (ModuleComponent)component;
            if (moduleComponent.opened) {
               this.height = moduleComponent.offset + 20.0F;
            }
         }
      }

      RenderUtil.blur(() -> {
         RoundedUtil.drawRound(this.x, this.y, 125.0F, this.getHeight(), 5.0F, new Color(20, 20, 20, 150));
      }, 10.0F);
      RenderUtil.bloom(() -> {
         RoundedUtil.drawRound(this.x, this.y, 125.0F, this.getHeight(), 5.0F, new Color(20, 20, 20, 150));
      }, 5.0F, 2.0F, 1);
      RoundedUtil.drawRound(this.x, this.y, 125.0F, this.getHeight(), 5.0F, new Color(10, 10, 10, 220));
      Fonts.RUB14.drawCenteredString(this.category.name(), this.x + 62.5F, this.y + 7.0F, -1);
      Gui.drawGradientRect(this.x, this.y + 17.0F, this.x + 125.0F, this.y + 25.0F, (new Color(0, 0, 0, 100)).getRGB(), (new Color(0, 0, 0, 0)).getRGB());
      StencilUtil.initStencilToWrite();
      RoundedUtil.drawRound(this.x, this.y, 125.0F, this.getHeight(), 5.0F, new Color(10, 10, 10, 180));
      StencilUtil.readStencilBuffer(1);
      this.components.forEach((componentx) -> {
         componentx.render(mouseX, mouseY);
      });
      StencilUtil.uninitStencilBuffer();
   }

   public float getHeight() {
      float height = 20.0F;
      Iterator var2 = this.components.iterator();

      while(var2.hasNext()) {
         Component component = (Component)var2.next();
         if (component instanceof ModuleComponent) {
            ModuleComponent moduleComponent = (ModuleComponent)component;
            if (moduleComponent.opened) {
               height = moduleComponent.offset + 20.0F;
            }
         }
      }

      return height;
   }

   public void click(int mouseX, int mouseY, int button) {
      this.components.forEach((component) -> {
         if (RenderUtil.isHovered((float)mouseX, (float)mouseY, component.x, component.y, component.width, component.height)) {
            component.click(mouseX, mouseY, button);
         }

      });
   }
}
