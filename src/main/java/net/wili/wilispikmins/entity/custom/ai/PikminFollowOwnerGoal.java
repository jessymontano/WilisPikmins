package net.wili.wilispikmins.entity.custom.ai;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.wili.wilispikmins.entity.custom.PikminEntity;
import net.wili.wilispikmins.entity.custom.enums.PikminState;

import java.util.EnumSet;

public class PikminFollowOwnerGoal extends Goal {
    private final PikminEntity pikmin;
    private final double speedModifier;
    private final float startDistance;
    private final float stopDistance;
    private float oldWaterCost;

    public PikminFollowOwnerGoal(PikminEntity pikmin, double speedModifier, float startDistance, float stopDistance) {
        this.pikmin = pikmin;
        this. speedModifier = speedModifier;
        this.startDistance = startDistance;
        this.stopDistance = stopDistance;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (pikmin.getPikminState() != PikminState.FOLLOWING) {
            return false;
        }
        if (pikmin.getOwner() == null) {
            return false;
        }
        if (pikmin.distanceToSqr(pikmin.getOwner()) < (double)(startDistance * startDistance)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (pikmin.getPikminState() != PikminState.FOLLOWING) {
            return false;
        }
        if (pikmin.getNavigation().isDone()) {
            return false;
        }
        return pikmin.distanceToSqr(pikmin.getOwner()) > (double) (stopDistance * stopDistance);
    }

    @Override
    public void start() {
        this.oldWaterCost = pikmin.getPathfindingMalus(BlockPathTypes.WATER);
        pikmin.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    @Override public void stop() {
        pikmin.getNavigation().stop();
        pikmin.setPathfindingMalus(BlockPathTypes.WATER, oldWaterCost);
    }

    @Override
    public void tick() {
        pikmin.getLookControl().setLookAt(pikmin.getOwner(), 10.0F, (float)pikmin.getMaxHeadXRot());
        if (pikmin.distanceToSqr(pikmin.getOwner()) >= 144.0D) {
            pikmin.teleportTo(pikmin.getOwner().getX(), pikmin.getOwner().getY(), pikmin.getOwner().getZ());
        } else {
            pikmin.getNavigation().moveTo(pikmin.getOwner(), speedModifier);
        }
    }
}
