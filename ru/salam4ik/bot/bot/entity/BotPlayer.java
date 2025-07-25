/* Decompiler 127ms, total 3286ms, lines 806 */
package ru.salam4ik.bot.bot.entity;

import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.inventory.ContainerBrewingStand;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.inventory.ContainerShulkerBox;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerAbilities;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.client.CPacketClientStatus.State;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.network.play.client.CPacketPlayer.PositionRotation;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.RecipeBook;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.MovementInput;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IInteractionObject;
import ru.salam4ik.bot.bot.network.BotPlayClient;
import wtf.evolution.helpers.animation.Counter;

public class BotPlayer extends AbstractClientPlayer {
   private float lastReportedYaw;
   private float lastReportedPitch;
   private float horseJumpPower;
   public float renderArmPitch;
   private int permissionLevel = 0;
   private String serverBrand;
   public float prevTimeInPortal;
   private int horseJumpPowerCounter;
   private RecipeBook field_192036_cb;
   private boolean serverSneakState;
   private double lastReportedPosY;
   public int sprintingTicksLeft;
   public float renderArmYaw;
   private boolean prevOnGround;
   public float timeInPortal;
   private boolean handActive;
   private EnumHand activeHand;
   private double lastReportedPosZ;
   private StatisticsManager statWriter;
   public Counter c;
   private double lastReportedPosX;
   private boolean rowingBoat;
   private boolean autoJumpEnabled = true;
   public String currentContainerName = "";
   public float prevRenderArmPitch;
   public final BotPlayClient connection;
   public float prevRenderArmYaw;
   public MovementInput movementInput;
   private int autoJumpTime;
   private boolean hasValidHealth;
   protected int sprintToggleTimer;
   private boolean wasFallFlying;
   private boolean serverSprintState;
   private int positionUpdateTicks;

   public void openBook(ItemStack itemStack, EnumHand enumHand) {
   }

   public boolean isSneaking() {
      boolean bl = this.movementInput != null && this.movementInput.sneak;
      return bl && !this.sleeping;
   }

   public float getHorseJumpPower() {
      return this.horseJumpPower;
   }

   public void displayVillagerTradeGui(IMerchant iMerchant) {
   }

   public void playSound(SoundEvent soundEvent, float f, float f2) {
      this.world.playSound(this.posX, this.posY, this.posZ, soundEvent, this.getSoundCategory(), f, f2, false);
   }

   public void onEnchantmentCritical(Entity entity) {
   }

   public Vec3d getLook(float f) {
      return getVectorForRotation(this.rotationPitch, this.rotationYaw);
   }

   public void setXPStats(float f, int n, int n2) {
      this.experience = f;
      this.experienceTotal = n;
      this.experienceLevel = n2;
   }

   public void sendHorseInventory() {
      this.connection.sendPacket(new CPacketEntityAction(this, Action.OPEN_INVENTORY));
   }

   public void handleStatusUpdate(byte by) {
      if (by >= 24 && by <= 28) {
         this.setPermissionLevel(by - 24);
      } else {
         super.handleStatusUpdate(by);
      }

   }

   public int getPermissionLevel() {
      return this.permissionLevel;
   }

   public void displayGuiCommandBlock(TileEntityCommandBlock tileEntityCommandBlock) {
   }

   public void swingArm(EnumHand enumHand) {
      super.swingArm(enumHand);
      this.connection.sendPacket(new CPacketAnimation(enumHand));
   }

   public String getServerBrand() {
      return this.serverBrand;
   }

   public void openHorseInventory(AbstractHorse abstractHorse, IInventory iInventory) {
   }

   public StatisticsManager getStatFileWriter() {
      return this.statWriter;
   }

   public void move(MoverType moverType, double d, double d2, double d3) {
      double d4 = this.posX;
      double d5 = this.posZ;
      super.move(moverType, d, d2, d3);
      this.updateAutoJump((float)(this.posX - d4), (float)(this.posZ - d5));
   }

   public boolean attackEntityFrom(DamageSource damageSource, float f) {
      return false;
   }

   public EnumHand getActiveHand() {
      return this.activeHand;
   }

   public void closeScreen() {
      this.connection.sendPacket(new CPacketCloseWindow(this.openContainer.windowId));
      this.closeScreenAndDropStack();
   }

   public boolean startRiding(Entity entity, boolean bl) {
      if (!super.startRiding(entity, bl)) {
         return false;
      } else {
         if (entity instanceof EntityMinecart) {
         }

         if (entity instanceof EntityBoat) {
            this.prevRotationYaw = entity.rotationYaw;
            this.rotationYaw = entity.rotationYaw;
            this.setRotationYawHead(entity.rotationYaw);
         }

         return true;
      }
   }

   public void setSprinting(boolean bl) {
      super.setSprinting(bl);
      this.sprintingTicksLeft = 0;
   }

   public void displayGUIChest(IInventory iInventory) {
      String string = this.inventory instanceof IInteractionObject ? ((IInteractionObject)this.inventory).getGuiID() : "minecraft:container";
      if ("minecraft:chest".equals(string)) {
         this.openContainer = new ContainerChest(this.inventory, this.inventory, this);
      } else if ("minecraft:hopper".equals(string)) {
         this.openContainer = new ContainerHopper(this.inventory, this.inventory, this);
      } else if ("minecraft:furnace".equals(string)) {
         this.openContainer = new ContainerFurnace(this.inventory, this.inventory);
      } else if ("minecraft:brewing_stand".equals(string)) {
         this.openContainer = new ContainerBrewingStand(this.inventory, this.inventory);
      } else if ("minecraft:beacon".equals(string)) {
         this.openContainer = new ContainerBeacon(this.inventory, this.inventory);
      } else if (!"minecraft:dispenser".equals(string) && !"minecraft:dropper".equals(string)) {
         if ("minecraft:shulker_box".equals(string)) {
            this.openContainer = new ContainerShulkerBox(this.inventory, this.inventory, this);
         }
      } else {
         this.openContainer = new ContainerDispenser(this.inventory, this.inventory);
      }

   }

   public void respawnPlayer() {
      this.connection.sendPacket(new CPacketClientStatus(State.PERFORM_RESPAWN));
   }

   public void dismountRidingEntity() {
      super.dismountRidingEntity();
      this.rowingBoat = false;
   }

   public void sendChatMessage(String string) {
      this.connection.sendPacket(new CPacketChatMessage(string));
   }

   public void openEditStructure(TileEntityStructure tileEntityStructure) {
   }

   protected void updateAutoJump(float f, float f2) {
      if (this.isAutoJumpEnabled() && this.autoJumpTime <= 0 && this.onGround && !this.isSneaking() && !this.isRiding()) {
         Vec2f vec2f = this.movementInput.getMoveVector();
         if (vec2f.x != 0.0F || vec2f.y != 0.0F) {
            Vec3d vec3d = new Vec3d(this.posX, this.getEntityBoundingBox().minY, this.posZ);
            double d = this.posX + (double)f;
            double d2 = this.posZ + (double)f2;
            Vec3d vec3d2 = new Vec3d(d, this.getEntityBoundingBox().minY, d2);
            Vec3d vec3d3 = new Vec3d((double)f, 0.0D, (double)f2);
            float f5 = this.getAIMoveSpeed();
            float f6 = (float)vec3d3.lengthSquared();
            float f3;
            float f4;
            if (f6 <= 0.001F) {
               f4 = f5 * vec2f.x;
               float f7 = f5 * vec2f.y;
               float f8 = MathHelper.sin(this.rotationYaw * 0.017453292F);
               f3 = MathHelper.cos(this.rotationYaw * 0.017453292F);
               vec3d3 = new Vec3d((double)(f4 * f3 - f7 * f8), vec3d3.y, (double)(f7 * f3 + f4 * f8));
               f6 = (float)vec3d3.lengthSquared();
               if (f6 <= 0.001F) {
                  return;
               }
            }

            f4 = (float)MathHelper.fastInvSqrt((double)f6);
            Vec3d vec3d4 = vec3d3.scale((double)f4);
            Vec3d vec3d5 = this.getForward();
            f3 = (float)(vec3d5.x * vec3d4.x + vec3d5.z * vec3d4.z);
            BlockPos blockPos;
            if (f3 >= -0.15F && this.world.getBlockState(blockPos = new BlockPos(this.posX, this.getEntityBoundingBox().maxY, this.posZ)).getCollisionBoundingBox(this.world, blockPos) == null && this.world.getBlockState(blockPos = blockPos.up()).getCollisionBoundingBox(this.world, blockPos) == null) {
               float f10 = 7.0F;
               float f11 = 1.2F;
               if (this.isPotionActive(MobEffects.JUMP_BOOST)) {
                  f11 += (float)(this.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.75F;
               }

               float f12 = Math.max(f5 * 7.0F, 1.0F / f4);
               Vec3d vec3d6 = vec3d2.add(vec3d4.scale((double)f12));
               float f13 = this.width;
               float f14 = this.height;
               AxisAlignedBB axisAlignedBB = (new AxisAlignedBB(vec3d, vec3d6.add(0.0D, (double)f14, 0.0D))).expand((double)f13, 0.0D, (double)f13);
               Vec3d vec3d7 = vec3d.add(0.0D, 0.5099999904632568D, 0.0D);
               vec3d6 = vec3d6.add(0.0D, 0.5099999904632568D, 0.0D);
               Vec3d vec3d8 = vec3d4.crossProduct(new Vec3d(0.0D, 1.0D, 0.0D));
               Vec3d vec3d9 = vec3d8.scale((double)(f13 * 0.5F));
               Vec3d vec3d10 = vec3d7.subtract(vec3d9);
               Vec3d vec3d11 = vec3d6.subtract(vec3d9);
               Vec3d vec3d12 = vec3d7.add(vec3d9);
               Vec3d vec3d13 = vec3d6.add(vec3d9);
               List<AxisAlignedBB> list = this.world.getCollisionBoxes(this, axisAlignedBB);
               if (!list.isEmpty()) {
               }

               float f15 = Float.MIN_VALUE;
               Iterator var37 = list.iterator();

               while(true) {
                  AxisAlignedBB axisAlignedBB2;
                  do {
                     if (!var37.hasNext()) {
                        float f9;
                        if (f15 != Float.MIN_VALUE && (f9 = (float)((double)f15 - this.getEntityBoundingBox().minY)) > 0.5F && f9 <= f11) {
                           this.autoJumpTime = 1;
                        }

                        return;
                     }

                     axisAlignedBB2 = (AxisAlignedBB)var37.next();
                  } while(!axisAlignedBB2.intersects(vec3d10, vec3d11) && !axisAlignedBB2.intersects(vec3d12, vec3d13));

                  f15 = (float)axisAlignedBB2.maxY;
                  Vec3d vec3d14 = axisAlignedBB2.getCenter();
                  BlockPos blockPos2 = new BlockPos(vec3d14);

                  for(int n = 1; (float)n < f11; ++n) {
                     BlockPos blockPos3 = blockPos2.up(n);
                     IBlockState iBlockState4 = this.world.getBlockState(blockPos3);
                     AxisAlignedBB axisAlignedBB3 = iBlockState4.getCollisionBoundingBox(this.world, blockPos3);
                     if (axisAlignedBB3 != null && (double)(f15 = (float)axisAlignedBB3.maxY + (float)blockPos3.getY()) - this.getEntityBoundingBox().minY > (double)f11) {
                        return;
                     }

                     if (n > 1 && this.world.getBlockState(blockPos = blockPos.up()).getCollisionBoundingBox(this.world, blockPos) != null) {
                        return;
                     }
                  }
               }
            }
         }
      }

   }

   protected void damageEntity(DamageSource damageSource, float f) {
      if (!this.isEntityInvulnerable(damageSource)) {
         this.setHealth(this.getHealth() - f);
      }

   }

   public boolean isRidingHorse() {
      Entity entity = this.getRidingEntity();
      return this.isRiding() && entity instanceof IJumpingMount && ((IJumpingMount)entity).canJump();
   }

   @Nullable
   public EntityItem dropItem(boolean bl) {
      net.minecraft.network.play.client.CPacketPlayerDigging.Action action = bl ? net.minecraft.network.play.client.CPacketPlayerDigging.Action.DROP_ALL_ITEMS : net.minecraft.network.play.client.CPacketPlayerDigging.Action.DROP_ITEM;
      this.connection.sendPacket(new CPacketPlayerDigging(action, BlockPos.ORIGIN, EnumFacing.DOWN));
      return null;
   }

   public boolean isHandActive() {
      return this.handActive;
   }

   public void onUpdateWalkingPlayer() {
      boolean bl3 = this.isSprinting();
      if (bl3 != this.serverSprintState) {
         if (bl3) {
            this.connection.sendPacket(new CPacketEntityAction(this, Action.START_SPRINTING));
         } else {
            this.connection.sendPacket(new CPacketEntityAction(this, Action.STOP_SPRINTING));
         }

         this.serverSprintState = bl3;
      }

      boolean bl2;
      if ((bl2 = this.isSneaking()) != this.serverSneakState) {
         if (bl2) {
            this.connection.sendPacket(new CPacketEntityAction(this, Action.START_SNEAKING));
         } else {
            this.connection.sendPacket(new CPacketEntityAction(this, Action.STOP_SNEAKING));
         }

         this.serverSneakState = bl2;
      }

      AxisAlignedBB axisAlignedBB = this.getEntityBoundingBox();
      double d = this.posX - this.lastReportedPosX;
      double d2 = axisAlignedBB.minY - this.lastReportedPosY;
      double d3 = this.posZ - this.lastReportedPosZ;
      double d4 = (double)(this.rotationYaw - this.lastReportedYaw);
      double d5 = (double)(this.rotationPitch - this.lastReportedPitch);
      ++this.positionUpdateTicks;
      boolean bl4 = d * d + d2 * d2 + d3 * d3 > 9.0E-4D || this.positionUpdateTicks >= 20;
      boolean bl = d4 != 0.0D || d5 != 0.0D;
      if (this.isRiding()) {
         this.connection.sendPacket(new PositionRotation(this.motionX, -999.0D, this.motionZ, this.rotationYaw, this.rotationPitch, this.onGround));
         bl4 = false;
      } else if (bl4 && bl) {
         this.connection.sendPacket(new PositionRotation(this.posX, axisAlignedBB.minY, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround));
      } else if (bl4) {
         this.connection.sendPacket(new Position(this.posX, axisAlignedBB.minY, this.posZ, this.onGround));
      } else if (bl) {
         this.connection.sendPacket(new Rotation(this.rotationYaw, this.rotationPitch, this.onGround));
      } else if (this.prevOnGround != this.onGround) {
         this.connection.sendPacket(new CPacketPlayer(this.onGround));
      }

      if (bl4) {
         this.lastReportedPosX = this.posX;
         this.lastReportedPosY = axisAlignedBB.minY;
         this.lastReportedPosZ = this.posZ;
         this.positionUpdateTicks = 0;
      }

      if (bl) {
         this.lastReportedYaw = this.rotationYaw;
         this.lastReportedPitch = this.rotationPitch;
      }

      this.prevOnGround = this.onGround;
   }

   public void updateEntityActionState() {
      super.updateEntityActionState();
      this.moveStrafing = this.movementInput.moveStrafe;
      this.moveForward = this.movementInput.moveForward;
      this.isJumping = this.movementInput.jump;
      this.prevRenderArmYaw = this.renderArmYaw;
      this.prevRenderArmPitch = this.renderArmPitch;
      this.renderArmPitch = (float)((double)this.renderArmPitch + (double)(this.rotationPitch - this.renderArmPitch) * 0.5D);
      this.renderArmYaw = (float)((double)this.renderArmYaw + (double)(this.rotationYaw - this.renderArmYaw) * 0.5D);
   }

   public boolean isUser() {
      return true;
   }

   public void updateRidden() {
      super.updateRidden();
      this.rowingBoat = false;
      if (this.getRidingEntity() instanceof EntityBoat) {
         EntityBoat entityBoat = (EntityBoat)this.getRidingEntity();
         entityBoat.updateInputs(this.movementInput.leftKeyDown, this.movementInput.rightKeyDown, this.movementInput.forwardKeyDown, this.movementInput.backKeyDown);
         this.rowingBoat |= this.movementInput.leftKeyDown || this.movementInput.rightKeyDown || this.movementInput.forwardKeyDown || this.movementInput.backKeyDown;
      }

   }

   public void setPermissionLevel(int n) {
      this.permissionLevel = n;
   }

   public void displayGuiEditCommandCart(CommandBlockBaseLogic commandBlockBaseLogic) {
   }

   public void sendPlayerAbilities() {
      this.connection.sendPacket(new CPacketPlayerAbilities(this.capabilities));
   }

   public void onLivingUpdate() {
      ++this.sprintingTicksLeft;
      if (this.sprintToggleTimer > 0) {
         --this.sprintToggleTimer;
      }

      this.prevTimeInPortal = this.timeInPortal;
      if (this.inPortal) {
         if (this.timeInPortal == 0.0F) {
         }

         this.timeInPortal += 0.0125F;
         if (this.timeInPortal >= 1.0F) {
            this.timeInPortal = 1.0F;
         }

         this.inPortal = false;
      } else if (this.isPotionActive(MobEffects.NAUSEA) && this.getActivePotionEffect(MobEffects.NAUSEA).getDuration() > 60) {
         this.timeInPortal += 0.006666667F;
         if (this.timeInPortal > 1.0F) {
            this.timeInPortal = 1.0F;
         }
      } else {
         if (this.timeInPortal > 0.0F) {
            this.timeInPortal -= 0.05F;
         }

         if (this.timeInPortal < 0.0F) {
            this.timeInPortal = 0.0F;
         }
      }

      if (this.timeUntilPortal > 0) {
         --this.timeUntilPortal;
      }

      boolean bl2 = this.movementInput.jump;
      boolean bl3 = this.movementInput.sneak;
      float f = 0.8F;
      boolean bl4 = this.movementInput.moveForward >= 0.8F;
      this.movementInput.updatePlayerMoveState();
      if (this.isHandActive() && !this.isRiding()) {
         MovementInput var10000 = this.movementInput;
         var10000.moveStrafe *= 0.2F;
         var10000 = this.movementInput;
         var10000.moveForward *= 0.2F;
         this.sprintToggleTimer = 0;
      }

      boolean bl5 = false;
      if (this.autoJumpTime > 0) {
         --this.autoJumpTime;
         bl5 = true;
         this.movementInput.jump = true;
      }

      AxisAlignedBB axisAlignedBB = this.getEntityBoundingBox();
      this.pushOutOfBlocks(this.posX - (double)this.width * 0.35D, axisAlignedBB.minY + 0.5D, this.posZ + (double)this.width * 0.35D);
      this.pushOutOfBlocks(this.posX - (double)this.width * 0.35D, axisAlignedBB.minY + 0.5D, this.posZ - (double)this.width * 0.35D);
      this.pushOutOfBlocks(this.posX + (double)this.width * 0.35D, axisAlignedBB.minY + 0.5D, this.posZ - (double)this.width * 0.35D);
      this.pushOutOfBlocks(this.posX + (double)this.width * 0.35D, axisAlignedBB.minY + 0.5D, this.posZ + (double)this.width * 0.35D);
      boolean bl = (float)this.getFoodStats().getFoodLevel() > 6.0F || this.capabilities.allowFlying;
      if (this.onGround && !bl3 && !bl4 && this.movementInput.moveForward >= 0.8F && !this.isSprinting() && bl && !this.isHandActive() && !this.isPotionActive(MobEffects.BLINDNESS)) {
         this.setSprinting(true);
      }

      if (!this.isSprinting() && this.movementInput.moveForward >= 0.8F && bl && !this.isHandActive() && !this.isPotionActive(MobEffects.BLINDNESS)) {
         this.setSprinting(true);
      }

      if (this.isSprinting() && (this.movementInput.moveForward < 0.8F || this.collidedHorizontally || !bl)) {
         this.setSprinting(false);
      }

      if (this.capabilities.allowFlying) {
         if (this.connection.getController().isSpectatorMode()) {
            if (!this.capabilities.isFlying) {
               this.capabilities.isFlying = true;
               this.sendPlayerAbilities();
            }
         } else if (!bl2 && this.movementInput.jump && !bl5) {
            if (this.flyToggleTimer == 0) {
               this.flyToggleTimer = 7;
            } else {
               this.capabilities.isFlying = !this.capabilities.isFlying;
               this.sendPlayerAbilities();
               this.flyToggleTimer = 0;
            }
         }
      }

      ItemStack object;
      if (this.movementInput.jump && !bl2 && !this.onGround && this.motionY < 0.0D && !this.isElytraFlying() && !this.capabilities.isFlying && ((ItemStack)((ItemStack)(object = this.getItemStackFromSlot(EntityEquipmentSlot.CHEST)))).getItem() == Items.ELYTRA && !ItemElytra.isUsable((ItemStack)object)) {
         this.connection.sendPacket(new CPacketEntityAction(this, Action.START_FALL_FLYING));
      }

      this.wasFallFlying = this.isElytraFlying();
      if (this.capabilities.isFlying) {
         if (this.movementInput.sneak) {
            this.movementInput.moveStrafe = (float)((double)this.movementInput.moveStrafe / 0.3D);
            this.movementInput.moveForward = (float)((double)this.movementInput.moveForward / 0.3D);
            this.motionY -= (double)(this.capabilities.getFlySpeed() * 3.0F);
         }

         if (this.movementInput.jump) {
            this.motionY += (double)(this.capabilities.getFlySpeed() * 3.0F);
         }
      }

      if (this.isRidingHorse()) {
         Object object = (IJumpingMount)this.getRidingEntity();
         if (this.horseJumpPowerCounter < 0) {
            ++this.horseJumpPowerCounter;
            if (this.horseJumpPowerCounter == 0) {
               this.horseJumpPower = 0.0F;
            }
         }

         if (bl2 && !this.movementInput.jump) {
            this.horseJumpPowerCounter = -10;
            ((IJumpingMount)object).setJumpPower(MathHelper.floor(this.getHorseJumpPower() * 100.0F));
            this.sendHorseJump();
         } else if (!bl2 && this.movementInput.jump) {
            this.horseJumpPowerCounter = 0;
            this.horseJumpPower = 0.0F;
         } else if (bl2) {
            ++this.horseJumpPowerCounter;
            this.horseJumpPower = this.horseJumpPowerCounter < 10 ? (float)this.horseJumpPowerCounter * 0.1F : 0.8F + 2.0F / (float)(this.horseJumpPowerCounter - 9) * 0.1F;
         }
      } else {
         this.horseJumpPower = 0.0F;
      }

      super.onLivingUpdate();
      if (this.onGround && this.capabilities.isFlying && !this.connection.getController().isSpectatorMode()) {
         this.capabilities.isFlying = false;
         this.sendPlayerAbilities();
      }

   }

   public void displayGui(IInteractionObject iInteractionObject) {
   }

   public void setActiveHand(EnumHand enumHand) {
      ItemStack itemStack = this.getHeldItem(enumHand);
      if (!itemStack.isEmpty() && !this.isHandActive()) {
         super.setActiveHand(enumHand);
         this.handActive = true;
         this.activeHand = enumHand;
      }

   }

   public void setServerBrand(String string) {
      this.serverBrand = string;
   }

   public boolean isAutoJumpEnabled() {
      return this.autoJumpEnabled;
   }

   public void openEditSign(TileEntitySign tileEntitySign) {
   }

   public void notifyDataManagerChange(DataParameter<?> dataParameter) {
      super.notifyDataManagerChange(dataParameter);
      if (HAND_STATES.equals(dataParameter)) {
         boolean bl = ((Byte)this.dataManager.get(HAND_STATES) & 1) > 0;
         EnumHand enumHand = ((Byte)this.dataManager.get(HAND_STATES) & 2) > 0 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
         if (bl && !this.handActive) {
            this.setActiveHand(enumHand);
         } else if (!bl && this.handActive) {
            this.resetActiveHand();
         }
      }

      if (FLAGS.equals(dataParameter) && this.isElytraFlying() && !this.wasFallFlying) {
      }

   }

   protected void sendHorseJump() {
      this.connection.sendPacket(new CPacketEntityAction(this, Action.START_RIDING_JUMP, MathHelper.floor(this.getHorseJumpPower() * 100.0F)));
   }

   public void closeScreenAndDropStack() {
      this.inventory.setItemStack(ItemStack.EMPTY);
      super.closeScreen();
   }

   public void addStat(StatBase statBase, int n) {
      if (statBase != null && statBase.isIndependent) {
         super.addStat(statBase, n);
      }

   }

   public BlockPos getPosition() {
      return new BlockPos(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D);
   }

   protected ItemStack dropItemAndGetStack(EntityItem entityItem) {
      return ItemStack.EMPTY;
   }

   public void setPlayerSPHealth(float f) {
      if (this.hasValidHealth) {
         float f2 = this.getHealth() - f;
         if (f2 <= 0.0F) {
            this.setHealth(f);
            if (f2 < 0.0F) {
               this.hurtResistantTime = this.maxHurtResistantTime / 2;
            }
         } else {
            this.lastDamage = f2;
            this.setHealth(this.getHealth());
            this.hurtResistantTime = this.maxHurtResistantTime;
            this.damageEntity(DamageSource.GENERIC, f2);
            this.hurtTime = this.maxHurtTime = 10;
         }
      } else {
         this.setHealth(f);
         this.hasValidHealth = true;
      }

   }

   private boolean isOpenBlockSpace(BlockPos blockPos) {
      return !this.world.getBlockState(blockPos).isNormalCube() && !this.world.getBlockState(blockPos.up()).isNormalCube();
   }

   public BotPlayer(BotPlayClient botPlayClient) {
      super(botPlayClient.getWorld(), botPlayClient.getGameProfile());
      this.connection = botPlayClient;
      this.dimension = 0;
      this.c = new Counter();
   }

   protected boolean pushOutOfBlocks(double d, double d2, double d3) {
      if (this.noClip) {
         return false;
      } else {
         BlockPos blockPos = new BlockPos(d, d2, d3);
         double d4 = d - (double)blockPos.getX();
         double d5 = d3 - (double)blockPos.getZ();
         if (!this.isOpenBlockSpace(blockPos)) {
            int n = -1;
            double d6 = 9999.0D;
            if (this.isOpenBlockSpace(blockPos.west()) && d4 < d6) {
               d6 = d4;
               n = 0;
            }

            if (this.isOpenBlockSpace(blockPos.east()) && 1.0D - d4 < d6) {
               d6 = 1.0D - d4;
               n = 1;
            }

            if (this.isOpenBlockSpace(blockPos.north()) && d5 < d6) {
               d6 = d5;
               n = 4;
            }

            if (this.isOpenBlockSpace(blockPos.south()) && 1.0D - d5 < d6) {
               d6 = 1.0D - d5;
               n = 5;
            }

            float f = 0.1F;
            if (n == 0) {
               this.motionX = -0.10000000149011612D;
            }

            if (n == 1) {
               this.motionX = 0.10000000149011612D;
            }

            if (n == 4) {
               this.motionZ = -0.10000000149011612D;
            }

            if (n == 5) {
               this.motionZ = 0.10000000149011612D;
            }
         }

         return false;
      }
   }

   public void onUpdate() {
      if (this.world.isBlockLoaded(new BlockPos(this.posX, 0.0D, this.posZ))) {
         try {
            super.onUpdate();
         } catch (NullPointerException var2) {
            this.connection.sendPacket(new SPacketDisconnect());
         }

         if (this.isRiding()) {
            this.connection.sendPacket(new Rotation(this.rotationYaw, this.rotationPitch, this.onGround));
            this.connection.sendPacket(new CPacketInput(this.moveStrafing, this.moveForward, this.movementInput.jump, this.movementInput.sneak));
            Entity entity = this.getLowestRidingEntity();
            if (entity != this && entity.canPassengerSteer()) {
               this.connection.sendPacket(new CPacketVehicleMove(entity));
            }
         } else {
            this.onUpdateWalkingPlayer();
         }
      }

   }

   public boolean isServerWorld() {
      return true;
   }

   public void heal(float f) {
   }

   public void onCriticalHit(Entity entity) {
   }

   @Nullable
   public PotionEffect removeActivePotionEffect(@Nullable Potion potion) {
      if (potion == MobEffects.NAUSEA) {
         this.prevTimeInPortal = 0.0F;
         this.timeInPortal = 0.0F;
      }

      return super.removeActivePotionEffect(potion);
   }

   public void resetActiveHand() {
      super.resetActiveHand();
      this.handActive = false;
   }
}
