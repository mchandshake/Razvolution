/* Decompiler 15ms, total 323ms, lines 134 */
package wtf.evolution.module.impl.Movement;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer.PositionRotation;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventMotion;
import wtf.evolution.event.events.impl.EventPacket;
import wtf.evolution.event.events.impl.EventUpdate;
import wtf.evolution.helpers.MovementUtil;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.Setting;
import wtf.evolution.settings.options.ModeSetting;
import wtf.evolution.settings.options.SliderSetting;

@ModuleInfo(
   name = "Flight",
   type = Category.Movement
)
public class Flight extends Module {
   public ModeSetting mode = new ModeSetting("Mode", "Creative", new String[]{"Creative", "Matrix Damage", "Matrix Jump", "Motion", "Big Grief", "Sunrise XD", "Really World"});
   public SliderSetting height = (new SliderSetting("Height", 1.0F, 0.0F, 9.0F, 0.1F)).setHidden(() -> {
      return !this.mode.is("Matrix Damage");
   });
   public SliderSetting speed = (new SliderSetting("Speed", 1.9F, 0.0F, 1.9F, 0.1F)).setHidden(() -> {
      return !this.mode.is("Matrix Damage");
   });
   public SliderSetting height2 = (new SliderSetting("Height", 1.0F, 0.0F, 9.0F, 0.1F)).setHidden(() -> {
      return !this.mode.is("Matrix Jump");
   });
   public SliderSetting speed2 = (new SliderSetting("Speed", 1.9F, 0.0F, 1.9F, 0.1F)).setHidden(() -> {
      return !this.mode.is("Matrix Jump");
   });
   public SliderSetting MotionSpeed = (new SliderSetting("Speed", 1.0F, 0.1F, 10.0F, 0.1F)).setHidden(() -> {
      return !this.mode.is("Motion");
   });

   public Flight() {
      this.addSettings(new Setting[]{this.mode, this.height, this.speed, this.height2, this.speed2, this.MotionSpeed});
   }

   @EventTarget
   public void onReceive(EventPacket e) {
      SPacketPlayerPosLook packet;
      if (this.mode.get().equalsIgnoreCase("Matrix Damage") && e.getPacket() instanceof SPacketPlayerPosLook) {
         packet = (SPacketPlayerPosLook)e.getPacket();
         this.mc.player.connection.sendPacket(new CPacketConfirmTeleport(packet.getTeleportId()));
         this.mc.player.connection.sendPacket(new PositionRotation(packet.getX(), packet.getY(), packet.getZ(), packet.getYaw(), packet.getPitch(), false));
         this.mc.player.setPosition(packet.getX(), packet.getY(), packet.getZ());
         e.cancel();
         this.toggle();
      }

      if (this.mode.get().equalsIgnoreCase("Matrix Jump") && e.getPacket() instanceof SPacketPlayerPosLook) {
         packet = (SPacketPlayerPosLook)e.getPacket();
         this.mc.player.connection.sendPacket(new CPacketConfirmTeleport(packet.getTeleportId()));
         this.mc.player.connection.sendPacket(new PositionRotation(packet.getX(), packet.getY(), packet.getZ(), packet.getYaw(), packet.getPitch(), false));
         this.mc.player.setPosition(packet.getX(), packet.getY(), packet.getZ());
         e.cancel();
         this.toggle();
      }

   }

   @EventTarget
   public void onUpdate(EventUpdate e) {
      this.setSuffix(this.mode.get());
      if (this.mode.get().equalsIgnoreCase("Matrix Damage") && this.mc.player.hurtTime > 0) {
         if (this.mc.player.onGround) {
            if (MovementUtil.isMoving()) {
               this.mc.player.jump();
            }
         } else if (MovementUtil.isMoving()) {
            this.mc.player.motionY = (double)this.height.get();
            MovementUtil.setSpeed(this.speed.get());
         }
      }

      if (this.mode.get().equalsIgnoreCase("Matrix Jump")) {
         if (this.mc.player.onGround) {
            if (MovementUtil.isMoving()) {
               this.mc.player.jump();
            }
         } else if (MovementUtil.isMoving()) {
            this.mc.player.motionY = (double)this.height2.get();
            MovementUtil.setSpeed(this.speed2.get());
         }
      }

      if (this.mode.is("Big Grief")) {
         this.mc.player.motionY = -0.003D;
      }

   }

   @EventTarget
   public void onMotion(EventMotion e) {
      if (this.mode.get().equalsIgnoreCase("Creative")) {
         this.mc.player.capabilities.isFlying = true;
      }

      if (this.mode.is("Sunrise XD") && this.mc.player.collidedVertically) {
         this.mc.player.motionY = 0.10000000149011612D;
      }

      if (this.mode.get().equalsIgnoreCase("Motion")) {
         MovementUtil.setSpeed(this.MotionSpeed.get());
         this.mc.player.motionY = 0.0D;
         EntityPlayerSP var10000;
         if (this.mc.gameSettings.keyBindJump.isKeyDown()) {
            var10000 = this.mc.player;
            var10000.motionY += (double)this.MotionSpeed.get();
         }

         if (this.mc.gameSettings.keyBindSneak.isKeyDown()) {
            var10000 = this.mc.player;
            var10000.motionY -= (double)this.MotionSpeed.get();
         }
      }

   }

   public void onDisable() {
      super.onDisable();
      if (this.mc.player != null) {
         this.mc.player.capabilities.isFlying = false;
      }

   }
}
