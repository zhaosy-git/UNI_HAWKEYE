package com.uni.hawkeye.crawler.womai.logic.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.uni.hawkeye.crawler.womai.bean.CategoryInfo;
import com.uni.hawkeye.crawler.womai.bean.PriceInfo_WOMAI;
import com.uni.hawkeye.crawler.womai.bean.ProductInfo;
import com.uni.hawkeye.crawler.womai.bean.ProductList;
import com.uni.hawkeye.crawler.womai.bean.ReviewBadInfo_WOMAI;
import com.uni.hawkeye.crawler.womai.bean.ReviewImpression_WOMAI;
import com.uni.hawkeye.crawler.womai.bean.ReviewInfo_WOMAI;
import com.uni.hawkeye.crawler.womai.bean.TaskControl;
import com.uni.hawkeye.crawler.womai.enums.CategoryControlStatus;
import com.uni.hawkeye.crawler.womai.enums.PriceEnum;
import com.uni.hawkeye.crawler.womai.enums.ProductListUrlStatusEnum;
import com.uni.hawkeye.crawler.womai.logic.CrawlerLogic;
import com.uni.hawkeye.crawler.womai.logic.PersistenceLogic;
import com.uni.hawkeye.jsoup.JsoupCrawler;
import com.uni.hawkeye.log.Uni_LOG_Common;
import com.uni.hawkeye.utils.DataUtil;

import uni_hawkeye.core.EBTaskInfo;

public class CrawlerLogicImpl implements CrawlerLogic {
	
	private static Log log = LogFactory.getLog(CrawlerLogicImpl.class);

	private static final ResourceBundle RESOURCE_BUNDLE_WHITE_LIST = ResourceBundle.getBundle("white_list_womai", Locale.getDefault());

	private PersistenceLogic persistenceLogic;
	
	private String html_category_navigation;
	
	private String html_base_url;
	
	private String html_list_last_page;
	private String html_list_product_block;
	private String html_list_product_a;
	private String html_list_title;
	private String html_detail_title;
	private String html_detail_price_json;
	private String html_detail_brand_navigation;
	private String html_detail_grade_avg;
	private String html_detail_grade_r3;
	private String html_detail_grade_r2;
	private String html_detail_grade_r1;
	private String html_detail_impression_contents;
	private String html_detail_impression_contents_avg;
	private String html_detail_bad_review_info_url;
	private String html_detail_bad_review_content;
	private String html_detail_bad_review_time;

	@Override
	public void crawlCategoryList(TaskControl tc) {
		Iterator<String> white_list = RESOURCE_BUNDLE_WHITE_LIST.keySet().iterator();
		while (white_list.hasNext()) {
			String white_list_url = RESOURCE_BUNDLE_WHITE_LIST.getString(white_list.next());
			Document doc = null;
			String url = DataUtil.format(white_list_url, new String[]{"1"});
			try {
				doc = JsoupCrawler.fetchDocument(url, tc.getSite_code());
			} catch (Exception e) {
				log.error(e.getMessage() + Uni_LOG_Common.Fetch_WhiteList_ERROR("WOMAI", url));
			}
			
			if (doc != null) {
				Elements navigations = doc.select(html_category_navigation);
				if(navigations != null && navigations.size() > 0){
						int level = 1;
						String category_parent_id = "";
						for(Element n : navigations){
							String href = n.select("a").attr("href");
							String category_code = "";
							String category_name = "";
							String url_result = "";
							if(navigations.size() > level){
								category_code = DataUtil.getParam(href).get("id");
								category_name = n.text();
							}else if(navigations.size() == level){
								url_result = white_list_url;
								category_name = n.select("em").text();
								category_code = DataUtil.getParam(url).get("Cid");
							}else{break;}
							CategoryInfo categoryInfo = new CategoryInfo(category_code, category_name, tc.getExecute_id(), url_result, CategoryControlStatus.UNDO.value(), level+"", category_parent_id);
							persistenceLogic.insertCategoryInfo(categoryInfo);
							level++;
							category_parent_id = category_code;
						}
				}else{
					log.error(Uni_LOG_Common.Parse_Category_ERROR("WOMAI", white_list_url));
				}
			}
		}
	}

	@Override
	public void crawlerProductList(TaskControl tc) {
		List<CategoryInfo> undoCategoryInfoList = persistenceLogic.getCategoryInfoByStatus(tc.getExecute_id(), CategoryControlStatus.UNDO.value());
		int undoCategoryInfoList_SIZE = undoCategoryInfoList.size();
		for(int i=0; i<undoCategoryInfoList_SIZE; i++){
			CategoryInfo catInfo = undoCategoryInfoList.get(i);
			persistenceLogic.updateCategoryInfoStatus(catInfo.getCategory_id(), CategoryControlStatus.CRAWLING.value());
			String url = catInfo.getCategory_url();
			Document doc = null;
			String url_ = DataUtil.format(url, new String[]{"1"});
			try {
				doc = JsoupCrawler.fetchDocument(url_, tc.getSite_code());
			} catch (Exception e) {
				log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "WOMAI", url_));
			}
			ProductList productList = new ProductList(catInfo.getCategory_id(), catInfo.getExecute_id(), 1, url_, ProductListUrlStatusEnum.UNDO.value()+"");
			persistenceLogic.insertProductList(productList);
			int lastPage = 0;
			if(doc != null){
				String pageCount = doc.select(html_list_last_page).text();
				lastPage = Integer.valueOf(pageCount);
			}
			for(int j=2; j<=lastPage; j++){
				productList = new ProductList(catInfo.getCategory_id(), catInfo.getExecute_id(), j, DataUtil.format(url, new String[]{j+""}), ProductListUrlStatusEnum.UNDO.value()+"");
				persistenceLogic.insertProductList(productList);
			}
			persistenceLogic.updateCategoryInfoStatus(catInfo.getCategory_id(), CategoryControlStatus.CRAWLING_FINISHED.value());
		}
	}
	@Override
	public void crawlerProduct(TaskControl tc, EBTaskInfo ebTaskInfo) {
		String key_word = "";
		BigDecimal p_l = new BigDecimal(0);
		BigDecimal p_h = new BigDecimal(999999999);
		if (ebTaskInfo != null) {
			key_word = ebTaskInfo.getKey_word().trim();
			float price_low = Float.valueOf(ebTaskInfo.getPrice_low());
			float price_high = Float.valueOf(ebTaskInfo.getPrice_high());
			p_l = new BigDecimal(price_low);
			p_h = new BigDecimal(price_high);
		}
		
		List<ProductList> productLists = persistenceLogic.getProductListByStatus(tc.getExecute_id(), ProductListUrlStatusEnum.UNDO.value()+"");
		for(int i=0; i<productLists.size(); i++){
			ProductList productList = productLists.get(i);
			productList.setStatus(ProductListUrlStatusEnum.CRAWLING.value()+"");
			persistenceLogic.undateProductList(productList);
			String productlistURL = productList.getUrl();
			int product_list_id = productList.getProduct_list_id();
			log.info(Uni_LOG_Common.Fetch_Product_Begin("WOMAI", productlistURL));
			Document doc = null;
			try {
				doc = JsoupCrawler.fetchDocument(productlistURL, tc.getSite_code());
			} catch (Exception e) {
				log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "WOMAI", productlistURL));
			}
			if(doc != null){
				Elements product_block_elements = doc.select(html_list_product_block);
				for(Element product_block_element : product_block_elements){
					String product_id = product_block_element.attr("data-productid");
					String url = html_base_url + product_block_element.select(html_list_product_a).attr("href");
					Document detailDOC = null;
					try {
						detailDOC = JsoupCrawler.fetchDocument(url, tc.getSite_code());
					} catch (Exception e) {
						log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "WOMAI", url));
						continue;
					}
					if(detailDOC != null && !StringUtil.isBlank(detailDOC.body().html())){
						log.info(Uni_LOG_Common.Print_Product_ID("WOMAI", product_id+"\t"+url));
						String product_title = detailDOC.select(html_detail_title).attr("title");
						if(StringUtil.isBlank(product_title)){
							product_title = product_block_element.select(html_list_product_a).get(0).text();
						}
						if (product_title.contains(key_word)) {
							String brand_navigation = detailDOC.select(html_detail_brand_navigation).text();
							String brand_navigations[] = brand_navigation.split(">");
							String brand_name = brand_navigations[brand_navigations.length-2];
							int status = 0;
							String json = "";
							
							Document priceDOC = null;
							String price_json_url = DataUtil.format(html_detail_price_json, new String[]{product_id});
							try {
								priceDOC = JsoupCrawler.fetchDocument(price_json_url, tc.getSite_code());
							} catch (Exception e) {
								log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "WOMAI", price_json_url));
							}
							BigDecimal price = new BigDecimal(0);
							int price_type = 0; 
							if(priceDOC != null){
								String price_json = priceDOC.text().replaceAll("callback+\\(|\\)", "");
								JSONObject price_json_obj = null;
								try {
									price_json_obj = new JSONObject(price_json);
									price_json_obj = price_json_obj.getJSONArray("result").getJSONObject(0);
									JSONObject price_obj = price_json_obj.getJSONObject("price");
									for(PriceEnum pe : PriceEnum.values()){
										if(price_obj.has(pe.name())){
											JSONObject po = price_obj.getJSONObject(pe.name());
											if(po != null){
												String product_price = po.getString("priceValue");
												price = new BigDecimal(product_price);
												price_type = pe.value();
											}
										}
									}
								} catch (JSONException e) {
									log.error(Uni_LOG_Common.Fetch_JSON_ERROR(e.getMessage(), "WOMAI", price_json, price_json_url));
								}
							}

							if (p_l.floatValue() <= price.floatValue() && p_h.floatValue() >= price.floatValue()) {
								ProductInfo productInfo = new ProductInfo(product_id, product_title, brand_name, status, product_list_id, url, json);
								persistenceLogic.insertProductInfo(productInfo);
								
								PriceInfo_WOMAI priceInfo_WOMAI = new PriceInfo_WOMAI(product_id, price_type, product_list_id, price);
								persistenceLogic.insertPriceInfo(priceInfo_WOMAI);
								
								String review_grade_avg_str = detailDOC.select(html_detail_grade_avg).text();
								float review_grade_avg = StringUtil.isBlank(review_grade_avg_str) ? 0 : Float.valueOf(review_grade_avg_str);
								String good_cnt_str = detailDOC.select(html_detail_grade_r3).text();
								good_cnt_str = DataUtil.matchByRegex("\\d+", good_cnt_str);
								int good_cnt = StringUtil.isBlank(good_cnt_str) ? 0 : Integer.valueOf(good_cnt_str);
								String general_cnt_str = detailDOC.select(html_detail_grade_r2).text();
								general_cnt_str = DataUtil.matchByRegex("\\d+", general_cnt_str);
								int general_cnt = StringUtil.isBlank(general_cnt_str) ? 0 : Integer.valueOf(general_cnt_str);
								String poor_cnt_str = detailDOC.select(html_detail_grade_r1).text();
								poor_cnt_str = DataUtil.matchByRegex("\\d+", poor_cnt_str);
								int poor_cnt = StringUtil.isBlank(poor_cnt_str) ? 0 : Integer.valueOf(poor_cnt_str);
								ReviewInfo_WOMAI reviewInfo = new ReviewInfo_WOMAI(tc.getExecute_id(), product_id, good_cnt, general_cnt, poor_cnt, review_grade_avg, "");
								persistenceLogic.insertReviewInfo(reviewInfo);
								
								Elements impression_elements = detailDOC.select(html_detail_impression_contents);
								String impression_content = "";
								Map<String, String> impression_map = new HashMap<String, String>();
								for(int j=0; j<impression_elements.size(); j++){
									Element impression = impression_elements.get(j);
									impression_map.put(impression.text(), impression.text());
								}
								
								Iterator<String> impression_iter = impression_map.values().iterator();
								while(impression_iter.hasNext()){
									impression_content += impression_iter.next() + " ";
								}
								
								Elements impression_grade_elements = detailDOC.select(html_detail_impression_contents_avg);
								String grade_str = impression_grade_elements.text();
								float impression_grade = 0;
								if(!StringUtil.isBlank(grade_str)){
									impression_grade = Float.valueOf(grade_str);
								}
								ReviewImpression_WOMAI review_impression = new ReviewImpression_WOMAI(tc.getExecute_id(), product_id, impression_content, impression_grade, "");
								persistenceLogic.insertReviewImpressionInfo(review_impression);
								
								Document badReviewDOC = null;
								String bad_review_page_url = DataUtil.format(html_detail_bad_review_info_url, new String[]{product_id});
								try {
									badReviewDOC = JsoupCrawler.fetchDocument(bad_review_page_url, tc.getSite_code());
								} catch (Exception e) {
									log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "WOMAI", bad_review_page_url));
								}
								if(badReviewDOC != null){
									Elements badReview_elements = badReviewDOC.select(html_detail_bad_review_content);
									Elements badReviewTime_elements = badReviewDOC.select(html_detail_bad_review_time);
									for(int j=0; j<badReview_elements.size(); j++){
										Element badReview = badReview_elements.get(j);
										String review_bad_info = StringUtil.isBlank(badReview.text()) ? "" : badReview.text().trim();
										Date review_bad_time = null;
										if(!badReviewTime_elements.isEmpty() && badReviewTime_elements.size() > 0){
											Element badReviewTime = badReviewTime_elements.get(j);
											review_bad_time = StringUtil.isBlank(badReviewTime.text()) ? null : DataUtil.StrToDate(badReviewTime.text().trim(), "yyyy-MM-dd HH:mm");
											
										}
										ReviewBadInfo_WOMAI reviewBadInfo = new ReviewBadInfo_WOMAI(tc.getExecute_id(), review_bad_info, review_bad_time, product_id, "");
										persistenceLogic.insertReviewBadInfo(reviewBadInfo);
									}
								}
							}
						}
					}
				}
			}

			int product_count = persistenceLogic.getProductCountByProductListID(product_list_id);
			log.info("womai fetch product end, total product count ---->> " + product_count + " list url  ---->> " + productlistURL);
			if(product_count > 0){
				productList.setStatus(ProductListUrlStatusEnum.CRAWLING_FINISHED.value()+"");
			}else{
				productList.setStatus(ProductListUrlStatusEnum.EMPTY.value()+"");
			}
			persistenceLogic.undateProductList(productList);
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
	 * @param html_category_navigation the html_category_navigation to set
	 */
	public void setHtml_category_navigation(String html_category_navigation) {
		this.html_category_navigation = html_category_navigation;
	}

	/**
	 * @param html_base_url the html_base_url to set
	 */
	public void setHtml_base_url(String html_base_url) {
		this.html_base_url = html_base_url;
	}

	/**
	 * @param html_list_last_page the html_list_last_page to set
	 */
	public void setHtml_list_last_page(String html_list_last_page) {
		this.html_list_last_page = html_list_last_page;
	}

	/**
	 * @param html_list_product_block the html_list_product_block to set
	 */
	public void setHtml_list_product_block(String html_list_product_block) {
		this.html_list_product_block = html_list_product_block;
	}

	/**
	 * @param html_list_product_a the html_list_product_a to set
	 */
	public void setHtml_list_product_a(String html_list_product_a) {
		this.html_list_product_a = html_list_product_a;
	}

	/**
	 * @param html_list_title the html_list_title to set
	 */
	public void setHtml_list_title(String html_list_title) {
		this.html_list_title = html_list_title;
	}

	/**
	 * @param html_detail_title the html_detail_title to set
	 */
	public void setHtml_detail_title(String html_detail_title) {
		this.html_detail_title = html_detail_title;
	}

	/**
	 * @param html_detail_brand_navigation the html_detail_brand_navigation to set
	 */
	public void setHtml_detail_brand_navigation(String html_detail_brand_navigation) {
		this.html_detail_brand_navigation = html_detail_brand_navigation;
	}

	/**
	 * @param html_detail_price_json the html_detail_price_json to set
	 */
	public void setHtml_detail_price_json(String html_detail_price_json) {
		this.html_detail_price_json = html_detail_price_json;
	}

	/**
	 * @param html_detail_grade_avg the html_detail_grade_avg to set
	 */
	public void setHtml_detail_grade_avg(String html_detail_grade_avg) {
		this.html_detail_grade_avg = html_detail_grade_avg;
	}

	/**
	 * @param html_detail_grade_r3 the html_detail_grade_r3 to set
	 */
	public void setHtml_detail_grade_r3(String html_detail_grade_r3) {
		this.html_detail_grade_r3 = html_detail_grade_r3;
	}

	/**
	 * @param html_detail_grade_r2 the html_detail_grade_r2 to set
	 */
	public void setHtml_detail_grade_r2(String html_detail_grade_r2) {
		this.html_detail_grade_r2 = html_detail_grade_r2;
	}

	/**
	 * @param html_detail_grade_r1 the html_detail_grade_r1 to set
	 */
	public void setHtml_detail_grade_r1(String html_detail_grade_r1) {
		this.html_detail_grade_r1 = html_detail_grade_r1;
	}

	/**
	 * @param html_detail_impression_contents the html_detail_impression_contents to set
	 */
	public void setHtml_detail_impression_contents(String html_detail_impression_contents) {
		this.html_detail_impression_contents = html_detail_impression_contents;
	}

	/**
	 * @param html_detail_impression_contents_avg the html_detail_impression_contents_avg to set
	 */
	public void setHtml_detail_impression_contents_avg(String html_detail_impression_contents_avg) {
		this.html_detail_impression_contents_avg = html_detail_impression_contents_avg;
	}

	/**
	 * @param html_detail_bad_review_info_url the html_detail_bad_review_info_url to set
	 */
	public void setHtml_detail_bad_review_info_url(String html_detail_bad_review_info_url) {
		this.html_detail_bad_review_info_url = html_detail_bad_review_info_url;
	}

	/**
	 * @param html_detail_bad_review_content the html_detail_bad_review_content to set
	 */
	public void setHtml_detail_bad_review_content(String html_detail_bad_review_content) {
		this.html_detail_bad_review_content = html_detail_bad_review_content;
	}

	/**
	 * @param html_detail_bad_review_time the html_detail_bad_review_time to set
	 */
	public void setHtml_detail_bad_review_time(String html_detail_bad_review_time) {
		this.html_detail_bad_review_time = html_detail_bad_review_time;
	}

	@Override
	public void crawlCategoryList(TaskControl tc, EBTaskInfo ebTaskInfo) {

		String category_1 = ebTaskInfo.getCategory_level_1();
		String category_1_id = tc.getExecute_id() + "_cid_1";
		CategoryInfo level_1_category_info = new CategoryInfo(category_1_id, category_1, tc.getExecute_id(), "", CategoryControlStatus.UNDO.value(), "1", "");
		persistenceLogic.insertCategoryInfo(level_1_category_info);

		String category_2 = ebTaskInfo.getCategory_level_2();
		String category_2_id = tc.getExecute_id() + "_cid_2";
		CategoryInfo level_2_category_info = new CategoryInfo(category_2_id, category_2, tc.getExecute_id(), "", CategoryControlStatus.UNDO.value(), "2", category_1_id);
		persistenceLogic.insertCategoryInfo(level_2_category_info);

		String brand = ebTaskInfo.getBrand();
		String brand_href = ebTaskInfo.getBrand_href();
		String brand_id = DataUtil.getParam(brand_href).get("brand");
		CategoryInfo brand_info = new CategoryInfo(brand_id, brand, tc.getExecute_id(), brand_href, CategoryControlStatus.UNDO.value(), "3", category_2_id);
		persistenceLogic.insertCategoryInfo(brand_info);
	}

}
