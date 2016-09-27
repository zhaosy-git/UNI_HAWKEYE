package com.uni.hawkeye.crawler.tmall.enums;

public enum ProductDetailStatusEnum {
	
	PRODUCT_DETAIL_CRAWLING_SUCCESS(0), PRODUCT_DETAIL_CRAWLING_FAIL(1);

	private final int value;

	private ProductDetailStatusEnum(int value) {
		this.value = value;
	}

	public int value() {
		return value;
	}

	public static ProductDetailStatusEnum convertEnum(int value) {
		for (ProductDetailStatusEnum d : values()) {
			if (d.value == value) {
				return d;
			}
		}
		return null;
	}
}
