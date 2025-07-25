/* Decompiler 8ms, total 290ms, lines 59 */
package wtf.evolution.helpers.render;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import wtf.evolution.helpers.render.Simulation.Point;
import wtf.evolution.helpers.render.Simulation.Stick;
import wtf.evolution.helpers.render.Simulation.Vector2;

public interface CapeHolder {
   Simulation getSimulation();

   default void updateSimulation(EntityPlayer abstractClientPlayer, int partCount) {
      Simulation simulation = this.getSimulation();
      boolean dirty = false;
      int i;
      if (simulation.points.size() != partCount) {
         simulation.points.clear();
         simulation.sticks.clear();

         for(i = 0; i < partCount; ++i) {
            Point point = new Point();
            point.position.y = (float)(-i);
            point.locked = i == 0;
            simulation.points.add(point);
            if (i > 0) {
               simulation.sticks.add(new Stick((Point)simulation.points.get(i - 1), point, 1.0F));
            }
         }

         dirty = true;
      }

      if (dirty) {
         for(i = 0; i < 10; ++i) {
            this.simulate(abstractClientPlayer);
         }
      }

   }

   default void simulate(EntityPlayer abstractClientPlayer) {
      Simulation simulation = this.getSimulation();
      if (!simulation.points.isEmpty()) {
         ((Point)simulation.points.get(0)).prevPosition.copy(((Point)simulation.points.get(0)).position);
         double d = abstractClientPlayer.chasingPosX - abstractClientPlayer.posX;
         double m = abstractClientPlayer.chasingPosZ - abstractClientPlayer.posZ;
         float n = abstractClientPlayer.prevRenderYawOffset + abstractClientPlayer.renderYawOffset - abstractClientPlayer.prevRenderYawOffset;
         double o = Math.sin((double)(n * 0.017453292F));
         double p = -Math.cos((double)(n * 0.017453292F));
         float heightMul = 10.0F;
         double fallHack = MathHelper.clamp((double)((Point)simulation.points.get(0)).position.y - abstractClientPlayer.posY * 10.0D, 0.0D, 1.0D);
         Vector2 position = ((Point)simulation.points.get(0)).position;
         position.x += (float)(d * o + m * p + fallHack);
         ((Point)simulation.points.get(0)).position.y = (float)(abstractClientPlayer.posY * 10.0D + (double)(abstractClientPlayer.isSneaking() ? -4 : 0));
         simulation.simulate();
      }
   }
}
