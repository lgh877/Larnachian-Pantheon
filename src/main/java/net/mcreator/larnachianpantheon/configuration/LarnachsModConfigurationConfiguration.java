package net.mcreator.larnachianpantheon.configuration;

import net.neoforged.neoforge.common.ModConfigSpec;

public class LarnachsModConfigurationConfiguration {
	public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
	public static final ModConfigSpec SPEC;
	public static final ModConfigSpec.ConfigValue<Boolean> CAMERASHAKE;
	public static final ModConfigSpec.ConfigValue<Boolean> CINEMATICCAMERACHANGE;
	public static final ModConfigSpec.ConfigValue<Boolean> RAMPAGEMODE;
	static {
		CAMERASHAKE = BUILDER.comment("Enables shake of camera in certain events.").define("should_enable_camera_shake", true);
		CINEMATICCAMERACHANGE = BUILDER.comment("Enables camera movement when certain scene change is required.").define("should_play_cinematic_camera_change", true);
		RAMPAGEMODE = BUILDER.comment("Makes Larnachs attack every living entity, not only player.").define("rampage_mode", true);

		SPEC = BUILDER.build();
	}

}