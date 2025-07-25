/* Decompiler 4ms, total 267ms, lines 11 */
package wtf.evolution.helpers.animation;

public enum Direction {
   FORWARDS,
   BACKWARDS;

   public Direction opposite() {
      return this == FORWARDS ? BACKWARDS : FORWARDS;
   }
}
