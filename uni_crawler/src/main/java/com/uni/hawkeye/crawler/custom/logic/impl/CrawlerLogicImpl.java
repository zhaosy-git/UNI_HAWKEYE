package com.uni.hawkeye.crawler.custom.logic.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.uni.hawkeye.crawler.custom.logic.PersistenceLogic;
import com.uni.hawkeye.crawler.custom.bean.TaskControl;
import com.uni.hawkeye.crawler.custom.logic.CrawlerLogic;
import com.uni.hawkeye.jsoup.JsoupCrawler;
import com.uni.hawkeye.log.Uni_LOG_Common;

import uni_hawkeye.core.CustomTaskInfo;

public class CrawlerLogicImpl implements CrawlerLogic {

	private static Log log = LogFactory.getLog(CrawlerLogicImpl.class);

	private PersistenceLogic persistenceLogic;

	private String custom_html_page_title;
	private String custom_html_page_desc;
	private String custom_html_page_keyword;

	@Override
	public void crawler(TaskControl tc, CustomTaskInfo customInfo) {

		String urls = "";
		if (customInfo != null) {
			urls = customInfo.getUrl();
		}

		if (!StringUtil.isBlank(urls)) {
			String[] urlArr = urls.split("/n");

			for (int i = 0; i < urlArr.length; i++) {
				String url = urlArr[i];
				Document doc = null;
				try {
					doc = JsoupCrawler.fetchDocument(url, tc.getSite_code());
				} catch (Exception e) {
					Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "CUSTOM", url);
				}

				if (doc != null) {
					String title = doc.select(custom_html_page_title).text();
					String desc = doc.select(custom_html_page_desc).attr("content");
					String keyword = doc.select(custom_html_page_keyword).attr("content");

					Pattern p = Pattern.compile("<.*>.*" + desc);
					Matcher m = p.matcher(doc.body().html());
					if (m.find()) {
						String result = m.group(0);
						String tag = result.substring(result.lastIndexOf("<") + 1, result.lastIndexOf(">"));
						Elements tag_elts = doc.select(tag + ":contains(" + desc + ")");
						int current_tag_text_length = tag_elts.text().length();
						int desc_text_length = desc.length();
						String content = getContent(tag_elts, current_tag_text_length, desc_text_length);
						System.out.println(content);
					}
				}
			}
		}
	}

	private String getContent(Elements tag_elts, int current_tag_text_length, int desc_text_length) {

		if (current_tag_text_length - desc_text_length < 50) {
			tag_elts = tag_elts.parents();
			current_tag_text_length = tag_elts.text().length();
			getContent(tag_elts, current_tag_text_length, desc_text_length);
		}
		return tag_elts.text();
	}

	public static void main(String[] args) {
		String aa = "<h1>北京植物园举办押花展</h1><span>北京植物园举办押花展</span>";
		Pattern p = Pattern.compile("<.*>.*园");
		Matcher m = p.matcher(aa);
		if (m.find()) {
			String result = m.group(0);
			System.out.println(result);
			System.out.println(result.substring(result.lastIndexOf("<") + 1, result.lastIndexOf(">")));
		}
	}

	/**
	 * @param persistenceLogic
	 *            the persistenceLogic to set
	 */
	public void setPersistenceLogic(PersistenceLogic persistenceLogic) {
		this.persistenceLogic = persistenceLogic;
	}

	/**
	 * @param custom_html_page_title
	 *            the custom_html_page_title to set
	 */
	public void setCustom_html_page_title(String custom_html_page_title) {
		this.custom_html_page_title = custom_html_page_title;
	}

	/**
	 * @param custom_html_page_desc
	 *            the custom_html_page_desc to set
	 */
	public void setCustom_html_page_desc(String custom_html_page_desc) {
		this.custom_html_page_desc = custom_html_page_desc;
	}

	/**
	 * @param custom_html_page_keyword
	 *            the custom_html_page_keyword to set
	 */
	public void setCustom_html_page_keyword(String custom_html_page_keyword) {
		this.custom_html_page_keyword = custom_html_page_keyword;
	}

}
