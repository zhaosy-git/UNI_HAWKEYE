package com.uni.hawkeye.crawler.womai_phone.logic;

import com.uni.hawkeye.crawler.womai_phone.bean.TaskControl;

public interface CrawlerLogic {

	void crawlCategoryList(TaskControl tc);

	void crawlerProductList(TaskControl tc);

	void crawlerProduct(TaskControl tc);

}
