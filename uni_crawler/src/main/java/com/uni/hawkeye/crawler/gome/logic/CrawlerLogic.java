package com.uni.hawkeye.crawler.gome.logic;

import com.uni.hawkeye.crawler.gome.bean.TaskControl;
import com.uni.hawkeye.crawler.jd.bean.FromWebParam;

import uni_hawkeye.core.EBTaskInfo;

public interface CrawlerLogic {

	void crawlCategoryList(TaskControl tc);

	void crawlerProductList(TaskControl tc);

	void crawlerProduct(TaskControl tc, EBTaskInfo ebTaskInfo);

	void crawlCategoryList(TaskControl tc, EBTaskInfo ebTaskInfo);

}
