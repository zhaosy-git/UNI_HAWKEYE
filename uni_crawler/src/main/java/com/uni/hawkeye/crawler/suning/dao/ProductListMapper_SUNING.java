package com.uni.hawkeye.crawler.suning.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.uni.hawkeye.crawler.suning.bean.PriceInfo_SUNING;
import com.uni.hawkeye.crawler.suning.bean.ProductInfo;
import com.uni.hawkeye.crawler.suning.bean.ProductList;
import com.uni.hawkeye.crawler.suning.bean.ReviewBadInfo_SUNING;
import com.uni.hawkeye.crawler.suning.bean.ReviewImpression_SUNING;
import com.uni.hawkeye.crawler.suning.bean.ReviewInfo_SUNING;

public interface ProductListMapper_SUNING {

	void insertProductList(ProductList productList);

	List<ProductList> getProductListByStatus(@Param(value = "executeId") Integer executeId, @Param(value = "status") String status);

	void undateProductList(ProductList productList);

	void insertProductInfo(ProductInfo productInfo);

	void insertPriceInfo(PriceInfo_SUNING priceInfo_SUNING);

	void insertReviewImpressionInfo(ReviewImpression_SUNING reviewImpression_SUNING);

	void insertReviewInfo(ReviewInfo_SUNING reviewInfo_SUNING);

	void insertReviewBadInfo(ReviewBadInfo_SUNING reviewBadInfo_SUNING);

	int getProductCountByProductListID(@Param(value = "product_list_id") Integer product_list_id);

}
