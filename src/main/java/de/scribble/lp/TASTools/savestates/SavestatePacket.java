package de.scribble.lp.TASTools.savestates;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SavestatePacket implements IMessage{
	private boolean loadSave;
	private int mode;
	
	public SavestatePacket() {
		this.loadSave=false;
		this.mode=0;
	}
	public SavestatePacket(boolean loadSave) {
		this.loadSave=loadSave;
		this.mode=0;
	}
	public SavestatePacket(boolean loadSave, int modeIn) {
		this.loadSave=loadSave;
		this.mode=modeIn;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
		this.loadSave=buf.readBoolean();
		this.mode=buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(loadSave);
		buf.writeInt(mode);
	}
	
	public boolean isLoadSave() {
		return loadSave;
	}
	
	public int getMode() {
		return mode;
	}
}
