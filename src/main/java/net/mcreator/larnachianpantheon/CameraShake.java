/**
 * The code of this mod element is always locked.
 *
 * You can register new events in this class too.
 *
 * If you want to make a plain independent class, create it using
 * Project Browser -> New... and make sure to make the class
 * outside net.mcreator.larnachianpantheon as this package is managed by MCreator.
 *
 * If you change workspace package, modid or prefix, you will need
 * to manually adapt this file to these changes or remake it.
 *
 * This class will be added in the mod root package.
*/
package net.mcreator.larnachianpantheon;

import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.phys.Vec3;

import net.mcreator.larnachianpantheon.configuration.LarnachsModConfigurationConfiguration;

import java.util.List;
import java.util.ArrayList;

@EventBusSubscriber(modid = LarnachianPantheonMod.MODID, value = Dist.CLIENT)
public class CameraShake {
	private static final List<CameraShakeSegment> SHAKES = new ArrayList<>();

	public static void addShake(CameraShakeSegment segment) {
		if (LarnachsModConfigurationConfiguration.CAMERASHAKE.get())
			SHAKES.add(segment);
	}

	@SubscribeEvent
	public static void onCameraSetup(ViewportEvent.ComputeCameraAngles event) {
		if (!LarnachsModConfigurationConfiguration.CAMERASHAKE.get() && SHAKES.isEmpty())
			return;
		Vec3 cameraPos = event.getCamera().position();
		float totalPitch = 0f;
		float totalYaw = 0f;
		float totalRoll = 0f;
		boolean needsCleanup = false;
		boolean canShake = false;
		for (int i = 0; i < SHAKES.size(); i++) {
			CameraShakeSegment shake = SHAKES.get(i);
			if (shake.isExpired()) {
				needsCleanup = true;
				continue;
			}
			float intensity = shake.getIntensity(cameraPos);
			if (intensity > 0) {
				canShake = true;
				long time = System.currentTimeMillis();
				totalPitch += (float) (Math.sin(time * 0.05) * intensity + (Math.random() - 0.5) * intensity * 0.5);
				totalYaw += (float) (Math.cos(time * 0.04) * intensity + (Math.random() - 0.5) * intensity * 0.5);
				totalRoll += (float) (Math.sin(time * 0.06) * intensity + (Math.random() - 0.5) * intensity * 0.5);
			}
		}
		if (needsCleanup) {
			SHAKES.removeIf(CameraShakeSegment::isExpired);
		}
		if (canShake) {
			event.setPitch(event.getPitch() + totalPitch);
			event.setYaw(event.getYaw() + totalYaw);
			event.setRoll(event.getRoll() + totalRoll);
		}
	}
}