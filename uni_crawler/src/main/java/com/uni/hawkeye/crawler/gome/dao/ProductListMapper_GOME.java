package com.uni.hawkeye.crawler.gome.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.uni.hawkeye.crawler.gome.bean.ProductInfo;
import com.uni.hawkeye.crawler.gome.bean.ProductList;
import com.uni.hawkeye.crawler.gome.bean.ReviewBadInfo_GOME;
import com.uni.hawkeye.crawler.gome.bean.ReviewImpression_GOME;
import com.uni.hawkeye.crawler.gome.bean.ReviewInfo_GOME;


public interface ProductListMapper_GOME {

	void insertProductList(ProductList productList);

	List<ProductList> getProductListByStatus(@Param(value = "executeId") Integer executeId, @Param(value = "status") String status);

	void undateProductList(ProductList productList);

	void insertProductInfo(ProductInfo productInfo);

	void insertReviewInfo(ReviewInfo_GOME reviewInfo_GOME);

	void insertReviewImpressionInfo(ReviewImpression_GOME reviewImpression_GOME);

	void insertReviewBadInfo(ReviewBadInfo_GOME reviewBadInfo_GOME);

	int getProductCountByProductListID(@Param(value = "product_list_id") Integer product_list_id);


}
