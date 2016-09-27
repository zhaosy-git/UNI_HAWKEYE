package com.uni.hawkeye.crawler.custom.logic;

import com.uni.hawkeye.crawler.custom.bean.TaskControl;

import uni_hawkeye.core.CustomTaskInfo;

public interface CrawlerLogic {

	void crawler(TaskControl tc, CustomTaskInfo customInfo);

}
