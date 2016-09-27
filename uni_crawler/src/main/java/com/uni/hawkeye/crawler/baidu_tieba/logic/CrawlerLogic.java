package com.uni.hawkeye.crawler.baidu_tieba.logic;

import com.uni.hawkeye.crawler.baidu_tieba.bean.TaskControl;

import uni_hawkeye.core.TiebaTaskInfo;

public interface CrawlerLogic {

	void crawlCategoryList(TaskControl tc);

	void crawlerBarList(TaskControl tc, TiebaTaskInfo tiebaInfo);

	void crawlerTiezi(TaskControl tc, TiebaTaskInfo tiebaInfo);

	void crawlCategoryList(TaskControl tc, TiebaTaskInfo tiebaInfo);


}
