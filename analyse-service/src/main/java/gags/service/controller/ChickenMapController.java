package gags.service.controller;

import gags.service.dao.ChickenMapDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by jiahang Lee on 2017/7/20.
 */
@RestController
public class ChickenMapController {

    @Autowired
    private ChickenMapDAO chickenMapDAO;

    @RequestMapping(path = "/chicken")
    public Map getChicken(){

        Map result = new HashMap();
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        String tableName = new StringBuffer("T_").append("province").append("_")
                .append("overview").append("_")
                .append("common").append("_")
                .append("day").append(",t_config_area").toString().toUpperCase();
        String whereStr = "";
        String group = "";
        String fields = "label,area_name,sum(open_device_user) AS 'total'";
        //TODO 确定时间格式
        Date now = new Date();
        Calendar date = Calendar.getInstance();
        date.set(Calendar.DATE,date.get(Calendar.DATE)-1);
        String yesterday = "";
        String endTimeStr = "";
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
//            yesterday = dateFormat.parse(dateFormat.format(date).toString()).getTime() / 1000 + "";
            yesterday = dateFormat.parse("20170202").getTime() / 1000 + "";

        } catch (ParseException e) {
            e.printStackTrace();
        }

        group = "area_name";
        whereStr += " statistic_time=" + yesterday+" and t_province_overview_common_day.area_name = t_config_area.id";
        String fieldsStr = "*";
        fieldsStr = fields.toLowerCase();
        fieldsStr += ",area_name, DATE_FORMAT(FROM_UNIXTIME(statistic_time),'%Y-%c-%d')  AS \"statistic_time\" ,statistic_time as orderTime ";
        try {
            data = chickenMapDAO.query(tableName, whereStr, fieldsStr,group);
            result.put("chicken", data);
            return result;
        } catch (Exception e) {
            result.put("chicken", data);
            e.printStackTrace();
        } finally {
            return result                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           ;
        }
    }
}
