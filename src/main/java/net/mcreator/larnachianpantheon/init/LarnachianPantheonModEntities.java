/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.larnachianpantheon.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;

import net.mcreator.larnachianpantheon.entity.TestProjectileEntity;
import net.mcreator.larnachianpantheon.entity.LarnachsEntity;
import net.mcreator.larnachianpantheon.LarnachianPantheonMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class LarnachianPantheonModEntities {
	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, LarnachianPantheonMod.MODID);
	public static final RegistryObject<EntityType<LarnachsEntity>> LARNACHS = register("larnachs",
			EntityType.Builder.<LarnachsEntity>of(LarnachsEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(LarnachsEntity::new)

					.sized(2f, 5.7f));
	public static final RegistryObject<EntityType<TestProjectileEntity>> TEST_PROJECTILE = register("test_projectile",
			EntityType.Builder.<TestProjectileEntity>of(TestProjectileEntity::new, MobCategory.MISC).setCustomClientFactory(TestProjectileEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));

	// Start of user code block custom entities
	// End of user code block custom entities
	private static <T extends Entity> RegistryObject<EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
		return REGISTRY.register(registryname, () -> (EntityType<T>) entityTypeBuilder.build(registryname));
	}

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			LarnachsEntity.init();
		});
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(LARNACHS.get(), LarnachsEntity.createAttributes().build());
	}
}