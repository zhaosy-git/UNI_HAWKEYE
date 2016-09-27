package com.uni.hawkeye.crawler.jd.logic.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.uni.hawkeye.crawler.jd.bean.CategoryInfo;
import com.uni.hawkeye.crawler.jd.bean.ProductInfo;
import com.uni.hawkeye.crawler.jd.bean.ProductList;
import com.uni.hawkeye.crawler.jd.bean.ReviewBadInfo_JD;
import com.uni.hawkeye.crawler.jd.bean.ReviewImpression_JD;
import com.uni.hawkeye.crawler.jd.bean.ReviewInfo_JD;
import com.uni.hawkeye.crawler.jd.bean.TaskControl;
import com.uni.hawkeye.crawler.jd.enums.CategoryControlStatus;
import com.uni.hawkeye.crawler.jd.enums.ProductListUrlStatusEnum;
import com.uni.hawkeye.crawler.jd.logic.CrawlerLogic;
import com.uni.hawkeye.crawler.jd.logic.PersistenceLogic;
import com.uni.hawkeye.enums.WebSiteEnum;
import com.uni.hawkeye.jsoup.JsoupCrawler;
import com.uni.hawkeye.jsoup.bean.CrawlerPropertyInfo;
import com.uni.hawkeye.log.Uni_LOG_Common;
import com.uni.hawkeye.utils.DataUtil;

import uni_hawkeye.core.EBTaskInfo;

public class CrawlerLogicImpl implements CrawlerLogic {

	private static Log log = LogFactory.getLog(CrawlerLogicImpl.class);
	
	private static final ResourceBundle RESOURCE_BUNDLE_WHITE_LIST = ResourceBundle.getBundle("white_list_jd", Locale.getDefault());

	private PersistenceLogic persistenceLogic;

	private com.uni.hawkeye.jsoup.logic.PersistenceLogic persistenceLogic_jsoup;
	
	private int crawler_hour;
	
	private String html_category_navigation;
	private String html_category_level_1;
	private String html_category_level_2;
	private String html_category_level_3;
	private String html_list_last_page;
	private String html_list_product_block;
	private String html_list_product_detail_url;
	private String html_detail_title;
	private String html_detail_price_json_url;
	private String html_detail_brand;
	private String html_review_info_json_url;

	@Override
	public void crawlCategoryList(TaskControl tc) {
		Iterator<String> white_list = RESOURCE_BUNDLE_WHITE_LIST.keySet().iterator();
		while(white_list.hasNext()){
			String white_list_url = RESOURCE_BUNDLE_WHITE_LIST.getString(white_list.next());
			String url = DataUtil.format(white_list_url, new String[]{"1"});
			Document doc = null;
			try {
				doc = JsoupCrawler.fetchDocument(url, tc.getSite_code());
			} catch (Exception e) {
				log.error(e.getMessage() + Uni_LOG_Common.Fetch_WhiteList_ERROR("JD", url));
			}
			Map<String, String> catParam = DataUtil.getParam(url);
			String categoryArray = catParam.get("cat");
			if(doc != null){
				Elements navigations = doc.select(html_category_navigation);
				if(navigations != null && navigations.size() > 0){
					Element navigation = navigations.get(0);
					Elements level_1s = navigation.select(html_category_level_1);
					if(level_1s != null && level_1s.size() > 0){
						Element level_1 = level_1s.get(0);
						String level_1_cat = categoryArray.split(",")[0];
						CategoryInfo level_1_category_info = new CategoryInfo(level_1_cat, level_1.text(), tc.getExecute_id(), "",CategoryControlStatus.UNDO.value(), "1", "");
						persistenceLogic.insertCategoryInfo(level_1_category_info);
						
						Element level_2 = navigation.select(html_category_level_2).get(0);
						if(level_2 != null){
							String level_2_cat = categoryArray.split(",")[1];
							CategoryInfo level_2_category_info = new CategoryInfo(level_2_cat, level_2.text(), tc.getExecute_id(), "",CategoryControlStatus.UNDO.value(), "2", level_1_cat);
							persistenceLogic.insertCategoryInfo(level_2_category_info);
							
							Element level_3 = navigation.select(html_category_level_3).get(0);
							if(level_3 != null){
								String level_3_cat = categoryArray.split(",")[2];
								CategoryInfo level_3_category_info = new CategoryInfo(level_3_cat, level_3.text(), tc.getExecute_id(), white_list_url,CategoryControlStatus.UNDO.value(), "3", level_2_cat);
								persistenceLogic.insertCategoryInfo(level_3_category_info);
							}else{
								log.error(Uni_LOG_Common.Parse_Category_ERROR("JD", white_list_url));
							}
						}else{
							log.error(Uni_LOG_Common.Parse_Category_ERROR("JD", white_list_url));
						}
					}else{
						log.error(Uni_LOG_Common.Parse_Category_ERROR("JD", white_list_url));
					}
				}else{
					log.error(Uni_LOG_Common.Parse_Category_ERROR("JD", white_list_url));
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
			String url = DataUtil.format(catInfo.getCategory_url(), new String[]{"1"});
			Document doc = null;
			try {
				doc = JsoupCrawler.fetchDocument(url, tc.getSite_code());
			} catch (Exception e) {
				log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "JD", url));
			}
			persistenceLogic.updateCategoryInfoStatus(catInfo.getCategory_id(), catInfo.getExecute_id(), CategoryControlStatus.CRAWLING.value());
			ProductList productList = new ProductList(catInfo.getCategory_id(), catInfo.getExecute_id(), 1, DataUtil.format(url, new String[]{"1"}), ProductListUrlStatusEnum.UNDO.value()+"");
			persistenceLogic.insertProductList(productList);
			int lastPage = 0;
			if(doc != null){
				lastPage = Integer.valueOf(doc.select(html_list_last_page).text());
			}
			for(int j=2; j<=lastPage; j++){
				productList = new ProductList(catInfo.getCategory_id(), catInfo.getExecute_id(), j, DataUtil.format(catInfo.getCategory_url(), new String[]{j+""}), ProductListUrlStatusEnum.UNDO.value()+"");
				persistenceLogic.insertProductList(productList);
			}
			persistenceLogic.updateCategoryInfoStatus(catInfo.getCategory_id(), catInfo.getExecute_id(), CategoryControlStatus.CRAWLING_FINISHED.value());
		}
	}
	
	@Deprecated
	@Override
	public void crawlerProduct(TaskControl tc, Date startTime) {
		List<ProductList> productLists = persistenceLogic.getProductListByStatus(tc.getExecute_id(), ProductListUrlStatusEnum.UNDO.value()+"");
		for(int i=0; i<productLists.size(); i++){
			if(DataUtil.dateDiff(startTime, new Date()) < crawler_hour){
				ProductList productList = productLists.get(i);
				int product_list_id = productList.getProduct_list_id();
				productList.setStatus(ProductListUrlStatusEnum.CRAWLING.value()+"");
				persistenceLogic.undateProductList(productList);
				String productlistURL = productList.getUrl();
				log.info(Uni_LOG_Common.Fetch_Product_Begin("JD", productlistURL));
				Document doc = null;
				try {
					doc = JsoupCrawler.fetchDocument(productlistURL, tc.getSite_code());
				} catch (Exception e) {
					log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "JD", productlistURL));
				}
				if(doc != null){
					Elements product_detail_url_elements = doc.select(html_list_product_detail_url);
					for(Element detail_url : product_detail_url_elements){
						String url = detail_url.attr("href");
						if(url.indexOf("http:") == -1){
							url = "http:" + url;
						}
						Document detailDOC = null;
						String product_id = "";
						try {
							detailDOC = JsoupCrawler.fetchDocument(url, tc.getSite_code());
							Pattern p = Pattern.compile("\\d+");
							Matcher m = p.matcher(url);
							if(m.find()){
								product_id = m.group(0);
								log.info(Uni_LOG_Common.Print_Product_ID("JD", product_id));
							}else{
								continue;
							}
						} catch (Exception e) {
							log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "JD", url));
							continue;
						}
						if(detailDOC != null){
							String product_title = detailDOC.select(html_detail_title).text();
							BigDecimal price = null;
							CrawlerPropertyInfo crawlerPropertyInfo = persistenceLogic_jsoup.getJsoupProperty(WebSiteEnum.convertEnum(tc.getSite_code()).name());
							String price_json_url = DataUtil.format(html_detail_price_json_url, new String[]{crawlerPropertyInfo.getToken(), product_id});
							Document priceJsonDOC = null;
							try {
								priceJsonDOC = JsoupCrawler.fetchDocument(price_json_url, tc.getSite_code());
							} catch (Exception e) {
								log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "JD", price_json_url));
							}
							if(priceJsonDOC != null){
								try {
									JSONArray jsonArr = new JSONArray(priceJsonDOC.text());
									JSONObject jsonObj = (JSONObject)jsonArr.get(0);
									price = new BigDecimal(jsonObj.getString("p"));
									if(price != null && price.equals(-1)){
										price = null;
									}
								} catch (JSONException e) {
									log.error(Uni_LOG_Common.Fetch_JSON_ERROR(e.getMessage(), "JD", priceJsonDOC.text(), price_json_url));
								}
							}
							String brand_name = detailDOC.select(html_detail_brand).text();
							if(StringUtils.isBlank(brand_name)){
								brand_name = detailDOC.select("#parameter2 > li:nth-child(3)").text().indexOf("品牌")!=-1 ? detailDOC.select("#parameter2 > li:nth-child(3)").attr("title"):"其他";
							}
							int status = 0;
							String json = "";
							ProductInfo productInfo = new ProductInfo(product_id, product_title, price, brand_name, status, product_list_id, url, json);
							persistenceLogic.insertProductInfo(productInfo);
							Document reviewJsonDOC = null;
							String review_json_url = DataUtil.format(html_review_info_json_url, new String[]{product_id});
							try {
								reviewJsonDOC = JsoupCrawler.fetchDocument(review_json_url, tc.getSite_code());
							} catch (Exception e) {
								log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "JD", review_json_url));
							}
							if(reviewJsonDOC != null){
								JSONObject jsonObj = null;
								try {
									jsonObj = new JSONObject(reviewJsonDOC.text());
								} catch (JSONException e) {
									String reviewJson = DataUtil.jsonString(reviewJsonDOC.text());
									try {
										jsonObj = new JSONObject(reviewJson);
									} catch (JSONException e1) {
										log.error(Uni_LOG_Common.Fetch_JSON_ERROR(e1.getMessage(), "JD", reviewJsonDOC.text(), review_json_url));
									}
								}
								try {
									JSONObject productCommentSummary = jsonObj.getJSONObject("productCommentSummary");
									String goodCount = productCommentSummary.getString("goodCount");
									String generalCount = productCommentSummary.getString("generalCount");
									String poorCount = productCommentSummary.getString("poorCount");
									String goodRate = productCommentSummary.getString("goodRate");
									ReviewInfo_JD reviewJD = new ReviewInfo_JD(product_id, Integer.valueOf(goodCount), Integer.valueOf(generalCount), Integer.valueOf(poorCount), Float.valueOf(goodRate), reviewJsonDOC.text(), tc.getExecute_id());
									persistenceLogic.insertReviewInfo(reviewJD);
									
									JSONArray hotCommentTagStatistics = jsonObj.getJSONArray("hotCommentTagStatistics");
									for(int j=0; j<hotCommentTagStatistics.length(); j++){
										JSONObject hotCommentTagStatistic = (JSONObject)hotCommentTagStatistics.get(j);
										String tagId = hotCommentTagStatistic.getString("id");
										String content = hotCommentTagStatistic.getString("name");
										String count = hotCommentTagStatistic.getString("count");
										ReviewImpression_JD reviewImpression_JD = new ReviewImpression_JD(tagId, product_id, content, Integer.valueOf(count), hotCommentTagStatistic.toString(), tc.getExecute_id()); 
										persistenceLogic.insertReviewImpressionInfo(reviewImpression_JD);
									}
									
									JSONArray comments = jsonObj.getJSONArray("comments");
									for(int j=0; j<comments.length(); j++){
										JSONObject comment = (JSONObject)comments.get(j);
										String content = comment.getString("content");
										//String userClientShow = comment.getString("userClientShow");
										Date reviewTime = DataUtil.StrToDate(comment.getString("creationTime"), "yyyy-MM-dd HH:mm:ss");
										ReviewBadInfo_JD reviewBadInfo_JD = new ReviewBadInfo_JD(content, reviewTime, product_id, comment.toString(), tc.getExecute_id());
										persistenceLogic.insertReviewBadInfo(reviewBadInfo_JD);
									}
								} catch (Exception e) {
									log.error(Uni_LOG_Common.Fetch_JSON_ERROR(e.getMessage(), "JD", reviewJsonDOC.text(), review_json_url));
								}
							}
						}
					}
					
				}
	
				int product_count = persistenceLogic.getProductCountByProductListID(product_list_id);
				log.info("jd fetch product end, total product count ---->> " + product_count + " list url  ---->> " + productlistURL);
				if(product_count > 0){
					productList.setStatus(ProductListUrlStatusEnum.CRAWLING_FINISHED.value()+"");
				}else{
					productList.setStatus(ProductListUrlStatusEnum.EMPTY.value()+"");
				}
				persistenceLogic.undateProductList(productList);
			}else{
				log.info("jd .....开始睡眠");
				try {
					Thread.sleep(1000 * 60 * 60);
					startTime = new Date();
				} catch (InterruptedException e) {
					log.error(e.getMessage() + "jd --- 睡眠出现异常");
				}
				log.info("jd .....睡眠结束");
			}
		}
	}
	
	public void setPersistenceLogic(PersistenceLogic persistenceLogic) {
		this.persistenceLogic = persistenceLogic;
	}

	/**
	 * @param persistenceLogic_jsoup the persistenceLogic_jsoup to set
	 */
	public void setPersistenceLogic_jsoup(com.uni.hawkeye.jsoup.logic.PersistenceLogic persistenceLogic_jsoup) {
		this.persistenceLogic_jsoup = persistenceLogic_jsoup;
	}

	/**
	 * @param crawler_hour the crawler_hour to set
	 */
	public void setCrawler_hour(int crawler_hour) {
		this.crawler_hour = crawler_hour;
	}

	public void setHtml_category_navigation(String html_category_navigation) {
		this.html_category_navigation = html_category_navigation;
	}

	public void setHtml_category_level_1(String html_category_level_1) {
		this.html_category_level_1 = html_category_level_1;
	}

	public void setHtml_category_level_2(String html_category_level_2) {
		this.html_category_level_2 = html_category_level_2;
	}

	public void setHtml_category_level_3(String html_category_level_3) {
		this.html_category_level_3 = html_category_level_3;
	}

	public void setHtml_list_last_page(String html_list_last_page) {
		this.html_list_last_page = html_list_last_page;
	}

	/**
	 * @param html_list_product_block the html_list_product_block to set
	 */
	public void setHtml_list_product_block(String html_list_product_block) {
		this.html_list_product_block = html_list_product_block;
	}

	public void setHtml_list_product_detail_url(String html_list_product_detail_url) {
		this.html_list_product_detail_url = html_list_product_detail_url;
	}

	public void setHtml_detail_title(String html_detail_title) {
		this.html_detail_title = html_detail_title;
	}

	public void setHtml_detail_price_json_url(String html_detail_price_json_url) {
		this.html_detail_price_json_url = html_detail_price_json_url;
	}

	public void setHtml_detail_brand(String html_detail_brand) {
		this.html_detail_brand = html_detail_brand;
	}

	public void setHtml_review_info_json_url(String html_review_info_json_url) {
		this.html_review_info_json_url = html_review_info_json_url;
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
		String brand_id = DataUtil.getParam(brand_href).get("ev");
		brand_id = brand_id.replace("exbrand_", "");
		CategoryInfo brand_info = new CategoryInfo(brand_id, brand, tc.getExecute_id(), brand_href, CategoryControlStatus.UNDO.value(), "3", category_2_id);
		persistenceLogic.insertCategoryInfo(brand_info);
	}

	@Override
	public void crawlerProduct(TaskControl tc, Date startTime, EBTaskInfo ebTaskInfo) {
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
			if(DataUtil.dateDiff(startTime, new Date()) < crawler_hour){
				ProductList productList = productLists.get(i);
				int product_list_id = productList.getProduct_list_id();
				productList.setStatus(ProductListUrlStatusEnum.CRAWLING.value()+"");
				persistenceLogic.undateProductList(productList);
				String productlistURL = productList.getUrl();
				log.info(Uni_LOG_Common.Fetch_Product_Begin("JD", productlistURL));
				Document doc = null;
				try {
					doc = JsoupCrawler.fetchDocument(productlistURL, tc.getSite_code());
				} catch (Exception e) {
					log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "JD", productlistURL));
				}
				if(doc != null){
					Elements product_blocks = doc.select(html_list_product_block);
					for(Element detail_block : product_blocks){
						Elements detail_url = detail_block.select(html_list_product_detail_url);
						String url = detail_url.attr("href");
						if(url.indexOf("http:") == -1){
							url = "http:" + url;
						}
						Document detailDOC = null;
						String product_id = "";
						try {
							detailDOC = JsoupCrawler.fetchDocument(url, tc.getSite_code());
							Pattern p = Pattern.compile("\\d+");
							Matcher m = p.matcher(url);
							if(m.find()){
								product_id = m.group(0);
								log.info(Uni_LOG_Common.Print_Product_ID("JD", product_id));
							}else{
								continue;
							}
						} catch (Exception e) {
							log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "JD", url));
							continue;
						}
						if(detailDOC != null){
							String product_title = detailDOC.select(html_detail_title).text();

							BigDecimal price = new BigDecimal(0);
							CrawlerPropertyInfo crawlerPropertyInfo = persistenceLogic_jsoup.getJsoupProperty(WebSiteEnum.convertEnum(tc.getSite_code()).name());
							String price_json_url = DataUtil.format(html_detail_price_json_url, new String[]{crawlerPropertyInfo.getToken() ,product_id});
							Document priceJsonDOC = null;
							try {
								priceJsonDOC = JsoupCrawler.fetchDocument(price_json_url, tc.getSite_code());
							} catch (Exception e) {
								log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "JD", price_json_url));
							}
							if(priceJsonDOC != null){
								try {
									JSONArray jsonArr = new JSONArray(priceJsonDOC.text());
									JSONObject jsonObj = (JSONObject)jsonArr.get(0);
									String price_str = jsonObj.getString("p");
									if(!StringUtil.isBlank(price_str)){
										price = new BigDecimal(price_str);
										if(price != null && price.equals(-1)){
											price = new BigDecimal(0);
										}
									}
								} catch (JSONException e) {
									log.error(Uni_LOG_Common.Fetch_JSON_ERROR(e.getMessage(), "JD", priceJsonDOC.text(), price_json_url));
								}
							}
							String brand_name = detailDOC.select(html_detail_brand).text();
							if(StringUtils.isBlank(brand_name)){
								brand_name = detailDOC.select("#parameter2 > li:nth-child(3)").text().indexOf("品牌")!=-1 ? detailDOC.select("#parameter2 > li:nth-child(3)").attr("title"):"其他";
							}
							int status = 0;
							String json = "";
							if(product_title.contains(key_word)){
								if(p_l.floatValue() <= price.floatValue() && p_h.floatValue() >= price.floatValue()){
									ProductInfo productInfo = new ProductInfo(product_id, product_title, price, brand_name, status, product_list_id, url, json);
									persistenceLogic.insertProductInfo(productInfo);
									Document reviewJsonDOC = null;
									String review_json_url = DataUtil.format(html_review_info_json_url, new String[]{product_id});
									try {
										reviewJsonDOC = JsoupCrawler.fetchDocument(review_json_url, tc.getSite_code());
									} catch (Exception e) {
										log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "JD", review_json_url));
									}
									if(reviewJsonDOC != null){
										JSONObject jsonObj = null;
										try {
											jsonObj = new JSONObject(reviewJsonDOC.text());
										} catch (JSONException e) {
											String reviewJson = DataUtil.jsonString(reviewJsonDOC.text());
											try {
												jsonObj = new JSONObject(reviewJson);
											} catch (JSONException e1) {
												log.error(Uni_LOG_Common.Fetch_JSON_ERROR(e1.getMessage(), "JD", reviewJsonDOC.text(), review_json_url));
											}
										}
										try {
											JSONObject productCommentSummary = jsonObj.getJSONObject("productCommentSummary");
											String goodCount = productCommentSummary.getString("goodCount");
											String generalCount = productCommentSummary.getString("generalCount");
											String poorCount = productCommentSummary.getString("poorCount");
											String goodRate = productCommentSummary.getString("goodRate");
											ReviewInfo_JD reviewJD = new ReviewInfo_JD(product_id, Integer.valueOf(goodCount), Integer.valueOf(generalCount), Integer.valueOf(poorCount), Float.valueOf(goodRate), reviewJsonDOC.text(), tc.getExecute_id());
											persistenceLogic.insertReviewInfo(reviewJD);
											
											JSONArray hotCommentTagStatistics = jsonObj.getJSONArray("hotCommentTagStatistics");
											for(int j=0; j<hotCommentTagStatistics.length(); j++){
												JSONObject hotCommentTagStatistic = (JSONObject)hotCommentTagStatistics.get(j);
												String tagId = hotCommentTagStatistic.getString("id");
												String content = hotCommentTagStatistic.getString("name");
												String count = hotCommentTagStatistic.getString("count");
												ReviewImpression_JD reviewImpression_JD = new ReviewImpression_JD(tagId, product_id, content, Integer.valueOf(count), hotCommentTagStatistic.toString(), tc.getExecute_id()); 
												persistenceLogic.insertReviewImpressionInfo(reviewImpression_JD);
											}
											
											JSONArray comments = jsonObj.getJSONArray("comments");
											for(int j=0; j<comments.length(); j++){
												JSONObject comment = (JSONObject)comments.get(j);
												String content = comment.getString("content");
												//String userClientShow = comment.getString("userClientShow");
												Date reviewTime = DataUtil.StrToDate(comment.getString("creationTime"), "yyyy-MM-dd HH:mm:ss");
												ReviewBadInfo_JD reviewBadInfo_JD = new ReviewBadInfo_JD(content, reviewTime, product_id, comment.toString(), tc.getExecute_id());
												persistenceLogic.insertReviewBadInfo(reviewBadInfo_JD);
											}
										} catch (Exception e) {
											log.error(Uni_LOG_Common.Fetch_JSON_ERROR(e.getMessage(), "JD", reviewJsonDOC.text(), review_json_url));
										}
									}
								}
							}
						}
					}
					
				}
	
				int product_count = persistenceLogic.getProductCountByProductListID(product_list_id);
				log.info("jd fetch product end, total product count ---->> " + product_count + " list url  ---->> " + productlistURL);
				if(product_count > 0){
					productList.setStatus(ProductListUrlStatusEnum.CRAWLING_FINISHED.value()+"");
				}else{
					productList.setStatus(ProductListUrlStatusEnum.EMPTY.value()+"");
				}
				persistenceLogic.undateProductList(productList);
			}else{
				log.info("jd .....开始睡眠");
				try {
					Thread.sleep(1000 * 60 * 60);
					startTime = new Date();
				} catch (InterruptedException e) {
					log.error(e.getMessage() + "jd --- 睡眠出现异常");
				}
				log.info("jd .....睡眠结束");
			}
		}
	}

}
