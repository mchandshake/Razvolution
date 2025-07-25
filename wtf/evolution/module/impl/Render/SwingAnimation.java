/* Decompiler 3ms, total 406ms, lines 60 */
package wtf.evolution.module.impl.Render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventRenderHand;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.ModeSetting;
import wtf.evolution.settings.options.SliderSetting;

@ModuleInfo(
   name = "SwingAnimation",
   type = Category.Render
)
public class SwingAnimation extends Module {
   public ModeSetting mode = (new ModeSetting("Mode", "Bonk", new String[]{"Bonk", "Knife", "Block"})).call(this);
   public SliderSetting mult = (new SliderSetting("Hand Speed", 6.0F, 1.0F, 20.0F, 1.0F)).call(this);

   @EventTarget
   public void onRender(EventRenderHand e) {
      if (this.mc.player.isSwingInProgress && !this.mc.player.getHeldItemMainhand().isEmpty() && e.e == EnumHandSide.RIGHT) {
         String var2 = this.mode.get();
         byte var3 = -1;
         switch(var2.hashCode()) {
         case 2076394:
            if (var2.equals("Bonk")) {
               var3 = 0;
            }
            break;
         case 64279661:
            if (var2.equals("Block")) {
               var3 = 2;
            }
            break;
         case 72645253:
            if (var2.equals("Knife")) {
               var3 = 1;
            }
         }

         switch(var3) {
         case 0:
            GlStateManager.translate(0.0F, 0.5F, 0.0F);
            GlStateManager.rotate(45.0F, 0.0F, 0.0F, 1.0F);
            break;
         case 1:
            GlStateManager.translate(0.0F, 0.5F, 0.0F);
            GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
         case 2:
            GlStateManager.translate(0.0F, 0.5F, 0.0F);
            GlStateManager.rotate(this.mc.gameSettings.mainHand == EnumHandSide.RIGHT ? 60.0F : -75.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(this.mc.gameSettings.mainHand == EnumHandSide.RIGHT ? 110.0F : -20.0F, -0.2F, 1.0F, 0.0F);
         }
      }

   }
}
