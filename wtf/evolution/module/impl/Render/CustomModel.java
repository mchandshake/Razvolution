/* Decompiler 4ms, total 308ms, lines 61 */
package wtf.evolution.module.impl.Render;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import org.lwjgl.opengl.GL11;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventCustomModel;
import wtf.evolution.helpers.math.MathHelper;
import wtf.evolution.model.models.TessellatorModel;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;

@ModuleInfo(
   name = "CustomModel",
   type = Category.Render
)
public class CustomModel extends Module {
   private TessellatorModel hitlerHead;
   private TessellatorModel hitlerBody;

   public void onEnable() {
      super.onEnable();
      this.hitlerHead = new TessellatorModel("/assets/minecraft/skidproject/obj/head.obj");
      this.hitlerBody = new TessellatorModel("/assets/minecraft/skidproject/obj/body.obj");
   }

   public void onDisable() {
      super.onDisable();
      this.hitlerHead = null;
      this.hitlerBody = null;
   }

   @EventTarget
   private void on(EventCustomModel event) {
      GlStateManager.pushMatrix();
      AbstractClientPlayer entity = this.mc.player;
      RenderManager manager = this.mc.getRenderManager();
      double x = MathHelper.interpolate(entity.posX, entity.lastTickPosX, (double)this.mc.getRenderPartialTicks()) - manager.renderPosX;
      double y = MathHelper.interpolate(entity.posY, entity.lastTickPosY, (double)this.mc.getRenderPartialTicks()) - manager.renderPosY;
      double z = MathHelper.interpolate(entity.posZ, entity.lastTickPosZ, (double)this.mc.getRenderPartialTicks()) - manager.renderPosZ;
      float yaw = this.mc.player.prevRotationYaw + (this.mc.player.rotationYaw - this.mc.player.prevRotationYaw) * this.mc.getRenderPartialTicks();
      boolean sneak = this.mc.player.isSneaking();
      GL11.glTranslated(x, y, z);
      if (!(this.mc.currentScreen instanceof GuiContainer)) {
         GL11.glRotatef(-yaw, 0.0F, this.mc.player.height, 0.0F);
      }

      GlStateManager.scale(0.03D, sneak ? 0.027D : 0.029D, 0.03D);
      GlStateManager.disableLighting();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.hitlerHead.render();
      this.hitlerBody.render();
      GlStateManager.enableLighting();
      GlStateManager.resetColor();
      GlStateManager.popMatrix();
   }
}
