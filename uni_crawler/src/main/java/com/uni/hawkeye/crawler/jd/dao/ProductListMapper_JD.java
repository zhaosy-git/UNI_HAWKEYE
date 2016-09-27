package com.uni.hawkeye.crawler.jd.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.uni.hawkeye.crawler.jd.bean.ProductInfo;
import com.uni.hawkeye.crawler.jd.bean.ProductList;
import com.uni.hawkeye.crawler.jd.bean.ReviewBadInfo_JD;
import com.uni.hawkeye.crawler.jd.bean.ReviewImpression_JD;
import com.uni.hawkeye.crawler.jd.bean.ReviewInfo_JD;

public interface ProductListMapper_JD {

	void insertProductList(ProductList productList);

	List<ProductList> getProductListByStatus(@Param(value = "executeId") Integer executeId, @Param(value = "status") String status);

	void insertProductInfo(ProductInfo productInfo);

	void undateProductList(ProductList productList);

	void insertReviewInfo(ReviewInfo_JD reviewJD);

	void insertReviewImpressionInfo(ReviewImpression_JD reviewImpression_JD);

	void insertReviewBadInfo(ReviewBadInfo_JD reviewBadInfo_JD);

	int getProductCountByProductListID(@Param(value = "product_list_id") Integer product_list_id);

}
