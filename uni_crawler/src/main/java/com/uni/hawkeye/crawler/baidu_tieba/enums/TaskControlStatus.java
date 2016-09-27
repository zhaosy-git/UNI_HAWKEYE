package com.uni.hawkeye.crawler.baidu_tieba.enums;

public enum TaskControlStatus {
	
	UNDO("0"),
	CATEGORY_CRAWLING("1"),
	CATEGORY_CRAWLING_FINISH("2"),
	BAR_CRAWLING("3"),
	BAR_CRAWLING_FINISH("4"),
	TIEZI_CRAWLING("5"),
	TIEZI_CRAWLING_FINISH("6");
	
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
