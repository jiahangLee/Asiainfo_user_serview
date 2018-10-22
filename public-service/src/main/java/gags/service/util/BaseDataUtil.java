package gags.service.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangtao15 on 2017-06-16.
 */
public class BaseDataUtil {
    /**
     * 状态正常
     */
    public static final Integer STATUS_INVALID = 0;
    /**
     * 状态失效
     */
    public static final Integer STATUS_NORMAL = 1;

    public static final String  FAILD_VALUE = "FAIL";

    public static final String  SUCCESS_VALUE = "SUCCESS";


    public static final String DATE_TYPE_YYYY_MM_DD = "yyyy-MM-dd";

    public static final String DATE_TYPE_YYYY_MM_DD_HHMMSS = "yyyy-MM-dd HH:mm:ss";


    public static final String AUTH_ERROR_MSG = "权限不足！";

    /**
     * 获取用户信息url
     */
    public static final String USER_URL = "http://USER-SERVICE/user/operator/login?loginName={loginName}&password={password}";

    /**
     * 获取地市信息url
     */
    public static final String AREA_URL = "http://USER-SERVICE/user/organize/areaList?operatorId={1}";

    /**
     * 排行接口url
     */
    public static final String ANALYSES_RANK_URL = "http://ANALYSE-SERVICE/analyses/{requestName}?areaType={areaType}&biz={biz}&bizSubtype={bizSubtype}&period={period}&startTime={startTime}&endTime={endTime}&areaName={areaName}&fields={fields}&sectionName={sectionName}&topNumber={topNumber}";

    /**
     * 洞察接口url
     */
    public static final String ANALYSES_STATISTIC_URL = "http://ANALYSE-SERVICE/analyses/statistic?areaType={areaType}&biz={biz}&bizSubtype={bizSubtype}&period={period}&startTime={startTime}&endTime={endTime}&areaName={areaName}&fields={fields}&bizCustom1={sectionName}";




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


    public static final Date parseDate(Long param){
        return new Date(param);
    }
}
