package com.uni.hawkeye.crawler.jd.enums;

public enum TaskControlStatus {
	
	UNDO("0"),
	CATEGORY_CRAWLING("1"),
	CATEGORY_CRAWLING_FINISH("2"),
	PRODUCT_URL_GENERATOR("3"),
	PRODUCT_URL_GENERATOR_FINISH("4"),
	PRODUCT_CRAWLING("5"),
	PRODUCT_CRAWLING_FINISH("6");
	
    private final String value;
    
    private TaskControlStatus(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
    
    public static TaskControlStatus convertEnum(String value) {
        for (TaskControlStatus d : values()) {
            if (d.value().equals(value)) {
                return d;
            }
        }
        return null;
    }
}
