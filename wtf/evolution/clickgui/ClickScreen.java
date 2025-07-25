/* Decompiler 5ms, total 278ms, lines 44 */
package wtf.evolution.clickgui;

import java.io.IOException;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiScreen;
import wtf.evolution.clickgui.panels.Panel;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.module.Category;

public class ClickScreen extends GuiScreen {
   public ArrayList<Panel> panels = new ArrayList();

   public ClickScreen() {
      int y = 5;
      int x = 5;
      Category[] var3 = Category.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Category category = var3[var5];
         this.panels.add(new Panel(category, (float)x, (float)y));
         x += 130;
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      super.drawScreen(mouseX, mouseY, partialTicks);
      this.panels.forEach((panel) -> {
         panel.render(mouseX, mouseY);
      });
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.mouseClicked(mouseX, mouseY, mouseButton);
      this.panels.forEach((panel) -> {
         if (RenderUtil.isHovered((float)mouseX, (float)mouseY, panel.x, panel.y, 125.0F, panel.getHeight())) {
            panel.click(mouseX, mouseY, mouseButton);
         }

      });
   }
}
