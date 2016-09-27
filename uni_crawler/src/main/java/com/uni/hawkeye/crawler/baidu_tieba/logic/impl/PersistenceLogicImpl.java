package com.uni.hawkeye.crawler.baidu_tieba.logic.impl;

import java.util.List;

import javax.annotation.Resource;

import com.uni.hawkeye.crawler.baidu_tieba.bean.BarInfo;
import com.uni.hawkeye.crawler.baidu_tieba.bean.CategoryInfo;
import com.uni.hawkeye.crawler.baidu_tieba.bean.TaskControl;
import com.uni.hawkeye.crawler.baidu_tieba.bean.TieziInfo;
import com.uni.hawkeye.crawler.baidu_tieba.bean.UserInfo;
import com.uni.hawkeye.crawler.baidu_tieba.dao.BarMapper_BAIDU_TIEBA;
import com.uni.hawkeye.crawler.baidu_tieba.dao.CategoryMapper_BAIDU_TIEBA;
import com.uni.hawkeye.crawler.baidu_tieba.dao.TaskControlMapper_BAIDU_TIEBA;
import com.uni.hawkeye.crawler.baidu_tieba.dao.TieziMapper_BAIDU_TIEBA;
import com.uni.hawkeye.crawler.baidu_tieba.dao.UserMapper_BAIDU_TIEBA;
import com.uni.hawkeye.crawler.baidu_tieba.logic.PersistenceLogic;


public class PersistenceLogicImpl implements PersistenceLogic {

	private TaskControlMapper_BAIDU_TIEBA tcMapper;
	private CategoryMapper_BAIDU_TIEBA catMapper;
	private BarMapper_BAIDU_TIEBA barMapper;
	private TieziMapper_BAIDU_TIEBA tieziMapper;
	private UserMapper_BAIDU_TIEBA userMapper;
	
	/**
	 * @param tcMapper the tcMapper to set
	 */
	@Resource
	public void setTcMapper(TaskControlMapper_BAIDU_TIEBA tcMapper) {
		this.tcMapper = tcMapper;
	}

	/**
	 * @param catMapper the catMapper to set
	 */
	@Resource
	public void setCatMapper(CategoryMapper_BAIDU_TIEBA catMapper) {
		this.catMapper = catMapper;
	}

	/**
	 * @param barMapper the barMapper to set
	 */
	@Resource
	public void setBarMapper(BarMapper_BAIDU_TIEBA barMapper) {
		this.barMapper = barMapper;
	}

	/**
	 * @param tieziMapper the tieziMapper to set
	 */
	@Resource
	public void setTieziMapper(TieziMapper_BAIDU_TIEBA tieziMapper) {
		this.tieziMapper = tieziMapper;
	}

	/**
	 * @param userMapper the userMapper to set
	 */
	@Resource
	public void setUserMapper(UserMapper_BAIDU_TIEBA userMapper) {
		this.userMapper = userMapper;
	}

	@Override
	public TaskControl getLastestTask(String site_code, int task_id) {
		return tcMapper.selectMaxTaskControl(site_code, task_id);
	}

	@Override
	public void insertTaskControl(TaskControl tc) {
		tcMapper.insertTaskControl(tc);
	}

	@Override
	public TaskControl updateTaskAsUrlCrawling(TaskControl tc) {
		tcMapper.updateByPrimaryKeySelective(tc);
		return tc;
	}

	@Override
	public CategoryInfo insertCategoryInfo(CategoryInfo cInfo) {
		catMapper.insertCategoryInfo(cInfo);
		return cInfo;
	}

	@Override
	public List<CategoryInfo> getCategoryInfoByStatus(Integer execute_id_fk, String status, String target_category_name) {
		return catMapper.getCategoryInfoByStatus(execute_id_fk, status, target_category_name);
	}

	@Override
	public void insertBarInfo(BarInfo barInfo) {
		barMapper.insertBarInfo(barInfo);
	}

	@Override
	public void updateCategoryInfoStatus(CategoryInfo cInfo) {
		catMapper.updateCategoryInfoStatus(cInfo.getCategory_id(), cInfo.getStatus());
	}

	@Override
	public List<BarInfo> getBarInfoByStatus(Integer execute_id_fk, String status, String target_bar_name) {
		return barMapper.getBarInfoByStatus(execute_id_fk, status, target_bar_name);
	}

	@Override
	public void insertTieziInfo(TieziInfo tieziInfo) {
		tieziMapper.insertTieziInfo(tieziInfo);
	}

	@Override
	public void insertUserInfo(UserInfo userInfo) {
		userMapper.insertUserInfo(userInfo);
	}

	@Override
	public void updateBarInfoStatus(BarInfo bInfo) {
		barMapper.updateBarInfoStatus(bInfo.getTieba_id(), bInfo.getStatus());
	}

}
