package com.uni.hawkeye.crawler.tmall.logic;

import java.util.List;

import com.uni.hawkeye.crawler.tmall.bean.CategoryInfo;
import com.uni.hawkeye.crawler.tmall.bean.PriceInfo_TMALL;
import com.uni.hawkeye.crawler.tmall.bean.ProductInfo;
import com.uni.hawkeye.crawler.tmall.bean.ProductList;
import com.uni.hawkeye.crawler.tmall.bean.ReviewBadInfo_TMALL;
import com.uni.hawkeye.crawler.tmall.bean.ReviewImpression_TMALL;
import com.uni.hawkeye.crawler.tmall.bean.ReviewInfo_TMALL;
import com.uni.hawkeye.crawler.tmall.bean.TaskControl;


public interface PersistenceLogic {

	TaskControl getLastestTask(String site_code, int task_id);

	void insertTaskControl(TaskControl tc);

	TaskControl updateTaskAsUrlCrawling(TaskControl tc);

	void insertCategoryInfo(CategoryInfo level_1_category_info);

	List<CategoryInfo> getCategoryInfoByStatus(Integer execute_id, String value);

	void updateCategoryInfoStatus(int category_id, String value);

	void insertProductList(ProductList productList);

	List<ProductList> getProductListByStatus(Integer execute_id, Integer string);

	void undateProductList(ProductList productList);

	void insertProductInfo(ProductInfo productInfo);

	void insertPriceInfo(PriceInfo_TMALL priceInfo_TMALL);

	void insertReviewImpressionInfo(ReviewImpression_TMALL reviewImpression_TMALL);

	void insertReviewInfo(ReviewInfo_TMALL reviewInfo_TMALL);

	void insertReviewBadInfo(ReviewBadInfo_TMALL reviewBadInfo_TMALL);

	int getProductCountByProductListID(int product_list_id);

}
