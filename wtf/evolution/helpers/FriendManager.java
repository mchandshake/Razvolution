/* Decompiler 2ms, total 682ms, lines 20 */
package wtf.evolution.helpers;

import java.util.ArrayList;

public class FriendManager {
   public ArrayList<String> friends = new ArrayList();

   public void add(String name) {
      this.friends.add(name);
   }

   public void remove(String name) {
      this.friends.remove(name);
   }

   public boolean isFriend(String name) {
      return this.friends.contains(name);
   }
}
