package de.scribble.lp.TASTools.freezeV2;

public class MotionSaverServer {
	private String playername;
	
	private double motionSavedX;
	private double motionSavedY;
	private double motionSavedZ;
	
	private float relSavedX;
	private float relSavedY;
	private float relSavedZ;
	
	private float falldistance;

	public MotionSaverServer(String playername, double motionSavedX, double motionSavedY, double motionSavedZ, float relSavedX, float relSavedY, float relSavedZ, float falldistance) {
		this.playername=playername;
		this.motionSavedX=motionSavedX;
		this.motionSavedY=motionSavedY;
		this.motionSavedZ=motionSavedZ;
		
		this.relSavedX=relSavedX;
		this.relSavedY=relSavedY;
		this.relSavedZ=relSavedZ;
		
		this.falldistance=falldistance;
	}
	public float getRelSavedX() {
		return relSavedX;
	}
	public float getRelSavedY() {
		return relSavedY;
	}
	public float getRelSavedZ() {
		return relSavedZ;
	}
	public String getPlayername() {
		return playername;
	}
	public double getMotionSavedX() {
		return motionSavedX;
	}
	public double getMotionSavedY() {
		return motionSavedY;
	}
	public double getMotionSavedZ() {
		return motionSavedZ;
	}
	public float getFalldistance() {
		return falldistance;
	}
}
