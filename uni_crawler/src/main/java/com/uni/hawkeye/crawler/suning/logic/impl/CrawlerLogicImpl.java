package com.uni.hawkeye.crawler.suning.logic.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.uni.hawkeye.crawler.suning.bean.CategoryInfo;
import com.uni.hawkeye.crawler.suning.bean.PriceInfo_SUNING;
import com.uni.hawkeye.crawler.suning.bean.ProductInfo;
import com.uni.hawkeye.crawler.suning.bean.ProductList;
import com.uni.hawkeye.crawler.suning.bean.ReviewBadInfo_SUNING;
import com.uni.hawkeye.crawler.suning.bean.ReviewImpression_SUNING;
import com.uni.hawkeye.crawler.suning.bean.ReviewInfo_SUNING;
import com.uni.hawkeye.crawler.suning.bean.TaskControl;
import com.uni.hawkeye.crawler.suning.enums.CategoryControlStatus;
import com.uni.hawkeye.crawler.suning.enums.PriceEnum;
import com.uni.hawkeye.crawler.suning.enums.ProductListUrlStatusEnum;
import com.uni.hawkeye.crawler.suning.logic.CrawlerLogic;
import com.uni.hawkeye.crawler.suning.logic.PersistenceLogic;
import com.uni.hawkeye.jsoup.JsoupCrawler;
import com.uni.hawkeye.log.Uni_LOG_Common;
import com.uni.hawkeye.utils.DataUtil;

import uni_hawkeye.core.EBTaskInfo;

public class CrawlerLogicImpl implements CrawlerLogic {
	
	private static Log log = LogFactory.getLog(CrawlerLogicImpl.class);

	private static final ResourceBundle RESOURCE_BUNDLE_WHITE_LIST = ResourceBundle.getBundle("white_list_suning", Locale.getDefault());

	private PersistenceLogic persistenceLogic;
	
	private Pattern p = Pattern.compile("\\d+");
	
	private String html_list_navigation_path;
	private String html_list_navigation_current;
	private String html_list_last_page;
	private String html_list_product_block;
	private String html_list_title;
	private String html_detail_title;
	private String html_detail_brand;
	private String html_detail_price_json;
	private String html_detail_review_info;
	private String html_detail_impression;
	private String html_detail_bad_review_info;

	@Override
	public void crawlCategoryList(TaskControl tc) {
		Iterator<String> white_list = RESOURCE_BUNDLE_WHITE_LIST.keySet().iterator();
		while (white_list.hasNext()) {
			String white_list_url = RESOURCE_BUNDLE_WHITE_LIST.getString(white_list.next());
			Document doc = null;
			String url = DataUtil.format(white_list_url, new String[]{"0"});
			try {
				doc = JsoupCrawler.fetchDocument(url, tc.getSite_code());
			} catch (Exception e) {
				log.error(e.getMessage() + Uni_LOG_Common.Fetch_WhiteList_ERROR("SUNING", url));
			}
			
			if (doc != null) {
				Elements category_before = doc.select(html_list_navigation_path);
				int level = 1;
				String category_parent_id = "";
				for(Element a : category_before){
					String category_code = a.attr("id");
					String category_name = a.text();
					if(StringUtil.isBlank(category_code)){
						category_code = category_name;
					}
					CategoryInfo categoryInfo = new CategoryInfo(category_code, category_name, tc.getExecute_id(), "", CategoryControlStatus.UNDO.value(), level+"", category_parent_id);
					persistenceLogic.insertCategoryInfo(categoryInfo);
					level++;
					category_parent_id = category_code;
				}
				Element category_current = doc.select(html_list_navigation_current).get(0);
				String category_code = white_list_url.substring(white_list_url.indexOf("-")+1, white_list_url.lastIndexOf("-"));
				String category_name = category_current.text();
				CategoryInfo categoryInfo = new CategoryInfo(category_code, category_name, tc.getExecute_id(), white_list_url, CategoryControlStatus.UNDO.value(), level+"", category_parent_id);
				persistenceLogic.insertCategoryInfo(categoryInfo);
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
			String url_tmp = DataUtil.format(url, new String[]{"0"});
			Document doc = null;
			try {
				doc = JsoupCrawler.fetchDocument(url_tmp, tc.getSite_code());
			} catch (Exception e) {
				log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "SUNING", url_tmp));
			}
			ProductList productList = new ProductList(catInfo.getCategory_id(), catInfo.getExecute_id(), 1, url_tmp, ProductListUrlStatusEnum.UNDO.value()+"");
			persistenceLogic.insertProductList(productList);
			int lastPage = 0;
			if(doc != null){
				String pageCount = doc.select(html_list_last_page).text();
				pageCount = pageCount.split("/")[1];
				lastPage = Integer.valueOf(pageCount.trim());
			}
			for(int j=1; j<=lastPage; j++){
				productList = new ProductList(catInfo.getCategory_id(), catInfo.getExecute_id(), j+1, DataUtil.format(url, new String[]{j+""}), ProductListUrlStatusEnum.UNDO.value()+"");
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
			log.info(Uni_LOG_Common.Fetch_Product_Begin("SUNING", productlistURL));
			Document doc = null;
			try {
				doc = JsoupCrawler.fetchDocument(productlistURL, tc.getSite_code());
			} catch (Exception e) {
				log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "SUNING", productlistURL));
			}
			if(doc != null){
				Elements product_block_elements = doc.select(html_list_product_block);
				for(Element product_block_element : product_block_elements){
					Element titleElement = product_block_element.select(html_list_title).get(0);
					String product_id = titleElement.attr("href");
					product_id = product_id.substring(product_id.lastIndexOf("/")+1, product_id.lastIndexOf(".html"));
					String url = titleElement.attr("href");
					Document detailDOC = null;
					try {
						detailDOC = JsoupCrawler.fetchDocument(url, tc.getSite_code());
					} catch (Exception e) {
						log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "SUNING", url));
						continue;
					}
					if(detailDOC != null){
						log.info(Uni_LOG_Common.Print_Product_ID("SUNING", product_id));
						String product_title = detailDOC.select(html_detail_title).text();
						if(StringUtil.isBlank(product_title)){
							product_title = product_block_element.select(html_list_title).get(0).text();
						}
						if (product_title.contains(key_word)) {
							String product_brand = detailDOC.select(html_detail_brand).text();
							int status = 0;
							String json = "";
							
							Document priceDOC = null;
							String price_json_url = DataUtil.format(html_detail_price_json, new String[]{product_id});
							try {
								priceDOC = JsoupCrawler.fetchDocument(price_json_url, tc.getSite_code());
							} catch (Exception e) {
								log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "SUNING", price_json_url));
							}
							List<PriceInfo_SUNING> priceList = new ArrayList<PriceInfo_SUNING>();
							BigDecimal price_promotion = new BigDecimal(0);
							BigDecimal price_net = new BigDecimal(0);
							int x = 2;
							if(priceDOC != null){
								String price_json = priceDOC.text().replaceAll("showSaleStatus+\\(|\\);", "");
								JSONObject price_json_obj = null;
								try {
									price_json_obj = new JSONObject(price_json);
									price_json_obj = price_json_obj.getJSONArray("saleInfo").getJSONObject(0);
									PriceInfo_SUNING priceInfo_SUNING = null;
									String promotionPrice = price_json_obj.getString("promotionPrice");
									if(!StringUtil.isBlank(promotionPrice)){
										price_promotion = new BigDecimal(promotionPrice);
										priceInfo_SUNING = new PriceInfo_SUNING(product_id, product_list_id, PriceEnum.promotionPrice.value(), price_promotion);
										priceList.add(priceInfo_SUNING);
									}else{
										x--;
									}
									String netPrice = price_json_obj.getString("netPrice");
									if(!StringUtil.isBlank(netPrice)){
										price_net = new BigDecimal(netPrice);
										priceInfo_SUNING = new PriceInfo_SUNING(product_id, product_list_id, PriceEnum.netPrice.value(), price_net);
										priceList.add(priceInfo_SUNING);
									}else{
										x--;
									}
								} catch (JSONException e) {
									log.error(Uni_LOG_Common.Fetch_JSON_ERROR(e.getMessage(), "SUNING", price_json, price_json_url));
								}
							}
							
							if(x == 0){
								x = 1;
							}
							float avg_price = (price_promotion.floatValue() + price_net.floatValue()) / x;
							
							if (p_l.floatValue() <= avg_price && p_h.floatValue() >= avg_price) {
								ProductInfo productInfo = new ProductInfo(product_id, product_title, product_brand, status, product_list_id, url, json);
								persistenceLogic.insertProductInfo(productInfo);
								
								for(PriceInfo_SUNING priceInfo : priceList){
									persistenceLogic.insertPriceInfo(priceInfo);
								}
								
								Document reviewDOC = null;
								String review_json_url = DataUtil.format(html_detail_review_info, new String[]{product_id});
								try {
									reviewDOC = JsoupCrawler.fetchDocument(review_json_url, tc.getSite_code());
								} catch (Exception e) {
									log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "SUNING", review_json_url));
								}
								if(reviewDOC != null){
									String review_json_str = reviewDOC.text();
									if(!StringUtil.isBlank(review_json_str)){
										JSONObject review_json_obj = null;
										JSONArray review_json_arr = null;
										try {
											review_json_obj = new JSONObject(review_json_str.replaceAll("satisfy+\\(|\\)", ""));
											review_json_arr = review_json_obj.getJSONArray("reviewCounts");
											JSONObject review_json = review_json_arr.getJSONObject(0);
											int oneStarCount = review_json.getInt("oneStarCount");
											int twoStarCount = review_json.getInt("twoStarCount");
											int threeStarCount = review_json.getInt("threeStarCount");
											int fourStarCount = review_json.getInt("fourStarCount");
											int fiveStarCount = review_json.getInt("fiveStarCount");
											int totalCount = review_json.getInt("totalCount");
											
											int good_cnt = fiveStarCount + fourStarCount;
											int general_cnt = threeStarCount + twoStarCount;
											int poor_cnt = oneStarCount;
											float review_grade_avg = 0;
											if(totalCount > 0){
												review_grade_avg = Float.valueOf(good_cnt) / Float.valueOf(totalCount);
											}
											BigDecimal b = new BigDecimal(review_grade_avg);
											review_grade_avg = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
											
											ReviewInfo_SUNING reviewInfo_SUNING = new ReviewInfo_SUNING(product_id, good_cnt, general_cnt, poor_cnt, review_grade_avg, review_json_str, tc.getExecute_id());
											persistenceLogic.insertReviewInfo(reviewInfo_SUNING);
										} catch (JSONException e) {
											log.error(Uni_LOG_Common.Fetch_JSON_ERROR(e.getMessage(), "SUNING", review_json_str.replaceAll("satisfy+\\(|\\)", ""), review_json_url));
										}
									}
								}
								
								Document impressionDOC = null;
								String impression_json_url = DataUtil.format(html_detail_impression, new String[]{product_id});
								try {
									impressionDOC = JsoupCrawler.fetchDocument(impression_json_url, tc.getSite_code());
								} catch (Exception e) {
									log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "SUNING", impression_json_url));
								}
								if(impressionDOC != null){
									String impression_json_str = impressionDOC.text();
									if(!StringUtil.isBlank(impression_json_str)){
										JSONObject impression_json_obj = null;
										JSONArray impression_json_arr = null;
										try {
											impression_json_obj = new JSONObject(impression_json_str.replaceAll("commodityrLabels+\\(|\\)", ""));
											if(impression_json_obj.has("commodityLabelCountList")){
												impression_json_arr = impression_json_obj.getJSONArray("commodityLabelCountList");
												for(int j=0; j<impression_json_arr.length(); j++){
													JSONObject impression_json_sub = impression_json_arr.getJSONObject(j);
													String impression_content = impression_json_sub.getString("labelName");
													int impression_cnt = impression_json_sub.getInt("labelCnt");
													String impression_id = product_id + "_" + j;
													ReviewImpression_SUNING reviewImpression_SUNING = new ReviewImpression_SUNING(impression_id, product_id, impression_content, impression_cnt, impression_json_str, tc.getExecute_id()); 
													persistenceLogic.insertReviewImpressionInfo(reviewImpression_SUNING);
												}
											}
										} catch (JSONException e) {
											log.error(Uni_LOG_Common.Fetch_JSON_ERROR(e.getMessage(), "SUNING", impression_json_str.replaceAll("commodityrLabels+\\(|\\)", ""), impression_json_url));
										}
									}
								}
								
								Document badReviewDOC = null;
								String bad_review_page_url = DataUtil.format(html_detail_bad_review_info, new String[]{product_id});
								try {
									badReviewDOC = JsoupCrawler.fetchDocument(bad_review_page_url, tc.getSite_code());
								} catch (Exception e) {
									log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "SUNING", bad_review_page_url));
								}
								if(badReviewDOC != null){
									String badReview_json_str = badReviewDOC.text();
									if(!StringUtil.isBlank(badReview_json_str)){
										JSONObject badReview_json_obj = null;
										JSONArray badReview_json_arr = null;
										try {
											badReview_json_obj = new JSONObject(badReview_json_str.replaceAll("reviewList+\\(|\\)", ""));
											badReview_json_arr = badReview_json_obj.getJSONArray("commodityReviews");
											for(int j=0; j<badReview_json_arr.length(); j++){
												JSONObject badReview = badReview_json_arr.getJSONObject(j);
												String review_bad_info = badReview.getString("content");
												Date review_bad_time = DataUtil.StrToDate(badReview.getString("publishTime"), "yyyy-MM-dd HH:mm:ss");
												ReviewBadInfo_SUNING reviewBadInfo_SUNING = new ReviewBadInfo_SUNING(review_bad_info, review_bad_time, product_id, badReview_json_str, tc.getExecute_id());
												persistenceLogic.insertReviewBadInfo(reviewBadInfo_SUNING);
											}
										} catch (JSONException e) {
											log.error(Uni_LOG_Common.Fetch_JSON_ERROR(e.getMessage(), "SUNING", badReview_json_str.replaceAll("reviewList+\\(|\\)", ""), bad_review_page_url));
										}
									}
								}
							}
						}
					}
				}
			}

			int product_count = persistenceLogic.getProductCountByProductListID(product_list_id);
			log.info("suning fetch product end, total product count ---->> " + product_count + " list url  ---->> " + productlistURL);
			if(product_count > 0){
				productList.setStatus(ProductListUrlStatusEnum.CRAWLING_FINISHED.value()+"");
			}else{
				productList.setStatus(ProductListUrlStatusEnum.EMPTY.value()+"");
			}
			persistenceLogic.undateProductList(productList);
		}
	}

	/**
	 * @param persistenceLogic the persistenceLogic to set
	 */
	public void setPersistenceLogic(PersistenceLogic persistenceLogic) {
		this.persistenceLogic = persistenceLogic;
	}

	/**
	 * @param html_list_navigation_path the html_list_navigation_path to set
	 */
	public void setHtml_list_navigation_path(String html_list_navigation_path) {
		this.html_list_navigation_path = html_list_navigation_path;
	}

	/**
	 * @param html_list_navigation_current the html_list_navigation_current to set
	 */
	public void setHtml_list_navigation_current(String html_list_navigation_current) {
		this.html_list_navigation_current = html_list_navigation_current;
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
	 * @param html_detail_brand the html_detail_brand to set
	 */
	public void setHtml_detail_brand(String html_detail_brand) {
		this.html_detail_brand = html_detail_brand;
	}

	/**
	 * @param html_detail_price_json the html_detail_price_json to set
	 */
	public void setHtml_detail_price_json(String html_detail_price_json) {
		this.html_detail_price_json = html_detail_price_json;
	}

	/**
	 * @param html_detail_review_info the html_detail_review_info to set
	 */
	public void setHtml_detail_review_info(String html_detail_review_info) {
		this.html_detail_review_info = html_detail_review_info;
	}

	/**
	 * @param html_detail_impression the html_detail_impression to set
	 */
	public void setHtml_detail_impression(String html_detail_impression) {
		this.html_detail_impression = html_detail_impression;
	}

	/**
	 * @param html_detail_bad_review_info the html_detail_bad_review_info to set
	 */
	public void setHtml_detail_bad_review_info(String html_detail_bad_review_info) {
		this.html_detail_bad_review_info = html_detail_bad_review_info;
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
		String brand_id = brand_href.split("-")[1];
		CategoryInfo brand_info = new CategoryInfo(brand_id, brand, tc.getExecute_id(), brand_href, CategoryControlStatus.UNDO.value(), "3", category_2_id);
		persistenceLogic.insertCategoryInfo(brand_info);
	}

}
