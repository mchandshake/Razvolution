/* Decompiler 7ms, total 347ms, lines 73 */
package wtf.evolution.module.impl.Player;

import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Mouse;
import wtf.evolution.Main;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventUpdate;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.module.impl.Combat.KillAura;
import wtf.evolution.notifications.NotificationType;

@ModuleInfo(
   name = "MiddleClickFriend",
   type = Category.Player
)
public class MiddleClickFriend extends Module {
   public boolean onFriend = true;

   @EventTarget
   public void onUpd(EventUpdate event) {
      Iterator var2 = this.mc.world.loadedEntityList.iterator();

      while(var2.hasNext()) {
         Entity in = (Entity)var2.next();
         if (in != this.mc.player && in instanceof EntityPlayer && !this.isLookingAtTarget(this.mc.player.rotationYaw, this.mc.player.rotationPitch, in, 500.0D)) {
            if (Mouse.isButtonDown(2) && this.onFriend) {
               KillAura var10000 = (KillAura)Main.m.getModule(KillAura.class);
               if (KillAura.target == null) {
                  this.onFriend = false;
                  if (!Main.f.isFriend(in.getName())) {
                     Main.notify.call("FriendManager", TextFormatting.getTextWithoutFormattingCodes(in.getName()) + " added", NotificationType.INFO);
                     Main.f.add(in.getName());
                  } else {
                     Main.notify.call("FriendManager", TextFormatting.getTextWithoutFormattingCodes(in.getName()) + " removed", NotificationType.INFO);
                     Main.f.remove(in.getName());
                  }
               }
            }

            if (!Mouse.isButtonDown(2)) {
               this.onFriend = true;
            }
         }
      }

   }

   public boolean isLookingAtTarget(float yaw, float pitch, Entity entity, double range) {
      Vec3d src = Minecraft.getMinecraft().player.getPositionEyes(1.0F);
      Vec3d vectorForRotation = Entity.getVectorForRotation(pitch, yaw);
      Vec3d dest = src.add(vectorForRotation.x * range, vectorForRotation.y * range, vectorForRotation.z * range);
      RayTraceResult rayTraceResult = Minecraft.getMinecraft().world.rayTraceBlocks(src, dest, false, false, true);
      if (rayTraceResult == null) {
         return true;
      } else {
         return entity.getEntityBoundingBox().expand(0.06D, 0.06D, 0.06D).calculateIntercept(src, dest) == null;
      }
   }

   public void onDisable() {
      super.onDisable();
      this.onFriend = true;
   }
}
