package de.scribble.lp.TASTools.shield;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelShield;
import net.minecraft.client.renderer.BannerTextures;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;

public class CustomTileEntityItemStackRenderer {
	public static CustomTileEntityItemStackRenderer instance = new CustomTileEntityItemStackRenderer();
    private final TileEntityChest chestBasic = new TileEntityChest(BlockChest.Type.BASIC);
    private final TileEntityChest chestTrap = new TileEntityChest(BlockChest.Type.TRAP);
    private final TileEntityEnderChest enderChest = new TileEntityEnderChest();
    private final TileEntityBanner banner = new TileEntityBanner();
    private final TileEntitySkull skull = new TileEntitySkull();
    private final ModelShield modelShield = new ModelShield();

    private ShieldDownloader shieldD= new ShieldDownloader();
    
    public void renderByItem(ItemStack itemStackIn, EntityLivingBase entity)
    {
        if (itemStackIn.getItem() == Items.BANNER)
        {
            this.banner.setItemValues(itemStackIn);
            TileEntityRendererDispatcher.instance.renderTileEntityAt(this.banner, 0.0D, 0.0D, 0.0D, 0.0F);
        }
        else if (itemStackIn.getItem() == Items.SHIELD)
        {
            if (itemStackIn.getSubCompound("BlockEntityTag", false) != null)
            {
                this.banner.setItemValues(itemStackIn);
                Minecraft.getMinecraft().getTextureManager().bindTexture(BannerTextures.SHIELD_DESIGNS.getResourceLocation(this.banner.getPatternResourceLocation(), this.banner.getPatternList(), this.banner.getColorList()));
            }
            else
            {
            	if(entity!=null) {
            		Minecraft.getMinecraft().getTextureManager().bindTexture(shieldD.getResourceLocation(entity));
            	}else {
            		Minecraft.getMinecraft().getTextureManager().bindTexture(BannerTextures.SHIELD_BASE_TEXTURE);
            	}
            }

            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0F, -1.0F, -1.0F);
            this.modelShield.render();
            GlStateManager.popMatrix();
        }
        else if (itemStackIn.getItem() == Items.SKULL)
        {
            GameProfile gameprofile = null;

            if (itemStackIn.hasTagCompound())
            {
                NBTTagCompound nbttagcompound = itemStackIn.getTagCompound();

                if (nbttagcompound.hasKey("SkullOwner", 10))
                {
                    gameprofile = NBTUtil.readGameProfileFromNBT(nbttagcompound.getCompoundTag("SkullOwner"));
                }
                else if (nbttagcompound.hasKey("SkullOwner", 8) && !nbttagcompound.getString("SkullOwner").isEmpty())
                {
                    GameProfile lvt_2_2_ = new GameProfile((UUID)null, nbttagcompound.getString("SkullOwner"));
                    gameprofile = TileEntitySkull.updateGameprofile(lvt_2_2_);
                    nbttagcompound.removeTag("SkullOwner");
                    nbttagcompound.setTag("SkullOwner", NBTUtil.writeGameProfile(new NBTTagCompound(), gameprofile));
                }
            }

            if (TileEntitySkullRenderer.instance != null)
            {
                GlStateManager.pushMatrix();
                GlStateManager.disableCull();
                TileEntitySkullRenderer.instance.renderSkull(0.0F, 0.0F, 0.0F, EnumFacing.UP, 0.0F, itemStackIn.getMetadata(), gameprofile, -1, 0.0F);
                GlStateManager.enableCull();
                GlStateManager.popMatrix();
            }
        }
        else
        {
            Block block = Block.getBlockFromItem(itemStackIn.getItem());

            if (block == Blocks.ENDER_CHEST)
            {
                TileEntityRendererDispatcher.instance.renderTileEntityAt(this.enderChest, 0.0D, 0.0D, 0.0D, 0.0F);
            }
            else if (block == Blocks.TRAPPED_CHEST)
            {
                TileEntityRendererDispatcher.instance.renderTileEntityAt(this.chestTrap, 0.0D, 0.0D, 0.0D, 0.0F);
            }
            else if (block != Blocks.CHEST) net.minecraftforge.client.ForgeHooksClient.renderTileItem(itemStackIn.getItem(), itemStackIn.getMetadata());
            else
            {
                TileEntityRendererDispatcher.instance.renderTileEntityAt(this.chestBasic, 0.0D, 0.0D, 0.0D, 0.0F);
            }
        }
    }
}
