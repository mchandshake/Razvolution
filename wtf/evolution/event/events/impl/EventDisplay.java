/* Decompiler 1ms, total 255ms, lines 15 */
package wtf.evolution.event.events.impl;

import net.minecraft.client.gui.ScaledResolution;
import wtf.evolution.event.events.Event;

public class EventDisplay implements Event {
   public float ticks;
   public ScaledResolution sr;

   public EventDisplay(float t, ScaledResolution sr) {
      this.sr = sr;
      this.ticks = t;
   }
}
