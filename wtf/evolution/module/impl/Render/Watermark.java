/* Decompiler 4ms, total 302ms, lines 39 */
package wtf.evolution.module.impl.Render;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import wtf.evolution.Main;
import wtf.evolution.editor.Drag;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventDisplay;
import wtf.evolution.helpers.font.Fonts;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;

@ModuleInfo(
   name = "Watermark",
   type = Category.Render
)
public class Watermark extends Module {
   private int tick;
   public Drag d = Main.createDrag(this, "watermark", 4.0F, 4.0F);

   @EventTarget
   public void onRender(EventDisplay e) {
      String ping = String.valueOf(this.mc.getConnection().getPlayerInfo(this.mc.player.getName()).getResponseTime());
      String text = "BY_STILIST1CK" + ChatFormatting.GRAY + " | " + ChatFormatting.RESET + Main.username + ChatFormatting.GRAY + " | " + ChatFormatting.RESET + (this.mc.isSingleplayer() ? "localhost" : this.mc.getCurrentServerData().serverIP) + ChatFormatting.GRAY + " | " + ChatFormatting.RESET + ping + "ms";
      float width = (float)(Fonts.REG16.getStringWidth(text) + 6);
      int xx = (int)this.d.getX();
      int yy = (int)this.d.getY();
      this.d.setWidth(width);
      this.d.setHeight(12.0F);
      RenderUtil.drawBlurredShadow((float)xx, (float)yy, width, 12.0F, 15, new Color(20, 20, 20));
      RenderUtil.drawRectWH((float)xx, (float)yy, width, 12.0F, (new Color(20, 20, 20)).getRGB());
      RenderUtil.drawBlurredShadow((float)xx, (float)yy, width, 1.0F, 15, new Color(105, 157, 253));
      RenderUtil.drawRectWH((float)xx, (float)yy, width, 0.9F, (new Color(105, 157, 253)).getRGB());
      Fonts.REG16.drawStringWithShadow(text, (float)(xx + 3), (float)yy + 3.0F, -1);
   }
}
