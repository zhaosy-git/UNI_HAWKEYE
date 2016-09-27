package com.uni.hawkeye.crawler.tmall.enums;

public enum PriceEnum {
	
	PRICE(1), TAG_PRICE(2), PROMOTION_PRICE(3), SUGGESTIVE_PROMOTION_PRICE(4);

	private final int value;

	private PriceEnum(int value) {
		this.value = value;
	}

	public int value() {
		return value;
	}

	public static PriceEnum convertEnum(int value) {
		for (PriceEnum d : values()) {
			if (d.value == value) {
				return d;
			}
		}
		return null;
	}
}
