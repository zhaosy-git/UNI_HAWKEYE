package com.uni.hawkeye.crawler.tmall.enums;

public enum ProductInfoWayTypeEnum {
	
	EMPTY(-1), OTHER(0), 旗舰(1), 超市(2), 专营(3), 海外旗舰店(4), 海外专营店(5);

	private final int value;

	private ProductInfoWayTypeEnum(int value) {
		this.value = value;
	}

	public int value() {
		return value;
	}
	
	public static ProductInfoWayTypeEnum convertEnum(int value) {
		for (ProductInfoWayTypeEnum d : values()) {
			if (d.value == value) {
				return d;
			} 
		} 
		return null;
	}
}
