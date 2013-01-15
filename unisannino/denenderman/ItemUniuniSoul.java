package unisannino.denenderman;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemUniuniSoul extends ItemEgg
{
    public ItemUniuniSoul(int i)
    {
        super(i);
        maxStackSize = 64;
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    @Override
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
    {
        return 0x41cd34;
    }

    @Override
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        if (!entityplayer.capabilities.isCreativeMode)
        {
            itemstack.stackSize--;
        }

        world.playSoundAtEntity(entityplayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!world.isRemote)
        {
            world.spawnEntityInWorld(new EntityUniuniSoul(world, entityplayer));
        }

        return itemstack;
    }
}
