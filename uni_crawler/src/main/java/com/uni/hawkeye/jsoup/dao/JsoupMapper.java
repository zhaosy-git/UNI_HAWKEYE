package com.uni.hawkeye.jsoup.dao;

import org.apache.ibatis.annotations.Param;

import com.uni.hawkeye.jsoup.bean.CrawlerPropertyInfo;

public interface JsoupMapper {

	CrawlerPropertyInfo getJsoupProperty(@Param(value = "cookie_from") String cookie_from);

}
