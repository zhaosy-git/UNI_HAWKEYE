package com.uni.hawkeye.crawler.amazon.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.uni.hawkeye.crawler.amazon.bean.ProductInfo;
import com.uni.hawkeye.crawler.amazon.bean.ProductList;
import com.uni.hawkeye.crawler.amazon.bean.ReviewBadInfo_AMAZON;
import com.uni.hawkeye.crawler.amazon.bean.ReviewImpression_AMAZON;
import com.uni.hawkeye.crawler.amazon.bean.ReviewInfo_AMAZON;

public interface ProductListMapper_AMAZON {

	void insertProductList(ProductList productList);

	List<ProductList> getProductListByStatus(@Param(value = "executeId") Integer executeId, @Param(value = "status") String status);

	void undateProductList(ProductList productList);

	void insertProductInfo(ProductInfo productInfo);

	void insertReviewImpressionInfo(ReviewImpression_AMAZON reviewImpression_AMAZON);

	void insertReviewInfo(ReviewInfo_AMAZON reviewInfo_AMAZON);

	void insertReviewBadInfo(ReviewBadInfo_AMAZON reviewBadInfo_AMAZON);

	int getProductCountByProductListID(@Param(value = "product_list_id") Integer product_list_id);

}
