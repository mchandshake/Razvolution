/* Decompiler 1ms, total 258ms, lines 28 */
package wtf.evolution.event;

import java.lang.reflect.Method;

final class EventManager$MethodData {
   private final Object source;
   private final Method target;
   private final byte priority;

   public EventManager$MethodData(Object source, Method target, byte priority) {
      this.source = source;
      this.target = target;
      this.priority = priority;
   }

   public Object getSource() {
      return this.source;
   }

   public Method getTarget() {
      return this.target;
   }

   public byte getPriority() {
      return this.priority;
   }
}
