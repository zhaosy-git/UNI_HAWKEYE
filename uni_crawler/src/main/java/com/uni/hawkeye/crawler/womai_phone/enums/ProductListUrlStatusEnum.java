package com.uni.hawkeye.crawler.womai_phone.enums;

public enum ProductListUrlStatusEnum {

	UNDO(0), CRAWLING(1), CRAWLING_FINISHED(2), EMPTY(3), FAIL(4);

	private final int value;

	private ProductListUrlStatusEnum(int value) {
		this.value = value;
	}

	public int value() {
		return value;
	}

	public static ProductListUrlStatusEnum convertEnum(int value) {
		for (ProductListUrlStatusEnum d : values()) {
			if (d.value() == value) {
				return d;
			}
		}
		return null;
	}
}
