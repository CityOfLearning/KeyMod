package com.mrcrayfish.key.proxy;

import com.mrcrayfish.key.tileentity.TileEntityKeyRack;
import com.mrcrayfish.key.tileentity.render.KeyRackRenderer;

import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {
	@Override
	public void init() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityKeyRack.class, new KeyRackRenderer());
	}
}
