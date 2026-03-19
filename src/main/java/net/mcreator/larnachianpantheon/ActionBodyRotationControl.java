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

import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.Mob;
import net.minecraft.util.Mth;

import com.crimsonsteve.crimsonsteveapi.interfaces.IActionStateMob;

public class ActionBodyRotationControl<T extends Mob & IActionStateMob & IForceBodyRotation> extends BodyRotationControl {
	private final T actionMob;

	public ActionBodyRotationControl(T mob) {
		super(mob);
		actionMob = mob;
	}

	@Override
	public void clientTick() {
		if (this.actionMob.isInAction()) {
			int rotationSpeed = actionMob.getRotationSpeed();
			float targetRotation = this.actionMob.getSupposedRotation();
			float bodyRot = Mth.clamp(//
					Mth.wrapDegrees(targetRotation - actionMob.yBodyRot), -rotationSpeed, rotationSpeed//
			);
			float headRot = Mth.clamp(//
					Mth.wrapDegrees(targetRotation - actionMob.yHeadRot), -rotationSpeed, rotationSpeed//
			);
			this.actionMob.yBodyRot = actionMob.yBodyRot + bodyRot;
			this.actionMob.yHeadRot = actionMob.yHeadRot + headRot;
		} else {
			super.clientTick();
		}
	}
}