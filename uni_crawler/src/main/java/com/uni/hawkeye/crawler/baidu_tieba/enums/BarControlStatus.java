package com.uni.hawkeye.crawler.baidu_tieba.enums;

/**
 * 
 * @author zhao.siyuan
 *
 */
public enum BarControlStatus {

	UNDO("0"),
	CRAWLING("1"),
	CRAWLING_FINISHED("2");
	
    private final String value;
    
    private BarControlStatus(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
    
    public static BarControlStatus convertEnum(String value) {
        for (BarControlStatus d : values()) {
            if (d.value().equals(value)) {
                return d;
            }
        }
        return null;
    }

}
