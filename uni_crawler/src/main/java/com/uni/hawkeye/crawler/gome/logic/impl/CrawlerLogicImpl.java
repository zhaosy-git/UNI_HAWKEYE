package com.uni.hawkeye.crawler.gome.logic.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.uni.hawkeye.crawler.gome.bean.CategoryInfo;
import com.uni.hawkeye.crawler.gome.bean.ProductInfo;
import com.uni.hawkeye.crawler.gome.bean.ProductList;
import com.uni.hawkeye.crawler.gome.bean.ReviewBadInfo_GOME;
import com.uni.hawkeye.crawler.gome.bean.ReviewImpression_GOME;
import com.uni.hawkeye.crawler.gome.bean.ReviewInfo_GOME;
import com.uni.hawkeye.crawler.gome.bean.TaskControl;
import com.uni.hawkeye.crawler.gome.enums.CategoryControlStatus;
import com.uni.hawkeye.crawler.gome.enums.ProductListUrlStatusEnum;
import com.uni.hawkeye.crawler.gome.logic.CrawlerLogic;
import com.uni.hawkeye.crawler.gome.logic.PersistenceLogic;
import com.uni.hawkeye.jsoup.JsoupCrawler;
import com.uni.hawkeye.log.Uni_LOG_Common;
import com.uni.hawkeye.utils.DataUtil;

import uni_hawkeye.core.EBTaskInfo;

public class CrawlerLogicImpl implements CrawlerLogic {
	
	private static Log log = LogFactory.getLog(CrawlerLogicImpl.class);

	private static final ResourceBundle RESOURCE_BUNDLE_WHITE_LIST = ResourceBundle.getBundle("white_list_gome", Locale.getDefault());

	private PersistenceLogic persistenceLogic;
	
	private String html_list_category_1;
	private String html_list_category_2;
	private String html_list_category_3;
	
	private String html_list_last_page;
	private String html_list_product_block;
	private String html_list_product_a;
	private String html_detail_title;
	private String html_detail_brand;
	private String html_detail_price_json_url;
	private String html_detail_review_json_url;
	private String html_detail_good_avg;
	private String html_detail_impression_url;
	private String html_detail_bad_review_url;
	
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
				log.error(e.getMessage() + Uni_LOG_Common.Fetch_WhiteList_ERROR("GOME", url));
			}
			
			if (doc != null) {

				CategoryInfo categoryInfo = null;
				
				Elements category_1 = doc.select(html_list_category_1);
				if(category_1 != null && category_1.size() > 0){
					Element category_1_doc = category_1.get(0);
					String category_1_id = category_1_doc.attr("catgoryid").replace("cat", "");
					String category_1_name = category_1_doc.text();
					categoryInfo = new CategoryInfo(category_1_id, category_1_name, tc.getExecute_id(), "", CategoryControlStatus.UNDO.value(), "1", "");
					persistenceLogic.insertCategoryInfo(categoryInfo);
					
					Elements category_2 = doc.select(html_list_category_2);
					if(category_2 != null && category_2.size() > 0){
						Element category_2_doc = category_2.get(0);
						String category_2_id = category_2_doc.attr("catgoryid").replace("cat", "");
						String category_2_name = category_2_doc.text();
						categoryInfo = new CategoryInfo(category_2_id, category_2_name, tc.getExecute_id(), "", CategoryControlStatus.UNDO.value(), "1", category_1_id);
						persistenceLogic.insertCategoryInfo(categoryInfo);
						
						Elements category_3 = doc.select(html_list_category_3);
						if(category_3 != null && category_3.size() > 0){
							Element category_3_doc = category_3.get(0);
							String category_3_id = white_list_url.substring(white_list_url.indexOf("cat")+3, white_list_url.indexOf("-"));
							String category_3_name = category_3_doc.text();
							categoryInfo = new CategoryInfo(category_3_id, category_3_name, tc.getExecute_id(), white_list_url, CategoryControlStatus.UNDO.value(), "1", category_2_id);
							persistenceLogic.insertCategoryInfo(categoryInfo);
						}else{
							log.error(Uni_LOG_Common.Parse_Category_ERROR("GOME", url));
						}
					}else{
						log.error(Uni_LOG_Common.Parse_Category_ERROR("GOME", url));
					}
				}else{
					log.error(Uni_LOG_Common.Parse_Category_ERROR("GOME", url));
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
				log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "GOME", url_));
			}
			ProductList productList = new ProductList(catInfo.getCategory_id(), catInfo.getExecute_id(), 1, url_, ProductListUrlStatusEnum.UNDO.value()+"");
			persistenceLogic.insertProductList(productList);
			int lastPage = 0;
			if(doc != null){
				String pageCount = doc.select(html_list_last_page).text().split("/")[1];
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
		if(ebTaskInfo != null){
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
			log.info(Uni_LOG_Common.Fetch_Product_Begin("GOME", productlistURL));
			Document doc = null;
			try {
				doc = JsoupCrawler.fetchDocument(productlistURL, tc.getSite_code());
			} catch (Exception e) {
				log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "GOME", productlistURL));
			}
			if(doc != null){
				Elements product_block_elements = doc.select(html_list_product_block);
				for(Element product_block_element : product_block_elements){
					String p_id = product_block_element.attr("pid");
					String s_id = product_block_element.attr("sid");
					String url = product_block_element.select(html_list_product_a).attr("href");
					Document detailDOC = null;
					try {
						detailDOC = JsoupCrawler.fetchDocument(url, tc.getSite_code());
					} catch (Exception e) {
						log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "GOME", url));
						continue;
					}
					if(detailDOC != null){
						log.info(Uni_LOG_Common.Print_Product_ID("GOME", p_id));
						String product_title = detailDOC.select(html_detail_title).text();
						String brand_name = detailDOC.select(html_detail_brand).text();
						Document priceDOC = null;
						String priceURL = DataUtil.format(html_detail_price_json_url, new String[]{p_id, s_id});
						String priceStr = "0";
						JSONObject price_json_obj = null;
						try {
							priceDOC = JsoupCrawler.fetchDocument(priceURL, tc.getSite_code());
						} catch (Exception e) {
							log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "GOME", priceURL));
						}

						try {
							price_json_obj = new JSONObject(priceDOC.text().replaceAll("callback+\\(|\\)", ""));
							priceStr = price_json_obj.getJSONObject("result").getString("price");
						} catch (JSONException e) {
							log.error(Uni_LOG_Common.Fetch_JSON_ERROR(e.getMessage(), "GOME", priceDOC.text(), priceURL));
						}
						
						BigDecimal price = new BigDecimal(priceStr);
						

						if(product_title.contains(key_word)){
							if(p_l.floatValue() <= price.floatValue() && p_h.floatValue() >= price.floatValue()){
								int status = 0;
								String json = "";
								ProductInfo productInfo = new ProductInfo(p_id, product_title, brand_name, price, status, product_list_id, url, json);
								persistenceLogic.insertProductInfo(productInfo);
								
								int good_cnt = 0, mid_cnt = 0, bad_cnt = 0;
								Document reviewDOC = null;
								String reviewURL = DataUtil.format(html_detail_review_json_url, new String[]{p_id});
								try {
									reviewDOC = JsoupCrawler.fetchDocument(reviewURL, tc.getSite_code());
								} catch (Exception e) {
									log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "GOME", reviewURL));
								}
		
								JSONObject review_json_obj;
								try {
									review_json_obj = new JSONObject(reviewDOC.text().replaceAll("all+\\(|\\)", ""));
									good_cnt = review_json_obj.getInt("good");
									mid_cnt = review_json_obj.getInt("mid");
									bad_cnt = review_json_obj.getInt("bad");
								} catch (JSONException e) {
									log.error(Uni_LOG_Common.Fetch_JSON_ERROR(e.getMessage(), "GOME", reviewDOC.text().replaceAll("all+\\(|\\)", ""), reviewURL));
								}
								
								int goodAvg = 0;
								try {
									goodAvg = price_json_obj.getJSONObject("result").getJSONObject("appraise").getInt("goodCommentPercent");
								} catch (JSONException e) {
									log.error(Uni_LOG_Common.Fetch_JSON_ERROR(e.getMessage(), "GOME", price_json_obj.toString(), priceURL));
								}
								ReviewInfo_GOME reviewInfo = new ReviewInfo_GOME(p_id, good_cnt, mid_cnt, bad_cnt, goodAvg, "", tc.getExecute_id());
								persistenceLogic.insertReviewInfo(reviewInfo);
								
								Document impressionDOC = null;
								String impressionURL = DataUtil.format(html_detail_impression_url, new String[]{p_id});
								try {
									impressionDOC = JsoupCrawler.fetchDocument(impressionURL, tc.getSite_code());
								} catch (Exception e) {
									log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "GOME", impressionURL));
									continue;
								}
								JSONObject impression_json_obj = null; 
								try {
									int impression_id_int = 1;
									impression_json_obj = new JSONObject(impressionDOC.text().replaceAll("totleMarks+\\(|\\)", ""));
									JSONArray recocontentlist = impression_json_obj.getJSONArray("recocontentlist");
									for(int j=0; j<recocontentlist.length(); j++){
										JSONObject recocontent_json_obj = (JSONObject)recocontentlist.get(j);
										String impression_id = p_id + "_" + impression_id_int;
										String impression_content = recocontent_json_obj.getString("recocontent");
										ReviewImpression_GOME review_impression = new ReviewImpression_GOME(impression_id, p_id, impression_content, "",tc.getExecute_id());
										persistenceLogic.insertReviewImpressionInfo(review_impression);
										impression_id_int++;
									}
								} catch (JSONException e) {
									log.error(Uni_LOG_Common.Fetch_JSON_ERROR(e.getMessage(), "GOME", impressionDOC.text(), impressionURL));
								}
								
								Document badReviewDOC = null;
								String badReviewURL = DataUtil.format(html_detail_bad_review_url, new String[]{p_id});
								try {
									badReviewDOC = JsoupCrawler.fetchDocument(badReviewURL, tc.getSite_code());
								} catch (Exception e) {
									log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "GOME", badReviewURL));
									continue;
								}
								JSONObject badReview_json_obj = null;
								try {
									badReview_json_obj = new JSONObject(badReviewDOC.text().replaceAll("bad+\\(|\\)", ""));
									JSONArray evaList = badReview_json_obj.getJSONObject("evaList").getJSONArray("Evalist");
									for(int j=0; j<evaList.length(); j++){
										JSONObject eva_json_obj = evaList.getJSONObject(j);
										String review_bad_info = eva_json_obj.getString("appraiseElSum");
										Date review_bad_time = DataUtil.StrToDate(eva_json_obj.getString("post_time"), "yyyy-MM-dd HH:mm");
										ReviewBadInfo_GOME reviewBadInfo = new ReviewBadInfo_GOME(review_bad_info, review_bad_time, p_id, "", tc.getExecute_id());
										persistenceLogic.insertReviewBadInfo(reviewBadInfo);
									}
								} catch (JSONException e) {
									log.error(Uni_LOG_Common.Fetch_JSON_ERROR(e.getMessage(), "GOME", badReviewDOC.text(), badReviewURL));
								}
							}
						}
					}
				}
			}

			int product_count = persistenceLogic.getProductCountByProductListID(product_list_id);
			log.info("gome fetch product end, total product count ---->> " + product_count + " list url  ---->> " + productlistURL);
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
	 * @param log the log to set
	 */
	public static void setLog(Log log) {
		CrawlerLogicImpl.log = log;
	}

	/**
	 * @param html_list_category_1 the html_list_category_1 to set
	 */
	public void setHtml_list_category_1(String html_list_category_1) {
		this.html_list_category_1 = html_list_category_1;
	}

	/**
	 * @param html_list_category_2 the html_list_category_2 to set
	 */
	public void setHtml_list_category_2(String html_list_category_2) {
		this.html_list_category_2 = html_list_category_2;
	}

	/**
	 * @param html_list_category_3 the html_list_category_3 to set
	 */
	public void setHtml_list_category_3(String html_list_category_3) {
		this.html_list_category_3 = html_list_category_3;
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
	 * @param html_detail_title the html_detail_title to set
	 */
	public void setHtml_detail_title(String html_detail_title) {
		this.html_detail_title = html_detail_title;
	}

	/**
	 * @param html_detail_brand the html_detail_brand to set
	 */
	public void setHtml_detail_brand(String html_detail_brand) {
		this.html_detail_brand = html_detail_brand;
	}

	/**
	 * @param html_detail_price_json_url the html_detail_price_json_url to set
	 */
	public void setHtml_detail_price_json_url(String html_detail_price_json_url) {
		this.html_detail_price_json_url = html_detail_price_json_url;
	}

	/**
	 * @param html_detail_review_json_url the html_detail_review_json_url to set
	 */
	public void setHtml_detail_review_json_url(String html_detail_review_json_url) {
		this.html_detail_review_json_url = html_detail_review_json_url;
	}

	/**
	 * @param html_detail_good_avg the html_detail_good_avg to set
	 */
	public void setHtml_detail_good_avg(String html_detail_good_avg) {
		this.html_detail_good_avg = html_detail_good_avg;
	}

	/**
	 * @param html_detail_impression_url the html_detail_impression_url to set
	 */
	public void setHtml_detail_impression_url(String html_detail_impression_url) {
		this.html_detail_impression_url = html_detail_impression_url;
	}

	/**
	 * @param html_detail_bad_review_url the html_detail_bad_review_url to set
	 */
	public void setHtml_detail_bad_review_url(String html_detail_bad_review_url) {
		this.html_detail_bad_review_url = html_detail_bad_review_url;
	}

	@Override
	public void crawlCategoryList(TaskControl tc, EBTaskInfo ebTaskInfo) {
		
		String category_1 = ebTaskInfo.getCategory_level_1();
		String category_1_id = tc.getExecute_id()+"_cid_1";
		CategoryInfo level_1_category_info = new CategoryInfo(category_1_id, category_1, tc.getExecute_id(), "", CategoryControlStatus.UNDO.value(), "1", "");
		persistenceLogic.insertCategoryInfo(level_1_category_info);
		
		String category_2 = ebTaskInfo.getCategory_level_2();
		String category_2_id = tc.getExecute_id()+"_cid_2";
		CategoryInfo level_2_category_info = new CategoryInfo(category_2_id, category_2, tc.getExecute_id(), "", CategoryControlStatus.UNDO.value(), "2", category_1_id);
		persistenceLogic.insertCategoryInfo(level_2_category_info);
		
		String brand = ebTaskInfo.getBrand();
		String brand_href = ebTaskInfo.getBrand_href();
		String brand_id = brand_href.substring(brand_href.indexOf("${0}")+5, brand_href.lastIndexOf("-0-0-0-0-0-0-0-0.html"));
		CategoryInfo brand_info = new CategoryInfo(brand_id, brand, tc.getExecute_id(), brand_href, CategoryControlStatus.UNDO.value(), "3", category_2_id);
		persistenceLogic.insertCategoryInfo(brand_info);
	}

	public static void main(String[] args) {
		String url = "http://list.gome.com.cn/cat16035578-00-0-48-1-0-0-0-${0}-15WG-0-0-0-0-0-0-0-0.html";
		url = url.substring(url.indexOf("${0}")+5, url.lastIndexOf("-0-0-0-0-0-0-0-0.html"));
		System.out.println(url);
	}
}
