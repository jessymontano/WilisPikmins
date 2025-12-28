package net.wili.wilispikmins.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
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
import net.wili.wilispikmins.entity.ModEntities;
import net.wili.wilispikmins.entity.custom.enums.GrowthStage;
import net.wili.wilispikmins.entity.custom.enums.PikminState;
import net.wili.wilispikmins.entity.custom.enums.PikminType;
import net.wili.wilispikmins.entity.custom.ai.PikminFollowOwnerGoal;
import net.wili.wilispikmins.sound.ModSounds;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class PikminEntity extends TamableAnimal {
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
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState popAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;
    private int popAnimationTimeout = 0;
    private int attackAnimationTimeout = 0;

    // estados temporales
    private int drowningTicks = 0;
    private int burningTicks = 0;
    private int growthTicks = 0;
    @Nullable private LivingEntity target;
    @Nullable private BlockPos onionPos;
    private int followRange = 10;

    // constructorsitos
    public PikminEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

        // crear pikmin rojo
        setPikminType(PikminType.RED);
        setGrowthStage(GrowthStage.LEAF);
        setPikminState(PikminState.IDLE);
    }

    public PikminEntity(Level level, PikminType type) {
        this(ModEntities.PIKMIN.get(), level);
        setPikminType(type);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_TYPE, 0);
        this.entityData.define(DATA_STAGE, 0);
        this.entityData.define(DATA_STATE, 0);
        this.entityData.define(DATA_OWNER_UUID, Optional.empty());
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 2.0));
        this.goalSelector.addGoal(2, new PikminFollowOwnerGoal(this, 1.2, 10.0f, 2.0f));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttribute() {
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

        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(baseSpeed * speedMultiplier);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (this.level().isClientSide) {
            return InteractionResult.CONSUME;
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
            } else {
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

        if (this.level().isClientSide()) {
            this.setupAnimationStates();
        } else {
            updateGrowth();
            updateDangerStates();
            updateAIState();
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
            if (drowningTicks > 100) {
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

    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 40;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }

        if (this.popAnimationTimeout > 0) {
            this.popAnimationTimeout--;
        }

        if (this.attackAnimationTimeout > 0) {
            this.attackAnimationTimeout--;
        } else {
            this.attackAnimationState.stop();
        }

        if (this.tickCount == 1) {
            triggerPopAnimation();
        }
    }

    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        float f;
        if (this.getPose() == Pose.STANDING) {
            f = Math.min(pPartialTick * 6f, 1f);
        } else {
            f = 0f;
        }

        this.walkAnimation.update(f, 0.2f);
    }

    public void triggerPopAnimation() {
        this.popAnimationTimeout = 20;
        this.popAnimationState.start(this.tickCount);
    }

    public void triggerAttackAnimation() {
        this.attackAnimationTimeout = 10;
        this.attackAnimationState.start(this.tickCount);
    }

    // atacar
    @Override
    public boolean doHurtTarget(Entity target) {
        triggerAttackAnimation();
        return super.doHurtTarget(target);
    }

    // guardar y cargar datos
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("PikminType", getPikminType().ordinal());
        tag.putInt("GrowthStage", getGrowthStage().ordinal());
        tag.putInt("GrowthTicks", growthTicks);
        tag.putInt("PikminState", getPikminState().ordinal());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
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
    protected @Nullable SoundEvent getHurtSound(DamageSource pDamageSource) {
        return ModSounds.PIKMIN_SCREAM.get();
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return false;
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        PikminEntity parent = (PikminEntity) ageableMob;
        PikminEntity offspring = new PikminEntity(serverLevel, parent.getPikminType());
        offspring.setOwnerUUID(parent.getOwnerUUID());
        return offspring;
    }
}
