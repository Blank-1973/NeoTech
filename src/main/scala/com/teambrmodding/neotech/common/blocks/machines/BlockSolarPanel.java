package com.teambrmodding.neotech.common.blocks.machines;

import com.teambrmodding.neotech.common.tiles.AbstractMachine;
import com.teambrmodding.neotech.common.tiles.machines.generators.TileSolarPanel;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * This file was created for NeoTech
 * <p>
 * NeoTech is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis - pauljoda
 * @since 2/15/2017
 */
@SuppressWarnings("deprecation")
public class BlockSolarPanel extends BlockMachine {
    private int tier;

    /**
     * Main constructor for the block
     */
    public BlockSolarPanel(String name, int tier) {
        super(name, TileSolarPanel.class);
        this.tier = tier;
    }

    /**
     * The tier of this block
     * @return The tier
     */
    public int getTier() {
        return tier;
    }

    /*******************************************************************************************************************
     * Block Methods                                                                                                   *
     *******************************************************************************************************************/

    /***
     * Used to tell if this is a full block
     *
     * Is listed deprecated in favor of logic in block state, but our state calls this
     *
     * @param state The block state
     * @return Is this cube full
     */
    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    /**
     * Get the bounding box of this block
     * @param state  The block state
     * @param source The world
     * @param pos    The block location
     * @return The bounding box
     */
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.25, 0.0);
    }

    /**
     * Called when the block is placed
     * @param worldIn The world
     * @param pos The block position
     * @param state The block state
     * @param placer Who placed the block
     * @param stack The stack that was the block
     */
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {}

    /*******************************************************************************************************************
     * BlockState Methods                                                                                              *
     *******************************************************************************************************************/

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    /**
     * Creates the block state with our properties
     * @return The block state
     */
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this);
    }

    /**
     * We want to get the actual state, passes info to the model not present in meta
     *
     * Is listed deprecated in favor of logic in block state, but our state calls this
     *
     * @param state The incoming state
     * @param worldIn The world
     * @param pos The position
     * @return A state that represents the actual state, not just what was stored
     */
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state;
    }
}