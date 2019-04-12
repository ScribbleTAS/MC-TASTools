package de.scribble.lp.TASTools.freeze;

import java.util.List;

import de.scribble.lp.TASTools.commands.TastoolsCommandc;
import de.scribble.lp.TASTools.proxy.CommonProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.world.World;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickEmpty;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class FreezeEvents {
	
	public static EntityPlayer playerSp;
	public static List<EntityPlayerMP> playerMP;
	

	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent ev) {
		if(!FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()) {
			playerSp.setPositionAndUpdate(FreezePacketHandler.entity.getPosX(), FreezePacketHandler.entity.getPosY(), FreezePacketHandler.entity.getPosZ());
			playerSp.rotationPitch=FreezePacketHandler.entity.getPitch();
			playerSp.rotationYaw=FreezePacketHandler.entity.getYaw();
		}
		else {
			for(int i=0;i<playerMP.size();i++) {
				playerMP.get(i).setPositionAndUpdate(TastoolsCommandc.entity.get(i).getPosX(), TastoolsCommandc.entity.get(i).getPosY(), TastoolsCommandc.entity.get(i).getPosZ());
				playerMP.get(i).rotationPitch=TastoolsCommandc.entity.get(i).getPitch();
				playerMP.get(i).prevRotationYaw=TastoolsCommandc.entity.get(i).getYaw();
			}
		}
	}
	@SubscribeEvent
	public void disableMouse(MouseEvent ev) {
		ev.setCanceled(true);
	}
}
