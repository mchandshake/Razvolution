/* Decompiler 3ms, total 295ms, lines 15 */
package wtf.evolution.helpers.render;

public class Crypting {
   public static String get(String value, int key) {
      String result = "";

      for(int i = 0; i < value.length(); ++i) {
         char c = value.charAt(i);
         result = result + Character.toString((char)(c ^ key));
      }

      return result;
   }
}
