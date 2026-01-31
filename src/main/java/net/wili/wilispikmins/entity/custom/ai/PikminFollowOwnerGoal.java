package net.wili.wilispikmins.entity.custom.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.wili.wilispikmins.entity.custom.PikminEntity;
import net.wili.wilispikmins.entity.custom.enums.PikminState;

import java.util.EnumSet;
import java.util.Objects;

public class PikminFollowOwnerGoal extends Goal {
    private final PikminEntity pikmin;
    private final double speedModifier;
    private final float startDistance;
    private final float stopDistance;
    private final float minDistance;
    private float oldWaterCost;

    public PikminFollowOwnerGoal(PikminEntity pikmin, double speedModifier, float startDistance, float stopDistance, float minDistance) {
        this.pikmin = pikmin;
        this. speedModifier = speedModifier;
        this.startDistance = startDistance;
        this.stopDistance = stopDistance;
        this.minDistance = minDistance;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (pikmin.getPikminState() == PikminState.POPPING) {
            return false;
        }
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
        if (pikmin.getPikminState() == PikminState.POPPING) {
            return false;
        }
        if (pikmin.getPikminState() != PikminState.FOLLOWING) {
            return false;
        }
        if (pikmin.getNavigation().isDone()) {
            return false;
        }
        return pikmin.distanceToSqr(Objects.requireNonNull(pikmin.getOwner())) > (double) (stopDistance * stopDistance);
    }

    @Override
    public void start() {
        this.oldWaterCost = pikmin.getPathfindingMalus(PathType.WATER);
        pikmin.setPathfindingMalus(PathType.WATER, 0.0F);
    }

    @Override public void stop() {
        pikmin.getNavigation().stop();
        pikmin.setPathfindingMalus(PathType.WATER, oldWaterCost);
    }

    @Override
    public void tick() {
        LivingEntity owner = pikmin.getOwner();
        assert owner != null;
        pikmin.getLookControl().setLookAt(owner, 10.0F, (float)pikmin.getMaxHeadXRot());
        if (pikmin.distanceToSqr(owner) >= 144.0D) {
            pikmin.teleportTo(owner.getX(), owner.getY(), owner.getZ());
        } else if (pikmin.distanceToSqr(owner) < (double) (minDistance * minDistance)) {
            Vec3 awayVector = pikmin.position()
                    .subtract(owner.position())
                    .normalize()
                    .scale(minDistance);
            BlockPos targetPos = BlockPos.containing(
                    owner.getX() + awayVector.x,
                    owner.getY(),
                    owner.getZ() + awayVector.z
            );

            pikmin.getNavigation().moveTo(targetPos.getX(), targetPos.getY(), targetPos.getZ(), speedModifier * 0.5);
        }
        else {
            pikmin.getNavigation().moveTo(owner, speedModifier);
        }
    }
}
