package com.uni.hawkeye.crawler.sina_weibo_api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.uni.hawkeye.crawler.sina_weibo_api.bean.CategoryInfo;

public interface CategoryMapper_SINA_WEIBO_API {

	void insertCategoryInfo(CategoryInfo category_info);

	List<CategoryInfo> getCategoryInfoByStatus(@Param(value = "execute_id_fk") Integer execute_id_fk, @Param(value = "status") String status, @Param(value = "category_name") String category_name);

	void updateCategoryInfoStatus(@Param(value = "categoryId") int category_id, @Param(value = "status") String status);

}
