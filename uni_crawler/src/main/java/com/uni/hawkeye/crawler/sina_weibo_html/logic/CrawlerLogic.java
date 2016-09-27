package com.uni.hawkeye.crawler.sina_weibo_html.logic;

import com.uni.hawkeye.crawler.sina_weibo_html.bean.TaskControl;

import uni_hawkeye.core.WeiboTaskInfo;

public interface CrawlerLogic {

	void crawlCategoryList(TaskControl tc);

	void crawlerBlockList(TaskControl tc, WeiboTaskInfo weiboInfo);

	void crawlCategoryList(TaskControl tc, WeiboTaskInfo weiboInfo);

	void crawlSearchList(TaskControl tc, WeiboTaskInfo weiboInfo);

	void crawlerSearchBlockList(TaskControl tc, WeiboTaskInfo weiboInfo);

}
