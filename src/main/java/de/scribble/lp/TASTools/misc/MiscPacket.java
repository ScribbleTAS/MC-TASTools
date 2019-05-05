package de.scribble.lp.TASTools.misc;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MiscPacket implements IMessage{
	private int mode;
	public MiscPacket() {
		this.mode=0;
	}
	/**
	 * Misc Packet for Clientside activation
	 * @param mode 0=Reloads the Clientconfig, 1=Disables/Enables the Logo
	 */
	public MiscPacket(int mode) {
		this.mode=mode;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
		this.mode=buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(mode);
	}
	public int getMode() {
		return mode;
	}
}
