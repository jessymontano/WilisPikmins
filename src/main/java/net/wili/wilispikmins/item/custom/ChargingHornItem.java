package net.wili.wilispikmins.item.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.wili.wilispikmins.entity.custom.PikminEntity;
import net.wili.wilispikmins.entity.custom.enums.PikminState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChargingHornItem extends Item {
    public ChargingHornItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);

        if (!level.isClientSide()) {
            double maxReach = 20.0D;
            Vec3 eyePos = player.getEyePosition();
            Vec3 viewVector = player.getViewVector(1.0F);
            Vec3 endPos = eyePos.add(viewVector.x * maxReach, viewVector.y * maxReach, viewVector.z * maxReach);

            AABB searchBox = player.getBoundingBox().expandTowards(viewVector.x * maxReach, viewVector.y * maxReach, viewVector.z * maxReach);

            EntityHitResult hitResult = ProjectileUtil.getEntityHitResult(level, player, eyePos, endPos, searchBox, entity -> entity instanceof LivingEntity && !entity.isSpectator() && entity.isPickable() && !(entity instanceof PikminEntity));

            if (hitResult != null && hitResult.getEntity() instanceof LivingEntity target) {
                double commandRadius = 10.0D;
                AABB aabb = player.getBoundingBox().inflate(commandRadius);
                List<PikminEntity> pikmins = level.getEntitiesOfClass(PikminEntity.class, aabb);

                boolean sentAny = false;

                for (PikminEntity pikmin : pikmins) {
                    if (pikmin.isOwnedBy(player) && pikmin.getPikminState() != PikminState.POPPING) {
                        pikmin.setTarget(target);
                        pikmin.setPikminState(PikminState.ATTACKING);
                        pikmin.getNavigation().moveTo(target, 1.4D);
                        sentAny = true;
                    }
                }

                if (sentAny) {
                    ServerLevel serverLevel = (ServerLevel) level;

                    serverLevel.sendParticles(ParticleTypes.ANGRY_VILLAGER,
                            target.getX(), target.getY() + target.getBbHeight() + 0.5, target.getZ(), 5, 0.3D, 0.2D, 0.3D, 0.0D);
                    level.playSound(null, player.blockPosition(), SoundEvents.GOAT_HORN_SOUND_VARIANTS.getFirst().value(), SoundSource.PLAYERS, 1.0F, 1.0F);
                    player.getCooldowns().addCooldown(this, 40);
                }
            }
        }
        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }
}
