package net.wili.wilispikmins.item.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.wili.wilispikmins.advancement.ModTriggers;
import net.wili.wilispikmins.entity.custom.PikminEntity;
import net.wili.wilispikmins.entity.custom.enums.GrowthStage;
import net.wili.wilispikmins.sound.ModSounds;
import org.jetbrains.annotations.NotNull;

public class NectarItem extends Item {
    private static final int NUTRITION = 4;
    private static final float SATURATION = 0.6f;

    public NectarItem(Properties properties) {
        super(properties.food(new FoodProperties.Builder()
                .nutrition(NUTRITION)
                .saturationModifier(SATURATION)
                .alwaysEdible()
                .build()));
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, @NotNull Player player, @NotNull LivingEntity interactionTarget, @NotNull InteractionHand usedHand) {
        if (interactionTarget == player) {
            return InteractionResult.PASS;
        }
        if (interactionTarget instanceof PikminEntity pikmin) {
            return feedNectarToPikmin(stack, player, pikmin, usedHand);
        }
        return super.interactLivingEntity(stack, player, interactionTarget, usedHand);
    }

    private InteractionResult feedNectarToPikmin(ItemStack stack, Player player, PikminEntity pikmin, InteractionHand hand) {
        Level level = player.level();
        if (!pikmin.isOwnedBy(player)) {
            return InteractionResult.PASS;
        }
        GrowthStage currentStage = pikmin.getGrowthStage();

        if (currentStage == GrowthStage.FLOWER) {
            return InteractionResult.FAIL;
        }

        if (!level.isClientSide()) {
            level.playSound(null, pikmin.getX(), pikmin.getY(), pikmin.getZ(),
                    ModSounds.PIKMIN_DRINK_NECTAR.get(), SoundSource.NEUTRAL,
                    0.7f, 0.9f + level.random.nextFloat() * 0.2f);
        }

        GrowthStage nextStage = GrowthStage.values()[currentStage.ordinal() + 1];
        pikmin.setGrowthStage(nextStage);

        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            ModTriggers.PIKMIN_GROW.get().trigger(serverPlayer, nextStage);
        }

        if (level.isClientSide()) {
            spawnNectarParticles(level, pikmin);
        }

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    private void spawnNectarParticles(Level level, PikminEntity pikmin) {
        if (level.isClientSide) {
            for (int i = 0; i < 10; i++) {
                double x = pikmin.getX() + (level.random.nextDouble() - 0.5) * 0.5;
                double y = pikmin.getY() + 0.5;
                double z = pikmin.getZ() + (level.random.nextDouble() - 0.5) * 0.5;

                level.addParticle(ParticleTypes.HEART,
                        x, y, z,
                        0, 0.1, 0);
            }
        }
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return 16;
    }

    @Override
    public @NotNull SoundEvent getEatingSound() {
        return ModSounds.PIKMIN_DRINK_NECTAR.get();
    }
}
