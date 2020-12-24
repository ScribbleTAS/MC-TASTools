package de.scribble.lp.TASTools.freezeV2;

public class RelMotionSaver {
	private String playername;
	private boolean applied;
	
	private float relSavedX;
	private float relSavedY;
	private float relSavedZ;
	
	public RelMotionSaver(String playername, boolean applied, float relSavedX, float relSavedY, float relSavedZ) {
		this.relSavedX=relSavedX;
		this.relSavedY=relSavedY;
		this.relSavedZ=relSavedZ;
		
		this.applied=applied;
	}
	public void setRelativeMotionSaved(float[] in) {
		relSavedX=in[0];
		relSavedY=in[1];
		relSavedZ=in[2];
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
	public boolean isApplied() {
		return applied;
	}
	public void setApplied(boolean val) {
		this.applied=val;
	}
}
