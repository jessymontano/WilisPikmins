package net.wili.wilispikmins.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.wili.wilispikmins.entity.ModEntities;
import net.wili.wilispikmins.entity.custom.enums.GrowthStage;
import net.wili.wilispikmins.entity.custom.enums.PikminState;
import net.wili.wilispikmins.entity.custom.enums.PikminType;
import net.wili.wilispikmins.entity.custom.ai.PikminFollowOwnerGoal;
import net.wili.wilispikmins.sound.ModSounds;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class PikminEntity extends TamableAnimal implements GeoEntity {
    // datos cliente-servidor
    private static final EntityDataAccessor<Integer> DATA_TYPE =
            SynchedEntityData.defineId(PikminEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_STAGE =
            SynchedEntityData.defineId(PikminEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_STATE =
            SynchedEntityData.defineId(PikminEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Optional<UUID>> DATA_OWNER_UUID =
            SynchedEntityData.defineId(PikminEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    // animaciones
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private boolean shouldPlayPop = false;
    private int popAnimationTicks = 0;
    private static final int POP_ANIMATION_DURATION = 15;

    // vuelo de pikmin rosa
    private boolean isFlying = false;
    private int flyCooldown = 0;
    private int spawnTicks = 0;
    private float zigzagPhase = 0f;

    // estados temporales
    private int drowningTicks = 0;
    private int burningTicks = 0;
    private int growthTicks = 0;

    // constructorsitos
    public PikminEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

        // rotacion aleatoria al spawnear
        this.setYRot(this.level().random.nextFloat() * 360.0F);
        this.yHeadRot = this.getYRot();
        this.yBodyRot = this.getYRot();
    }

    public PikminEntity(Level level, PikminType type) {
        this(ModEntities.PIKMIN.get(), level);
        setPikminType(type);

        // rotacion aleatoria al spawnear
        this.setYRot(this.level().random.nextFloat() * 360.0F);
        this.yHeadRot = this.getYRot();
        this.yBodyRot = this.getYRot();
    }


    protected PlayState predicate(AnimationState<PikminEntity> event) {
        if (this.getPikminState() == PikminState.POPPING) {
            return PlayState.STOP;
        }
        if (this.getPikminType() == PikminType.WINGED && this.isFlying) {
            return PlayState.STOP;
        }
        if (event.isMoving()) {
            return event.setAndContinue(RawAnimation.begin()
                    .thenLoop("animation.pikmin.walk"));
        } else {
            return event.setAndContinue(RawAnimation.begin()
                    .thenLoop("animation.pikmin.idle"));
        }
    }

    protected PlayState attackPredicate(AnimationState<PikminEntity> event) {
        if (this.swinging && event.getController().getAnimationState() != AnimationController.State.TRANSITIONING) {
            return event.setAndContinue(RawAnimation.begin()
                    .thenPlay("animation.pikmin.attack")
                    .thenLoop("animation.pikmin.idle"));
        }
        return PlayState.STOP;
    }

    protected PlayState popPredicate(AnimationState<PikminEntity> event) {
        if (this.shouldPlayPop) {
            this.shouldPlayPop = false;
            return event.setAndContinue(RawAnimation.begin()
                    .thenPlay("animation.pikmin.pop"));
        }
        if (this.tickCount < 20 && this.getPikminState() == PikminState.POPPING) {
            return event.setAndContinue(RawAnimation.begin()
                    .thenPlay("animation.pikmin.pop"));
        }
        return PlayState.STOP;
    }

    protected PlayState flyPredicate(AnimationState<PikminEntity> event) {
        if (this.getPikminType() == PikminType.WINGED && this.isFlying) {
            return event.setAndContinue(RawAnimation.begin()
                    .thenLoop("animation.pikmin.fly"));
        }
        return PlayState.STOP;
    }

    public void triggerPopAnimation() {
        this.shouldPlayPop = true;
        this.setPikminState(PikminState.POPPING);
        this.popAnimationTicks = 0;

        this.getNavigation().stop();
        this.setDeltaMovement(Vec3.ZERO);

        this.triggerAnim("pop", "pop");
    }

    @Override
    public void travel(@NotNull Vec3 pTravelVector) {
        if (this.getPikminType() == PikminType.WINGED && shouldFly()) {
            handleWingedMovement(pTravelVector);
            return;
        }

        this.isFlying = false;
        this.setNoGravity(false);
        super.travel(pTravelVector);
    }

    private void handleWingedMovement(Vec3 travelVector) {
        this.isFlying = true;
        this.setNoGravity(true);

        float targetHeight = calculateFlyHeight();

        double currentY = this.getY();
        double yDifference = targetHeight - currentY;
        double verticalMovement = Mth.clamp(yDifference * 0.05, -0.05, 0.05);

        Vec3 horizontalMovement = calculateWingedHorizontalMovement(travelVector);
        double dampenedY = Mth.lerp(0.2, this.getDeltaMovement().y, verticalMovement);

        this.setDeltaMovement(
                horizontalMovement.x,
                dampenedY,
                horizontalMovement.z
        );

        Vec3 currentMotion = this.getDeltaMovement();
        double speed = currentMotion.horizontalDistance();
        if (speed > 0.5) {
            Vec3 limitedMotion = currentMotion.normalize().scale(0.5);
            this.setDeltaMovement(limitedMotion.x, currentMotion.y, limitedMotion.z);
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
    }

    private float calculateFlyHeight() {
        BlockPos groundPos = findGroundPosition();
        float groundY = groundPos.getY() + 1.0f;

        float baseHeight = switch (getPikminState()) {
            case IDLE -> groundY + 0.2f;
            case FOLLOWING -> groundY + 1.2f;
            default -> groundY + 0.8f;
        };
        float bob = Mth.sin(this.tickCount * 0.08f) * 0.06f;

        return baseHeight + bob;
    }

    private BlockPos findGroundPosition() {
        BlockPos currentPos = this.blockPosition();

        for (int y = currentPos.getY(); y > this.level().getMinBuildHeight(); y--) {
            BlockPos checkPos = new BlockPos(currentPos.getX(), y, currentPos.getZ());
            if (!this.level().isEmptyBlock(checkPos)) {
                return checkPos;
            }
        }
        return currentPos;
    }

    private Vec3 calculateWingedHorizontalMovement(Vec3 travelVector) {
        Vec3 movement;

        if (getPikminState() == PikminState.FOLLOWING && getOwner() != null) {
            LivingEntity owner = getOwner();
            Vec3 toOwner = new Vec3(
                    owner.getX() - this.getX(),
                    0,
                    owner.getZ() - this.getZ()
            );
            double distance = toOwner.horizontalDistance();

            if (distance < 1.5) {
                return Vec3.ZERO;
            }

           Vec3 forward = toOwner.normalize();
            Vec3 side = new Vec3(-forward.z, 0, forward.x);
            float zigzag = Mth.sin(zigzagPhase) * 0.15f;

            return forward.scale(0.22).add(side.scale(zigzag));
        } else if (getPikminState() == PikminState.IDLE) {
            movement = getRandomWanderingMovement().scale(0.3);
        } else {
            movement = travelVector.scale(this.getSpeed());

        }

        return movement;
    }

    private Vec3 getRandomWanderingMovement() {
        float angle = (this.tickCount * 0.1f) % (2 * Mth.PI);
        float radius = 0.2f + Mth.sin(this.tickCount * 0.05f) * 0.1f;

        return new Vec3(
                Mth.cos(angle) * radius,
                0,
                Mth.sin(angle) * radius
        );
    }

    private boolean shouldFly() {
        if (this.getPikminType() != PikminType.WINGED) return false;
        if (spawnTicks < 20) return false;
        if (this.isInWaterOrBubble() || this.isInLava()) return false;
        if (this.getPikminState() == PikminState.POPPING) return false;
        if (this.flyCooldown > 0) return false;
        return true;
    }

    public boolean isFlying() {
        return this.isFlying;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_TYPE, 0);
        builder.define(DATA_STAGE, 0);
        builder.define(DATA_STATE, 0);
        builder.define(DATA_OWNER_UUID, Optional.empty());
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 2.0));
        this.goalSelector.addGoal(2, new PikminFollowOwnerGoal(this, 1.2, 10.0f, 2.0f, 3.0f));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F) {
            @Override
            public boolean canUse() {
                return getPikminState() != PikminState.POPPING && super.canUse();
            }
        });
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this) {
            @Override
            public boolean canUse() {
                return getPikminState() != PikminState.POPPING && super.canUse();
            }
        });
    }

    public static AttributeSupplier.Builder createAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 16.0) // 8 corazones
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 1.5)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.ATTACK_KNOCKBACK, 0.1);
    }

    public PikminType getPikminType() {
        return PikminType.values()[this.entityData.get(DATA_TYPE)];
    }

    public void setPikminType(PikminType type) {
        this.entityData.set(DATA_TYPE, type.ordinal());
        updateAttributes();
    }

    public GrowthStage getGrowthStage() {
        return GrowthStage.values()[this.entityData.get(DATA_STAGE)];
    }

    public void setGrowthStage(GrowthStage stage) {
        this.entityData.set(DATA_STAGE, stage.ordinal());
        updateAttributes();
    }

    public PikminState getPikminState() {
        return PikminState.values()[this.entityData.get(DATA_STATE)];
    }

    public void setPikminState(PikminState state) {
        this.entityData.set(DATA_STATE, state.ordinal());
    }

    @Override
    public UUID getOwnerUUID() {
        return this.entityData.get(DATA_OWNER_UUID).orElse(null);
    }

    @Override
    public void setOwnerUUID(@Nullable UUID uuid) {
        this.entityData.set(DATA_OWNER_UUID, Optional.ofNullable(uuid));
    }

    // modificar velocidad dependiendo de la etapa de crecimiento y tipo
    private void updateAttributes() {
        float speedMultiplier = switch(getGrowthStage()) {
            case LEAF -> 1.0f;
            case BUD -> 1.2f;
            case FLOWER -> 1.4f;
        };

        float baseSpeed = 0.25f;
        if (getPikminType() == PikminType.WHITE) {
            baseSpeed = 0.3f;
        } else if (getPikminType() == PikminType.PURPLE) {
            baseSpeed = 0.2f;
        }

        Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(baseSpeed * speedMultiplier);
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if (this.level().isClientSide) {
            return InteractionResult.CONSUME;
        }

        if (getPikminState() == PikminState.POPPING) {
            return InteractionResult.PASS;
        }

        // si no tiene dueño, asignar dueño
        if (getOwnerUUID() == null) {
            setOwnerUUID(player.getUUID());
            setPersistenceRequired();
            player.swing(hand);
            return InteractionResult.SUCCESS;
        }

        // si tiene dueño, cambiar estado de idle a following y viceversa
        if (isOwnedBy(player)) {
            if (getPikminState() == PikminState.FOLLOWING) {
                setPikminState(PikminState.IDLE);
                this.level().playSound(this, this.getOnPos(), ModSounds.PIKMIN_ENTER_IDLE.get(), SoundSource.NEUTRAL, 0.5f, 1.0f);
            } else {
                this.level().playSound(this, this.getOnPos(), ModSounds.PIKMIN_JOIN_SQUAD.get(), SoundSource.NEUTRAL, 0.5f, 1.0f);
                setPikminState(PikminState.FOLLOWING);
            }
            player.swing(hand);
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public void tick() {
        super.tick();

        zigzagPhase += 0.08f;

        if (spawnTicks < 20) {
            spawnTicks++;
        }

        if (this.tickCount % 20 == 0 && this.shouldPlayPop) {
            this.shouldPlayPop = false;
        }

        if (getPikminState() == PikminState.POPPING) {
            if (this.level().isClientSide && this.tickCount %2 == 0) {
                spawnPopParticles();
            }
            this.popAnimationTicks++;

            this.getNavigation().stop();
            this.setDeltaMovement(Vec3.ZERO);

            if (this.popAnimationTicks >= POP_ANIMATION_DURATION) {
                if (getOwner() != null) {
                    setPikminState(PikminState.FOLLOWING);
                } else {
                    setPikminState(PikminState.IDLE);
                }
            }
            return;
        }

        if (this.flyCooldown > 0) {
            this.flyCooldown--;
        }

        if (this.getPikminType() == PikminType.WINGED &&
        this.isFlying &&
        this.level().isClientSide &&
        this.tickCount % 3 == 0) {
            spawnWingParticles();
        }

        if (!this.level().isClientSide()) {
            updateGrowth();
            updateDangerStates();
            updateAIState();
        }
    }

    private void spawnWingParticles() {
        for (int i = 0; i < 2; i++) {
            double offsetX = (this.random.nextDouble() - 0.5) * 0.3;
            double offsetY = this.random.nextDouble() * 0.2;
            double offsetZ = (this.random.nextDouble() - 0.5) * 0.3;

            this.level().addParticle(ParticleTypes.GLOW,
                    this.getX() + offsetX,
                    this.getY() + offsetY,
                    this.getZ() + offsetZ,
                    0, 0.01, 0);
        }
    }

    private void spawnPopParticles() {
        Level level = this.level();
        PikminType type = getPikminType();
        int color = type.getColor();

        for (int i = 0; i < 3; i++) {
            double x = this.getX() + (level.random.nextDouble() - 0.5) * 0.3;
            double y = this.getY() + 0.2;
            double z = this.getZ() + (level.random.nextDouble() - 0.5) * 0.3;
            if (level.isClientSide) {
                level.addParticle(ParticleTypes.NOTE, x, y, z,
                        ((color >> 16) & 0xFF) / 255.0,
                        ((color >> 8) & 0xFF) / 255.0,
                        (color & 0xFF) / 255.0);
            }
        }
    }

    private void updateGrowth() {
        // crecer cada 10 minutos
        if (++growthTicks >= 12000) {
            growthTicks = 0;
            advanceGrowthStage();
        }
    }

    private void advanceGrowthStage() {
        GrowthStage current = getGrowthStage();
        if (current.ordinal() < GrowthStage.values().length - 1) {
            setGrowthStage(GrowthStage.values()[current.ordinal() + 1]);
        }
    }

    private void updateDangerStates() {
        // pikmin se ahoga excepto el azul
        if (isInWater() && !isWaterResistant()) {
            drowningTicks++;
            if (drowningTicks > 60) {
                hurt(this.damageSources().drown(), 2.0f);
                drowningTicks = 0;
            }
            if (drowningTicks > 0) {
                setPikminState(PikminState.DROWNING);
            }
        } else {
            drowningTicks = 0;
            if (getPikminState() == PikminState.DROWNING) {
                setPikminState(PikminState.IDLE);
            }
        }

        // pikmin se quema excepto el rojo
        if (isOnFire() && !isFireResistant()) {
            burningTicks++;
            if(burningTicks > 80) {
                hurt(this.damageSources().onFire(), 1.0f);
                burningTicks = 0;
            }
            setPikminState(PikminState.BURNING);
        } else {
            burningTicks = 0;
            if (getPikminState() == PikminState.BURNING) {
                setPikminState(PikminState.IDLE);
            }
        }
    }

    private void updateAIState() {
        LivingEntity owner = getOwner();
        // seguir al dueño o permanecer en su lugar
        if (getPikminState() == PikminState.FOLLOWING && owner != null) {
            this.getNavigation().moveTo(owner, 1.2f);
            this.lookAt(owner, 30.0f, 30.0f);
        } else if (getPikminState() == PikminState.IDLE) {
            this.getNavigation().stop();
        }
    }

    public boolean isFireResistant() {
        return getPikminType() == PikminType.RED;
    }

    public boolean isElectricResistant() {
        return getPikminType() == PikminType.YELLOW;
    }

    public boolean isWaterResistant() {
        return getPikminType() == PikminType.BLUE;
    }

    // guardar y cargar datos
    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("PikminType", getPikminType().ordinal());
        tag.putInt("GrowthStage", getGrowthStage().ordinal());
        tag.putInt("GrowthTicks", growthTicks);
        tag.putInt("PikminState", getPikminState().ordinal());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("PikminType")) {
            setPikminType(PikminType.values()[tag.getInt("PikminType")]);
        }
        if (tag.contains("GrowthStage")) {
            setGrowthStage(GrowthStage.values()[tag.getInt("GrowthStage")]);
        }
        if (tag.contains("GrowthTicks")) {
            growthTicks = tag.getInt("GrowthTicks");
        }
        if (tag.contains("PikminState")) {
            setPikminState(PikminState.values()[tag.getInt("PikminState")]);
        }
    }

    public static boolean checkPikminSpawnRules(EntityType<PikminEntity> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, net.minecraft.util.RandomSource random) {
        return Animal.checkAnimalSpawnRules(type, level, spawnType, pos, random) && pos.getY() > 60;
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return ModSounds.PIKMIN_DEATH.get();
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(@NotNull DamageSource pDamageSource) {
        return ModSounds.PIKMIN_SCREAM.get();
    }

    @Override
    public boolean isFood(@NotNull ItemStack pStack) {
        return false;
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        PikminEntity parent = (PikminEntity) ageableMob;
        PikminEntity offspring = new PikminEntity(serverLevel, parent.getPikminType());
        offspring.setOwnerUUID(parent.getOwnerUUID());
        return offspring;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "main", 0, this::predicate));
        controllerRegistrar.add(new AnimationController<>(this, "attack", 0, this::attackPredicate));
        controllerRegistrar.add(new AnimationController<>(this, "pop", 0, this::popPredicate));
        controllerRegistrar.add(new AnimationController<>(this, "fly", 0, this::flyPredicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void swing(@NotNull InteractionHand hand) {
        super.swing(hand);

        this.triggerAnim("attack", "attack");
    }
}
