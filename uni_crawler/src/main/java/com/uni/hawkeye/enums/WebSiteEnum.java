package com.uni.hawkeye.enums;

public enum WebSiteEnum {

	tmall("1"), 
	jd("2"), 
	yhd("3"), 
	womai("4"), 
	gome("5"), 
	suning("6"), 
	amazon("7"), 
	womai_phone("8"), 
	baidu_tieba("9"), 
	sina_weibo_html("10"), 
	sina_weibo_api("11"), 
	custom("12");

	private final String value;

	private WebSiteEnum(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public static WebSiteEnum convertEnum(String value) {
		for (WebSiteEnum d : values()) {
			if (d.value().equals(value)) {
				return d;
			}
		}
		return null;
	}
}
