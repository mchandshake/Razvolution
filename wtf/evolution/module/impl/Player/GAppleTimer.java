/* Decompiler 4ms, total 352ms, lines 50 */
package wtf.evolution.module.impl.Player;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import org.lwjgl.input.Mouse;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventUpdate;
import wtf.evolution.event.events.impl.GAppleEatEvent;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.SliderSetting;

@ModuleInfo(
   name = "GAppleTimer",
   type = Category.Player
)
public class GAppleTimer extends Module {
   private final SliderSetting cooldown = (new SliderSetting("Cooldown", 55.0F, 0.0F, 100.0F, 10.0F)).call(this);
   private boolean isEated;

   public void onDisable() {
      this.isEated = false;
   }

   @EventTarget
   public void onEat(GAppleEatEvent event) {
      this.isEated = true;
      if (this.mc.player.getCooldownTracker().hasCooldown(Items.GOLDEN_APPLE) && this.mc.player.getActiveItemStack().getItem() == Items.GOLDEN_APPLE) {
         event.cancel();
      }

   }

   @EventTarget
   public void onUpdate(EventUpdate e) {
      if (this.isEated) {
         this.mc.player.getCooldownTracker().setCooldown(Items.GOLDEN_APPLE, (int)this.cooldown.get());
         this.isEated = false;
      }

      if (this.mc.player.getCooldownTracker().hasCooldown(Items.GOLDEN_APPLE) && this.mc.player.getActiveItemStack().getItem() == Items.GOLDEN_APPLE) {
         this.mc.gameSettings.keyBindUseItem.pressed = false;
      } else if (Mouse.isButtonDown(1) && !(this.mc.currentScreen instanceof GuiContainer)) {
         this.mc.gameSettings.keyBindUseItem.pressed = true;
      }

   }
}
