/* Decompiler 2ms, total 340ms, lines 30 */
package wtf.evolution.module.impl.Render;

import java.awt.Color;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventRender;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;

@ModuleInfo(
   name = "ItemESP",
   type = Category.Render
)
public class ItemESP extends Module {
   @EventTarget
   public void onRender(EventRender e) {
      Iterator var2 = this.mc.world.getEntities(EntityItem.class, Entity::isEntityAlive).iterator();

      while(var2.hasNext()) {
         EntityItem item = (EntityItem)var2.next();
         RenderUtil.renderItem(item, new Color(255, 255, 255, 100), e.pt);
      }

   }
}
