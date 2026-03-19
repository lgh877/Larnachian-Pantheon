package net.mcreator.larnachianpantheon.init;

import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.mcreator.larnachianpantheon.configuration.LarnachsModConfigurationConfiguration;
import net.mcreator.larnachianpantheon.LarnachianPantheonMod;

@Mod.EventBusSubscriber(modid = LarnachianPantheonMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LarnachianPantheonModConfigs {
	@SubscribeEvent
	public static void register(FMLConstructModEvent event) {
		event.enqueueWork(() -> {
			ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, LarnachsModConfigurationConfiguration.SPEC, "LarnachianPantheonConfig.toml");
		});
	}
}