/* Decompiler 3ms, total 545ms, lines 38 */
package wtf.evolution.helpers.animation;

public class Counter {
   private long lastMS = -1L;

   public Counter() {
      this.lastMS = System.currentTimeMillis();
   }

   public boolean hasReached(double delay) {
      return (double)(System.currentTimeMillis() - this.lastMS) >= delay;
   }

   public boolean hasReached(boolean active, double delay) {
      return active || this.hasReached(delay);
   }

   public long getLastMS() {
      return this.lastMS;
   }

   public void reset() {
      this.lastMS = System.currentTimeMillis();
   }

   public long getTimePassed() {
      return System.currentTimeMillis() - this.lastMS;
   }

   public long getCurrentTime() {
      return System.nanoTime() / 1000000L;
   }

   public void setTime(long time) {
      this.lastMS = time;
   }
}
