package gags.service.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static final String  DEFAULT_PWD= "123456";

    /**
     * 洞察分析id
     */
    public static final Long PARENTID_IA = 1002L;

    /**
     *页面访问id
     */
    public static final Long PARENTID_PAGEMANAGE = 103L;
    /**
     * 角色管理id
     */
    public static final Long PARENTID_ROLEMANAGE = 100L;
    /**
     * 用户管理id
     */
    public static final Long PARENTID_USERMANAGE = 101L;

    /**
     * 首页菜单级别
     */
    public static final Integer HOME_MENU_LEVEL = 1;
    /**
     * 洞察分析菜单级别
     */
    public static final Integer IA_MENU_LEVEL = 3;

    /**
     * 顶层id
     */
    public static final Long PARENT_ID = -1L;

    /**
     * 超级管理员id
     */
    public static final Long SUPER_USER_ID = 1L;

    /**
     * 区域顶层id
     */
    public static final Long AREA_ROOT_ID = -1L;

    public static final String DATE_TYPE_YYYY_MM_DD = "yyyy-MM-dd";

    public static final String DATE_TYPE_YYYY_MM_DD_HHMMSS = "yyyy-MM-dd HH:mm:ss";


    public static final String ZJ_ID = "9601";




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
