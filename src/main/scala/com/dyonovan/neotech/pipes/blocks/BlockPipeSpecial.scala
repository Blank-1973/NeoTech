package com.dyonovan.neotech.pipes.blocks

import com.dyonovan.neotech.NeoTech
import com.dyonovan.neotech.client.gui.machines.GuiMachineUpgrade
import com.dyonovan.neotech.common.blocks.traits.Upgradeable
import com.dyonovan.neotech.common.container.machines.ContainerMachineUpgrade
import com.dyonovan.neotech.lib.Reference
import com.dyonovan.neotech.managers.{BlockManager, ItemManager}
import com.dyonovan.neotech.pipes.types.{ExtractionPipe, SimplePipe}
import com.teambr.bookshelf.common.tiles.traits.OpensGui
import com.teambr.bookshelf.util.WorldUtils
import net.minecraft.block.BlockContainer
import net.minecraft.block.material.Material
import net.minecraft.block.state.{BlockState, IBlockState}
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.{AxisAlignedBB, BlockPos, EnumFacing, EnumWorldBlockLayer}
import net.minecraft.world.{WorldServer, IBlockAccess, World}
import net.minecraftforge.fml.relauncher.{SideOnly, Side}

import scala.util.Random

/**
  * This file was created for NeoTech
  *
  * NeoTech is licensed under the
  * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
  * http://creativecommons.org/licenses/by-nc-sa/4.0/
  *
  * @author Paul Davis pauljoda
  * @since August 14, 2015
  */
class BlockPipeSpecial(val name : String, mat : Material, tileClass : Class[_ <: SimplePipe]) extends BlockContainer(mat) with OpensGui {

    //Constructor
    setUnlocalizedName(Reference.MOD_ID + ":" + name)
    setCreativeTab(NeoTech.tabPipes)
    setHardness(1.5F)
    setLightOpacity(0)
    setDefaultState(this.blockState.getBaseState
            .withProperty(PipeProperties.SPECIAL_UP, 0.asInstanceOf[Integer])
            .withProperty(PipeProperties.SPECIAL_DOWN, 0.asInstanceOf[Integer])
            .withProperty(PipeProperties.SPECIAL_NORTH, 0.asInstanceOf[Integer])
            .withProperty(PipeProperties.SPECIAL_EAST, 0.asInstanceOf[Integer])
            .withProperty(PipeProperties.SPECIAL_SOUTH, 0.asInstanceOf[Integer])
            .withProperty(PipeProperties.SPECIAL_WEST, 0.asInstanceOf[Integer]))

    protected override def createBlockState: BlockState = {
        new BlockState(this, PipeProperties.SPECIAL_UP, PipeProperties.SPECIAL_DOWN, PipeProperties.SPECIAL_NORTH, PipeProperties.SPECIAL_SOUTH, PipeProperties.SPECIAL_EAST, PipeProperties.SPECIAL_WEST)
    }

    override def getActualState(state: IBlockState, worldIn: IBlockAccess, pos: BlockPos): IBlockState = {
        state.withProperty(PipeProperties.SPECIAL_UP, countConnections(worldIn, pos, EnumFacing.UP).asInstanceOf[Integer])
                .withProperty(PipeProperties.SPECIAL_DOWN, countConnections(worldIn, pos, EnumFacing.DOWN).asInstanceOf[Integer])
                .withProperty(PipeProperties.SPECIAL_NORTH, countConnections(worldIn, pos, EnumFacing.NORTH).asInstanceOf[Integer])
                .withProperty(PipeProperties.SPECIAL_EAST, countConnections(worldIn, pos, EnumFacing.EAST).asInstanceOf[Integer])
                .withProperty(PipeProperties.SPECIAL_SOUTH, countConnections(worldIn, pos, EnumFacing.SOUTH).asInstanceOf[Integer])
                .withProperty(PipeProperties.SPECIAL_WEST, countConnections(worldIn, pos, EnumFacing.WEST).asInstanceOf[Integer])
    }

    /**
      * Convert the given metadata into a BlockState for this Block
      */
    override def getStateFromMeta(meta: Int): IBlockState = {
        getDefaultState
    }

    /**
      * Convert the BlockState into the correct metadata value
      */
    override def getMetaFromState(state: IBlockState): Int = {
        0
    }

    override def breakBlock(worldIn: World, pos: BlockPos, state: IBlockState): Unit = {
        worldIn match {
            case _: WorldServer => //We are on a server
                worldIn.getTileEntity(pos) match {
                    case upgradeable: Upgradeable =>
                        if (upgradeable.upgradeInventory.getStackInSlot(0) != null) {
                            val stack = upgradeable.upgradeInventory.getStackInSlot(0)
                            val random = new Random

                            if(stack != null && stack.stackSize > 0) {
                                val rx = random.nextFloat * 0.8F + 0.1F
                                val ry = random.nextFloat * 0.8F + 0.1F
                                val rz = random.nextFloat * 0.8F + 0.1F

                                val itemEntity = new EntityItem(worldIn,
                                    pos.getX + rx, pos.getY + ry, pos.getZ + rz,
                                    new ItemStack(stack.getItem, stack.stackSize, stack.getItemDamage))

                                if(stack.hasTagCompound)
                                    itemEntity.getEntityItem.setTagCompound(stack.getTagCompound)

                                val factor = 0.05F

                                itemEntity.motionX = random.nextGaussian * factor
                                itemEntity.motionY = random.nextGaussian * factor + 0.2F
                                itemEntity.motionZ = random.nextGaussian * factor
                                worldIn.spawnEntityInWorld(itemEntity)

                                stack.stackSize = 0
                            }
                        }
                }
        }

        worldIn.getTileEntity(pos) match {
            case pipe: SimplePipe =>
                pipe.onPipeBroken()
            case _ =>
        }
    }

    override def setBlockBoundsBasedOnState(worldIn: IBlockAccess, pos: BlockPos) {
        var x1 = 0.25F
        var x2 = 1.0F - x1
        var y1 = 0.25F
        var y2 = 1.0F - y1
        var z1 = 0.25F
        var z2 = 1.0F - z1
        if (countConnections(worldIn, pos, EnumFacing.WEST) > 0) {
            x1 = 0.0F
        }

        if (countConnections(worldIn, pos, EnumFacing.EAST) > 0) {
            x2 = 1.0F
        }

        if (countConnections(worldIn, pos, EnumFacing.NORTH) > 0) {
            z1 = 0.0F
        }

        if (countConnections(worldIn, pos, EnumFacing.SOUTH) > 0) {
            z2 = 1.0F
        }

        if (countConnections(worldIn, pos, EnumFacing.DOWN) > 0) {
            y1 = 0.0F
        }

        if (countConnections(worldIn, pos, EnumFacing.UP) > 0) {
            y2 = 1.0F
        }

        this.setBlockBounds(x1, y1, z1, x2, y2, z2)
    }

    def isPipeConnected(world: IBlockAccess, pos: BlockPos, facing: EnumFacing): Boolean = {
        world.getTileEntity(pos) match {
            case pipe: SimplePipe =>
                pipe.canConnect(facing) || pipe.isSpecialConnection(facing)
            case _ => false
        }
    }

    def countConnections(world: IBlockAccess, pos: BlockPos, facing: EnumFacing): Int = {
        world.getTileEntity(pos) match {
            case pipe: SimplePipe =>
                if (pipe.isSpecialConnection(facing) && pipe.canConnect(facing))
                    2
                else if (pipe.canConnect(facing))
                    1
                else
                    0
            case _ => 0
        }
    }

    override def addCollisionBoxesToList(worldIn: World, pos: BlockPos, state: IBlockState, mask: AxisAlignedBB, list: java.util.List[AxisAlignedBB], collidingEntity: Entity) {
        this.setBlockBoundsBasedOnState(worldIn, pos)
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity)
    }

    override def getRenderType: Int = 3

    override def isOpaqueCube: Boolean = false

    @SideOnly(Side.CLIENT)
    override def isTranslucent: Boolean = true

    override def isFullCube: Boolean = false

    @SideOnly(Side.CLIENT)
    override def getBlockLayer: EnumWorldBlockLayer = EnumWorldBlockLayer.CUTOUT

    override def canRenderInLayer(layer: EnumWorldBlockLayer): Boolean =
        layer == EnumWorldBlockLayer.TRANSLUCENT || layer == EnumWorldBlockLayer.CUTOUT

    override def createNewTileEntity(worldIn: World, meta: Int): TileEntity = tileClass.newInstance()

    override def getServerGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): AnyRef = {
        world.getTileEntity(new BlockPos(x, y, z)) match {
            case upgradeable: Upgradeable =>
                if (player.inventory.getCurrentItem != null && player.inventory.getCurrentItem.getItem == ItemManager.wrench) {
                    return new ContainerMachineUpgrade(player.inventory, world.getTileEntity(new BlockPos(x, y, z)).asInstanceOf[Upgradeable])
                }
            case __ =>
        }
        null
    }

    @SideOnly(Side.CLIENT)
    override def getClientGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): AnyRef = {
        world.getTileEntity(new BlockPos(x, y, z)) match {
            case upgradeable: Upgradeable =>
                if (player.inventory.getCurrentItem != null && player.inventory.getCurrentItem.getItem == ItemManager.wrench) {
                    return new GuiMachineUpgrade(player, world.getTileEntity(new BlockPos(x, y, z)).asInstanceOf[Upgradeable])
                }
            case _ =>
        }
        null
    }
}
