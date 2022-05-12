package com.daquv.Utils;


import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 정규화 체크, 공통적으로 사용되는 유틸은 여기다 작성하세요
 */
public class CommonUtil {

    private static DecimalFormat decimalFormat = new DecimalFormat("#,##0");
    private static DecimalFormat usdDecimalFormat = new DecimalFormat("#,##0.##");

    /**
     * email 정규패턴 체크
     * @param email
     * @return
     */
    public boolean isValidEmail(String email)
    {
        boolean err = false;
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        if(m.matches())
        {
            err = true;
        }
        return err;
    }

    /**
     * 숫자 여부 체크
     * @param strNum
     * @return
     */
    public static boolean isValidNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * 클라이언트 IP
     * @param request
     * @return
     * @throws Exception
     */
    public String getIp(HttpServletRequest request) throws Exception {

        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("WL-Proxy-Client-IP"); // 웹로직
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


    /**
     * 입력 date가 yyyy-MM-dd 형태로 들어옴
     *
     * @param checkDate
     * @return
     */
    public static boolean isValidDate(String checkDate){
        try{
            SimpleDateFormat  dateFormat = new  SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            dateFormat.parse(checkDate);
            return  true;
        }catch (ParseException  e){
            return  false;
        }
    }

    /**
     * Gets format date.
     * 현재 날짜를 format에 맞춰 반환.
     *
     * format 유형
     * - yyyy : 년도
     * - MM : 월
     * - dd : 일
     * - hh : 시간
     * - mm : 분
     * - ss : 초
     * - SSS 밀리초
     *
     * @param format the format
     * @return the format date
     */
    public static Date getFormatDate(String date, String format) {
        SimpleDateFormat sDate = new SimpleDateFormat(format);
        Date formatDate = null;
        try {
            formatDate = sDate.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return formatDate;
    }
    public static String getCurrentDateFormatString(String format) {
        SimpleDateFormat sDate = new SimpleDateFormat((format));
        return sDate.format(new Date());
    }

    public static String getFullFormatDate(String date) {
        SimpleDateFormat sDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date formatDate = null;
        try {
            formatDate = sDate.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        return formatDate.toString();
    }



    public static String formatUSDDecimal(String usdDecimal) {
        return usdDecimalFormat.format(usdDecimal);
    }

    public static String formatDecimal(String decimal) {
        return decimalFormat.format(decimal);
    }
    public static String formatDecimal(double decimal) {
        return decimalFormat.format(decimal);
    }

}