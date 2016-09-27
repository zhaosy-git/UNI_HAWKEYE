package com.uni.hawkeye.crawler.suning.enums;

public enum PriceEnum {
	
	netPrice(1), promotionPrice(2);

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
