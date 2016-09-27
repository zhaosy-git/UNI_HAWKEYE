package com.uni.hawkeye.crawler.sina_weibo_html.logic.impl;

import java.util.List;

import javax.annotation.Resource;

import com.uni.hawkeye.crawler.sina_weibo_html.bean.BlockInfo;
import com.uni.hawkeye.crawler.sina_weibo_html.bean.CategoryInfo;
import com.uni.hawkeye.crawler.sina_weibo_html.bean.CommentInfo;
import com.uni.hawkeye.crawler.sina_weibo_html.bean.SearchInfo;
import com.uni.hawkeye.crawler.sina_weibo_html.bean.TaskControl;
import com.uni.hawkeye.crawler.sina_weibo_html.bean.UserAttentionInfo;
import com.uni.hawkeye.crawler.sina_weibo_html.bean.UserInfo;
import com.uni.hawkeye.crawler.sina_weibo_html.dao.BlockMapper_SINA_WEIBO;
import com.uni.hawkeye.crawler.sina_weibo_html.dao.CategoryMapper_SINA_WEIBO;
import com.uni.hawkeye.crawler.sina_weibo_html.dao.CommentMapper_SINA_WEIBO;
import com.uni.hawkeye.crawler.sina_weibo_html.dao.SearchMapper_SINA_WEIBO;
import com.uni.hawkeye.crawler.sina_weibo_html.dao.TaskControlMapper_SINA_WEIBO;
import com.uni.hawkeye.crawler.sina_weibo_html.dao.UserAttentionMapper_SINA_WEIBO;
import com.uni.hawkeye.crawler.sina_weibo_html.dao.UserMapper_SINA_WEIBO;
import com.uni.hawkeye.crawler.sina_weibo_html.logic.PersistenceLogic;


public class PersistenceLogicImpl implements PersistenceLogic {

	private TaskControlMapper_SINA_WEIBO tcMapper;
	private CategoryMapper_SINA_WEIBO catMapper;
	private BlockMapper_SINA_WEIBO blockMapper;
	private UserMapper_SINA_WEIBO userMapper;
	private UserAttentionMapper_SINA_WEIBO userAttentionMapper;
	private CommentMapper_SINA_WEIBO commentMapper;
	private SearchMapper_SINA_WEIBO searchMapper;
	
	/**
	 * @param tcMapper the tcMapper to set
	 */
	@Resource
	public void setTcMapper(TaskControlMapper_SINA_WEIBO tcMapper) {
		this.tcMapper = tcMapper;
	}
	
	/**
	 * @param catMapper the catMapper to set
	 */
	@Resource
	public void setCatMapper(CategoryMapper_SINA_WEIBO catMapper) {
		this.catMapper = catMapper;
	}
	
	/**
	 * @param blockMapper the blockMapper to set
	 */
	@Resource
	public void setBlockMapper(BlockMapper_SINA_WEIBO blockMapper) {
		this.blockMapper = blockMapper;
	}

	/**
	 * @param userMapper the userMapper to set
	 */
	@Resource
	public void setUserMapper(UserMapper_SINA_WEIBO userMapper) {
		this.userMapper = userMapper;
	}

	/**
	 * @param userAttentionMapper the userAttentionMapper to set
	 */
	@Resource
	public void setUserAttentionMapper(UserAttentionMapper_SINA_WEIBO userAttentionMapper) {
		this.userAttentionMapper = userAttentionMapper;
	}

	/**
	 * @param commentMapper the commentMapper to set
	 */
	@Resource
	public void setCommentMapper(CommentMapper_SINA_WEIBO commentMapper) {
		this.commentMapper = commentMapper;
	}

	/**
	 * @param searchMapper the searchMapper to set
	 */
	@Resource
	public void setSearchMapper(SearchMapper_SINA_WEIBO searchMapper) {
		this.searchMapper = searchMapper;
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
	public void insertCategoryInfo(CategoryInfo categoryInfo) {
		catMapper.insertCategoryInfo(categoryInfo);
	}

	@Override
	public List<CategoryInfo> getCategoryInfoByStatus(Integer execute_id_fk, String status, String target_category_name) {
		return catMapper.getCategoryInfoByStatus(execute_id_fk, status, target_category_name);
	}

	@Override
	public void insertBlockInfo(BlockInfo blockInfo) {
		blockMapper.insertBlockInfo(blockInfo);
	}

	@Override
	public int insertUserInfo(UserInfo userInfo) {
		int result = userMapper.insertUserInfo(userInfo);
		return result;
	}

	@Override
	public void insertUserAttentionInfo(UserAttentionInfo userAttentionInfo) {
		userAttentionMapper.insertUserAttentionInfo(userAttentionInfo);
	}

	@Override
	public void insertCommentInfo(CommentInfo commentInfo) {
		commentMapper.insertCommentInfo(commentInfo);
	}

	@Override
	public void updateCategoryInfo(int category_id, String status) {
		catMapper.updateCategoryInfoStatus(category_id, status);
	}

	@Override
	public void insertSearchInfo(SearchInfo searchInfo) {
		searchMapper.insertSearchInfo(searchInfo);
	}

	@Override
	public SearchInfo getSearchinfoByByExecuteID(Integer execute_id) {
		return searchMapper.getSearchinfoByByExecuteID(execute_id);
	}
}
