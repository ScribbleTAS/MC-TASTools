package de.scribble.lp.TASTools.freeze;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class FreezePacket implements IMessage{

	private int mode;
	private boolean enabled;
	
	public FreezePacket() {}
	
	public FreezePacket(boolean init, int mode) {
		this.enabled=init;
		this.mode=mode;
	}
	public FreezePacket(boolean enable) {
		this.enabled=enable;
		mode=0;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
		this.enabled=buf.readBoolean();
		this.mode=buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(enabled);
		buf.writeInt(mode);
	}
	
	public int getMode() {
		return mode;
	}
	public boolean startstop() {
		return enabled;
	}
}
