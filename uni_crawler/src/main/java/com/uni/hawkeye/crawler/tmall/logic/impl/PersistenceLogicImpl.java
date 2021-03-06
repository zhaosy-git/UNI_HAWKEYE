package com.uni.hawkeye.crawler.tmall.logic.impl;

import java.util.List;

import javax.annotation.Resource;

import com.uni.hawkeye.crawler.tmall.bean.CategoryInfo;
import com.uni.hawkeye.crawler.tmall.bean.PriceInfo_TMALL;
import com.uni.hawkeye.crawler.tmall.bean.ProductInfo;
import com.uni.hawkeye.crawler.tmall.bean.ProductList;
import com.uni.hawkeye.crawler.tmall.bean.ReviewBadInfo_TMALL;
import com.uni.hawkeye.crawler.tmall.bean.ReviewImpression_TMALL;
import com.uni.hawkeye.crawler.tmall.bean.ReviewInfo_TMALL;
import com.uni.hawkeye.crawler.tmall.bean.TaskControl;
import com.uni.hawkeye.crawler.tmall.dao.CategoryMapper_TMALL;
import com.uni.hawkeye.crawler.tmall.dao.ProductListMapper_TMALL;
import com.uni.hawkeye.crawler.tmall.dao.TaskControlMapper_TMALL;
import com.uni.hawkeye.crawler.tmall.logic.PersistenceLogic;

public class PersistenceLogicImpl implements PersistenceLogic {
	
	private TaskControlMapper_TMALL tcMapper;
	private CategoryMapper_TMALL catMapper;
	private ProductListMapper_TMALL productMapper;

	@Resource
	public void setTcMapper(TaskControlMapper_TMALL tcMapper) {
		this.tcMapper = tcMapper;
	}
	
	@Resource
	public void setCatMapper(CategoryMapper_TMALL catMapper) {
		this.catMapper = catMapper;
	}

	@Resource
	public void setProductMapper(ProductListMapper_TMALL productMapper) {
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
	public List<ProductList> getProductListByStatus(Integer execute_id, Integer status) {
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
	public void insertPriceInfo(PriceInfo_TMALL priceInfo_TMALL) {
		productMapper.insertPriceInfo(priceInfo_TMALL);
	}

	@Override
	public void insertReviewImpressionInfo(ReviewImpression_TMALL reviewImpression_TMALL) {
		productMapper.insertReviewImpressionInfo(reviewImpression_TMALL);
	}

	@Override
	public void insertReviewInfo(ReviewInfo_TMALL reviewInfo_TMALL) {
		productMapper.insertReviewInfo(reviewInfo_TMALL);
	}

	@Override
	public void insertReviewBadInfo(ReviewBadInfo_TMALL reviewBadInfo_TMALL) {
		productMapper.insertReviewBadInfo(reviewBadInfo_TMALL);
	}

	@Override
	public int getProductCountByProductListID(int product_list_id) {
		return productMapper.getProductCountByProductListID(product_list_id);
	}
}
