/* Decompiler 3ms, total 295ms, lines 33 */
package wtf.evolution.module.impl.Movement;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventUpdate;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;

@ModuleInfo(
   name = "GuiWalk",
   type = Category.Movement
)
public class GuiWalk extends Module {
   @EventTarget
   public void update(EventUpdate e) {
      KeyBinding[] keys = new KeyBinding[]{this.mc.gameSettings.keyBindForward, this.mc.gameSettings.keyBindBack, this.mc.gameSettings.keyBindLeft, this.mc.gameSettings.keyBindRight, this.mc.gameSettings.keyBindJump, this.mc.gameSettings.keyBindSprint, this.mc.gameSettings.keyBindSneak};
      if (!(this.mc.currentScreen instanceof GuiChat) && !(this.mc.currentScreen instanceof GuiEditSign)) {
         KeyBinding[] var3 = keys;
         int var4 = keys.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            KeyBinding keyBinding = var3[var5];
            keyBinding.pressed = Keyboard.isKeyDown(keyBinding.getKeyCode());
         }

      }
   }
}
