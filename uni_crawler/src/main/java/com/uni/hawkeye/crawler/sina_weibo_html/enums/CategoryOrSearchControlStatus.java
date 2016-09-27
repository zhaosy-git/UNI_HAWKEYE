package com.uni.hawkeye.crawler.sina_weibo_html.enums;

/**
 * 
 * @author zhao.siyuan
 *
 */
public enum CategoryOrSearchControlStatus {

	UNDO("0"),
	CRAWLING("1"),
	CRAWLING_FINISHED("2");
	
    private final String value;
    
    private CategoryOrSearchControlStatus(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
    
    public static CategoryOrSearchControlStatus convertEnum(String value) {
        for (CategoryOrSearchControlStatus d : values()) {
            if (d.value().equals(value)) {
                return d;
            }
        }
        return null;
    }

}
