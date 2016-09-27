package com.uni.hawkeye.crawler.jd.logic;

import java.util.Date;

import com.uni.hawkeye.crawler.jd.bean.FromWebParam;
import com.uni.hawkeye.crawler.jd.bean.TaskControl;

import uni_hawkeye.core.EBTaskInfo;

public interface CrawlerLogic {

	void crawlCategoryList(TaskControl tc);

	void crawlerProductList(TaskControl tc);

	@Deprecated
	void crawlerProduct(TaskControl tc, Date startTime);

	void crawlCategoryList(TaskControl tc, EBTaskInfo ebTaskInfo);

	void crawlerProduct(TaskControl tc, Date startTime, EBTaskInfo ebTaskInfo);

}
