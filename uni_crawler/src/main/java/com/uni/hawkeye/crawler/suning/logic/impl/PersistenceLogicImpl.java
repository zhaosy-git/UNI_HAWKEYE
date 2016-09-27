package com.uni.hawkeye.crawler.suning.logic.impl;

import java.util.List;

import javax.annotation.Resource;

import com.uni.hawkeye.crawler.suning.bean.CategoryInfo;
import com.uni.hawkeye.crawler.suning.bean.PriceInfo_SUNING;
import com.uni.hawkeye.crawler.suning.bean.ProductInfo;
import com.uni.hawkeye.crawler.suning.bean.ProductList;
import com.uni.hawkeye.crawler.suning.bean.ReviewBadInfo_SUNING;
import com.uni.hawkeye.crawler.suning.bean.ReviewImpression_SUNING;
import com.uni.hawkeye.crawler.suning.bean.ReviewInfo_SUNING;
import com.uni.hawkeye.crawler.suning.bean.TaskControl;
import com.uni.hawkeye.crawler.suning.dao.CategoryMapper_SUNING;
import com.uni.hawkeye.crawler.suning.dao.ProductListMapper_SUNING;
import com.uni.hawkeye.crawler.suning.dao.TaskControlMapper_SUNING;
import com.uni.hawkeye.crawler.suning.logic.PersistenceLogic;

public class PersistenceLogicImpl implements PersistenceLogic {
	
	private TaskControlMapper_SUNING tcMapper;
	private CategoryMapper_SUNING catMapper;
	private ProductListMapper_SUNING productMapper;

	@Resource
	public void setTcMapper(TaskControlMapper_SUNING tcMapper) {
		this.tcMapper = tcMapper;
	}
	
	@Resource
	public void setCatMapper(CategoryMapper_SUNING catMapper) {
		this.catMapper = catMapper;
	}

	@Resource
	public void setProductMapper(ProductListMapper_SUNING productMapper) {
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
	public void insertPriceInfo(PriceInfo_SUNING priceInfo_SUNING) {
		productMapper.insertPriceInfo(priceInfo_SUNING);
	}

	@Override
	public void insertReviewImpressionInfo(ReviewImpression_SUNING reviewImpression_SUNING) {
		productMapper.insertReviewImpressionInfo(reviewImpression_SUNING);
	}

	@Override
	public void insertReviewInfo(ReviewInfo_SUNING reviewInfo_SUNING) {
		productMapper.insertReviewInfo(reviewInfo_SUNING);
	}

	@Override
	public void insertReviewBadInfo(ReviewBadInfo_SUNING reviewBadInfo_SUNING) {
		productMapper.insertReviewBadInfo(reviewBadInfo_SUNING);
	}

	@Override
	public int getProductCountByProductListID(int product_list_id) {
		return productMapper.getProductCountByProductListID(product_list_id);
	}
}
