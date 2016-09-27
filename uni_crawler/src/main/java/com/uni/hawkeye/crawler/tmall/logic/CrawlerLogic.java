package com.uni.hawkeye.crawler.tmall.logic;

import java.util.Date;

import com.uni.hawkeye.crawler.jd.bean.FromWebParam;
import com.uni.hawkeye.crawler.tmall.bean.TaskControl;

import uni_hawkeye.core.EBTaskInfo;

public interface CrawlerLogic {

	void crawlCategoryList(TaskControl tc);

	void crawlerProductList(TaskControl tc);

	void crawlerProduct(TaskControl tc, Date startTime, EBTaskInfo ebTaskInfo);

	void crawlCategoryList(TaskControl tc, EBTaskInfo ebTaskInfo);

}
