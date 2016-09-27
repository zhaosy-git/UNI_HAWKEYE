package com.uni.hawkeye.crawler.yhd.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.uni.hawkeye.crawler.yhd.bean.ProductInfo;
import com.uni.hawkeye.crawler.yhd.bean.ProductList;
import com.uni.hawkeye.crawler.yhd.bean.ReviewBadInfo_YHD;
import com.uni.hawkeye.crawler.yhd.bean.ReviewImpression_YHD;
import com.uni.hawkeye.crawler.yhd.bean.ReviewInfo_YHD;

public interface ProductListMapper_YHD {

	void insertProductList(ProductList productList);

	List<ProductList> getProductListByStatus(@Param(value = "executeId") Integer executeId, @Param(value = "status") String status);

	void undateProductList(ProductList productList);

	void insertProductInfo(ProductInfo productInfo);

	void insertReviewImpressionInfo(ReviewImpression_YHD reviewImpression_YHD);

	void insertReviewInfo(ReviewInfo_YHD reviewInfo_YHD);

	void insertReviewBadInfo(ReviewBadInfo_YHD reviewBadInfo_YHD);

	int getProductCountByProductListID(@Param(value = "product_list_id") Integer product_list_id);

}
