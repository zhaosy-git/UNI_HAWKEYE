package com.uni.hawkeye.crawler.amazon.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.uni.hawkeye.crawler.amazon.bean.CategoryInfo;

public interface CategoryMapper_AMAZON {

	void insertCategoryInfo(CategoryInfo category_info);

	List<CategoryInfo> getCategoryInfoByStatus(@Param(value = "executeId") Integer executeId, @Param(value = "status") String status);

	void updateCategoryInfoStatus(@Param(value = "categoryId") int category_id, @Param(value = "status") String status);

}
