package com.mrcrayfish.key;

import org.apache.logging.log4j.Logger;

import com.mrcrayfish.key.blocks.BlockKeyRack;
import com.mrcrayfish.key.event.KeyEvents;
import com.mrcrayfish.key.gui.GuiHandler;
import com.mrcrayfish.key.items.ItemKey;
import com.mrcrayfish.key.items.ItemKeys;
import com.mrcrayfish.key.items.ItemMasterKey;
import com.mrcrayfish.key.lock.LockManager;
import com.mrcrayfish.key.proxy.CommonProxy;

import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION)
public class MrCrayfishKeyMod {

	@Instance(Reference.MOD_ID)
	public static MrCrayfishKeyMod instance;

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;

	public static KeyTab tabKey = new KeyTab("tabKey");

	public static Logger logger;

	public static final BlockKeyRack block_key_rack = new BlockKeyRack(Material.WOOD);
	public static final Item item_key = new ItemKey()
			.setRegistryName(new ResourceLocation(Reference.MOD_ID, "item_key")).setUnlocalizedName("item_key")
			.setCreativeTab(MrCrayfishKeyMod.tabKey);
	public static final Item item_master_key = new ItemMasterKey()
			.setRegistryName(new ResourceLocation(Reference.MOD_ID, "item_master_key"))
			.setUnlocalizedName("item_master_key");

	public static final Item item_key_ring = new ItemKeys()
			.setRegistryName(new ResourceLocation(Reference.MOD_ID, "item_key_ring"))
			.setUnlocalizedName("item_key_ring").setCreativeTab(MrCrayfishKeyMod.tabKey);

	@EventHandler
	public void init(FMLInitializationEvent event) {
		MrCrayfishKeyMod.proxy.init();

		MinecraftForge.EVENT_BUS.register(new KeyEvents());
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

		// GameRegistry.addRecipe(new ItemStack(KeyItems.item_key), "NNI", 'I',
		// Items.gold_ingot, 'N', Items.gold_nugget);
		// GameRegistry.addRecipe(new ItemStack(KeyItems.item_iron_nugget, 9), "I", 'I',
		// Items.iron_ingot);
		// GameRegistry.addRecipe(new ItemStack(KeyItems.item_key_ring), "NNN", "N N",
		// "NNN", 'N',
		// KeyItems.item_iron_nugget);
		// GameRegistry.addRecipe(new ItemStack(Items.iron_ingot), "NNN", "NNN", "NNN",
		// 'N', KeyItems.item_iron_nugget);
		// GameRegistry.addRecipe(new ItemStack(KeyBlocks.block_key_rack), "WWW", "NNN",
		// 'W',
		// new ItemStack(Blocks.log2, 1, 1), 'N', KeyItems.item_iron_nugget);
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		MrCrayfishKeyMod.logger = event.getModLog();

		LockManager.registerTypes();
	}
}
