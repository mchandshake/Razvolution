/* Decompiler 1ms, total 312ms, lines 11 */
package wtf.evolution.settings;

import java.util.function.Supplier;

public class Setting extends Config {
   public String name;
   public Supplier<Boolean> hidden = () -> {
      return false;
   };
}
