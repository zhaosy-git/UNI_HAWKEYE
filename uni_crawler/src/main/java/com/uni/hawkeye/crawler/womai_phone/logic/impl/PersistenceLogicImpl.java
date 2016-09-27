package com.uni.hawkeye.crawler.womai_phone.logic.impl;

import java.util.List;

import javax.annotation.Resource;

import com.uni.hawkeye.crawler.womai_phone.bean.CategoryInfo;
import com.uni.hawkeye.crawler.womai_phone.bean.PriceInfo_WOMAI;
import com.uni.hawkeye.crawler.womai_phone.bean.ProductInfo;
import com.uni.hawkeye.crawler.womai_phone.bean.ProductList;
import com.uni.hawkeye.crawler.womai_phone.bean.ReviewBadInfo_WOMAI;
import com.uni.hawkeye.crawler.womai_phone.bean.ReviewImpression_WOMAI;
import com.uni.hawkeye.crawler.womai_phone.bean.ReviewInfo_WOMAI;
import com.uni.hawkeye.crawler.womai_phone.bean.TaskControl;
import com.uni.hawkeye.crawler.womai_phone.dao.CategoryMapper_WOMAI_PHONE;
import com.uni.hawkeye.crawler.womai_phone.dao.ProductListMapper_WOMAI_PHONE;
import com.uni.hawkeye.crawler.womai_phone.dao.TaskControlMapper_WOMAI_PHONE;
import com.uni.hawkeye.crawler.womai_phone.logic.PersistenceLogic;


public class PersistenceLogicImpl implements PersistenceLogic {
	
	private TaskControlMapper_WOMAI_PHONE tcMapper;
	private CategoryMapper_WOMAI_PHONE catMapper;
	private ProductListMapper_WOMAI_PHONE productMapper;

	@Resource
	public void setTcMapper(TaskControlMapper_WOMAI_PHONE tcMapper) {
		this.tcMapper = tcMapper;
	}
	
	@Resource
	public void setCatMapper(CategoryMapper_WOMAI_PHONE catMapper) {
		this.catMapper = catMapper;
	}

	@Resource
	public void setProductMapper(ProductListMapper_WOMAI_PHONE productMapper) {
		this.productMapper = productMapper;
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
