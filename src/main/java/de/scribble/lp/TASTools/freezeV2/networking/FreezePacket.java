package de.scribble.lp.TASTools.freezeV2.networking;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class FreezePacket implements IMessage{
	private boolean enabled;
	
	private boolean sendPacket;
	
	public FreezePacket() {
	}
	public FreezePacket(boolean enabled, boolean sendPacket) {
		this.enabled=enabled;
		this.sendPacket=sendPacket;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		enabled=buf.readBoolean();
		sendPacket=buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(enabled);
		buf.writeBoolean(sendPacket);
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	public boolean sendPacket() {
		return sendPacket;
	}
}
