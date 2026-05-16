package net.wili.wilispikmins.entity.custom.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.wili.wilispikmins.entity.custom.BaseBulborbEntity;
import net.wili.wilispikmins.entity.custom.PikminEntity;
import net.wili.wilispikmins.entity.custom.enums.BulborbState;
import net.wili.wilispikmins.entity.custom.enums.PikminState;

import java.util.EnumSet;

public class PikminDefendOwnerGoal extends TargetGoal {
    private final PikminEntity pikmin;
    private LivingEntity ownerLastHurtBy;
    private LivingEntity ownerLastHurt;
    private int timestamp;

    public PikminDefendOwnerGoal(PikminEntity pikmin) {
        super(pikmin, false);
        this.pikmin = pikmin;
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        LivingEntity owner = this.pikmin.getOwner();
        if (owner == null || !owner.isAlive() || this.pikmin.getPikminState() == PikminState.POPPING) {
            return false;
        }

        if (this.pikmin.getPikminState() == PikminState.ATTACKING && this.pikmin.getTarget() != null) {
            return false;
        }

        this.ownerLastHurtBy = owner.getLastHurtByMob();
        this.ownerLastHurt = owner.getLastHurtMob();

        if (isValidTarget(this.ownerLastHurtBy, owner)) {
            int lastHurtTimestamp = owner.getLastHurtByMobTimestamp();
            if (lastHurtTimestamp != this.timestamp) {
                return true;
            }
        }

        if (isValidTarget(this.ownerLastHurt, owner)) {
            return true;
        }

        return false;
    }

    @Override
    public void start() {
        LivingEntity target = null;
        if (this.ownerLastHurtBy != null) {
            target = this.ownerLastHurtBy;
            LivingEntity owner = this.pikmin.getOwner();
            if (owner != null) {
                this.timestamp = owner.getLastHurtByMobTimestamp();
            }
        } else if (this.ownerLastHurt != null) {
            target = this.ownerLastHurt;
        }

        if (target != null && this.pikmin.canAttack(target)) {
            this.pikmin.setTarget(target);
            this.pikmin.setPikminState(PikminState.ATTACKING);
        }

        super.start();
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = this.pikmin.getTarget();
        LivingEntity owner = this.pikmin.getOwner();

        boolean isTargetDeadBulborb = target instanceof BaseBulborbEntity bulborb && bulborb.getBulborbState() == BulborbState.DEAD;

        if (target == null || !target.isAlive() || owner == null || !owner.isAlive() || isTargetDeadBulborb || this.pikmin.getPikminState() == PikminState.POPPING) {
            if (this.pikmin.getPikminState() == PikminState.ATTACKING) {
                this.pikmin.setPikminState(
                        this.pikmin.getOwner() != null ?
                                PikminState.FOLLOWING : PikminState.IDLE
                );
                this.pikmin.setTarget(null);
            }
            return false;
        }
        double distance = this.pikmin.distanceToSqr(target);
        return distance <= 256.0;
    }

    @Override
    public void stop() {
        super.stop();

        if (this.pikmin.getPikminState() == PikminState.ATTACKING &&
        this.pikmin.getOwner() != null) {
            this.pikmin.setPikminState(PikminState.FOLLOWING);
        }
    }

    private boolean isValidTarget(LivingEntity target, LivingEntity owner) {
        if (target == null || target == owner || !target.isAlive()) return false;
        if (this.pikmin.isAlliedTo(target)) return false;
        if (target instanceof PikminEntity pikminEntity && owner.getUUID().equals(pikminEntity.getOwnerUUID())) return false;
        if (target instanceof BaseBulborbEntity bulborb && bulborb.getBulborbState() == BulborbState.DEAD) {
            return false;
        }

        return true;
    }
}
