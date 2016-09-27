package com.uni.hawkeye.crawler.baidu_tieba.logic;

import java.util.List;

import com.uni.hawkeye.crawler.baidu_tieba.bean.BarInfo;
import com.uni.hawkeye.crawler.baidu_tieba.bean.CategoryInfo;
import com.uni.hawkeye.crawler.baidu_tieba.bean.TaskControl;
import com.uni.hawkeye.crawler.baidu_tieba.bean.TieziInfo;
import com.uni.hawkeye.crawler.baidu_tieba.bean.UserInfo;

public interface PersistenceLogic {

	TaskControl getLastestTask(String site_code, int task_id);

	void insertTaskControl(TaskControl tc);

	TaskControl updateTaskAsUrlCrawling(TaskControl tc);

	CategoryInfo insertCategoryInfo(CategoryInfo cInfo);

	List<CategoryInfo> getCategoryInfoByStatus(Integer execute_id_fk, String status, String target_category_name);

	void insertBarInfo(BarInfo barInfo);

	void updateCategoryInfoStatus(CategoryInfo cInfo);

	List<BarInfo> getBarInfoByStatus(Integer execute_id_fk, String status, String target_bar_name);

	void insertUserInfo(UserInfo userInfo);

	void insertTieziInfo(TieziInfo tieziInfo);

	void updateBarInfoStatus(BarInfo bInfo);

}
