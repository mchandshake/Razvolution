/* Decompiler 1ms, total 277ms, lines 17 */
package wtf.evolution.settings.config;

import java.util.ArrayList;
import java.util.List;

public abstract class Manager<T> {
   private List<T> contents = new ArrayList();

   public List<T> getContents() {
      return this.contents;
   }

   public void setContents(ArrayList<T> contents) {
      this.contents = contents;
   }
}
