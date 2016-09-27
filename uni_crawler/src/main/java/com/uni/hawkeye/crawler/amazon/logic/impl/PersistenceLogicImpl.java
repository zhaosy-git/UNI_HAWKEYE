package com.uni.hawkeye.crawler.amazon.logic.impl;

import java.util.List;

import javax.annotation.Resource;

import com.uni.hawkeye.crawler.amazon.bean.CategoryInfo;
import com.uni.hawkeye.crawler.amazon.bean.ProductInfo;
import com.uni.hawkeye.crawler.amazon.bean.ProductList;
import com.uni.hawkeye.crawler.amazon.bean.ReviewBadInfo_AMAZON;
import com.uni.hawkeye.crawler.amazon.bean.ReviewImpression_AMAZON;
import com.uni.hawkeye.crawler.amazon.bean.ReviewInfo_AMAZON;
import com.uni.hawkeye.crawler.amazon.bean.TaskControl;
import com.uni.hawkeye.crawler.amazon.dao.CategoryMapper_AMAZON;
import com.uni.hawkeye.crawler.amazon.dao.ProductListMapper_AMAZON;
import com.uni.hawkeye.crawler.amazon.dao.TaskControlMapper_AMAZON;
import com.uni.hawkeye.crawler.amazon.logic.PersistenceLogic;

public class PersistenceLogicImpl implements PersistenceLogic {
	
	private TaskControlMapper_AMAZON tcMapper;
	private CategoryMapper_AMAZON catMapper;
	private ProductListMapper_AMAZON productMapper;

	@Resource
	public void setTcMapper(TaskControlMapper_AMAZON tcMapper) {
		this.tcMapper = tcMapper;
	}
	
	@Resource
	public void setCatMapper(CategoryMapper_AMAZON catMapper) {
		this.catMapper = catMapper;
	}

	@Resource
	public void setProductMapper(ProductListMapper_AMAZON productMapper) {
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
	public void insertReviewImpressionInfo(ReviewImpression_AMAZON reviewImpression_AMAZON) {
		productMapper.insertReviewImpressionInfo(reviewImpression_AMAZON);
	}

	@Override
	public void insertReviewInfo(ReviewInfo_AMAZON reviewInfo_AMAZON) {
		productMapper.insertReviewInfo(reviewInfo_AMAZON);
	}

	@Override
	public void insertReviewBadInfo(ReviewBadInfo_AMAZON reviewBadInfo_AMAZON) {
		productMapper.insertReviewBadInfo(reviewBadInfo_AMAZON);
	}

	@Override
	public int getProductCountByProductListID(int product_list_id) {
		return productMapper.getProductCountByProductListID(product_list_id);
	}
}
