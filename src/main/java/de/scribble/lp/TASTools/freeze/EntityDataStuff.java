package de.scribble.lp.TASTools.freeze;

public class EntityDataStuff {
	private String playername;
	private double posX;
	private double posY;
	private double posZ;
	private float pitch;
	private float yaw;
	private double motionX;
	private double motionY;
	private double motionZ;
	
	public EntityDataStuff(String playername, double posX, double posY , double posZ, float pitch, float yaw, double motionX,double motionY,double motionZ) {
		this.playername=playername;
		this.posX=posX;
		this.posY=posY;
		this.posZ=posZ;
		this.pitch=pitch;
		this.yaw=yaw;
		this.motionX=motionX;
		this.motionY=motionY;
		this.motionZ=motionZ;
	}
	public String getPlayername() {
		return playername;
	}
	public double getPosX() {
		return posX;
	}
	public double getPosY() {
		return posY;
	}
	public double getPosZ() {
		return posZ;
	}
	public float getPitch() {
		return pitch;
	}
	public float getYaw() {
		return yaw;
	}
	public double getMotionX() {
		return motionX;
	}
	public double getMotionY() {
		return motionY;
	}
	public double getMotionZ() {
		return motionZ;
	}
}
