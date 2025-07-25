/* Decompiler 9ms, total 378ms, lines 20 */
package wtf.evolution.helpers.render.cape;

public final class PoseStack$Pose {
   final Matrix4f pose;
   final Matrix3f normal;

   PoseStack$Pose(Matrix4f matrix4f, Matrix3f matrix3f) {
      this.pose = matrix4f;
      this.normal = matrix3f;
   }

   public Matrix4f pose() {
      return this.pose;
   }

   public Matrix3f normal() {
      return this.normal;
   }
}
