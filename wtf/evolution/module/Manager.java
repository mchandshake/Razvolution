/* Decompiler 12ms, total 371ms, lines 211 */
package wtf.evolution.module;

import java.util.ArrayList;
import java.util.Iterator;
import wtf.evolution.module.impl.Combat.AntiBot;
import wtf.evolution.module.impl.Combat.AutoArmor;
import wtf.evolution.module.impl.Combat.AutoPotion;
import wtf.evolution.module.impl.Combat.AutoTotem;
import wtf.evolution.module.impl.Combat.BowAimBot;
import wtf.evolution.module.impl.Combat.KillAura;
import wtf.evolution.module.impl.Combat.Resolver;
import wtf.evolution.module.impl.Combat.SuperKnockback;
import wtf.evolution.module.impl.Combat.TargetHud;
import wtf.evolution.module.impl.Combat.Velocity;
import wtf.evolution.module.impl.Misc.BeaconExploit;
import wtf.evolution.module.impl.Misc.ItemSwapFix;
import wtf.evolution.module.impl.Misc.PasswordHider;
import wtf.evolution.module.impl.Misc.PlayerLogger;
import wtf.evolution.module.impl.Misc.XrayBypass;
import wtf.evolution.module.impl.Movement.Flight;
import wtf.evolution.module.impl.Movement.GuiWalk;
import wtf.evolution.module.impl.Movement.HighJump;
import wtf.evolution.module.impl.Movement.Jesus;
import wtf.evolution.module.impl.Movement.LongJump;
import wtf.evolution.module.impl.Movement.NoClip;
import wtf.evolution.module.impl.Movement.NoPush;
import wtf.evolution.module.impl.Movement.NoSlow;
import wtf.evolution.module.impl.Movement.NoWaterCollision;
import wtf.evolution.module.impl.Movement.Speed;
import wtf.evolution.module.impl.Movement.Spider;
import wtf.evolution.module.impl.Movement.Sprint;
import wtf.evolution.module.impl.Movement.Strafe;
import wtf.evolution.module.impl.Movement.WebLeave;
import wtf.evolution.module.impl.Player.AntiLagMachine;
import wtf.evolution.module.impl.Player.AutoLeave;
import wtf.evolution.module.impl.Player.AutoRespawn;
import wtf.evolution.module.impl.Player.Baritone;
import wtf.evolution.module.impl.Player.BotSpammer;
import wtf.evolution.module.impl.Player.ClickTP;
import wtf.evolution.module.impl.Player.Freecam;
import wtf.evolution.module.impl.Player.GAppleTimer;
import wtf.evolution.module.impl.Player.InventoryDroper;
import wtf.evolution.module.impl.Player.ItemScroller;
import wtf.evolution.module.impl.Player.MiddleClickFriend;
import wtf.evolution.module.impl.Player.MiddleClickPearl;
import wtf.evolution.module.impl.Player.NoCom;
import wtf.evolution.module.impl.Player.NoDelay;
import wtf.evolution.module.impl.Player.NoFall;
import wtf.evolution.module.impl.Player.NoRotationSet;
import wtf.evolution.module.impl.Player.Optimizer;
import wtf.evolution.module.impl.Player.StaffAlert;
import wtf.evolution.module.impl.Player.Timer;
import wtf.evolution.module.impl.Player.XCarry;
import wtf.evolution.module.impl.Render.BotStatictics;
import wtf.evolution.module.impl.Render.CameraClip;
import wtf.evolution.module.impl.Render.Chams;
import wtf.evolution.module.impl.Render.ChinaHat;
import wtf.evolution.module.impl.Render.ClickGui;
import wtf.evolution.module.impl.Render.ClientSound;
import wtf.evolution.module.impl.Render.CustomModel;
import wtf.evolution.module.impl.Render.ESP;
import wtf.evolution.module.impl.Render.EnchantmentColor;
import wtf.evolution.module.impl.Render.ExtraTab;
import wtf.evolution.module.impl.Render.FeatureList;
import wtf.evolution.module.impl.Render.FogColor;
import wtf.evolution.module.impl.Render.FullBright;
import wtf.evolution.module.impl.Render.Hotbar;
import wtf.evolution.module.impl.Render.ItemESP;
import wtf.evolution.module.impl.Render.JumpCircle;
import wtf.evolution.module.impl.Render.MotionGraph;
import wtf.evolution.module.impl.Render.NameTag;
import wtf.evolution.module.impl.Render.NoRender;
import wtf.evolution.module.impl.Render.Predict;
import wtf.evolution.module.impl.Render.SwingAnimation;
import wtf.evolution.module.impl.Render.Trails;
import wtf.evolution.module.impl.Render.TriangleESP;
import wtf.evolution.module.impl.Render.ViewModel;
import wtf.evolution.module.impl.Render.Watermark;
import wtf.evolution.module.impl.Render.WorldColor;
import wtf.evolution.module.impl.Render.WorldTime;

public class Manager {
   public ArrayList<Module> m = new ArrayList();

   public Manager() {
      this.m.add(new AntiBot());
      this.m.add(new AutoArmor());
      this.m.add(new AutoTotem());
      this.m.add(new BowAimBot());
      this.m.add(new KillAura());
      this.m.add(new Resolver());
      this.m.add(new Velocity());
      this.m.add(new AutoPotion());
      this.m.add(new SuperKnockback());
      this.m.add(new BeaconExploit());
      this.m.add(new ItemSwapFix());
      this.m.add(new PasswordHider());
      this.m.add(new PlayerLogger());
      this.m.add(new XrayBypass());
      this.m.add(new Flight());
      this.m.add(new GuiWalk());
      this.m.add(new Jesus());
      this.m.add(new NoClip());
      this.m.add(new NoSlow());
      this.m.add(new Speed());
      this.m.add(new Spider());
      this.m.add(new Sprint());
      this.m.add(new Strafe());
      this.m.add(new NoPush());
      this.m.add(new NoWaterCollision());
      this.m.add(new HighJump());
      this.m.add(new LongJump());
      this.m.add(new WebLeave());
      this.m.add(new AutoRespawn());
      this.m.add(new MiddleClickPearl());
      this.m.add(new ItemScroller());
      this.m.add(new NoDelay());
      this.m.add(new Timer());
      this.m.add(new XCarry());
      this.m.add(new NoCom());
      this.m.add(new NoFall());
      this.m.add(new NoRotationSet());
      this.m.add(new AntiLagMachine());
      this.m.add(new Baritone());
      this.m.add(new Freecam());
      this.m.add(new GAppleTimer());
      this.m.add(new AutoLeave());
      this.m.add(new InventoryDroper());
      this.m.add(new BotSpammer());
      this.m.add(new Optimizer());
      this.m.add(new ClickTP());
      this.m.add(new CustomModel());
      this.m.add(new BotStatictics());
      this.m.add(new ExtraTab());
      this.m.add(new ViewModel());
      this.m.add(new MiddleClickFriend());
      this.m.add(new Chams());
      this.m.add(new CameraClip());
      this.m.add(new SwingAnimation());
      this.m.add(new NameTag());
      this.m.add(new JumpCircle());
      this.m.add(new Trails());
      this.m.add(new ClickGui());
      this.m.add(new EnchantmentColor());
      this.m.add(new ESP());
      this.m.add(new FeatureList());
      this.m.add(new FogColor());
      this.m.add(new FullBright());
      this.m.add(new NoRender());
      this.m.add(new Predict());
      this.m.add(new Watermark());
      this.m.add(new WorldColor());
      this.m.add(new WorldTime());
      this.m.add(new Hotbar());
      this.m.add(new StaffAlert());
      this.m.add(new MotionGraph());
      this.m.add(new ItemESP());
      this.m.add(new ClientSound());
      this.m.add(new ChinaHat());
      this.m.add(new TriangleESP());
      this.m.add(new TargetHud());
      this.m.sort((o1, o2) -> {
         return o1.getSettings().size() >= o2.getSettings().size() ? 1 : -1;
      });
   }

   public Module getModule(String name) {
      Iterator var2 = this.m.iterator();

      Module m;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         m = (Module)var2.next();
      } while(!m.name.equals(name));

      return m;
   }

   public ArrayList<Module> getModulesFromCategory(Category category) {
      ArrayList<Module> modules = new ArrayList();
      Iterator var3 = this.m.iterator();

      while(var3.hasNext()) {
         Module m = (Module)var3.next();
         if (m.category == category) {
            modules.add(m);
         }
      }

      return modules;
   }

   public Module getModule(Class<?> clazz) {
      Iterator var2 = this.m.iterator();

      Module m;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         m = (Module)var2.next();
      } while(m.getClass() != clazz);

      return m;
   }
}
