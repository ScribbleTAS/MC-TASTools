package de.scribble.lp.TASTools.freezeV2;

public class MotionSaver {
	private String playername;
	private boolean applied;
	
	private double motionSavedX;
	private double motionSavedY;
	private double motionSavedZ;

	
	public MotionSaver(String playername, boolean applied, double motionSavedX, double motionSavedY, double motionSavedZ) {
		this.playername=playername;
		this.motionSavedX=motionSavedX;
		this.motionSavedY=motionSavedY;
		this.motionSavedZ=motionSavedZ;
		
		this.applied=applied;
	}
	public void setMotionSaved(double[] in) {
		motionSavedX=in[0];
		motionSavedY=in[1];
		motionSavedZ=in[2];
	}
	public void setMotionSavedX(double in) {
		motionSavedX=in;
	}
	public void setMotionSavedY(double in) {
		motionSavedY=in;
	}
	public void setMotionSavedZ(double in) {
		motionSavedZ=in;
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
	public String getPlayername() {
		return playername;
	}
	public boolean isApplied() {
		return applied;
	}
	public void setApplied(boolean val) {
		this.applied=val;
	}
}
