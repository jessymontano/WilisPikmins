package net.wili.wilispikmins.entity.custom.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.wili.wilispikmins.entity.custom.PikminEntity;
import net.wili.wilispikmins.entity.custom.enums.PikminState;
import net.wili.wilispikmins.entity.custom.enums.PikminType;

public class WingedPikminFlightSystem {
    private final PikminEntity pikmin;
    private float hoverPhase = 0f;
    private float personalOffset;

    public WingedPikminFlightSystem(PikminEntity pikmin) {
        this.pikmin = pikmin;
        this.personalOffset = pikmin.getRandom().nextFloat() * 100f;
    }

    public void setPersonalOffset(float offset) {
        this.personalOffset = offset;
    }

    public float getPersonalOffset() {
        return personalOffset;
    }

    public void tick() {
        hoverPhase += 0.04f;
    }

    public boolean shouldFly() {
        if (pikmin.getPikminType() != PikminType.WINGED) return false;
        if (pikmin.tickCount < 20) return false;
        if (pikmin.isInWaterOrBubble() || pikmin.isInLava()) return  false;
        if (pikmin.getPikminState() == PikminState.POPPING) return false;
        return true;
    }

    public Vec3 calculateMovement(float deltaTime) {
        LivingEntity owner = pikmin.getOwner();
        PikminState state = pikmin.getPikminState();

        Vec3 horizontalMovement = calculateHorizontalMovement(owner, state);
        double verticalMovement = calculateVerticalMovement(owner, state, deltaTime);

        return new Vec3(
                horizontalMovement.x,
                verticalMovement,
                horizontalMovement.z
        );
    }

    private Vec3 calculateHorizontalMovement(LivingEntity owner, PikminState state) {
        if (state == PikminState.FOLLOWING && owner != null) {
            return calculateFollowingMovement(owner);
        } else if (state == PikminState.IDLE) {
            return calculateIdleHoverMovement();
        } else if (state == PikminState.ATTACKING && pikmin.getTarget() != null) {
            return calculateAttackingMovement(pikmin.getTarget());
        }
        return Vec3.ZERO;
    }

    private Vec3 calculateAttackingMovement(LivingEntity target) {
        Vec3 toTarget = new Vec3(
                target.getX() - pikmin.getX(),
                0,
                target.getZ() - pikmin.getZ()
        );

        double distance = toTarget.horizontalDistance();

        if (distance < 1.0) {
            return Vec3.ZERO;
        }

        Vec3 direction = toTarget.normalize();
        double speed = 0.18;

        return direction.scale(speed);
    }

    private Vec3 calculateFollowingMovement(LivingEntity owner) {
        Vec3 toOwner = new Vec3(
                owner.getX() - pikmin.getX(),
                0,
                owner.getZ() - pikmin.getZ()
        );

        double distance = toOwner.horizontalDistance();

        if (distance < 3.0) {
            return calculateHoverMovement();
        }

        Vec3 direction = toOwner.normalize();

        float time = (pikmin.tickCount + personalOffset) * 0.04f;
        float zigzagX = Mth.sin(time) * 0.1f;
        float zigzagZ = Mth.cos(time * 0.7f) * 0.1f;

        float lateralOffset = calculateLateralOffset();
        Vec3 lateral = new Vec3(-direction.z, 0, direction.x).scale(lateralOffset * 0.3f);

        double speed = 0.12 + (Mth.clamp(distance / 25.0, 0, 0.08));
        speed = Math.min(speed, 0.16);

        Vec3 baseMovement = direction.scale(speed);

        return baseMovement
                .add(lateral)
                .add(new Vec3(zigzagX, 0, zigzagZ));
    }

    private float calculateLateralOffset() {
        if (pikmin.getOwnerUUID() == null) return 0f;

        int hash = pikmin.getOwnerUUID().hashCode() + pikmin.getId();
        return Mth.sin(hash * 0.1f + pikmin.tickCount * 0.02f);
    }

    private Vec3 calculateIdleHoverMovement() {
        float time = (pikmin.tickCount + personalOffset) * 0.08f;
        float radius = 0.1f;

        return new Vec3(
                Mth.cos(time) * radius * 0.03f,
                0,
                Mth.sin(time) * radius * 0.03f
        );
    }

    private Vec3 calculateHoverMovement() {
        float time = pikmin.tickCount * 0.1f;

        float radius = 0.3f;
        float angle = time * 0.5f;

        return new Vec3(
                Mth.cos(angle) * radius * 0.05f,
                0,
                Mth.sin(angle) * radius * 0.05f
        );
    }

    private double calculateVerticalMovement(LivingEntity owner, PikminState state, float deltaTime) {
        double targetHeight = calculateTargetFlyHeight(owner, state);
        double currentY = pikmin.getY();
        double yDifference = targetHeight - currentY;

        double verticalSpeed = Mth.clamp(yDifference * 0.08, -0.08, 0.08);

        hoverPhase += 0.06f * deltaTime;
        double bob = Mth.sin(hoverPhase) * 0.02;

        return verticalSpeed + bob;
    }

    private double calculateTargetFlyHeight(LivingEntity owner, PikminState state) {
        BlockPos groundPos = findGroundPosition();
        double groundY = groundPos.getY() + 1.0;

        if (state == PikminState.FOLLOWING && owner != null) {
            return owner.getY() + 1.5;
        } else if (state == PikminState.IDLE) {
            return groundY + 1.2 + Mth.sin(pikmin.tickCount * 0.05f) * 0.3;
        } else if (state == PikminState.ATTACKING && pikmin.getTarget() != null) {
            return pikmin.getTarget().getY() + (pikmin.getTarget().getBbHeight() / 2.0);
        }
        return groundY + 1.0;
    }

    public float calculateTargetRotation(Vec3 movement) {
        if (movement.horizontalDistanceSqr() > 0.001) {
            return (float)(Mth.atan2(movement.z, movement.x) * (180 /Math.PI)) - 90;
        }
        return pikmin.getYRot();
    }

    private BlockPos findGroundPosition() {
        Level level = pikmin.level();
        BlockPos currentPos = pikmin.blockPosition();

        for (int y = currentPos.getY(); y > level.getMinBuildHeight(); y--) {
            BlockPos checkPos = new BlockPos(currentPos.getX(), y, currentPos.getZ());
            if (!level.isEmptyBlock(checkPos)) {
                return checkPos;
            }
        }
        return currentPos;
    }
}
