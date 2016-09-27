package com.uni.hawkeye.crawler.yhd.logic.impl;

import java.util.List;

import javax.annotation.Resource;

import com.uni.hawkeye.crawler.yhd.bean.CategoryInfo;
import com.uni.hawkeye.crawler.yhd.bean.ProductInfo;
import com.uni.hawkeye.crawler.yhd.bean.ProductList;
import com.uni.hawkeye.crawler.yhd.bean.ReviewBadInfo_YHD;
import com.uni.hawkeye.crawler.yhd.bean.ReviewImpression_YHD;
import com.uni.hawkeye.crawler.yhd.bean.ReviewInfo_YHD;
import com.uni.hawkeye.crawler.yhd.bean.TaskControl;
import com.uni.hawkeye.crawler.yhd.dao.CategoryMapper_YHD;
import com.uni.hawkeye.crawler.yhd.dao.ProductListMapper_YHD;
import com.uni.hawkeye.crawler.yhd.dao.TaskControlMapper_YHD;
import com.uni.hawkeye.crawler.yhd.logic.PersistenceLogic;

public class PersistenceLogicImpl implements PersistenceLogic {
	
	private TaskControlMapper_YHD tcMapper;
	private CategoryMapper_YHD catMapper;
	private ProductListMapper_YHD productMapper;

	@Resource
	public void setTcMapper(TaskControlMapper_YHD tcMapper) {
		this.tcMapper = tcMapper;
	}
	
	@Resource
	public void setCatMapper(CategoryMapper_YHD catMapper) {
		this.catMapper = catMapper;
	}

	@Resource
	public void setProductMapper(ProductListMapper_YHD productMapper) {
		this.productMapper = productMapper;
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
	public void insertCategoryInfo(CategoryInfo category_info) {
		catMapper.insertCategoryInfo(category_info);
	}

	@Override
	public List<CategoryInfo> getCategoryInfoByStatus(Integer execute_id, String status) {
		return catMapper.getCategoryInfoByStatus(execute_id, status);
	}

	@Override
	public void updateCategoryInfoStatus(int category_id, String status) {
		catMapper.updateCategoryInfoStatus(category_id, status);
	}

	@Override
	public void insertProductList(ProductList productList) {
		productMapper.insertProductList(productList);
	}

	@Override
	public List<ProductList> getProductListByStatus(Integer execute_id, String status) {
		return productMapper.getProductListByStatus(execute_id, status);
	}

	@Override
	public void undateProductList(ProductList productList) {
		productMapper.undateProductList(productList);
	}

	@Override
	public void insertProductInfo(ProductInfo productInfo) {
		productMapper.insertProductInfo(productInfo);
	}

	@Override
	public void insertReviewImpressionInfo(ReviewImpression_YHD reviewImpression_YHD) {
		productMapper.insertReviewImpressionInfo(reviewImpression_YHD);
	}

	@Override
	public void insertReviewInfo(ReviewInfo_YHD reviewInfo_YHD) {
		productMapper.insertReviewInfo(reviewInfo_YHD);
	}

	@Override
	public void insertReviewBadInfo(ReviewBadInfo_YHD reviewBadInfo_YHD) {
		productMapper.insertReviewBadInfo(reviewBadInfo_YHD);
	}

	@Override
	public int getProductCountByProductListID(int product_list_id) {
		return productMapper.getProductCountByProductListID(product_list_id);
	}
}
