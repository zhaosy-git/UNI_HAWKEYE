package com.uni.hawkeye.crawler.sina_weibo_api.enums;

public enum TaskControlStatus {
	
	UNDO("0"),
	CATEGORY_CRAWLING("1"),
	CATEGORY_CRAWLING_FINISH("2"),
	BLOCK_CRAWLING("3"),
	BLOCK_CRAWLING_FINISH("4"),
	USER_ATTENTION_CRAWLING("5"),
	USER_ATTENTION_CRAWLING_FINISH("6");
	
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
