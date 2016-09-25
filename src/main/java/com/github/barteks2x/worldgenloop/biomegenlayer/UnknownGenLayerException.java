package com.github.barteks2x.worldgenloop.biomegenlayer;

import net.minecraft.world.gen.layer.GenLayer;

public class UnknownGenLayerException extends RuntimeException {

	public UnknownGenLayerException(GenLayer layer) {
		super("Unknown gen layer class: " + layer.getClass().getName());
	}

}
