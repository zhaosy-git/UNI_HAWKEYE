package com.uni.hawkeye.crawler.sina_weibo_html.logic;

import java.util.List;

import com.uni.hawkeye.crawler.sina_weibo_html.bean.BlockInfo;
import com.uni.hawkeye.crawler.sina_weibo_html.bean.CategoryInfo;
import com.uni.hawkeye.crawler.sina_weibo_html.bean.CommentInfo;
import com.uni.hawkeye.crawler.sina_weibo_html.bean.SearchInfo;
import com.uni.hawkeye.crawler.sina_weibo_html.bean.TaskControl;
import com.uni.hawkeye.crawler.sina_weibo_html.bean.UserAttentionInfo;
import com.uni.hawkeye.crawler.sina_weibo_html.bean.UserInfo;

public interface PersistenceLogic {

	TaskControl getLastestTask(String site_code, int task_id);

	void insertTaskControl(TaskControl tc);

	TaskControl updateTaskAsUrlCrawling(TaskControl tc);

	void insertCategoryInfo(CategoryInfo categoryInfo);

	List<CategoryInfo> getCategoryInfoByStatus(Integer execute_id_fk, String status, String target_category_name);

	void insertBlockInfo(BlockInfo blockInfo);

	int insertUserInfo(UserInfo userInfo);

	void insertUserAttentionInfo(UserAttentionInfo userAttentionInfo);

	void insertCommentInfo(CommentInfo commentInfo);

	void updateCategoryInfo(int category_id, String status);

	void insertSearchInfo(SearchInfo searchInfo);

	SearchInfo getSearchinfoByByExecuteID(Integer execute_id);

}
