package com.daquv.Connector;

import com.daquv.Dto.ResponseResult;
import com.daquv.Utils.StringUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import retrofit2.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component("commonUtils")
public class CommonUtils {
	private final static String CLASS_NM = "[commonUtils]";
	
	public static String getRandomString() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * printMap
	 * @param map
	 */
	public static void printMap(Map<String, Object> map) {
		Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
		Entry<String, Object> entry = null;
		System.out.println("--------------------printMap--------------------\n");
		while (iterator.hasNext()) {
			entry = iterator.next();
			System.out.println("key : " + entry.getKey() + ",\tvalue : " + entry.getValue());
		}
		System.out.println("");
		System.out.println("------------------------------------------------\n");
	}

	/**
	 * printList
	 * @param list
	 */
	public static void printList(List<Map<String, Object>> list) {
		Iterator<Entry<String, Object>> iterator = null;
		Entry<String, Object> entry = null;
		System.out.println("--------------------printList--------------------\n");
		int listSize = list.size();
		for (int i = 0; i < listSize; i++) {
			System.out.println("list index : " + i);
			iterator = list.get(i).entrySet().iterator();
			while (iterator.hasNext()) {
				entry = iterator.next();
				System.out.println("key : " + entry.getKey() + ",\tvalue : " + entry.getValue());
			}
			System.out.println("\n");
		}
		System.out.println("------------------------------------------------\n");
	}



	/**
	 * 숫자에 천단위마다 콤마 넣기
	 *
	 * @param num
	 * @return String
	 */
	public static String toNumFormat(int num) {
		DecimalFormat df = new DecimalFormat("#,###");
		return df.format(num);
	}

	public static String[] weekCalendar(String yyyymmdd) throws Exception {

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -7);

		// 1. 현재 로그인 날짜의 월요일부터 금요일까지 날짜 예를 지금 2012-06-11 월요일에 것은 바로 2012-06-15
		// 2012-06-11 금요일.
		calendar.set(Calendar.DAY_OF_WEEK, 1);
		System.out.println("로그인 날짜의 일요일: " + calendar.getTime());
		// 2.금요일, 일s
		calendar.set(Calendar.DAY_OF_WEEK, 7);
		System.out.println("로그인 날짜의 토요일: " + calendar.getTime());

		return null;
	}

	/**
	 * Util 추가 시작 Auth :  Date : 2017-12-19
	 */

	// 세자리마다 콤마를 찍어주고 fl길이만큼 소수 표시
	public static String formatColor(String str, int fl, int comma, String upcolor, String downcolor,
			String stablecolor) {
		String lsValue = "";
		String formatted = "";
		double d_price = 0;
		try {
			d_price = (new Double(str)).doubleValue();
		} catch (Exception e) {
			return str;
		}
		formatted = format(str, fl, comma);

		if (d_price > 0)
			lsValue = "<font color=" + upcolor + ">" + formatted + "</font>";
		else if (d_price < 0)
			lsValue = "<font color=" + downcolor + ">" + formatted + "</font>";
		else
			lsValue = "<font color=" + stablecolor + ">" + formatted + "</font>";
		return lsValue;
	}

	// 세자리마다 콤마를 찍어주고 fl길이만큼 소수 표시
	public static String format(String str, int fl, int comma) {
		// try{
		int MAXCHAR = 32;
		if (str == null || str.length() <= 0)
			return "";

		char[] buffer = new char[MAXCHAR];
		if ((str.charAt(0) == '+') || (str.charAt(0) == '-')) {
			buffer[0] = str.charAt(0);
			str = str.substring(1);
		} else {
			buffer[0] = ' ';
		}

		// Remove Left Zero
		if (str.length() > 0) {
			str = removeFrontZero(str);
		}

		int index = MAXCHAR - 1;
		int i = str.length() - 1;

		if (fl != 0) {
			for (int j = 0; j < fl; j++) {
				if (i >= 0) {
					if (Character.isDigit(str.charAt(i))) {
						buffer[index] = str.charAt(i);
					}
				} else {
					buffer[index] = '0';
				}
				index--;
				i--;
			}
			buffer[index] = '.';

			index--;
			if (i < 0) {
				buffer[index] = '0';
				if (buffer[0] == '+' || buffer[0] == '-') {
					index--;
					buffer[index] = buffer[0];
				}
				return new String(buffer, index, MAXCHAR - index);
			}
		}

		int intIndex = index - comma;
		for (; i >= 0; i--) {
			if (str.charAt(i) == '.' || str.charAt(i) == ',')
				continue;
			if (comma > 0) {
				if ((((index - intIndex) % (comma + 1)) == 0) && Character.isDigit(str.charAt(i))) {
					buffer[index] = ',';
					index--;
				}
			}
			if (index < 0)
				return str;
			buffer[index] = str.charAt(i);
			index--;
		}

		if (buffer[0] == '+' || buffer[0] == '-') {
			buffer[index] = buffer[0];
			index--;
		}
		return (new String(buffer, index + 1, MAXCHAR - index - 1)).trim();
	}

	// 숫자 0 없애주기
	public static String removeFrontZero(String data) {
		if (data.length() <= 0)
			return "0";
		if (data == null)
			return "0";

		String str = data;
		String sign = "", dot = "";
		if (data.startsWith("-")) {
			sign = "-";
			str = str.substring(1);
		} else if (data.startsWith("+")) {
			sign = "";
			str = str.substring(1);
		}
		while (str.charAt(0) == '0') {
			if (str.length() >= 2)
				str = str.substring(1);
			else {
				return "0";
			}
		}

		if (str.charAt(0) == '.')
			return sign + "0" + str;
		else
			return sign + str;
	}

	// 등락부호에 따른 이미지 태그 삽입함수
	public static String SigntoImage(String signal) {
		String strImg = " ";
		if (signal.equals("1") || signal.equals("6")) {
			strImg = "<font color=red><img src=/stock/image/arrow_up.gif border=0></font>";
		} else if (signal.equals("2") || signal.equals("7")) {
			strImg = "<font color=red><img src=/stock/image/triangle_up.gif border=0></font>";
		} else if (signal.equals("4") || signal.equals("8")) {
			strImg = "<font color=blue><img src=/stock/image/arrow_dn.gif border=0></font>";
		} else if (signal.equals("5") || signal.equals("9")) {
			strImg = "<font color=blue><img src=/stock/image/triangle_dn.gif border=0></font>";
		} else {
			strImg = "";
		}
		return strImg;
	}

	/**
	 * 숫자 이외의 모든 캐릭터를 없앤다.(단, - 부호는 없애지 않는다,)
	 */
	public static String removeCharNotMinus(String str) {
		byte arry[] = str.getBytes();

		String ret_value = "";

		for (int ii = 0; ii < arry.length; ii++) {
			if (48 <= arry[ii] && arry[ii] <= 57 || arry[ii] == 45) {
				ret_value += (char) arry[ii];
			}
		}

		return ret_value;
	}

	// 날짜형식(YY/MM/DD)으로 변환
	public static String StringToDate(String Date) {
		String ret_date = "";
		try {
			if (Date == null || Date == "" || Date.length() < 8) {
				ret_date = "";
			} else {
				ret_date = Date.substring(0, 4) + "/" + Date.substring(4, 6) + "/" + Date.substring(6, 8);
			}
		} catch (Exception ex) {
			ret_date = Date;
			System.out.println("[ERR][UTILITY.STRINGTODATE] : [" + ex + "]");
		}
		return ret_date;
	}

	// 시간형식(hh:mm:ss)으로 바꿔주는 함수
	public static String StringToTime(String time) {
		if (time == null || time == "") {
			return time;
		} else {
			return time.substring(0, 2) + ":" + time.substring(2, 4) + ":" + time.substring(4, 6);
		}
	}

	// 문자열에서 특정 문자열 삭제하는 함수
	public static String removeString(String charSource, String removeChar) {
		if (removeChar == null || charSource == null)
			return removeChar;

		int i = charSource.indexOf(removeChar);
		String temp = charSource;
		while (i != -1) {
			temp = temp.substring(0, i) + temp.substring(i + removeChar.length());
			i = temp.indexOf(removeChar);
		}
		return temp;
	}

	//EUC_KR
	public static String toKorean(String s) {
		try {
			if (s != null)
				return new String(s.getBytes("8859_1"), "EUC_KR");
			else
				return s;
		} catch (UnsupportedEncodingException e) {
			return "Encoding Error";
		}
	}

	//8859_1
	public static String toKorean2(String s) {
		try {
			if (s != null)
				return new String(s.getBytes("ksc5601"), "8859_1");
			else
				return s;
		} catch (UnsupportedEncodingException e) {
			return "Encoding Error";
		}
	}

	// 콤마 삭제
	public static String delComma(String strValue) {
		int strlength = strValue.length();
		String tempStr = "";
		// System.out.println(strValue+":"+strlength);
		for (int i = 0; i < strlength; i++) {
			tempStr = strValue.substring(i, i + 1);
			if (tempStr.equals(",")) {
				strValue = strValue.substring(0, i) + strValue.substring(i + 1);
				strlength--;
			}
		}
		return strValue;
	}

	/**
	 * String을 byte[]로 변환하여 길이가 cnt가 될때까지 s문자를 반복하여 붙인다. gubun = 1 : 앞에 반복하여 붙인다.
	 * gubun = 2 : 뒤에 반복하여 붙인다.
	 *
	 * @param in
	 * @param cnt
	 * @param s
	 * @param gubun
	 * @return String
	 */

	public static String fillString(String in, int cnt, String s, String gubun) {
		if (in == null)
			in = "";

		if (s.equals("0")) {
			in = delComma(in);
		}

		StringBuffer strbuf = new StringBuffer(in);
		byte[] b = in.getBytes();

		if ("1".equals(gubun))
			strbuf = strbuf.reverse();

		for (int i = 0; i < (cnt - b.length); i++) {
			strbuf.append(s);
		}
		if ("1".equals(gubun))
			strbuf = strbuf.reverse();

		return strbuf.toString();
	}

	/**
	 * @param date
	 * @return 날짜형식으로 변환
	 */
	public static String formatDate(String date) {
		if (date.trim().length() != 8) {
			return date;
		}

		return date.substring(0, 4) + "/" + date.substring(4, 6) + "/" + date.substring(6, 8);
	}

	/**
	 * IP형식으로 변환
	 *
	 * @param ip
	 * @return
	 */
	public static String getFormatIP(String ip) {
		if (ip == null || ip.indexOf(".") < 0)
			return "";
		try {
			StringTokenizer st = new StringTokenizer(ip, ".");
			String ip1 = fillString(st.nextToken(), 3, "0", "1");
			String ip2 = fillString(st.nextToken(), 3, "0", "1");
			String ip3 = fillString(st.nextToken(), 3, "0", "1");
			String ip4 = fillString(st.nextToken(), 3, "0", "1");
			System.out.println(ip1 + ip2 + ip3 + ip4);
			return ip1 + ip2 + ip3 + ip4;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 소수점 n자리까지 표시
	 *
	 * @param str
	 * @param n
	 * @return
	 */
	public static String dotsubString(String str, int n) {
		double db = Double.parseDouble(str);
		StringBuffer strBf = new StringBuffer();
		strBf.setLength(0);
		strBf.append("#,##0.");

		for (int i = 0; i < n; i++) {
			strBf.append("0");
		}
		return new DecimalFormat(strBf.toString()).format(db);
	}

	/**
	 * Linux Commond 실행
	 *
	 * @param shname
	 */
	public static void exeSh(String shname) {

		try {
			String s = null;
			Process proc = null;
			BufferedReader in = null;

			proc = Runtime.getRuntime().exec("/home/web/bin/" + shname);
			in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			while ((s = in.readLine()) != null) {
				break;
			}
		} catch (Exception ex) {
			System.out.println("[ERR] INSERTDOT[" + ex + "]");
		}
	}

	/**
	 * 오늘날짜
	 *
	 * @return
	 */
	public static String getDate() {
		Calendar now = Calendar.getInstance();
		String y = "", m = "", d = "", hh = "", mm = "", ss = "", ms = "";
		int year = now.get(Calendar.YEAR);
		int month = (now.get(Calendar.MONTH)) + 1;
		int day = now.get(Calendar.DAY_OF_MONTH);
		int hour = now.get(Calendar.HOUR_OF_DAY);
		int min = now.get(Calendar.MINUTE);
		int sec = now.get(Calendar.SECOND);
		long msec = now.get(Calendar.MILLISECOND);

		y = year + "";
		if (month < 10) {
			m = "0" + month;
		} else if (month >= 10) {
			m = "" + month;
		}
		if (day < 10) {
			d = "0" + day;
		} else if (day >= 10) {
			d = "" + day;
		}
		if (hour < 10) {
			hh = "0" + hour;
		} else if (hour >= 10) {
			hh = "" + hour;
		}
		if (min == 0) {
			mm = "00";
		} else if (min < 10) {
			mm = "0" + min;
		} else if (min >= 10) {
			mm = "" + min;
		}
		if (sec == 0) {
			ss = "00";
		} else if (sec < 10) {
			ss = "0" + sec;
		} else if (sec >= 10) {
			ss = "" + sec;
		}
		if (msec == 0) {
			ms = "000";
		} else if (msec < 100 && msec >= 10) {
			ms = "0" + msec;
		} else if (msec <= 10) {
			ms = "00" + msec;
		} else if (msec >= 100) {
			ms = "" + msec;
		}

		return y + m + d + hh + mm + ss + ms;
	}

	/**
	 * 현재시간
	 *
	 * @return
	 */
	public static String getTime() {
		Calendar now = Calendar.getInstance();
		String hh = "", mm = "", ss = "", ms = "";
		int hour = now.get(Calendar.HOUR_OF_DAY);
		int min = now.get(Calendar.MINUTE);
		int sec = now.get(Calendar.SECOND);
		long msec = now.get(Calendar.MILLISECOND);

		if (hour < 10) {
			hh = "0" + hour;
		} else if (hour >= 10) {
			hh = "" + hour;
		}
		if (min == 0) {
			mm = "00";
		} else if (min < 10) {
			mm = "0" + min;
		} else if (min >= 10) {
			mm = "" + min;
		}
		if (sec == 0) {
			ss = "00";
		} else if (sec < 10) {
			ss = "0" + sec;
		} else if (sec >= 10) {
			ss = "" + sec;
		}
		if (msec == 0) {
			ms = "000";
		} else if (msec < 100 && msec >= 10) {
			ms = "0" + msec;
		} else if (msec <= 10) {
			ms = "00" + msec;
		} else if (msec >= 100) {
			ms = "" + msec;
		}

		return hh + mm + ss + ms;
	}

	/**
	 * iDay 이전 날짜
	 *
	 * @param iDay
	 * @return
	 */
	public static String getDate(int iDay) {
		Calendar temp = Calendar.getInstance();
		StringBuffer sbDate = new StringBuffer();
		temp.add(Calendar.DAY_OF_MONTH, iDay);
		int nYear = temp.get(Calendar.YEAR);
		int nMonth = temp.get(Calendar.MONTH) + 1;
		int nDay = temp.get(Calendar.DAY_OF_MONTH);
		sbDate.append(nYear);
		if (nMonth < 10)
			sbDate.append("0");
		sbDate.append(nMonth);
		if (nDay < 10)
			sbDate.append("0");
		sbDate.append(nDay);
		return sbDate.toString();
	}

	public static String getOrdno() {
		Calendar now = Calendar.getInstance();
		String y = "", m = "", d = "", hh = "", mm = "", ss = "", ms = "";
		int year = now.get(Calendar.YEAR);
		int month = (now.get(Calendar.MONTH)) + 1;
		int day = now.get(Calendar.DAY_OF_MONTH);
		int hour = now.get(Calendar.HOUR_OF_DAY);
		int min = now.get(Calendar.MINUTE);
		int sec = now.get(Calendar.SECOND);
		long msec = now.get(Calendar.MILLISECOND);

		y = year + "";
		if (y.length() > 3)
			y = y.substring(2, 4);
		if (month < 10) {
			m = "0" + month;
		} else if (month >= 10) {
			m = "" + month;
		}
		if (day < 10) {
			d = "0" + day;
		} else if (day >= 10) {
			d = "" + day;
		}
		if (hour < 10) {
			hh = "0" + hour;
		} else if (hour >= 10) {
			hh = "" + hour;
		}
		if (min == 0) {
			mm = "00";
		} else if (min < 10) {
			mm = "0" + min;
		} else if (min >= 10) {
			mm = "" + min;
		}
		if (sec == 0) {
			ss = "00";
		} else if (sec < 10) {
			ss = "0" + sec;
		} else if (sec >= 10) {
			ss = "" + sec;
		}
		if (msec == 0) {
			ms = "000";
		} else if (msec < 100 && msec >= 10) {
			ms = "0" + msec;
		} else if (msec <= 10) {
			ms = "00" + msec;
		} else if (msec >= 100) {
			ms = "" + msec;
		}

		return y + m + d + hh + mm + ss + ms;
	}

	/**
	 * mysql datetime 타입으로 반환한다.
	 *
	 * @param gubun
	 * @return
	 */
	public static String getDate(String gubun) {
		String formatDateTime = null;
		if (gubun.equals("mysql")) {
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

			formatDateTime = now.format(formatter);
		} else {
			formatDateTime = getDate(); // 일반적인 형식으로 Return
		}

		return formatDateTime;
	}

	// ============================
	// 8859_1 --> KSC5601.
	// ============================
	public static String E2K(String english) {
		String korean = null;

		if (english == null)
			return null;

		try {
			korean = new String(new String(english.getBytes("8859_1"), "EUC-KR"));
		} catch (UnsupportedEncodingException e) {
			korean = new String(english);
		}
		return korean;
	}

	// ============================
	// KSC5601 --> 8859_1.
	// ============================
	public static synchronized String K2E(String korean) {
		String english = null;

		if (korean == null)
			return null;

		english = new String(korean);
		try {
			english = new String(new String(korean.getBytes("KSC5601"), "8859_1"));
		} catch (UnsupportedEncodingException e) {
			english = new String(korean);
		}
		return english;
	}

	/**
	 * 숫자앞부분 0제거
	 *
	 * @param str
	 * @return
	 */
	public static String frontZero(String str) {
		Pattern p = Pattern.compile("0*");
		Matcher m = p.matcher(str);
		boolean b = m.matches();
		if (b) {
			return "0";
		}

		int len = 0;
		boolean flag = true;
		for (int i = 0; i < str.length(); i++) {
			if (Double.parseDouble(str.substring(i, i + 1)) > 0 && flag) {
				len = i;
				flag = false;
			}
		}
		return str.substring(len);
	}

	/**
	 * 소수점 이하 뒷부분 0제거
	 *
	 * @param str
	 * @return
	 */
	public static String backZero(String str) {
		Pattern p = Pattern.compile("0*");
		Matcher m = p.matcher(str);
		boolean b = m.matches();
		if (b) {
			return "0";
		}

		int len = 0;
		for (int i = 0; i < str.length(); i++) {
			if (Integer.parseInt(str.substring(i, i + 1)) > 0) {
				len = i;
			}
		}
		return str.substring(0, len + 1);
	}

	/**
	 * , 삽입
	 *
	 * @param str
	 * @return
	 */
	public static String addCommaStr(String str) {
		if (str.length() < 4) {
			return str;
		}
		int len = str.length();
		int index = 0;
		StringBuffer sb = new StringBuffer();
		for (int i = len; i > 0; i--) {
			index++;
			sb.append(str.substring(i - 1, i));
			if (index % 3 == 0 && i != 1) {
				sb.append(",");
			}
		}
		return sb.reverse().toString();
	}

	/**
	 * 숫자 String을 기본 숫자데이터 형식으로 변환
	 *
	 * @param str
	 * @return
	 */
	public static String reStr(String str) {
		if (str.length() < 2) {
			return str;
		}
		StringBuffer sb = new StringBuffer();
		String sign = "";

		if (str.substring(0, 1).equals("-")) {
			sign = "-";
			sb.append(sign);
		}
		if (str.substring(0, 1).equals("+") || str.substring(0, 1).equals("-")) {
			str = str.substring(1);
		}

		if (str.indexOf(".") > 0) {
			String[] arr = str.split("[.]");
			sb.append(addCommaStr(frontZero(arr[0])));
			if (Integer.parseInt(backZero(arr[1])) > 0) {
				sb.append(".");
				sb.append(backZero(arr[1]));
			}
		} else {
			sb.append(addCommaStr(frontZero(str)));
		}
		return sb.toString();
	}

	/**
	 * Util 추가 : HashMap 기존데이터와 신규데이터 비교 후 기존데이터 치환
	 */
	public static HashMap mergeHeader(HashMap<String, Object> oldHeader, HashMap<String, Object> newHeader) {
		for(Entry<String, Object> newElement : newHeader.entrySet()){
			boolean flag = true;
			for(Entry<String, Object> oldElement : oldHeader.entrySet()){
					if(newElement.getKey().equalsIgnoreCase(oldElement.getKey())){
						oldElement.setValue(newElement.getValue());
						flag = false;
					}
			}
			if(flag){
				oldHeader.put(newElement.getKey(), newElement.getValue());
			}
		}
		return oldHeader;
	}

	public static ResponseResult returnResponse(Response<?> response){
		try {
			if (response.isSuccessful()) {
				return ResponseResult.success(response.body());
			} else {
				return ResponseResult.build(-1, response.message(), "", response.errorBody().string());
			}
		}catch(Exception e){
			return new ResponseResult(500, e.getMessage(), e.getMessage(), null, 0, "");
		}
	}

	

	public static String getLoginIp(HashMap<String, Object> headerMap) {
		String loginIp = "";
		try {
			String xfIp = StringUtil.null2String(headerMap.get("x-forwarded-for"));

			if(!xfIp.equals("")) {
				String[] aXfIp = xfIp.split(",");
				int iaXfIp = aXfIp.length;
				if(iaXfIp > 1) {
					loginIp = aXfIp[iaXfIp-2];
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

			try {
				loginIp = StringUtil.null2String(headerMap.get("x-forwarded-for"));
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("user_login_ip 추출 실패");
			}
		}

		return loginIp;
	}

	public static boolean notNullObject(Map params, String key) {
		if( params != null && params.containsKey(key) && params.get(key) != null ) {
			return true;
		}
		return false;
	}
	public static boolean notNull(Map params, String key) {
		if( params != null && params.containsKey(key) && params.get(key) != null && !"".equalsIgnoreCase(params.get(key).toString().replaceAll("\\s", "")) ) {
			return true;
		}
		return false;
	}
	public static boolean notNull(String value) {
		if( value != null && !"".equalsIgnoreCase(value.replaceAll("\\s", "")) ) {
			return true;
		}
		return false;
	}
	public static String getCurrentDateFormatString(String format) {
		SimpleDateFormat sDate = new SimpleDateFormat((format));
		return sDate.format(new Date());
	}
}
