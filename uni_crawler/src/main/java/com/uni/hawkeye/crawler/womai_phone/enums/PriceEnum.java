package com.uni.hawkeye.crawler.womai_phone.enums;

public enum PriceEnum {
	
	抢购(1), 市场(2), 零售(3), VIP(4), 积分(5), 节省了(6);

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
