package de.scribble.lp.TASTools.freeze;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class FreezePacket implements IMessage{

	private double motionX;
	private double motionY;
	private double motionZ;
	private boolean init;
	
	public FreezePacket() {}
	
	public FreezePacket(boolean init, double X,double Y, double Z) {
		motionX=X;
		motionY=Y;
		motionZ=Z;
		this.init=init;
	}
	public FreezePacket(boolean init) {
		this.init=init;
		motionX=0;
		motionY=0;
		motionZ=0;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
		this.init=buf.readBoolean();
		motionX=buf.readDouble();
		motionY=buf.readDouble();
		motionZ=buf.readDouble();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(init);
		buf.writeDouble(motionX);
		buf.writeDouble(motionY);
		buf.writeDouble(motionZ);
	}
	
	public boolean isMotion() {
		if (motionX==0&&motionY==0&&motionZ==0) {
			return false;
		}else return true;
	}
	public double getX() {
		return motionX;
	}
	public double getY() {
		return motionY;
	}
	public double getZ() {
		return motionZ;
	}
	public boolean startstop() {
		return init;
	}
}
