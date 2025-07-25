/* Decompiler 2ms, total 303ms, lines 24 */
package wtf.evolution.event.events.impl;

import net.minecraft.network.play.server.SPacketPlayerListItem.Action;
import net.minecraft.network.play.server.SPacketPlayerListItem.AddPlayerData;
import wtf.evolution.event.events.Event;

public class EventPlayer implements Event {
   private final AddPlayerData addPlayerData;
   private final Action action;

   public EventPlayer(AddPlayerData addPlayerData, Action action) {
      this.addPlayerData = addPlayerData;
      this.action = action;
   }

   public AddPlayerData getPlayerData() {
      return this.addPlayerData;
   }

   public Action getAction() {
      return this.action;
   }
}
