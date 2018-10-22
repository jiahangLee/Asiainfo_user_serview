package gags.service.util;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangtao15 on 2017-07-24.
 */
public class DateUtil {
    public static final String DATE_TYPE_YYYY_MM_DD = "yyyy-MM-dd";

    public static final String DATE_TYPE_YYYY_MM_DD_HHMMSS = "yyyy-MM-dd HH:mm:ss";

    public static final Long DATE_DAY_NUMBER = 86400L;
    /**
     * 获取当前日期
     * @return
     */
    public static final Date getGlobalDate(){
        return new Date(System.currentTimeMillis());
    }

    /**
     * 转换日期
     * @param param
     * @param format
     * @return
     */
    public static final Date parseDate(String param,String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(param);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 日期转换
     * @param date
     * @param format
     * @return
     */
    public static final String formatDate(Date date ,String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当天日期
     * @param lastDate
     * @return
     */
    public static final Long getToday(Date lastDate) {
        Date date = lastDate;
        date.setDate(date.getDate());
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        return date.getTime() / 1000 ;
    }

    /**
     * 获取前一天日期
     * @param lastDate
     * @return
     */
    public static final Long getYesterday(Date lastDate) {
        Date date = lastDate;
        date.setDate(date.getDate()-1);
        date.setHours(date.getHours()+1);
        date.setMinutes(0);
        date.setSeconds(0);
        return date.getTime() / 1000 ;
    }

    /**
     * 获取前一天日期
     * @param lastDate
     * @return
     */
    public static final Long getYester2day(Date lastDate) {
        Date date = lastDate;
        date.setDate(date.getDate()-3);
        date.setHours(date.getHours()+1);
        date.setMinutes(0);
        date.setSeconds(0);
        return date.getTime() / 1000 ;
    }

    /**
     * 获取当前日期
     * @param currDate
     * @return
     */
    public static final Long getCurrentDay(Date currDate) {
        Date date = currDate;
        date.setMinutes(0);
        date.setSeconds(0);
        return date.getTime() / 1000 ;
    }

    public static String getTransdate(String period, String dateStr) {
        String transdate = "";
        if ("week".equals(period)) {
            transdate = getWeekStartDate(dateStr);
        } else if ("halfmonth".equals(period)) {
            transdate = getHalfMonthStartDate(dateStr);
        } else if ("month".equals(period)) {
            transdate = getMonthStartDate(dateStr);
        } else if ("quarter".equals(period)) {
            transdate = getQuarterStartDate(dateStr);
        } else if ("year".equals(period)) {
            transdate = getYearStartDate(dateStr);
        } else if ("day".equals(period)) {
            transdate = dateStr.substring(0, dateStr.length() - 1);
        } else {
            transdate = dateStr;
        }
        return transdate;
    }


    /**
     * 获取开始日期到结束日期期间的所有日期
     * @param startTime
     * @param endTime
     * @param period
     * @return
     */
    public static String [] convertESDate(String startTime,String endTime,String period) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
        List indies = new ArrayList();
        try {
            Date startDate = new Date(Long.valueOf(startTime+"000"));
            String startDateStr = dateFormat.format(startDate);

            int startYear = Integer.parseInt(startDateStr.substring(0, 4));
            int startMonth = Integer.parseInt(startDateStr.substring(4, 6));

            if (endTime == null) {
                if (period.equals("year")) {
                    indies.add(startYear + "12");
                } else if (period.equals("quarter")) {
                    indies.add(startYear + "" + (startMonth + 2));
                } else {
                    indies.add(startDateStr);
                }
            } else {
                Date endDate = new Date(Long.valueOf(endTime+"000"));
                String endDateStr = dateFormat.format(endDate);

                int endYear = Integer.parseInt(endDateStr.substring(0, 4));
                int endMonth = Integer.parseInt(endDateStr.substring(4, 6));

                if (period.equals("year")) {
                    while (startYear <= endYear) {
                        indies.add(startYear + "12");
                        startYear++;
                    }
                } else if (period.equals("quarter")) {
                    long startTimes = startDate.getTime();
                    startTimes =  convertMonth(new Date(startTimes),2).getTime();

                    Date esDate1 = new Date(startTimes);
                    indies.add(dateFormat.format(esDate1));
                    System.out.println(dateFormat.format(esDate1));

                    while(startTimes < convertMonth(endDate,3).getTime()){
                        startTimes =  convertMonth(new Date(startTimes),3).getTime();
                        Date esDate = new Date(startTimes);
                        indies.add(dateFormat.format(esDate));
                        System.out.println(dateFormat.format(esDate));
                    }

                } else {
                    indies.add(startDateStr);
                    long startTimes = startDate.getTime();
                    while(startTimes < convertMonth(endDate,0).getTime()){
                        startTimes =  convertMonth(new Date(startTimes),1).getTime();
                        Date esDate = new Date(startTimes);
                        indies.add(dateFormat.format(esDate));
                        System.out.println(dateFormat.format(esDate));
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (indies.size() > 0) {
            String lastIndex = indies.get(indies.size() - 1).toString();
            int year = Integer.valueOf(lastIndex.substring(0, 4));
            int month = Integer.valueOf(lastIndex.substring(4, 6));
            if (month == 12) {
                indies.add(++year + "01");
            } else {
                if (month < 9) {
                    indies.add(year + "0" + (++month));
                } else {
                    indies.add(year + "" + (++month));
                }
            }
        }
        return (String[])indies.toArray(new String[indies.size()]);
    }

    private static Date convertMonth(Date startDate,int count)throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateFormat.parse(dateFormat.format(startDate)));
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + count);
        Date date = cal.getTime();
        return date;
    }

    /**
     * 转换周日期显示格式
     * @param value 时间戳
     * @return
     */
    public static String getWeekCnvDate(long value){
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);//设置周一为一周的第一天
        cal.setTime(new Date(value));
        String week = String.valueOf(cal.get(Calendar.WEEK_OF_YEAR));
        String year = String.valueOf(cal.get(Calendar.YEAR));
        return year + "年" + week + "周";
    }

    /**
     * 转换半月日期显示格式
     * @param value 时间戳
     * @return
     */
    public static String getHalfMonthCnvDate(long value) {
        String halfMonth = "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(value));
        String day = String.valueOf(cal.get(Calendar.DATE));
        String month = String.valueOf(cal.get(Calendar.MONTH)+1);
        String year = String.valueOf(cal.get(Calendar.YEAR));
        if (day.equals("1")) {
            halfMonth = "上半月";
        } else {
            halfMonth = "下半月";
        }
        return year + "年" + month + "月" + halfMonth;
    }

    /**
     * 转换月日期显示格式
     * @param value 时间戳
     * @return
     */
    public static String getMonthCnvDate(long value) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(value));
        String month = String.valueOf(cal.get(Calendar.MONTH)+1);
        String year = String.valueOf(cal.get(Calendar.YEAR));
        return year + "年" + month + "月";
    }

    /**
     * 转换季度日期显示格式
     * @param value 时间戳
     * @return
     */
    public static String getQuarterCnvDate(long value) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(value));
        String month = String.valueOf(cal.get(Calendar.MONTH)+1);
        String year = String.valueOf(cal.get(Calendar.YEAR));
        switch (month) {
            case "1":
                return year + "年1季度";
            case "4":
                return year + "年2季度";
            case "7":
                return year + "年3季度";
            case "10":
                return year + "年4季度";
            default:
                return year + "年" + month + "月";
        }
    }

    /**
     * 转换年日期显示格式
     * @param value 时间戳
     * @return
     */
    public static String getYearCnvDate(long value){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(value));
        String year = String.valueOf(cal.get(Calendar.YEAR));
        return year + "年" ;
    }

    //取周开始时间
    public static String getWeekStartDate(String dateStr) {
        int year = Integer.parseInt(dateStr.substring(0, 4));
        int week = Integer.parseInt(dateStr.substring(4, 6));
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);//设置周一为一周的第一天
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        cal.set(Calendar.WEEK_OF_YEAR, week);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date date = cal.getTime();
        String weekDate = date.getTime() / 1000 + "";
        System.out.println(new SimpleDateFormat("yyyyMMdd").format(date));
        return new SimpleDateFormat("yyyyMMdd").format(date);
    }

    //取半月开始时间
    public static String getHalfMonthStartDate(String dateStr) {
        int year = Integer.parseInt(dateStr.substring(0, 4));
        int month = Integer.parseInt(dateStr.substring(4, 6));
        int halfmonth = Integer.parseInt(dateStr.substring(6, 7));
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        if (halfmonth == 0) {
            cal.set(Calendar.DAY_OF_MONTH, 1);
        } else {
            cal.set(Calendar.DAY_OF_MONTH, 16);
        }
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date date = cal.getTime();
        String weekDate = date.getTime() / 1000 + "";
//        System.out.println(new SimpleDateFormat("yyyyMMdd").format(date));
        return new SimpleDateFormat("yyyyMMdd").format(date);
    }

    //取月开始时间
    public static String getMonthStartDate(String dateStr) {
        int year = Integer.parseInt(dateStr.substring(0, 4));
        int month = Integer.parseInt(dateStr.substring(4, 6));
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date date = cal.getTime();
        String monthDate = date.getTime() / 1000 + "";
//        System.out.println(new SimpleDateFormat("yyyyMMdd").format(date));
        return new SimpleDateFormat("yyyyMMdd").format(date);
    }

    //取季度开始时间
    public static String getQuarterStartDate(String dateStr) {
        int year = Integer.parseInt(dateStr.substring(0, 4));
        int month = Integer.parseInt(dateStr.substring(4, 5));
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        if (month == 0) {
            cal.set(Calendar.MONTH, 0);
        }
        if (month == 1) {
            cal.set(Calendar.MONTH, 3);
        }
        if (month == 2) {
            cal.set(Calendar.MONTH, 6);
        }
        if (month == 3) {
            cal.set(Calendar.MONTH, 9);
        }
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date date = cal.getTime();
        String weekDate = date.getTime() / 1000 + "";
//        System.out.println(new SimpleDateFormat("yyyyMMdd").format(date));
        return new SimpleDateFormat("yyyyMMdd").format(date);
    }

    //取年开始时间
    public static String getYearStartDate(String dateStr) {
        int year = Integer.parseInt(dateStr.substring(0, 4));
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date date = cal.getTime();
        String weekDate = date.getTime() / 1000 + "";
//        System.out.println(new SimpleDateFormat("yyyyMMdd").format(date));
        return new SimpleDateFormat("yyyyMMdd").format(date);
    }

    public static String getKpiTimeES(@RequestParam String period, @RequestParam String kpiTime) {
        try {
            if (NumberUtils.isNumber(period)) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return dateFormat.parse(kpiTime).getTime() / 1000 + "";
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                return dateFormat.parse(DateUtil.getTransdate(period,kpiTime)).getTime() / 1000 + "";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "0";
    }

    public static void main1(String args[]){
//        convertESDate("1451577600","1514736000","quarter");
        System.out.print( getYesterday(new Date()).toString());
//        System.out.print(getQuarterStartDate("20163"));
    }

}
