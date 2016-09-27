package com.uni.hawkeye.crawler.amazon.logic;

import java.util.List;

import com.uni.hawkeye.crawler.amazon.bean.CategoryInfo;
import com.uni.hawkeye.crawler.amazon.bean.ProductInfo;
import com.uni.hawkeye.crawler.amazon.bean.ProductList;
import com.uni.hawkeye.crawler.amazon.bean.ReviewBadInfo_AMAZON;
import com.uni.hawkeye.crawler.amazon.bean.ReviewImpression_AMAZON;
import com.uni.hawkeye.crawler.amazon.bean.ReviewInfo_AMAZON;
import com.uni.hawkeye.crawler.amazon.bean.TaskControl;

public interface PersistenceLogic {

	TaskControl getLastestTask(String site_code, int task_id);

	void insertTaskControl(TaskControl tc);

	TaskControl updateTaskAsUrlCrawling(TaskControl tc);

	void insertCategoryInfo(CategoryInfo level_1_category_info);

	List<CategoryInfo> getCategoryInfoByStatus(Integer execute_id, String value);

	void updateCategoryInfoStatus(int category_id, String value);

	void insertProductList(ProductList productList);

	List<ProductList> getProductListByStatus(Integer execute_id, String string);

	void undateProductList(ProductList productList);

	void insertProductInfo(ProductInfo productInfo);

	void insertReviewImpressionInfo(ReviewImpression_AMAZON reviewImpression_AMAZON);

	void insertReviewInfo(ReviewInfo_AMAZON reviewInfo_AMAZON);

	void insertReviewBadInfo(ReviewBadInfo_AMAZON reviewBadInfo_AMAZON);

	int getProductCountByProductListID(int product_list_id);

}
