/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.larnachianpantheon.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredItem;

import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.Item;

import net.mcreator.larnachianpantheon.LarnachianPantheonMod;

import java.util.function.Function;

public class LarnachianPantheonModItems {
	public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(LarnachianPantheonMod.MODID);
	public static final DeferredItem<Item> LARNACHS_SPAWN_EGG;
	static {
		LARNACHS_SPAWN_EGG = register("larnachs_spawn_egg", properties -> new SpawnEggItem(LarnachianPantheonModEntities.LARNACHS.get(), properties));
	}

	// Start of user code block custom items
	// End of user code block custom items
	private static <I extends Item> DeferredItem<I> register(String name, Function<Item.Properties, ? extends I> supplier) {
		return REGISTRY.registerItem(name, supplier, new Item.Properties());
	}
}