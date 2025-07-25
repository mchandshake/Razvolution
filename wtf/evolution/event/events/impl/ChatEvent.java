/* Decompiler 1ms, total 315ms, lines 12 */
package wtf.evolution.event.events.impl;

import wtf.evolution.event.events.Event;

public class ChatEvent implements Event {
   public String message;

   public ChatEvent(String message) {
      this.message = message;
   }
}
