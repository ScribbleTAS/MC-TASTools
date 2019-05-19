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
	private float falldistance;
	
	public EntityDataStuff(String playername, double posX, double posY , double posZ, float pitch, float yaw, double motionX, double motionY, double motionZ, float falldistance) {
		this.playername=playername;
		this.posX=posX;
		this.posY=posY;
		this.posZ=posZ;
		this.pitch=pitch;
		this.yaw=yaw;
		this.motionX=motionX;
		this.motionY=motionY;
		this.motionZ=motionZ;
		this.falldistance=falldistance;
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
	}public float getYaw() {
		return yaw;
	}
	public double getMotionX() {
		return motionX;
	}
	public double getMotionY() {
		return motionY;
		
	}public double getMotionZ() {
		return motionZ;
	}
	public float getFalldistance() {
		return falldistance;
	}
}
