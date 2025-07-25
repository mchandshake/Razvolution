/* Decompiler 9ms, total 281ms, lines 88 */
package wtf.evolution.helpers.animation;

public abstract class Animation {
   public Counter timerUtil = new Counter();
   protected int duration;
   protected double endPoint;
   protected Direction direction;

   public Animation(int ms, double endPoint) {
      this.duration = ms;
      this.endPoint = endPoint;
      this.direction = Direction.FORWARDS;
   }

   public Animation(int ms, double endPoint, Direction direction) {
      this.duration = ms;
      this.endPoint = endPoint;
      this.direction = direction;
   }

   public boolean finished(Direction direction) {
      return this.isDone() && this.direction.equals(direction);
   }

   public double getLinearOutput() {
      return 1.0D - (double)this.timerUtil.getTimePassed() / (double)this.duration * this.endPoint;
   }

   public double getEndPoint() {
      return this.endPoint;
   }

   public void setEndPoint(double endPoint) {
      this.endPoint = endPoint;
   }

   public void reset() {
      this.timerUtil.reset();
   }

   public boolean isDone() {
      return this.timerUtil.hasReached((double)this.duration);
   }

   public void changeDirection() {
      this.setDirection(this.direction.opposite());
   }

   public Direction getDirection() {
      return this.direction;
   }

   public void setDirection(Direction direction) {
      if (this.direction != direction) {
         this.direction = direction;
         this.timerUtil.setTime(System.currentTimeMillis() - ((long)this.duration - Math.min((long)this.duration, this.timerUtil.getTimePassed())));
      }

   }

   public void setDuration(int duration) {
      this.duration = duration;
   }

   protected boolean correctOutput() {
      return false;
   }

   public long getTimePassed() {
      return this.timerUtil.getTimePassed();
   }

   public double getOutput() {
      if (this.direction == Direction.FORWARDS) {
         return this.isDone() ? this.endPoint : this.getEquation((double)this.timerUtil.getTimePassed()) * this.endPoint;
      } else if (this.isDone()) {
         return 0.0D;
      } else if (this.correctOutput()) {
         double revTime = (double)Math.min((long)this.duration, Math.max(0L, (long)this.duration - this.timerUtil.getTimePassed()));
         return this.getEquation(revTime) * this.endPoint;
      } else {
         return (1.0D - this.getEquation((double)this.timerUtil.getTimePassed())) * this.endPoint;
      }
   }

   protected abstract double getEquation(double var1);
}
