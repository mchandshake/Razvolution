/* Decompiler 1ms, total 252ms, lines 16 */
package wtf.evolution.event;

import java.util.concurrent.CopyOnWriteArrayList;
import wtf.evolution.event.EventManager.MethodData;

final class EventManager$1 extends CopyOnWriteArrayList<MethodData> {
   private static final long serialVersionUID = 666L;
   // $FF: synthetic field
   final MethodData val$data;

   EventManager$1(MethodData var1) {
      this.val$data = var1;
      this.add(this.val$data);
   }
}
