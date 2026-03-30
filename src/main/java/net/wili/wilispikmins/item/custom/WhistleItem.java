package net.wili.wilispikmins.item.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.wili.wilispikmins.entity.custom.PikminEntity;
import net.wili.wilispikmins.entity.custom.enums.PikminState;
import net.wili.wilispikmins.sound.ModSounds;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WhistleItem extends Item {
    public WhistleItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        double maxReach = 15.0D;
        double radius = 6.0D;

        if (!level.isClientSide()) {
            ServerLevel serverLevel = (ServerLevel) level;

            // obtener coordenadas que mira el jugador
            Vec3 eyePos = player.getEyePosition();
            Vec3 lookVector = player.getLookAngle();
            Vec3 traceEnd = eyePos.add(lookVector.x * maxReach, lookVector.y * maxReach, lookVector.z * maxReach);

            ClipContext context = new ClipContext(eyePos, traceEnd, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player);
            BlockHitResult hitResult = level.clip(context);

            Vec3 center;
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                center = hitResult.getLocation();
            } else {
                center = traceEnd;
            }

            AABB aabb = new AABB(center, center).inflate(radius);
            List<PikminEntity> pikmins = level.getEntitiesOfClass(PikminEntity.class, aabb);

            for (PikminEntity pikmin : pikmins) {
                if (pikmin.isOwnedBy(player)) {
                    // apagar fuego
                    if (pikmin.isOnFire()) {
                        pikmin.clearFire();
                    }

                    // dejar de pelear
                    pikmin.setTarget(null);

                    // ir con el jugador
                    pikmin.setPikminState(PikminState.FOLLOWING);
                    pikmin.getNavigation().moveTo(player, 1.2D);
                }
            }

            spawnWhistleParticles(serverLevel, center, radius);

            level.playSound(null, player.blockPosition(), ModSounds.WHISTLE.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

            player.getCooldowns().addCooldown(this, 20);
        }
        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }

    private void spawnWhistleParticles(ServerLevel level, Vec3 center, double radius) {
        int particleCount = 48;

        for (int i = 0; i < particleCount; i++) {
            double angle = 2 * Math.PI * i / particleCount;
            double x = center.x() + radius * Math.cos(angle);
            double z = center.z() + radius * Math.sin(angle);
            double y = center.y() + 0.1D;

            level.sendParticles(ParticleTypes.WAX_OFF, x, y, z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }
}
