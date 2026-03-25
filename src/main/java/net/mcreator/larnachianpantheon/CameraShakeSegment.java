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

import net.minecraft.world.phys.Vec3;

public class CameraShakeSegment {
	private final Vec3 origin;
	private final long expirationTime;
	private final int maxLifeTime;
	private final float baseIntensity;
	private final float maxDistance;

	public CameraShakeSegment(Vec3 origin, int lifeTimeMs, float intensity, float maxDistance) {
		this.origin = origin;
		this.maxLifeTime = lifeTimeMs;
		this.expirationTime = System.currentTimeMillis() + lifeTimeMs;
		this.baseIntensity = intensity;
		this.maxDistance = maxDistance;
	}

	public boolean isExpired() {
		return System.currentTimeMillis() > this.expirationTime;
	}

	public float getIntensity(Vec3 cameraPos) {
		if (isExpired())
			return 0f;
		float timeRemaining = Math.max(0, this.expirationTime - System.currentTimeMillis());
		float timeMultiplier = timeRemaining / (float) this.maxLifeTime;
		timeMultiplier = timeMultiplier * timeMultiplier;
		double distance = Math.max(0.5, this.origin.distanceTo(cameraPos));
		if (distance > this.maxDistance)
			return 0f;
		float distanceMultiplier = (float) (1.0 - (distance / this.maxDistance));
		distanceMultiplier = distanceMultiplier * distanceMultiplier;
		return this.baseIntensity * timeMultiplier * distanceMultiplier;
	}
}