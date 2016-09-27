package com.uni.hawkeye.crawler.womai.logic.impl;

import java.util.List;

import javax.annotation.Resource;

import com.uni.hawkeye.crawler.womai.bean.CategoryInfo;
import com.uni.hawkeye.crawler.womai.bean.PriceInfo_WOMAI;
import com.uni.hawkeye.crawler.womai.bean.ProductInfo;
import com.uni.hawkeye.crawler.womai.bean.ProductList;
import com.uni.hawkeye.crawler.womai.bean.ReviewBadInfo_WOMAI;
import com.uni.hawkeye.crawler.womai.bean.ReviewImpression_WOMAI;
import com.uni.hawkeye.crawler.womai.bean.ReviewInfo_WOMAI;
import com.uni.hawkeye.crawler.womai.bean.TaskControl;
import com.uni.hawkeye.crawler.womai.dao.CategoryMapper_WOMAI;
import com.uni.hawkeye.crawler.womai.dao.ProductListMapper_WOMAI;
import com.uni.hawkeye.crawler.womai.dao.TaskControlMapper_WOMAI;
import com.uni.hawkeye.crawler.womai.logic.PersistenceLogic;

public class PersistenceLogicImpl implements PersistenceLogic {
	
	private TaskControlMapper_WOMAI tcMapper;
	private CategoryMapper_WOMAI catMapper;
	private ProductListMapper_WOMAI productMapper;

	@Resource
	public void setTcMapper(TaskControlMapper_WOMAI tcMapper) {
		this.tcMapper = tcMapper;
	}
	
	@Resource
	public void setCatMapper(CategoryMapper_WOMAI catMapper) {
		this.catMapper = catMapper;
	}

	@Resource
	public void setProductMapper(ProductListMapper_WOMAI productMapper) {
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
	public void insertPriceInfo(PriceInfo_WOMAI priceInfo_WOMAI) {
		productMapper.insertPriceInfo(priceInfo_WOMAI);
	}

	@Override
	public void insertReviewImpressionInfo(ReviewImpression_WOMAI reviewImpression_WOMAI) {
		productMapper.insertReviewImpressionInfo(reviewImpression_WOMAI);
	}

	@Override
	public void insertReviewInfo(ReviewInfo_WOMAI reviewInfo_WOMAI) {
		productMapper.insertReviewInfo(reviewInfo_WOMAI);
	}

	@Override
	public void insertReviewBadInfo(ReviewBadInfo_WOMAI reviewBadInfo_WOMAI) {
		productMapper.insertReviewBadInfo(reviewBadInfo_WOMAI);
	}

	@Override
	public int getProductCountByProductListID(int product_list_id) {
		return productMapper.getProductCountByProductListID(product_list_id);
	}
}
