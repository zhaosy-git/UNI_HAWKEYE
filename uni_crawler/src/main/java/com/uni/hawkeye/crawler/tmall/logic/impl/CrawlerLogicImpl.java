package com.uni.hawkeye.crawler.tmall.logic.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
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

import com.uni.hawkeye.crawler.tmall.bean.CategoryInfo;
import com.uni.hawkeye.crawler.tmall.bean.PriceInfo_TMALL;
import com.uni.hawkeye.crawler.tmall.bean.ProductInfo;
import com.uni.hawkeye.crawler.tmall.bean.ProductList;
import com.uni.hawkeye.crawler.tmall.bean.ReviewBadInfo_TMALL;
import com.uni.hawkeye.crawler.tmall.bean.ReviewImpression_TMALL;
import com.uni.hawkeye.crawler.tmall.bean.ReviewInfo_TMALL;
import com.uni.hawkeye.crawler.tmall.bean.TaskControl;
import com.uni.hawkeye.crawler.tmall.enums.CategoryControlStatus;
import com.uni.hawkeye.crawler.tmall.enums.PriceEnum;
import com.uni.hawkeye.crawler.tmall.enums.ProductDetailStatusEnum;
import com.uni.hawkeye.crawler.tmall.enums.ProductInfoWayTypeEnum;
import com.uni.hawkeye.crawler.tmall.enums.ProductListUrlStatusEnum;
import com.uni.hawkeye.crawler.tmall.logic.CrawlerLogic;
import com.uni.hawkeye.crawler.tmall.logic.PersistenceLogic;
import com.uni.hawkeye.jsoup.JsoupCrawler;
import com.uni.hawkeye.log.Uni_LOG_Common;
import com.uni.hawkeye.utils.DataUtil;

import uni_hawkeye.core.EBTaskInfo;

public class CrawlerLogicImpl implements CrawlerLogic {

	private static Log log = LogFactory.getLog(CrawlerLogicImpl.class);

	private static final ResourceBundle RESOURCE_BUNDLE_WHITE_LIST = ResourceBundle.getBundle("white_list_tmall", Locale.getDefault());

	private PersistenceLogic persistenceLogic;
	private String referrer;
	private int crawler_hour;

	private String html_list_navigation;
	private String html_list_last_page;
	private String html_list_product_block;
	private String html_list_product_title;
	private String html_list_way_type;
	private String html_detail_url_pattern_change;
	private String html_detail_url_pattern_change_result;
	private String html_detail_request_url;
	private String html_detail_brand;
	private String html_detail_brand_pattern;
	private String html_review_request_url;
	private String html_review_grade_avg_url;
	private String html_product_impression_url;
	
	private float avg_price = 0;

	@Override
	public void crawlCategoryList(TaskControl tc) {
		Iterator<String> white_list = RESOURCE_BUNDLE_WHITE_LIST.keySet().iterator();
		while (white_list.hasNext()) {
			String white_list_url = RESOURCE_BUNDLE_WHITE_LIST.getString(white_list.next());
			String url = DataUtil.format(white_list_url, new String[] { "1", "1" });
			Document doc = null;
			try {
				doc = JsoupCrawler.fetchDocument(url, tc.getSite_code());
			} catch (Exception e) {
				log.error(e.getMessage() + Uni_LOG_Common.Fetch_WhiteList_ERROR("TMALL", url));
			}

			if (doc != null) {
				Elements navigation = doc.select(html_list_navigation);
				if (navigation != null && navigation.size() > 0) {
					int level = 1;
					String category_parent_id = "";
					for (Element navi : navigation) {
						String category_name = navi.attr("title");
						if (!StringUtil.isBlank(category_name)) {
							String category_code = DataUtil.getParam(navi.attr("href")).get("cat");
							int execute_id = tc.getExecute_id();
							String category_url = white_list_url;
							String status = CategoryControlStatus.UNDO.value();
							CategoryInfo categoryInfo = new CategoryInfo(category_code, category_name, execute_id, category_url, status, level + "", category_parent_id);
							persistenceLogic.insertCategoryInfo(categoryInfo);
							category_parent_id = category_code;
							level++;
						}
					}
				} else {
					log.error(Uni_LOG_Common.Parse_Category_ERROR("TMALL", white_list_url));
				}
			}
		}
	}

	@Override
	public void crawlerProductList(TaskControl tc) {
		List<CategoryInfo> undoCategoryInfoList = persistenceLogic.getCategoryInfoByStatus(tc.getExecute_id(), CategoryControlStatus.UNDO.value());
		int undoCategoryInfoList_SIZE = undoCategoryInfoList.size();
		for (int i = 0; i < undoCategoryInfoList_SIZE; i++) {
			CategoryInfo catInfo = undoCategoryInfoList.get(i);
			persistenceLogic.updateCategoryInfoStatus(catInfo.getCategory_id(), CategoryControlStatus.CRAWLING.value());
			String url = catInfo.getCategory_url();
			Document doc = null;
			String url_ = DataUtil.format(url, new String[] { "0" });
			try {
				JsoupCrawler.setReferrer("https://www.tmall.com/");
				doc = JsoupCrawler.fetchDocument(url_, tc.getSite_code());
			} catch (Exception e) {
				log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "TMALL", url_));
			}
			ProductList productList = new ProductList(catInfo.getCategory_id(), catInfo.getExecute_id(), 1, url_, ProductListUrlStatusEnum.UNDO.value());
			persistenceLogic.insertProductList(productList);
			int lastPage = 0;
			if (doc != null) {
				String pageCount = doc.select(html_list_last_page).text();
				pageCount = pageCount.split("/")[1];
				lastPage = Integer.valueOf(pageCount);
			}
			for (int j = 2; j <= lastPage; j++) {
				productList = new ProductList(catInfo.getCategory_id(), catInfo.getExecute_id(), j, DataUtil.format(url, new String[] { ((j - 1) * 60) + "" }),
						ProductListUrlStatusEnum.UNDO.value());
				persistenceLogic.insertProductList(productList);
			}
			persistenceLogic.updateCategoryInfoStatus(catInfo.getCategory_id(), CategoryControlStatus.CRAWLING_FINISHED.value());
		}
	}

	@Override
	public void crawlerProduct(TaskControl tc, Date startTime, EBTaskInfo ebTaskInfo) {
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

		List<ProductList> productLists = persistenceLogic.getProductListByStatus(tc.getExecute_id(), ProductListUrlStatusEnum.UNDO.value());
		for (int i = 0; i < productLists.size(); i++) {
			if (DataUtil.dateDiff(startTime, new Date()) < crawler_hour) {
				ProductList productList = productLists.get(i);
				productList.setStatus(ProductListUrlStatusEnum.CRAWLING.value());
				persistenceLogic.undateProductList(productList);
				String productlistURL = productList.getUrl();
				int product_list_id = productList.getProduct_list_id();
				log.info(Uni_LOG_Common.Fetch_Product_Begin("TMALL", productlistURL));
				Document doc = null;
				try {
					doc = JsoupCrawler.fetchDocument(productlistURL, tc.getSite_code());
				} catch (Exception e) {
					log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "TMALL", productlistURL));
				}
				if (doc != null) {
					String data_id;
					String title;
					int way_type;
					String brandName;
					Document document_detail = null;
					String mdSkipJSON = null;
					String reviewJSON = null;
					String reviewGradeAVGJSON = null;
					Elements productElements = doc.select(html_list_product_block);
					for (Element product : productElements) {
						/* --------product_info TABLE BEGIN----------- */
						data_id = product.attr("data-id");
						if (!StringUtil.isBlank(data_id)) {
							log.info(Uni_LOG_Common.Print_Product_ID("TMALL", data_id));
							title = product.select(html_list_product_title).text();
							if (title.contains(key_word)) {
								String wayTypeFromWEB = product.select(html_list_way_type).text();
								if (!StringUtils.isBlank(wayTypeFromWEB)) {
									if (wayTypeFromWEB.indexOf(ProductInfoWayTypeEnum.海外旗舰店.name()) != -1) {
										way_type = ProductInfoWayTypeEnum.海外旗舰店.value();
									} else if (wayTypeFromWEB.indexOf(ProductInfoWayTypeEnum.海外专营店.name()) != -1) {
										way_type = ProductInfoWayTypeEnum.海外专营店.value();
									} else if (wayTypeFromWEB.indexOf(ProductInfoWayTypeEnum.旗舰.name()) != -1) {
										way_type = ProductInfoWayTypeEnum.旗舰.value();
									} else if (wayTypeFromWEB.indexOf(ProductInfoWayTypeEnum.超市.name()) != -1) {
										way_type = ProductInfoWayTypeEnum.超市.value();
									} else if (wayTypeFromWEB.indexOf(ProductInfoWayTypeEnum.专营.name()) != -1) {
										way_type = ProductInfoWayTypeEnum.专营.value();
									} else {
										way_type = ProductInfoWayTypeEnum.OTHER.value();
									}
								} else {
									way_type = ProductInfoWayTypeEnum.EMPTY.value();
								}

								String detailURL = "http:" + product.select(html_list_product_title).attr("href");
								String referrerURL = referrer + data_id;
								if (way_type == ProductInfoWayTypeEnum.海外旗舰店.value() || way_type == ProductInfoWayTypeEnum.海外专营店.value()) {
									detailURL = detailURL.replaceFirst(html_detail_url_pattern_change, html_detail_url_pattern_change_result);
									referrerURL = referrerURL.replaceFirst(html_detail_url_pattern_change, html_detail_url_pattern_change_result);
								}

								try {
									document_detail = JsoupCrawler.fetchDocument(detailURL, tc.getSite_code());
								} catch (Exception e) {
									log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "TMALL", detailURL));
								}
								String brandName_datail = document_detail.select(html_detail_brand).attr("title");
								String brandName_navigation = "";
								Pattern p = Pattern.compile(html_detail_brand_pattern);
								Matcher m = p.matcher(document_detail.html());
								if (m.find()) {
									brandName_navigation = m.group(0);
								}
								if (!StringUtils.isBlank(brandName_datail)) {
									brandName = brandName_datail;
								} else if (!StringUtils.isBlank(brandName_navigation)) {
									brandName = brandName_navigation;
								} else if (way_type == ProductInfoWayTypeEnum.超市.value()) {
									brandName = "天猫超市";
								} else {
									brandName = "其他";
								}

								ProductInfo pInfo = new ProductInfo();

								List<PriceInfo_TMALL> priceInfoList = parseAndSavePrice(data_id, mdSkipJSON, referrerURL, pInfo, product_list_id, tc);

								if (p_l.floatValue() <= avg_price && p_h.floatValue() >= avg_price) {
									pInfo.setProduct_id(data_id);
									pInfo.setProduct_title(title);
									pInfo.setWay_type(way_type);
									pInfo.setBrand_name(brandName);
									pInfo.setProduct_list_id(product_list_id);
									pInfo.setUrl(detailURL);
									pInfo.setJson(mdSkipJSON);
									pInfo.setCtime(new Date());
									pInfo.setMtime(new Date());
									if (!StringUtils.isBlank(data_id)) {
										pInfo.setStatus(ProductDetailStatusEnum.PRODUCT_DETAIL_CRAWLING_SUCCESS.value());
									} else {
										pInfo.setStatus(ProductDetailStatusEnum.PRODUCT_DETAIL_CRAWLING_FAIL.value());
									}
									persistenceLogic.insertProductInfo(pInfo);

									for (PriceInfo_TMALL priceInfo : priceInfoList) {
										persistenceLogic.insertPriceInfo(priceInfo);
									}

									/* --------product_info TABLE END----------- */

									/* --------review_tmall_info TABLE BEGIN----------- */
									String sellerID = DataUtil.matchParamValFromParamKey(detailURL, "user_id=\\d+");
									String reviewURL = DataUtil.format(html_review_request_url, new String[] { data_id, sellerID, "", "", "" });
									String reviewGradeAVGURL = DataUtil.format(html_review_grade_avg_url, new String[] { data_id, sellerID });
									Integer reviewTotalCNT = 0;
									float gradeAvg = 0;
									Document reviewJSONDOC = null;
									try {
										reviewJSONDOC = JsoupCrawler.fetchDocument(reviewURL, tc.getSite_code());
									} catch (Exception e) {
										log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "TMALL", reviewURL));
									}
									Document reviewGradeAVGJSONDOC = null;
									try {
										reviewGradeAVGJSONDOC = JsoupCrawler.fetchDocument(reviewGradeAVGURL, tc.getSite_code());
									} catch (Exception e) {
										log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "TMALL", reviewGradeAVGURL));
									}
									try {
										reviewJSON = reviewJSONDOC.text();
										JSONObject reviewJsonOBJ = new JSONObject(DataUtil.jsonString("{" + reviewJSON + "}"));
										JSONObject reviewJsonOBJ2 = (JSONObject) reviewJsonOBJ.get("rateDetail");
										JSONObject reviewJsonOBJ3 = (JSONObject) reviewJsonOBJ2.get("rateCount");
										reviewTotalCNT = Integer.valueOf(reviewJsonOBJ3.getString("total"));
									} catch (JSONException e) {
										log.error(Uni_LOG_Common.Fetch_JSON_ERROR(e.getMessage(), "TMALL", reviewJSON, reviewURL));
									}
									try {
										reviewGradeAVGJSON = reviewGradeAVGJSONDOC.text();
										reviewGradeAVGJSON = reviewGradeAVGJSON.replaceAll("(jsonp+(\\d+))+\\(|\\)", "");
										JSONObject reviewGradeAVGJsonOBJ;
										reviewGradeAVGJsonOBJ = new JSONObject(reviewGradeAVGJSON);
										JSONObject reviewGradeAVGJsonOBJ2 = (JSONObject) reviewGradeAVGJsonOBJ.get("dsr");
										gradeAvg = Float.valueOf(reviewGradeAVGJsonOBJ2.getString("gradeAvg"));
									} catch (JSONException e) {
										log.error(Uni_LOG_Common.Fetch_JSON_ERROR(e.getMessage(), "TMALL", reviewGradeAVGJSON, reviewGradeAVGURL));
									}

									ReviewInfo_TMALL tmallReview = new ReviewInfo_TMALL();
									tmallReview.setProduct_id_fk(data_id);
									tmallReview.setReview_cnt(reviewTotalCNT);
									tmallReview.setReview_grade_avg(gradeAvg);
									tmallReview.setReview_json(reviewJSON);
									tmallReview.setReview_grade_avg_json(reviewGradeAVGJSON);
									tmallReview.setExecute_id(tc.getExecute_id());
									persistenceLogic.insertReviewInfo(tmallReview);
									/* --------review_tmall_info TABLE END----------- */

									/* --------review_tmall_impression TABLE BEGIN----------- */
									String impressionID = "";
									String content = "";
									int cnt = 0;
									String posi = "";
									String productImpressionURL = DataUtil.format(html_product_impression_url, new String[] { data_id });
									String productImpressionJSON = "";
									JSONObject productImpressionJsonOBJ = null;
									JSONObject productImpressionJsonOBJ1 = null;
									JSONArray productImpressionJsonArray = null;
									try {
										productImpressionJSON = JsoupCrawler.fetchDocument(productImpressionURL, tc.getSite_code()).text();
									} catch (Exception e) {
										log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "TMALL", productImpressionURL));
									}
									try {
										if (!StringUtil.isBlank(productImpressionJSON)) {
											productImpressionJsonOBJ = new JSONObject("{" + productImpressionJSON + "}");
											if (productImpressionJsonOBJ.has("tags")) {
												productImpressionJsonOBJ1 = (JSONObject) productImpressionJsonOBJ.get("tags");
												if (productImpressionJsonOBJ1.has("tagClouds")) {
													productImpressionJsonArray = (JSONArray) productImpressionJsonOBJ1.get("tagClouds");
													if (productImpressionJsonArray != null && productImpressionJsonArray.length() > 0) {
														for (int j = 0; j < productImpressionJsonArray.length(); j++) {
															JSONObject jsonOBJ = (JSONObject) productImpressionJsonArray.get(j);
															impressionID = jsonOBJ.getString("id");
															content = jsonOBJ.getString("tag");
															cnt = Integer.valueOf(jsonOBJ.getString("count"));
															posi = jsonOBJ.getString("posi") == "true" ? "1" : "-1";
															ReviewImpression_TMALL reviewImpression_TMALL = new ReviewImpression_TMALL();
															reviewImpression_TMALL.setImpression_id(impressionID);
															reviewImpression_TMALL.setProduct_id_fk(data_id);
															reviewImpression_TMALL.setImpression_content(content);
															reviewImpression_TMALL.setImpression_cnt(cnt);
															reviewImpression_TMALL.setPosi(posi);
															reviewImpression_TMALL.setJson(productImpressionJsonOBJ.toString());
															reviewImpression_TMALL.setExecute_id(tc.getExecute_id());
															persistenceLogic.insertReviewImpressionInfo(reviewImpression_TMALL);
															/*
															 * --------review_tmall_bad_info TABLE BEGIN-----------
															 */
															if ("-1".equals(posi)) {
																parseBadReviewInfo_TMALL(1, data_id, sellerID, impressionID, tc.getExecute_id(), tc);
															}
															/* --------review_tmall_bad_info TABLE END----------- */
														}
													}
												}
											}
										}
									} catch (Exception e) {
										log.error(Uni_LOG_Common.Fetch_JSON_ERROR(e.getMessage(), "TMALL", productImpressionJSON, productImpressionURL));
										ReviewImpression_TMALL reviewImpression_TMALL = new ReviewImpression_TMALL();
										reviewImpression_TMALL.setImpression_id(impressionID);
										reviewImpression_TMALL.setProduct_id_fk(data_id);
										reviewImpression_TMALL.setImpression_content(content);
										reviewImpression_TMALL.setImpression_cnt(cnt);
										reviewImpression_TMALL.setPosi(posi);
										reviewImpression_TMALL.setJson(productImpressionJsonOBJ.toString());
										reviewImpression_TMALL.setExecute_id(tc.getExecute_id());
										persistenceLogic.insertReviewImpressionInfo(reviewImpression_TMALL);
									}
									/* --------review_tmall_impression TABLE END----------- */
								}
							}
						}
					}
				}

				int product_count = persistenceLogic.getProductCountByProductListID(product_list_id);
				log.info("tmall fetch product end, total product count ---->> " + product_count + " list url  ---->> " + productlistURL);
				if (product_count > 0) {
					productList.setStatus(ProductListUrlStatusEnum.CRAWLING_FINISHED.value());
				} else {
					productList.setStatus(ProductListUrlStatusEnum.EMPTY.value());
				}
				persistenceLogic.undateProductList(productList);
			} else {
				log.info("tmall .....开始睡眠");
				try {
					Thread.sleep(1000 * 60 * 60);
					startTime = new Date();
				} catch (InterruptedException e) {
					log.error(e.getMessage() + "tmall --- 睡眠出现异常");
				}
				log.info("tmall .....睡眠结束");
			}
		}
	}

	private List<PriceInfo_TMALL> parseAndSavePrice(String data_id, String mdSkipJSON, String referrerURL, ProductInfo pInfo, int product_list_id, TaskControl tc) {
		List<PriceInfo_TMALL> priceList = new ArrayList<PriceInfo_TMALL>();
		BigDecimal tagPrice = null; // 专柜价
		BigDecimal price = null; // 价格
		BigDecimal promotionPrice = null; // 促销价
		BigDecimal suggestivePromotionPrice = null; // 双11价格
		try {
			JsoupCrawler.setReferrer(referrerURL);
			mdSkipJSON = JsoupCrawler.fetchDocument(html_detail_request_url + data_id, tc.getSite_code()).text();
		} catch (Exception e) {
			log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "TMALL", html_detail_request_url + data_id));
		}

		PriceInfo_TMALL priceInfo = null;
		PriceInfo_TMALL tag_priceInfo = null;
		PriceInfo_TMALL promotion_priceInfo = null;
		PriceInfo_TMALL suggestive_promotion_priceInfo = null;
		JSONObject object = null;
		JSONObject object2 = null;
		JSONObject object3 = null;
		JSONObject object4 = null;
		JSONObject object5 = null;
		try {
			object = new JSONObject(mdSkipJSON);
			if (!object.isNull("defaultModel")) {
				object2 = (JSONObject) object.get("defaultModel");
				if (!object2.isNull("itemPriceResultDO")) {
					object3 = (JSONObject) object2.get("itemPriceResultDO");
					if (!object3.isNull("priceInfo")) {
						object4 = (JSONObject) object3.get("priceInfo");
						if (!object4.isNull("def")) {
							object5 = (JSONObject) object4.get("def");
						} else {
							object5 = object4;
						}

						if (!object5.isNull("price") && !StringUtils.isBlank(object5.get("price").toString())) {
							price = new BigDecimal(object5.get("price").toString());
							priceInfo = new PriceInfo_TMALL();
							priceInfo.setPrice_product_id_fk(data_id);
							priceInfo.setPrice_type(PriceEnum.PRICE.value());
							priceInfo.setPrice(price);
							priceInfo.setProduct_list_id(product_list_id);
							priceList.add(priceInfo);
						}
						if (!object5.isNull("tagPrice") && !StringUtils.isBlank(object5.get("tagPrice").toString())) {
							tagPrice = new BigDecimal(object5.get("tagPrice").toString());
							tag_priceInfo = new PriceInfo_TMALL();
							tag_priceInfo.setPrice_product_id_fk(data_id);
							tag_priceInfo.setPrice_type(PriceEnum.TAG_PRICE.value());
							tag_priceInfo.setPrice(tagPrice);
							tag_priceInfo.setProduct_list_id(product_list_id);
							priceList.add(tag_priceInfo);
						}
						if (!object5.isNull("promotionList") && !StringUtils.isBlank(object5.get("promotionList").toString())) {
							JSONArray jsonArray = new JSONArray(object5.get("promotionList").toString());
							if (jsonArray.length() > 0) {
								JSONObject object6 = (JSONObject) jsonArray.get(0);
								if (!object6.isNull("price") && !StringUtils.isBlank(object6.get("price").toString())) {
									promotionPrice = new BigDecimal(object6.get("price").toString());
									promotion_priceInfo = new PriceInfo_TMALL();
									promotion_priceInfo.setPrice_product_id_fk(data_id);
									promotion_priceInfo.setPrice_type(PriceEnum.PROMOTION_PRICE.value());
									promotion_priceInfo.setPrice(promotionPrice);
									promotion_priceInfo.setProduct_list_id(product_list_id);
									priceList.add(promotion_priceInfo);
								}
							}
						}

						if (!object5.isNull("suggestivePromotionList") && !StringUtils.isBlank(object5.get("suggestivePromotionList").toString())) {
							JSONArray jsonArray = new JSONArray(object5.get("suggestivePromotionList").toString());
							if (jsonArray.length() > 0) {
								JSONObject object6 = (JSONObject) jsonArray.get(0);
								if (!object6.isNull("price") && !StringUtils.isBlank(object6.get("price").toString())) {
									suggestivePromotionPrice = new BigDecimal(object6.get("price").toString());
									suggestive_promotion_priceInfo = new PriceInfo_TMALL();
									suggestive_promotion_priceInfo.setPrice_product_id_fk(data_id);
									suggestive_promotion_priceInfo.setPrice_type(PriceEnum.SUGGESTIVE_PROMOTION_PRICE.value());
									suggestive_promotion_priceInfo.setPrice(suggestivePromotionPrice);
									suggestive_promotion_priceInfo.setProduct_list_id(product_list_id);
									priceList.add(suggestive_promotion_priceInfo);
								}
							}
						}

						object3 = (JSONObject) object2.get("sellCountDO");
						if (!StringUtils.isBlank(object3.get("sellCount").toString())) {
							pInfo.setProduct_month_sale_cnt(Integer.valueOf(object3.get("sellCount").toString()));
						} else {
							pInfo.setProduct_month_sale_cnt(0);
						}
					}
				}
			}
		} catch (JSONException e) {
			log.error(Uni_LOG_Common.Fetch_JSON_ERROR(e.getMessage(), "TMALL", mdSkipJSON, html_detail_request_url + data_id));
		}
		
		int x = 4;
		if(tagPrice == null){
			tagPrice = new BigDecimal(0);
			x--;
		}
		if(price == null){
			price = new BigDecimal(0);
			x--;
		}
		if(promotionPrice == null){
			promotionPrice = new BigDecimal(0);
			x--;
		}
		if(suggestivePromotionPrice == null){
			suggestivePromotionPrice = new BigDecimal(0);
			x--;
		}
		if(x == 0){
			x = 1;
		}
		avg_price = (tagPrice.floatValue() + price.floatValue() +  promotionPrice.floatValue() + suggestivePromotionPrice.floatValue()) / x; 
		return priceList;
	}

	private void parseBadReviewInfo_TMALL(int pageNo, String id, String sellerID, String impressionID, int execute_id, TaskControl tc) {
		String reviewURL = DataUtil.format(html_review_request_url, new String[] { id, sellerID, impressionID, "-1", pageNo + "" });
		String reviewJSON = "";
		String rateContent = "";
		Date rateDate = null;
		Document reviewJSONDOC = null;
		try {
			reviewJSONDOC = JsoupCrawler.fetchDocument(reviewURL, tc.getSite_code());
		} catch (Exception e) {
			log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "TMALL", reviewURL));
		}
		try {
			reviewJSON = reviewJSONDOC.text();
			if (!StringUtil.isBlank(reviewJSON)) {
				JSONObject reviewJsonOBJ = new JSONObject("{" + reviewJSON + "}");
				if (reviewJsonOBJ.has("rateDetail")) {
					JSONObject reviewJsonOBJ2 = (JSONObject) reviewJsonOBJ.get("rateDetail");
					if (reviewJsonOBJ2.has("rateList")) {
						JSONArray reviewJsonArray = (JSONArray) reviewJsonOBJ2.get("rateList");
						if (reviewJsonArray != null && reviewJsonArray.length() > 0) {
							for (int i = 0; i < reviewJsonArray.length(); i++) {
								JSONObject reviewOBJ = (JSONObject) reviewJsonArray.get(i);
								rateContent = reviewOBJ.getString("rateContent");
								rateDate = DataUtil.StrToDate(reviewOBJ.getString("rateDate"), "yyyy-MM-dd HH:mm:ss");
								ReviewBadInfo_TMALL reviewBadInfo_TMALL = new ReviewBadInfo_TMALL();
								reviewBadInfo_TMALL.setReview_bad_info(rateContent);
								reviewBadInfo_TMALL.setReview_bad_time(rateDate);
								reviewBadInfo_TMALL.setReview_impression_id_fk(impressionID);
								reviewBadInfo_TMALL.setProduct_id_fk(id);
								reviewBadInfo_TMALL.setJson(reviewJSON);
								reviewBadInfo_TMALL.setExecute_id(execute_id);
								persistenceLogic.insertReviewBadInfo(reviewBadInfo_TMALL);
							}
						} else {
							return;
						}
					}
				}
			}
			// ++pageNo;
			// parseBadReviewInfo_TMALL(pageNo, id, sellerID, impressionID,
			// reviewBadInfoList);
		} catch (Exception e) {
			log.error(Uni_LOG_Common.Fetch_JSON_ERROR(e.getMessage(), "TMALL", reviewJSON, reviewURL));
			ReviewBadInfo_TMALL reviewBadInfo_TMALL = new ReviewBadInfo_TMALL();
			reviewBadInfo_TMALL.setReview_bad_info(rateContent);
			reviewBadInfo_TMALL.setReview_bad_time(rateDate);
			reviewBadInfo_TMALL.setReview_impression_id_fk(impressionID);
			reviewBadInfo_TMALL.setProduct_id_fk(id);
			reviewBadInfo_TMALL.setJson(reviewJSON);
			reviewBadInfo_TMALL.setExecute_id(execute_id);
			persistenceLogic.insertReviewBadInfo(reviewBadInfo_TMALL);
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
	 * @param referrer
	 *            the referrer to set
	 */
	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}

	/**
	 * @param crawler_hour
	 *            the crawler_hour to set
	 */
	public void setCrawler_hour(int crawler_hour) {
		this.crawler_hour = crawler_hour;
	}

	/**
	 * @param log
	 *            the log to set
	 */
	public static void setLog(Log log) {
		CrawlerLogicImpl.log = log;
	}

	/**
	 * @param html_list_navigation
	 *            the html_list_navigation to set
	 */
	public void setHtml_list_navigation(String html_list_navigation) {
		this.html_list_navigation = html_list_navigation;
	}

	/**
	 * @param html_list_last_page
	 *            the html_list_last_page to set
	 */
	public void setHtml_list_last_page(String html_list_last_page) {
		this.html_list_last_page = html_list_last_page;
	}

	/**
	 * @param html_list_product_block
	 *            the html_list_product_block to set
	 */
	public void setHtml_list_product_block(String html_list_product_block) {
		this.html_list_product_block = html_list_product_block;
	}

	/**
	 * @param html_list_product_title
	 *            the html_list_product_title to set
	 */
	public void setHtml_list_product_title(String html_list_product_title) {
		this.html_list_product_title = html_list_product_title;
	}

	/**
	 * @param html_list_way_type
	 *            the html_list_way_type to set
	 */
	public void setHtml_list_way_type(String html_list_way_type) {
		this.html_list_way_type = html_list_way_type;
	}

	/**
	 * @param html_detail_url_pattern_change
	 *            the html_detail_url_pattern_change to set
	 */
	public void setHtml_detail_url_pattern_change(String html_detail_url_pattern_change) {
		this.html_detail_url_pattern_change = html_detail_url_pattern_change;
	}

	/**
	 * @param html_detail_url_pattern_change_result
	 *            the html_detail_url_pattern_change_result to set
	 */
	public void setHtml_detail_url_pattern_change_result(String html_detail_url_pattern_change_result) {
		this.html_detail_url_pattern_change_result = html_detail_url_pattern_change_result;
	}

	/**
	 * @param html_detail_request_url
	 *            the html_detail_request_url to set
	 */
	public void setHtml_detail_request_url(String html_detail_request_url) {
		this.html_detail_request_url = html_detail_request_url;
	}

	/**
	 * @param html_detail_brand
	 *            the html_detail_brand to set
	 */
	public void setHtml_detail_brand(String html_detail_brand) {
		this.html_detail_brand = html_detail_brand;
	}

	/**
	 * @param html_detail_brand_pattern
	 *            the html_detail_brand_pattern to set
	 */
	public void setHtml_detail_brand_pattern(String html_detail_brand_pattern) {
		this.html_detail_brand_pattern = html_detail_brand_pattern;
	}

	/**
	 * @param html_review_request_url
	 *            the html_review_request_url to set
	 */
	public void setHtml_review_request_url(String html_review_request_url) {
		this.html_review_request_url = html_review_request_url;
	}

	/**
	 * @param html_review_grade_avg_url
	 *            the html_review_grade_avg_url to set
	 */
	public void setHtml_review_grade_avg_url(String html_review_grade_avg_url) {
		this.html_review_grade_avg_url = html_review_grade_avg_url;
	}

	/**
	 * @param html_product_impression_url
	 *            the html_product_impression_url to set
	 */
	public void setHtml_product_impression_url(String html_product_impression_url) {
		this.html_product_impression_url = html_product_impression_url;
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
