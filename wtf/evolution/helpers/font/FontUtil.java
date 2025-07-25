/* Decompiler 1ms, total 314ms, lines 21 */
package wtf.evolution.helpers.font;

import java.awt.Font;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class FontUtil {
   public static Font getFontFromTTF(ResourceLocation fontLocation, float fontSize, int fontType) {
      Font output = null;

      try {
         output = Font.createFont(fontType, Minecraft.getMinecraft().getResourceManager().getResource(fontLocation).getInputStream());
         output = output.deriveFont(fontSize);
      } catch (Exception var5) {
         var5.printStackTrace();
      }

      return output;
   }
}
