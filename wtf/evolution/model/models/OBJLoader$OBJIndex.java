/* Decompiler 2ms, total 356ms, lines 27 */
package wtf.evolution.model.models;

public final class OBJLoader$OBJIndex {
   int positionIndex;
   int texCoordsIndex;
   int normalIndex;

   public boolean equals(Object o) {
      if (!(o instanceof OBJLoader$OBJIndex)) {
         return false;
      } else {
         OBJLoader$OBJIndex index = (OBJLoader$OBJIndex)o;
         return index.normalIndex == this.normalIndex && index.positionIndex == this.positionIndex && index.texCoordsIndex == this.texCoordsIndex;
      }
   }

   public int hashCode() {
      boolean base = true;
      boolean multiplier = true;
      int result = 17;
      int result = 31 * result + this.positionIndex;
      result = 31 * result + this.texCoordsIndex;
      result = 31 * result + this.normalIndex;
      return result;
   }
}
