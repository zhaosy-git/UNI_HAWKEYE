package com.uni.hawkeye.crawler.gome.logic.impl;

import java.util.List;

import javax.annotation.Resource;

import com.uni.hawkeye.crawler.gome.bean.CategoryInfo;
import com.uni.hawkeye.crawler.gome.bean.ProductInfo;
import com.uni.hawkeye.crawler.gome.bean.ProductList;
import com.uni.hawkeye.crawler.gome.bean.ReviewBadInfo_GOME;
import com.uni.hawkeye.crawler.gome.bean.ReviewImpression_GOME;
import com.uni.hawkeye.crawler.gome.bean.ReviewInfo_GOME;
import com.uni.hawkeye.crawler.gome.bean.TaskControl;
import com.uni.hawkeye.crawler.gome.dao.CategoryMapper_GOME;
import com.uni.hawkeye.crawler.gome.dao.ProductListMapper_GOME;
import com.uni.hawkeye.crawler.gome.dao.TaskControlMapper_GOME;
import com.uni.hawkeye.crawler.gome.logic.PersistenceLogic;

public class PersistenceLogicImpl implements PersistenceLogic {
	
	private TaskControlMapper_GOME tcMapper;
	private CategoryMapper_GOME catMapper;
	private ProductListMapper_GOME productMapper;

	@Resource
	public void setTcMapper(TaskControlMapper_GOME tcMapper) {
		this.tcMapper = tcMapper;
	}
	
	@Resource
	public void setCatMapper(CategoryMapper_GOME catMapper) {
		this.catMapper = catMapper;
	}

	@Resource
	public void setProductMapper(ProductListMapper_GOME productMapper) {
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
	public void insertReviewInfo(ReviewInfo_GOME reviewInfo_GOME) {
		productMapper.insertReviewInfo(reviewInfo_GOME);
	}

	@Override
	public void insertReviewImpressionInfo(ReviewImpression_GOME reviewImpression_GOME) {
		productMapper.insertReviewImpressionInfo(reviewImpression_GOME);
	}

	@Override
	public void insertReviewBadInfo(ReviewBadInfo_GOME reviewBadInfo_GOME) {
		productMapper.insertReviewBadInfo(reviewBadInfo_GOME);
	}

	@Override
	public int getProductCountByProductListID(int product_list_id) {
		return productMapper.getProductCountByProductListID(product_list_id);
	}

}
