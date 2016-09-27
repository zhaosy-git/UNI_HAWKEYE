package com.uni.hawkeye.crawler.sina_weibo_api.logic;

import com.uni.hawkeye.crawler.sina_weibo_api.bean.TaskControl;
import com.uni.hawkeye.crawler.sina_weibo_api.bean.CategoryInfo;

public interface PersistenceLogic {

	TaskControl getLastestTask(String site_code);

	TaskControl updateTaskAsUrlCrawling(TaskControl tc);

	void insertTaskControl(TaskControl tc);

	void insertCategoryInfo(CategoryInfo categoryInfo);


}
