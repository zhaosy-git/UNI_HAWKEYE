package com.uni.hawkeye.crawler.tmall.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.uni.hawkeye.crawler.tmall.bean.PriceInfo_TMALL;
import com.uni.hawkeye.crawler.tmall.bean.ReviewBadInfo_TMALL;
import com.uni.hawkeye.crawler.tmall.bean.ReviewImpression_TMALL;
import com.uni.hawkeye.crawler.tmall.bean.ReviewInfo_TMALL;
import com.uni.hawkeye.crawler.tmall.bean.ProductInfo;
import com.uni.hawkeye.crawler.tmall.bean.ProductList;

public interface ProductListMapper_TMALL {

	void insertProductList(ProductList productList);

	List<ProductList> getProductListByStatus(@Param(value = "execute_id") Integer executeId, @Param(value = "status") Integer status);

	void undateProductList(ProductList productList);

	void insertProductInfo(ProductInfo productInfo);

	void insertPriceInfo(PriceInfo_TMALL priceInfo_TMALL);

	void insertReviewImpressionInfo(ReviewImpression_TMALL reviewImpression_TMALL);

	void insertReviewInfo(ReviewInfo_TMALL reviewInfo_TMALL);

	void insertReviewBadInfo(ReviewBadInfo_TMALL reviewBadInfo_TMALL);

	int getProductCountByProductListID(@Param(value = "product_list_id") Integer product_list_id);

}
