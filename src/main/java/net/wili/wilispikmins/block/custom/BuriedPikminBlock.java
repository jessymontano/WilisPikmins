package net.wili.wilispikmins.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.wili.wilispikmins.entity.ModEntities;
import net.wili.wilispikmins.entity.custom.PikminEntity;
import net.wili.wilispikmins.entity.custom.RedPikminEntity;
import net.wili.wilispikmins.entity.custom.enums.GrowthStage;
import net.wili.wilispikmins.entity.custom.enums.PikminState;
import net.wili.wilispikmins.entity.custom.enums.PikminType;
import net.wili.wilispikmins.sound.ModSounds;
import org.jetbrains.annotations.Nullable;

public class BuriedPikminBlock extends BushBlock {

    private static final VoxelShape LEAF_SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 8.0, 12.0);
    private static final VoxelShape BUD_SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 10.0, 12.0);
    private static final VoxelShape FLOWER_SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 12.0, 12.0);

    public static final EnumProperty<PikminType> PIKMIN_TYPE =
            EnumProperty.create("type", PikminType.class);
    public static final EnumProperty<GrowthStage> GROWTH_STAGE =
            EnumProperty.create("stage", GrowthStage.class);
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 2);

    public BuriedPikminBlock(Properties pProperties) {
        super(pProperties);

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(PIKMIN_TYPE, PikminType.RED)
                .setValue(GROWTH_STAGE, GrowthStage.LEAF)
                .setValue(AGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PIKMIN_TYPE, GROWTH_STAGE, AGE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        GrowthStage stage = state.getValue(GROWTH_STAGE);
        return switch (stage) {
            case LEAF -> LEAF_SHAPE;
            case BUD -> BUD_SHAPE;
            case FLOWER -> FLOWER_SHAPE;
        };
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

            // activar sonido
            pLevel.playSound(null, pPos, ModSounds.PIKMIN_GREETING.get(), SoundSource.NEUTRAL, 0.5f, 1.0f);
            
            // spawnear pikmin con el tipo y etapa correctos
            PikminType type = pState.getValue(PIKMIN_TYPE);
            GrowthStage stage = pState.getValue(GROWTH_STAGE);

            PikminEntity pikmin = new PikminEntity(pLevel, type);
            pikmin.setPos(pPos.getX() + 0-5, pPos.getY(), pPos.getZ() + 0.5);
            pikmin.setGrowthStage(stage);
            pikmin.setOwnerUUID(pPlayer.getUUID());

            pLevel.addFreshEntity(pikmin);

            // activar animacion de salida
            pikmin.triggerPopAnimation();

            return InteractionResult.SUCCESS;
        }
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);

        if (random.nextInt(25) == 0) {
            this.grow(level, pos, state);
        }
    }

    protected void grow(ServerLevel level, BlockPos pos, BlockState state) {
        int age = state.getValue(AGE);
        GrowthStage currentStage = state.getValue(GROWTH_STAGE);

        if (age < AGE.getPossibleValues().size() - 1) {
            int newAge = age + 1;
            BlockState newState = state.setValue(AGE, newAge);

            GrowthStage newStage = GrowthStage.values()[newAge / 2];
            if (newStage != currentStage) {
                newState = newState.setValue(GROWTH_STAGE, newStage);
            }

            level.setBlock(pos, newState, 3);
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(AGE) < AGE.getPossibleValues().size() - 1;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(PIKMIN_TYPE, getRandomPikminType(context.getLevel().random))
                .setValue(GROWTH_STAGE, GrowthStage.LEAF)
                .setValue(AGE, 0);
    }

    private PikminType getRandomPikminType(RandomSource random) {
        int roll = random.nextInt(100);
        if (roll < 15) return PikminType.RED;
        if (roll < 30) return PikminType.YELLOW;
        if (roll < 45) return PikminType.BLUE;
        if (roll < 60) return PikminType.WHITE;
        if (roll < 75) return PikminType.PURPLE;
        if (roll < 90) return PikminType.WINGED;
        return PikminType.ROCK;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return !state.canSurvive(level, pos) ?
                net.minecraft.world.level.block.Blocks.AIR.defaultBlockState() :
                super.updateShape(state,direction,neighborState,level,pos,neighborPos);
    }
}
