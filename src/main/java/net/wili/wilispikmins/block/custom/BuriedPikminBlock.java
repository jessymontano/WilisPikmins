package net.wili.wilispikmins.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.wili.wilispikmins.entity.ModEntities;
import net.wili.wilispikmins.entity.custom.PikminEntity;
import net.wili.wilispikmins.entity.custom.enums.GrowthStage;
import net.wili.wilispikmins.entity.custom.enums.PikminType;
import net.wili.wilispikmins.sound.ModSounds;
import net.wili.wilispikmins.util.ModTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BuriedPikminBlock extends Block {

    private static final VoxelShape LEAF_SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 8.0, 12.0);
    private static final VoxelShape BUD_SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 10.0, 12.0);
    private static final VoxelShape FLOWER_SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 12.0, 12.0);

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final EnumProperty<PikminType> PIKMIN_TYPE =
            EnumProperty.create("type", PikminType.class);
    public static final EnumProperty<GrowthStage> GROWTH_STAGE =
            EnumProperty.create("stage", GrowthStage.class);
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 2);

    public BuriedPikminBlock(Properties pProperties) {
        super(pProperties);

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(PIKMIN_TYPE, PikminType.RED)
                .setValue(GROWTH_STAGE, GrowthStage.LEAF)
                .setValue(AGE, 0)
                .setValue(WATERLOGGED, false));
    }

    @Override
    public boolean canSurvive(@NotNull BlockState pState, LevelReader pLevel, BlockPos pPos) {
        BlockPos below = pPos.below();
        BlockState belowState = pLevel.getBlockState(below);

        return !belowState.isAir();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PIKMIN_TYPE, GROWTH_STAGE, AGE, WATERLOGGED);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
       boolean water = context.getLevel()
               .getFluidState(context.getClickedPos())
               .getType() == Fluids.WATER;

        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(PIKMIN_TYPE, getPikminTypeForBiome(context.getLevel(), context.getClickedPos()))
                .setValue(GROWTH_STAGE, GrowthStage.LEAF)
                .setValue(AGE, 0)
                .setValue(WATERLOGGED, water);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        GrowthStage stage = state.getValue(GROWTH_STAGE);
        return switch (stage) {
            case LEAF -> LEAF_SHAPE;
            case BUD -> BUD_SHAPE;
            case FLOWER -> FLOWER_SHAPE;
        };
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.MODEL;
    }

    // interacción con el bloque
    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState pState, Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull BlockHitResult pHit) {
        if(!pLevel.isClientSide()) {
            // quitar bloque
            pLevel.destroyBlock(pPos, false);

            // activar sonido
            pLevel.playSound(null, pPos, ModSounds.PIKMIN_GREETING.get(), SoundSource.NEUTRAL, 0.5f, 1.0f);
            
            // spawnear pikmin con el tipo y etapa correctos
            PikminType type = pState.getValue(PIKMIN_TYPE);
            GrowthStage stage = pState.getValue(GROWTH_STAGE);

            PikminEntity pikmin = ModEntities.PIKMIN.get().create(pLevel);

            assert pikmin != null;
            pikmin.moveTo(
                    pPos.getX() + 0.5,
                    pPos.getY(),
                    pPos.getZ() + 0.5,
                    pLevel.random.nextFloat() * 360f,
                    0
            );

            pikmin.setPikminType(type);
            pikmin.setGrowthStage(stage);
            pikmin.setOwnerUUID(pPlayer.getUUID());

            Direction facing = pState.getValue(FACING);
            pikmin.setYRot(facing.toYRot());

            // activar animacion de salida
            pikmin.triggerPopAnimation();
            pLevel.addFreshEntity(pikmin);

            return InteractionResult.SUCCESS;
        }
        return super.useWithoutItem(pState, pLevel, pPos, pPlayer, pHit);
    }

    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
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


    private PikminType getPikminTypeForBiome(LevelAccessor level, BlockPos pos) {
       Holder<Biome> biome = level.getBiome(pos);

       if (biome.is(ModTags.Biomes.HAS_RED_PIKMIN)) {
           return PikminType.RED;
       }
       if (biome.is(ModTags.Biomes.HAS_YELLOW_PIKMIN)) {
           return PikminType.YELLOW;
       }
       if (biome.is(ModTags.Biomes.HAS_BLUE_PIKMIN)) {
           return PikminType.BLUE;
       }
       if (biome.is(ModTags.Biomes.HAS_PURPLE_PIKMIN)) {
           return PikminType.PURPLE;
       }
       if (biome.is(ModTags.Biomes.HAS_WHITE_PIKMIN)){
           return PikminType.WHITE;
       }
       if (biome.is(ModTags.Biomes.HAS_WINGED_PIKMIN)) {
           return PikminType.WINGED;
       }
        return PikminType.ROCK;
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        return !state.canSurvive(level, pos) ?
                net.minecraft.world.level.block.Blocks.AIR.defaultBlockState() :
                super.updateShape(state,direction,neighborState,level,pos,neighborPos);
    }

    @Override
    public @NotNull FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED)
                ? Fluids.WATER.getSource(false)
                : super.getFluidState(pState);
    }
}
