/* Decompiler 1ms, total 277ms, lines 13 */
package wtf.evolution.event.events.impl;

import net.minecraft.util.EnumHandSide;
import wtf.evolution.event.events.Event;

public class EventRenderHand implements Event {
   public EnumHandSide e;

   public EventRenderHand(EnumHandSide e) {
      this.e = e;
   }
}
