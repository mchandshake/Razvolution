/* Decompiler 1ms, total 246ms, lines 17 */
package wtf.evolution.event.events.callables;

import wtf.evolution.event.events.Cancellable;
import wtf.evolution.event.events.Event;

public abstract class EventCancellable implements Event, Cancellable {
   private boolean cancelled;

   public boolean isCancelled() {
      return this.cancelled;
   }

   public void cancel() {
      this.cancelled = true;
   }
}
