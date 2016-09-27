package com.uni.hawkeye.crawler.custom.enums;

public enum TaskControlStatus {
	
	UNDO("0"),
	DOING("1"),
	DONE("2");
	
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
