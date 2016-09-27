package com.uni.hawkeye.crawler.gome.enums;

public enum PriceEnum {
	
	buyPrice(1), marketPrice(2), WMPrice(3), VIPPrice(4), userPoints(5), saveMoney(6), specialPrice(7);

	private final int value;

	private PriceEnum(int value) {
		this.value = value;
	}

	public int value() {
		return value;
	}

	public static PriceEnum convertEnum(int value) {
		for (PriceEnum d : values()) {
			if (d.value() == value) {
				return d;
			}
		}
		return null;
	}
}
