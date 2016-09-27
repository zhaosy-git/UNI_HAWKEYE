package com.uni.hawkeye.crawler.baidu_tieba.logic.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.uni.hawkeye.crawler.baidu_tieba.bean.BarInfo;
import com.uni.hawkeye.crawler.baidu_tieba.bean.CategoryInfo;
import com.uni.hawkeye.crawler.baidu_tieba.bean.TaskControl;
import com.uni.hawkeye.crawler.baidu_tieba.bean.TieziInfo;
import com.uni.hawkeye.crawler.baidu_tieba.bean.UserInfo;
import com.uni.hawkeye.crawler.baidu_tieba.enums.BarControlStatus;
import com.uni.hawkeye.crawler.baidu_tieba.enums.CategoryControlStatus;
import com.uni.hawkeye.crawler.baidu_tieba.logic.CrawlerLogic;
import com.uni.hawkeye.crawler.baidu_tieba.logic.PersistenceLogic;
import com.uni.hawkeye.jsoup.JsoupCrawler;
import com.uni.hawkeye.log.Uni_LOG_Common;
import com.uni.hawkeye.utils.DataUtil;

import uni_hawkeye.core.TiebaTaskInfo;

public class CrawlerLogicImpl implements CrawlerLogic {
	
	private static Log log = LogFactory.getLog(CrawlerLogicImpl.class);

	private static final ResourceBundle RESOURCE_BUNDLE_WHITE_LIST = ResourceBundle.getBundle("white_list_baidu_tieba", Locale.getDefault());

	private PersistenceLogic persistenceLogic;
	
	private String baidu_tieba_base_url;
	private String baidu_tieba_category_all_url;
	
	private String baidu_tieba_html_category_block;
	private String baidu_tieba_html_category_block_level_1;
	private String baidu_tieba_html_category_block_level_2;
	
	private String baidu_tieba_html_bar_last_page;
	private String baidu_tieba_html_bar_block;
	private String baidu_tieba_html_bar_href;
	private String baidu_tieba_html_bar_title;
	private String baidu_tieba_html_bar_user_cnt;
	private String baidu_tieba_html_bar_tiezi_cnt;
	private String baidu_tieba_html_bar_summary;
	
	private String baidu_tieba_html_tiezi_list_page_block;
	private String baidu_tieba_html_tiezi_list_page_title_a;
	private String baidu_tieba_html_tiezi_list_page_last_page;
	
	private String baidu_tieba_html_tiezi_detail_page_last_page;
	private String baidu_tieba_html_tiezi_detail_title;
	private String baidu_tieba_html_tiezi_detail_block;
	private String baidu_tieba_html_tiezi_detail_post_time_root_in_floor;
	private String baidu_tieba_html_tiezi_detail_user;
	private String baidu_tieba_html_tiezi_detail_text;
	private String baidu_tieba_html_tiezi_detail_louzhu_flag;
	
	private String baidu_tieba_html_user_detail_name;
	private String baidu_tieba_html_user_detail_bar_age;
	private String baidu_tieba_html_user_detail_tiezi_cnt;

	@Override
	public void crawlCategoryList(TaskControl tc) {
		Document doc = null;
		try {
			doc = JsoupCrawler.fetchDocument(baidu_tieba_category_all_url, tc.getSite_code());
		} catch (Exception e) {
			log.error(e.getMessage() + Uni_LOG_Common.Fetch_WhiteList_ERROR("BAIDU_TIEBA", baidu_tieba_category_all_url));
		}
		
		if (doc != null) {
			Elements category_blocks = doc.select(baidu_tieba_html_category_block);
			if(!category_blocks.isEmpty()){
				CategoryInfo cInfo = null;
				for(int i=0; i<category_blocks.size(); i++){
					Element category_block = category_blocks.get(i);
					log.info("----block : "+i+" -----");
					Elements level_1_elts = category_block.select(baidu_tieba_html_category_block_level_1);
					String level_1 = "";
					if(!level_1_elts.isEmpty()){
						level_1 = level_1_elts.text();
						String category_code = level_1;
						String category_name = level_1;
						String category_level = "1";
						int category_parent_id = 0;
						String category_url = "";
						String status = CategoryControlStatus.UNDO.value();
						int execute_id = tc.getExecute_id();
						cInfo = new CategoryInfo(category_code, category_name, category_level, category_parent_id, category_url, status, execute_id);
						persistenceLogic.insertCategoryInfo(cInfo);
					}
					Integer category_parent_id = cInfo.getCategory_id();
					Elements level_2_elts = category_block.select(baidu_tieba_html_category_block_level_2);
					if(!level_2_elts.isEmpty()){
						for(int j=0; j<level_2_elts.size(); j++){
							Element level_2_elt = level_2_elts.get(j);
							String level_2 = level_2_elt.text();
							String category_code = level_2+"_"+j;
							String category_name = level_2;
							String category_level = "2";
							String category_url = baidu_tieba_base_url + level_2_elt.attr("href");
							String status = CategoryControlStatus.UNDO.value();
							int execute_id = tc.getExecute_id();
							cInfo = new CategoryInfo(category_code, category_name, category_level, category_parent_id, category_url, status, execute_id);
							persistenceLogic.insertCategoryInfo(cInfo);
						}
					}
				}
			}
		}
	}

	@Override
	public void crawlerBarList(TaskControl tc, TiebaTaskInfo tiebaInfo) {
		if(tiebaInfo == null){
			Iterator<String> white_list = RESOURCE_BUNDLE_WHITE_LIST.keySet().iterator();
			if(white_list.hasNext()){
				while (white_list.hasNext()) {
					String white_list_target = RESOURCE_BUNDLE_WHITE_LIST.getString(white_list.next());
					String target_category_name = white_list_target.split("-->")[0];
					crawlerBarList_have_or_nohave(tc, target_category_name, null);
				}
			}else{
				crawlerBarList_have_or_nohave(tc, null, null);
			}
		}else{
			crawlerBarList_have_or_nohave(tc, tiebaInfo.getCategory_2_name(), tiebaInfo);
		}
	}

	private void crawlerBarList_have_or_nohave(TaskControl tc, String target_category_name, TiebaTaskInfo tiebaInfo) {
		
		String barName = "";
		
		if(tiebaInfo != null){
			barName = tiebaInfo.getBar_name();
		}
		
		List<CategoryInfo> categoryList = persistenceLogic.getCategoryInfoByStatus(tc.getExecute_id(), CategoryControlStatus.UNDO.value(), target_category_name);
		for(int i=0; i<categoryList.size(); i++){
			CategoryInfo cInfo = categoryList.get(i);
			
			cInfo.setStatus(CategoryControlStatus.CRAWLING.value());
			persistenceLogic.updateCategoryInfoStatus(cInfo);
			
			String cHref = cInfo.getCategory_url();
			
			Document doc = null;
			try {
				doc = JsoupCrawler.fetchDocument(cHref, tc.getSite_code());
			} catch (Exception e) {
				log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "BAIDU_TIEBA_BAR", cHref));
			}
			
			if(doc != null){
				String lastPageHref = doc.select(baidu_tieba_html_bar_last_page).attr("href");
				int lastPageNO = 0;
				if(!DataUtil.getParam(lastPageHref).isEmpty() && DataUtil.getParam(lastPageHref).get("pn") != null){
					lastPageNO = Integer.valueOf(DataUtil.getParam(lastPageHref).get("pn"));
				}
				
				for(int j=1; j<=lastPageNO; j++){
					try {
						doc = JsoupCrawler.fetchDocument(cHref + "&pn="+j, tc.getSite_code());
					} catch (Exception e) {
						log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "BAIDU_TIEBA_BAR CHANGE PAGE", cHref));
					}
					Elements bars = doc.select(baidu_tieba_html_bar_block);
					if(!bars.isEmpty()){
						BarInfo barInfo = null;
						for(int k=0; k<bars.size(); k++){
							Element bar = bars.get(k);
							String title = bar.select(baidu_tieba_html_bar_title).text();
							if(title.contains(barName)){
								String href = baidu_tieba_base_url + bar.select(baidu_tieba_html_bar_href).attr("href");
								int user_cnt = Integer.valueOf(bar.select(baidu_tieba_html_bar_user_cnt).text());
								int tiezi_cnt = Integer.valueOf(bar.select(baidu_tieba_html_bar_tiezi_cnt).text());
								String summary = bar.select(baidu_tieba_html_bar_summary).text();
								barInfo = new BarInfo(title, user_cnt, tiezi_cnt, summary, href, cInfo.getCategory_id(), BarControlStatus.UNDO.value());
								persistenceLogic.insertBarInfo(barInfo);
							}
						}
					}
				}
			}
			
			cInfo.setStatus(CategoryControlStatus.CRAWLING_FINISHED.value());
			persistenceLogic.updateCategoryInfoStatus(cInfo);
		}
	}
	
	@Override
	public void crawlerTiezi(TaskControl tc, TiebaTaskInfo tiebaInfo) {
		if(tiebaInfo == null){
			Iterator<String> white_list = RESOURCE_BUNDLE_WHITE_LIST.keySet().iterator();
			if(white_list.hasNext()){
				while (white_list.hasNext()) {
					String white_list_target = RESOURCE_BUNDLE_WHITE_LIST.getString(white_list.next());
					String target_bar_name = white_list_target.split("-->")[1];
					crawlerTiezi_have_or_nohave(tc, target_bar_name, null);
				}
			}else{
				crawlerTiezi_have_or_nohave(tc, null, null);
			}
		}else{
			crawlerTiezi_have_or_nohave(tc, tiebaInfo.getBar_name(), tiebaInfo);
		}
	}

	private void crawlerTiezi_have_or_nohave(TaskControl tc, String target_bar_name, TiebaTaskInfo tiebaInfo) {
		List<BarInfo> barList = persistenceLogic.getBarInfoByStatus(tc.getExecute_id(), BarControlStatus.CRAWLING_FINISHED.value(), target_bar_name);
		for(BarInfo bInfo : barList){
			int barID = bInfo.getTieba_id();
			String barURL = bInfo.getTieba_url();
			
			bInfo.setStatus(BarControlStatus.CRAWLING.value());
			persistenceLogic.updateBarInfoStatus(bInfo);
			
			Document doc = null;
			try {
				doc = JsoupCrawler.fetchDocument(barURL, tc.getSite_code());
			} catch (Exception e) {
				log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "BAIDU_TIEBA_TIEZI", barURL));
			}
			if(doc != null){
				int lastPageNO = 1;
				String listPageLastPageHref = doc.select(baidu_tieba_html_tiezi_list_page_last_page).attr("href");
				if(!StringUtil.isBlank(listPageLastPageHref)){
					lastPageNO = Integer.valueOf(DataUtil.getParam(listPageLastPageHref).get("pn"));
					lastPageNO = lastPageNO / 50 + 1;
				}
				int pn = 0;
				for(int i=0; i<lastPageNO; i++){
					String tieziChangePageHref = barURL+"&pn="+pn;
					try {
						doc = JsoupCrawler.fetchDocument(tieziChangePageHref, tc.getSite_code());
					} catch (Exception e) {
						log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "BAIDU_TIEBA_TIEZI_CHANGE_PAGE", tieziChangePageHref));
					}
					Elements interviewzero_dls = doc.select(baidu_tieba_html_tiezi_list_page_block);
					for(int j=0; j<interviewzero_dls.size(); j++){
						Element interviewzero_dl = interviewzero_dls.get(j);
						if(interviewzero_dl != null){
							String title_href = interviewzero_dl.select(baidu_tieba_html_tiezi_list_page_title_a).attr("href");
							if(!StringUtil.isBlank(title_href)){
								try {
									if(!title_href.contains(baidu_tieba_base_url)){
										title_href = baidu_tieba_base_url + title_href;
									}
									log.info("帖子URL\t"+title_href);
									doc = JsoupCrawler.fetchDocument(title_href, tc.getSite_code());
								} catch (Exception e) {
									log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "BAIDU_TIEBA_TIEZI_DETAIL", title_href));
								}
								Elements pageLastPages = doc.select(baidu_tieba_html_tiezi_detail_page_last_page);
								if(pageLastPages == null || pageLastPages.isEmpty()){
									fetchTieziInfo(doc, barID, title_href, j, tiebaInfo, tc.getSite_code());
								}else{
									int detailPageLastPageNO = Integer.valueOf(pageLastPages.text());
									if(detailPageLastPageNO == 1){
										fetchTieziInfo(doc, barID, title_href, j, tiebaInfo, tc.getSite_code());
									}else{
										if(detailPageLastPageNO > 1){
											for(int k=1; k<=detailPageLastPageNO; k++){
												String detailChangePageHref = title_href+"?pn="+k;
												try {
													doc = JsoupCrawler.fetchDocument(detailChangePageHref, tc.getSite_code());
												} catch (Exception e) {
													log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "BAIDU_TIEBA_TIEZI_DETAIL_CHANGE_PAGE", detailChangePageHref));
												}
												fetchTieziInfo(doc, barID, detailChangePageHref, j, tiebaInfo, tc.getSite_code());
											}
										}
									}
								}
							}
						}
					}
					pn+=50;
				}
			}
			
			bInfo.setStatus(BarControlStatus.CRAWLING_FINISHED.value());
			persistenceLogic.updateBarInfoStatus(bInfo);
		}
	}

	private void fetchTieziInfo(Document doc, int barID, String url, int tieziList_index, TiebaTaskInfo tiebaInfo, String site_code) {
		
		String content_key_word = "";
		String tieba_begin_time = "";
		String tieba_end_time = "";
		if(tiebaInfo != null){
			content_key_word = tiebaInfo.getContent_key_word();
			tieba_begin_time = tiebaInfo.getCrawler_tieba_begin_time();
			tieba_begin_time = StringUtil.isBlank(tieba_begin_time) ? "1900-01-01" : tieba_begin_time;
			tieba_end_time = tiebaInfo.getCrawler_tieba_end_time();
			tieba_end_time = StringUtil.isBlank(tieba_end_time) ? "3000-01-01" : tieba_end_time;
		}
		
		String tieziTitle = doc.select(baidu_tieba_html_tiezi_detail_title).text();
		log.info("----------------------------");
		log.info("贴吧标题\t"+tieziTitle);
		Elements detailPageBlocks = doc.select(baidu_tieba_html_tiezi_detail_block);
		if(!detailPageBlocks.isEmpty()){
			for(int l=0; l<detailPageBlocks.size(); l++){
				log.info("----------------------------");
				Element detailPageBlock = detailPageBlocks.get(l);
				
				String tiezi_id = "";
				String dataField = detailPageBlock.attr("data-field");
				int blockFloor = 0;
				String blockPostTime = "";
				int replyCnt = 0;
				String rootIn = "";
				try {
					JSONObject dataField_json_obj = new JSONObject(dataField);
					if(!dataField_json_obj.isNull("content")){
						JSONObject content_json_obj = dataField_json_obj.getJSONObject("content");
						if(!content_json_obj.isNull("post_no")){
							blockFloor = content_json_obj.getInt("post_no");
						} else{
							String blockFloorStr = detailPageBlock.select(baidu_tieba_html_tiezi_detail_post_time_root_in_floor).text();;
							if(blockFloorStr.indexOf("楼") != -1){
								blockFloor = Integer.valueOf(blockFloorStr.replace("楼", ""));
							}
						}
						if(!content_json_obj.isNull("date")){
							blockPostTime = content_json_obj.getString("date");
						}else{
							blockPostTime = detailPageBlock.select(baidu_tieba_html_tiezi_detail_post_time_root_in_floor).text();

							Pattern p = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}");
							Matcher m = p.matcher(blockPostTime);
							if(m.find()){
								blockPostTime = m.group(0);
							}
						}
						
						if(StringUtil.isBlank(blockPostTime)){
							continue;
						}
						Date post_time = DataUtil.StrToDate(blockPostTime, "yyyy-MM-dd");
						Date begin_time = DataUtil.StrToDate(tieba_begin_time, "yyyy-MM-dd");
						Date end_time = DataUtil.StrToDate(tieba_end_time, "yyyy-MM-dd");
						if(post_time.getTime() < begin_time.getTime() || post_time.getTime() > end_time.getTime()){
							continue;
						}
						
						if(!content_json_obj.isNull("comment_num")){
							replyCnt = content_json_obj.getInt("comment_num");
						}
						if(!content_json_obj.isNull("open_type")){
							rootIn = content_json_obj.getString("open_type");
						}else{
							rootIn = detailPageBlock.select(baidu_tieba_html_tiezi_detail_post_time_root_in_floor).text();
							if(rootIn.indexOf("来自") != -1 && rootIn.indexOf("客户端") != -1){
								rootIn = rootIn.substring(rootIn.indexOf("来自") + 2, rootIn.indexOf("客户端"));
							} else{
								rootIn = "";
							}
						}
					}
				} catch (JSONException e) {
					log.error(Uni_LOG_Common.Fetch_JSON_ERROR(e.getMessage(), "BAIDU_TIEBA", dataField, url));
				}
				tiezi_id = barID +"_"+ tieziList_index +"_"+ blockFloor;
				String blockText = detailPageBlock.select(baidu_tieba_html_tiezi_detail_text).text();
				String blockText_tmp = blockText.trim().replace(" ", "").toUpperCase();
				String content_key_word_tmp = content_key_word.trim().replace(" ", "").toUpperCase();
				if(!blockText_tmp.contains(content_key_word_tmp)){
					continue;
				}
				Elements louzhus = detailPageBlock.select(baidu_tieba_html_tiezi_detail_louzhu_flag);
				String isMaster = "0";
				if(!louzhus.isEmpty()){
					isMaster = "1";
				}
				
				Elements users = detailPageBlock.select(baidu_tieba_html_tiezi_detail_user);
				String userID = "";
				if(!users.isEmpty()){
					userID = fetchUserInfo(url, users, site_code);
				}
				
				TieziInfo tieziInfo = new TieziInfo(tiezi_id, userID, tieziTitle, blockText, blockFloor, blockPostTime, replyCnt, isMaster, rootIn, "", barID);
				persistenceLogic.insertTieziInfo(tieziInfo);
				log.info("正文\t"+blockText);
				log.info("楼层\t"+blockFloor);
				log.info("发表时间\t"+blockPostTime);
				log.info("回复数\t"+replyCnt);
				log.info("是否楼主\t"+isMaster);
				log.info("来自于\t"+rootIn);
				log.info("----------------------------");
			}
		}
	}
	
	private String fetchUserInfo(String url, Elements users, String site_code) {
		String userID;
		userID = users.attr("data-field");
		JSONObject user_json_obj;
		try {
			user_json_obj = new JSONObject(userID);
			if(user_json_obj.has("user_id")){
				userID = user_json_obj.getString("user_id");
				if(StringUtil.isBlank(userID) || "null".equals(userID) || "".equals(userID)){
					userID = "manulSet_user_id" + new Date().getTime();
				}
			}
		} catch (JSONException e) {
			log.error(Uni_LOG_Common.Fetch_JSON_ERROR(e.getMessage(), "BAIDU_TIEBA", userID, url));
		}
		String userDetailHREF = baidu_tieba_base_url + users.select("li > a").attr("href");
		Document userDOC = null;
		try {
			userDOC = JsoupCrawler.fetchDocument(userDetailHREF, site_code);
		} catch (Exception e) {
			log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "BAIDU_TIEBA", userDetailHREF));
		}
		String userName = "";
		float barAge = 0;
		int tieziCNT = 0;
		if(userDOC != null){
			Elements userNames = userDOC.select(baidu_tieba_html_user_detail_name);
			if(!userNames.isEmpty()){
				userName = userNames.text();
			}
			Elements barAges = userDOC.select(baidu_tieba_html_user_detail_bar_age);
			if(!barAges.isEmpty()){
				String barAgeStr = barAges.text();
				if(!StringUtil.isBlank(barAgeStr)){
					barAgeStr = barAgeStr.replace("吧龄:", "").replace("年", "").trim();
					if(!"-".equals(barAgeStr)){
						barAge = Float.valueOf(barAgeStr);
					}
				}
			}
			Elements tieziCNTs = userDOC.select(baidu_tieba_html_user_detail_tiezi_cnt);
			if(!tieziCNTs.isEmpty()){
				String tieziCNTStr = tieziCNTs.text();
				if(!StringUtil.isBlank(tieziCNTStr)){
					tieziCNTStr = tieziCNTStr.replace("发贴:", "");
					if(tieziCNTStr.indexOf("万") != -1){
						tieziCNT = (int)(Float.valueOf(tieziCNTStr.replace("万", "")) * 10000);
					}else{
						tieziCNT = Integer.valueOf(tieziCNTStr);
					}
				}
			}
		}
		log.info(userID +" -- "+ userName +" -- "+ barAge +" -- "+ tieziCNT);
		if(!StringUtil.isBlank(userID)){
			UserInfo userInfo = new UserInfo(userID, userName, barAge, tieziCNT);
			persistenceLogic.insertUserInfo(userInfo);
		}
		return userID;
	}

	/**
	 * @param persistenceLogic the persistenceLogic to set
	 */
	public void setPersistenceLogic(PersistenceLogic persistenceLogic) {
		this.persistenceLogic = persistenceLogic;
	}

	/**
	 * @param baidu_tieba_base_url the baidu_tieba_base_url to set
	 */
	public void setBaidu_tieba_base_url(String baidu_tieba_base_url) {
		this.baidu_tieba_base_url = baidu_tieba_base_url;
	}

	/**
	 * @param baidu_tieba_category_all_url the baidu_tieba_category_all_url to set
	 */
	public void setBaidu_tieba_category_all_url(String baidu_tieba_category_all_url) {
		this.baidu_tieba_category_all_url = baidu_tieba_category_all_url;
	}

	/**
	 * @param baidu_tieba_html_category_block the baidu_tieba_html_category_block to set
	 */
	public void setBaidu_tieba_html_category_block(String baidu_tieba_html_category_block) {
		this.baidu_tieba_html_category_block = baidu_tieba_html_category_block;
	}

	/**
	 * @param baidu_tieba_html_category_block_level_1 the baidu_tieba_html_category_block_level_1 to set
	 */
	public void setBaidu_tieba_html_category_block_level_1(String baidu_tieba_html_category_block_level_1) {
		this.baidu_tieba_html_category_block_level_1 = baidu_tieba_html_category_block_level_1;
	}

	/**
	 * @param baidu_tieba_html_category_block_level_2 the baidu_tieba_html_category_block_level_2 to set
	 */
	public void setBaidu_tieba_html_category_block_level_2(String baidu_tieba_html_category_block_level_2) {
		this.baidu_tieba_html_category_block_level_2 = baidu_tieba_html_category_block_level_2;
	}

	/**
	 * @param baidu_tieba_html_bar_last_page the baidu_tieba_html_bar_last_page to set
	 */
	public void setBaidu_tieba_html_bar_last_page(String baidu_tieba_html_bar_last_page) {
		this.baidu_tieba_html_bar_last_page = baidu_tieba_html_bar_last_page;
	}

	/**
	 * @param baidu_tieba_html_bar_block the baidu_tieba_html_bar_block to set
	 */
	public void setBaidu_tieba_html_bar_block(String baidu_tieba_html_bar_block) {
		this.baidu_tieba_html_bar_block = baidu_tieba_html_bar_block;
	}

	/**
	 * @param baidu_tieba_html_tiezi_detail_user the baidu_tieba_html_tiezi_detail_user to set
	 */
	public void setBaidu_tieba_html_tiezi_detail_user(String baidu_tieba_html_tiezi_detail_user) {
		this.baidu_tieba_html_tiezi_detail_user = baidu_tieba_html_tiezi_detail_user;
	}

	/**
	 * @param baidu_tieba_html_bar_href the baidu_tieba_html_bar_href to set
	 */
	public void setBaidu_tieba_html_bar_href(String baidu_tieba_html_bar_href) {
		this.baidu_tieba_html_bar_href = baidu_tieba_html_bar_href;
	}

	/**
	 * @param baidu_tieba_html_bar_title the baidu_tieba_html_bar_title to set
	 */
	public void setBaidu_tieba_html_bar_title(String baidu_tieba_html_bar_title) {
		this.baidu_tieba_html_bar_title = baidu_tieba_html_bar_title;
	}

	/**
	 * @param baidu_tieba_html_bar_user_cnt the baidu_tieba_html_bar_user_cnt to set
	 */
	public void setBaidu_tieba_html_bar_user_cnt(String baidu_tieba_html_bar_user_cnt) {
		this.baidu_tieba_html_bar_user_cnt = baidu_tieba_html_bar_user_cnt;
	}

	/**
	 * @param baidu_tieba_html_bar_tiezi_cnt the baidu_tieba_html_bar_tiezi_cnt to set
	 */
	public void setBaidu_tieba_html_bar_tiezi_cnt(String baidu_tieba_html_bar_tiezi_cnt) {
		this.baidu_tieba_html_bar_tiezi_cnt = baidu_tieba_html_bar_tiezi_cnt;
	}

	/**
	 * @param baidu_tieba_html_bar_summary the baidu_tieba_html_bar_summary to set
	 */
	public void setBaidu_tieba_html_bar_summary(String baidu_tieba_html_bar_summary) {
		this.baidu_tieba_html_bar_summary = baidu_tieba_html_bar_summary;
	}

	/**
	 * @param baidu_tieba_html_tiezi_list_page_block the baidu_tieba_html_tiezi_list_page_block to set
	 */
	public void setBaidu_tieba_html_tiezi_list_page_block(String baidu_tieba_html_tiezi_list_page_block) {
		this.baidu_tieba_html_tiezi_list_page_block = baidu_tieba_html_tiezi_list_page_block;
	}

	/**
	 * @param baidu_tieba_html_tiezi_list_page_title_a the baidu_tieba_html_tiezi_list_page_title_a to set
	 */
	public void setBaidu_tieba_html_tiezi_list_page_title_a(String baidu_tieba_html_tiezi_list_page_title_a) {
		this.baidu_tieba_html_tiezi_list_page_title_a = baidu_tieba_html_tiezi_list_page_title_a;
	}

	/**
	 * @param baidu_tieba_html_tiezi_list_page_last_page the baidu_tieba_html_tiezi_list_page_last_page to set
	 */
	public void setBaidu_tieba_html_tiezi_list_page_last_page(String baidu_tieba_html_tiezi_list_page_last_page) {
		this.baidu_tieba_html_tiezi_list_page_last_page = baidu_tieba_html_tiezi_list_page_last_page;
	}

	/**
	 * @param baidu_tieba_html_tiezi_detail_page_last_page the baidu_tieba_html_tiezi_detail_page_last_page to set
	 */
	public void setBaidu_tieba_html_tiezi_detail_page_last_page(String baidu_tieba_html_tiezi_detail_page_last_page) {
		this.baidu_tieba_html_tiezi_detail_page_last_page = baidu_tieba_html_tiezi_detail_page_last_page;
	}

	/**
	 * @param baidu_tieba_html_tiezi_detail_title the baidu_tieba_html_tiezi_detail_title to set
	 */
	public void setBaidu_tieba_html_tiezi_detail_title(String baidu_tieba_html_tiezi_detail_title) {
		this.baidu_tieba_html_tiezi_detail_title = baidu_tieba_html_tiezi_detail_title;
	}

	/**
	 * @param baidu_tieba_html_tiezi_detail_block the baidu_tieba_html_tiezi_detail_block to set
	 */
	public void setBaidu_tieba_html_tiezi_detail_block(String baidu_tieba_html_tiezi_detail_block) {
		this.baidu_tieba_html_tiezi_detail_block = baidu_tieba_html_tiezi_detail_block;
	}

	/**
	 * @param baidu_tieba_html_tiezi_detail_post_time_root_in_floor the baidu_tieba_html_tiezi_detail_post_time_root_in_floor to set
	 */
	public void setBaidu_tieba_html_tiezi_detail_post_time_root_in_floor(String baidu_tieba_html_tiezi_detail_post_time_root_in_floor) {
		this.baidu_tieba_html_tiezi_detail_post_time_root_in_floor = baidu_tieba_html_tiezi_detail_post_time_root_in_floor;
	}

	/**
	 * @param baidu_tieba_html_tiezi_detail_text the baidu_tieba_html_tiezi_detail_text to set
	 */
	public void setBaidu_tieba_html_tiezi_detail_text(String baidu_tieba_html_tiezi_detail_text) {
		this.baidu_tieba_html_tiezi_detail_text = baidu_tieba_html_tiezi_detail_text;
	}

	/**
	 * @param baidu_tieba_html_tiezi_detail_louzhu_flag the baidu_tieba_html_tiezi_detail_louzhu_flag to set
	 */
	public void setBaidu_tieba_html_tiezi_detail_louzhu_flag(String baidu_tieba_html_tiezi_detail_louzhu_flag) {
		this.baidu_tieba_html_tiezi_detail_louzhu_flag = baidu_tieba_html_tiezi_detail_louzhu_flag;
	}

	/**
	 * @param baidu_tieba_html_user_detail_name the baidu_tieba_html_user_detail_name to set
	 */
	public void setBaidu_tieba_html_user_detail_name(String baidu_tieba_html_user_detail_name) {
		this.baidu_tieba_html_user_detail_name = baidu_tieba_html_user_detail_name;
	}

	/**
	 * @param baidu_tieba_html_user_detail_bar_age the baidu_tieba_html_user_detail_bar_age to set
	 */
	public void setBaidu_tieba_html_user_detail_bar_age(String baidu_tieba_html_user_detail_bar_age) {
		this.baidu_tieba_html_user_detail_bar_age = baidu_tieba_html_user_detail_bar_age;
	}

	/**
	 * @param baidu_tieba_html_user_detail_tiezi_cnt the baidu_tieba_html_user_detail_tiezi_cnt to set
	 */
	public void setBaidu_tieba_html_user_detail_tiezi_cnt(String baidu_tieba_html_user_detail_tiezi_cnt) {
		this.baidu_tieba_html_user_detail_tiezi_cnt = baidu_tieba_html_user_detail_tiezi_cnt;
	}

	@Override
	public void crawlCategoryList(TaskControl tc, TiebaTaskInfo tiebaInfo) {
		CategoryInfo cInfo = new CategoryInfo(
				tiebaInfo.getCategory_1_name(), 
				tiebaInfo.getCategory_1_name(), 
				"1", 
				0, 
				tiebaInfo.getCategory_1_href(), 
				CategoryControlStatus.UNDO.value(), 
				tc.getExecute_id());
		persistenceLogic.insertCategoryInfo(cInfo);
		
		cInfo = new CategoryInfo(
				tiebaInfo.getCategory_2_name(), 
				tiebaInfo.getCategory_2_name(), 
				"2", 
				cInfo.getCategory_id(), 
				tiebaInfo.getCategory_2_href(), 
				CategoryControlStatus.UNDO.value(), 
				tc.getExecute_id());
		persistenceLogic.insertCategoryInfo(cInfo);
	}
}
