package de.pfannekuchen.TASTools.tracer;

import org.lwjgl.opengl.GL11;

import de.pfannekuchen.TASTools.utils.RenderUtils;
import de.scribble.lp.TASTools.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class Trajectories {
	
	private boolean isEnabled = false;
	public int index = 0; // 11 max
	public float[][] color = new float[][] {
		new float[] {1, 0, 0, 1},
		new float[] {1, .5f, 0, 1},
		new float[] {1, 1, 0, 1},
		new float[] {.5f, 1, 0, 1},
		new float[] {0, 1f, 0, 1},
		new float[] {0, 1, .5f, 1},
		new float[] {0, 1, 1f, 1},
		new float[] {0, 0.5f, 1f, 1},
		new float[] {0, 0, 1f, 1},
		new float[] {1f, 0, 1f, 1},
		new float[] {.5f, 0, 1f, 1},
		new float[] {1, 0, .5f, 1}
	};
	
	@SubscribeEvent
	public void onKey(InputEvent.KeyInputEvent e) {
		if (ClientProxy.TracerlineToggleKey.pressed) {
			isEnabled=!isEnabled;
			index++;
			if (index > 11) index = 0;
		}
	}
	
	@SubscribeEvent
	public void render(RenderWorldLastEvent e) {
		
		if (!isEnabled) return;
		
		EntityPlayerSP player = Minecraft.getMinecraft().player;
		
		// check if player is holding item
		ItemStack stack = player.inventory.getCurrentItem();
		if(stack == null)
			return;
		
		boolean usingBow = stack.getItem() instanceof ItemBow;
		
		// calculate starting position
		double arrowPosX = player.lastTickPosX
			+ (player.posX - player.lastTickPosX) * Minecraft.getMinecraft().timer.renderPartialTicks
			- Math.cos((float)Math.toRadians(player.rotationYaw)) * 0.16F;
		double arrowPosY = player.lastTickPosY
			+ (player.posY - player.lastTickPosY)
				* Minecraft.getMinecraft().timer.renderPartialTicks
			+ player.getEyeHeight() - 0.1;
		double arrowPosZ = player.lastTickPosZ
			+ (player.posZ - player.lastTickPosZ)
				* Minecraft.getMinecraft().timer.renderPartialTicks
			- Math.sin((float)Math.toRadians(player.rotationYaw)) * 0.16F;
		
		// calculate starting motion
		float arrowMotionFactor = usingBow ? 1F : 0.4F;
		float yaw = (float)Math.toRadians(player.rotationYaw);
		float pitch = (float)Math.toRadians(player.rotationPitch);
		double arrowMotionX =
			-Math.sin(yaw) * Math.cos(pitch) * arrowMotionFactor;
		double arrowMotionY = -Math.sin(pitch) * arrowMotionFactor;
		double arrowMotionZ =
			Math.cos(yaw) * Math.cos(pitch) * arrowMotionFactor;
		double arrowMotion = Math.sqrt(arrowMotionX * arrowMotionX
			+ arrowMotionY * arrowMotionY + arrowMotionZ * arrowMotionZ);
		arrowMotionX /= arrowMotion;
		arrowMotionY /= arrowMotion;
		arrowMotionZ /= arrowMotion;
		if(usingBow)
		{
			float bowPower = (72000 - player.getItemInUseCount()) / 20F;
			bowPower = (bowPower * bowPower + bowPower * 2F) / 3F;
			
			if(bowPower > 1F || bowPower <= 0.1F)
				bowPower = 1F;
			
			bowPower *= 3F;
			arrowMotionX *= bowPower;
			arrowMotionY *= bowPower;
			arrowMotionZ *= bowPower;
			
		}else
		{
			arrowMotionX *= 1.5D;
			arrowMotionY *= 1.5D;
			arrowMotionZ *= 1.5D;
		}
		
		// GL settings
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glLineWidth(2);
		
		RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
		
		// draw trajectory line
		double gravity =
			usingBow ? 0.05D : stack.getItem() instanceof ItemPotion ? 0.4D
				: stack.getItem() instanceof ItemFishingRod ? 0.15D : 0.03D;
		Vec3d playerVector = new Vec3d(player.posX,
			player.posY + player.getEyeHeight(), player.posZ);
		GL11.glColor4f(color[index][0], color[index][1], color[index][2], color[index][3]); // Color
		GL11.glBegin(GL11.GL_LINE_STRIP);
		for(int i = 0; i < 1000; i++)
		{
			GL11.glVertex3d(arrowPosX - renderManager.renderPosX,
				arrowPosY - renderManager.renderPosY,
				arrowPosZ - renderManager.renderPosZ);
			
			arrowPosX += arrowMotionX * 0.1;
			arrowPosY += arrowMotionY * 0.1;
			arrowPosZ += arrowMotionZ * 0.1;
			arrowMotionX *= 0.999D;
			arrowMotionY *= 0.999D;
			arrowMotionZ *= 0.999D;
			arrowMotionY -= gravity * 0.1;
			
			if(Minecraft.getMinecraft().world.rayTraceBlocks(playerVector,
				new Vec3d(arrowPosX, arrowPosY, arrowPosZ)) != null)
				break;
		}
		GL11.glEnd();
		
		// draw end of trajectory line
		double renderX = arrowPosX - renderManager.renderPosX;
		double renderY = arrowPosY - renderManager.renderPosY;
		double renderZ = arrowPosZ - renderManager.renderPosZ;
		
		GL11.glPushMatrix();
		GL11.glTranslated(renderX - 0.5, renderY - 0.5, renderZ - 0.5);
		
		GL11.glColor4f(color[index][0], color[index][1], color[index][2], .25f); // Color
		RenderUtils.drawSolidBox();
		GL11.glColor4f(color[index][0], color[index][1], color[index][2], .75f); // Color
		RenderUtils.drawOutlinedBox();
		
		GL11.glPopMatrix();
		
		// GL resets
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glPopMatrix();
	}
	
}
