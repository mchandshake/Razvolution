/* Decompiler 3ms, total 365ms, lines 40 */
package wtf.evolution.module.impl.Combat;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventMotion;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;

@ModuleInfo(
   name = "AntiBot",
   type = Category.Combat
)
public class AntiBot extends Module {
   public static ArrayList<Entity> isBot = new ArrayList();

   @EventTarget
   public void onMotion(EventMotion e) {
      Iterator var2 = this.mc.world.loadedEntityList.iterator();

      while(var2.hasNext()) {
         Entity entity = (Entity)var2.next();
         if (!entity.getUniqueID().equals(UUID.nameUUIDFromBytes(("OfflinePlayer:" + entity.getName()).getBytes(StandardCharsets.UTF_8))) && entity instanceof EntityOtherPlayerMP && !isBot.contains(entity)) {
            isBot.add(entity);
         }
      }

   }

   public void onDisable() {
      super.onDisable();
      isBot.clear();
   }
}
