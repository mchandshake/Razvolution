/* Decompiler 6ms, total 288ms, lines 51 */
package ru.salam4ik.bot.bot.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.util.MovementInput;
import org.lwjgl.input.Keyboard;

class BotPlayClient$1 extends MovementInput {
   // $FF: synthetic field
   final BotPlayClient this$0;

   BotPlayClient$1(BotPlayClient this$0) {
      this.this$0 = this$0;
   }

   public void updatePlayerMoveState() {
      this.moveForward = 0.0F;
      this.moveStrafe = 0.0F;
      this.jump = false;
      this.sneak = false;
      if (!(Minecraft.getMinecraft().currentScreen instanceof GuiChat)) {
         if (Keyboard.isKeyDown(72) || Keyboard.isKeyDown(200) || this.this$0.forward) {
            ++this.moveForward;
         }

         if (Keyboard.isKeyDown(76) || Keyboard.isKeyDown(208) || this.this$0.backward) {
            --this.moveForward;
         }

         if (Keyboard.isKeyDown(75) || Keyboard.isKeyDown(203) || this.this$0.left) {
            ++this.moveStrafe;
         }

         if (Keyboard.isKeyDown(77) || Keyboard.isKeyDown(205) || this.this$0.right) {
            --this.moveStrafe;
         }

         if (Keyboard.isKeyDown(79) || this.this$0.jump || BotPlayClient.access$000(this.this$0).isInWater()) {
            this.jump = true;
         }

         if (Keyboard.isKeyDown(81) || this.this$0.sneak) {
            this.sneak = true;
            this.moveStrafe = (float)((double)this.moveStrafe * 0.3D);
            this.moveForward = (float)((double)this.moveForward * 0.3D);
         }
      }

   }
}
