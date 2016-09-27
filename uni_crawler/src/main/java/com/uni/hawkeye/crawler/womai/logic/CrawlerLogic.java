package com.uni.hawkeye.crawler.womai.logic;

import com.uni.hawkeye.crawler.jd.bean.FromWebParam;
import com.uni.hawkeye.crawler.womai.bean.TaskControl;

import uni_hawkeye.core.EBTaskInfo;

public interface CrawlerLogic {

	void crawlCategoryList(TaskControl tc);

	void crawlerProductList(TaskControl tc);

	void crawlerProduct(TaskControl tc, EBTaskInfo ebTaskInfo);

	void crawlCategoryList(TaskControl tc, EBTaskInfo ebTaskInfo);

}
