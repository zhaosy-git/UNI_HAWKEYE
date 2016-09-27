package com.uni.hawkeye.proxy.logic.impl;

import javax.annotation.Resource;

import com.uni.hawkeye.proxy.bean.UniProxyInfo;
import com.uni.hawkeye.proxy.dao.ProxyMapper;
import com.uni.hawkeye.proxy.logic.PersistenceLogic;

public class PersistenceLogicImpl implements PersistenceLogic {

	private ProxyMapper proxyMapper;
	
	/**
	 * @param proxyMapper the proxyMapper to set
	 */
	@Resource
	public void setProxyMapper(ProxyMapper proxyMapper) {
		this.proxyMapper = proxyMapper;
	}

	@Override
	public UniProxyInfo getUniProxyInfo(String site_code, String do_flag, String expire) {
		return proxyMapper.getUniProxyInfo(site_code, do_flag, expire);
	}

	@Override
	public void updateUniProxyInfo(UniProxyInfo uniProxyInfo) {
		proxyMapper.updateUniProxyInfo(uniProxyInfo);
	}

}
