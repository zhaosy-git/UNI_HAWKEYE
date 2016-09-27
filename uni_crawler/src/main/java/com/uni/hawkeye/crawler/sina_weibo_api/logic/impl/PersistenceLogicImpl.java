package com.uni.hawkeye.crawler.sina_weibo_api.logic.impl;

import javax.annotation.Resource;

import com.uni.hawkeye.crawler.sina_weibo_api.bean.CategoryInfo;
import com.uni.hawkeye.crawler.sina_weibo_api.bean.TaskControl;
import com.uni.hawkeye.crawler.sina_weibo_api.dao.CategoryMapper_SINA_WEIBO_API;
import com.uni.hawkeye.crawler.sina_weibo_api.dao.TaskControlMapper_SINA_WEIBO_API;
import com.uni.hawkeye.crawler.sina_weibo_api.logic.PersistenceLogic;

public class PersistenceLogicImpl implements PersistenceLogic {

	private TaskControlMapper_SINA_WEIBO_API tcMapper;
	private CategoryMapper_SINA_WEIBO_API catMapper;
	
	/**
	 * @param tcMapper the tcMapper to set
	 */
	@Resource
	public void setTcMapper(TaskControlMapper_SINA_WEIBO_API tcMapper) {
		this.tcMapper = tcMapper;
	}

	/**
	 * @param catMapper the catMapper to set
	 */
	@Resource
	public void setCatMapper(CategoryMapper_SINA_WEIBO_API catMapper) {
		this.catMapper = catMapper;
	}

	@Override
	public TaskControl getLastestTask(String site_code) {
		return tcMapper.selectMaxTaskControl(site_code);
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
	public void insertCategoryInfo(CategoryInfo categoryInfo) {
		catMapper.insertCategoryInfo(categoryInfo);
	}
	
}
