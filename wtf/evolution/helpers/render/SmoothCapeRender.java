/* Decompiler 30ms, total 435ms, lines 265 */
package wtf.evolution.helpers.render;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.layers.LayerCape;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import wtf.evolution.helpers.render.Simulation.Point;
import wtf.evolution.helpers.render.cape.Matrix4f;
import wtf.evolution.helpers.render.cape.PoseStack;
import wtf.evolution.helpers.render.cape.Vector3f;
import wtf.evolution.helpers.render.cape.Vector4f;

public class SmoothCapeRender {
   public void renderSmoothCape(LayerCape layer, AbstractClientPlayer abstractClientPlayer, float delta) {
      BufferBuilder worldrenderer = Tessellator.getInstance().getBuffer();
      worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
      PoseStack poseStack = new PoseStack();
      poseStack.pushPose();
      Matrix4f oldPositionMatrix = null;

      for(int part = 0; part < 16; ++part) {
         this.modifyPoseStack(layer, poseStack, abstractClientPlayer, delta, part);
         if (oldPositionMatrix == null) {
            oldPositionMatrix = poseStack.last().pose();
         }

         if (part == 0) {
            addTopVertex(worldrenderer, poseStack.last().pose(), oldPositionMatrix, 0.3F, 0.0F, 0.0F, -0.3F, 0.0F, -0.06F, part);
         }

         if (part == 15) {
            addBottomVertex(worldrenderer, poseStack.last().pose(), poseStack.last().pose(), 0.3F, (float)(part + 1) * 0.06F, 0.0F, -0.3F, (float)(part + 1) * 0.06F, -0.06F, part);
         }

         addLeftVertex(worldrenderer, poseStack.last().pose(), oldPositionMatrix, -0.3F, (float)(part + 1) * 0.06F, 0.0F, -0.3F, (float)part * 0.06F, -0.06F, part);
         addRightVertex(worldrenderer, poseStack.last().pose(), oldPositionMatrix, 0.3F, (float)(part + 1) * 0.06F, 0.0F, 0.3F, (float)part * 0.06F, -0.06F, part);
         addBackVertex(worldrenderer, poseStack.last().pose(), oldPositionMatrix, 0.3F, (float)(part + 1) * 0.06F, -0.06F, -0.3F, (float)part * 0.06F, -0.06F, part);
         addFrontVertex(worldrenderer, oldPositionMatrix, poseStack.last().pose(), 0.3F, (float)(part + 1) * 0.06F, 0.0F, -0.3F, (float)part * 0.06F, 0.0F, part);
         oldPositionMatrix = poseStack.last().pose();
         poseStack.popPose();
      }

      Tessellator.getInstance().draw();
   }

   void modifyPoseStack(LayerCape layer, PoseStack poseStack, AbstractClientPlayer abstractClientPlayer, float h, int part) {
      this.modifyPoseStackSimulation(layer, poseStack, abstractClientPlayer, h, part);
   }

   private void modifyPoseStackSimulation(LayerCape layer, PoseStack poseStack, AbstractClientPlayer abstractClientPlayer, float delta, int part) {
      Simulation simulation = abstractClientPlayer.getSimulation();
      poseStack.pushPose();
      poseStack.translate(0.0D, 0.0D, 0.125D);
      float z = ((Point)simulation.points.get(part)).getLerpX(delta) - ((Point)simulation.points.get(0)).getLerpX(delta);
      if (z > 0.0F) {
         z = 0.0F;
      }

      float y = ((Point)simulation.points.get(0)).getLerpY(delta) - (float)part - ((Point)simulation.points.get(part)).getLerpY(delta);
      float sidewaysRotationOffset = 0.0F;
      float partRotation = (float)(-Math.atan2((double)y, (double)z));
      partRotation = Math.max(partRotation, 0.0F);
      if (partRotation != 0.0F) {
         partRotation = (float)(3.141592653589793D - (double)partRotation);
      }

      partRotation *= 57.2958F;
      partRotation *= 2.0F;
      float height = 0.0F;
      if (abstractClientPlayer.isSneaking()) {
         height += 25.0F;
         poseStack.translate(0.0D, 0.15000000596046448D, 0.0D);
      }

      float naturalWindSwing = layer.getNatrualWindSwing(part);
      poseStack.mulPose(Vector3f.XP.rotationDegrees(6.0F + height + naturalWindSwing));
      poseStack.mulPose(Vector3f.ZP.rotationDegrees(0.0F));
      poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
      poseStack.translate(0.0D, (double)(y / 16.0F), (double)(z / 16.0F));
      poseStack.translate(0.0D, 0.03D, -0.03D);
      poseStack.translate(0.0D, (double)((float)part * 1.0F / 16.0F), 0.0D);
      poseStack.mulPose(Vector3f.XP.rotationDegrees(-partRotation));
      poseStack.translate(0.0D, (double)((float)(-part) * 1.0F / 16.0F), 0.0D);
      poseStack.translate(0.0D, -0.03D, 0.03D);
   }

   private static void addBackVertex(BufferBuilder worldrenderer, Matrix4f matrix, Matrix4f oldMatrix, float x1, float y1, float z1, float x2, float y2, float z2, int part) {
      float minU;
      if (x1 < x2) {
         minU = x1;
         x1 = x2;
         x2 = minU;
      }

      if (y1 < y2) {
         minU = y1;
         y1 = y2;
         y2 = minU;
         Matrix4f k = matrix;
         matrix = oldMatrix;
         oldMatrix = k;
      }

      minU = 0.015625F;
      float maxU = 0.171875F;
      float minV = 0.03125F;
      float maxV = 0.53125F;
      float deltaV = maxV - minV;
      float vPerPart = deltaV / 16.0F;
      maxV = minV + vPerPart * (float)(part + 1);
      minV += vPerPart * (float)part;
      vertex(worldrenderer, oldMatrix, x1, y2, z1).tex(0.171875D, (double)minV).normal(1.0F, 0.0F, 0.0F).endVertex();
      vertex(worldrenderer, oldMatrix, x2, y2, z1).tex(0.015625D, (double)minV).normal(1.0F, 0.0F, 0.0F).endVertex();
      vertex(worldrenderer, matrix, x2, y1, z2).tex(0.015625D, (double)maxV).normal(1.0F, 0.0F, 0.0F).endVertex();
      vertex(worldrenderer, matrix, x1, y1, z2).tex(0.171875D, (double)maxV).normal(1.0F, 0.0F, 0.0F).endVertex();
   }

   private static void addFrontVertex(BufferBuilder worldrenderer, Matrix4f matrix, Matrix4f oldMatrix, float x1, float y1, float z1, float x2, float y2, float z2, int part) {
      float minU;
      if (x1 < x2) {
         minU = x1;
         x1 = x2;
         x2 = minU;
      }

      if (y1 < y2) {
         minU = y1;
         y1 = y2;
         y2 = minU;
         Matrix4f k = matrix;
         matrix = oldMatrix;
         oldMatrix = k;
      }

      minU = 0.1875F;
      float maxU = 0.34375F;
      float minV = 0.03125F;
      float maxV = 0.53125F;
      float deltaV = maxV - minV;
      float vPerPart = deltaV / 16.0F;
      maxV = minV + vPerPart * (float)(part + 1);
      minV += vPerPart * (float)part;
      vertex(worldrenderer, oldMatrix, x1, y1, z1).tex(0.34375D, (double)maxV).normal(1.0F, 0.0F, 0.0F).endVertex();
      vertex(worldrenderer, oldMatrix, x2, y1, z1).tex(0.1875D, (double)maxV).normal(1.0F, 0.0F, 0.0F).endVertex();
      vertex(worldrenderer, matrix, x2, y2, z2).tex(0.1875D, (double)minV).normal(1.0F, 0.0F, 0.0F).endVertex();
      vertex(worldrenderer, matrix, x1, y2, z2).tex(0.34375D, (double)minV).normal(1.0F, 0.0F, 0.0F).endVertex();
   }

   private static void addLeftVertex(BufferBuilder worldrenderer, Matrix4f matrix, Matrix4f oldMatrix, float x1, float y1, float z1, float x2, float y2, float z2, int part) {
      if (x1 < x2) {
         x2 = x1;
      }

      float minU;
      if (y1 < y2) {
         minU = y1;
         y1 = y2;
         y2 = minU;
      }

      minU = 0.0F;
      float maxU = 0.015625F;
      float minV = 0.03125F;
      float maxV = 0.53125F;
      float deltaV = maxV - minV;
      float vPerPart = deltaV / 16.0F;
      maxV = minV + vPerPart * (float)(part + 1);
      minV += vPerPart * (float)part;
      vertex(worldrenderer, matrix, x2, y1, z1).tex(0.015625D, (double)maxV).normal(1.0F, 0.0F, 0.0F).endVertex();
      vertex(worldrenderer, matrix, x2, y1, z2).tex(0.0D, (double)maxV).normal(1.0F, 0.0F, 0.0F).endVertex();
      vertex(worldrenderer, oldMatrix, x2, y2, z2).tex(0.0D, (double)minV).normal(1.0F, 0.0F, 0.0F).endVertex();
      vertex(worldrenderer, oldMatrix, x2, y2, z1).tex(0.015625D, (double)minV).normal(1.0F, 0.0F, 0.0F).endVertex();
   }

   private static void addRightVertex(BufferBuilder worldrenderer, Matrix4f matrix, Matrix4f oldMatrix, float x1, float y1, float z1, float x2, float y2, float z2, int part) {
      if (x1 < x2) {
         x2 = x1;
      }

      float minU;
      if (y1 < y2) {
         minU = y1;
         y1 = y2;
         y2 = minU;
      }

      minU = 0.171875F;
      float maxU = 0.1875F;
      float minV = 0.03125F;
      float maxV = 0.53125F;
      float deltaV = maxV - minV;
      float vPerPart = deltaV / 16.0F;
      maxV = minV + vPerPart * (float)(part + 1);
      minV += vPerPart * (float)part;
      vertex(worldrenderer, matrix, x2, y1, z2).tex(0.171875D, (double)maxV).normal(1.0F, 0.0F, 0.0F).endVertex();
      vertex(worldrenderer, matrix, x2, y1, z1).tex(0.1875D, (double)maxV).normal(1.0F, 0.0F, 0.0F).endVertex();
      vertex(worldrenderer, oldMatrix, x2, y2, z1).tex(0.1875D, (double)minV).normal(1.0F, 0.0F, 0.0F).endVertex();
      vertex(worldrenderer, oldMatrix, x2, y2, z2).tex(0.171875D, (double)minV).normal(1.0F, 0.0F, 0.0F).endVertex();
   }

   private static void addBottomVertex(BufferBuilder worldrenderer, Matrix4f matrix, Matrix4f oldMatrix, float x1, float y1, float z1, float x2, float y2, float z2, int part) {
      float minU;
      if (x1 < x2) {
         minU = x1;
         x1 = x2;
         x2 = minU;
      }

      if (y1 < y2) {
         minU = y1;
         y1 = y2;
         y2 = minU;
      }

      minU = 0.171875F;
      float maxU = 0.328125F;
      float minV = 0.0F;
      float maxV = 0.03125F;
      float deltaV = maxV - minV;
      float vPerPart = deltaV / 16.0F;
      maxV = minV + vPerPart * (float)(part + 1);
      minV += vPerPart * (float)part;
      vertex(worldrenderer, oldMatrix, x1, y2, z2).tex(0.328125D, (double)minV).normal(1.0F, 0.0F, 0.0F).endVertex();
      vertex(worldrenderer, oldMatrix, x2, y2, z2).tex(0.171875D, (double)minV).normal(1.0F, 0.0F, 0.0F).endVertex();
      vertex(worldrenderer, matrix, x2, y1, z1).tex(0.171875D, (double)maxV).normal(1.0F, 0.0F, 0.0F).endVertex();
      vertex(worldrenderer, matrix, x1, y1, z1).tex(0.328125D, (double)maxV).normal(1.0F, 0.0F, 0.0F).endVertex();
   }

   private static BufferBuilder vertex(BufferBuilder worldrenderer, Matrix4f matrix4f, float f, float g, float h) {
      Vector4f vector4f = new Vector4f(f, g, h, 1.0F);
      vector4f.transform(matrix4f);
      worldrenderer.pos((double)vector4f.x(), (double)vector4f.y(), (double)vector4f.z());
      return worldrenderer;
   }

   private static void addTopVertex(BufferBuilder worldrenderer, Matrix4f matrix, Matrix4f oldMatrix, float x1, float y1, float z1, float x2, float y2, float z2, int part) {
      float minU;
      if (x1 < x2) {
         minU = x1;
         x1 = x2;
         x2 = minU;
      }

      if (y1 < y2) {
         minU = y1;
         y1 = y2;
         y2 = minU;
      }

      minU = 0.015625F;
      float maxU = 0.171875F;
      float minV = 0.0F;
      float maxV = 0.03125F;
      float deltaV = maxV - minV;
      float vPerPart = deltaV / 16.0F;
      maxV = minV + vPerPart * (float)(part + 1);
      minV += vPerPart * (float)part;
      vertex(worldrenderer, oldMatrix, x1, y2, z1).tex(0.171875D, (double)maxV).normal(0.0F, 1.0F, 0.0F).endVertex();
      vertex(worldrenderer, oldMatrix, x2, y2, z1).tex(0.015625D, (double)maxV).normal(0.0F, 1.0F, 0.0F).endVertex();
      vertex(worldrenderer, matrix, x2, y1, z2).tex(0.015625D, (double)minV).normal(0.0F, 1.0F, 0.0F).endVertex();
      vertex(worldrenderer, matrix, x1, y1, z2).tex(0.171875D, (double)minV).normal(0.0F, 1.0F, 0.0F).endVertex();
   }
}
