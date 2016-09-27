package com.uni.hawkeye.crawler.womai.logic;

import java.util.List;

import com.uni.hawkeye.crawler.womai.bean.CategoryInfo;
import com.uni.hawkeye.crawler.womai.bean.PriceInfo_WOMAI;
import com.uni.hawkeye.crawler.womai.bean.ProductInfo;
import com.uni.hawkeye.crawler.womai.bean.ProductList;
import com.uni.hawkeye.crawler.womai.bean.ReviewBadInfo_WOMAI;
import com.uni.hawkeye.crawler.womai.bean.ReviewImpression_WOMAI;
import com.uni.hawkeye.crawler.womai.bean.ReviewInfo_WOMAI;
import com.uni.hawkeye.crawler.womai.bean.TaskControl;

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

	void insertPriceInfo(PriceInfo_WOMAI priceInfo_WOMAI);

	void insertReviewImpressionInfo(ReviewImpression_WOMAI reviewImpression_YHD);

	void insertReviewInfo(ReviewInfo_WOMAI reviewInfo_YHD);

	void insertReviewBadInfo(ReviewBadInfo_WOMAI reviewBadInfo_YHD);

	int getProductCountByProductListID(int product_list_id);

}
