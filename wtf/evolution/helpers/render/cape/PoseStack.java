/* Decompiler 7ms, total 293ms, lines 73 */
package wtf.evolution.helpers.render.cape;

import com.google.common.collect.Queues;
import java.util.Deque;

public class PoseStack {
   private final Deque<wtf.evolution.helpers.render.cape.PoseStack.Pose> poseStack = Queues.newArrayDeque();

   public PoseStack() {
      Matrix4f matrix4f = new Matrix4f();
      matrix4f.setIdentity();
      Matrix3f matrix3f = new Matrix3f();
      matrix3f.setIdentity();
      this.poseStack.add(new wtf.evolution.helpers.render.cape.PoseStack.Pose(matrix4f, matrix3f));
   }

   public void translate(double d, double e, double f) {
      wtf.evolution.helpers.render.cape.PoseStack.Pose pose = (wtf.evolution.helpers.render.cape.PoseStack.Pose)this.poseStack.getLast();
      pose.pose.multiplyWithTranslation((float)d, (float)e, (float)f);
   }

   public void scale(float f, float g, float h) {
      wtf.evolution.helpers.render.cape.PoseStack.Pose pose = (wtf.evolution.helpers.render.cape.PoseStack.Pose)this.poseStack.getLast();
      pose.pose.multiply(Matrix4f.createScaleMatrix(f, g, h));
      if (f == g && g == h) {
         if (f > 0.0F) {
            return;
         }

         pose.normal.mul(-1.0F);
      }

      float i = 1.0F / f;
      float j = 1.0F / g;
      float k = 1.0F / h;
      float l = Mth.fastInvCubeRoot(i * j * k);
      pose.normal.mul(Matrix3f.createScaleMatrix(l * i, l * j, l * k));
   }

   public void mulPose(Quaternion quaternion) {
      wtf.evolution.helpers.render.cape.PoseStack.Pose pose = (wtf.evolution.helpers.render.cape.PoseStack.Pose)this.poseStack.getLast();
      pose.pose.multiply(quaternion);
      pose.normal.mul(quaternion);
   }

   public void pushPose() {
      wtf.evolution.helpers.render.cape.PoseStack.Pose pose = (wtf.evolution.helpers.render.cape.PoseStack.Pose)this.poseStack.getLast();
      this.poseStack.addLast(new wtf.evolution.helpers.render.cape.PoseStack.Pose(pose.pose.copy(), pose.normal.copy()));
   }

   public void popPose() {
      this.poseStack.removeLast();
   }

   public wtf.evolution.helpers.render.cape.PoseStack.Pose last() {
      return (wtf.evolution.helpers.render.cape.PoseStack.Pose)this.poseStack.getLast();
   }

   public boolean clear() {
      return this.poseStack.size() == 1;
   }

   public void setIdentity() {
      wtf.evolution.helpers.render.cape.PoseStack.Pose pose = (wtf.evolution.helpers.render.cape.PoseStack.Pose)this.poseStack.getLast();
      pose.pose.setIdentity();
      pose.normal.setIdentity();
   }

   public void mulPoseMatrix(Matrix4f matrix4f) {
      ((wtf.evolution.helpers.render.cape.PoseStack.Pose)this.poseStack.getLast()).pose.multiply(matrix4f);
   }
}
