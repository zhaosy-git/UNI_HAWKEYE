package com.uni.hawkeye.crawler.jd.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.uni.hawkeye.crawler.jd.bean.CategoryInfo;


public interface CategoryMapper_JD {

	void insertCategoryInfo(CategoryInfo category_info);

	List<CategoryInfo> getCategoryInfoByStatus(@Param(value = "executeId") Integer executeId, @Param(value = "status") String status);

	void updateCategoryInfoStatus(@Param(value = "categoryId") String category_id, @Param(value = "executeId") int execute_id, @Param(value = "status") String status);

}
