package com.uni.hawkeye.crawler.gome.logic;

import java.util.List;

import com.uni.hawkeye.crawler.gome.bean.CategoryInfo;
import com.uni.hawkeye.crawler.gome.bean.ProductInfo;
import com.uni.hawkeye.crawler.gome.bean.ProductList;
import com.uni.hawkeye.crawler.gome.bean.ReviewBadInfo_GOME;
import com.uni.hawkeye.crawler.gome.bean.ReviewImpression_GOME;
import com.uni.hawkeye.crawler.gome.bean.ReviewInfo_GOME;
import com.uni.hawkeye.crawler.gome.bean.TaskControl;

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

	void insertReviewInfo(ReviewInfo_GOME reviewInfo);

	void insertReviewImpressionInfo(ReviewImpression_GOME review_impression);

	void insertReviewBadInfo(ReviewBadInfo_GOME reviewBadInfo);

	int getProductCountByProductListID(int product_list_id);

}
