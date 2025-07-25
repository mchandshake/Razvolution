/* Decompiler 58ms, total 477ms, lines 187 */
package wtf.evolution.module.impl.Render;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.vecmath.Vector4f;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventDisplay;
import wtf.evolution.helpers.font.Fonts;
import wtf.evolution.helpers.math.MathHelper;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.BooleanSetting;

@ModuleInfo(
   name = "ESP",
   type = Category.Render
)
public class ESP extends Module {
   public final List<Entity> collectedEntities = new ArrayList();
   private final IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
   private final FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
   private final FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
   private final FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);
   public BooleanSetting box = (new BooleanSetting("Box", true)).call(this);
   public BooleanSetting name = (new BooleanSetting("Name", true)).call(this);
   public BooleanSetting item = (new BooleanSetting("Item", true)).call(this);
   public BooleanSetting health = (new BooleanSetting("Health", true)).call(this);
   public BooleanSetting healthBar = (new BooleanSetting("HealthBar", true)).call(this);

   @EventTarget
   public void onRender2D(EventDisplay event) {
      GL11.glPushMatrix();
      this.collectEntities();
      float partialTicks = event.ticks;
      int scaleFactor = ScaledResolution.getScaleFactor();
      double scaling = (double)scaleFactor / Math.pow((double)scaleFactor, 2.0D);
      GL11.glScaled(scaling, scaling, scaling);
      RenderManager renderMng = this.mc.getRenderManager();
      EntityRenderer entityRenderer = this.mc.entityRenderer;
      List<Entity> collectedEntities = this.collectedEntities;
      Iterator var9 = collectedEntities.iterator();

      while(true) {
         Entity entity;
         do {
            do {
               if (!var9.hasNext()) {
                  GL11.glPopMatrix();
                  return;
               }

               entity = (Entity)var9.next();
            } while(!(entity instanceof EntityPlayer));
         } while(!RenderUtil.isInViewFrustrum(entity));

         double x = MathHelper.interpolate(entity.posX, entity.lastTickPosX, (double)partialTicks);
         double y = MathHelper.interpolate(entity.posY, entity.lastTickPosY, (double)partialTicks);
         double z = MathHelper.interpolate(entity.posZ, entity.lastTickPosZ, (double)partialTicks);
         double width = (double)entity.width / 1.5D;
         double n = (double)(entity.height + 0.2F - (entity.isSneaking() ? 0.2F : 0.0F));
         AxisAlignedBB aabb = new AxisAlignedBB(x - width, y, z - width, x + width, y + n, z + width);
         Vec3d[] vectors = new Vec3d[]{new Vec3d(aabb.minX, aabb.minY, aabb.minZ), new Vec3d(aabb.minX, aabb.maxY, aabb.minZ), new Vec3d(aabb.maxX, aabb.minY, aabb.minZ), new Vec3d(aabb.maxX, aabb.maxY, aabb.minZ), new Vec3d(aabb.minX, aabb.minY, aabb.maxZ), new Vec3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vec3d(aabb.maxX, aabb.minY, aabb.maxZ), new Vec3d(aabb.maxX, aabb.maxY, aabb.maxZ)};
         entityRenderer.setupCameraTransform(partialTicks, 0);
         Vector4f position = null;
         Vec3d[] var24 = vectors;
         int var25 = vectors.length;

         for(int var26 = 0; var26 < var25; ++var26) {
            Vec3d vector = var24[var26];
            vector = this.project2D(scaleFactor, vector.x - renderMng.viewerPosX, vector.y - renderMng.viewerPosY, vector.z - renderMng.viewerPosZ);
            if (vector != null && vector.z >= 0.0D && vector.z < 1.0D) {
               if (position == null) {
                  position = new Vector4f((float)vector.x, (float)vector.y, (float)vector.z, 1.0F);
               }

               position.x = (float)Math.min(vector.x, (double)position.x);
               position.y = (float)Math.min(vector.y, (double)position.y);
               position.z = (float)Math.max(vector.x, (double)position.z);
               position.w = (float)Math.max(vector.y, (double)position.w);
            }
         }

         if (position != null) {
            entityRenderer.setupOverlayRendering();
            double posX = (double)position.x;
            double posY = (double)position.y;
            double endPosX = (double)position.z;
            double endPosY = (double)position.w;
            if (this.box.get()) {
               RenderUtil.drawRect(posX - 1.0D, posY, posX + 0.5D, endPosY + 0.5D, Color.black.getRGB());
               RenderUtil.drawRect(posX - 1.0D, posY - 0.5D, endPosX + 0.5D, posY + 0.5D + 0.5D, Color.black.getRGB());
               RenderUtil.drawRect(endPosX - 0.5D - 0.5D, posY, endPosX + 0.5D, endPosY + 0.5D, Color.black.getRGB());
               RenderUtil.drawRect(posX - 1.0D, endPosY - 0.5D - 0.5D, endPosX + 0.5D, endPosY + 0.5D, Color.black.getRGB());
               RenderUtil.drawRect(posX - 0.5D, posY, posX + 0.5D - 0.5D, endPosY, -1);
               RenderUtil.drawRect(posX, endPosY - 0.5D, endPosX, endPosY, -1);
               RenderUtil.drawRect(posX - 0.5D, posY, endPosX, posY + 0.5D, -1);
               RenderUtil.drawRect(endPosX - 0.5D, posY, endPosX, endPosY, -1);
            }

            EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            double hpPercentage = (double)(entityLivingBase.getHealth() / entityLivingBase.getMaxHealth());
            double hpHeight2 = (endPosY - posY) * hpPercentage;
            double hpHeight3 = endPosY - posY;
            double dif = (endPosX - posX) / 2.0D;
            double textWidth = (double)Fonts.pix.getStringWidth(entityLivingBase.getName());
            if (entityLivingBase.getHealth() > 0.0F) {
               if (this.name.get()) {
                  Fonts.pix.drawStringWithOutline(ChatFormatting.stripFormatting(entity.getName()), (double)((float)(posX + dif - textWidth / 2.0D) - 1.0F), (double)((float)posY - 20.0F + 18.0F), -1);
               }

               if (this.healthBar.get()) {
                  RenderUtil.drawRect((float)(posX - 3.5D), (float)(endPosY + 0.5D), (float)(posX - 1.5D), (float)(endPosY - hpHeight3 - 0.5D), Color.BLACK.getRGB());
                  RenderUtil.drawVGradientRect((float)(posX - 3.0D), (float)endPosY, (float)(posX - 2.0D), (float)(endPosY - hpHeight3), (new Color(255, 86, 86)).getRGB(), (new Color(86, 255, 125)).getRGB());
                  RenderUtil.drawRect(posX - 3.5D, posY, posX - 1.5D, endPosY - hpHeight2, (new Color(0, 0, 0, 255)).getRGB());
               }
            }

            if (!entityLivingBase.getHeldItemMainhand().isEmpty() && this.item.get()) {
               Fonts.pix.drawCenteredStringWithOutline(ChatFormatting.stripFormatting(entityLivingBase.getHeldItemMainhand().getDisplayName()), (double)((float)(posX + (endPosX - posX) / 2.0D)), (double)((float)(endPosY + 0.5D) + 4.0F), -1);
            }

            if (this.health.get()) {
               Fonts.pix.drawStringWithOutline((int)entityLivingBase.getHealth() + "HP", (double)((float)(posX - 4.5D) - (float)Fonts.pix.getStringWidth((int)entityLivingBase.getHealth() + "HP")), (double)((float)endPosY) - hpHeight2 + 4.0D, getHealthColor(entityLivingBase, (new Color(255, 86, 86)).getRGB(), (new Color(86, 255, 125)).getRGB()));
            }
         }
      }
   }

   public static int getHealthColor(EntityLivingBase entity, int c1, int c2) {
      float health = entity.getHealth();
      float maxHealth = entity.getMaxHealth();
      float hpPercentage = health / maxHealth;
      int red = (int)((float)(c2 >> 16 & 255) * hpPercentage + (float)(c1 >> 16 & 255) * (1.0F - hpPercentage));
      int green = (int)((float)(c2 >> 8 & 255) * hpPercentage + (float)(c1 >> 8 & 255) * (1.0F - hpPercentage));
      int blue = (int)((float)(c2 & 255) * hpPercentage + (float)(c1 & 255) * (1.0F - hpPercentage));
      return (new Color(red, green, blue)).getRGB();
   }

   private boolean isValid(Entity entity) {
      if (entity == this.mc.player && this.mc.gameSettings.thirdPersonView == 0) {
         return false;
      } else {
         return entity.isDead ? false : entity instanceof EntityPlayer;
      }
   }

   private void collectEntities() {
      this.collectedEntities.clear();
      List<Entity> playerEntities = this.mc.world.loadedEntityList;
      Iterator var2 = playerEntities.iterator();

      while(var2.hasNext()) {
         Entity entity = (Entity)var2.next();
         if (this.isValid(entity)) {
            this.collectedEntities.add(entity);
         }
      }

   }

   private Vec3d project2D(int scaleFactor, double x, double y, double z) {
      GL11.glGetFloat(2982, this.modelview);
      GL11.glGetFloat(2983, this.projection);
      GL11.glGetInteger(2978, this.viewport);
      return GLU.gluProject((float)x, (float)y, (float)z, this.modelview, this.projection, this.viewport, this.vector) ? new Vec3d((double)(this.vector.get(0) / (float)scaleFactor), (double)(((float)Display.getHeight() - this.vector.get(1)) / (float)scaleFactor), (double)this.vector.get(2)) : null;
   }
}
