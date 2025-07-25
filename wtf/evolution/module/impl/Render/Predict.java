/* Decompiler 15ms, total 404ms, lines 134 */
package wtf.evolution.module.impl.Render;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import org.lwjgl.opengl.GL11;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventRender;
import wtf.evolution.helpers.render.ColorUtil;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;

@ModuleInfo(
   name = "Predict",
   type = Category.Render
)
public class Predict extends Module {
   public HashMap<Entity, Vec3d> lastPoss = new HashMap();
   public HashMap<Entity, Integer> i1 = new HashMap();

   @EventTarget
   public void render(EventRender e) {
      double ix = -(this.mc.player.lastTickPosX + (this.mc.player.posX - this.mc.player.lastTickPosX) * (double)e.pt);
      double iy = -(this.mc.player.lastTickPosY + (this.mc.player.posY - this.mc.player.lastTickPosY) * (double)e.pt);
      double iz = -(this.mc.player.lastTickPosZ + (this.mc.player.posZ - this.mc.player.lastTickPosZ) * (double)e.pt);
      GlStateManager.pushMatrix();
      GlStateManager.translate(ix, iy, iz);
      GlStateManager.disableDepth();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GL11.glEnable(2848);
      GL11.glLineWidth(3.0F);
      GL11.glColor4f(0.0F, 0.0F, 0.0F, 1.0F);
      GL11.glBegin(1);
      Iterator var8 = this.mc.world.loadedEntityList.iterator();

      Entity en;
      while(var8.hasNext()) {
         en = (Entity)var8.next();
         if (en instanceof EntityEnderPearl) {
            this.penisbobra(en, (double)((EntityEnderPearl)en).getGravityVelocity(), 0.800000011920929D, false);
         }

         if (en instanceof EntityArrow) {
            this.penisbobra(en, 0.05D, 0.6000000238418579D, false);
         }
      }

      GL11.glEnd();
      GL11.glLineWidth(1.5F);
      GL11.glBegin(1);
      var8 = this.mc.world.loadedEntityList.iterator();

      while(var8.hasNext()) {
         en = (Entity)var8.next();
         if (en instanceof EntityEnderPearl) {
            this.penisbobra(en, (double)((EntityEnderPearl)en).getGravityVelocity(), 0.800000011920929D, true);
         }

         if (en instanceof EntityArrow) {
            this.penisbobra(en, 0.05D, 0.6000000238418579D, true);
         }
      }

      GL11.glEnd();
      GL11.glDisable(2848);
      GlStateManager.enableTexture2D();
      GlStateManager.enableDepth();
      GlStateManager.disableBlend();
      GlStateManager.resetColor();
      GlStateManager.popMatrix();
   }

   private void penisbobra(Entity e, double g, double water, boolean r) {
      double motionX = e.motionX;
      double motionY = e.motionY;
      double motionZ = e.motionZ;
      double x = e.posX;
      double y = e.posY;
      double z = e.posZ;
      new Vec3d(x, y, z);

      for(int i = 0; i < 300; ++i) {
         if (r) {
            RenderUtil.color(ColorUtil.fade(1, i * 70, Color.WHITE, 1.0F).getRGB());
         }

         Vec3d lastPos = new Vec3d(x, y, z);
         x += motionX;
         y += motionY;
         z += motionZ;
         if (this.mc.world.getBlockState(new BlockPos((int)x, (int)y, (int)z)).getBlock() == Blocks.WATER) {
            motionX *= water;
            motionY *= water;
            motionZ *= water;
         } else {
            motionX *= 0.99D;
            motionY *= 0.99D;
            motionZ *= 0.99D;
         }

         motionY -= g;
         Vec3d pos = new Vec3d(x, y, z);
         if (this.mc.world.rayTraceBlocks(lastPos, pos) != null) {
            if (this.mc.world.rayTraceBlocks(lastPos, pos).typeOfHit == Type.ENTITY) {
            }
            break;
         }

         if (y <= 0.0D) {
            break;
         }

         if (e.motionZ != 0.0D || e.motionX != 0.0D || e.motionY != 0.0D) {
            this.lastPoss.put(e, new Vec3d(lastPos.x, lastPos.y, lastPos.z));
            GL11.glVertex3d(lastPos.x, lastPos.y, lastPos.z);
            GL11.glVertex3d(x, y, z);
            this.i1.put(e, i);
         }
      }

   }
}
