package unisannino.denenderman;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStarSandlayer extends Block
{
    protected BlockStarSandlayer(int i)
    {
        super(i, Material.circuits);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        setTickRandomly(true);
        rand = new Random();
    }

    @Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        int l = world.getBlockMetadata(i, j, k) & 7;

        if (l >= 3)
        {
            return AxisAlignedBB.getAABBPool().getAABB(i + minX, j + minY, k + minZ, i + maxX, j + 0.5F, k + maxZ);
        }
        else
        {
            return null;
        }
    }

    @Override
	public int getRenderColor(int i)
    {
        return 0x41cd34;
        //0xfcedba;
    }

    @Override
	public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k)
    {
        return 0x41cd34;
        //0xfcedba;
    }

    @Override
	public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
	public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k)
    {
        int l = iblockaccess.getBlockMetadata(i, j, k) & 7;
        float f = (2 * (1 + l)) / 16F;
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
    }

    @Override
	public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {
        int l = world.getBlockId(i, j - 1, k);

        if (l == 0 || !Block.blocksList[l].isOpaqueCube())
        {
            return false;
        }
        else
        {
            return world.getBlockMaterial(i, j - 1, k).isSolid();
        }
    }

    @Override
	public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        func_314_h(world, i, j, k);

        if (rand.nextInt(10) == 0)
        {
            world.setBlock(i, j, k, 0);
        }
    }

    private boolean func_314_h(World world, int i, int j, int k)
    {
        if (!canPlaceBlockAt(world, i, j, k))
        {
            world.setBlock(i, j, k, 0);
            return false;
        }
        else
        {
            return true;
        }
    }

    @Override
	public void harvestBlock(World world, EntityPlayer entityplayer, int i, int j, int k, int l)
    {
        int i1 = Mod_DenEnderman_Core.starPowder.itemID;
        float f = 0.7F;
        double d = (world.rand.nextFloat() * f) + (1.0F - f) * 0.5D;
        double d1 = (world.rand.nextFloat() * f) + (1.0F - f) * 0.5D;
        double d2 = (world.rand.nextFloat() * f) + (1.0F - f) * 0.5D;
        EntityItem entityitem = new EntityItem(world, i + d, j + d1, k + d2, new ItemStack(i1, 1, 0));
        entityitem.delayBeforeCanPickup = 10;
        world.spawnEntityInWorld(entityitem);
        world.setBlock(i, j, k, 0);
        entityplayer.addStat(StatList.mineBlockStatArray[blockID], 1);
    }

    @Override
	public int idDropped(int i, Random random, int j)
    {
        return Mod_DenEnderman_Core.starPowder.itemID;
    }

    @Override
	public int quantityDropped(Random random)
    {
        return 0;
    }

    @Override
	public void updateTick(World world, int i, int j, int k, Random random)
    {
        world.setBlock(i, j, k, 0);
    }

    @Override
	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
        if (l == 1)
        {
            return true;
        }
        else
        {
            return super.shouldSideBeRendered(iblockaccess, i, j, k, l);
        }
    }

    private Random rand;

    @Override
    public void registerIcons(IconRegister iconreg)
    {
        this.blockIcon = Block.sand.getIcon(0, 0);
    }

    //ここからforge独自のメソッドを利用

   @Override
   public boolean isBlockReplaceable(World world, int x, int y, int z)
   {
	   return true;
   }
}
