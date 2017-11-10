package com.mrcrayfish.key;

import com.mrcrayfish.key.tileentity.TileEntityKeyRack;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class RegistrationHandler {
	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		event.getRegistry().register(MrCrayfishKeyMod.block_key_rack);
	}

	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> event) {
		event.getRegistry().register(MrCrayfishKeyMod.item_key);
		event.getRegistry().register(MrCrayfishKeyMod.item_master_key);
		event.getRegistry().register(MrCrayfishKeyMod.item_key_ring);

		event.getRegistry().register(MrCrayfishKeyMod.block_key_rack.getItemBlock());

		RegistrationHandler.registerTileEntities();
	}

	private static void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityKeyRack.class, "ckmKeyRack");
	}
}