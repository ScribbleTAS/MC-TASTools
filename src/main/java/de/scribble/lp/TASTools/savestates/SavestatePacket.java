package de.scribble.lp.TASTools.savestates;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SavestatePacket implements IMessage{
	private boolean loadSave;
	
	public SavestatePacket() {
		this.loadSave=false;
	}
	public SavestatePacket(boolean loadSave) {
		this.loadSave=loadSave;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
		this.loadSave=buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(loadSave);
	}
	
	public boolean isLoadSave() {
		return loadSave;
	}
}
