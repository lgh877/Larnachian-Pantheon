package net.mcreator.larnachianpantheon.configuration;

import net.minecraftforge.common.ForgeConfigSpec;

public class LarnachsModConfigurationConfiguration {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;
	public static final ForgeConfigSpec.ConfigValue<Boolean> CAMERASHAKE;
	public static final ForgeConfigSpec.ConfigValue<Boolean> CINEMATICCAMERACHANGE;
	public static final ForgeConfigSpec.ConfigValue<Boolean> RAMPAGEMODE;
	static {
		CAMERASHAKE = BUILDER.comment("Enables shake of camera in certain events.").define("ShouldEnableCameraShake", true);
		CINEMATICCAMERACHANGE = BUILDER.comment("Enables camera movement when certain scene change is required.").define("ShouldPlayCinematicCameraChange", true);
		RAMPAGEMODE = BUILDER.comment("Makes Larnachs attack every living entity, not only player.").define("RampageMode", true);

		SPEC = BUILDER.build();
	}

}