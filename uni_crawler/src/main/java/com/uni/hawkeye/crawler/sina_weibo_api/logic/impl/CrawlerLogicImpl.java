package com.uni.hawkeye.crawler.sina_weibo_api.logic.impl;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.uni.hawkeye.crawler.sina_weibo_api.bean.TaskControl;
import com.uni.hawkeye.crawler.sina_weibo_api.logic.CrawlerLogic;
import com.uni.hawkeye.crawler.sina_weibo_api.logic.PersistenceLogic;
import com.uni.hawkeye.crawler.sina_weibo_api.bean.CategoryInfo;
import com.uni.hawkeye.crawler.sina_weibo_api.enums.CategoryControlStatus;
import com.uni.hawkeye.jsoup.JsoupCrawler;
import com.uni.hawkeye.log.Uni_LOG_Common;

public class CrawlerLogicImpl implements CrawlerLogic {

	private static Log log = LogFactory.getLog(CrawlerLogicImpl.class);

	private static final ResourceBundle RESOURCE_BUNDLE_WHITE_LIST = ResourceBundle.getBundle("white_list_sina_weibo_api", Locale.getDefault());

	private PersistenceLogic persistenceLogic;

	private String sina_weibo_api_base_url;
	private String sina_weibo_api_hot_code;

	private String sina_weibo_api_html_category;
	@Override
	public void crawlCategoryList(TaskControl tc) {
		Document doc = null;
		try {
			doc = JsoupCrawler.fetchDocument(sina_weibo_api_base_url + sina_weibo_api_hot_code, tc.getSite_code());
		} catch (Exception e) {
			log.error(e.getMessage() + Uni_LOG_Common.Fetch_WhiteList_ERROR("sina_weibo_html", sina_weibo_api_base_url + sina_weibo_api_hot_code));
		}

		if (doc != null) {
			String body = doc.body().html();

			String[] ss = body.split("<script>FM.view\\(");
			for (String x : ss) {
				if (x.lastIndexOf(")</script>") != -1) {
					x = x.replace(")</script>", "");
					try {
						JSONObject category_json_obj = new JSONObject(x);
						if (!category_json_obj.isNull("html")) {
							String html = category_json_obj.getString("html");
							Document category_doc = Jsoup.parse(html);
							Elements cate_elts = category_doc.select(sina_weibo_api_html_category);
							if (!cate_elts.isEmpty() && cate_elts.size() > 0) {
								for (int i = 0; i < cate_elts.size(); i++) {
									Element cate_elt = cate_elts.get(i);
									String category_name = cate_elt.text();
									String category_href = sina_weibo_api_base_url + cate_elt.attr("href");
									Pattern cate_code_p = Pattern.compile("ctg1_\\d+");
									Matcher cate_code_m = cate_code_p.matcher(category_href);
									String category_code = "";
									if (cate_code_m.find()) {
										category_code = cate_code_m.group(0);
									}
									CategoryInfo categoryInfo = new CategoryInfo(category_code, category_name, category_href, CategoryControlStatus.UNDO.value(),
											tc.getExecute_id());
									persistenceLogic.insertCategoryInfo(categoryInfo);
								}
							}
						}
					} catch (JSONException e) {
						log.error(Uni_LOG_Common.Fetch_JSON_ERROR(e.getMessage(), "sina_weibo_html", x, sina_weibo_api_base_url + sina_weibo_api_hot_code));
					}
				}
			}
		}
	}

	@Override
	public void crawlerBlockList(TaskControl tc) {}

	/**
	 * @param persistenceLogic the persistenceLogic to set
	 */
	public void setPersistenceLogic(PersistenceLogic persistenceLogic) {
		this.persistenceLogic = persistenceLogic;
	}

	/**
	 * @param sina_weibo_api_base_url the sina_weibo_api_base_url to set
	 */
	public void setSina_weibo_api_base_url(String sina_weibo_api_base_url) {
		this.sina_weibo_api_base_url = sina_weibo_api_base_url;
	}

	/**
	 * @param sina_weibo_api_hot_code the sina_weibo_api_hot_code to set
	 */
	public void setSina_weibo_api_hot_code(String sina_weibo_api_hot_code) {
		this.sina_weibo_api_hot_code = sina_weibo_api_hot_code;
	}

	/**
	 * @param sina_weibo_api_html_category the sina_weibo_api_html_category to set
	 */
	public void setSina_weibo_api_html_category(String sina_weibo_api_html_category) {
		this.sina_weibo_api_html_category = sina_weibo_api_html_category;
	}

}
