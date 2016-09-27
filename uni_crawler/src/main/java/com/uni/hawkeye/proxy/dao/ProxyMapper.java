package com.uni.hawkeye.proxy.dao;

import org.apache.ibatis.annotations.Param;

import com.uni.hawkeye.proxy.bean.UniProxyInfo;

public interface ProxyMapper {
	
	public UniProxyInfo getUniProxyInfo(@Param(value = "site_code") String site_code, @Param(value = "do_flag") String do_flag, @Param(value = "expire") String expire);

	public void updateUniProxyInfo(UniProxyInfo uniProxyInfo);
}
