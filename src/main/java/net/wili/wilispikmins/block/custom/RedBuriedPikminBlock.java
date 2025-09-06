package net.wili.wilispikmins.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.wili.wilispikmins.entity.ModEntities;
import net.wili.wilispikmins.entity.custom.RedPikminEntity;

public class RedBuriedPikminBlock extends BushBlock {

    private static final VoxelShape SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 8.0, 12.0);

    public RedBuriedPikminBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    // interacción con el bloque
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(!pLevel.isClientSide() && pHand == InteractionHand.MAIN_HAND) {
            // quitar bloque
            pLevel.destroyBlock(pPos, false);

            // spawnear pikmin rojo
            RedPikminEntity pikmin = new RedPikminEntity(ModEntities.RED_PIKMIN.get(), pLevel);
            pikmin.moveTo(pPos.getX() + 0.5, pPos.getY(), pPos.getZ() + 0.5, 0, 0);
            pLevel.addFreshEntity(pikmin);

            return InteractionResult.SUCCESS;
        }
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }
}
