/* Decompiler 17ms, total 368ms, lines 149 */
package wtf.evolution.module.impl.Movement;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventMotion;
import wtf.evolution.helpers.animation.Counter;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.ModeSetting;

@ModuleInfo(
   name = "Spider",
   type = Category.Movement
)
public class Spider extends Module {
   public Counter c = new Counter();
   public ModeSetting mode = (new ModeSetting("Mode", "Matrix", new String[]{"Matrix", "Sunrise"})).call(this);

   @EventTarget
   public void onMotion(EventMotion e) {
      if (this.mode.is("Matrix") && this.mc.player.collidedHorizontally && this.c.hasReached(100.0D)) {
         e.setOnGround(true);
         this.mc.player.onGround = true;
         this.mc.player.jump();
         this.c.reset();
      }

      if (this.mode.is("Sunrise")) {
         if (this.mc.player.collidedHorizontally) {
            this.mc.player.connection.sendPacket(new CPacketEntityAction(this.mc.player, Action.START_SNEAKING));
            int find = -2;

            for(int i = 0; i <= 8; ++i) {
               if (this.mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock) {
                  find = i;
               }
            }

            if (find == -2) {
               return;
            }

            BlockPos pos = new BlockPos(this.mc.player.posX, this.mc.player.posY + 2.0D, this.mc.player.posZ);
            EnumFacing side = this.getPlaceableSide(pos);
            if (side != null) {
               this.mc.getConnection().sendPacket(new CPacketHeldItemChange(find));
               BlockPos neighbour = (new BlockPos(this.mc.player.posX, this.mc.player.posY + 2.0D, this.mc.player.posZ)).offset(side);
               EnumFacing opposite = side.getOpposite();
               Vec3d hitVec = (new Vec3d(neighbour)).add(0.5D, 0.5D, 0.5D).add((new Vec3d(opposite.getDirectionVec())).scale(0.5D));
               Vec2f rotation = this.getRotationTo(hitVec);
               e.setPitch(rotation.y);
               e.setYaw(rotation.x);
               float x = (float)(hitVec.x - (double)neighbour.getX());
               float y = (float)(hitVec.y - (double)neighbour.getY());
               float z = (float)(hitVec.z - (double)neighbour.getZ());
               this.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(neighbour, opposite, EnumHand.MAIN_HAND, x, y, z));
               this.mc.player.connection.sendPacket(new CPacketEntityAction(this.mc.player, Action.START_SNEAKING));
               this.mc.player.connection.sendPacket(new CPacketPlayerDigging(net.minecraft.network.play.client.CPacketPlayerDigging.Action.START_DESTROY_BLOCK, neighbour, opposite));
               this.mc.player.connection.sendPacket(new CPacketPlayerDigging(net.minecraft.network.play.client.CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, neighbour, opposite));
               this.mc.player.connection.sendPacket(new CPacketEntityAction(this.mc.player, Action.STOP_SNEAKING));
               this.mc.player.connection.sendPacket(new CPacketEntityAction(this.mc.player, Action.STOP_SPRINTING));
            }
         }

         if (this.mc.player.collidedHorizontally && this.c.hasReached(100.0D)) {
            e.setOnGround(true);
            this.mc.player.onGround = true;
            this.mc.player.jump();
            this.c.reset();
         }
      }

   }

   public EnumFacing getPlaceableSide(BlockPos pos) {
      EnumFacing[] var2 = EnumFacing.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EnumFacing side = var2[var4];
         BlockPos neighbour = pos.offset(side);
         if (!this.mc.world.isAirBlock(neighbour)) {
            IBlockState blockState = this.getState(neighbour);
            if (!blockState.getMaterial().isReplaceable()) {
               return side;
            }
         }
      }

      return null;
   }

   private Vec2f getRotationTo(Vec3d posTo) {
      EntityPlayerSP player = this.mc.player;
      return player != null ? this.getRotationTo(player.getPositionEyes(1.0F), posTo) : Vec2f.ZERO;
   }

   public Block getBlock(BlockPos pos) {
      return this.getState(pos).getBlock();
   }

   public IBlockState getState(BlockPos blockPos) {
      return this.mc.world.getBlockState(blockPos);
   }

   private Vec2f getRotationTo(Vec3d posFrom, Vec3d posTo) {
      return this.getRotationFromVec(posTo.subtract(posFrom));
   }

   private Vec2f getRotationFromVec(Vec3d vec) {
      double lengthXZ = Math.hypot(vec.x, vec.z);
      double yaw = this.normalizeAngle((double)this.getFixedRotation((float)(Math.toDegrees(Math.atan2(vec.z, vec.x)) - 90.0D)));
      double pitch = this.normalizeAngle((double)this.getFixedRotation((float)Math.toDegrees(-Math.atan2(vec.y, lengthXZ))));
      return new Vec2f((float)yaw, (float)pitch);
   }

   public float getFixedRotation(float value) {
      float f1 = (f1 = (float)((double)this.mc.gameSettings.mouseSensitivity * 0.6D + 0.2D)) * f1 * f1 * 8.0F;
      return (float)Math.round(value / (float)((double)f1 * 0.15D)) * (float)((double)f1 * 0.15D);
   }

   public double normalizeAngle(double angle) {
      angle %= 360.0D;
      if (angle >= 180.0D) {
         angle -= 360.0D;
      }

      if (angle < -180.0D) {
         angle += 360.0D;
      }

      return angle;
   }
}
