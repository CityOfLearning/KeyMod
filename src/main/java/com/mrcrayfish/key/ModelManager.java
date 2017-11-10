package com.mrcrayfish.key;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Reference.MOD_ID)
public class ModelManager {
	public static final ModelManager INSTANCE = new ModelManager();

	/**
	 * Register this mod's {@link Fluid}, {@link Block} and {@link Item} models.
	 *
	 * @param event
	 *            The event
	 */
	@SubscribeEvent
	public static void registerAllModels(final ModelRegistryEvent event) {
		ModelManager.INSTANCE.registerBlockModels();
		ModelManager.INSTANCE.registerItemModels();

	}

	public static void registerRenderVariants(Item item, int amount) {
		ResourceLocation[] variants = new ResourceLocation[amount];
		for (int i = 0; i < amount; i++) {
			variants[i] = new ResourceLocation(Reference.MOD_ID, item.getUnlocalizedName().substring(5) + "_" + i);
			ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(variants[i], "inventory"));
		}
		ModelBakery.registerItemVariants(item, variants);
	}

	/**
	 * A {@link StateMapperBase} used to create property strings.
	 */
	private final StateMapperBase propertyStringMapper = new StateMapperBase() {
		@Override
		protected ModelResourceLocation getModelResourceLocation(final IBlockState state) {
			return new ModelResourceLocation("minecraft:air");
		}
	};

	private ModelManager() {
	}

	/**
	 * Register a single model for the {@link Block}'s {@link Item}.
	 * <p>
	 * Uses the registry name as the domain/path and the {@link IBlockState} as the
	 * variant.
	 *
	 * @param state
	 *            The state to use as the variant
	 */
	private void registerBlockItemModel(final IBlockState state) {
		final Block block = state.getBlock();
		final Item item = Item.getItemFromBlock(block);

		if (item != Items.AIR) {
			registerItemModel(item, new ModelResourceLocation(block.getRegistryName(),
					propertyStringMapper.getPropertyString(state.getProperties())));
		}
	}

	/**
	 * Register this mod's {@link Block} models.
	 */
	private void registerBlockModels() {
		registerBlockItemModel(MrCrayfishKeyMod.block_key_rack.getDefaultState());
	}

	/**
	 * Register a single model for an {@link Item}.
	 * <p>
	 * Uses the registry name as the domain/path and {@code "inventory"} as the
	 * variant.
	 *
	 * @param item
	 *            The Item
	 */
	private void registerItemModel(final Item item) {
		registerItemModel(item, item.getRegistryName().toString());
	}

	/**
	 * Register an {@link ItemMeshDefinition} for an {@link Item}.
	 *
	 * @param item
	 *            The Item
	 * @param meshDefinition
	 *            The ItemMeshDefinition
	 */
	private void registerItemModel(final Item item, final ItemMeshDefinition meshDefinition) {
		ModelLoader.setCustomMeshDefinition(item, meshDefinition);
	}

	/**
	 * Register a single model for an {@link Item}.
	 * <p>
	 * Uses {@code fullModelLocation} as the domain, path and variant.
	 *
	 * @param item
	 *            The Item
	 * @param fullModelLocation
	 *            The full model location
	 */
	private void registerItemModel(final Item item, final ModelResourceLocation fullModelLocation) {
		ModelBakery.registerItemVariants(item, fullModelLocation); // Ensure the custom model is loaded and prevent the
																	// default model from being loaded
		registerItemModel(item, stack -> fullModelLocation);
	}

	/**
	 * Register a single model for an {@link Item}.
	 * <p>
	 * Uses {@code modelLocation} as the domain/path and {@link "inventory"} as the
	 * variant.
	 *
	 * @param item
	 *            The Item
	 * @param modelLocation
	 *            The model location
	 */
	private void registerItemModel(final Item item, final String modelLocation) {
		final ModelResourceLocation fullModelLocation = new ModelResourceLocation(modelLocation, "inventory");
		registerItemModel(item, fullModelLocation);
	}

	/**
	 * Register a model for a metadata value of an {@link Item}.
	 * <p>
	 * Uses {@code modelResourceLocation} as the domain, path and variant.
	 *
	 * @param item
	 *            The Item
	 * @param metadata
	 *            The metadata
	 * @param modelResourceLocation
	 *            The full model location
	 */
	private void registerItemModelForMeta(final Item item, final int metadata,
			final ModelResourceLocation modelResourceLocation) {
		ModelLoader.setCustomModelResourceLocation(item, metadata, modelResourceLocation);
		registerItemModel(item, modelResourceLocation);
	}

	/**
	 * Register this mod's {@link Item} models.
	 */
	private void registerItemModels() {
		registerItemModel(MrCrayfishKeyMod.item_key);
		registerItemModel(MrCrayfishKeyMod.item_master_key);
		ModelManager.registerRenderVariants(MrCrayfishKeyMod.item_key_ring, 4);
	}
}