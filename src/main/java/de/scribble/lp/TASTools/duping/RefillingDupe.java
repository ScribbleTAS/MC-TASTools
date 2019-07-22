package de.scribble.lp.TASTools.duping;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import de.scribble.lp.TASTools.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;

public class RefillingDupe {
	private Minecraft mc= Minecraft.getMinecraft(); 
	private TileEntityChest foundchest;
	private int chestcounter=0;
	private int chestitemcounter=0;
	private int itemcounter=0;
	
	public void killItems(List<EntityItem> list){
		for(int index=0;index<list.size();index++){
			list.get(index).setDead();
		}
	}
	
	/**
	 * Loads/refills chests and item entitys on the ground, essentially replacing them or spawning new Items
	 * @param file
	 * @param player
	 */
	public void refill(File file, EntityPlayer player){
		String[] coords;
		String[] items;
		String[] enchantments;
		World world = player.getEntityWorld();
		try{
			BufferedReader Buff = new BufferedReader(new FileReader(file));
			String s;
			while (true){
				if((s=Buff.readLine()).equalsIgnoreCase("END")){
					break;
				}
				else if(s.startsWith("#")){				//comments
					continue;
				}
				else if(s.startsWith("Chest:")){		//refill chests
					while (true){
						if((s=Buff.readLine()).equalsIgnoreCase("\t-")){	//check for the end of the chest section
							break;
						}
						else if(s.startsWith("#")){		//comments
							continue;
						}
						else if(s.startsWith("\tx")){
							coords=s.split("(x=)|(,\\ y=)|(,\\ z=)");		//getting the coordinates of the chest
							if (world.getBlock(Integer.parseInt(coords[1]),Integer.parseInt(coords[2]),Integer.parseInt(coords[3]))== Blocks.chest||world.getBlock(Integer.parseInt(coords[1]),Integer.parseInt(coords[2]),Integer.parseInt(coords[3]))== Blocks.trapped_chest){	//check if the targeted block is a chest or a redstone chest
									
								foundchest= (TileEntityChest) world.getTileEntity(Integer.parseInt(coords[1]),Integer.parseInt(coords[2]),Integer.parseInt(coords[3]));
								
								/*Check if the player is too far away from the chest and prevents it from being refilled... A failsafe and cheat prevention*/
								if(player.getDistanceSq((double)foundchest.xCoord, (double)foundchest.yCoord, (double)foundchest.zCoord)>50.0){
										CommonProxy.logger.error("Chest at "+Integer.parseInt(coords[1])+" "+Integer.parseInt(coords[2])+" "+Integer.parseInt(coords[3])+" is too far away! Distance: "+player.getDistanceSq((double)foundchest.xCoord, (double)foundchest.yCoord, (double)foundchest.zCoord));
										continue;
								}
								while(true){
									if((s=Buff.readLine()).equalsIgnoreCase("\t\t-")){ 	//check for the end of the chest contains section
										break;
									}
									else if(s.startsWith("#")){		//comments
										continue;
									}
									else if(s.startsWith("\t\tSlot")){ 		//read out the items from the line and puts it into the chest slot
										items=s.split(";");
										
										/*items[2]= itemID, items[4]=amount, items[5]=damage*/
										ItemStack properties= new ItemStack(Item.getItemById(Integer.parseInt(items[2])),
																								Integer.parseInt(items[4]),
																								Integer.parseInt(items[5]));
										
										/*Split items[7] into enchantmentID and enchantmentLvl*/
										if(!items[7].equals("[]")){
											enchantments=items[7].split("(\\[\\{lvl:)|(s,id:)|(s\\},\\{lvl:)|(s\\})");
											for(int index=1;index<=(enchantments.length-2)/2;index++){
												addEnchantmentbyID(properties, Integer.parseInt(enchantments[2*index]), Integer.parseInt(enchantments[2*index-1]));
											}
										}
										/*Add the custom name if available*/
										if(!items[6].equals("null")){
											properties.setStackDisplayName(items[6]);
										}
										foundchest.setInventorySlotContents(Integer.parseInt(items[1]), properties);	//Set the item into the slot
										chestitemcounter++; //for logging
									}
								}chestcounter++; //for logging
							}
							else{	//Message if there is no chest at the specified coordinates, can happen when using /dupe
								CommonProxy.logger.error("Didn't find a chest at "+Integer.parseInt(coords[1])+" "+Integer.parseInt(coords[2])+" "+Integer.parseInt(coords[3])+".");
								continue;
							}
						}
					}
				}
				else if(s.startsWith("Items:")){ 	//refill items on the ground, here just titled as "Items
					
					
					String[] position=s.split(":");
					double[] dupePos= {Double.parseDouble(position[1]),Double.parseDouble(position[2]),Double.parseDouble(position[3])};	//get the position where the s+q was done
					List<EntityItem> entitylist= world.getEntitiesWithinAABB(EntityItem.class, player.boundingBox.expand(10.0, 10.0, 10.0));
					
					
					if(player.getDistanceSq((double)dupePos[0],(double)dupePos[1],(double)dupePos[2])>=50.0){						//abort if the player is too far away from the duping position, cheat prevention and failsafe when using /dupe
						CommonProxy.logger.error("Player moved too far from initial duping position. Aborting EntityDupe! DupePosition: ("+dupePos[0]+";"+dupePos[1]+";"+dupePos[2]+") Distance: "+player.getDistanceSq((double)dupePos[0],(double)dupePos[1],(double)dupePos[2]));
						continue;
					}
					if(!entitylist.isEmpty()){	//Kill all items in the surrounding area
						killItems(entitylist);
					}
					while (true){
						if((s=Buff.readLine()).equalsIgnoreCase("\t-")){	//check for the end of the item section
							break;
						}
						else if(s.startsWith("#")){		//comments
							continue;
						}
						else if(s.startsWith("\tItem;")){
							String[] props=s.split(";");
							ItemStack Overflow= new ItemStack(Item.getItemById(Integer.parseInt(props[5])), //Create the ItemStack
									Integer.parseInt(props[7]),
									Integer.parseInt(props[8]));
							
							if(!props[10].equals("[]")){	//add Enchantments
								enchantments=props[10].split("(\\[\\{lvl:)|(s,id:)|(s\\},\\{lvl:)|(s\\})");
								for(int index=1;index<=(enchantments.length-2)/2;index++){
									addEnchantmentbyID(Overflow, Integer.parseInt(enchantments[2*index]), Integer.parseInt(enchantments[2*index-1]));
								}
							}
							if(!props[9].equals("null")){ //set customName
								Overflow.setStackDisplayName(props[9]);
							}
							//Create the EntityItem from the Itemstack Overflow
							EntityItem newitem=new EntityItem(world, Double.parseDouble(props[2]), Double.parseDouble(props[3]), Double.parseDouble(props[4]), Overflow);
							world.spawnEntityInWorld(newitem);
							
							//Apply the age
							newitem.age=Integer.parseInt(props[11]);
							
							//Apply the pickupdelay
							newitem.delayBeforeCanPickup=Integer.parseInt(props[12]);
							
							newitem.motionX=0;	//set the motion to zero so it doesn't fly around
							newitem.motionY=0;
							newitem.motionZ=0;
							itemcounter++; //for logging
						}
					}
				}
			}
			Buff.close();
			if(chestcounter==0&&itemcounter==0){
				CommonProxy.logger.info("Nothing refilled");
			}else{
				CommonProxy.logger.info("Refilled "+chestcounter+" chest(s) with "+chestitemcounter+" item(s) and spawned "+ itemcounter+ " item(s) on the ground.");
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds the enchantment by ID
	 * @param Stack	ItemStack to enchant
	 * @param ID	EnchantmentID
	 * @param level	Strength of the Enchantment
	 * @return ItemStack
	 */
	private ItemStack addEnchantmentbyID(ItemStack Stack, int ID, int level){
		switch (ID) {
		case 0:
			Stack.addEnchantment(Enchantment.protection, level);
			break;
		case 1:
			Stack.addEnchantment(Enchantment.fireProtection, level);
			break;
		case 2:
			Stack.addEnchantment(Enchantment.featherFalling, level);
			break;
		case 3:
			Stack.addEnchantment(Enchantment.blastProtection, level);
			break;
		case 4:
			Stack.addEnchantment(Enchantment.projectileProtection, level);
			break;
		case 5:
			Stack.addEnchantment(Enchantment.respiration, level);
			break;
		case 6:
			Stack.addEnchantment(Enchantment.aquaAffinity,level);
			break;
		case 7:
			Stack.addEnchantment(Enchantment.thorns, level);
			break;
		case 16:
			Stack.addEnchantment(Enchantment.sharpness, level);
			break;
		case 17:
			Stack.addEnchantment(Enchantment.smite, level);
			break;
		case 18:
			Stack.addEnchantment(Enchantment.baneOfArthropods, level);
			break;
		case 19:
			Stack.addEnchantment(Enchantment.knockback, level);
			break;
		case 20:
			Stack.addEnchantment(Enchantment.fireAspect, level);
			break;
		case 21:
			Stack.addEnchantment(Enchantment.looting, level);
			break;
		case 32:
			Stack.addEnchantment(Enchantment.efficiency, level);
			break;
		case 33:
			Stack.addEnchantment(Enchantment.silkTouch, level);
			break;
		case 34:
			Stack.addEnchantment(Enchantment.unbreaking, level);
			break;
		case 35:
			Stack.addEnchantment(Enchantment.fortune, level);
			break;
		case 48:
			Stack.addEnchantment(Enchantment.power, level);
			break;
		case 49:
			Stack.addEnchantment(Enchantment.punch, level);
			break;
		case 50:
			Stack.addEnchantment(Enchantment.flame, level);
			break;
		case 51:
			Stack.addEnchantment(Enchantment.infinity, level);
			break;
		}
		return Stack;
	}
}
