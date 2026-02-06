package net.wili.wilispikmins.entity.custom.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.wili.wilispikmins.entity.custom.PikminEntity;
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

        if (this.ownerLastHurtBy != null &&
        this.ownerLastHurtBy != owner &&
        this.ownerLastHurtBy.isAlive() &&
        !this.pikmin.isAlliedTo(this.ownerLastHurtBy) &&
                !(this.ownerLastHurtBy instanceof PikminEntity pikminEntity && owner.getUUID().equals(pikminEntity.getOwnerUUID()))) {
            int lastHurtTimestamp = owner.getLastHurtByMobTimestamp();
            if (lastHurtTimestamp != this.timestamp) {
                return true;
            }
        }

        if (this.ownerLastHurt != null &&
        this.ownerLastHurt != owner &&
        this.ownerLastHurt.isAlive() &&
        !this.pikmin.isAlliedTo(this.ownerLastHurt) &&
                !(this.ownerLastHurt instanceof PikminEntity pikminEntity && owner.getUUID().equals(pikminEntity.getOwnerUUID()))) {
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

        if (target == null || !target.isAlive() || owner == null || !owner.isAlive() || this.pikmin.getPikminState() == PikminState.POPPING) {
            if (this.pikmin.getPikminState() == PikminState.ATTACKING) {
                this.pikmin.setPikminState(
                        this.pikmin.getOwner() != null ?
                                PikminState.FOLLOWING : PikminState.IDLE
                );
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
}
