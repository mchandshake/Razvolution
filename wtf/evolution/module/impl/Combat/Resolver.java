/* Decompiler 5ms, total 408ms, lines 71 */
package wtf.evolution.module.impl.Combat;

import java.util.Iterator;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import wtf.evolution.Main;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventUpdate;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.Setting;
import wtf.evolution.settings.options.BooleanSetting;

@ModuleInfo(
   name = "Resolver",
   type = Category.Combat
)
public class Resolver extends Module {
   public BooleanSetting auraResolver = new BooleanSetting("Aura Resolver", true);
   public BooleanSetting resolver = new BooleanSetting("Player Resolver", true);

   public Resolver() {
      this.addSettings(new Setting[]{this.auraResolver, this.resolver});
   }

   @EventTarget
   public void onResolve(EventUpdate e) {
      if (this.resolver.get()) {
         this.resolvePlayers();
      }

   }

   public void onDisable() {
      super.onDisable();
      if (this.mc.player != null) {
         this.releaseResolver();
      }

   }

   public void resolvePlayers() {
      Iterator var1 = this.mc.world.playerEntities.iterator();

      while(var1.hasNext()) {
         EntityPlayer player = (EntityPlayer)var1.next();
         if (player instanceof EntityOtherPlayerMP) {
            ((EntityOtherPlayerMP)player).resolve();
         }
      }

   }

   public void releaseResolver() {
      Iterator var1 = this.mc.world.playerEntities.iterator();

      while(var1.hasNext()) {
         EntityPlayer player = (EntityPlayer)var1.next();
         if (player instanceof EntityOtherPlayerMP) {
            ((EntityOtherPlayerMP)player).releaseResolver();
         }
      }

   }

   public static boolean resolveAura() {
      return ((Resolver)Main.m.getModule(Resolver.class)).auraResolver.get() && Main.m.getModule(Resolver.class).state;
   }
}
