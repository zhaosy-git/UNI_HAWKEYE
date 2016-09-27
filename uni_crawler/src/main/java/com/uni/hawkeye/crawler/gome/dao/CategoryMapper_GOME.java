package com.uni.hawkeye.crawler.gome.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.uni.hawkeye.crawler.gome.bean.CategoryInfo;

public interface CategoryMapper_GOME {

	void insertCategoryInfo(CategoryInfo category_info);

	List<CategoryInfo> getCategoryInfoByStatus(@Param(value = "executeId") Integer executeId, @Param(value = "status") String status);

	void updateCategoryInfoStatus(@Param(value = "categoryId") int category_id, @Param(value = "status") String status);

}
