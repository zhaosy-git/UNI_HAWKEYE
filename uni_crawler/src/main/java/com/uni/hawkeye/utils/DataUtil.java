package com.uni.hawkeye.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.helper.StringUtil;

public class DataUtil {

	public static String format(String str, String[] param) {
		if (str == null) {
			return "";
		}

		if (param == null || param.length == 0) {
			return str;
		}

		String returnStr = str;
		for (int i = 0; i < param.length; i++) {
			returnStr = returnStr.replaceAll("\\$\\{" + i + "\\}", param[i]);
		}
		return returnStr;
	}

	public static Date StrToDate(String str, String formatKey) {

		SimpleDateFormat format = new SimpleDateFormat(formatKey);
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String jsonString(String s) {
		char[] temp = s.toCharArray();
		int n = temp.length;
		for (int i = 0; i < n; i++) {
			if (temp[i] == ':' && temp[i + 1] == '"') {
				for (int j = i + 2; j < n; j++) {
					if (temp[j] == '"') {
						if ((temp[j + 1] != ',' && temp[j + 1] != '}') || (temp[j + 1] == ',' && temp[j + 2] != '"')) {
							temp[j] = '”';
						} else if (temp[j + 1] == ',' || temp[j + 1] == '}') {
							break;
						}
					}
				}
			}
		}
		return new String(temp);
	}

	public static String matchParamValFromParamKey(String href, String regx) {
		if (null == href) {
			return null;
		}

		List<String> patternString = new ArrayList<String>();
		patternString.add(regx);

		for (String string : patternString) {
			Pattern p = Pattern.compile(string);
			Matcher m = p.matcher(href);
			if (m.find()) {
				String tmp = m.group();
				tmp = tmp.split("=")[1];
				return tmp;
			}
		}
		return null;
	}

	public static String matchByRegex(String regex, String str) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		String result = "";
		while (m.find()) {
			result += m.group(0) + " ";
		}
		return result.trim();
	}

	public static Map<String, String> getParam(String href) {
		Map<String, String> paramMap = new HashMap<String, String>();
		if (href.indexOf("?") != -1) {
			String param = href.split("\\?")[1];
			if (param.indexOf("&") != -1) {
				for (String p : param.split("&")) {
					String[] p_arr = p.split("=");
					String paramKey = "";
					String paramVal = "";
					for(int i=0; i<p_arr.length; i++){
						if(i == 0){
							paramKey = p_arr[i];
						}else if(i == 1){
							paramVal = p_arr[i];
						}
					}
					paramMap.put(paramKey, paramVal);
				}
			} else {
				String[] p_arr = param.split("=");
				String paramKey = "";
				String paramVal = "";
				if(p_arr.length == 2){
					paramKey = param.split("=")[0];
					paramVal = param.split("=")[1];
				}else if(p_arr.length == 1){
					paramKey = param.split("=")[0];
				}
				paramMap.put(paramKey, paramVal);
			}
		}
		return paramMap;
	}

	public static Map<String, String> getCookieMap(String cookie_str) {
		Map<String, String> map = new HashMap<String, String>();
		if (!StringUtil.isBlank(cookie_str)) {
			String[] cookie_str_sub = cookie_str.split(";");
			for (String cookie_key_val : cookie_str_sub) {
				String[] keyVal = cookie_key_val.trim().split("=");
				String key = keyVal[0];
				String val = "";
				if(keyVal.length == 2){
					val = keyVal[1];
				}
				map.put(key, val);
			}
		}
		return map;
	}

	public static long dateDiff(Date startDate, Date endDate) {
		long different = endDate.getTime() - startDate.getTime();
		long hour = 0;
		long day = 0;
		long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
		long nh = 1000 * 60 * 60;// 一小时的毫秒数
		day = different / nd;// 计算差多少天
		hour = different % nd / nh + day * 24;// 计算差多少小时
		return hour;
	}

	public static String decodeUnicode(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					// Read the xxxx
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException("Malformed   \\uxxxx   encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';
					else if (aChar == 'n')
						aChar = '\n';
					else if (aChar == 'f')
						aChar = '\f';
					outBuffer.append(aChar);
				}
			} else
				outBuffer.append(aChar);
		}
		return outBuffer.toString();
	}

	public static String filterHtml(String str) {
		Pattern pattern = Pattern.compile("<[^>]*>");
		Matcher matcher = pattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		boolean result1 = matcher.find();
		while (result1) {
			matcher.appendReplacement(sb, "");
			result1 = matcher.find();
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	public static String filterDirty(String str) {
		Pattern pattern = Pattern.compile("&([^;]*);");
		Matcher matcher = pattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		boolean result1 = matcher.find();
		while (result1) {
			matcher.appendReplacement(sb, "");
			result1 = matcher.find();
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	public static String filterLeftAndRightSpace(String str) {
		Pattern pattern = Pattern.compile(">([^ ]*)<");
		Matcher matcher = pattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		boolean result1 = matcher.find();
		while (result1) {
			matcher.appendReplacement(sb, "");
			result1 = matcher.find();
		}
		matcher.appendTail(sb);
		return sb.toString();
	}
	
	public static void saveStr2File(String str, String filePath){
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(filePath);
			fileWriter.write(str);
			fileWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {

		String aa = "a=";
		System.out.println(aa.split("=").length);
	}
}
