package com.uni.hawkeye.crawler.baidu_tieba.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.uni.hawkeye.crawler.baidu_tieba.bean.BarInfo;

public interface BarMapper_BAIDU_TIEBA {

	void insertBarInfo(BarInfo barInfo);

	List<BarInfo> getBarInfoByStatus(@Param(value = "execute_id_fk") Integer execute_id_fk, @Param(value = "status") String status, @Param(value = "bar_name") String target_bar_name);

	void updateBarInfoStatus(@Param(value = "tieba_id") int tieba_id, @Param(value = "status") String status);

}
