package net.slickramen.producecraft.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.IPlantable;
import net.slickramen.producecraft.item.ModItems;

public class RiceCropBlock extends CropBlock {
    public static final int FIRST_STAGE_MAX_AGE = 5;
    public static final int SECOND_STAGE_MAX_AGE = 1;
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 6);
    public static final IntegerProperty SPAWNED = IntegerProperty.create("spawned", 0, 1);

    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 6.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0)
    };

    public RiceCropBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(AGE, 0)
                .setValue(SPAWNED, 0));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(AGE, 0).setValue(SPAWNED, 0);
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!pLevel.isAreaLoaded(pPos, 1)) return;
        if (pLevel.getRawBrightness(pPos, 0) >= 9) {
            int currentAge = this.getAge(pState);
            if (currentAge < this.getMaxAge()) {
                float growthSpeed = getGrowthSpeed(this, pLevel, pPos);
                if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(pLevel, pPos, pState, pRandom.nextInt((int)(25.0F / growthSpeed) + 1) == 0)) {
                    if (currentAge == FIRST_STAGE_MAX_AGE) {
                        if (pLevel.getBlockState(pPos.above(1)).is(Blocks.AIR)) {
                            pLevel.setBlock(pPos.above(1), this.getStateForAge(currentAge + 1).setValue(SPAWNED, 1), 2);
                        }
                    } else {
                        pLevel.setBlock(pPos, this.getStateForAge(currentAge + 1).setValue(SPAWNED, pState.getValue(SPAWNED)), 2);
                    }
                    net.minecraftforge.common.ForgeHooks.onCropsGrowPost(pLevel, pPos, pState);
                }
            }
        }
    }

    @Override
    public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, IPlantable plantable) {
        return super.mayPlaceOn(state, world, pos);
    }

    @Override
    protected boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return super.canSurvive(pState, pLevel, pPos) ||
                (pLevel.getBlockState(pPos.below()).is(this)
                        && pLevel.getBlockState(pPos.below()).getValue(AGE) == FIRST_STAGE_MAX_AGE
                        && pLevel.getBlockState(pPos).is(this)
                        && pLevel.getBlockState(pPos).getValue(SPAWNED) == 1);
    }

    @Override
    public void growCrops(Level pLevel, BlockPos pPos, BlockState pState) {
        int i = this.getAge(pState) + this.getBonemealAgeIncrease(pLevel);
        int j = this.getMaxAge();
        if (i > j) {
            i = j;
        }

        if (this.getAge(pState) == FIRST_STAGE_MAX_AGE && pLevel.getBlockState(pPos.above(1)).is(Blocks.AIR)) {
            pLevel.setBlock(pPos.above(1), this.getStateForAge(i).setValue(SPAWNED, 1), 2);
        } else {
            pLevel.setBlock(pPos, this.getStateForAge(i - SECOND_STAGE_MAX_AGE), 2);
        }
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModItems.RICE_SEEDS.get();
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return FIRST_STAGE_MAX_AGE + SECOND_STAGE_MAX_AGE;
    }

    @Override
    protected VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE_BY_AGE[this.getAge(pState)];
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(AGE, SPAWNED);
    }
}