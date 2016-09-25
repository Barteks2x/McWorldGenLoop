package com.github.barteks2x.worldgenloop.util;

public class Bits {
	public static int bitmask(int n) {
		return (1 << n) - 1;
	}
	
	public static int bitmaskClamped(int n) {
		return bitmask(n < 0 ? 0 : n);
	}
}
