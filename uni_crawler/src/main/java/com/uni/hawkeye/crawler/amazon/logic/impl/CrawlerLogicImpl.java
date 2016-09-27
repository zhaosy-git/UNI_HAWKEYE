package com.uni.hawkeye.crawler.amazon.logic.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.uni.hawkeye.crawler.amazon.bean.CategoryInfo;
import com.uni.hawkeye.crawler.amazon.bean.ProductInfo;
import com.uni.hawkeye.crawler.amazon.bean.ProductList;
import com.uni.hawkeye.crawler.amazon.bean.ReviewBadInfo_AMAZON;
import com.uni.hawkeye.crawler.amazon.bean.ReviewImpression_AMAZON;
import com.uni.hawkeye.crawler.amazon.bean.ReviewInfo_AMAZON;
import com.uni.hawkeye.crawler.amazon.bean.TaskControl;
import com.uni.hawkeye.crawler.amazon.enums.CategoryControlStatus;
import com.uni.hawkeye.crawler.amazon.enums.ProductListUrlStatusEnum;
import com.uni.hawkeye.crawler.amazon.logic.CrawlerLogic;
import com.uni.hawkeye.crawler.amazon.logic.PersistenceLogic;
import com.uni.hawkeye.jsoup.JsoupCrawler;
import com.uni.hawkeye.log.Uni_LOG_Common;
import com.uni.hawkeye.utils.DataUtil;

import uni_hawkeye.core.EBTaskInfo;

public class CrawlerLogicImpl implements CrawlerLogic {
	
	private static Log log = LogFactory.getLog(CrawlerLogicImpl.class);

	private static final ResourceBundle RESOURCE_BUNDLE_WHITE_LIST = ResourceBundle.getBundle("white_list_amazon", Locale.getDefault());

	private PersistenceLogic persistenceLogic;
	
	private Pattern p_int = Pattern.compile("\\d+");

	private String html_list_navigation_before;
	private String html_list_navigation_current;
	private String html_list_last_page;
	private String html_list_product_block;
	private String html_list_title;
	private String html_list_brand;
	private String html_list_price;
	private String html_list_review_url;
	private String html_list_review_avg;
	private String html_list_review_count;
	private String html_review_page_url;
	private String html_impression_url;
	private String html_impression_block;
	private String html_impression_title;
	private String html_impression_count;
	private String html_bad_review_content;
	private String html_bad_review_time;

	@Override
	public void crawlCategoryList(TaskControl tc) {
		Iterator<String> white_list = RESOURCE_BUNDLE_WHITE_LIST.keySet().iterator();
		while (white_list.hasNext()) {
			String white_list_url = RESOURCE_BUNDLE_WHITE_LIST.getString(white_list.next());
			String url = DataUtil.format(white_list_url, new String[]{"1", "1"});
			Document doc = null;
			try {
				doc = JsoupCrawler.fetchDocument(url, tc.getSite_code());
			} catch (Exception e) {
				log.error(e.getMessage() + Uni_LOG_Common.Fetch_WhiteList_ERROR("AMAZON", url));
			}
			
			if (doc != null) {
				Elements navigation_before = doc.select(html_list_navigation_before);
				int level = 1;
				String category_parent_id = "";
				if(navigation_before != null && navigation_before.size() > 0){
					for(Element navigation : navigation_before){
						String category_name = navigation.text();
						String href = navigation.attr("href");
						String category_code = "";
						Map<String, String> paramMap = DataUtil.getParam(href);
						String param_rh = "";
						try {
							param_rh = new URLCodec().decode(paramMap.get("rh"));
						} catch (DecoderException e) {
							e.printStackTrace();
						}
						
						if(param_rh.indexOf(",") != -1){
							String[] param_n = param_rh.split(",");
							for(int i=1, x=1; i<=param_n.length; i++,x++){
								String n = param_n[i-1];
								if(n.indexOf("!") != -1){
									x--;
									continue;
								}
								if(level == x){
									category_code = n.split(":")[1];
									break;
								}
							}
						}else{
							category_code = param_rh.split(":")[1];
						}
						CategoryInfo categoryInfo = new CategoryInfo(category_code, category_name, tc.getExecute_id(), "", CategoryControlStatus.UNDO.value(), level+"", category_parent_id);
						persistenceLogic.insertCategoryInfo(categoryInfo);
						category_parent_id = category_code;
						level++;
					}
				}else{
					log.error(Uni_LOG_Common.Parse_Category_ERROR("AMAZON", url));
				}
				
				Elements navigation_current_elements = doc.select(html_list_navigation_current);
				if(navigation_current_elements != null && navigation_current_elements.size() > 0){
					Element navigation_current = navigation_current_elements.get(0);
					String category_name = navigation_current.text();
					String category_code = "";
					Map<String, String> paramMap = DataUtil.getParam(url);
					String param_rh = "";
					try {
						param_rh = new URLCodec().decode(paramMap.get("rh"));
					} catch (DecoderException e) {
						e.printStackTrace();
					}
					if(param_rh.indexOf(",") != -1){
						String[] param_n = param_rh.split(",");
						category_code = param_n[param_n.length-1].split(":")[1];
					}else{
						category_code = param_rh.split(":")[1];
					}
					CategoryInfo categoryInfo = new CategoryInfo(category_code, category_name, tc.getExecute_id(), white_list_url, CategoryControlStatus.UNDO.value(), level+"", category_parent_id);
					persistenceLogic.insertCategoryInfo(categoryInfo);
				}else{
					log.error(Uni_LOG_Common.Parse_Category_ERROR("AMAZON", url));
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
			String url_tmp = DataUtil.format(url, new String[]{"1", "1"});
			Document doc = null;
			try {
				doc = JsoupCrawler.fetchDocument(url_tmp, tc.getSite_code());
			} catch (Exception e) {
				log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "AMAZON", url_tmp));
			}
			ProductList productList = new ProductList(catInfo.getCategory_id(), catInfo.getExecute_id(), 1, url_tmp, ProductListUrlStatusEnum.UNDO.value()+"");
			persistenceLogic.insertProductList(productList);
			int lastPage = 0;
			if(doc != null){
				String pageCount = doc.select(html_list_last_page).text();
				Matcher m = p_int.matcher(pageCount);
				while(m.find()){
					pageCount = m.group();
				}
				if(!StringUtil.isBlank(pageCount)){
					lastPage = Integer.valueOf(pageCount);
				}
			}
			for(int j=1; j<=lastPage; j++){
				productList = new ProductList(catInfo.getCategory_id(), catInfo.getExecute_id(), j+1, DataUtil.format(url, new String[]{j+1+"", j+1+""}), ProductListUrlStatusEnum.UNDO.value()+"");
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
			log.info(Uni_LOG_Common.Fetch_Product_Begin("AMAZON", productlistURL));
			Document doc = null;
			try {
				doc = JsoupCrawler.fetchDocument(productlistURL, tc.getSite_code());
			} catch (Exception e) {
				log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "AMAZON", productlistURL));
			}
			if(doc != null){
				Elements product_block_elements = doc.select(html_list_product_block);
				for(Element product_block_element : product_block_elements){
					String product_id = product_block_element.attr("data-asin");
					log.info(Uni_LOG_Common.Print_Product_ID("AMAZON", product_id));
					Element titleElement = product_block_element.select(html_list_title).get(0);
					String product_title = titleElement.attr("title");
					String url = titleElement.attr("href");
					if(url.indexOf("/gp") != -1){
						URL url_ = null;
						try {
							url_ = Jsoup.connect("http://www.amazon.cn"+url).followRedirects(true).execute().url();
							url = url_.toString();
						} catch (IOException e) {
							log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "AMAZON", url));
						}
					}
					String product_brand = product_block_element.select(html_list_brand).text();
					Elements price_elts = product_block_element.select(html_list_price);
					if(price_elts != null && price_elts.size() > 0){
						BigDecimal price = new BigDecimal(price_elts.get(0).text().replace("￥", "").replace(",", ""));

						if(product_title.contains(key_word)){
							if(p_l.floatValue() <= price.floatValue() && p_h.floatValue() >= price.floatValue()){
								int status = 0;
								String json = "";
								ProductInfo productInfo = new ProductInfo(product_id, product_title, product_brand, price, status, product_list_id, url, json);
								persistenceLogic.insertProductInfo(productInfo);
								
								Document reviewDOC = null;
								String review_json_url = DataUtil.format(html_list_review_url, new String[]{product_id});
								try {
									reviewDOC = JsoupCrawler.fetchDocument(review_json_url, tc.getSite_code());
								} catch (Exception e) {
									log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "AMAZON", review_json_url));
								}
								if(reviewDOC != null){
									float avg_float = 0;
									String avg_str = "";
									Elements review_avg_elements = reviewDOC.select(html_list_review_avg);
									if(review_avg_elements != null && review_avg_elements.size() > 0){
										avg_str = review_avg_elements.get(0).text();
										avg_str = avg_str.replace("平均", "").replace("星", "").trim();
										avg_float = Float.valueOf(avg_str);
										Element count_5 = reviewDOC.select(html_list_review_count).get(0);
										Element count_4 = reviewDOC.select(html_list_review_count).get(1);
										Element count_3 = reviewDOC.select(html_list_review_count).get(2);
										Element count_2 = reviewDOC.select(html_list_review_count).get(3);
										Element count_1 = reviewDOC.select(html_list_review_count).get(4);
										int good_cnt = formatMath(count_5.text()) + formatMath(count_4.text());
										int general_cnt = formatMath(count_3.text()) + formatMath(count_2.text());
										int poor_cnt = formatMath(count_1.text());
										ReviewInfo_AMAZON reviewInfo_AMAZON = new ReviewInfo_AMAZON(product_id, good_cnt, general_cnt, poor_cnt, avg_float, "", tc.getExecute_id());
										persistenceLogic.insertReviewInfo(reviewInfo_AMAZON);
									}
								}
								
								Document reviewPageDOC = null;
								String reviewURL = DataUtil.format(html_review_page_url, new String[]{product_id});
								try {
									reviewPageDOC = JsoupCrawler.fetchDocument(reviewURL, tc.getSite_code());
								} catch (Exception e) {
									log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "AMAZON", reviewURL));
								}
								if(reviewPageDOC != null){
									Elements bad_review_content_elts = reviewPageDOC.select(html_bad_review_content);
									Elements bad_review_time_elts = reviewPageDOC.select(html_bad_review_time);
									for(int j=0; j<bad_review_content_elts.size(); j++){
										Element bad_review_content = bad_review_content_elts.get(j);
										String bad_review_time_str = "";
										if(bad_review_time_elts != null && bad_review_time_elts.size() > 0){
											Element bad_review_time = bad_review_time_elts.get(j);
											bad_review_time_str = bad_review_time.text();
										}
										String bad_review_content_str = bad_review_content.text();
										bad_review_time_str = bad_review_time_str.replace("于", "");
										bad_review_time_str = bad_review_time_str.replace("年", "-").replace("月", "-").replace("日", "-");
										Date bad_review_time_date = DataUtil.StrToDate(bad_review_time_str, "yyyy-MM-dd");
										ReviewBadInfo_AMAZON reviewBadInfo_AMAZON = new ReviewBadInfo_AMAZON(bad_review_content_str, bad_review_time_date, product_id, "", tc.getExecute_id());
										persistenceLogic.insertReviewBadInfo(reviewBadInfo_AMAZON);
									}
								}
								
								Document impressionPageDOC = null;
								String impressionURL = DataUtil.format(html_impression_url, new String[]{product_id});
								try {
									impressionPageDOC = JsoupCrawler.fetchDocument(impressionURL, tc.getSite_code());
								} catch (Exception e) {
									log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "AMAZON", impressionURL));
								}
								if(impressionPageDOC != null){
									ReviewImpression_AMAZON reviewImpression_AMAZON = null;
									Elements review_blocks = impressionPageDOC.select(html_impression_block);
									for(int j=0; j<review_blocks.size(); j++){
										String title = review_blocks.get(j).select(html_impression_title).text();
										String count = review_blocks.get(j).select(html_impression_count).text();
										Matcher m = p_int.matcher(count);
										while(m.find()){
											count = m.group();
										}
										if(StringUtil.isBlank(count)){
											count = "0";
										}
										reviewImpression_AMAZON = new ReviewImpression_AMAZON(product_id + "_" + j, product_id, title, Integer.valueOf(count), "", tc.getExecute_id());
										persistenceLogic.insertReviewImpressionInfo(reviewImpression_AMAZON);
									}
								}
							}
						}
					}
				}
			}
			
			int product_count = persistenceLogic.getProductCountByProductListID(product_list_id);
			log.info("amazon fetch product end, total product count ---->> " + product_count + " list url  ---->> " + productlistURL);
			if(product_count > 0){
				productList.setStatus(ProductListUrlStatusEnum.CRAWLING_FINISHED.value()+"");
			}else{
				productList.setStatus(ProductListUrlStatusEnum.EMPTY.value()+"");
			}
			persistenceLogic.undateProductList(productList);
			
		}
	}
	
	private int formatMath(String count) {
		if(count.indexOf(",") != -1){
			count = count.replace(",", "");
		}
		return Integer.valueOf(count);
	}

	/**
	 * @param persistenceLogic the persistenceLogic to set
	 */
	public void setPersistenceLogic(PersistenceLogic persistenceLogic) {
		this.persistenceLogic = persistenceLogic;
	}

	/**
	 * @param html_list_navigation_before the html_list_navigation_before to set
	 */
	public void setHtml_list_navigation_before(String html_list_navigation_before) {
		this.html_list_navigation_before = html_list_navigation_before;
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
	 * @param html_list_brand the html_list_brand to set
	 */
	public void setHtml_list_brand(String html_list_brand) {
		this.html_list_brand = html_list_brand;
	}

	/**
	 * @param html_list_price the html_list_price to set
	 */
	public void setHtml_list_price(String html_list_price) {
		this.html_list_price = html_list_price;
	}

	/**
	 * @param html_list_review_url the html_list_review_url to set
	 */
	public void setHtml_list_review_url(String html_list_review_url) {
		this.html_list_review_url = html_list_review_url;
	}

	/**
	 * @param html_list_review_avg the html_list_review_avg to set
	 */
	public void setHtml_list_review_avg(String html_list_review_avg) {
		this.html_list_review_avg = html_list_review_avg;
	}

	/**
	 * @param html_list_review_count the html_list_review_count to set
	 */
	public void setHtml_list_review_count(String html_list_review_count) {
		this.html_list_review_count = html_list_review_count;
	}

	/**
	 * @param html_review_page_url the html_review_page_url to set
	 */
	public void setHtml_review_page_url(String html_review_page_url) {
		this.html_review_page_url = html_review_page_url;
	}

	/**
	 * @param html_impression_url the html_impression_url to set
	 */
	public void setHtml_impression_url(String html_impression_url) {
		this.html_impression_url = html_impression_url;
	}

	/**
	 * @param html_impression_block the html_impression_block to set
	 */
	public void setHtml_impression_block(String html_impression_block) {
		this.html_impression_block = html_impression_block;
	}

	/**
	 * @param html_impression_title the html_impression_title to set
	 */
	public void setHtml_impression_title(String html_impression_title) {
		this.html_impression_title = html_impression_title;
	}

	/**
	 * @param html_impression_count the html_impression_count to set
	 */
	public void setHtml_impression_count(String html_impression_count) {
		this.html_impression_count = html_impression_count;
	}

	/**
	 * @param html_bad_review_content the html_bad_review_content to set
	 */
	public void setHtml_bad_review_content(String html_bad_review_content) {
		this.html_bad_review_content = html_bad_review_content;
	}

	/**
	 * @param html_bad_review_time the html_bad_review_time to set
	 */
	public void setHtml_bad_review_time(String html_bad_review_time) {
		this.html_bad_review_time = html_bad_review_time;
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
		String brand_id = tc.getExecute_id()+"_bid_"+brand;
		CategoryInfo brand_info = new CategoryInfo(brand_id, brand, tc.getExecute_id(), brand_href, CategoryControlStatus.UNDO.value(), "3", category_2_id);
		persistenceLogic.insertCategoryInfo(brand_info);
	}

}
