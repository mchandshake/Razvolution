/* Decompiler 3ms, total 262ms, lines 21 */
package wtf.evolution.model.models;

public enum ObjEvent$EventType {
   PRE_RENDER_ALL(true),
   PRE_RENDER_GROUPS(true),
   PRE_RENDER_GROUP(true),
   POST_RENDER_ALL(false),
   POST_RENDER_GROUPS(false),
   POST_RENDER_GROUP(false);

   private boolean cancel;

   private ObjEvent$EventType(boolean cancelable) {
      this.cancel = cancelable;
   }

   public boolean isCancelable() {
      return this.cancel;
   }
}
