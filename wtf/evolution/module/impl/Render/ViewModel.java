/* Decompiler 3ms, total 381ms, lines 41 */
package wtf.evolution.module.impl.Render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventRenderHand;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.Setting;
import wtf.evolution.settings.options.SliderSetting;

@ModuleInfo(
   name = "ViewModel",
   type = Category.Render
)
public class ViewModel extends Module {
   public SliderSetting rightX = new SliderSetting("Right X", 0.0F, -180.0F, 180.0F, 1.0F);
   public SliderSetting rightY = new SliderSetting("Right Y", 0.0F, -180.0F, 180.0F, 1.0F);
   public SliderSetting rightZ = new SliderSetting("Right Z", 0.0F, -180.0F, 180.0F, 1.0F);
   public SliderSetting leftX = new SliderSetting("Left X", 0.0F, -180.0F, 180.0F, 1.0F);
   public SliderSetting leftY = new SliderSetting("Left Y", 0.0F, -180.0F, 180.0F, 1.0F);
   public SliderSetting leftZ = new SliderSetting("Left Z", 0.0F, -180.0F, 180.0F, 1.0F);

   public ViewModel() {
      this.addSettings(new Setting[]{this.rightX, this.rightY, this.rightZ, this.leftX, this.leftY, this.leftZ});
   }

   @EventTarget
   public void onRender(EventRenderHand e) {
      if (e.e == EnumHandSide.RIGHT) {
         GlStateManager.translate(this.rightX.get() / 180.0F, this.rightY.get() / 180.0F, this.rightZ.get() / 180.0F);
      }

      if (e.e == EnumHandSide.LEFT) {
         GlStateManager.translate(this.leftX.get() / 180.0F, this.leftY.get() / 180.0F, this.leftZ.get() / 180.0F);
      }

   }
}
