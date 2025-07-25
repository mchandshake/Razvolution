/* Decompiler 4ms, total 294ms, lines 37 */
package wtf.evolution.click;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import wtf.evolution.helpers.StencilUtil;
import wtf.evolution.helpers.font.Fonts;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.helpers.render.RoundedUtil;

public class BotScreen extends GuiScreen {
   float width = 450.0F;
   float height = 280.0F;

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      super.drawScreen(mouseX, mouseY, partialTicks);
      ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
      float finalWidth = this.width;
      float finalHeight = this.height;
      RenderUtil.blur(() -> {
         RoundedUtil.drawRound((float)sr.getScaledWidth() / 2.0F - finalWidth / 2.0F, (float)sr.getScaledHeight() / 2.0F - finalHeight / 2.0F, finalWidth, finalHeight, 2.0F, new Color(20, 20, 20, 150));
      }, 5.0F);
      RoundedUtil.drawRound((float)sr.getScaledWidth() / 2.0F - this.width / 2.0F, (float)sr.getScaledHeight() / 2.0F - this.height / 2.0F, this.width, this.height, 2.0F, new Color(20, 20, 20, 150));
      float width = this.width - 2.0F;
      float height = this.height - 2.0F;
      StencilUtil.initStencilToWrite();
      RenderUtil.drawRectWH((float)sr.getScaledWidth() / 2.0F - width / 2.0F, (float)sr.getScaledHeight() / 2.0F - height / 2.0F, width, height, (new Color(20, 20, 20, 150)).getRGB());
      StencilUtil.readStencilBuffer(0);
      RenderUtil.drawBlurredShadow((float)sr.getScaledWidth() / 2.0F - width / 2.0F, (float)sr.getScaledHeight() / 2.0F - height / 2.0F, width, height, 15, new Color(0, 0, 0, 255));
      StencilUtil.uninitStencilBuffer();
      float x = (float)sr.getScaledWidth() / 2.0F - width / 2.0F;
      float y = (float)sr.getScaledHeight() / 2.0F - height / 2.0F;
      Fonts.RUB14.drawCenteredString("Evolution Web", x + width / 2.0F, y - 10.0F, (new Color(255, 255, 255, 100)).getRGB());
   }
}
