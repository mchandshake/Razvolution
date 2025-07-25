/* Decompiler 2ms, total 483ms, lines 37 */
package wtf.evolution.module.impl.Player;

import java.util.Iterator;
import ru.salam4ik.bot.bot.Bot;
import wtf.evolution.command.impl.BotCommand;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventUpdate;
import wtf.evolution.helpers.animation.Counter;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.SliderSetting;

@ModuleInfo(
   name = "BotSpammer",
   type = Category.Player
)
public class BotSpammer extends Module {
   public SliderSetting delay = (new SliderSetting("Delay", 5000.0F, 0.0F, 30000.0F, 500.0F)).call(this);
   public Counter c = new Counter();

   @EventTarget
   public void onUpdate(EventUpdate e) {
      if (this.c.hasReached((double)this.delay.get())) {
         Iterator var2 = Bot.bots.iterator();

         while(var2.hasNext()) {
            Bot bot = (Bot)var2.next();
            bot.getBot().sendChatMessage(BotCommand.text);
         }

         this.c.reset();
      }

   }
}
