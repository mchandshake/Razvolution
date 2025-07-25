/* Decompiler 2ms, total 319ms, lines 22 */
package wtf.evolution.model.models;

public class ObjEvent {
   public ObjModel model;
   public wtf.evolution.model.models.ObjEvent.EventType type;
   public Object[] data = new Object[0];

   public ObjEvent(ObjModel caller, wtf.evolution.model.models.ObjEvent.EventType type) {
      this.model = caller;
      this.type = type;
   }

   public boolean canBeCancelled() {
      return this.type.isCancelable();
   }

   public ObjEvent setData(Object... data) {
      this.data = data;
      return this;
   }
}
