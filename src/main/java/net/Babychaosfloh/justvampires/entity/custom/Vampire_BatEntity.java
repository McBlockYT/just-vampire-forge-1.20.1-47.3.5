package net.Babychaosfloh.justvampires.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

//9:30
public class Vampire_BatEntity extends FlyingMob implements Enemy {
    public static final float FLAP_DEGREES_PER_TICK = 74.48451F;
    public static final int TICKS_PER_FLAP = Mth.ceil(2.4166098F);
    private static final EntityDataAccessor<Byte> DATA_ID_FLAGS = SynchedEntityData.defineId(Vampire_BatEntity.class, EntityDataSerializers.BYTE);
    private static final int FLAG_RESTING = 1;
    private static final TargetingConditions BAT_RESTING_TARGETING = TargetingConditions.forNonCombat().range(4.0D);
    @Nullable
    private BlockPos targetPosition;
    Vec3 moveTargetPoint = Vec3.ZERO;
    Vampire_BatEntity.AttackPhase attackPhase = Vampire_BatEntity.AttackPhase.CIRCLE;
    BlockPos anchorPoint = BlockPos.ZERO;


    public Vampire_BatEntity(EntityType<? extends FlyingMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        if (!pLevel.isClientSide) {
            this.setResting(true);
        }
        this.xpReward = 5;
        this.moveControl = new Vampire_BatEntity.VampBatMoveControl(this);
        //this.lookControl = new Vampire_BatEntity.VampBatLookControl(this);

    }

    public boolean isFlapping() {
        return !this.isResting() && this.tickCount % TICKS_PER_FLAP == 0;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_FLAGS, (byte)0);
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume() {
        return 0.1F;
    }

    /**
     * Gets the pitch of living sounds in living entities.
     */
    public float getVoicePitch() {
        return super.getVoicePitch() * 0.95F;
    }

    @Nullable
    public SoundEvent getAmbientSound() {
        return this.isResting() && this.random.nextInt(4) != 0 ? null : SoundEvents.BAT_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.BAT_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.BAT_DEATH;
    }

    /**
     * Returns {@code true} if this entity should push and be pushed by other entities when colliding.
     */
    public boolean isPushable() {
        return false;
    }

    protected void doPush(Entity pEntity) {
    }

    protected void pushEntities() {
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 6.0D)
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                .add(Attributes.FLYING_SPEED, 0.6D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D);
    }

    public boolean isResting() {
        return (this.entityData.get(DATA_ID_FLAGS) & 1) != 0;
    }

    public void setResting(boolean pIsResting) {
        byte b0 = this.entityData.get(DATA_ID_FLAGS);
        if (pIsResting) {
            this.entityData.set(DATA_ID_FLAGS, (byte)(b0 | 1));
        } else {
            this.entityData.set(DATA_ID_FLAGS, (byte)(b0 & -2));
        }

    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new Vampire_BatEntity.VampBatAttackStrategyGoal());
        this.goalSelector.addGoal(2, new Vampire_BatEntity.VampBatSweepAttackGoal());
        //this.goalSelector.addGoal(3, new Vampire_BatEntity.VampBatCircleAroundAnchorGoal());
        this.targetSelector.addGoal(1, new Vampire_BatEntity.VampBatAttackPlayerTargetGoal());
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void tick() {
        super.tick();
        if (this.isResting()) {
            this.setDeltaMovement(Vec3.ZERO);
            this.setPosRaw(this.getX(), (double) Mth.floor(this.getY()) + 1.0D - (double)this.getBbHeight(), this.getZ());
        } else {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));
        }

    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        BlockPos blockpos = this.blockPosition();
        BlockPos blockpos1 = blockpos.above();
        if (this.isResting()) {
            boolean flag = this.isSilent();
            if (this.level().getBlockState(blockpos1).isRedstoneConductor(this.level(), blockpos)) {
                if (this.random.nextInt(200) == 0) {
                    this.yHeadRot = (float)this.random.nextInt(360);
                }

                if (this.level().getNearestPlayer(BAT_RESTING_TARGETING, this) != null) {
                    this.setResting(false);
                    if (!flag) {
                        this.level().levelEvent((Player)null, 1025, blockpos, 0);
                    }
                }
            } else {
                this.setResting(false);
                if (!flag) {
                    this.level().levelEvent((Player)null, 1025, blockpos, 0);
                }
            }
        } else {
            if (this.targetPosition != null && (!this.level().isEmptyBlock(this.targetPosition) || this.targetPosition.getY() <= this.level().getMinBuildHeight())) {
                this.targetPosition = null;
            }

            if (this.targetPosition == null || this.random.nextInt(30) == 0 || this.targetPosition.closerToCenterThan(this.position(), 2.0D)) {
                this.targetPosition = BlockPos.containing(this.getX() + (double)this.random.nextInt(7) - (double)this.random.nextInt(7), this.getY() + (double)this.random.nextInt(6) - 2.0D, this.getZ() + (double)this.random.nextInt(7) - (double)this.random.nextInt(7));
            }

            double d2 = (double)this.targetPosition.getX() + 0.5D - this.getX();
            double d0 = (double)this.targetPosition.getY() + 0.1D - this.getY();
            double d1 = (double)this.targetPosition.getZ() + 0.5D - this.getZ();
            Vec3 vec3 = this.getDeltaMovement();
            Vec3 vec31 = vec3.add((Math.signum(d2) * 0.5D - vec3.x) * (double)0.1F, (Math.signum(d0) * (double)0.7F - vec3.y) * (double)0.1F, (Math.signum(d1) * 0.5D - vec3.z) * (double)0.1F);
            this.setDeltaMovement(vec31);
            float f = (float)(Mth.atan2(vec31.z, vec31.x) * (double)(180F / (float)Math.PI)) - 90.0F;
            float f1 = Mth.wrapDegrees(f - this.getYRot());
            this.zza = 0.5F;
            this.setYRot(this.getYRot() + f1);
            if (this.random.nextInt(100) == 0 && this.level().getBlockState(blockpos1).isRedstoneConductor(this.level(), blockpos1)) {
                this.setResting(true);
            }
        }

    }

    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.EVENTS;
    }

    protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
    }

    /**
     * Return whether this entity should NOT trigger a pressure plate or a tripwire.
     */
    public boolean isIgnoringBlockTriggers() {
        return true;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.isInvulnerableTo(pSource)) {
            return false;
        } else {
            if (!this.level().isClientSide && this.isResting()) {
                this.setResting(false);
            }

            return super.hurt(pSource, pAmount);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.entityData.set(DATA_ID_FLAGS, pCompound.getByte("BatFlags"));
        if (pCompound.contains("AX")) {
            this.anchorPoint = new BlockPos(pCompound.getInt("AX"), pCompound.getInt("AY"), pCompound.getInt("AZ"));
        }
    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putByte("BatFlags", this.entityData.get(DATA_ID_FLAGS));
    }

    public static boolean checkBatSpawnRules(EntityType<Vampire_BatEntity> pVampire_Bat, LevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        if (pPos.getY() >= pLevel.getSeaLevel()) {
            return false;
        } else {
            int i = pLevel.getMaxLocalRawBrightness(pPos);
            int j = 4;
            if (isHalloween()) {
                j = 7;
            } else if (pRandom.nextBoolean()) {
                return false;
            }

            return i > pRandom.nextInt(j) ? false : checkMobSpawnRules(pVampire_Bat, pLevel, pSpawnType, pPos, pRandom);
        }
    }

    private static boolean isHalloween() {
        LocalDate localdate = LocalDate.now();
        int i = localdate.get(ChronoField.DAY_OF_MONTH);
        int j = localdate.get(ChronoField.MONTH_OF_YEAR);
        return j == 10 && i >= 20 || j == 11 && i <= 3;
    }

    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pSize) {
        return pSize.height / 2.0F;
    }

    public boolean canAttackType(EntityType<?> pType) {
        return true;
    }

    enum AttackPhase {
        CIRCLE,
        SWOOP;
    }

    class VampBatAttackPlayerTargetGoal extends Goal {
        private final TargetingConditions attackTargeting = TargetingConditions.forCombat().range(64.0D);
        private int nextScanTick = reducedTickDelay(20);

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            if (this.nextScanTick > 0) {
                --this.nextScanTick;
                return false;
            } else {
                this.nextScanTick = reducedTickDelay(60);
                List<Player> list = Vampire_BatEntity.this.level().getNearbyPlayers(this.attackTargeting, Vampire_BatEntity.this, Vampire_BatEntity.this.getBoundingBox().inflate(0.0D, 0.0D, 0.0D));
                if (!list.isEmpty()) {
                    list.sort(Comparator.<Entity, Double>comparing(Entity::getY).reversed());

                    for(Player player : list) {
                        if (Vampire_BatEntity.this.canAttack(player, TargetingConditions.DEFAULT)) {
                            Vampire_BatEntity.this.setTarget(player);
                            return true;
                        }
                    }
                }

                return false;
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            LivingEntity livingentity = Vampire_BatEntity.this.getTarget();
            return livingentity != null ? Vampire_BatEntity.this.canAttack(livingentity, TargetingConditions.DEFAULT) : false;
        }
    }

    class VampBatAttackStrategyGoal extends Goal {
        private int nextSweepTick;

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            LivingEntity livingentity = Vampire_BatEntity.this.getTarget();
            return livingentity != null ? Vampire_BatEntity.this.canAttack(livingentity, TargetingConditions.DEFAULT) : false;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            this.nextSweepTick = this.adjustedTickDelay(10);
            Vampire_BatEntity.this.attackPhase = Vampire_BatEntity.AttackPhase.CIRCLE;
            this.setAnchorAboveTarget();
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void stop() {
            Vampire_BatEntity.this.anchorPoint = Vampire_BatEntity.this.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, Vampire_BatEntity.this.anchorPoint).above(10 + Vampire_BatEntity.this.random.nextInt(20));
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            if (!isResting()) {
                if (Vampire_BatEntity.this.attackPhase == Vampire_BatEntity.AttackPhase.CIRCLE) {
                    --this.nextSweepTick;
                    if (this.nextSweepTick <= 0) {
                        Vampire_BatEntity.this.attackPhase = Vampire_BatEntity.AttackPhase.SWOOP;
                        this.setAnchorAboveTarget();
                        this.nextSweepTick = this.adjustedTickDelay((8 + Vampire_BatEntity.this.random.nextInt(4)) * 20);
                    }
                }
            }

        }

        private void setAnchorAboveTarget() {
            Vampire_BatEntity.this.anchorPoint = Vampire_BatEntity.this.getTarget().blockPosition().above(20 + Vampire_BatEntity.this.random.nextInt(20));
            if (Vampire_BatEntity.this.anchorPoint.getY() < Vampire_BatEntity.this.level().getSeaLevel()) {
                Vampire_BatEntity.this.anchorPoint = new BlockPos(Vampire_BatEntity.this.anchorPoint.getX(), Vampire_BatEntity.this.level().getSeaLevel() + 1, Vampire_BatEntity.this.anchorPoint.getZ());
            }

        }
    }

    class VampBatBodyRotationControl extends BodyRotationControl {
        public VampBatBodyRotationControl(Mob pMob) {
            super(pMob);
        }

        /**
         * Update the Head and Body rendering angles
         */
        public void clientTick() {
            Vampire_BatEntity.this.yHeadRot = Vampire_BatEntity.this.yBodyRot;
            Vampire_BatEntity.this.yBodyRot = Vampire_BatEntity.this.getYRot();
        }
    }

    class VampBatCircleAroundAnchorGoal extends VampBatMoveTargetGoal {
        private float angle;
        private float distance;
        private float height;
        private float clockwise;

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            return Vampire_BatEntity.this.getTarget() == null || Vampire_BatEntity.this.attackPhase == Vampire_BatEntity.AttackPhase.CIRCLE;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            this.distance = 5.0F + Vampire_BatEntity.this.random.nextFloat() * 10.0F;
            this.height = -4.0F + Vampire_BatEntity.this.random.nextFloat() * 9.0F;
            this.clockwise = Vampire_BatEntity.this.random.nextBoolean() ? 1.0F : -1.0F;
            this.selectNext();
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            if (Vampire_BatEntity.this.random.nextInt(this.adjustedTickDelay(350)) == 0) {
                this.height = -4.0F + Vampire_BatEntity.this.random.nextFloat() * 9.0F;
            }

            if (Vampire_BatEntity.this.random.nextInt(this.adjustedTickDelay(250)) == 0) {
                ++this.distance;
                if (this.distance > 15.0F) {
                    this.distance = 5.0F;
                    this.clockwise = -this.clockwise;
                }
            }

            if (Vampire_BatEntity.this.random.nextInt(this.adjustedTickDelay(450)) == 0) {
                this.angle = Vampire_BatEntity.this.random.nextFloat() * 2.0F * (float)Math.PI;
                this.selectNext();
            }

            if (this.touchingTarget()) {
                this.selectNext();
            }

            if (Vampire_BatEntity.this.moveTargetPoint.y < Vampire_BatEntity.this.getY() && !Vampire_BatEntity.this.level().isEmptyBlock(Vampire_BatEntity.this.blockPosition().below(1))) {
                this.height = Math.max(1.0F, this.height);
                this.selectNext();
            }

            if (Vampire_BatEntity.this.moveTargetPoint.y > Vampire_BatEntity.this.getY() && !Vampire_BatEntity.this.level().isEmptyBlock(Vampire_BatEntity.this.blockPosition().above(1))) {
                this.height = Math.min(-1.0F, this.height);
                this.selectNext();
            }

        }

        private void selectNext() {
            if (BlockPos.ZERO.equals(Vampire_BatEntity.this.anchorPoint)) {
                Vampire_BatEntity.this.anchorPoint = Vampire_BatEntity.this.blockPosition();
            }

            this.angle += this.clockwise * 15.0F * ((float)Math.PI / 180F);
            Vampire_BatEntity.this.moveTargetPoint = Vec3.atLowerCornerOf(Vampire_BatEntity.this.anchorPoint).add((double)(this.distance * Mth.cos(this.angle)), (double)(-4.0F + this.height), (double)(this.distance * Mth.sin(this.angle)));
        }
    }

    class VampBatLookControl extends LookControl {
        public VampBatLookControl(Mob pMob) {
            super(pMob);
        }

        /**
         * Updates look
         */
        public void tick() {
        }
    }

    class VampBatMoveControl extends MoveControl {
        private float speed = 0.1F;
        public VampBatMoveControl(Mob pMob) {
            super(pMob);
        }

        public void tick() {
            if (Vampire_BatEntity.this.horizontalCollision) {
                Vampire_BatEntity.this.setYRot(Vampire_BatEntity.this.getYRot() + 180.0F);
                this.speed = 0.1F;
            }

            double d0 = Vampire_BatEntity.this.moveTargetPoint.x - Vampire_BatEntity.this.getX();
            double d1 = Vampire_BatEntity.this.moveTargetPoint.y - Vampire_BatEntity.this.getY();
            double d2 = Vampire_BatEntity.this.moveTargetPoint.z - Vampire_BatEntity.this.getZ();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            if (Math.abs(d3) > (double)1.0E-5F) {
                double d4 = 1.0D - Math.abs(d1 * (double)0.7F) / d3;
                d0 *= d4;
                d2 *= d4;
                d3 = Math.sqrt(d0 * d0 + d2 * d2);
                double d5 = Math.sqrt(d0 * d0 + d2 * d2 + d1 * d1);
                float f = Vampire_BatEntity.this.getYRot();
                float f1 = (float)Mth.atan2(d2, d0);
                float f2 = Mth.wrapDegrees(Vampire_BatEntity.this.getYRot() + 90.0F);
                float f3 = Mth.wrapDegrees(f1 * (180F / (float)Math.PI));
                Vampire_BatEntity.this.setYRot(Mth.approachDegrees(f2, f3, 4.0F) - 90.0F);
                Vampire_BatEntity.this.yBodyRot = Vampire_BatEntity.this.getYRot();
                if (Mth.degreesDifferenceAbs(f, Vampire_BatEntity.this.getYRot()) < 3.0F) {
                    this.speed = Mth.approach(this.speed, 1.8F, 0.005F * (1.8F / this.speed));
                } else {
                    this.speed = Mth.approach(this.speed, 0.2F, 0.025F);
                }

                float f4 = (float)(-(Mth.atan2(-d1, d3) * (double)(180F / (float)Math.PI)));
                Vampire_BatEntity.this.setXRot(f4);
                float f5 = Vampire_BatEntity.this.getYRot() + 90.0F;
                double d6 = (double)(this.speed * Mth.cos(f5 * ((float)Math.PI / 180F))) * Math.abs(d0 / d5);
                double d7 = (double)(this.speed * Mth.sin(f5 * ((float)Math.PI / 180F))) * Math.abs(d2 / d5);
                double d8 = (double)(this.speed * Mth.sin(f4 * ((float)Math.PI / 180F))) * Math.abs(d1 / d5);
                Vec3 vec3 = Vampire_BatEntity.this.getDeltaMovement();
                Vampire_BatEntity.this.setDeltaMovement(vec3.add((new Vec3(d6, d8, d7)).subtract(vec3).scale(0.2D)));
            }

        }
    }

    abstract class VampBatMoveTargetGoal extends Goal {
        public VampBatMoveTargetGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        protected boolean touchingTarget() {
            return Vampire_BatEntity.this.moveTargetPoint.distanceToSqr(Vampire_BatEntity.this.getX(), Vampire_BatEntity.this.getY(), Vampire_BatEntity.this.getZ()) < 4.0D;
        }
    }

    class VampBatSweepAttackGoal extends VampBatMoveTargetGoal {
        private static final int CAT_SEARCH_TICK_DELAY = 20;
        private boolean isScaredOfCat;
        private int catSearchTick;

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            return Vampire_BatEntity.this.getTarget() != null && Vampire_BatEntity.this.attackPhase == Vampire_BatEntity.AttackPhase.SWOOP;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            LivingEntity livingentity = Vampire_BatEntity.this.getTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else {
                if (livingentity instanceof Player) {
                    Player player = (Player)livingentity;
                    if (livingentity.isSpectator() || player.isCreative()) {
                        return false;
                    }
                }

                if (!this.canUse()) {
                    return false;
                } else {
                    if (Vampire_BatEntity.this.tickCount > this.catSearchTick) {
                        this.catSearchTick = Vampire_BatEntity.this.tickCount + 20;
                        List<Cat> list = Vampire_BatEntity.this.level().getEntitiesOfClass(Cat.class, Vampire_BatEntity.this.getBoundingBox().inflate(16.0D), EntitySelector.ENTITY_STILL_ALIVE);

                        for(Cat cat : list) {
                            cat.hiss();
                        }

                        this.isScaredOfCat = !list.isEmpty();
                    }

                    return !this.isScaredOfCat;
                }
            }
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void stop() {
            Vampire_BatEntity.this.setTarget((LivingEntity)null);
            Vampire_BatEntity.this.attackPhase = Vampire_BatEntity.AttackPhase.CIRCLE;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            LivingEntity livingentity = Vampire_BatEntity.this.getTarget();
            if (livingentity != null) {
                Vampire_BatEntity.this.moveTargetPoint = new Vec3(livingentity.getX(), livingentity.getY(0.5D), livingentity.getZ());
                if (Vampire_BatEntity.this.getBoundingBox().inflate((double)0.2F).intersects(livingentity.getBoundingBox())) {
                    Vampire_BatEntity.this.doHurtTarget(livingentity);
                    Vampire_BatEntity.this.attackPhase = Vampire_BatEntity.AttackPhase.CIRCLE;
                    if (!Vampire_BatEntity.this.isSilent()) {
                        Vampire_BatEntity.this.level().levelEvent(1039, Vampire_BatEntity.this.blockPosition(), 0);
                    }
                } else if (Vampire_BatEntity.this.horizontalCollision || Vampire_BatEntity.this.hurtTime > 0) {
                    Vampire_BatEntity.this.attackPhase = Vampire_BatEntity.AttackPhase.CIRCLE;
                }

            }
        }
    }
}
