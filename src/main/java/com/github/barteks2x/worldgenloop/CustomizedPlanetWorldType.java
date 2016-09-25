package com.github.barteks2x.worldgenloop;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiCreateFlatWorld;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiCustomizeWorldScreen;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkProvider;

public class CustomizedPlanetWorldType extends WorldType {

	public CustomizedPlanetWorldType() {
		super("custom_planet");
	}

	@Override
	public void onCustomizeButton(Minecraft mc, GuiCreateWorld guiCreateWorld) {
		mc.displayGuiScreen(new CustomizedPlanetTypeGUI(guiCreateWorld));
	}

	@Override
	public IChunkProvider getChunkGenerator(World world, String generatorOptions) {
		return new TileableChunkProvider(world, world.getSeed(), world.getWorldInfo()
				.isMapFeaturesEnabled(), generatorOptions);
	}
	
	public boolean isCustomizable()
    {
        return true;
    }

}
