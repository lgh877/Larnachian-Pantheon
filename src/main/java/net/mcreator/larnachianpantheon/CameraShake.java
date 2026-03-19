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

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.phys.Vec3;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CameraShake {
	private static final List<CameraShakeSegment> SHAKES = new CopyOnWriteArrayList<>();

	public static void addShake(CameraShakeSegment segment) {
		SHAKES.add(segment);
	}

	@SubscribeEvent
	public static void onCameraSetup(ViewportEvent.ComputeCameraAngles event) {
		if (SHAKES.isEmpty())
			return;
		Vec3 cameraPos = event.getCamera().getPosition();
		float totalPitch = 0f;
		float totalYaw = 0f;
		float totalRoll = 0f;
		boolean needsCleanup = false;
		boolean canShake = false;
		for (CameraShakeSegment shake : SHAKES) {
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