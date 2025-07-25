/* Decompiler 14ms, total 326ms, lines 71 */
package wtf.evolution.module.impl.Render;

import java.awt.Color;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventRender;
import wtf.evolution.helpers.render.AntiAliasing;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;

@ModuleInfo(
   name = "Hitbox",
   type = Category.Render
)
public class HitBox extends Module {
   @EventTarget
   public void onRender(EventRender e) {
      this.renderDebugBoundingBox(this.mc.player, e.pt, new Color(255, 0, 255, 255), 1.0F, false, true);
   }

   private void renderDebugBoundingBox(Entity entityIn, float partialTicks, Color color, float line, boolean outline, boolean smooth) {
      float x = (float)(entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * (double)partialTicks);
      float y = (float)(entityIn.lastTickPosY + (this.mc.player.posY - entityIn.lastTickPosY) * (double)partialTicks);
      float z = (float)(entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * (double)partialTicks);
      double renderPosX = this.mc.getRenderManager().renderPosX;
      double renderPosY = this.mc.getRenderManager().renderPosY;
      double renderPosZ = this.mc.getRenderManager().renderPosZ;
      x = (float)((double)x - renderPosX);
      y = (float)((double)y - renderPosY);
      z = (float)((double)z - renderPosZ);
      GL11.glPushMatrix();
      GlStateManager.enableBlend();
      GL11.glBlendFunc(770, 771);
      GlStateManager.disableTexture2D();
      GL11.glDisable(2884);
      AntiAliasing.hook(smooth, false, false);
      AxisAlignedBB axisalignedbb = entityIn.getEntityBoundingBox();
      Tessellator tessellator;
      BufferBuilder bufferbuilder;
      if (outline) {
         tessellator = Tessellator.getInstance();
         bufferbuilder = tessellator.getBuffer();
         GL11.glLineWidth(line + 1.0F);
         bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
         RenderGlobal.drawBoundingBox(bufferbuilder, axisalignedbb.minX - entityIn.posX + (double)x, axisalignedbb.minY - entityIn.posY + (double)y, axisalignedbb.minZ - entityIn.posZ + (double)z, axisalignedbb.maxX - entityIn.posX + (double)x, axisalignedbb.maxY - entityIn.posY + (double)y, axisalignedbb.maxZ - entityIn.posZ + (double)z, 0.0F, 0.0F, 0.0F, (float)color.getAlpha() / 255.0F);
         tessellator.draw();
      }

      tessellator = Tessellator.getInstance();
      bufferbuilder = tessellator.getBuffer();
      GL11.glLineWidth(line);
      bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
      RenderGlobal.drawBoundingBox(bufferbuilder, axisalignedbb.minX - entityIn.posX + (double)x, axisalignedbb.minY - entityIn.posY + (double)y, axisalignedbb.minZ - entityIn.posZ + (double)z, axisalignedbb.maxX - entityIn.posX + (double)x, axisalignedbb.maxY - entityIn.posY + (double)y, axisalignedbb.maxZ - entityIn.posZ + (double)z, (float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F);
      tessellator.draw();
      AntiAliasing.unhook(smooth, false, false);
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
      GL11.glEnable(2884);
      GlStateManager.resetColor();
      GL11.glPopMatrix();
   }
}
