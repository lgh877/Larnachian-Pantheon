/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.larnachianpantheon.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredItem;

import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.Item;

import net.mcreator.larnachianpantheon.LarnachianPantheonMod;

import java.util.function.Function;

public class LarnachianPantheonModItems {
	public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(LarnachianPantheonMod.MODID);
    public static final DeferredItem<SpawnEggItem> LARNACHS_SPAWN_EGG = REGISTRY.register("larnachs_spawn_egg", () -> new SpawnEggItem((new Item.Properties().spawnEgg(LarnachianPantheonModEntities.LARNACHS.get()).setId(prefix("larnachs_spawn_egg")))));

    private static ResourceKey<Item> prefix(String path) {
        return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(LarnachianPantheonMod.MODID, path));
    }
}