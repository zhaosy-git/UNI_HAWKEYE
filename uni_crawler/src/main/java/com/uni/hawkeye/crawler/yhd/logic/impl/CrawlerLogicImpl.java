package com.uni.hawkeye.crawler.yhd.logic.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
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

import com.uni.hawkeye.crawler.yhd.bean.CategoryInfo;
import com.uni.hawkeye.crawler.yhd.bean.ProductInfo;
import com.uni.hawkeye.crawler.yhd.bean.ProductList;
import com.uni.hawkeye.crawler.yhd.bean.ReviewBadInfo_YHD;
import com.uni.hawkeye.crawler.yhd.bean.ReviewImpression_YHD;
import com.uni.hawkeye.crawler.yhd.bean.ReviewInfo_YHD;
import com.uni.hawkeye.crawler.yhd.bean.TaskControl;
import com.uni.hawkeye.crawler.yhd.enums.CategoryControlStatus;
import com.uni.hawkeye.crawler.yhd.enums.ProductListUrlStatusEnum;
import com.uni.hawkeye.crawler.yhd.logic.CrawlerLogic;
import com.uni.hawkeye.crawler.yhd.logic.PersistenceLogic;
import com.uni.hawkeye.jsoup.JsoupCrawler;
import com.uni.hawkeye.log.Uni_LOG_Common;
import com.uni.hawkeye.utils.DataUtil;

import uni_hawkeye.core.EBTaskInfo;

public class CrawlerLogicImpl implements CrawlerLogic {
	
	private static Log log = LogFactory.getLog(CrawlerLogicImpl.class);

	private static final ResourceBundle RESOURCE_BUNDLE_WHITE_LIST = ResourceBundle.getBundle("white_list_yhd", Locale.getDefault());

	private PersistenceLogic persistenceLogic;

	private Pattern p = Pattern.compile("\\d+");
	
	private String html_category_navigation;
	private String html_category_level_1;
	private String html_category_level_2;
	private String html_category_level_3;
	
	private String html_list_last_page;
	private String html_list_last_page_1;
	private String html_list_product_id;
	private String html_list_product_detail_url;
	private String html_detail_id;
	private String html_detail_title;
	private String html_detail_title_1;
	private String html_detail_brand;
	private String html_detail_price_json_url;
	private String html_detail_sale_count;
	private String html_detail_impression_json;
	private String html_detail_good_review_avg;
	private String html_detail_review;
	private String html_detail_bad_review;

	@Override
	public void crawlCategoryList(TaskControl tc) {
		Iterator<String> white_list = RESOURCE_BUNDLE_WHITE_LIST.keySet().iterator();
		while (white_list.hasNext()) {
			String white_list_url = RESOURCE_BUNDLE_WHITE_LIST.getString(white_list.next());
			Document doc = null;
			try {
				doc = JsoupCrawler.fetchDocument(white_list_url, tc.getSite_code());
			} catch (Exception e) {
				log.error(e.getMessage() + Uni_LOG_Common.Fetch_WhiteList_ERROR("YHD", white_list_url));
			}
			
			if (doc != null) {
				Element navigation = doc.select(html_category_navigation).get(0);
				Element level_1 = navigation.select(html_category_level_1).get(0);
				Element level_2 = navigation.select(html_category_level_2).get(0);
				Element level_3 = navigation.select(html_category_level_3).isEmpty() ? null : navigation.select(html_category_level_3).get(0);
				
				if(level_1 != null){
					String level_1_title = level_1.select("span > a").attr("title");
					String level_1_cat = level_1.attr("ngcateid");
					CategoryInfo level_1_category_info = null;
					if(level_2 != null){
						level_1_category_info = new CategoryInfo(level_1_cat, level_1_title, tc.getExecute_id(), "", CategoryControlStatus.UNDO.value(), "1", "");
						
						String level_2_title = level_2.select("span > a").attr("title");
						String level_2_cat = level_2.attr("ngcateid");
						
						CategoryInfo level_2_category_info = null;
						if(level_3 != null){
							level_2_category_info = new CategoryInfo(level_2_cat, level_2_title, tc.getExecute_id(), "", CategoryControlStatus.UNDO.value(), "2", level_1_cat);
							
							String level_3_title = level_3.select("span > a").attr("title");
							String level_3_cat = level_3.attr("ngcateid");
							
							CategoryInfo level_3_category_info = new CategoryInfo(level_3_cat, level_3_title, tc.getExecute_id(), white_list_url, CategoryControlStatus.UNDO.value(), "3",
									level_2_cat);
							persistenceLogic.insertCategoryInfo(level_3_category_info);
						}else{
							level_2_category_info = new CategoryInfo(level_2_cat, level_2_title, tc.getExecute_id(), white_list_url, CategoryControlStatus.UNDO.value(), "2", level_1_cat);
						}
						persistenceLogic.insertCategoryInfo(level_2_category_info);
					}else{
						level_1_category_info = new CategoryInfo(level_1_cat, level_1_title, tc.getExecute_id(), white_list_url, CategoryControlStatus.UNDO.value(), "1", "");
					}
					persistenceLogic.insertCategoryInfo(level_1_category_info);
					
				}else{
					log.error(Uni_LOG_Common.Parse_Category_ERROR("YHD", white_list_url));
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
				log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "YHD", url_));
			}
			ProductList productList = new ProductList(catInfo.getCategory_id(), catInfo.getExecute_id(), 1, url_, ProductListUrlStatusEnum.UNDO.value()+"");
			persistenceLogic.insertProductList(productList);
			int lastPage = 0;
			if(doc != null){
				String pageCount = doc.select(html_list_last_page).text();
				if(!StringUtil.isBlank(pageCount) && pageCount.indexOf("/") != -1){
					pageCount = pageCount.split("/")[1];
				}else{
					pageCount = doc.select(html_list_last_page_1).text();
				}
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
			log.info(Uni_LOG_Common.Fetch_Product_Begin("YHD", productlistURL));
			Document doc = null;
			try {
				doc = JsoupCrawler.fetchDocument(productlistURL, tc.getSite_code());
			} catch (Exception e) {
				log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "YHD", productlistURL));
			}
			if(doc != null){
				Elements product_id_elements = doc.select(html_list_product_id);
				for(Element compro_id_element : product_id_elements){
					String comproid = compro_id_element.attr("id");
					Matcher m = p.matcher(comproid);
					if(m.find()){
						comproid = m.group(0);
					}else{
						log.error("comproid not find... " + compro_id_element.html());
						continue;
					}
					Element product_detail_url = null;
					try {
						product_detail_url = doc.select(html_list_product_detail_url+comproid).get(0);
					} catch (Exception e) {
						log.error(e.getMessage() + " yhd \n html: \n" + compro_id_element.html() +" \n url: \n"+ productlistURL);
					}
					String pmid = product_detail_url.attr("pmid");
					String url = product_detail_url.attr("href");
					Document detailDOC = null;
					try {
						detailDOC = JsoupCrawler.fetchDocument(url, tc.getSite_code());
					} catch (Exception e) {
						log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "YHD", url));
						continue;
					}
					if(detailDOC != null){
						String product_id = detailDOC.select(html_detail_id).text();
						Matcher m_product_id = p.matcher(product_id);
						if(m_product_id.find()){
							product_id = m_product_id.group(0);
						}else{
							continue;
						}
						log.info(Uni_LOG_Common.Print_Product_ID("YHD", product_id));
						String product_title = detailDOC.select(html_detail_title).text();
						if(StringUtils.isBlank(product_title)){
							product_title = detailDOC.select(html_detail_title_1).text();
						}
						BigDecimal price = new BigDecimal(0);
						String price_json_url = html_detail_price_json_url+pmid;
						Document priceJsonDOC = null;
						try {
							priceJsonDOC = JsoupCrawler.fetchDocument(price_json_url, tc.getSite_code());
						} catch (Exception e) {
							log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "YHD", price_json_url));
						}
						if(priceJsonDOC != null && !StringUtil.isBlank(priceJsonDOC.text())){
							try {
								JSONObject jsonOBJ = new JSONObject(priceJsonDOC.text());
								price = new BigDecimal(jsonOBJ.getString("currentPrice"));
							} catch (JSONException e) {
								log.error(Uni_LOG_Common.Fetch_JSON_ERROR(e.getMessage(), "YHD", priceJsonDOC.text(), price_json_url));
							}
						}
						if (product_title.contains(key_word)) {
							if (p_l.floatValue() <= price.floatValue() && p_h.floatValue() >= price.floatValue()) {
								
								int sale_count = 0;
								Elements sale_count_elements = detailDOC.select(html_detail_sale_count);
								if(!sale_count_elements.isEmpty()){
									sale_count = Integer.valueOf(sale_count_elements.get(0).attr("salenumber"));
								}
								String brand_name = detailDOC.select(html_detail_brand) == null ? "" : detailDOC.select(html_detail_brand).text();
								int status = 0;
								String json = "";
								ProductInfo productInfo = new ProductInfo(product_id, product_title, price, sale_count, brand_name, status, product_list_id, url, json);
								persistenceLogic.insertProductInfo(productInfo);
								
								Document impressionDOC = null;
								try {
									impressionDOC = JsoupCrawler.fetchDocument(html_detail_impression_json+comproid, tc.getSite_code());
								} catch (Exception e) {
									log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "YHD", html_detail_impression_json+comproid));
								}
								if(impressionDOC != null && !StringUtil.isBlank(impressionDOC.text())){
									try {
										JSONObject impression_jsonOBJ = new JSONObject(impressionDOC.text());
										if(!impression_jsonOBJ.isNull("resultList")){
											JSONArray jsonArray = impression_jsonOBJ.getJSONArray("resultList");
											if(jsonArray != null && jsonArray.length() > 0){
												for(int j=0; j<jsonArray.length(); j++){
													JSONObject jsonOBJ = (JSONObject) jsonArray.get(j);
													String labelId = jsonOBJ.getString("labelId");
													int peCount = jsonOBJ.getInt("peCount");
													String labelName = jsonOBJ.getString("labelName");
													ReviewImpression_YHD reviewImpression_YHD = new ReviewImpression_YHD(labelId, product_id, labelName, peCount, jsonOBJ.toString(), tc.getExecute_id());
													persistenceLogic.insertReviewImpressionInfo(reviewImpression_YHD);
												}
											}
										}
									} catch (JSONException e) {
										log.error(Uni_LOG_Common.Fetch_JSON_ERROR(e.getMessage(), "YHD", impressionDOC.text(), html_detail_impression_json+comproid));
									}
								}
		
								float goodReviewAVG = 0;
								int goodCNT = 0;
								int mediumCNT = 0;
								int poorCNT = 0;
								Document goodReviewAVGJsonDOC = null;
								try {
									goodReviewAVGJsonDOC = JsoupCrawler.fetchDocument(html_detail_good_review_avg+pmid, tc.getSite_code());
								} catch (Exception e) {
									log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "YHD", html_detail_good_review_avg+pmid));
								}
								if(goodReviewAVGJsonDOC != null && !StringUtil.isBlank(goodReviewAVGJsonDOC.text())){
									try {
										JSONObject goodReviewAVG_jsonOBJ = new JSONObject(goodReviewAVGJsonDOC.text());
										goodReviewAVG = Float.valueOf(goodReviewAVG_jsonOBJ.getString("proScore"));
									} catch (JSONException e) {
										log.error(Uni_LOG_Common.Fetch_JSON_ERROR(e.getMessage(), "YHD", goodReviewAVGJsonDOC.text(), html_detail_good_review_avg+pmid));
									}
								}
								
								
								Document reviewJsonDOC = null;
								String reviewURL = DataUtil.format(html_detail_review, new String[]{comproid});
								try {
									reviewJsonDOC = JsoupCrawler.fetchDocument(reviewURL, tc.getSite_code());
								} catch (Exception e) {
									log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "YHD", reviewURL));
								}
								if(reviewJsonDOC != null && !StringUtil.isBlank(reviewJsonDOC.text())){
									String reviewHTML = null;
									try {
										JSONObject badReview_jsonOBJ = new JSONObject(reviewJsonDOC.text());
										reviewHTML = badReview_jsonOBJ.getString("value").replace("\r\n", "").replace(" ", "").trim();
									} catch (JSONException e) {
										log.error(Uni_LOG_Common.Fetch_JSON_ERROR(e.getMessage(), "YHD", reviewJsonDOC.text(), reviewURL));
										reviewHTML = reviewJsonDOC.text()
												.substring(10, reviewJsonDOC.text().length()-2)
												.replace("\\r", "")
												.replace("\\n", "")
												.replace("\\t","")
												.replace(" ", "")
												.trim();
									}
									Pattern p_good = Pattern.compile("(?<=好评\\()[0-9]+(?=\\))");
									Matcher m_good = p_good.matcher(reviewHTML);
									Pattern p_medium = Pattern.compile("(?<=中评\\()[0-9]+(?=\\))");
									Matcher m_medium = p_medium.matcher(reviewHTML);
									Pattern p_poor = Pattern.compile("(?<=差评\\()[0-9]+(?=\\))");
									Matcher m_poor = p_poor.matcher(reviewHTML);
									if(m_good.find()){
										goodCNT = Integer.valueOf(m_good.group(0));
									}
									if(m_medium.find()){
										mediumCNT = Integer.valueOf(m_medium.group(0));
									}
									if(m_poor.find()){
										poorCNT = Integer.valueOf(m_poor.group(0));
									}
									ReviewInfo_YHD reviewInfo_YHD = new ReviewInfo_YHD(product_id, goodCNT, mediumCNT, poorCNT, goodReviewAVG, goodReviewAVGJsonDOC == null ? "" : goodReviewAVGJsonDOC.text(), tc.getExecute_id());
									persistenceLogic.insertReviewInfo(reviewInfo_YHD);
								}
								
								
								Document badReviewJsonDOC = null;
								String badReviewURL = DataUtil.format(html_detail_bad_review, new String[]{comproid});
								try {
									badReviewJsonDOC = JsoupCrawler.fetchDocument(badReviewURL, tc.getSite_code());
								} catch (Exception e) {
									log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "YHD", badReviewURL));
								}
								if(badReviewJsonDOC != null && !StringUtil.isBlank(badReviewJsonDOC.text())){
									String badReviewHTML = null;
									try {
										JSONObject badReview_jsonOBJ = new JSONObject(badReviewJsonDOC.text());
										badReviewHTML = badReview_jsonOBJ.getString("value").replace("\r\n", "").replace(" ", "").trim();
									} catch (JSONException e) {
										log.error(Uni_LOG_Common.Fetch_JSON_ERROR(e.getMessage(), "YHD", badReviewJsonDOC.text(), badReviewURL));
										badReviewHTML = badReviewJsonDOC.text()
												.substring(10, badReviewJsonDOC.text().length()-2)
												.replace("\\r", "")
												.replace("\\n", "")
												.replace("\\t","")
												.replace(" ", "")
												.trim();
									}
									Pattern p_review = Pattern.compile("((?<=内容：).*(?=回复))");
									Matcher m_review = p_review.matcher(badReviewHTML);
									if(m_review.find()){
										String review_tmp = m_review.group(0);//
										String[] reviews = review_tmp.split("内容：");
										for(String review : reviews){
											String tmp = review.replaceAll("[ \t]*", "").trim();
											String review_content = tmp;
											if(tmp.indexOf("展开") != -1){
												review_content = tmp.substring(0, tmp.indexOf("展开"));
											}
											Pattern p_time = Pattern.compile("[\\d]{4}-[\\d]{2}-[\\d]{4}:[\\d]{2}:[\\d]{2}");
											Matcher m_time = p_time.matcher(tmp);
											Date review_time = null;
											if(m_time.find()){
												String str = m_time.group(0);
												str = str.substring(0, 10) + " " + str.substring(10, str.length());
												review_time = DataUtil.StrToDate(str, "yyyy-MM-dd HH:mm:ss");
											}
											String from_way = "";
											if(review.indexOf("来自") != -1){
												from_way = review.substring(review.indexOf("来自")-2, review.indexOf("来自")+4);
											}
											ReviewBadInfo_YHD reviewBadInfo_YHD = new ReviewBadInfo_YHD(review_content, review_time, from_way, product_id, tmp, tc.getExecute_id());
											persistenceLogic.insertReviewBadInfo(reviewBadInfo_YHD);
										}
									}
								}
							}
						}
					}
				}
			}

			int product_count = persistenceLogic.getProductCountByProductListID(product_list_id);
			log.info("yhd fetch product end, total product count ---->> " + product_count + " list url  ---->> " + productlistURL);
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
	 * @param html_category_level_1 the html_category_level_1 to set
	 */
	public void setHtml_category_level_1(String html_category_level_1) {
		this.html_category_level_1 = html_category_level_1;
	}

	/**
	 * @param html_category_level_2 the html_category_level_2 to set
	 */
	public void setHtml_category_level_2(String html_category_level_2) {
		this.html_category_level_2 = html_category_level_2;
	}

	/**
	 * @param html_category_level_3 the html_category_level_3 to set
	 */
	public void setHtml_category_level_3(String html_category_level_3) {
		this.html_category_level_3 = html_category_level_3;
	}

	/**
	 * @param html_list_last_page the html_list_last_page to set
	 */
	public void setHtml_list_last_page(String html_list_last_page) {
		this.html_list_last_page = html_list_last_page;
	}

	/**
	 * @param html_list_last_page_1 the html_list_last_page_1 to set
	 */
	public void setHtml_list_last_page_1(String html_list_last_page_1) {
		this.html_list_last_page_1 = html_list_last_page_1;
	}

	/**
	 * @param html_list_product_id the html_list_product_id to set
	 */
	public void setHtml_list_product_id(String html_list_product_id) {
		this.html_list_product_id = html_list_product_id;
	}

	/**
	 * @param html_list_product_detail_url the html_list_product_detail_url to set
	 */
	public void setHtml_list_product_detail_url(String html_list_product_detail_url) {
		this.html_list_product_detail_url = html_list_product_detail_url;
	}

	/**
	 * @param html_detail_id the html_detail_id to set
	 */
	public void setHtml_detail_id(String html_detail_id) {
		this.html_detail_id = html_detail_id;
	}

	/**
	 * @param html_detail_title the html_detail_title to set
	 */
	public void setHtml_detail_title(String html_detail_title) {
		this.html_detail_title = html_detail_title;
	}

	/**
	 * @param html_detail_title_1 the html_detail_title_1 to set
	 */
	public void setHtml_detail_title_1(String html_detail_title_1) {
		this.html_detail_title_1 = html_detail_title_1;
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
	 * @param html_detail_sale_count the html_detail_sale_count to set
	 */
	public void setHtml_detail_sale_count(String html_detail_sale_count) {
		this.html_detail_sale_count = html_detail_sale_count;
	}

	/**
	 * @param html_detail_impression_json the html_detail_impression_json to set
	 */
	public void setHtml_detail_impression_json(String html_detail_impression_json) {
		this.html_detail_impression_json = html_detail_impression_json;
	}

	/**
	 * @param html_detail_good_review_avg the html_detail_good_review_avg to set
	 */
	public void setHtml_detail_good_review_avg(String html_detail_good_review_avg) {
		this.html_detail_good_review_avg = html_detail_good_review_avg;
	}

	/**
	 * @param html_detail_review the html_detail_review to set
	 */
	public void setHtml_detail_review(String html_detail_review) {
		this.html_detail_review = html_detail_review;
	}

	/**
	 * @param html_detail_bad_review the html_detail_bad_review to set
	 */
	public void setHtml_detail_bad_review(String html_detail_bad_review) {
		this.html_detail_bad_review = html_detail_bad_review;
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
		String brand_id = DataUtil.matchByRegex("b\\d+-\\d+", brand_href).replace("b", "");
		CategoryInfo brand_info = new CategoryInfo(brand_id, brand, tc.getExecute_id(), brand_href, CategoryControlStatus.UNDO.value(), "3", category_2_id);
		persistenceLogic.insertCategoryInfo(brand_info);
	}

	public static void main(String[] args) {
		String url = "http://list.yhd.com/c5404-0-81056/b1333-7138/a-s3-v4-p1-price-d0-f0d-m1-rt0-pid-mid0-k/";
		Pattern p = Pattern.compile("b\\d+-\\d+");
		Matcher m = p.matcher(url);
		if(m.find()){
			System.out.println(m.group(0));
		}
	}
}
