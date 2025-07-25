/* Decompiler 9ms, total 283ms, lines 59 */
package wtf.evolution.notifications;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import wtf.evolution.helpers.ScaleUtil;
import wtf.evolution.helpers.font.Fonts;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.helpers.render.RoundedUtil;
import wtf.evolution.helpers.render.Translate;

public class NotificationManager {
   public ArrayList<Notification> notifications = new ArrayList();

   public void call(String head, String text, NotificationType type) {
      ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
      Notification n = new Notification(text, type, head);
      n.translate = new Translate((float)(ScaleUtil.calc(sr.getScaledWidth()) - 50), (float)(ScaleUtil.calc(sr.getScaledHeight()) - 10));
      this.notifications.add(n);
   }

   public void render() {
      ScaleUtil.scale_pre();
      int offset = 30;
      byte x = 5;

      try {
         Iterator var3 = this.notifications.iterator();

         while(var3.hasNext()) {
            Notification n = (Notification)var3.next();
            if (System.currentTimeMillis() - n.startTime <= 2100L) {
               ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
               if (System.currentTimeMillis() - n.startTime > 2000L) {
                  n.translate.interpolate((double)(ScaleUtil.calc(sr.getScaledWidth()) + 100), (double)(ScaleUtil.calc(sr.getScaledHeight()) - offset), (double)(Minecraft.getMinecraft().deltaTime() * 10.0F));
               } else {
                  n.translate.interpolate((double)(ScaleUtil.calc(sr.getScaledWidth()) - 115), (double)(ScaleUtil.calc(sr.getScaledHeight()) - offset), (double)(Minecraft.getMinecraft().deltaTime() * 10.0F));
               }

               RenderUtil.drawBlurredShadow((float)Math.round((float)n.translate.getX()), (float)Math.round((float)n.translate.getY()), 110.0F, 25.0F, 15, Color.BLACK);
               RoundedUtil.drawRound((float)Math.round((float)n.translate.getX()), (float)Math.round((float)n.translate.getY()), 110.0F, 25.0F, 2.0F, new Color(20, 20, 20, 255));
               Fonts.REG16.drawString(n.header, (float)Math.round((float)n.translate.getX() + (float)x), (float)Math.round((float)n.translate.getY() + 5.0F), (new Color(255, 255, 255, 255)).getRGB());
               Fonts.RUB14.drawString(n.text, (float)Math.round((float)n.translate.getX() + (float)x), (float)Math.round((float)n.translate.getY() + 15.0F), (new Color(255, 255, 255, 255)).getRGB());
               offset += 30;
            }
         }
      } catch (Exception var6) {
      }

      if (this.notifications.size() >= 20) {
         this.notifications.remove(0);
      }

      ScaleUtil.scale_post();
   }
}
