package com.uni.hawkeye.crawler.sina_weibo_html.dao;

import org.apache.ibatis.annotations.Param;

import com.uni.hawkeye.crawler.sina_weibo_html.bean.SearchInfo;

public interface SearchMapper_SINA_WEIBO {

	void insertSearchInfo(SearchInfo searchInfo);

	SearchInfo getSearchinfoByByExecuteID(@Param(value = "execute_id") Integer execute_id);

}
