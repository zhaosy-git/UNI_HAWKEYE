package com.uni.hawkeye.crawler.suning.logic;

import java.util.List;

import com.uni.hawkeye.crawler.suning.bean.CategoryInfo;
import com.uni.hawkeye.crawler.suning.bean.PriceInfo_SUNING;
import com.uni.hawkeye.crawler.suning.bean.ProductInfo;
import com.uni.hawkeye.crawler.suning.bean.ProductList;
import com.uni.hawkeye.crawler.suning.bean.ReviewBadInfo_SUNING;
import com.uni.hawkeye.crawler.suning.bean.ReviewImpression_SUNING;
import com.uni.hawkeye.crawler.suning.bean.ReviewInfo_SUNING;
import com.uni.hawkeye.crawler.suning.bean.TaskControl;

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

	void insertPriceInfo(PriceInfo_SUNING priceInfo_SUNING);

	void insertReviewImpressionInfo(ReviewImpression_SUNING reviewImpression_SUNING);

	void insertReviewInfo(ReviewInfo_SUNING reviewInfo_SUNING);

	void insertReviewBadInfo(ReviewBadInfo_SUNING reviewBadInfo_SUNING);

	int getProductCountByProductListID(int product_list_id);

}
