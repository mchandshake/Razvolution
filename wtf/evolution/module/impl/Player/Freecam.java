/* Decompiler 6ms, total 470ms, lines 85 */
package wtf.evolution.module.impl.Player;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventPacket;
import wtf.evolution.event.events.impl.EventUpdate;
import wtf.evolution.helpers.MovementUtil;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.BooleanSetting;

@ModuleInfo(
   name = "Freecam",
   type = Category.Player
)
public class Freecam extends Module {
   public BooleanSetting notp = (new BooleanSetting("No Back Teleport", true)).call(this);
   public double savedX;
   public double savedY;
   public double savedZ;
   public float savedYaw;
   public float savedPitch;
   public double savedMotionX;
   public double savedMotionY;
   public double savedMotionZ;

   public void onEnable() {
      super.onEnable();
      if (this.mc.player != null) {
         this.savedX = this.mc.player.posX;
         this.savedY = this.mc.player.posY;
         this.savedZ = this.mc.player.posZ;
         this.savedYaw = this.mc.player.rotationYaw;
         this.savedPitch = this.mc.player.rotationPitch;
         this.savedMotionX = this.mc.player.motionX;
         this.savedMotionY = this.mc.player.motionY;
         this.savedMotionZ = this.mc.player.motionZ;
      }
   }

   @EventTarget
   public void onUpdate(EventUpdate e) {
      this.mc.player.motionY = 0.0D;
      if (this.mc.gameSettings.keyBindJump.pressed) {
         this.mc.player.motionY = 1.0D;
      }

      if (this.mc.gameSettings.keyBindSneak.pressed) {
         this.mc.player.motionY = -1.0D;
      }

      if (!this.mc.gameSettings.keyBindForward.pressed && !this.mc.gameSettings.keyBindBack.pressed && !this.mc.gameSettings.keyBindLeft.pressed && !this.mc.gameSettings.keyBindRight.pressed) {
         MovementUtil.setSpeed(0.0F);
      } else {
         MovementUtil.setSpeed(1.0F);
      }

      this.mc.player.noClip = true;
   }

   @EventTarget
   public void onPacket(EventPacket e) {
      if (this.notp.get()) {
         if (e.getPacket() instanceof SPacketPlayerPosLook && this.mc.player != null && this.mc.world != null && this.mc.getRenderViewEntity() instanceof EntityPlayerSP) {
            e.cancel();
         }

      }
   }

   public void onDisable() {
      super.onDisable();
      if (this.mc.player != null) {
         this.mc.player.setPosition(this.savedX, this.savedY, this.savedZ);
         this.mc.player.rotationYaw = this.savedYaw;
         this.mc.player.rotationPitch = this.savedPitch;
         this.mc.player.motionX = this.savedMotionX;
         this.mc.player.motionY = this.savedMotionY;
         this.mc.player.motionZ = this.savedMotionZ;
      }
   }
}
