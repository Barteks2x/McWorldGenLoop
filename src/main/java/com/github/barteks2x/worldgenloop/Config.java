package com.github.barteks2x.worldgenloop;

import java.util.HashMap;
import java.util.Map;

public class Config {
	private static final Map<Integer, Integer> config = new HashMap<Integer, Integer>(3);

	public static int loopBits(int dimensionID) {
		Integer i = config.get(dimensionID);
		return i == null ? defaultLoopBits() : i;
	}

	public static void setSizeBits(int dimensionID, int size) {
		config.put(dimensionID, size);
	}

	public static int defaultLoopBits() {
		return 13;
	}
}
