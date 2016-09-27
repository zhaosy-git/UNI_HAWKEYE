package com.uni.hawkeye.crawler.sina_weibo_api.logic;

import com.uni.hawkeye.crawler.sina_weibo_api.bean.TaskControl;

public interface CrawlerLogic {

	void crawlCategoryList(TaskControl tc);

	void crawlerBlockList(TaskControl tc);

}
