/* Decompiler 11ms, total 309ms, lines 61 */
package wtf.evolution.module.impl.Movement;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventMotion;
import wtf.evolution.helpers.MovementUtil;
import wtf.evolution.helpers.math.MathHelper;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.ModeSetting;

@ModuleInfo(
   name = "Jesus",
   type = Category.Movement
)
public class Jesus extends Module {
   public ModeSetting mode = (new ModeSetting("Mode", "Matrix Solid", new String[]{"Matrix Solid"})).call(this);

   @EventTarget
   public void onMotion(EventMotion e) {
      if (this.mode.is("Matrix Solid")) {
         if (this.mc.world.getBlockState(new BlockPos(this.mc.player.posX, this.mc.player.posY + 0.008D, this.mc.player.posZ)).getBlock() == Blocks.WATER && !this.mc.player.onGround) {
            boolean isUp = this.mc.world.getBlockState(new BlockPos(this.mc.player.posX, this.mc.player.posY + 0.03D, this.mc.player.posZ)).getBlock() == Blocks.WATER;
            this.mc.player.jumpMovementFactor = 0.0F;
            float yport = MovementUtil.getPlayerMotion() > 0.1D ? 0.02F : 0.032F;
            this.mc.player.setVelocity(this.mc.player.motionX, (double)this.mc.player.fallDistance < 3.5D ? (double)(isUp ? yport : -yport) : -0.1D, this.mc.player.motionZ);
         }

         if (this.mc.player.posY > (double)((int)this.mc.player.posY) + 0.89D && this.mc.player.posY <= (double)((int)this.mc.player.posY + 1) || (double)this.mc.player.fallDistance > 3.5D) {
            this.mc.player.posY = (double)((int)this.mc.player.posY + 1) + 1.0E-45D;
            if (!this.mc.player.isInWater() && this.mc.world.getBlockState(new BlockPos(this.mc.player.posX, this.mc.player.posY - 0.1D, this.mc.player.posZ)).getBlock() == Blocks.WATER) {
               if (this.mc.player.collidedHorizontally) {
                  this.mc.player.motionY = 0.2D;
                  EntityPlayerSP var10000 = this.mc.player;
                  var10000.motionX *= 0.0D;
                  var10000 = this.mc.player;
                  var10000.motionZ *= 0.0D;
               }

               MovementUtil.setSpeed(MathHelper.clamp((float)(MovementUtil.getPlayerMotion() + 0.20000000298023224D), 0.5F, 1.14F));
            }
         }

         if (this.mc.player.isInWater() || this.mc.world.getBlockState(new BlockPos(this.mc.player.posX, this.mc.player.posY + 0.15D, this.mc.player.posZ)).getBlock() == Blocks.WATER) {
            this.mc.player.motionY = 0.16D;
            if (this.mc.world.getBlockState(new BlockPos(this.mc.player.posX, this.mc.player.posY + 2.0D, this.mc.player.posZ)).getBlock() == Blocks.AIR) {
               this.mc.player.motionY = 0.12D;
            }

            if (this.mc.world.getBlockState(new BlockPos(this.mc.player.posX, this.mc.player.posY + 1.0D, this.mc.player.posZ)).getBlock() == Blocks.AIR) {
               this.mc.player.motionY = 0.18D;
            }
         }
      }

   }
}
