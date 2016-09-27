package com.uni.hawkeye.crawler.amazon.enums;

/**
 * 
 * @author zhao.siyuan
 *
 */
public enum CategoryControlStatus {

	UNDO("0"),
	CRAWLING("1"),
	CRAWLING_FINISHED("2");
	
    private final String value;
    
    private CategoryControlStatus(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
    
    public static CategoryControlStatus convertEnum(String value) {
        for (CategoryControlStatus d : values()) {
            if (d.value().equals(value)) {
                return d;
            }
        }
        return null;
    }

}
