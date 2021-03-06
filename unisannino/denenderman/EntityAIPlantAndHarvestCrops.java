package unisannino.denenderman;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.StepSound;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemSeedFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.common.ObfuscationReflectionHelper;

public class EntityAIPlantAndHarvestCrops extends EntityAIBase
{

    private EntityDenEnderman theFarmers;
    private World theWorld;

    public EntityAIPlantAndHarvestCrops(EntityDenEnderman par1EntityFarmers)
    {
        this.theFarmers = par1EntityFarmers;
        this.theWorld = par1EntityFarmers.worldObj;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!this.theWorld.isDaytime())
        {
            return false;
        }
        else
        {
        	if(this.theFarmers.inventory.getFirstEmptyStack() >= 0)
        	{
        		return true;
        	}
        }
        return false;
    }

    public void updateTask()
    {
    	this.hervestCrops(this.theFarmers.posX, this.theFarmers.posY, this.theFarmers.posZ);

    	Object obj = null;
    	for(int i = 0;i < this.theFarmers.inventory.mainInventory.length;i++)
    	{
        	obj = this.theFarmers.inventory.getInventoryObject(i);
        	if(obj == null)
        	{
        		continue;
        	}
        	if(obj instanceof ItemSeeds || obj instanceof ItemFarmerSeeds || obj instanceof ItemSeedFood)
        	{
                this.plantCrops(this.theFarmers.posX, this.theFarmers.posY, this.theFarmers.posZ);
        	}else
        	if(obj instanceof ItemDye)
        	{
        		if(this.theFarmers.inventory.getInventoryMetadata(i) == 15)
        		{
        	        this.plantCrops(this.theFarmers.posX, this.theFarmers.posY, this.theFarmers.posZ);
        		}
        	}
    	}
    }

    public void resetTask()
    {
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
    }

    private void hervestCrops(double var1, double var2, double var3)
    {
        Random rand = this.theFarmers.getRNG();

        for (double h = 4.0D; h > 0.0D; h--)
        {
            int fposX = MathHelper.floor_double((var1 - 1.0D) + rand.nextDouble() * 2D);
            int fposY = MathHelper.floor_double((var2 - 1.0D) + rand.nextDouble() * h);
            int fposZ = MathHelper.floor_double((var3 - 1.0D) + rand.nextDouble() * 2D);
            int fposBlock = this.theWorld.getBlockId(fposX, fposY, fposZ);
            int underblock = this.theWorld.getBlockId(fposX, fposY - 1, fposZ);
            int onFarmland = this.theWorld.getBlockId(fposX, fposY + 1 , fposZ);
            int fposYup = fposY + 1;
            int cropsMeta = this.theWorld.getBlockMetadata(fposX, fposY + 1 , fposZ);

            if(onFarmland > 0)
            {
                Block crops = Block.blocksList[onFarmland];
                if(crops instanceof BlockCrops && cropsMeta == 7)
                {
                    if (this.theFarmers.inventory.addItemStackToInventory(new ItemStack(crops.idDropped(cropsMeta, rand, 0), 1, 0)))
                    {
                    	crops.dropBlockAsItemWithChance(this.theWorld, fposX, fposYup, fposZ, 7, 1.0F, 0);
                    	this.theWorld.setBlock(fposX, fposYup, fposZ, 0);
                    	//this.theFarmers.facetoPath(i, l3, j1, 100F, 100F);
            			this.theFarmers.getLookHelper().setLookPosition(fposX, fposYup, fposZ, 10.0F, (float)this.theFarmers.getVerticalFaceSpeed());
                        this.theWorld.playSoundAtEntity(this.theFarmers, "damage.fallbig", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                        if(!this.theFarmers.worldObj.isRemote)
                        {
                        	this.theFarmers.setHarvestCrops(true);
                        }
                    }
                }else
                try
                {
                	if(Mod_DenEnderman_Core.toggleXies)
                	{
                		/*
                    	if(mod_XieClient.class.getClass() != null)
                    	{
                        	if(crops instanceof net.minecraft.src.xie.blocks.XieBlockPlant)
                        	{
                        		net.minecraft.src.xie.blocks.XieBlockPlant xiecrops = (net.minecraft.src.xie.blocks.XieBlockPlant)crops;
                    			if(ModLoader.getPrivateValue(net.minecraft.src.xie.blocks.XieBlockPlant.class, xiecrops, "drops") != null)
                    			{
                    				int metaxie = this.theWorld.getBlockMetadata(fposX, fposYup, fposZ);
                    				if(metaxie == xiecrops.maxGrowth)
                    				{
                        				net.minecraft.src.xie.Droppables  drops = (net.minecraft.src.xie.Droppables)ModLoader.getPrivateValue(net.minecraft.src.xie.blocks.XieBlockPlant.class, xiecrops, "drops");
                        				net.minecraft.src.xie.Xie.spawnItems(drops.getDropsForMetaAndMultiplier(metaxie, 0), this.theWorld, fposX, fposYup, fposZ);
                                    	this.theWorld.setBlockWithNotify(fposX, fposYup, fposZ, 0);
                                    	//this.theFarmers.facetoPath(i, l3, j1, 100F, 100F);
                            			this.theFarmers.getLookHelper().setLookPosition(fposX, fposYup, fposZ, 100F, 100F);
                                        this.theWorld.playSoundAtEntity(this.theFarmers, "damage.fallbig", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                                        this.theFarmers.hervestSeed = true;
                    				}
                    			}
                        	}
                    	}
                    	*/
                	}
                }catch(Exception e)
                {
                }
            }


            if (onFarmland == Block.melon.blockID)
            {
                if (this.theFarmers.getchanceItems(Item.melon, 5, 3))
                {
                	//this.theFarmers.facetoPath(i, l3, j1, 100F, 100F);
        			this.theFarmers.getLookHelper().setLookPosition(fposX, fposYup, fposZ, 10.0F, (float)this.theFarmers.getVerticalFaceSpeed());
                    this.theWorld.playSoundAtEntity(this.theFarmers, "damage.fallbig", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                    this.theWorld.setBlock(fposX, fposYup, fposZ, 0);
                    if(!this.theFarmers.worldObj.isRemote)
                    {
                    	this.theFarmers.setHarvestCrops(true);
                    }
                }
            }

            if (onFarmland == Block.pumpkin.blockID)
            {
                if (this.theFarmers.inventory.addItemStackToInventory(new ItemStack(Block.pumpkin)))
                {
                	//this.theFarmers.facetoPath(i, l3, j1, 100F, 100F);
        			this.theFarmers.getLookHelper().setLookPosition(fposX, fposYup, fposZ, 10.0F, (float)this.theFarmers.getVerticalFaceSpeed());
                    this.theWorld.playSoundAtEntity(this.theFarmers, "damage.fallbig", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                    this.theWorld.setBlock(fposX, fposYup, fposZ, 0);
                    if(!this.theFarmers.worldObj.isRemote)
                    {
                    	this.theFarmers.setHarvestCrops(true);
                    }
                }
            }
        }
    }

    private void plantCrops(double var1, double var2, double var3)
    {
    	Random rand = this.theFarmers.getRNG();

        for (double h = 4; h > 0; h--)
        {
            int fposX = MathHelper.floor_double((var1 - 1.0D) + rand.nextDouble() * 2D);
            int fposY = MathHelper.floor_double((var2 - 1.0D) + rand.nextDouble() * h);
            int fposZ = MathHelper.floor_double((var3 - 1.0D) + rand.nextDouble() * 2D);
            int fposBlock = this.theWorld.getBlockId(fposX, fposY, fposZ);
            int underblock = this.theWorld.getBlockId(fposX, fposY - 1, fposZ);
            int onFarmland = this.theWorld.getBlockId(fposX, fposY + 1 , fposZ);
            int fposYup = fposY + 1;

            //土を耕す
            if (fposBlock == Block.dirt.blockID && onFarmland == 0)
            {
            	//this.theFarmers.facetoPath(i, l3, j1, 100F, 100F);
    			this.theFarmers.getLookHelper().setLookPosition(fposX, fposYup, fposZ, 10.0F, (float)this.theFarmers.getVerticalFaceSpeed());
                StepSound stepsound1 = Block.tilledField.stepSound;
                this.theWorld.playSoundAtEntity(this.theFarmers, stepsound1.getStepSound(), stepsound1.getPitch(), stepsound1.getPitch());
                this.theWorld.setBlock(fposX, fposY, fposZ, Block.tilledField.blockID);
            }

            /*
             * ここは周囲にカボチャかスイカの苗が生えていないかの判定用
             */
            int l5 = this.theWorld.getBlockId(fposX + 1, fposY + 1 , fposZ);
            int l6 = this.theWorld.getBlockId(fposX - 1, fposY + 1 , fposZ);
            int l7 = this.theWorld.getBlockId(fposX, fposY + 1 , fposZ + 1);
            int l8 = this.theWorld.getBlockId(fposX, fposY + 1 , fposZ - 1);
            int l9 = this.theWorld.getBlockId(fposX + 1, fposY + 1 , fposZ + 1);
            int l10 = this.theWorld.getBlockId(fposX + 1, fposY + 1 , fposZ - 1);
            int l11 = this.theWorld.getBlockId(fposX - 1, fposY + 1 , fposZ - 1);
            int l12 = this.theWorld.getBlockId(fposX - 1, fposY + 1 , fposZ + 1);

            //種うえましょう
            if (l5 == Block.pumpkinStem.blockID || l6 == Block.pumpkinStem.blockID || l7 == Block.pumpkinStem.blockID || l8 == Block.pumpkinStem.blockID || l9 == Block.pumpkinStem.blockID || l10 == Block.pumpkinStem.blockID || l11 == Block.pumpkinStem.blockID || l12 == Block.pumpkinStem.blockID)
            {
            }
            else
            {
                //melon no nae check
                if (l5 == Block.melonStem.blockID || l6 == Block.melonStem.blockID || l7 == Block.melonStem.blockID || l8 == Block.melonStem.blockID || l9 == Block.melonStem.blockID || l10 == Block.melonStem.blockID || l11 == Block.melonStem.blockID || l12 == Block.melonStem.blockID)
                {
                }
                else
                {

					ItemStack itemstack = null;
					Object obj = null;
					ItemSeeds seeds = null;
					ItemSeedFood fseeds = null;
					int meta = 0;
			        for (int length = 0; length < this.theFarmers.inventory.mainInventory.length; length++)
			        {
			            if (this.theFarmers.inventory.mainInventory[length] != null)
			            {
			            	obj = this.theFarmers.inventory.getInventoryObject(length);
							if(obj instanceof ItemSeeds)
							{
								seeds = (ItemSeeds)obj;
								if(seeds.itemID == Item.pumpkinSeeds.itemID || seeds.itemID == Item.melonSeeds.itemID)
								{
									continue;
								}
								meta = this.theFarmers.inventory.getInventoryMetadata(length);
								itemstack = new ItemStack(seeds, 1, meta);
								break;
							}else if(obj instanceof ItemSeedFood)
							{
								fseeds = (ItemSeedFood) obj;
								meta = this.theFarmers.inventory.getInventoryMetadata(length);
								itemstack = new ItemStack(fseeds, 1, meta);
								break;
							}
			            }
			        }
			        if(itemstack != null)
			        {


			        	int cropblock;
			        	int baseblock;

                        try
                        {
                            if(seeds != null)
                            {
                            	cropblock = seeds.getPlantID(this.theWorld, fposX, fposYup, fposZ);
                            	baseblock = (Integer)ObfuscationReflectionHelper.getPrivateValue(ItemSeeds.class, seeds, 1);
                            	Block block = Block.blocksList[cropblock];
                            	if(block instanceof BlockCrops)
                            	{
                            		BlockCrops crops = (BlockCrops)block;
                                	if(fposBlock == baseblock && this.theWorld.isAirBlock(fposX, fposYup, fposZ))
                                	{
                                		if(this.theFarmers.inventory.consumeInventoryItem(itemstack.itemID, itemstack.getItemDamage()))
                                		{
                                        	//this.theFarmers.facetoPath(i, l3, j1, 100F, 100F);
                                			this.theFarmers.getLookHelper().setLookPosition(fposX, fposYup, fposZ, 100F, 100F);
                                            StepSound stepsound2 = Block.crops.stepSound;
                                            this.theWorld.playSoundAtEntity(this.theFarmers, stepsound2.getStepSound(), stepsound2.getPitch(), stepsound2.getPitch());
        									this.theWorld.setBlock(fposX, fposYup, fposZ, cropblock);
                                		}
                                	}
                            	}
							}else if(fseeds != null)
							{
                            	cropblock = (Integer)ObfuscationReflectionHelper.getPrivateValue(ItemSeedFood.class, fseeds, 0);
                            	baseblock = (Integer)ObfuscationReflectionHelper.getPrivateValue(ItemSeedFood.class, fseeds, 1);

                            	Block block = Block.blocksList[cropblock];
                            	if(block instanceof BlockCrops)
                            	{
                            		BlockCrops crops = (BlockCrops)block;
                                	if(fposBlock == baseblock && this.theWorld.isAirBlock(fposX, fposYup, fposZ))
                                	{
                                		if(this.theFarmers.inventory.consumeInventoryItem(itemstack.itemID, itemstack.getItemDamage()))
                                		{
                                        	//this.theFarmers.facetoPath(i, l3, j1, 100F, 100F);
                                			this.theFarmers.getLookHelper().setLookPosition(fposX, fposYup, fposZ, 100F, 100F);
                                            StepSound stepsound2 = Block.crops.stepSound;
                                            this.theWorld.playSoundAtEntity(this.theFarmers, stepsound2.getStepSound(), stepsound2.getPitch(), stepsound2.getPitch());
        									this.theWorld.setBlock(fposX, fposYup, fposZ, cropblock);
                                		}
                                	}
                            	}
							}

                        }
                        catch (Exception e)
                        {
                        }
			        }
                }
            }

            int l4 = this.theWorld.getBlockMetadata(fposX, fposY + 1 , fposZ);

            //骨粉ばらまき
            if (onFarmland == Block.crops.blockID && l4 < 7)
            {
            	if(this.theFarmers.inventory.consumeInventoryItem(Item.dyePowder.itemID, 15))
            	{
                	//this.theFarmers.facetoPath(fposX, fposYup, fposZ, 100F, 100F);
        			this.theFarmers.getLookHelper().setLookPosition(fposX, fposYup, fposZ, 100F, 100F);
                    this.theWorld.playSoundAtEntity(this.theFarmers, "mob.chickenplop", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                    ((BlockCrops)Block.crops).fertilize(this.theWorld, fposX, fposYup, fposZ);
            	}
            }

            int l14 = MathHelper.floor_double(this.theFarmers.posY);
            int l13 = this.theWorld.getBlockId(fposX, l14 - 1, fposZ);
            int l15 = this.theWorld.getBlockId(fposX, l14, fposZ);

            //ラベンダーを植える
            if (rand.nextInt(2000) == 0)
            {
                if (l13 == Block.grass.blockID && l15 == 0)
                {
                    if (this.theFarmers.inventory.consumeInventoryItem(Mod_DenEnderman_Core.farmerSeeds.itemID))
                    {
                    	//this.theFarmers.facetoPath(fposX, l14, fposZ, 100F, 100F);
                        StepSound stepsound1 = Block.plantRed.stepSound;
            			this.theFarmers.getLookHelper().setLookPosition(fposX, fposYup, fposZ, 100F, 100F);
                        this.theWorld.playSoundAtEntity(this.theFarmers, stepsound1.getStepSound(), stepsound1.getPitch(), stepsound1.getPitch());
                        this.theWorld.setBlock(fposX, l14, fposZ, Mod_DenEnderman_Core.lavender.blockID);
                    }
                }
            }
        }
    }
}

