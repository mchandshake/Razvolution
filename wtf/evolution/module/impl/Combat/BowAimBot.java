/* Decompiler 8ms, total 523ms, lines 82 */
package wtf.evolution.module.impl.Combat;

import java.util.Iterator;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventMotion;
import wtf.evolution.helpers.math.MathHelper;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;

@ModuleInfo(
   name = "BowAimBot",
   type = Category.Combat
)
public class BowAimBot extends Module {
   public Entity target;
   public double resolveX;
   public double resolveZ;

   @EventTarget
   public void onUpdate(EventMotion e) {
      this.resolvePlayers();
      this.target = (Entity)this.mc.world.getEntities(EntityLivingBase.class, this::isValid).get(0);
      if (this.target != null && this.mc.player.getActiveItemStack().getItem() instanceof ItemBow) {
         this.rotate(e);
      }

      this.releaseResolver();
   }

   public boolean isValid(Entity e) {
      if (e == this.mc.player) {
         return false;
      } else if (e instanceof EntityPlayer) {
         return true;
      } else {
         return this.mc.player.getDistance(e) < 50.0F;
      }
   }

   public void resolvePlayers() {
      Iterator var1 = this.mc.world.playerEntities.iterator();

      while(var1.hasNext()) {
         EntityPlayer player = (EntityPlayer)var1.next();
         if (player instanceof EntityOtherPlayerMP) {
            ((EntityOtherPlayerMP)player).resolve();
         }
      }

   }

   public void releaseResolver() {
      Iterator var1 = this.mc.world.playerEntities.iterator();

      while(var1.hasNext()) {
         EntityPlayer player = (EntityPlayer)var1.next();
         if (player instanceof EntityOtherPlayerMP) {
            ((EntityOtherPlayerMP)player).releaseResolver();
         }
      }

   }

   public void rotate(EventMotion e) {
      this.resolveX = MathHelper.interpolate((this.target.posX - this.target.prevPosX) * 3.0D, this.resolveX, 0.1D);
      this.resolveZ = MathHelper.interpolate((this.target.posZ - this.target.prevPosZ) * 3.0D, this.resolveZ, 0.1D);
      double x = this.target.posX - this.mc.player.posX + (this.target.posX - this.target.prevPosX == 0.0D ? 0.0D : this.resolveX);
      double y = this.target.posY - this.mc.player.posY + (double)(this.mc.player.getDistance(this.target) / 25.0F) + Math.abs(this.target.posY - this.target.prevPosY);
      double z = this.target.posZ - this.mc.player.posZ + (this.target.posZ - this.target.prevPosZ == 0.0D ? 0.0D : this.resolveZ);
      double yaw = Math.atan2(z, x);
      double pitch = net.minecraft.util.math.MathHelper.clamp(Math.atan2(y, Math.sqrt(x * x + z * z)), -90.0D, 90.0D);
      e.setYaw((float)(Math.toDegrees(yaw) - 90.0D));
      e.setPitch(-((float)Math.toDegrees(pitch)));
   }
}
