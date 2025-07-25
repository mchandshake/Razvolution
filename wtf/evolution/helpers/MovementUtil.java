/* Decompiler 11ms, total 279ms, lines 59 */
package wtf.evolution.helpers;

import net.minecraft.client.Minecraft;

public class MovementUtil {
   private static Minecraft mc = Minecraft.getMinecraft();

   public static void setSpeed(float speed) {
      if (mc.player.moveForward > 0.0F) {
         mc.player.motionX = -Math.sin(Math.toRadians((double)mc.player.rotationYaw)) * (double)speed;
         mc.player.motionZ = Math.cos(Math.toRadians((double)mc.player.rotationYaw)) * (double)speed;
      }

      if (mc.player.moveForward < 0.0F) {
         mc.player.motionX = Math.sin(Math.toRadians((double)mc.player.rotationYaw)) * (double)speed;
         mc.player.motionZ = -Math.cos(Math.toRadians((double)mc.player.rotationYaw)) * (double)speed;
      }

      if (mc.player.moveStrafing > 0.0F) {
         mc.player.motionX = Math.cos(Math.toRadians((double)mc.player.rotationYaw)) * (double)speed;
         mc.player.motionZ = Math.sin(Math.toRadians((double)mc.player.rotationYaw)) * (double)speed;
      }

      if (mc.player.moveStrafing < 0.0F) {
         mc.player.motionX = -Math.cos(Math.toRadians((double)mc.player.rotationYaw)) * (double)speed;
         mc.player.motionZ = -Math.sin(Math.toRadians((double)mc.player.rotationYaw)) * (double)speed;
      }

      if (mc.player.moveStrafing > 0.0F && mc.player.moveForward > 0.0F) {
         mc.player.motionX = Math.cos(Math.toRadians((double)(mc.player.rotationYaw + 45.0F))) * (double)speed;
         mc.player.motionZ = Math.sin(Math.toRadians((double)(mc.player.rotationYaw + 45.0F))) * (double)speed;
      }

      if (mc.player.moveStrafing < 0.0F && mc.player.moveForward > 0.0F) {
         mc.player.motionX = -Math.cos(Math.toRadians((double)(mc.player.rotationYaw - 45.0F))) * (double)speed;
         mc.player.motionZ = -Math.sin(Math.toRadians((double)(mc.player.rotationYaw - 45.0F))) * (double)speed;
      }

      if (mc.player.moveStrafing > 0.0F && mc.player.moveForward < 0.0F) {
         mc.player.motionX = -Math.cos(Math.toRadians((double)(mc.player.rotationYaw + 135.0F))) * (double)speed;
         mc.player.motionZ = -Math.sin(Math.toRadians((double)(mc.player.rotationYaw + 135.0F))) * (double)speed;
      }

      if (mc.player.moveStrafing < 0.0F && mc.player.moveForward < 0.0F) {
         mc.player.motionX = Math.cos(Math.toRadians((double)(mc.player.rotationYaw - 135.0F))) * (double)speed;
         mc.player.motionZ = Math.sin(Math.toRadians((double)(mc.player.rotationYaw - 135.0F))) * (double)speed;
      }

   }

   public static double getPlayerMotion() {
      return Math.hypot(mc.player.motionX, mc.player.motionZ);
   }

   public static boolean isMoving() {
      return mc.player.moveForward != 0.0F || mc.player.moveStrafing != 0.0F;
   }
}
