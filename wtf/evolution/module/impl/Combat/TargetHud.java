/* Decompiler 42ms, total 406ms, lines 205 */
package wtf.evolution.module.impl.Combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import wtf.evolution.Main;
import wtf.evolution.editor.Drag;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventDisplay;
import wtf.evolution.event.events.impl.EventRender;
import wtf.evolution.helpers.StencilUtil;
import wtf.evolution.helpers.animation.Animation;
import wtf.evolution.helpers.animation.Direction;
import wtf.evolution.helpers.animation.impl.EaseBackIn;
import wtf.evolution.helpers.font.Fonts;
import wtf.evolution.helpers.math.MathHelper;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.BooleanSetting;

@ModuleInfo(
   name = "TargetHud",
   type = Category.Combat
)
public class TargetHud extends Module {
   private final HashMap<EntityPlayer, float[]> wtf = new HashMap();
   public BooleanSetting on = (new BooleanSetting("On Target", true)).call(this);
   public Drag targethud1 = Main.createDrag(this, "targethud", 300.0F, 300.0F);
   float hp;
   float ar;
   int posX;
   int posY;
   float size = 0.0F;
   float status = 0.0F;
   public Animation animation = new EaseBackIn(400, 1.0D, 1.5F);
   EntityLivingBase target;

   public static int[] getScreenCoords(double x, double y, double z) {
      FloatBuffer screenCoords = BufferUtils.createFloatBuffer(3);
      IntBuffer viewport = BufferUtils.createIntBuffer(16);
      FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
      FloatBuffer projection = BufferUtils.createFloatBuffer(16);
      GL11.glGetFloat(2982, modelView);
      GL11.glGetFloat(2983, projection);
      GL11.glGetInteger(2978, viewport);
      boolean result = GLU.gluProject((float)x, (float)y, (float)z, modelView, projection, viewport, screenCoords);
      return result ? new int[]{(int)screenCoords.get(0), (int)((float)Display.getHeight() - screenCoords.get(1)), (int)screenCoords.get(2)} : null;
   }

   @EventTarget
   public void render(EventRender e) {
      if (this.on.get()) {
         this.wtf.clear();
         EntityPlayer player = (EntityPlayer)KillAura.target;
         double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)e.pt - this.mc.getRenderManager().viewerPosY;
         double x = player.lastTickPosX + (player.posX + 10.0D - (player.lastTickPosX + 10.0D)) * (double)e.pt - this.mc.getRenderManager().viewerPosX;
         double z = player.lastTickPosZ + (player.posZ + 10.0D - (player.lastTickPosZ + 10.0D)) * (double)e.pt - this.mc.getRenderManager().viewerPosZ;
         y += (double)(player.height / 2.0F);
         int[] convertedPoints = getScreenCoords(x, y, z);
         float xd = (float)Math.abs(getScreenCoords(x, y + 1.0D, z)[1] - getScreenCoords(x, y, z)[1]);

         assert convertedPoints != null;

         if ((double)convertedPoints[2] >= 0.0D && (double)convertedPoints[2] < 1.0D) {
            this.wtf.put(player, new float[]{(float)convertedPoints[0], (float)convertedPoints[1], xd, (float)convertedPoints[2]});
         }
      }

   }

   @EventTarget
   public void onRender2d(EventDisplay eventRender2D) {
      if (this.on.get() && RenderUtil.isInViewFrustrum(KillAura.target)) {
         if (this.mc.player != null && this.mc.world != null) {
            GlStateManager.pushMatrix();
            double twoDscale = (double)ScaledResolution.getScaleFactor() / Math.pow((double)ScaledResolution.getScaleFactor(), 2.0D);
            GlStateManager.scale(twoDscale, twoDscale, twoDscale);
            Iterator var4 = this.wtf.keySet().iterator();

            while(true) {
               EntityPlayer player;
               float[] renderPositions;
               do {
                  if (!var4.hasNext()) {
                     GlStateManager.popMatrix();
                     return;
                  }

                  player = (EntityPlayer)var4.next();
                  renderPositions = (float[])this.wtf.get(player);
               } while((double)renderPositions[2] < 1.0D && (double)renderPositions[3] > 1.0D);

               float posX = renderPositions[0];
               float posY = renderPositions[1];
               GlStateManager.pushMatrix();
               GlStateManager.translate(posX, posY, 0.0F);
               GlStateManager.scale(2.0F, 2.0F, 2.0F);
               GlStateManager.translate(-posX, -posY, 0.0F);
               String name = ChatFormatting.stripFormatting(player.getName());
               float width = (float)Math.max(Fonts.RUBM16.getStringWidth(name) + 50, 80);
               this.renderTarget(player, (int)((float)((int)posX) - width / 2.0F), (int)((float)((int)posY) - 17.75F));
               GlStateManager.popMatrix();
            }
         }
      } else {
         this.animation.setDuration(400);
         this.target = KillAura.target instanceof EntityPlayer ? KillAura.target : this.target;
         if (this.target == null) {
            this.target = this.mc.player;
         }

         if (KillAura.target == null && !(this.mc.currentScreen instanceof GuiChat)) {
            this.animation.setDirection(Direction.BACKWARDS);
         } else {
            if (KillAura.target == null) {
               this.target = this.mc.player;
            }

            this.animation.setDirection(Direction.FORWARDS);
         }

         this.size = (float)this.animation.getOutput();
         if ((double)this.size < 0.02D) {
            return;
         }

         String name = ChatFormatting.stripFormatting(this.target.getName());
         float width = (float)Math.max(Fonts.RUBM16.getStringWidth(name) + 50, 80);
         this.targethud1.setWidth(width);
         this.targethud1.setHeight(35.5F);
         this.posX = (int)this.targethud1.getX();
         this.posY = (int)this.targethud1.getY();
         this.hp = MathHelper.clamp(MathHelper.interpolate(this.hp, this.target.getHealth() / this.target.getMaxHealth(), 0.800000011920929D), 0.0F, 1.0F);
         this.ar = MathHelper.clamp(MathHelper.interpolate(this.ar, (float)this.target.getTotalArmorValue() / 20.0F, 0.800000011920929D), 0.0F, 1.0F);
         GlStateManager.pushMatrix();
         GlStateManager.translate((double)((float)this.posX + width / 2.0F), (double)this.posY + 17.75D, 0.0D);
         GlStateManager.scale(this.size, this.size, 1.0F);
         GlStateManager.translate((double)((float)(-this.posX) - width / 2.0F), (double)(-this.posY) - 17.75D, 0.0D);
         RenderUtil.blur(() -> {
            RenderUtil.drawRectWH((float)this.posX, (float)this.posY, width, 35.5F, (new Color(0, 0, 0, 128)).getRGB());
         }, 15.0F);
         RenderUtil.drawRectWH((float)this.posX, (float)this.posY, width, 35.5F, (new Color(0, 0, 0, 128)).getRGB());
         RenderUtil.drawRectWH((float)(this.posX + 2), (float)(this.posY + 2), 24.0F, 24.0F, Color.BLACK.getRGB());
         RenderUtil.drawFace((float)(this.posX + 2), (float)(this.posY + 2), 8.0F, 8.0F, 8, 8, 24, 24, 64.0F, 64.0F, (AbstractClientPlayer)this.target);
         Fonts.RUBM16.drawString(name, (float)(this.posX + 28), (float)(this.posY + 4), -1);
         Fonts.RUB14.drawString("Health: " + MathHelper.round((double)this.target.getHealth(), 0.5D), (float)(this.posX + 28), (float)(this.posY + 12), -1);
         Fonts.RUB14.drawString("Distance: " + MathHelper.round((double)this.target.getDistance(this.mc.player), 0.10000000149011612D), (float)(this.posX + 28), (float)(this.posY + 20), -1);
         RenderUtil.drawRectWH((float)this.posX + 1.5F, (float)this.posY + 28.5F, width - 3.0F, 2.5F, (new Color(0, 0, 0, 64)).getRGB());
         RenderUtil.horizontalGradient((double)((float)this.posX + 2.0F), (double)((float)this.posY + 29.0F), (double)((float)this.posX + (width - 2.0F) * this.hp), (double)((float)this.posY + 29.0F + 1.5F), (new Color(0, 156, 65)).getRGB(), (new Color(142, 255, 193)).getRGB());
         RenderUtil.drawRectWH((float)this.posX + 1.5F, (float)this.posY + 31.5F, width - 3.0F, 2.5F, (new Color(0, 0, 0, 63)).getRGB());
         RenderUtil.horizontalGradient((double)((float)this.posX + 2.0F), (double)((float)this.posY + 32.0F), (double)((float)this.posX + (width - 2.0F) * this.ar), (double)((float)this.posY + 32.0F + 1.5F), (new Color(0, 103, 176)).getRGB(), (new Color(57, 213, 255)).getRGB());
         StencilUtil.initStencilToWrite();
         RenderUtil.drawRectWH((float)this.posX, (float)this.posY, width, 35.5F, (new Color(0, 0, 0, 128)).getRGB());
         StencilUtil.readStencilBuffer(0);
         RenderUtil.drawBlurredShadow((float)this.posX, (float)this.posY, width, 35.5F, 5, new Color(0, 0, 0, 128));
         StencilUtil.uninitStencilBuffer();
         GlStateManager.popMatrix();
      }

   }

   public void renderTarget(EntityLivingBase target1, int x, int y) {
      EntityLivingBase target = target1 instanceof EntityPlayer ? target1 : this.mc.player;
      if (target != null && (target != this.mc.player || this.mc.currentScreen instanceof GuiChat)) {
         String name = ChatFormatting.stripFormatting(((EntityLivingBase)target).getName());
         float width = (float)Math.max(Fonts.RUBM16.getStringWidth(name) + 50, 80);
         this.hp = MathHelper.interpolate(this.hp, ((EntityLivingBase)target).getHealth() / ((EntityLivingBase)target).getMaxHealth(), 0.800000011920929D);
         this.ar = MathHelper.interpolate(this.ar, (float)((EntityLivingBase)target).getTotalArmorValue() / 20.0F, 0.800000011920929D);
         StencilUtil.initStencilToWrite();
         RenderUtil.drawRectWH((float)x, (float)y, width, 35.5F, (new Color(0, 0, 0, 128)).getRGB());
         StencilUtil.readStencilBuffer(0);
         RenderUtil.drawBlurredShadow((float)x, (float)y, width, 35.5F, 10, new Color(0, 0, 0, 200));
         StencilUtil.uninitStencilBuffer();
         RenderUtil.drawRectWH((float)x, (float)y, width, 35.5F, (new Color(0, 0, 0, 128)).getRGB());
         RenderUtil.drawRectWH((float)(x + 2), (float)(y + 2), 24.0F, 24.0F, Color.BLACK.getRGB());
         RenderUtil.drawFace((float)(x + 2), (float)(y + 2), 8.0F, 8.0F, 8, 8, 24, 24, 64.0F, 64.0F, (AbstractClientPlayer)target);
         Fonts.RUBM16.drawString(name, (float)(x + 28), (float)(y + 4), -1);
         Fonts.RUB14.drawString("Health: " + MathHelper.round((double)((EntityLivingBase)target).getHealth(), 0.5D), (float)(x + 28), (float)(y + 12), -1);
         Fonts.RUB14.drawString("Distance: " + MathHelper.round((double)((EntityLivingBase)target).getDistance(this.mc.player), 0.10000000149011612D), (float)(x + 28), (float)(y + 20), -1);
         RenderUtil.drawRectWH((float)x + 1.5F, (float)y + 28.5F, width - 3.0F, 2.5F, (new Color(0, 0, 0, 64)).getRGB());
         RenderUtil.horizontalGradient((double)((float)x + 2.0F), (double)((float)y + 29.0F), (double)((float)x + (width - 2.0F) * this.hp), (double)((float)y + 29.0F + 1.5F), (new Color(0, 156, 65)).getRGB(), (new Color(142, 255, 193)).getRGB());
         RenderUtil.drawRectWH((float)x + 1.5F, (float)y + 31.5F, width - 3.0F, 2.5F, (new Color(0, 0, 0, 63)).getRGB());
         RenderUtil.horizontalGradient((double)((float)x + 2.0F), (double)((float)y + 32.0F), (double)((float)x + (width - 2.0F) * this.ar), (double)((float)y + 32.0F + 1.5F), (new Color(0, 103, 176)).getRGB(), (new Color(57, 213, 255)).getRGB());
      } else {
         this.hp = 0.0F;
         this.ar = 0.0F;
      }
   }
}
