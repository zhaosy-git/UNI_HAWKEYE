package com.uni.hawkeye.crawler.jd.logic;

import java.util.List;

import com.uni.hawkeye.crawler.jd.bean.CategoryInfo;
import com.uni.hawkeye.crawler.jd.bean.ProductInfo;
import com.uni.hawkeye.crawler.jd.bean.ProductList;
import com.uni.hawkeye.crawler.jd.bean.ReviewBadInfo_JD;
import com.uni.hawkeye.crawler.jd.bean.ReviewImpression_JD;
import com.uni.hawkeye.crawler.jd.bean.ReviewInfo_JD;
import com.uni.hawkeye.crawler.jd.bean.TaskControl;

public interface PersistenceLogic {

	TaskControl getLastestTask(String site_code, int task_id);

	void insertTaskControl(TaskControl tc);

	TaskControl updateTaskAsUrlCrawling(TaskControl tc);

	void insertCategoryInfo(CategoryInfo level_1_category_info);

	List<CategoryInfo> getCategoryInfoByStatus(Integer executeId, String status);

	void insertProductList(ProductList productList);

	void updateCategoryInfoStatus(String category_id, int execute_id, String status);

	List<ProductList> getProductListByStatus(Integer executeId, String status);

	void insertProductInfo(ProductInfo productInfo);

	void undateProductList(ProductList productList);

	void insertReviewInfo(ReviewInfo_JD reviewJD);

	void insertReviewImpressionInfo(ReviewImpression_JD reviewImpression_JD);

	void insertReviewBadInfo(ReviewBadInfo_JD reviewBadInfo_JD);

	int getProductCountByProductListID(int product_list_id);

}
