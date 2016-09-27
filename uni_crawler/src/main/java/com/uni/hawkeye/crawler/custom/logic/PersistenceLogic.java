package com.uni.hawkeye.crawler.custom.logic;

import com.uni.hawkeye.crawler.custom.bean.TaskControl;

public interface PersistenceLogic {

	TaskControl getLastestTask(String site_code, int i);

	void insertTaskControl(TaskControl tc);

	TaskControl updateTaskAsUrlCrawling(TaskControl tc);

}
