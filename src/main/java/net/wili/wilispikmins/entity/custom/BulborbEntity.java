package net.wili.wilispikmins.entity.custom;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.wili.wilispikmins.entity.custom.enums.BulborbState;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class BulborbEntity extends Monster implements GeoEntity {
    // datos cliente-servidor
    private static final EntityDataAccessor<Integer> DATA_STATE =
            SynchedEntityData.defineId(BulborbEntity.class, EntityDataSerializers.INT);

    // transporte
    public int attachedPikmins = 0;
    public final int MIN_CARRY_WEIGHT = 5;
    public final int MAX_CARRY_WEIGHT = 10;

    // animaciones
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public BulborbEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_STATE, BulborbState.AWAKE.ordinal());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 50.0)
                .add(Attributes.MOVEMENT_SPEED, 0.20)
                .add(Attributes.ATTACK_DAMAGE, 4.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this) {
            @Override
            public boolean canUse() {
                return getBulborbState() != BulborbState.DEAD && super.canUse();
            }
        });
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2, false) {
            @Override
            public boolean canUse() {
                return getBulborbState() == BulborbState.AWAKE && super.canUse();
            }
        });

        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0) {
            @Override
            public boolean canUse() {
                return getBulborbState() == BulborbState.AWAKE && super.canUse();
            }
        });

        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F) {
            @Override
            public boolean canUse() {
                return getBulborbState() != BulborbState.DEAD && super.canUse();
            }
        });

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PikminEntity.class, true) {
            @Override
            public boolean canUse() {
                return getBulborbState() == BulborbState.AWAKE && super.canUse();
            }
        });
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true) {
            @Override
            public boolean canUse() {
                return getBulborbState() == BulborbState.AWAKE && super.canUse();
            }
        });
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (this.level().isClientSide) return super.hurt(source, amount);

        if (this.getBulborbState() == BulborbState.DEAD) {
            return false;
        }

        boolean isLethal = (this.getHealth() - amount) <= 0;

        if (isLethal) {
            this.setHealth(1.0F);
            this.setBulborbState(BulborbState.DEAD);
            this.setTarget(null);
            this.getNavigation().stop();
            this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
            return false;
        }

        if (this.getBulborbState() == BulborbState.SLEEPING) {
            this.setBulborbState(BulborbState.AWAKE);
        }

        return super.hurt(source, amount);
    }

    public BulborbState getBulborbState() {
        return BulborbState.values()[this.entityData.get(DATA_STATE)];
    }

    public void setBulborbState(BulborbState state) {
        this.entityData.set(DATA_STATE, state.ordinal());
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "main", this::predicate));
        controllers.add(new AnimationController<>(this, "attack", 0, state -> PlayState.STOP)
                .triggerableAnim("attackTrigger", RawAnimation.begin().then("attack", Animation.LoopType.PLAY_ONCE)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    protected PlayState predicate(AnimationState<BulborbEntity> event) {
        if (this.getBulborbState() == BulborbState.SLEEPING) {
            return event.setAndContinue(RawAnimation.begin()
                    .thenLoop("sleep"));
        }
        if (event.isMoving()) {
            return event.setAndContinue(RawAnimation.begin()
                    .thenLoop("walk"));
        } else {
            return event.setAndContinue(RawAnimation.begin()
                    .thenLoop("idle"));
        }
    }

    @Override
    public void swing(@NotNull InteractionHand hand, boolean updateSelf) {
        super.swing(hand, updateSelf);

        if (!this.level().isClientSide()) {
            this.triggerAnim("attack", "attackTrigger");
        }
    }
}
