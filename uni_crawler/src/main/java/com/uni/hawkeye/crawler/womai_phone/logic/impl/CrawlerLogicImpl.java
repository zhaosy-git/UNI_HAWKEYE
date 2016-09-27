package com.uni.hawkeye.crawler.womai_phone.logic.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
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

import com.uni.hawkeye.crawler.womai_phone.bean.ReviewBadInfo_WOMAI;
import com.uni.hawkeye.crawler.womai_phone.bean.ReviewInfo_WOMAI;
import com.uni.hawkeye.crawler.womai_phone.bean.CategoryInfo;
import com.uni.hawkeye.crawler.womai_phone.bean.PriceInfo_WOMAI;
import com.uni.hawkeye.crawler.womai_phone.bean.ProductInfo;
import com.uni.hawkeye.crawler.womai_phone.bean.ProductList;
import com.uni.hawkeye.crawler.womai_phone.bean.TaskControl;
import com.uni.hawkeye.crawler.womai_phone.enums.CategoryControlStatus;
import com.uni.hawkeye.crawler.womai_phone.enums.PriceEnum;
import com.uni.hawkeye.crawler.womai_phone.enums.ProductListUrlStatusEnum;
import com.uni.hawkeye.crawler.womai_phone.logic.CrawlerLogic;
import com.uni.hawkeye.crawler.womai_phone.logic.PersistenceLogic;
import com.uni.hawkeye.jsoup.JsoupCrawler;
import com.uni.hawkeye.log.Uni_LOG_Common;
import com.uni.hawkeye.utils.DataUtil;

public class CrawlerLogicImpl implements CrawlerLogic {
	
	private static Log log = LogFactory.getLog(CrawlerLogicImpl.class);

	private static final ResourceBundle RESOURCE_BUNDLE_WHITE_LIST = ResourceBundle.getBundle("white_list_womai_phone", Locale.getDefault());
	
	Pattern p = Pattern.compile("(?<=0c)[^<]+(?=.shtml)");
	Pattern p_brand = Pattern.compile("(?<=品牌：)[^<]+(?=规格)");

	private PersistenceLogic persistenceLogic;
	
	private String womai_phone_category_name;
	
	private String womai_phone_html_list_last_page;
	private String womai_phone_html_list_block;
	private String womai_phone_html_product_href;
	private String womai_phone_detail_title;
	private String womai_phone_detail_price;
	private String womai_phone_detail_brand;
	private String womai_phone_detail_brand_1;
	private String womai_phone_detail_jump_url;
	private String womai_phone_detail_price_type;
	private String womai_phone_detail_review_url;
	private String womai_phone_detail_review_referer;
	
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
				log.error(e.getMessage() + Uni_LOG_Common.Fetch_WhiteList_ERROR("WOMAI_PHONE", url));
			}
			
			if (doc != null) {
				Matcher m = p.matcher(white_list_url);
				if(m.find()){
					String category_code = m.group(0);
					String category_name = doc.select(womai_phone_category_name).text();
					if(!StringUtil.isBlank(category_name)){
						category_name = category_name.substring(0, category_name.indexOf("("));
						int level = 3;
						String category_parent_id = "";
						CategoryInfo categoryInfo = new CategoryInfo(category_code, category_name, tc.getExecute_id(), white_list_url, CategoryControlStatus.UNDO.value(), level+"", category_parent_id);
						persistenceLogic.insertCategoryInfo(categoryInfo);
					}else{
						log.error(Uni_LOG_Common.Parse_Category_ERROR("WOMAI_PHONE", url));
					}
				}else{
					log.error(Uni_LOG_Common.Parse_Category_ERROR("WOMAI_PHONE", url));
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
				log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "WOMAI_PHONE", url_));
			}
			ProductList productList = new ProductList(catInfo.getCategory_id(), catInfo.getExecute_id(), 1, url_, ProductListUrlStatusEnum.UNDO.value()+"");
			persistenceLogic.insertProductList(productList);
			int lastPage = 0;
			if(doc != null){
				String pageCount = doc.select(womai_phone_html_list_last_page).text();
				lastPage = pageCount.split(" ").length;
			}
			for(int j=2; j<=lastPage; j++){
				productList = new ProductList(catInfo.getCategory_id(), catInfo.getExecute_id(), j, DataUtil.format(url, new String[]{j+""}), ProductListUrlStatusEnum.UNDO.value()+"");
				persistenceLogic.insertProductList(productList);
			}
			persistenceLogic.updateCategoryInfoStatus(catInfo.getCategory_id(), CategoryControlStatus.CRAWLING_FINISHED.value());
		}
	}
	@Override
	public void crawlerProduct(TaskControl tc) {
		List<ProductList> productLists = persistenceLogic.getProductListByStatus(tc.getExecute_id(), ProductListUrlStatusEnum.UNDO.value()+"");
		for(int i=0; i<productLists.size(); i++){
			ProductList productList = productLists.get(i);
			productList.setStatus(ProductListUrlStatusEnum.CRAWLING.value()+"");
			persistenceLogic.undateProductList(productList);
			String productlistURL = productList.getUrl();
			int product_list_id = productList.getProduct_list_id();
			log.info(Uni_LOG_Common.Fetch_Product_Begin("WOMAI_PHONE", productlistURL));
			Document doc = null;
			try {
				doc = JsoupCrawler.fetchDocument(productlistURL, tc.getSite_code());
			} catch (Exception e) {
				log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "WOMAI_PHONE", productlistURL));
			}
			if(doc != null){
				Elements blocks = doc.select(womai_phone_html_list_block);
				if(blocks != null && blocks.size() > 0){
					for(Element block : blocks){
						String product_id = block.attr("pid");
						log.info(Uni_LOG_Common.Print_Product_ID("WOMAI_PHONE", product_id));
						String href = block.select(womai_phone_html_product_href).attr("href");
						Document productDoc = null;
						try {
							productDoc = JsoupCrawler.fetchDocument(href, tc.getSite_code());
						} catch (Exception e) {
							log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "WOMAI_PHONE", href));
						}
						String product_title = "";
						BigDecimal price;
						String priceStr = "0";
						int price_type = 0;
						String brand_name = "";
						if(productDoc != null){
							product_title = productDoc.select(womai_phone_detail_title).text();
							Elements prices = productDoc.select(womai_phone_detail_price);
							if(prices != null && prices.size() > 0){
								priceStr = prices.text();
								if(priceStr.indexOf("￥") != -1){
									priceStr = priceStr.replace("￥", "");
								}
							}
							price = new BigDecimal(priceStr);
							int status = 0;
							String json = "";
							brand_name = fetchBrand(productDoc);
							
							if(StringUtil.isBlank(brand_name)){
								Elements goToSingleUrls = productDoc.select(womai_phone_detail_jump_url);
								if(goToSingleUrls != null && goToSingleUrls.size() > 0){
									String goToSingleUrl = goToSingleUrls.attr("href");
									Document singleDoc = null;
									try {
										singleDoc = JsoupCrawler.fetchDocument(goToSingleUrl, tc.getSite_code());
									} catch (Exception e) {
										log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "WOMAI_PHONE", goToSingleUrl));
									}
									brand_name = fetchBrand(singleDoc);
								}
							}
							
							ProductInfo productInfo = new ProductInfo(product_id, product_title, brand_name, status, product_list_id, href, json);
							persistenceLogic.insertProductInfo(productInfo);
							
							String priceTypeBlock = productDoc.select(womai_phone_detail_price_type).text();
							if(priceTypeBlock.contains(PriceEnum.VIP.name())){
								price_type = PriceEnum.VIP.value();
							} else if(priceTypeBlock.contains(PriceEnum.市场.name())){
								price_type = PriceEnum.市场.value();
							} else if(priceTypeBlock.contains(PriceEnum.抢购.name())){
								price_type = PriceEnum.抢购.value();
							} else if(priceTypeBlock.contains(PriceEnum.积分.name())){
								price_type = PriceEnum.积分.value();
							} else if(priceTypeBlock.contains(PriceEnum.节省了.name())){
								price_type = PriceEnum.节省了.value();
							} else if(priceTypeBlock.contains(PriceEnum.零售.name())){
								price_type = PriceEnum.零售.value();
							} else{
								price_type = 0;
							}
							PriceInfo_WOMAI priceInfo_WOMAI = new PriceInfo_WOMAI(product_id, price_type, price, product_list_id);
							persistenceLogic.insertPriceInfo(priceInfo_WOMAI);
						}
						
						Document reviewDoc = null;
						String reviewURL = DataUtil.format(womai_phone_detail_review_url, new String[]{product_id});
						try {
							JsoupCrawler.setReferrer(DataUtil.format(womai_phone_detail_review_referer, new String[]{product_id}));
							reviewDoc = JsoupCrawler.fetchDocument(reviewURL, tc.getSite_code());
						} catch (Exception e) {
							log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "WOMAI_PHONE", reviewURL));
						}
						if(reviewDoc != null){
							JSONObject review_json_obj = null;
							JSONArray review_json_arr = null;
							try {
								String reviewStr = reviewDoc.text();
								if(!StringUtil.isBlank(reviewStr)){
									review_json_obj = new JSONObject(reviewStr);
									String review_grade_avg = review_json_obj.getString("total_point");
									if(!StringUtil.isBlank(review_grade_avg)){
										review_grade_avg = review_grade_avg.replace("分", "");
									}
									int good_cnt = review_json_obj.getInt("good_count");
									int general_cnt = review_json_obj.getInt("middle_count");
									int poor_cnt = review_json_obj.getInt("poor_count");
									ReviewInfo_WOMAI reviewInfo = new ReviewInfo_WOMAI(product_id, good_cnt, general_cnt, poor_cnt, Float.valueOf(review_grade_avg), "", tc.getExecute_id());
									persistenceLogic.insertReviewInfo(reviewInfo);
									
									review_json_arr = review_json_obj.getJSONArray("questionlist");
									for(int j=0; j<review_json_arr.length(); j++){
										JSONObject review = review_json_arr.getJSONObject(j);
										String review_bad_info = review.getString("comment");
										Date review_bad_time = DataUtil.StrToDate(review.getString("commenttime"), "yyyy-MM-dd HH:mm");
										ReviewBadInfo_WOMAI reviewBadInfo = new ReviewBadInfo_WOMAI(review_bad_info, review_bad_time, product_id, "", tc.getExecute_id());
										persistenceLogic.insertReviewBadInfo(reviewBadInfo);
									}
								}
							} catch (JSONException e) {
								log.error(Uni_LOG_Common.Fetch_JSON_ERROR(e.getMessage(), "WOMAI_PHONE", reviewDoc.text(), reviewURL));
							}
						}
					}
				}
				
				
			}

			int product_count = persistenceLogic.getProductCountByProductListID(product_list_id);
			log.info("womai_phone fetch product end, total product count ---->> " + product_count + " list url  ---->> " + productlistURL);
			if(product_count > 0){
				productList.setStatus(ProductListUrlStatusEnum.CRAWLING_FINISHED.value()+"");
			}else{
				productList.setStatus(ProductListUrlStatusEnum.EMPTY.value()+"");
			}
			persistenceLogic.undateProductList(productList);
		}
	}

	private String fetchBrand(Document productDoc) {
		String brand_name = "";
		if(productDoc != null){
			brand_name = productDoc.select(womai_phone_detail_brand).text();
			Matcher m_brand = p_brand.matcher(brand_name);
			if(m_brand.find()){
				brand_name = m_brand.group(0);
			}
			
			if(StringUtil.isBlank(brand_name)){
				brand_name = productDoc.select(womai_phone_detail_brand_1).text();
				m_brand = p_brand.matcher(brand_name);
				if(m_brand.find()){
					brand_name = m_brand.group(0);
				}
			}
		}
		return brand_name;
	}

	/**
	 * @param persistenceLogic
	 *            the persistenceLogic to set
	 */
	public void setPersistenceLogic(PersistenceLogic persistenceLogic) {
		this.persistenceLogic = persistenceLogic;
	}

	/**
	 * @param womai_phone_category_name the womai_phone_category_name to set
	 */
	public void setWomai_phone_category_name(String womai_phone_category_name) {
		this.womai_phone_category_name = womai_phone_category_name;
	}

	/**
	 * @param womai_phone_html_list_last_page the womai_phone_html_list_last_page to set
	 */
	public void setWomai_phone_html_list_last_page(String womai_phone_html_list_last_page) {
		this.womai_phone_html_list_last_page = womai_phone_html_list_last_page;
	}

	/**
	 * @param womai_phone_html_list_block the womai_phone_html_list_block to set
	 */
	public void setWomai_phone_html_list_block(String womai_phone_html_list_block) {
		this.womai_phone_html_list_block = womai_phone_html_list_block;
	}

	/**
	 * @param womai_phone_html_product_href the womai_phone_html_product_href to set
	 */
	public void setWomai_phone_html_product_href(String womai_phone_html_product_href) {
		this.womai_phone_html_product_href = womai_phone_html_product_href;
	}

	/**
	 * @param womai_phone_detail_title the womai_phone_detail_title to set
	 */
	public void setWomai_phone_detail_title(String womai_phone_detail_title) {
		this.womai_phone_detail_title = womai_phone_detail_title;
	}

	/**
	 * @param womai_phone_detail_price the womai_phone_detail_price to set
	 */
	public void setWomai_phone_detail_price(String womai_phone_detail_price) {
		this.womai_phone_detail_price = womai_phone_detail_price;
	}

	/**
	 * @param womai_phone_detail_brand the womai_phone_detail_brand to set
	 */
	public void setWomai_phone_detail_brand(String womai_phone_detail_brand) {
		this.womai_phone_detail_brand = womai_phone_detail_brand;
	}

	/**
	 * @param womai_phone_detail_brand_1 the womai_phone_detail_brand_1 to set
	 */
	public void setWomai_phone_detail_brand_1(String womai_phone_detail_brand_1) {
		this.womai_phone_detail_brand_1 = womai_phone_detail_brand_1;
	}

	/**
	 * @param womai_phone_detail_jump_url the womai_phone_detail_jump_url to set
	 */
	public void setWomai_phone_detail_jump_url(String womai_phone_detail_jump_url) {
		this.womai_phone_detail_jump_url = womai_phone_detail_jump_url;
	}

	/**
	 * @param p_brand the p_brand to set
	 */
	public void setP_brand(Pattern p_brand) {
		this.p_brand = p_brand;
	}

	/**
	 * @param womai_phone_detail_price_type the womai_phone_detail_price_type to set
	 */
	public void setWomai_phone_detail_price_type(String womai_phone_detail_price_type) {
		this.womai_phone_detail_price_type = womai_phone_detail_price_type;
	}

	/**
	 * @param womai_phone_detail_review_url the womai_phone_detail_review_url to set
	 */
	public void setWomai_phone_detail_review_url(String womai_phone_detail_review_url) {
		this.womai_phone_detail_review_url = womai_phone_detail_review_url;
	}

	/**
	 * @param womai_phone_detail_review_referer the womai_phone_detail_review_referer to set
	 */
	public void setWomai_phone_detail_review_referer(String womai_phone_detail_review_referer) {
		this.womai_phone_detail_review_referer = womai_phone_detail_review_referer;
	}

}
