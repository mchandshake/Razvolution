/* Decompiler 33ms, total 363ms, lines 272 */
package wtf.evolution.module.impl.Misc;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketMultiBlockChange;
import net.minecraft.network.play.server.SPacketMultiBlockChange.BlockUpdateData;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventMotion;
import wtf.evolution.event.events.impl.EventPacket;
import wtf.evolution.event.events.impl.EventRender;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.SliderSetting;

@ModuleInfo(
   name = "XrayBypass",
   type = Category.Misc
)
public class XrayBypass extends Module {
   public SliderSetting range = (new SliderSetting("Radius", 20.0F, 0.0F, 100.0F, 1.0F)).call(this);
   public SliderSetting height = (new SliderSetting("Height", 20.0F, 0.0F, 100.0F, 1.0F)).call(this);
   public SliderSetting delay = (new SliderSetting("Speed", 7.0F, 0.0F, 10.0F, 1.0F)).call(this);
   ArrayList<BlockPos> ores = new ArrayList();
   ArrayList<BlockPos> toCheck = new ArrayList();
   public static int done;
   public static int all;
   public BlockPos clicked;

   public void onEnable() {
      if (this.mc.player != null) {
         this.ores.clear();
         this.toCheck.clear();
         int radXZ = (int)this.range.get();
         int radY = (int)this.height.get();
         ArrayList<BlockPos> blockPositions = this.getBlocks(radXZ, radY, radXZ);
         Iterator var4 = blockPositions.iterator();

         while(var4.hasNext()) {
            BlockPos pos = (BlockPos)var4.next();
            IBlockState state = this.mc.world.getBlockState(pos);
            if (this.isCheckableOre(Block.getIdFromBlock(state.getBlock()))) {
               this.toCheck.add(pos);
            }
         }

         all = this.toCheck.size();
         done = 0;
         super.onEnable();
      }

   }

   public void onDisable() {
      Minecraft var10000 = this.mc;
      Minecraft.getMinecraft().renderGlobal.loadRenderers();
      super.onDisable();
   }

   @EventTarget
   public void onUpdate(EventMotion e) {
      this.setSuffix(done + "/" + all);
      if (this.toCheck.size() > 0) {
         double spd = (double)this.delay.get();

         for(int i = 0; i < (int)spd; ++i) {
            if (this.toCheck.size() < 1) {
               return;
            }

            BlockPos pos = (BlockPos)this.toCheck.remove(0);
            this.clicked = pos;
            ++done;
            this.mc.getConnection().sendPacket(new CPacketPlayerDigging(Action.START_DESTROY_BLOCK, pos, EnumFacing.UP));
         }
      }

      if (this.mc.player.ticksExisted % 10 == 0) {
         Iterator var6 = this.ores.iterator();

         while(var6.hasNext()) {
            BlockPos b = (BlockPos)var6.next();
            this.mc.getConnection().sendPacket(new CPacketPlayerDigging(Action.START_DESTROY_BLOCK, b, EnumFacing.UP));
         }
      }

   }

   private boolean isCheckableOre(int id) {
      int check = 0;
      int check1 = 0;
      int check2 = 0;
      int check3 = 0;
      int check4 = 0;
      int check5 = 0;
      int check6 = 0;
      if (id != 0) {
         check = 56;
      }

      if (id != 0) {
         check1 = 14;
      }

      if (id != 0) {
         check2 = 15;
      }

      if (id != 0) {
         check3 = 129;
      }

      if (id != 0) {
         check4 = 73;
      }

      if (id != 0) {
         check5 = 16;
      }

      if (id != 0) {
         check6 = 21;
      }

      if (id == 0) {
         return false;
      } else {
         return id == check || id == check1 || id == check2 || id == check3 || id == check4 || id == check5 || id == check6;
      }
   }

   private boolean isEnabledOre(int id) {
      int check = 0;
      int check1 = 0;
      int check2 = 0;
      int check3 = 0;
      int check4 = 0;
      int check5 = 0;
      int check6 = 0;
      if (id != 0) {
         check = 56;
      }

      if (id != 0) {
         check1 = 14;
      }

      if (id != 0) {
         check2 = 15;
      }

      if (id != 0) {
         check3 = 129;
      }

      if (id != 0) {
         check4 = 73;
      }

      if (id != 0) {
         check5 = 16;
      }

      if (id != 0) {
         check6 = 21;
      }

      if (id == 0) {
         return false;
      } else {
         return id == check || id == check1 || id == check2 || id == check3 || id == check4 || id == check5 || id == check6;
      }
   }

   @EventTarget
   public void onReceivePacket(EventPacket e) {
      if (e.getPacket() instanceof SPacketBlockChange) {
         SPacketBlockChange p = (SPacketBlockChange)e.getPacket();
         if (this.isEnabledOre(Block.getIdFromBlock(p.getBlockState().getBlock())) && !this.ores.contains(p.getBlockPosition())) {
            this.ores.add(p.getBlockPosition());
         }
      } else if (e.getPacket() instanceof SPacketMultiBlockChange) {
         SPacketMultiBlockChange p = (SPacketMultiBlockChange)e.getPacket();
         BlockUpdateData[] var3 = p.getChangedBlocks();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            BlockUpdateData dat = var3[var5];
            if (this.isEnabledOre(Block.getIdFromBlock(dat.getBlockState().getBlock())) && !this.ores.contains(dat.getPos())) {
               this.ores.add(dat.getPos());
            }
         }
      }

   }

   @EventTarget
   public void onRender(EventRender event) {
      if (this.toCheck.size() > 0) {
         RenderUtil.blockEsp(this.clicked, new Color(72, 198, 255, 255));
      }

      Iterator var2 = this.ores.iterator();

      while(var2.hasNext()) {
         BlockPos pos = (BlockPos)var2.next();
         IBlockState state = this.mc.world.getBlockState(pos);
         Block mat = state.getBlock();
         if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 56 && Block.getIdFromBlock(mat) == 56) {
            RenderUtil.blockEsp(pos, new Color(0, 255, 255, 50));
         }

         if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 14 && Block.getIdFromBlock(mat) == 14) {
            RenderUtil.blockEsp(pos, new Color(255, 215, 0, 100));
         }

         if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 15 && Block.getIdFromBlock(mat) == 15) {
            RenderUtil.blockEsp(pos, new Color(213, 213, 213, 100));
         }

         if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 129 && Block.getIdFromBlock(mat) == 129) {
            RenderUtil.blockEsp(pos, new Color(0, 255, 77, 100));
         }

         if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 73 && Block.getIdFromBlock(mat) == 73) {
            RenderUtil.blockEsp(pos, new Color(255, 0, 0, 100));
         }

         if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 16 && Block.getIdFromBlock(mat) == 16) {
            RenderUtil.blockEsp(pos, new Color(0, 0, 0, 100));
         }

         if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 21 && Block.getIdFromBlock(mat) == 21) {
            RenderUtil.blockEsp(pos, new Color(38, 97, 156, 100));
         }
      }

   }

   private ArrayList<BlockPos> getBlocks(int x, int y, int z) {
      BlockPos min = new BlockPos(this.mc.player.posX - (double)x, this.mc.player.posY - (double)y, this.mc.player.posZ - (double)z);
      BlockPos max = new BlockPos(this.mc.player.posX + (double)x, this.mc.player.posY + (double)y, this.mc.player.posZ + (double)z);
      return getAllInBox(min, max);
   }

   public static ArrayList<BlockPos> getAllInBox(BlockPos from, BlockPos to) {
      ArrayList<BlockPos> blocks = new ArrayList();
      BlockPos min = new BlockPos(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
      BlockPos max = new BlockPos(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));

      for(int x = min.getX(); x <= max.getX(); ++x) {
         for(int y = min.getY(); y <= max.getY(); ++y) {
            for(int z = min.getZ(); z <= max.getZ(); ++z) {
               blocks.add(new BlockPos(x, y, z));
            }
         }
      }

      return blocks;
   }
}
