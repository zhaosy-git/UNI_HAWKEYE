package com.uni.hawkeye.proxy.logic;

import com.uni.hawkeye.proxy.bean.UniProxyInfo;

public interface PersistenceLogic {
	
	public UniProxyInfo getUniProxyInfo(String site_code, String do_flag, String expire);
	
	public void updateUniProxyInfo(UniProxyInfo uniProxyInfo);

}
