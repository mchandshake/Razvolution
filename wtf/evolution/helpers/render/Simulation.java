/* Decompiler 28ms, total 318ms, lines 104 */
package wtf.evolution.helpers.render;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Simulation {
   public List<wtf.evolution.helpers.render.Simulation.Point> points = new ArrayList();
   public List<wtf.evolution.helpers.render.Simulation.Stick> sticks = new ArrayList();
   public float gravity = 20.0F;
   public int numIterations = 30;
   private final float maxBend = 5.0F;

   public void simulate() {
      this.gravity = 10.0F;
      float deltaTime = 0.1F;
      wtf.evolution.helpers.render.Simulation.Vector2 down = new wtf.evolution.helpers.render.Simulation.Vector2(0.0F, this.gravity * 0.1F);
      wtf.evolution.helpers.render.Simulation.Vector2 tmp = new wtf.evolution.helpers.render.Simulation.Vector2(0.0F, 0.0F);
      Iterator var4 = this.points.iterator();

      while(var4.hasNext()) {
         wtf.evolution.helpers.render.Simulation.Point p = (wtf.evolution.helpers.render.Simulation.Point)var4.next();
         if (!p.locked) {
            tmp.copy(p.position);
            p.position.subtract(down);
            p.prevPosition.copy(tmp);
         }
      }

      wtf.evolution.helpers.render.Simulation.Point basePoint = (wtf.evolution.helpers.render.Simulation.Point)this.points.get(0);
      Iterator var11 = this.points.iterator();

      while(var11.hasNext()) {
         wtf.evolution.helpers.render.Simulation.Point p2 = (wtf.evolution.helpers.render.Simulation.Point)var11.next();
         if (p2 != basePoint && p2.position.x - basePoint.position.x > 0.0F) {
            p2.position.x = basePoint.position.x - 0.1F;
         }
      }

      int i;
      for(i = this.points.size() - 2; i >= 1; --i) {
         double angle = this.getAngle(((wtf.evolution.helpers.render.Simulation.Point)this.points.get(i)).position, ((wtf.evolution.helpers.render.Simulation.Point)this.points.get(i - 1)).position, ((wtf.evolution.helpers.render.Simulation.Point)this.points.get(i + 1)).position);
         angle *= 57.2958D;
         if (angle > 360.0D) {
            angle -= 360.0D;
         }

         if (angle < -360.0D) {
            angle += 360.0D;
         }

         double abs = Math.abs(angle);
         if (abs < (double)(180.0F - this.maxBend)) {
            ((wtf.evolution.helpers.render.Simulation.Point)this.points.get(i + 1)).position = this.getReplacement(((wtf.evolution.helpers.render.Simulation.Point)this.points.get(i)).position, ((wtf.evolution.helpers.render.Simulation.Point)this.points.get(i - 1)).position, angle, (double)(180.0F - this.maxBend + 1.0F));
         }

         if (abs > (double)(180.0F + this.maxBend)) {
            ((wtf.evolution.helpers.render.Simulation.Point)this.points.get(i + 1)).position = this.getReplacement(((wtf.evolution.helpers.render.Simulation.Point)this.points.get(i)).position, ((wtf.evolution.helpers.render.Simulation.Point)this.points.get(i - 1)).position, angle, (double)(180.0F + this.maxBend - 1.0F));
         }
      }

      for(i = 0; i < this.numIterations; ++i) {
         for(int x = this.sticks.size() - 1; x >= 0; --x) {
            wtf.evolution.helpers.render.Simulation.Stick stick = (wtf.evolution.helpers.render.Simulation.Stick)this.sticks.get(x);
            wtf.evolution.helpers.render.Simulation.Vector2 stickCentre = stick.pointA.position.clone().add(stick.pointB.position).div(2.0F);
            wtf.evolution.helpers.render.Simulation.Vector2 stickDir = stick.pointA.position.clone().subtract(stick.pointB.position).normalize();
            if (!stick.pointA.locked) {
               stick.pointA.position = stickCentre.clone().add(stickDir.clone().mul(stick.length / 2.0F));
            }

            if (!stick.pointB.locked) {
               stick.pointB.position = stickCentre.clone().subtract(stickDir.clone().mul(stick.length / 2.0F));
            }
         }
      }

      for(i = 0; i < this.sticks.size(); ++i) {
         wtf.evolution.helpers.render.Simulation.Stick stick2 = (wtf.evolution.helpers.render.Simulation.Stick)this.sticks.get(i);
         wtf.evolution.helpers.render.Simulation.Vector2 stickDir2 = stick2.pointA.position.clone().subtract(stick2.pointB.position).normalize();
         if (!stick2.pointB.locked) {
            stick2.pointB.position = stick2.pointA.position.clone().subtract(stickDir2.mul(stick2.length));
         }
      }

   }

   private wtf.evolution.helpers.render.Simulation.Vector2 getReplacement(wtf.evolution.helpers.render.Simulation.Vector2 middle, wtf.evolution.helpers.render.Simulation.Vector2 prev, double angle, double target) {
      double theta = target / 57.2958D;
      float x = prev.x - middle.x;
      float y = prev.y - middle.y;
      if (angle < 0.0D) {
         theta *= -1.0D;
      }

      double cs = Math.cos(theta);
      double sn = Math.sin(theta);
      return new wtf.evolution.helpers.render.Simulation.Vector2((float)((double)x * cs - (double)y * sn + (double)middle.x), (float)((double)x * sn + (double)y * cs + (double)middle.y));
   }

   private double getAngle(wtf.evolution.helpers.render.Simulation.Vector2 middle, wtf.evolution.helpers.render.Simulation.Vector2 prev, wtf.evolution.helpers.render.Simulation.Vector2 next) {
      return Math.atan2((double)(next.y - middle.y), (double)(next.x - middle.x)) - Math.atan2((double)(prev.y - middle.y), (double)(prev.x - middle.x));
   }
}
