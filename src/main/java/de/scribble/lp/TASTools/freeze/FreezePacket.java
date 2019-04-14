package de.scribble.lp.TASTools.freeze;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class FreezePacket implements IMessage{

	private double posmotionX;
	private double posmotionY;
	private double posmotionZ;
	private boolean init;
	
	public FreezePacket() {}
	
	public FreezePacket(boolean init, double X,double Y, double Z) {
		posmotionX=X;
		posmotionY=Y;
		posmotionZ=Z;
		this.init=init;
	}
	public FreezePacket(boolean init) {
		this.init=init;
		posmotionX=0;
		posmotionY=0;
		posmotionZ=0;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
		this.init=buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(init);
	}
	
	public boolean isMotion() {
		if (posmotionX==0&&posmotionY==0&&posmotionZ==0) {
			return false;
		}else return true;
	}
	public double getX() {
		return posmotionX;
	}
	public double getY() {
		return posmotionY;
	}
	public double getZ() {
		return posmotionZ;
	}
	public boolean startstop() {
		return init;
	}
}
