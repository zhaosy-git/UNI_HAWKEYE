package com.uni.hawkeye.crawler.yhd.logic;

import java.util.List;

import com.uni.hawkeye.crawler.yhd.bean.CategoryInfo;
import com.uni.hawkeye.crawler.yhd.bean.ProductInfo;
import com.uni.hawkeye.crawler.yhd.bean.ProductList;
import com.uni.hawkeye.crawler.yhd.bean.ReviewBadInfo_YHD;
import com.uni.hawkeye.crawler.yhd.bean.ReviewImpression_YHD;
import com.uni.hawkeye.crawler.yhd.bean.ReviewInfo_YHD;
import com.uni.hawkeye.crawler.yhd.bean.TaskControl;

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

	void insertReviewImpressionInfo(ReviewImpression_YHD reviewImpression_YHD);

	void insertReviewInfo(ReviewInfo_YHD reviewInfo_YHD);

	void insertReviewBadInfo(ReviewBadInfo_YHD reviewBadInfo_YHD);

	int getProductCountByProductListID(int product_list_id);

}
