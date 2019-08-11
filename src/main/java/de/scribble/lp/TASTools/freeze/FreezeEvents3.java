package de.scribble.lp.TASTools.freeze;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import de.scribble.lp.TASTools.CommonProxy;
import de.scribble.lp.TASTools.ModLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.world.WorldEvent;

public class FreezeEvents3 {
	/**
	 * To replace PlayerLoggedOut in multi and singleplayer. PLO is called 3 times when leaving the game...
	 * This is to prevent that
	 */
	public static boolean unloadstop=false;
	@SubscribeEvent
	public void onLeaveWorld(WorldEvent.Unload ev) {
		if (unloadstop) {
			CommonProxy.logger.debug("WorldUnload Event triggered in Freeze Events");
			unloadstop = false;
			{
				FreezeHandler.stopFreezeServerNoUpdate();
				ModLoader.NETWORK.sendTo(new FreezePacket(false), (EntityPlayerMP) FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().playerEntityList.get(0));
			}
		}
	}
}
