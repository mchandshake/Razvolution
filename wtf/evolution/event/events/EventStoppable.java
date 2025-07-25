/* Decompiler 1ms, total 297ms, lines 14 */
package wtf.evolution.event.events;

public abstract class EventStoppable implements Event {
   private boolean stopped;

   public void stop() {
      this.stopped = true;
   }

   public boolean isStopped() {
      return this.stopped;
   }
}
