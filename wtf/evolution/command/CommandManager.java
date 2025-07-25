/* Decompiler 2ms, total 256ms, lines 33 */
package wtf.evolution.command;

import java.util.ArrayList;
import wtf.evolution.command.impl.BindCommand;
import wtf.evolution.command.impl.BotCommand;
import wtf.evolution.command.impl.ConfigCommand;
import wtf.evolution.command.impl.CrashCommand;
import wtf.evolution.command.impl.FriendCommand;
import wtf.evolution.command.impl.HClipCommand;
import wtf.evolution.command.impl.HelpCommand;
import wtf.evolution.command.impl.KickCommand;
import wtf.evolution.command.impl.SettingCommand;
import wtf.evolution.command.impl.ToggleCommand;
import wtf.evolution.command.impl.VClipCommand;

public class CommandManager {
   public ArrayList<Command> cmds = new ArrayList();

   public CommandManager() {
      this.cmds.add(new VClipCommand());
      this.cmds.add(new HClipCommand());
      this.cmds.add(new HelpCommand());
      this.cmds.add(new ToggleCommand());
      this.cmds.add(new ConfigCommand());
      this.cmds.add(new FriendCommand());
      this.cmds.add(new BindCommand());
      this.cmds.add(new SettingCommand());
      this.cmds.add(new BotCommand());
      this.cmds.add(new KickCommand());
      this.cmds.add(new CrashCommand());
   }
}
