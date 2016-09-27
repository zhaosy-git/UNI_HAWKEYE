package com.uni.hawkeye.crawler.jd.logic.impl;

import java.util.List;

import javax.annotation.Resource;

import com.uni.hawkeye.crawler.jd.bean.CategoryInfo;
import com.uni.hawkeye.crawler.jd.bean.ProductInfo;
import com.uni.hawkeye.crawler.jd.bean.ProductList;
import com.uni.hawkeye.crawler.jd.bean.ReviewBadInfo_JD;
import com.uni.hawkeye.crawler.jd.bean.ReviewImpression_JD;
import com.uni.hawkeye.crawler.jd.bean.ReviewInfo_JD;
import com.uni.hawkeye.crawler.jd.bean.TaskControl;
import com.uni.hawkeye.crawler.jd.dao.CategoryMapper_JD;
import com.uni.hawkeye.crawler.jd.dao.ProductListMapper_JD;
import com.uni.hawkeye.crawler.jd.dao.TaskControlMapper_JD;
import com.uni.hawkeye.crawler.jd.logic.PersistenceLogic;

public class PersistenceLogicImpl implements PersistenceLogic {

	private TaskControlMapper_JD tcMapper;
	private CategoryMapper_JD catMapper;
	private ProductListMapper_JD productMapper;

	@Resource
	public void setTcMapper(TaskControlMapper_JD tcMapper) {
		this.tcMapper = tcMapper;
	}
	
	@Resource
	public void setCatMapper(CategoryMapper_JD catMapper) {
		this.catMapper = catMapper;
	}

	@Resource
	public void setProductMapper(ProductListMapper_JD productMapper) {
		this.productMapper = productMapper;
	}

	@Override
	public TaskControl getLastestTask(String siteCode, int task_id) {
		return tcMapper.selectMaxTaskControl(siteCode, task_id);
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
	public List<CategoryInfo> getCategoryInfoByStatus(Integer executeId, String status) {
		return catMapper.getCategoryInfoByStatus(executeId, status);
	}

	@Override
	public void insertProductList(ProductList productList) {
		productMapper.insertProductList(productList);
	}

	@Override
	public void updateCategoryInfoStatus(String category_id, int execute_id, String status) {
		catMapper.updateCategoryInfoStatus(category_id, execute_id, status);
	}

	@Override
	public List<ProductList> getProductListByStatus(Integer executeId, String status) {
		return productMapper.getProductListByStatus(executeId, status);
	}

	@Override
	public void insertProductInfo(ProductInfo productInfo) {
		productMapper.insertProductInfo(productInfo);
	}

	@Override
	public void undateProductList(ProductList productList) {
		productMapper.undateProductList(productList);
	}

	@Override
	public void insertReviewInfo(ReviewInfo_JD reviewJD) {
		productMapper.insertReviewInfo(reviewJD);
	}

	@Override
	public void insertReviewImpressionInfo(ReviewImpression_JD reviewImpression_JD) {
		productMapper.insertReviewImpressionInfo(reviewImpression_JD);
	}

	@Override
	public void insertReviewBadInfo(ReviewBadInfo_JD reviewBadInfo_JD) {
		productMapper.insertReviewBadInfo(reviewBadInfo_JD);
	}

	@Override
	public int getProductCountByProductListID(int product_list_id) {
		return productMapper.getProductCountByProductListID(product_list_id);
	}

}
