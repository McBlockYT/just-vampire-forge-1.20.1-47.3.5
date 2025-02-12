package net.Babychaosfloh.justvampires.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Interaction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;

public class AltarBlock extends Block {

    public static final BooleanProperty CLICKED = BooleanProperty.create("clicked");
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public AltarBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos,
                                 Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

        if (!pLevel.isClientSide) {

            boolean currentState = pState.getValue(CLICKED);
            pLevel.setBlock(pPos, pState.setValue(CLICKED, !currentState), 3);

            pLevel.playSound(pPlayer, pPos, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1f, 1f);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getClockWise());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        //  pBuilder.add(CLICKED);
        pBuilder.add(FACING);
    }
}
