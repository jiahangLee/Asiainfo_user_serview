package gags.service.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    /**
     * 过滤字段集合
     * @return
     */
    public static final List getFields(){
        List list = new ArrayList();
        list.add("section_name");
        list.add("subtype_name");
        list.add("appname");
        list.add("apptype1");
        list.add("apptype2");
        list.add("game_visit_type_name");
        list.add("game_visit_single_game_name");
        return list;
    }

    public static final Date parseDate(Long param){
        return new Date(param);
    }

    /**
     * 排序
     * @param data
     * @param sortField
     */
    public static List<Map<String, Object>> sortDataMap(List<Map<String, Object>> data, final String sortField, Integer topNumber) {
        Collections.sort(data, new Comparator<Map<String, Object>>() {

            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                double field1 = Double.valueOf(o1.get(sortField).toString());
                double field2 = Double.valueOf(o2.get(sortField).toString());
                if (field1 > field2) {
                    return -1;
                }
                if (field1 == field2) {
                    return 0;
                }
                return 1;
            }
        });

        for (int i = 1; i <= data.size(); i++) {
            data.get(i - 1).put("row_num", i);
        }

        if (topNumber != null && topNumber > 0 && topNumber < data.size()) {
            List<Map<String, Object>> newData = new ArrayList<>();
            for (int i = 0; i < topNumber; i++) {
                newData.add(data.get(i));
            }
            return newData;
        }
        return data;
    }
}
