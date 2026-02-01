package net.wili.wilispikmins.entity.custom.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.wili.wilispikmins.entity.custom.PikminEntity;
import net.wili.wilispikmins.entity.custom.enums.PikminState;

import java.util.List;

public class PikminSquadBehaviorGoal extends Goal {
    private final PikminEntity pikmin;
    private int cooldown;

    public PikminSquadBehaviorGoal(PikminEntity pikmin) {
        this.pikmin = pikmin;
    }

    @Override
    public boolean canUse() {
        return this.pikmin.getOwner() != null &&
                this.pikmin.getPikminState() != PikminState.POPPING;
    }

    @Override
    public void tick() {
        if (--this.cooldown > 0) return;
        this.cooldown = 20;
        LivingEntity owner = this.pikmin.getOwner();
        if (owner == null) return;

        List<PikminEntity> nearbyPikmins = this.pikmin.level().getEntitiesOfClass(
                PikminEntity.class,
                this.pikmin.getBoundingBox().inflate(8.0),
                other -> other.getOwner() == owner &&
                        other != this.pikmin &&
                        other.getPikminState() != PikminState.POPPING
        );

        if (this.pikmin.getPikminState() == PikminState.ATTACKING) {
            LivingEntity threat = this.pikmin.getTarget();
            if (threat != null && threat.isAlive() && !nearbyPikmins.isEmpty()) {
                for (PikminEntity other : nearbyPikmins) {
                    if (other.getTarget() == null &&
                    other.getPikminState() == PikminState.FOLLOWING ||
                    other.getPikminState() == PikminState.IDLE &&
                    other.canAttack(threat)) {
                        other.setTarget(threat);
                        other.setPikminState(PikminState.ATTACKING);
                    }
                }
            }
        }
    }
}
