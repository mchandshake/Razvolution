/* Decompiler 8ms, total 487ms, lines 67 */
package wtf.evolution.helpers;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.util.Iterator;
import java.util.Map.Entry;
import org.lwjgl.input.Mouse;
import wtf.evolution.helpers.font.Fonts;
import wtf.evolution.helpers.math.MathHelper;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.helpers.render.RoundedUtil;
import wtf.evolution.model.models.HashMapWithDefault;

public class ChangeLog {
   public HashMapWithDefault<String, wtf.evolution.helpers.ChangeLog.Type> update = new HashMapWithDefault();
   public double scroll = 0.0D;
   public double scrollA = 0.0D;

   public ChangeLog() {
      this.update.put("KillAura - удары идут лучше", wtf.evolution.helpers.ChangeLog.Type.FIXED);
      this.update.put("Jesus - работает лучше", wtf.evolution.helpers.ChangeLog.Type.FIXED);
      this.update.put("Spider - SunRise", wtf.evolution.helpers.ChangeLog.Type.FIXED);
      this.update.put("AutoLeave - выходит с сервера когда рядом игрок", wtf.evolution.helpers.ChangeLog.Type.NEW);
      this.update.put("GAppleTimer - показывает кулдаун яблока", wtf.evolution.helpers.ChangeLog.Type.NEW);
      this.update.put("SuperKnockback - дает больше велосити игроку", wtf.evolution.helpers.ChangeLog.Type.NEW);
      this.update.put("Freecam - позволяет летать в прогруженных чанках", wtf.evolution.helpers.ChangeLog.Type.NEW);
      this.update.put("InventoryDroper - выкидывает весь инвентарь", wtf.evolution.helpers.ChangeLog.Type.NEW);
      this.update.put("ClientSound - звуки при включении модлуя", wtf.evolution.helpers.ChangeLog.Type.NEW);
      this.update.put("HighJump - высокий прыжок", wtf.evolution.helpers.ChangeLog.Type.NEW);
      this.update.put("WebLeave - высокий прыжок когда ты в паутине", wtf.evolution.helpers.ChangeLog.Type.NEW);
   }

   public void render() {
      float maxWidth = (float)(Fonts.RUB14.getStringWidth((String)this.update.keySet().stream().max((s1, s2) -> {
         return Fonts.RUB14.getStringWidth(s1) > Fonts.RUB14.getStringWidth(s2) ? 1 : -1;
      }).get()) + 25);
      this.scroll += (double)((float)Mouse.getDWheel() / 120.0F * 10.0F);
      this.scrollA = MathHelper.interpolate(this.scroll, this.scrollA, 0.2D);
      this.scroll = (double)MathHelper.clamp((float)this.scroll, 0.0F, (float)(this.update.size() * 10));
      if (this.update.size() * 10 < 300) {
         this.scroll = 0.0D;
      }

      RenderUtil.bloom(() -> {
         RoundedUtil.drawRoundOutline(5.0F, 30.0F, maxWidth, 300.0F, 5.0F, 0.01F, new Color(20, 20, 20, 150), Color.WHITE);
      }, 5.0F, 2.0F, Color.BLACK.getRGB());
      RoundedUtil.drawRoundOutline(5.0F, 30.0F, maxWidth, 300.0F, 5.0F, 0.01F, new Color(10, 10, 10, 150), Color.WHITE);
      double y = 40.0D + this.scrollA;
      StencilUtil.initStencilToWrite();
      RoundedUtil.drawRoundOutline(7.0F, 32.0F, maxWidth - 6.0F, 295.0F, 5.0F, 0.1F, new Color(20, 20, 20, 150), Color.WHITE);
      StencilUtil.readStencilBuffer(1);

      for(Iterator var4 = this.update.entrySet().iterator(); var4.hasNext(); y += 10.0D) {
         Entry<String, wtf.evolution.helpers.ChangeLog.Type> entry = (Entry)var4.next();
         if (((wtf.evolution.helpers.ChangeLog.Type)entry.getValue()).name().equalsIgnoreCase("NEW")) {
            Fonts.RUB14.drawString(ChatFormatting.GRAY + "[" + ChatFormatting.GREEN + "+" + ChatFormatting.GRAY + "] " + ChatFormatting.RESET + (String)entry.getKey(), 10.0D, y, -1);
         } else if (((wtf.evolution.helpers.ChangeLog.Type)entry.getValue()).name().equalsIgnoreCase("FIXED")) {
            Fonts.RUB14.drawString(ChatFormatting.GRAY + "[" + ChatFormatting.YELLOW + "/" + ChatFormatting.GRAY + "] " + ChatFormatting.RESET + (String)entry.getKey(), 10.0D, y, -1);
         } else if (((wtf.evolution.helpers.ChangeLog.Type)entry.getValue()).name().equalsIgnoreCase("REMOVED")) {
            Fonts.RUB14.drawString(ChatFormatting.GRAY + "[" + ChatFormatting.RED + "-" + ChatFormatting.GRAY + "] " + ChatFormatting.RESET + (String)entry.getKey(), 10.0D, y, -1);
         }
      }

      StencilUtil.uninitStencilBuffer();
   }
}
