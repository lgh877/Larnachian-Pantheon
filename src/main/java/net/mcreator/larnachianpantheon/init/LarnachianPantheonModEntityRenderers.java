/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.larnachianpantheon.init;

import net.mcreator.larnachianpantheon.LarnachianPantheonMod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.mcreator.larnachianpantheon.client.renderer.LarnachsRenderer;

@EventBusSubscriber(modid = LarnachianPantheonMod.MODID, value = Dist.CLIENT)
public class LarnachianPantheonModEntityRenderers {
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(LarnachianPantheonModEntities.LARNACHS.get(), LarnachsRenderer::new);
	}
}