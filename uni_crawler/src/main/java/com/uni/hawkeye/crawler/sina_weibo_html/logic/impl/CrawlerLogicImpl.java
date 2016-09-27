package com.uni.hawkeye.crawler.sina_weibo_html.logic.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
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
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.uni.hawkeye.crawler.sina_weibo_html.bean.BlockInfo;
import com.uni.hawkeye.crawler.sina_weibo_html.bean.CategoryInfo;
import com.uni.hawkeye.crawler.sina_weibo_html.bean.CommentInfo;
import com.uni.hawkeye.crawler.sina_weibo_html.bean.SearchInfo;
import com.uni.hawkeye.crawler.sina_weibo_html.bean.TaskControl;
import com.uni.hawkeye.crawler.sina_weibo_html.bean.UserAttentionInfo;
import com.uni.hawkeye.crawler.sina_weibo_html.bean.UserInfo;
import com.uni.hawkeye.crawler.sina_weibo_html.enums.CategoryOrSearchControlStatus;
import com.uni.hawkeye.crawler.sina_weibo_html.logic.CrawlerLogic;
import com.uni.hawkeye.crawler.sina_weibo_html.logic.PersistenceLogic;
import com.uni.hawkeye.jsoup.JsoupCrawler;
import com.uni.hawkeye.log.Uni_LOG_Common;
import com.uni.hawkeye.utils.DataUtil;

import uni_hawkeye.core.WeiboTaskInfo;

public class CrawlerLogicImpl implements CrawlerLogic {

	private static Log log = LogFactory.getLog(CrawlerLogicImpl.class);

	private static final ResourceBundle RESOURCE_BUNDLE_WHITE_LIST = ResourceBundle.getBundle("white_list_sina_weibo_html", Locale.getDefault());

	private PersistenceLogic persistenceLogic;

	private String sina_weibo_html_base_url;
	private String sina_weibo_html_hot_code;
	private String sina_weibo_html_ret_code;

	private String sina_weibo_html_html_category;

	private String sina_weibo_html_json_block_base_url;
	private String sina_weibo_html_json_block_auto_next_page_param;
	private String sina_weibo_html_json_block_manual_next_page_param;

	private int sina_weibo_html_crawler_block_page_no;

	private int sina_weibo_html_crawler_focus_page_no;

	private String sina_weibo_html_user_page_url;
	private String sina_weibo_html_blueV_user_page_url;
	private String sina_weibo_html_comment_url;

	@Override
	public void crawlCategoryList(TaskControl tc) {
		Document doc = null;
		String hot_category_url = sina_weibo_html_base_url + sina_weibo_html_hot_code + "?" + sina_weibo_html_ret_code;
		try {
			doc = JsoupCrawler.fetchDocument(hot_category_url, tc.getSite_code());
		} catch (Exception e) {
			log.error(e.getMessage() + Uni_LOG_Common.Fetch_WhiteList_ERROR("sina_weibo_html", hot_category_url));
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
							Elements cate_elts = category_doc.select(sina_weibo_html_html_category);
							if (!cate_elts.isEmpty() && cate_elts.size() > 0) {
								for (int i = 0; i < cate_elts.size(); i++) {
									Element cate_elt = cate_elts.get(i);
									String category_name = cate_elt.text();
									String category_href = sina_weibo_html_base_url + cate_elt.attr("href");
									category_href = category_href.substring(0, category_href.length() - 1) + "&" + sina_weibo_html_ret_code;
									Pattern cate_code_p = Pattern.compile("ctg1_\\d+");
									Matcher cate_code_m = cate_code_p.matcher(category_href);
									String category_code = "";
									if (cate_code_m.find()) {
										category_code = cate_code_m.group(0);
									}
									CategoryInfo categoryInfo = new CategoryInfo(category_code, category_name, category_href, CategoryOrSearchControlStatus.UNDO.value(),
											tc.getExecute_id());
									persistenceLogic.insertCategoryInfo(categoryInfo);
								}
							}
						}
					} catch (JSONException e) {
						log.error(Uni_LOG_Common.Fetch_JSON_ERROR(e.getMessage(), "sina_weibo_html", x, sina_weibo_html_base_url + sina_weibo_html_hot_code));
					}
				}
			}
		}
	}

	@Override
	public void crawlerBlockList(TaskControl tc, WeiboTaskInfo weiboInfo) {
		log.info("crawling weibo block start....");
		if (weiboInfo != null) {
			crawlerBlockList_have_or_nohave_whiteList(tc, weiboInfo.getHot_category_name(), weiboInfo);
		} else {
			// 遍历白名单
			Iterator<String> white_list = RESOURCE_BUNDLE_WHITE_LIST.keySet().iterator();
			if (white_list.hasNext()) {
				while (white_list.hasNext()) {
					// 获取到配置文件中的白名单 热门分类名称
					String target_category_name = RESOURCE_BUNDLE_WHITE_LIST.getString(white_list.next());
					crawlerBlockList_have_or_nohave_whiteList(tc, target_category_name, null);
				}
			} else {
				crawlerBlockList_have_or_nohave_whiteList(tc, null, null);
			}
		}
		log.info("crawling weibo block end....");
	}

	@Override
	public void crawlerSearchBlockList(TaskControl tc, WeiboTaskInfo weiboInfo) {
		log.info("crawling weibo block start....");

		SearchInfo searchInfo = persistenceLogic.getSearchinfoByByExecuteID(tc.getExecute_id());
		if(searchInfo != null){
			String search_url = DataUtil.format(searchInfo.getSearch_url(), new String[] { "1" });
			Document doc = null;
			try {
				doc = JsoupCrawler.fetchDocument(search_url, tc.getSite_code());
			} catch (Exception e) {
				log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), tc.getSite_code(), search_url));
			}
			if (doc != null) {
				// 得到body信息
				String s = DataUtil.decodeUnicode(doc.body().toString());
				// 按照<script>FM.view分割，得到分割后的数组
				String[] ss = s.split("<script>STK && STK.pageletM && STK.pageletM.view");
				List<String> list = new ArrayList<String>();
				// 遍历循环该数组
				for (String x : ss) {
					// 如果内容中包含 "html": 字样，则进入
					if (x.contains("\"html\":\"")) {
						// 过滤字符串得到实质HTML
						String value = getHtml(x);
						// 如果包含 WB_feed 则 视为 我们想要的 真正内容
						if (value.contains("class=\"search_feed\"")) {
							// 加入到 真正内容 列表
							list.add(value);
						}
					}
				}
				// 如果 列表 大于0
				if (list.size() > 0) {
					// 通过Jsoup.parse 生成 document，便于进一步解析
					doc = Jsoup.parse(list.get(0));
					// 通过 选择器 配置 div > div > div 获取 微博 信息
					Elements dates = doc.select("a[date]");
					// 循环遍历 微博 信息
					for (int j = 0; j < dates.size(); j++) {
						Element date = dates.get(j);
						String date_href = date.attr("href");
						// 如果 存在mid 则视为 我们想要的 微博
						if (!StringUtil.isBlank(date_href) && !date_href.contains("javascript:void(0)")) {
							// 得到 微博 详情页 超链接
							// 这里 是通过 微博发送时间 得到的 超链接
							log.info(date_href);
							// 通过 微博详情页超链接 获取 微博详细信息
							fetchWeiboContent(date_href, 0, searchInfo.getSearch_id(), weiboInfo, tc);
						}
					}
				}
			}
		}

		log.info("crawling weibo block end....");
	}

	private void crawlerBlockList_have_or_nohave_whiteList(TaskControl tc, String target_category_name, WeiboTaskInfo weiboInfo) {
		// 通过热门分类名称，类型抓取状态为0：undo：没做，批次号 获取 热门分类列表
		List<CategoryInfo> categoryList = persistenceLogic.getCategoryInfoByStatus(tc.getExecute_id(), CategoryOrSearchControlStatus.UNDO.value(), target_category_name);
		for (int i = 0; i < categoryList.size(); i++) {
			CategoryInfo cInfo = categoryList.get(i);
			// 得到类型ID
			int category_id = cInfo.getCategory_id();
			persistenceLogic.updateCategoryInfo(category_id, CategoryOrSearchControlStatus.CRAWLING.value());
			// 得到类型URL
			String category_url = cInfo.getCategory_url();

			// 通过类型URL 获取对应页面
			Document doc = null;
			try {
				doc = JsoupCrawler.fetchDocument(category_url, tc.getSite_code());
			} catch (Exception e) {
				log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "sina_weibo_html", category_url));
			}
			// 如果获取到了,则 获取第一页信息
			if (doc != null) {
				getFirstPageContent(doc, category_id, weiboInfo, tc);
			}

			String category_param = category_url.substring(category_url.indexOf(sina_weibo_html_base_url) + sina_weibo_html_base_url.length() + 1, category_url.indexOf("?"));

			int currentPage = 1;
			int pre_page = 1;
			int page = 1;
			while (true) {

				for (int auto_index = 0; auto_index < 2; auto_index++) {
					String auto_url = sina_weibo_html_json_block_base_url + DataUtil.format(sina_weibo_html_json_block_auto_next_page_param,
							new String[] { category_param, category_param, category_param, category_param, pre_page + "", page + "", currentPage + "", auto_index + "" });
					try {
						doc = JsoupCrawler.fetchDocument(auto_url, tc.getSite_code());
					} catch (Exception e) {
						log.error(e.getMessage() + Uni_LOG_Common.Fetch_WhiteList_ERROR("sina_weibo_html", auto_url));
					}
					if (doc != null) {
						log.info("crawling weibo block other page auto....");
						getOtherPageContent(doc, category_id, weiboInfo, tc);
						currentPage++;
					}
				}
				page++;
				String manual_url = sina_weibo_html_json_block_base_url + DataUtil.format(sina_weibo_html_json_block_manual_next_page_param,
						new String[] { category_param, category_param, category_param, category_param, pre_page + "", page + "", currentPage + "" });
				try {
					doc = JsoupCrawler.fetchDocument(manual_url, tc.getSite_code());
				} catch (Exception e) {
					log.error(e.getMessage() + Uni_LOG_Common.Fetch_WhiteList_ERROR("sina_weibo_html", manual_url));
				}
				if (doc != null) {
					log.info("crawling weibo block other page manual....");
					getOtherPageContent(doc, category_id, weiboInfo, tc);
				}
				currentPage++;
				pre_page++;
				if (currentPage >= sina_weibo_html_crawler_block_page_no) {
					break;
				}

			}

			persistenceLogic.updateCategoryInfo(category_id, CategoryOrSearchControlStatus.CRAWLING_FINISHED.value());
		}
	}

	/**
	 * 获取第一页信息
	 * 
	 * @param doc
	 * @param weibo_category_id_fk
	 */
	private void getFirstPageContent(Document doc, int weibo_category_id_fk, WeiboTaskInfo weiboInfo, TaskControl tc) {

		log.info("crawling weibo block first page start....");

		// 得到body信息
		String s = doc.body().toString();
		// 按照<script>FM.view分割，得到分割后的数组
		String[] ss = s.split("<script>FM.view");
		List<String> list = new ArrayList<String>();
		// 遍历循环该数组
		for (String x : ss) {
			// 如果内容中包含 "html": 字样，则进入
			if (x.contains("\"html\":\"")) {
				// 过滤字符串得到实质HTML
				String value = getHtml(x);
				// 如果包含 WB_feed 则 视为 我们想要的 真正内容
				if (value.contains("WB_feed")) {
					// 加入到 真正内容 列表
					list.add(value);
				}
			}
		}
		// 如果 列表 大于0
		if (list.size() > 0) {
			// 通过Jsoup.parse 生成 document，便于进一步解析
			doc = Jsoup.parse(list.get(0));
			// 通过 选择器 配置 div > div > div 获取 微博 信息
			Elements blocks = doc.select("div[mid]");
			// 循环遍历 微博 信息
			for (int j = 0; j < blocks.size(); j++) {
				Element block = blocks.get(j);
				String mid = block.attr("mid");
				// 如果 存在mid 则视为 我们想要的 微博
				if (!StringUtil.isBlank(mid)) {
					// 得到 微博 详情页 超链接
					// 这里 是通过 微博发送时间 得到的 超链接
					String detailHref = block.select("div.WB_feed_detail > div.WB_detail > div.WB_from.S_txt2 > a:nth-child(1)").attr("href");
					log.info(detailHref);
					// 通过 微博详情页超链接 获取 微博详细信息
					fetchWeiboContent(detailHref, weibo_category_id_fk, 0, weiboInfo, tc);
				}
			}
		}

		log.info("crawling weibo block first page end....");
	}

	private void getOtherPageContent(Document doc, int weibo_category_id_fk, WeiboTaskInfo weiboInfo, TaskControl tc) {
		log.info("crawling weibo block other page start....");
		String content = doc.html().replace("\\r", "").replace("\\n", "").replace("\\t", "").replace("\\&quot;", "").replace("&amp;", "&").replace("&gt;", ">").replace("&lt;", "<")
				.replace("\\/", "/").replace("display:none;", "").replace("display: none;", "");
		doc = Jsoup.parse(content);

		Elements wb_details = doc.select("a.S_txt2");
		for (int j = 0; j < wb_details.size(); j++) {
			Element wb_detail = wb_details.get(j);
			String href = wb_detail.attr("href");
			Pattern href_p = Pattern.compile("http://weibo.com/\\d+/.*");
			Matcher href_m = href_p.matcher(href);
			if (href_m.find()) {
				String wb_detail_href = href_m.group(0);
				log.info(wb_detail_href);
				fetchWeiboContent(wb_detail_href, weibo_category_id_fk, 0, weiboInfo, tc);
			}
		}
		log.info("crawling weibo block other page end....");
	}

	/**
	 * 获取微博详情信息
	 * 
	 * @param detailHref
	 * @param weibo_category_id_fk
	 */
	private void fetchWeiboContent(String detailHref, Integer weibo_category_id_fk, Integer weibo_search_id_fk, WeiboTaskInfo weiboInfo, TaskControl tc) {

		String content_key_word = "";
		if(weiboInfo != null){
			content_key_word = weiboInfo.getContent_key_word().trim().replace(" ", "").toUpperCase();
		}
		
		log.info("crawling weibo content start....");
		String block_title = ""; // 微博标题 【】 之间的字符
		String block_text = ""; // 微博正文
		String block_post_time = ""; // 微博发送时间
		String block_weibo_url = ""; // 微博链接
		String block_root_in = ""; // 微博来自于（IPHONE....）
		String block_user_id_fk = ""; // 微博 博主ID
		int block_repost_cnt = 0; // 微博转发数量
		int block_comment_cnt = 0; // 微博评论数量
		int block_favour_cnt = 0; // 微博点赞数量
		String parent_block_weibo_url = ""; // 父级微博链接
		String block_at_username = ""; // 微博@用户
		String block_topic = ""; // 微博参与话题
		String block_is_original = ""; // 是否为原创微博

		// 通过链接获取详情页DOCUMENT
		Document doc = null;
		try {
			doc = JsoupCrawler.fetchDocument(detailHref, tc.getSite_code());
		} catch (Exception e) {
			log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "sina_weibo_html", detailHref));
		}
		if (doc != null) {
			// 获取 包含 "html": 和 WB_feed 的 信息
			List<String> list = getPageHTML(doc, "\"html\":\"", "WB_feed", "");

			if (list.size() > 0) {
				doc = Jsoup.parse(list.get(0));

				/* ---------以下是解析与赋值 各个block表信息-------------- */
				String mid = doc.select("div.WB_cardwrap.WB_feed_type.S_bg2").attr("mid");
				log.info("mid\t" + mid);
				Elements block = doc.select("div > div > div.WB_feed_detail");
				block_text = DataUtil.filterHtml(block.select("div > div.WB_detail > div.WB_text.W_f14").text().replace("\r\n", ""));
				String block_text_tmp = block_text.trim().replace(" ", "").toUpperCase();
				if(block_text_tmp.contains(content_key_word)){
					log.info("block_text\t" + block_text);
					// 通过正则 获取【】之间的内容为标题
					block_title = DataUtil.matchByRegex("(?<=【).*(?=】)", block_text);
					log.info("block_title\t" + block_title);
					Elements block_post_times = block.select("div > div.WB_detail > div.WB_from.S_txt2 > a:nth-child(1)");
					block_post_time = block_post_times.attr("title");
					String block_post_time_href = block_post_times.attr("href");
					log.info("block_post_time\t" + block_post_time);
					block_weibo_url = detailHref;
					log.info("block_weibo_url\t" + block_weibo_url);
					block_root_in = block.select("div > div.WB_detail > div.WB_from.S_txt2 > a:nth-child(2)").text();
					log.info("block_root_in\t" + block_root_in);
					Elements at_usercards = block.select("div > div.WB_detail > div.WB_text.W_f14 > a[usercard]");
					if (!at_usercards.isEmpty() && at_usercards.size() > 0) {
						for (Element at_usercard : at_usercards) {
							String usercard = at_usercard.attr("usercard");
							if (!StringUtil.isBlank(usercard)) {
								block_at_username += usercard.replaceAll("name=", "") + " ";
								String block_at_user_href = at_usercard.attr("href");
								log.info("block_at_user_href\t" + block_at_user_href);
								Document block_at_user_doc = null;
								try {
									block_at_user_doc = JsoupCrawler.fetchDocument(block_at_user_href, tc.getSite_code());
								} catch (Exception e) {
									log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "sina_weibo", block_at_user_href));
								}
								if (block_at_user_doc != null) {
									String fromWay = "fromBlock";
									fetchWeiboContentByMainPage("", block_at_user_doc, fromWay, tc, weiboInfo);
								}
							}
						}
					}
					log.info("block_at_username\t" + block_at_username.trim());
					// 通过正则获取 ## 之间的内容为 话题
					block_topic = DataUtil.matchByRegex("#(.*?)#", block_text);
					log.info("block_topic\t" + block_topic);
					
					// 程序走到这里 开始进行 获取 用户信息，因为当前抓取的是博主，所以 获取的是博主信息
					block_user_id_fk = DataUtil.matchByRegex("/[0-9]+/", block_post_time_href).replaceAll("/", "");
					log.info("userID\t" + block_user_id_fk);
					String longUID = DataUtil.getParam(block_post_time_href).get("from");
					longUID = DataUtil.matchByRegex("\\d+", longUID);
					if (!StringUtil.isBlank(longUID)) {
						String userDetailHref = DataUtil.format(sina_weibo_html_user_page_url, new String[] { longUID });
						String blueVuserDetailHref = DataUtil.format(sina_weibo_html_blueV_user_page_url, new String[]{block_user_id_fk});
						try {
							// 进入获取用户信息 方法
							if (!fetchWeiboUserContent(userDetailHref, block_user_id_fk, "fromUser", tc, weiboInfo)) {
								if(!fetchWeiboUserContent(blueVuserDetailHref, block_user_id_fk, "fromUser", tc, weiboInfo)){
									block_user_id_fk = "";
								}
							}
						} catch (Exception e) {
							log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "sina_weibo_html", userDetailHref));
						}
					}
					
					// 获取父级微博URL
					parent_block_weibo_url = block.select("div.WB_feed_spec[action-data]").attr("action-data"); 
					if (parent_block_weibo_url.indexOf("http") != -1) {
						parent_block_weibo_url = parent_block_weibo_url.substring(parent_block_weibo_url.indexOf("http"), parent_block_weibo_url.length());
						try {
							parent_block_weibo_url = URLDecoder.decode(parent_block_weibo_url, "UTF8");
						} catch (UnsupportedEncodingException e) {
							log.error(e.getMessage() + " url decoder parent_block_weibo_url error");
						}
					} else {
						parent_block_weibo_url = "http://weibo.com" + block.select("div.WB_func > div.WB_from > a[date]").attr("href");
					}
					log.info("parent_block_weibo_url\t" + parent_block_weibo_url);
					
					if (!StringUtil.isBlank(block_user_id_fk)) {
						/* ----------接下来，获取微博的 转发、评论、点赞数量------------- */
						block = doc.select("div > div > div.WB_feed_handle");
						/* -------------------获取转发数量-------------------------- */
						Elements repost_elts = block.select("div > div > ul > li:nth-child(2)");
						if (!repost_elts.isEmpty() && repost_elts.size() > 0) {
							String repostStr = repost_elts.text().trim();
							if (!StringUtil.isBlank(repostStr)) {
								String block_repost_cnt_str = DataUtil.matchByRegex("\\d+", repostStr);
								if (!StringUtil.isBlank(block_repost_cnt_str)) {
									block_repost_cnt = Integer.valueOf(block_repost_cnt_str);
								} else {
									block_repost_cnt = 0;
								}
							}
						}
						log.info("block_repost_cnt\t" + block_repost_cnt);
						
						/* -------------------获取评论数量-------------------------- */
						Elements comments_elts = block.select("div > div > ul > li:nth-child(3)");
						if (!comments_elts.isEmpty() && comments_elts.size() > 0) {
							String commentsStr = comments_elts.text().trim();
							if (!StringUtil.isBlank(commentsStr)) {
								String block_comment_cnt_str = DataUtil.matchByRegex("\\d+", commentsStr);
								if (!StringUtil.isBlank(block_comment_cnt_str)) {
									block_comment_cnt = Integer.valueOf(block_comment_cnt_str);
								} else {
									block_comment_cnt = 0;
								}
							}
						}
						log.info("block_comment_cnt\t" + block_comment_cnt);
						
						/* -------------------获取点赞数量-------------------------- */
						Elements favour_elts = block.select("div > div > ul > li:nth-child(4)");
						if (!favour_elts.isEmpty() && favour_elts.size() > 0) {
							String favourStr = favour_elts.text().trim();
							if (!StringUtil.isBlank(favourStr)) {
								String block_favour_cnt_str = DataUtil.matchByRegex("\\d+", favourStr);
								if (!StringUtil.isBlank(block_favour_cnt_str)) {
									block_favour_cnt = Integer.valueOf(block_favour_cnt_str);
								} else {
									block_favour_cnt = 0;
								}
							}
						}
						log.info("block_favour_cnt\t" + block_favour_cnt);
						
						// 判断是否为 原创微博
						block_is_original = isOriginal(block_text) ? "1" : "0";
						log.info("block_is_original\t" + block_is_original);
						
						// 到此。微博信息全部获取完毕，进行存储
						BlockInfo blockInfo = new BlockInfo(weibo_category_id_fk, weibo_search_id_fk, block_title, block_text, block_post_time, block_weibo_url, block_root_in,
								block_user_id_fk, block_repost_cnt, block_comment_cnt, block_favour_cnt, parent_block_weibo_url, block_at_username, block_topic, block_is_original);
						persistenceLogic.insertBlockInfo(blockInfo);
						
						// 微博信息存储完毕， 开始获取微博评论信息
						// 通过配置文件中 sina_weibo_html_comment_url 的配置 和 获取微博信息时的到 mid 拼接成 评论信息页的 url
						String commentHref = DataUtil.format(sina_weibo_html_comment_url, new String[] { mid });
						fetchCommentContent(commentHref, blockInfo.getBlock_id(), tc, weiboInfo);
					}
				}
			}
		}
		log.info("crawling weibo content end....");
	}

	/**
	 * 获取微博评论信息
	 * 
	 * @param commentHref
	 * @param block_id
	 */
	private void fetchCommentContent(String commentHref, int block_id, TaskControl tc, WeiboTaskInfo weiboInfo) {
		log.info("crawling weibo comment start....");
		Document doc = null;
		try {
			log.info("comment href\t" + commentHref);
			doc = JsoupCrawler.fetchDocument(commentHref, tc.getSite_code());
		} catch (Exception e) {
			log.error(Uni_LOG_Common.Fetch_JSON_ERROR(e.getMessage(), "sina_weibo_html", doc.text(), commentHref));
		}

		if (doc != null) {
			// 得到评论信息页BODY信息
			String html = doc.body().html();
			// 过滤掉 \r \n \t &quot; display:none; display: none;
			// 替换&amp;为& &gt;为> &lt;为< \/为/
			html = html.replace("\\r", "").replace("\\n", "").replace("\\t", "").replace("\\&quot;", "").replace("&amp;", "&").replace("&gt;", ">").replace("&lt;", "<")
					.replace("\\/", "/").replace("display:none;", "").replace("display: none;", "");
			// 解析html中存在的unicode编码
			html = DataUtil.decodeUnicode(html);
			// 因为 每个 评论块 都是 以 <div comment_id= 开头的， 所以 用<div comment_id= 分割 ， 生成 评论数组
			String[] comments = html.split("<div comment_id=");
			for (int i = 0; i < comments.length; i++) {
				// 因为上一步切割了，所以在得到评论HTML内容时需要把切割的 补全。 不然不能形成 完整良好的 HTML
				String comment = "<div comment_id=" + comments[i];
				doc = Jsoup.parse(comment);
				// 获取评论ID
				String comment_id = doc.select("div.list_li").attr("comment_id");
				log.info("comment_id\t" + comment_id);
				String comment_user_id_fk = "";
				// 获取评论URL
				String comment_href = doc.select("div.WB_text > a").attr("href");
				// 得到评论用户URL
				// 这里获取到的用户URL 是 用户的 微博主页URL 并不是 用户详情页URL
				// 因为在评论也无法获取到 用户的完整ID 所以 需要先行进入该用户微博主页， 再获取到用户完整ID 再 进入到用户详情页
				String comment_user_href = "";
				if (!StringUtil.isBlank(comment_href) && !comment_href.contains("javascript:void(0)")) {
					comment_user_href = comment_href;
				} else {
					// 如果a标签存在usercard属性 并且 存在ucardconf属性 并且 href不包含javascript:void(0) 则获取到 用户微博主页URL
					if (doc.select("a").hasAttr("usercard") && doc.select("a").hasAttr("ucardconf") && !doc.select("a").attr("href").contains("javascript:void(0)")) {
						comment_user_href = doc.select("a").attr("href");
					}
				}
				if(comment_user_href.indexOf("http://weibo.com") == -1){
					comment_user_href = "http://weibo.com" + comment_user_href;
				}

				// 获取该评论用户微博主页document
				Document comment_user_doc = null;
				try {
					log.info("comment_user_href\t" + comment_user_href);
					comment_user_doc = JsoupCrawler.fetchDocument(comment_user_href, tc.getSite_code());
				} catch (Exception e) {
					log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "sina_weibo_html", comment_user_href));
				}

				if (comment_user_doc != null) {
					String fromWay = "fromComment";
					comment_user_id_fk = fetchWeiboContentByMainPage(comment_user_id_fk, comment_user_doc, fromWay, tc, weiboInfo);

					if (!StringUtil.isBlank(comment_user_id_fk)) {
						// 获取评论正文
						String comment_text = doc.select("div.WB_text").text();
						if (StringUtil.isBlank(comment_text)) {
							comment_text = doc.select("i.W_icon").text();
						}
						if (comment_text.indexOf("举报") != -1) {
							comment_text = comment_text.substring(0, comment_text.indexOf("举报"));
						}
						// if(comment_text.indexOf("回复") != -1){
						// comment_text = comment_text.substring(0, comment_text.indexOf("回复"));
						// }
						log.info("comment_text\t" + comment_text);

						// 获取评论点赞数
						int comment_favour_cnt = 0;
						String favour_cnt_str = doc.select("span[node-type=like_status]").text();
						if (!StringUtil.isBlank(favour_cnt_str)) {
							favour_cnt_str = DataUtil.matchByRegex("\\d+", favour_cnt_str);
							if(!StringUtil.isBlank(favour_cnt_str)){
								comment_favour_cnt = Integer.valueOf(favour_cnt_str);
							}
						}
						log.info("comment_favour_cnt\t" + comment_favour_cnt);

						// 获取评论发表时间
						String comment_post_time = doc.select("div.WB_from").text();
						log.info("comment_post_time\t" + comment_post_time);

						// 获取评论@用户
						String comment_at_username = "";
						Elements ats = doc.select("a[usercard]");
						for (Element ata : ats) {
							String a_attr = ata.attr("usercard");
							String a_text = ata.text();
							if (a_attr.contains("name=") && !StringUtil.isBlank(a_text) && a_text.contains("@")) {
								comment_at_username += ata.attr("usercard").replace("name=", "") + " ";
								String comment_at_user_href = ata.attr("href");
								log.info("comment_at_user_href\t" + comment_at_user_href);
								Document comment_at_user_doc = null;
								try {
									comment_at_user_doc = JsoupCrawler.fetchDocument(comment_at_user_href, tc.getSite_code());
								} catch (Exception e) {
									log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "sina_weibo", comment_at_user_href));
								}
								if (comment_at_user_doc != null) {
									fetchWeiboContentByMainPage("", comment_at_user_doc, fromWay, tc, weiboInfo);
								}
							}
						}
						comment_at_username = comment_at_username.trim();
						log.info("comment_at_username\t" + comment_at_username);

						// 获取评论参与话题
						String comment_topic = DataUtil.matchByRegex("#(.*?)#", comment_text);
						log.info("comment_topic\t" + comment_topic);

						// 判断是否为原创评论
						String comment_is_original = isOriginal(comment_text) ? "1" : "0";
						log.info("comment_is_original\t" + comment_is_original);

						// 到此评论信息获取完毕，进行存储
						CommentInfo commentInfo = new CommentInfo(comment_id, block_id, comment_text, comment_favour_cnt, comment_user_id_fk, comment_post_time,
								comment_at_username, comment_topic, comment_is_original);
						persistenceLogic.insertCommentInfo(commentInfo);
					}
				}
			}
		}
		log.info("crawling weibo comment end....");
	}

	private String fetchWeiboContentByMainPage(String user_id_fk, Document comment_user_doc, String fromWay, TaskControl tc, WeiboTaskInfo weiboInfo) {
		List<String> list = getPageHTML(comment_user_doc, "\"html\":\"", "主页", "");

		if (list.size() > 0) {
			comment_user_doc = Jsoup.parse(list.get(0));
			Elements tab_links = comment_user_doc.select("a.tab_link");
			if (!tab_links.isEmpty() && tab_links.size() > 0) {
				Element tab_link = tab_links.get(0);
				String getUserIDHref = tab_link.attr("href");
				// 到此 才 获取到 评论用户的 完整ID！！！！
				user_id_fk = DataUtil.matchByRegex("(?<=/p/)\\d+(?=/home)", getUserIDHref);
				String paramFrom = DataUtil.getParam(getUserIDHref).get("from");
				String areaCode = DataUtil.matchByRegex("\\d+", paramFrom);
				user_id_fk = user_id_fk.replace(areaCode, "");
				log.info("comment user_id\t" + user_id_fk);
				if (!StringUtil.isBlank(user_id_fk)) {
					// 通过配置文件 的 sina_weibo_html_user_page_url 和 评论用户完整ID 拼接 用户详情页URL
					String comment_user_detail_url = DataUtil.format(sina_weibo_html_user_page_url, new String[] { areaCode + user_id_fk });
					try {
						// 获取用户信息，注意这里是 fromWay=fromComment， 只有 fromUser时 才抓取关注列表
						if (!fetchWeiboUserContent(comment_user_detail_url, user_id_fk, fromWay, tc, weiboInfo)) {
							user_id_fk = "";
						}
					} catch (Exception e) {
						log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "sina_weibo_html", comment_user_detail_url));
					}
				}
			}
		}
		return user_id_fk;
	}

	/**
	 * 获取用户信息 当fromWay=fromUser 时 该方法会进一步抓取 关注列表
	 * 
	 * @param href
	 * @param user_id
	 * @param fromWay
	 * @throws Exception
	 */
	private boolean fetchWeiboUserContent(String href, String user_id, String fromWay, TaskControl tc, WeiboTaskInfo weiboInfo) throws Exception {

		log.info("crawling weibo user start....");

		String user_nick = ""; // 用户昵称
		String user_area = ""; // 用户所在地区
		String user_sex = ""; // 用户性别
		String user_birthday = ""; // 用户生日
		String user_blog = ""; // 用户主页
		String user_blood_type = ""; // 用户血型
		String user_summery = ""; // 用户简介
		String user_tags = ""; // 用户标签
		int user_fans_cnt = 0; // 用户粉丝数
		int user_weibo_cnt = 0; // 用户发博数
		int user_focus_cnt = 0; // 用户关注数
		String user_focus_href = ""; // 用户关注URL --> 用户跳转到用户关注页
		Document doc = JsoupCrawler.fetchDocument(href, tc.getSite_code());
		List<String> list = getPageHTML(doc, "\"html\":\"", "模块", "");
		if(list == null || list.size() == 0){
			list = getPageHTML(doc, "\"html\":\"", "简介", "");
			if(list != null && list.size() > 0){
				List<String> listGuanfangWeibo = getPageHTML(doc, "\"html\":\"", "官方微博", "");
				if(listGuanfangWeibo != null && listGuanfangWeibo.size() > 0){
					list.add(listGuanfangWeibo.get(0));
				}
			}
		}
		if (list.size() > 0) {
			doc = Jsoup.parse(list.get(0));
			Elements user_base_infos = doc.select("div.WB_cardwrap > div.PCD_text_b > div.WB_innerwrap > div.m_wrap > ul > li");
			if (!user_base_infos.isEmpty() && user_base_infos.size() > 0) {
				for (int i = 0; i < user_base_infos.size(); i++) {
					Element user_base_info_elt = user_base_infos.get(i);
					String user_base_info = user_base_info_elt.text();
					log.info("抓取普通用户..");
					log.info(user_base_info);
					if (user_base_info.contains("昵称：")) {
						user_nick = user_base_info.replace("昵称：", "");
					} else if (user_base_info.contains("所在地：")) {
						user_area = user_base_info.replace("所在地：", "");
					} else if (user_base_info.contains("性别：")) {
						user_sex = user_base_info.replace("性别：", "");
					} else if (user_base_info.contains("生日：")) {
						user_birthday = user_base_info.replace("生日：", "");
					} else if (user_base_info.contains("博客：")) {
						user_blog = user_base_info.replace("博客：", "");
					} else if (user_base_info.contains("血型：")) {
						user_blood_type = user_base_info.replace("血型：", "");
					} else if (user_base_info.contains("简介：")) {
						user_summery = user_base_info.replace("简介：", "");
					} else if (user_base_info.contains("标签：")) {
						Elements tags = user_base_info_elt.select("li > span > a");
						for (Element tag : tags) {
							user_tags += tag.text() + " ";
						}
						user_tags = user_tags.trim();
					}
				}
			}else{
				log.info("抓取官方用户..");
				user_summery = doc.select("p.p_txt").text();
				log.info("user_summery\t"+user_summery);
				if(user_summery.contains("皇马")){
					System.out.println("debug");
				}
				if(list.size() == 2){
					doc = Jsoup.parse(list.get(1));
					user_nick = doc.select("div.pf_intro").text();
					log.info("user_nick\t"+user_nick);
				}
			}

			doc = JsoupCrawler.fetchDocument(href, tc.getSite_code());
			list = getPageHTML(doc, "\"html\":\"", "粉丝</span>", "");
			if (list.size() > 0) {
				doc = Jsoup.parse(list.get(0));
				Elements cnt_elts = doc.select("a");
				if (!cnt_elts.isEmpty() && cnt_elts.size() > 0) {
					for (int i = 0; i < cnt_elts.size(); i++) {
						Element cnt_elt = cnt_elts.get(i);
						String user_cnt_info = cnt_elt.text();
						if (user_cnt_info.contains("关注")) {
							user_focus_href = cnt_elt.attr("href");
							user_focus_cnt = Integer.valueOf(user_cnt_info.replace("关注", ""));
							log.info("关注：" + user_focus_cnt);
						} else if (user_cnt_info.contains("粉丝")) {
							user_fans_cnt = Integer.valueOf(user_cnt_info.replace("粉丝", ""));
							log.info("粉丝：" + user_fans_cnt);
						} else if (user_cnt_info.contains("微博")) {
							user_weibo_cnt = Integer.valueOf(user_cnt_info.replace("微博", ""));
							log.info("微博：" + user_weibo_cnt);
						}
					}
				}
			}

			// 获取微博用户信息完毕后 进行存储
			UserInfo userInfo = new UserInfo(user_id, user_nick, user_area, user_sex, user_birthday, user_blog, user_blood_type, user_summery, user_tags, user_fans_cnt,
					user_weibo_cnt, user_focus_cnt);
			int result = persistenceLogic.insertUserInfo(userInfo);

			if(weiboInfo == null){
				// 如果为fromUser 则 获取 关注列表
				if (fromWay.equals("fromUser")) {
					fetchWeiboUserFocusContent(user_focus_href, user_id, tc, weiboInfo);
				}
			}else{
				if(weiboInfo.getCrawler_user_focus().equals("yes")){
					fetchWeiboUserFocusContent(user_focus_href, user_id, tc, weiboInfo);
				}
			}
			if (result == 0) {
				log.info("crawling weibo user end....not save user info");
				return false;
			} else {
				log.info("crawling weibo user end....");
				return true;
			}
		} else {
			log.info("crawling weibo user end.... not found user info");
			return false;
		}

	}

	private void fetchWeiboUserFocusContent(String user_focus_href, String user_id, TaskControl tc, WeiboTaskInfo weiboInfo) {
		log.info("crawling weibo focus user start....");
		Document doc = null;
		try {
			doc = JsoupCrawler.fetchDocument(user_focus_href, tc.getSite_code());
		} catch (Exception e) {
			log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "sina_weibo_html", user_focus_href));
		}
		if (doc != null) {
			List<String> focusList = getPageHTML(doc, "\"ns\":\"pl.content.followTab.index\"", "关注", "");
			if (focusList.size() > 0) {
				doc = Jsoup.parse(focusList.get(0));
				String focus_str = doc.select("li.tab_li").text();
				int focus_total_cnt = 1;
				if (!StringUtil.isBlank(focus_str)) {
					focus_total_cnt = Integer.valueOf(DataUtil.matchByRegex("\\d+", focus_str));
				}
				user_focus_href = user_focus_href.substring(0, user_focus_href.lastIndexOf("#")) + "&page=";
				int focus_list_page_index = 1;
				int focus_user_index = 1;
				while (true) {

					try {
						log.info("user focus href\t" + user_focus_href + focus_list_page_index);
						doc = JsoupCrawler.fetchDocument(user_focus_href + focus_list_page_index, tc.getSite_code());
					} catch (Exception e) {
						log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "sina_weibo_html", user_focus_href + focus_list_page_index));
					}

					if (doc != null) {
						focusList = getPageHTML(doc, "\"ns\":\"pl.content.followTab.index\"", "关注", "");
						if (focusList.size() > 0) {
							doc = Jsoup.parse(focusList.get(0));
							Elements focus_users = doc.select("li.follow_item.S_line2");
							if (!focus_users.isEmpty() && focus_users.size() > 0) {
								for (int j = 0; j < focus_users.size(); j++) {
									Element focus_user = focus_users.get(j);
									String focus_user_href = "http://weibo.com" + focus_user.select("dt.mod_pic > a").attr("href");
									try {
										log.info("user focus main href\t" + focus_user_href);
										doc = JsoupCrawler.fetchDocument(focus_user_href, tc.getSite_code());
									} catch (Exception e) {
										log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "sina_weibo_html", focus_user_href));
									}

									List<String> list = getPageHTML(doc, "\"html\":\"", "主页", "");

									if (list.size() > 0) {
										doc = Jsoup.parse(list.get(0));
										Elements tab_links = doc.select("a.tab_link");
										if (!tab_links.isEmpty() && tab_links.size() > 0) {
											Element tab_link = tab_links.get(0);
											String getUserIDHref = tab_link.attr("href");
											String focus_user_id = DataUtil.matchByRegex("(?<=/p/)\\d+(?=/home)", getUserIDHref);
											String paramFrom = DataUtil.getParam(getUserIDHref).get("from");
											String areaCode = DataUtil.matchByRegex("\\d+", paramFrom);
											focus_user_id = focus_user_id.replace(areaCode, "");
											log.info("focus id\t" + focus_user_id);
											if (!StringUtil.isBlank(focus_user_id)) {
												String focus_user_detail_url = DataUtil.format(sina_weibo_html_user_page_url, new String[] { areaCode + focus_user_id });
												try {
													log.info("user focus detail href\t" + focus_user_detail_url);
													if (!fetchWeiboUserContent(focus_user_detail_url, focus_user_id, "fromFocus", tc, weiboInfo)) {
														focus_user_id = "";
													}
												} catch (Exception e) {
													log.error(Uni_LOG_Common.Fetch_Page_ERROR(e.getMessage(), "sina_weibo_html", focus_user_detail_url));
												}
												if (!StringUtil.isBlank(focus_user_id)) {
													UserAttentionInfo userAttentionInfo = new UserAttentionInfo(user_id, focus_user_id);
													persistenceLogic.insertUserAttentionInfo(userAttentionInfo);
												}
											}
										}
									}
									focus_user_index++;
								}
							}
						}
					}
					if (focus_user_index >= focus_total_cnt) {
						break;
					}
					if (focus_list_page_index > sina_weibo_html_crawler_focus_page_no) {
						break;
					}
					focus_list_page_index++;
				}
			}
		}
		log.info("crawling weibo focus user end....");
	}

	private List<String> getPageHTML(Document doc, String contains_1, String contains_2, String contains_3) {
		String s = doc.body().toString();
		String[] ss = s.split("<script>FM.view");
		List<String> list = new ArrayList<String>();
		for (String x : ss) {
			if (x.contains(contains_1)) {
				String value = getHtml(x);
				if (value.contains(contains_2) || (!contains_3.equals("") && value.contains(contains_3))) {
					value = value.substring(0, value.lastIndexOf("\""));
					list.add(value);
				}
			}
		}
		return list;
	}

	/**
	 * 过滤字符串得到实质HTML 主要过滤 \\t \\n \\r \\ \/ 这四种
	 * 
	 * @param s
	 * @return
	 */
	private String getHtml(String s) {
		String content = s.split("\"html\":\"")[1].replaceAll("(\\\\t|\\\\n|\\\\r)", "").replaceAll("\\\\\"", "\"").replaceAll("\\\\/", "/");
		content = content.substring(0, content.length() <= 13 ? content.length() : content.length() - 13);
		return content;
	}

	/**
	 * 判断是否为原创微博 如果包含 // 转发 字样的 不视为原创
	 * 
	 * @param text
	 * @return
	 */
	private boolean isOriginal(String text) {
		if (StringUtil.isBlank(text)) {
			return true;
		} else if (text.contains("//")) {
			return false;
		} else if (text.contains("转发")) {
			return false;
		} else {
			return true;
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
	 * @param sina_weibo_html_base_url
	 *            the sina_weibo_html_base_url to set
	 */
	public void setSina_weibo_html_base_url(String sina_weibo_html_base_url) {
		this.sina_weibo_html_base_url = sina_weibo_html_base_url;
	}

	/**
	 * @param sina_weibo_html_hot_code
	 *            the sina_weibo_html_hot_code to set
	 */
	public void setSina_weibo_html_hot_code(String sina_weibo_html_hot_code) {
		this.sina_weibo_html_hot_code = sina_weibo_html_hot_code;
	}

	/**
	 * @param sina_weibo_html_ret_code
	 *            the sina_weibo_html_ret_code to set
	 */
	public void setSina_weibo_html_ret_code(String sina_weibo_html_ret_code) {
		this.sina_weibo_html_ret_code = sina_weibo_html_ret_code;
	}

	/**
	 * @param sina_weibo_html_html_category
	 *            the sina_weibo_html_html_category to set
	 */
	public void setSina_weibo_html_html_category(String sina_weibo_html_html_category) {
		this.sina_weibo_html_html_category = sina_weibo_html_html_category;
	}

	/**
	 * @param sina_weibo_html_json_block_base_url
	 *            the sina_weibo_html_json_block_base_url to set
	 */
	public void setSina_weibo_html_json_block_base_url(String sina_weibo_html_json_block_base_url) {
		this.sina_weibo_html_json_block_base_url = sina_weibo_html_json_block_base_url;
	}

	/**
	 * @param sina_weibo_html_json_block_auto_next_page_param
	 *            the sina_weibo_html_json_block_auto_next_page_param to set
	 */
	public void setSina_weibo_html_json_block_auto_next_page_param(String sina_weibo_html_json_block_auto_next_page_param) {
		this.sina_weibo_html_json_block_auto_next_page_param = sina_weibo_html_json_block_auto_next_page_param;
	}

	/**
	 * @param sina_weibo_html_json_block_manual_next_page_param
	 *            the sina_weibo_html_json_block_manual_next_page_param to set
	 */
	public void setSina_weibo_html_json_block_manual_next_page_param(String sina_weibo_html_json_block_manual_next_page_param) {
		this.sina_weibo_html_json_block_manual_next_page_param = sina_weibo_html_json_block_manual_next_page_param;
	}

	/**
	 * @param sina_weibo_html_crawler_block_page_no
	 *            the sina_weibo_html_crawler_block_page_no to set
	 */
	public void setSina_weibo_html_crawler_block_page_no(int sina_weibo_html_crawler_block_page_no) {
		this.sina_weibo_html_crawler_block_page_no = sina_weibo_html_crawler_block_page_no;
	}

	/**
	 * @param sina_weibo_html_crawler_focus_page_no
	 *            the sina_weibo_html_crawler_focus_page_no to set
	 */
	public void setSina_weibo_html_crawler_focus_page_no(int sina_weibo_html_crawler_focus_page_no) {
		this.sina_weibo_html_crawler_focus_page_no = sina_weibo_html_crawler_focus_page_no;
	}

	/**
	 * @param sina_weibo_html_user_page_url
	 *            the sina_weibo_html_user_page_url to set
	 */
	public void setSina_weibo_html_user_page_url(String sina_weibo_html_user_page_url) {
		this.sina_weibo_html_user_page_url = sina_weibo_html_user_page_url;
	}

	/**
	 * @param sina_weibo_html_blueV_user_page_url the sina_weibo_html_blueV_user_page_url to set
	 */
	public void setSina_weibo_html_blueV_user_page_url(String sina_weibo_html_blueV_user_page_url) {
		this.sina_weibo_html_blueV_user_page_url = sina_weibo_html_blueV_user_page_url;
	}

	/**
	 * @param sina_weibo_html_comment_url
	 *            the sina_weibo_html_comment_url to set
	 */
	public void setSina_weibo_html_comment_url(String sina_weibo_html_comment_url) {
		this.sina_weibo_html_comment_url = sina_weibo_html_comment_url;
	}

	@Override
	public void crawlCategoryList(TaskControl tc, WeiboTaskInfo weiboInfo) {

		CategoryInfo categoryInfo = new CategoryInfo(weiboInfo.getHot_category_code(), weiboInfo.getHot_category_name(), weiboInfo.getHot_category_href(),
				CategoryOrSearchControlStatus.UNDO.value(), tc.getExecute_id());
		persistenceLogic.insertCategoryInfo(categoryInfo);
	}

	@Override
	public void crawlSearchList(TaskControl tc, WeiboTaskInfo weiboInfo) {

		String search_url = "";
		try {

			search_url = "http://s.weibo.com/weibo/" + URLEncoder.encode(weiboInfo.getContent_key_word(), "utf8").toString() + "&typeall=1&suball=1&timescope=custom:"
					+ weiboInfo.getCrawler_weibo_begin_time() + ":" + weiboInfo.getCrawler_weibo_end_time() + "&page=${0}";
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SearchInfo searchInfo = new SearchInfo(weiboInfo.getContent_key_word(), weiboInfo.getCrawler_weibo_begin_time(), weiboInfo.getCrawler_weibo_end_time(), search_url,
				CategoryOrSearchControlStatus.UNDO.value(), tc.getExecute_id());
		persistenceLogic.insertSearchInfo(searchInfo);
	}

}
