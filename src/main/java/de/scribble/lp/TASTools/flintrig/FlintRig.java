package de.scribble.lp.TASTools.flintrig;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FlintRig {
	  public static boolean enable = false;
	  
	  @SubscribeEvent
	  public void onGravelBreak(BlockEvent.HarvestDropsEvent ev)
	  {
	    if (!enable) {
	      return;
	    }
	    if (ev.state.getBlock() == Blocks.gravel)
	    {
	      for (int i = 0; i < ev.drops.size(); i++) {
	        ev.drops.remove(i);
	      }
	      ev.drops.add(new ItemStack(Items.flint));
	    }
	  }
	}
