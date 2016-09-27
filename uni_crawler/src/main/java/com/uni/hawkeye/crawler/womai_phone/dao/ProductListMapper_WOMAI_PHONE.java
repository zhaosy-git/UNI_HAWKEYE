package com.uni.hawkeye.crawler.womai_phone.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.uni.hawkeye.crawler.womai_phone.bean.PriceInfo_WOMAI;
import com.uni.hawkeye.crawler.womai_phone.bean.ProductInfo;
import com.uni.hawkeye.crawler.womai_phone.bean.ProductList;
import com.uni.hawkeye.crawler.womai_phone.bean.ReviewBadInfo_WOMAI;
import com.uni.hawkeye.crawler.womai_phone.bean.ReviewImpression_WOMAI;
import com.uni.hawkeye.crawler.womai_phone.bean.ReviewInfo_WOMAI;

public interface ProductListMapper_WOMAI_PHONE {

	void insertProductList(ProductList productList);

	List<ProductList> getProductListByStatus(@Param(value = "executeId") Integer executeId, @Param(value = "status") String status);

	void undateProductList(ProductList productList);

	void insertProductInfo(ProductInfo productInfo);

	void insertPriceInfo(PriceInfo_WOMAI priceInfo_WOMAI);

	void insertReviewImpressionInfo(ReviewImpression_WOMAI reviewImpression_YHD);

	void insertReviewInfo(ReviewInfo_WOMAI reviewInfo_YHD);

	void insertReviewBadInfo(ReviewBadInfo_WOMAI reviewBadInfo_YHD);

	int getProductCountByProductListID(@Param(value = "product_list_id") Integer product_list_id);

}
