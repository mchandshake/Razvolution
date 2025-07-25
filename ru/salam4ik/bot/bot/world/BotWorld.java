/* Decompiler 43ms, total 417ms, lines 306 */
package ru.salam4ik.bot.bot.world;

import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DimensionType;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.SaveDataMemoryStorage;
import net.minecraft.world.storage.SaveHandlerMP;
import net.minecraft.world.storage.WorldInfo;
import ru.salam4ik.bot.bot.entity.BotPlayer;
import ru.salam4ik.bot.bot.network.BotPlayClient;

public class BotWorld extends World {
   private ChunkProviderClient clientChunkProvider;
   private final BotPlayClient connection;
   private int playerChunkX = Integer.MIN_VALUE;
   private BotPlayer bot;
   private final Set<Entity> entitySpawnQueue = Sets.newHashSet();
   private final Set<Entity> entityList = Sets.newHashSet();
   private final Set<ChunkPos> viewableChunks = Sets.newHashSet();
   private final Set<ChunkPos> previousActiveChunkSet = Sets.newHashSet();
   private int playerChunkY = Integer.MIN_VALUE;

   public void removeAllEntities() {
      this.loadedEntityList.removeAll(this.unloadedEntityList);

      int n;
      Entity entity;
      int n2;
      for(n2 = 0; n2 < this.unloadedEntityList.size(); ++n2) {
         entity = (Entity)this.unloadedEntityList.get(n2);
         int n3 = entity.chunkCoordX;
         n = entity.chunkCoordZ;
         if (entity.addedToChunk && this.isChunkLoaded(n3, n, true)) {
            this.getChunk(n3, n).removeEntity(entity);
         }
      }

      for(n2 = 0; n2 < this.unloadedEntityList.size(); ++n2) {
         this.onEntityRemoved((Entity)this.unloadedEntityList.get(n2));
      }

      this.unloadedEntityList.clear();

      for(n2 = 0; n2 < this.loadedEntityList.size(); ++n2) {
         entity = (Entity)this.loadedEntityList.get(n2);
         Entity entity2 = entity.getRidingEntity();
         if (entity2 != null) {
            if (!entity2.isDead && entity2.isPassenger(entity)) {
               continue;
            }

            entity.dismountRidingEntity();
         }

         if (entity.isDead) {
            n = entity.chunkCoordX;
            int n4 = entity.chunkCoordZ;
            if (entity.addedToChunk && this.isChunkLoaded(n, n4, true)) {
               this.getChunk(n, n4).removeEntity(entity);
            }

            this.loadedEntityList.remove(n2--);
            this.onEntityRemoved(entity);
         }
      }

   }

   public void addEntityToWorld(int entityID, Entity entityToSpawn) {
      Entity entity = this.getEntityByID(entityID);
      if (entity != null) {
         this.removeEntity(entity);
      }

      this.entityList.add(entityToSpawn);
      entityToSpawn.setEntityId(entityID);
      if (!this.spawnEntity(entityToSpawn)) {
         this.entitySpawnQueue.add(entityToSpawn);
      }

      this.entitiesById.addKey(entityID, entityToSpawn);
   }

   public ChunkProviderClient getChunkProvider() {
      return (ChunkProviderClient)super.getChunkProvider();
   }

   public void setWorldScoreboard(Scoreboard scoreboard) {
      this.worldScoreboard = scoreboard;
   }

   public void sendPacketToServer(Packet<?> packet) {
      this.connection.sendPacket(packet);
   }

   public void tick() {
      super.tick();
      this.setTotalWorldTime(this.getTotalWorldTime() + 1L);
      if (this.getGameRules().getBoolean("doDaylightCycle")) {
         this.setWorldTime(this.getWorldTime() + 1L);
      }

      for(int i = 0; i < 10 && !this.entitySpawnQueue.isEmpty(); ++i) {
         Entity entity = (Entity)this.entitySpawnQueue.iterator().next();
         this.entitySpawnQueue.remove(entity);
         if (this.loadedEntityList.contains(entity)) {
         }
      }

      this.updateBlocks();
   }

   @Deprecated
   public boolean invalidateRegionAndSetBlock(BlockPos blockPos, IBlockState iBlockState) {
      int n = blockPos.getX();
      int n2 = blockPos.getY();
      int n3 = blockPos.getZ();
      this.invalidateBlockReceiveRegion(n, n2, n3, n, n2, n3);
      return super.setBlockState(blockPos, iBlockState, 3);
   }

   public void removeEntityFromWorld(int n) {
      Entity entity = (Entity)this.entitiesById.removeObject(n);
      if (entity != null) {
         this.entityList.remove(entity);
         this.removeEntity(entity);
      }

   }

   protected void onEntityRemoved(Entity entity) {
      super.onEntityRemoved(entity);
      if (this.entityList.contains(entity)) {
         if (entity.isEntityAlive()) {
            this.entitySpawnQueue.add(entity);
         } else {
            this.entityList.remove(entity);
         }
      }

   }

   public void invalidateBlockReceiveRegion(int n, int n2, int n3, int n4, int n5, int n6) {
   }

   public BotWorld(BotPlayClient botPlayClient, WorldSettings worldSettings, int n, EnumDifficulty enumDifficulty) {
      super(new SaveHandlerMP(), new WorldInfo(worldSettings, "MpServer"), DimensionType.getById(n).createDimension(), Minecraft.getMinecraft().profiler, true);
      this.connection = botPlayClient;
      this.getWorldInfo().setDifficulty(enumDifficulty);
      this.provider.setWorld(this);
      this.setSpawnPoint(new BlockPos(8, 64, 8));
      this.chunkProvider = this.createChunkProvider();
      this.mapStorage = new SaveDataMemoryStorage();
      this.calculateInitialSkylight();
      this.calculateInitialWeather();
   }

   public BotPlayer getBot() {
      return this.bot;
   }

   public void sendQuittingDisconnectingPacket() {
      this.connection.getNetworkManager().closeChannel();
   }

   public void setBot(BotPlayer botPlayer) {
      this.bot = botPlayer;
   }

   protected IChunkProvider createChunkProvider() {
      this.clientChunkProvider = new ChunkProviderClient(this);
      return this.clientChunkProvider;
   }

   public void playSound(double d, double d2, double d3, SoundEvent soundEvent, SoundCategory soundCategory, float f, float f2, boolean bl) {
   }

   protected boolean isChunkLoaded(int n, int n2, boolean bl) {
      return bl || !this.getChunkProvider().provideChunk(n, n2).isEmpty();
   }

   protected void onEntityAdded(Entity entity) {
      super.onEntityAdded(entity);
      this.entitySpawnQueue.remove(entity);
   }

   protected void updateWeather() {
   }

   public void setWorldTime(long l) {
      if (l < 0L) {
         l = -l;
         this.getGameRules().setOrCreateGameRule("doDaylightCycle", "false");
      } else {
         this.getGameRules().setOrCreateGameRule("doDaylightCycle", "true");
      }

      super.setWorldTime(l);
   }

   public void playSound(BlockPos blockPos, SoundEvent soundEvent, SoundCategory soundCategory, float f, float f2, boolean bl) {
   }

   protected void buildChunkCoordList() {
      int n = MathHelper.floor(this.bot.posX / 16.0D);
      int n2 = MathHelper.floor(this.bot.posZ / 16.0D);
      if (n != this.playerChunkX || n2 != this.playerChunkY) {
         this.playerChunkX = n;
         this.playerChunkY = n2;
         this.viewableChunks.clear();
         int n3 = 2;
         int n4 = MathHelper.floor(this.bot.posX / 16.0D);
         int n5 = MathHelper.floor(this.bot.posZ / 16.0D);

         for(int i = -n3; i <= n3; ++i) {
            for(int j = -n3; j <= n3; ++j) {
               this.viewableChunks.add(new ChunkPos(i + n4, j + n5));
            }
         }
      }

   }

   public void removeEntity(Entity entity) {
      super.removeEntity(entity);
      this.entityList.remove(entity);
   }

   protected void playMoodSoundAndCheckLight(int n, int n2, Chunk chunk) {
   }

   protected void updateBlocks() {
      this.buildChunkCoordList();
      this.previousActiveChunkSet.retainAll(this.viewableChunks);
      if (this.previousActiveChunkSet.size() == this.viewableChunks.size()) {
         this.previousActiveChunkSet.clear();
      }

      int n = 0;
      Iterator var2 = this.viewableChunks.iterator();

      while(var2.hasNext()) {
         ChunkPos chunkPos = (ChunkPos)var2.next();
         if (!this.previousActiveChunkSet.contains(chunkPos)) {
            int n2 = chunkPos.x * 16;
            int n3 = chunkPos.z * 16;
            Chunk chunk = this.getChunk(chunkPos.x, chunkPos.z);
            this.playMoodSoundAndCheckLight(n2, n3, chunk);
            this.previousActiveChunkSet.add(chunkPos);
            ++n;
            if (n >= 10) {
               return;
            }
         }
      }

   }

   public void makeFireworks(double d, double d2, double d3, double d4, double d5, double d6, @Nullable NBTTagCompound nBTTagCompound) {
   }

   public void doPreChunk(int n, int n2, boolean bl) {
      if (bl) {
         this.clientChunkProvider.loadChunk(n, n2);
      } else {
         this.clientChunkProvider.unloadChunk(n, n2);
         this.markBlockRangeForRenderUpdate(n * 16, 0, n2 * 16, n * 16 + 15, 256, n2 * 16 + 15);
      }

   }

   public void playSound(@Nullable EntityPlayer entityPlayer, double d, double d2, double d3, SoundEvent soundEvent, SoundCategory soundCategory, float f, float f2) {
   }

   @Nullable
   public Entity getEntityByID(int n) {
      return (Entity)(n == this.bot.getEntityId() ? this.bot : super.getEntityByID(n));
   }

   // $FF: synthetic method
   // $FF: bridge method
   public IChunkProvider getChunkProvider() {
      return this.getChunkProvider();
   }
}
