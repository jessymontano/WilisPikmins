package net.wili.wilispikmins.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.wili.wilispikmins.entity.ModEntities;
import net.wili.wilispikmins.entity.custom.PikminEntity;
import net.wili.wilispikmins.entity.custom.enums.PikminState;
import net.wili.wilispikmins.entity.custom.enums.PikminType;
import net.wili.wilispikmins.sound.ModSounds;


public class PikminSpawnEggItem extends ForgeSpawnEggItem {
    private final PikminType pikminType;

    public PikminSpawnEggItem(PikminType type) {
        super(ModEntities.PIKMIN, getPrimaryColor(type), getSecondaryColor(type),
                new Item.Properties());
        this.pikminType = type;
    }

    private static int getPrimaryColor(PikminType type) {
        return switch (type) {
            case RED -> 0xE32400;
            case YELLOW -> 0xF6D337;
            case BLUE -> 0x0044ff;
           /* case PURPLE -> 0x800080;
            case WHITE -> 0xFFFFFF;
            case WINGED -> 0xFF69B4;
            case ROCK -> 0x808080;*/
        };
    }

    private static int getSecondaryColor(PikminType type) {
        return switch (type) {
            case RED -> 0xFF3B1A;
            case YELLOW -> 0xFFE873;
            case BLUE -> 0x0066FF;
         /*   case PURPLE -> 0xA020F0;
            case WHITE -> 0xF0F0F0;
            case WINGED -> 0xFFB6C1;
            case ROCK -> 0xA0A0A0;*/
        };
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
        if (entity instanceof PikminEntity pikmin && !player.level().isClientSide) {
            pikmin.setPikminType(this.pikminType);
            stack.shrink(1);
            return InteractionResult.SUCCESS;
        }
        return super.interactLivingEntity(stack, player, entity, hand);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
       Level level = pContext.getLevel();

       if (level.isClientSide) {
           return InteractionResult.SUCCESS;
       }

       ServerLevel serverLevel = (ServerLevel) level;
       BlockPos spawnPos = pContext.getClickedPos().relative(pContext.getClickedFace());
       Player player = pContext.getPlayer();

       PikminEntity pikmin = ModEntities.PIKMIN.get().create(level);

       if (pikmin != null) {
           pikmin.setPikminType(this.pikminType);
           level.playSound(null, spawnPos, ModSounds.PIKMIN_GREETING.get(), SoundSource.NEUTRAL, 0.5f, 1.0f);

           pikmin.moveTo(
                   spawnPos.getX() + 0.5,
                   spawnPos.getY(),
                   spawnPos.getZ() + 0.5,
                   pContext.getPlayer() != null ? pContext.getPlayer().getYRot(): 0.0F,
                   0.0F
                );

                if (pContext.getPlayer() != null) {
                    pikmin.setOwnerUUID(pContext.getPlayer().getUUID());
                    pikmin.setPikminState(PikminState.FOLLOWING);
                }

                level.addFreshEntity(pikmin);

                ItemStack stack = pContext.getItemInHand();
                if (pContext.getPlayer() != null && !pContext.getPlayer().getAbilities().instabuild) {
                    stack.shrink(1);
                }
           return InteractionResult.CONSUME;
            }
        return InteractionResult.FAIL;
    }
}

