package de.scribble.lp.TASTools.keystroke;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;


public class KeystrokesPacket implements IMessage{

	private int corner;
	
	public KeystrokesPacket() {
		this.corner=4;
	}
	

	public KeystrokesPacket(int corner) {
		this.corner=corner;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.corner=buf.readInt();;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(corner);
	}

	
	public int getCorner() {
		return corner;
	}
}
