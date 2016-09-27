package com.uni.hawkeye.jsoup;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;

import com.uni.hawkeye.enums.WebSiteEnum;
import com.uni.hawkeye.jsoup.bean.CrawlerPropertyInfo;
import com.uni.hawkeye.proxy.bean.UniProxyInfo;
import com.uni.hawkeye.utils.DataUtil;

public class JsoupCrawler {

	private static Log log = LogFactory.getLog(JsoupCrawler.class);

	private static int retry_cnt;

	private static int sleep_time;

	private static int time_out;

	private static String userAgent;

	private static String referrer = "";

	private static boolean proxy_flag;

	private static com.uni.hawkeye.proxy.logic.PersistenceLogic persistenceLogic_proxy;

	private static com.uni.hawkeye.jsoup.logic.PersistenceLogic persistenceLogic_jsoup;
	
	private static Proxy proxy;
	
	private static boolean save_html;
	
	private static String save_html_path;
	
	public static Document fetchDocument(String url, String site_code) throws Exception {
		if(proxy_flag){
			proxy = getProxy(site_code);
			log.info("use proxy crawler...");
			log.info("proxy ip:port --- >>> " + proxy.address());
			return fetchDocumentByProxy(url, site_code);
		}else{
			return fetchDocumentByNoProxy(url, site_code);
		}
	}

	private static Document fetchDocumentByProxy(String urlStr, String site_code) throws Exception {
		int i = 1;
		Document doc = null;
		HttpURLConnection uc = null; 
		while (true) {
			try {
				URL url = new URL(urlStr);
				Thread.sleep(((int) (sleep_time * Math.random() + 1)) * 1000);
				uc = (HttpURLConnection) url.openConnection(proxy);
				uc.setConnectTimeout(time_out * 1000);
				uc.setReadTimeout(time_out * 1000);
				uc.setDoOutput(true);
				uc.setUseCaches(true);
				uc.setRequestProperty("Content-type", "application/x-java-serialized-object");
				uc.setRequestProperty("Accept",
					"application/x-ms-application, image/jpeg, application/xaml+xml, "+
					"image/gif, image/pjpeg, application/x-ms-xbap, application/vnd.ms-excel, "+
					"application/vnd.ms-powerpoint, application/msword,  */*");
				uc.setRequestProperty("Accept-Language", "zh-CN");
				uc.setRequestProperty("User-Agent", userAgent);
				uc.setRequestProperty("Accept-Encoding", "gzip, deflate");
				uc.connect();
				String line = null;
				StringBuffer tmp = new StringBuffer();
				BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
				while ((line = in.readLine()) != null) {
					tmp.append(line);
				}
				doc = Jsoup.parse(String.valueOf(tmp));
				return doc;
			} catch (Exception e) {
				if (i > retry_cnt) {
					String ip = proxy.address().toString().split(":")[0].replace("/", "");
					String port = proxy.address().toString().split(":")[1];
					UniProxyInfo uniProxyInfo = new UniProxyInfo();
					uniProxyInfo.setIp(ip);
					uniProxyInfo.setPort(port);
					uniProxyInfo.setExpire("0");
					uniProxyInfo.setDo_flag("1");
					persistenceLogic_proxy.updateUniProxyInfo(uniProxyInfo);
					log.error("重试超过" + retry_cnt + "次,访问失败!尝试下一个proxy", e);
					proxy = getProxy(site_code);
					i = 1;
				}else{
					log.info("重试第 " + i + " 次. " + e.getMessage());
					i++;
					Thread.sleep(((int) (sleep_time * Math.random() + 1)) * 1000);
				}
			}
		}
	}

	private static Document fetchDocumentByNoProxy(String url, String site_code) throws Exception {

		Connection conn = Jsoup.connect(url);
		int i = 1;
		Document doc = null;
		while (true) {
			try {
				Thread.sleep(((int) (sleep_time * Math.random() + 1)) * 1000);
				CrawlerPropertyInfo propertyInfo = persistenceLogic_jsoup.getJsoupProperty(WebSiteEnum.convertEnum(site_code).name());
				String cookie = "";
				if(propertyInfo != null && !StringUtil.isBlank(propertyInfo.getCookie())){
					cookie = propertyInfo.getCookie();
				}
				doc = conn
						.userAgent(userAgent)
						.ignoreContentType(true)
						.timeout(time_out * 1000)
						.followRedirects(true)
						.referrer(referrer)
						.cookies(DataUtil.getCookieMap(cookie))
						.get();
				String html = "";
				if(site_code.equals("12")){
					html = doc.html();
				}else{
					html = doc.body().html();
				}
				html = html.replaceAll("<!--", "").replaceAll("-->", "").replaceAll("style=\"display:none;\"", "");
				if(save_html){
					FileWriter fileWriter = null;
					try {
						fileWriter = new FileWriter(DataUtil.format(save_html_path, new String[]{new Date().getTime() + ""}));
						fileWriter.write(html);
						fileWriter.flush();
					} catch (IOException e) {
						e.printStackTrace();
					} finally{
						fileWriter.close();
					}
				}
				return Jsoup.parse(html);
			} catch (Exception e) {
				if (i > Integer.valueOf(retry_cnt)) {
					throw new Exception("重试超过" + retry_cnt + "次,访问失败!", e);
				}
				log.info("重试第 " + i + " 次. " + e.getMessage());
				i++;
				Thread.sleep(((int) (sleep_time * Math.random() + 1)) * 1000);
			}
		}
	}

	public static Proxy getProxy(String site_code) {
		Proxy proxy = null;
		if(proxy_flag){
			String do_flag = "0";
			String expire = "1";
			UniProxyInfo uniProxyInfo = persistenceLogic_proxy.getUniProxyInfo(site_code, do_flag, expire);
			if(uniProxyInfo != null){
				SocketAddress addr = new InetSocketAddress(uniProxyInfo.getIp(), Integer.valueOf(uniProxyInfo.getPort()));
				proxy = new Proxy(Proxy.Type.HTTP, addr);
				do_flag = "1";
				uniProxyInfo.setDo_flag(do_flag);
				persistenceLogic_proxy.updateUniProxyInfo(uniProxyInfo);
			}
		}
		return proxy;
	}

	/**
	 * @param retry_cnt the retry_cnt to set
	 */
	public static void setRetry_cnt(int retry_cnt) {
		JsoupCrawler.retry_cnt = retry_cnt;
	}

	/**
	 * @param sleep_time
	 *            the sleep_time to set
	 */
	public static void setSleep_time(int sleep_time) {
		JsoupCrawler.sleep_time = sleep_time;
	}

	/**
	 * @param time_out
	 *            the time_out to set
	 */
	public static void setTime_out(int time_out) {
		JsoupCrawler.time_out = time_out;
	}

	/**
	 * @param userAgent
	 *            the userAgent to set
	 */
	public static void setUserAgent(String userAgent) {
		JsoupCrawler.userAgent = userAgent;
	}

	/**
	 * @param referrer
	 *            the referrer to set
	 */
	public static void setReferrer(String referrer) {
		JsoupCrawler.referrer = referrer;
	}

	/**
	 * @param proxy_flag
	 *            the proxy_flag to set
	 */
	public static void setProxy_flag(boolean proxy_flag) {
		JsoupCrawler.proxy_flag = proxy_flag;
	}

	/**
	 * @param persistenceLogic_proxy the persistenceLogic_proxy to set
	 */
	public static void setPersistenceLogic_proxy(com.uni.hawkeye.proxy.logic.PersistenceLogic persistenceLogic_proxy) {
		JsoupCrawler.persistenceLogic_proxy = persistenceLogic_proxy;
	}

	/**
	 * @param persistenceLogic_jsoup the persistenceLogic_jsoup to set
	 */
	public static void setPersistenceLogic_jsoup(com.uni.hawkeye.jsoup.logic.PersistenceLogic persistenceLogic_jsoup) {
		JsoupCrawler.persistenceLogic_jsoup = persistenceLogic_jsoup;
	}

	/**
	 * @param proxy the proxy to set
	 */
	public static void setProxy(Proxy proxy) {
		JsoupCrawler.proxy = proxy;
	}

	/**
	 * @param save_html the save_html to set
	 */
	public static void setSave_html(boolean save_html) {
		JsoupCrawler.save_html = save_html;
	}

	/**
	 * @param save_html_path the save_html_path to set
	 */
	public static void setSave_html_path(String save_html_path) {
		JsoupCrawler.save_html_path = save_html_path;
	}

}
