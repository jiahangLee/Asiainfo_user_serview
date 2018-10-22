package gags.service.controller;

import gags.service.dao.ProgramRankQueryDAO;
import gags.service.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ProgramRankController {
    @Autowired
    private ProgramRankQueryDAO programRankQueryDAO;

    @RequestMapping(path = "/programRanks")
    public Map programRankQuery(@RequestParam String areaType,
                                @RequestParam String biz, //dvb, ...
                                @RequestParam String bizSubtype, //common
                                @RequestParam String period, //1,5,15,30,60,day,week,weekday,month,halfmonth,year
                                @RequestParam String startTime,
                                @RequestParam String sectionName,
                                @RequestParam(name = "fields", required = false) String fields, // kpi's englishName
                                @RequestParam(name = "areaName", required = false) String areaName,
                                @RequestParam(name = "bizCustom1", required = false) String bizCustom1,
                                @RequestParam(name = "bizCustom2", required = false) String bizCustom2,
                                @RequestParam(name = "bizCustom3", required = false) String bizCustom3){
        Map result = new HashMap();
        //图表/列表数据
        List<Map<String, Object>> data;
        //统计分布数据（）
        List distributionData = new ArrayList();
        List<String> newKpis = new ArrayList();
        String whereStr = "";
        //TODO 确定时间格式
        String startTimeStr = "";
        String fieldsStr = "*";
        String statistic_time_start = "";
        try {
            if (NumberUtils.isNumber(period)) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                startTimeStr = dateFormat.parse(startTime).getTime() / 1000 + "";
                statistic_time_start = " statistic_time=";
            } else {
                startTimeStr = DateUtil.getTransdate(period, startTime);
                statistic_time_start = " REPLACE(STR_TO_DATE(FROM_UNIXTIME(statistic_time),'%Y-%m-%d'),'-','')=";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(statistic_time_start);
        if (!startTime.equalsIgnoreCase("null")) {
            whereStr += statistic_time_start + " '" + startTimeStr + "'";
        }
        if (areaName != null && !"0".equals(areaName)) {
            whereStr += " and area_name='" + areaName + "'";
        }
        if (bizCustom1 != null) {
            whereStr += " and bizCustom1='" + bizCustom1 + "'";
        }
        if (bizCustom2 != null) {
            whereStr += " and bizCustom2='" + bizCustom2 + "'";
        }
        if (bizCustom3 != null) {
            whereStr += " and bizCustom3='" + bizCustom3 + "'";
        }

        fieldsStr = "*";
        if (fields != null) {
            //拼接环比，同比
            for (String kpi : fields.split(",")) {
                newKpis.add(kpi);
                newKpis.add(kpi + "_mom");
                newKpis.add(kpi + "_yoy");
            }
            fieldsStr = StringUtils.join(newKpis.toArray(new String[0]), ",").toLowerCase();
            if (!NumberUtils.isNumber(period)) {
                fieldsStr += ",area_name, DATE_FORMAT(FROM_UNIXTIME(statistic_time),'%Y-%c-%d')  AS \"statistic_time\" ,statistic_time as orderTime ";
            }else{
                fieldsStr += ",area_name, DATE_FORMAT(FROM_UNIXTIME(statistic_time),'%Y-%c-%d %H:%i:%s')  AS \"statistic_time\" ,statistic_time as orderTime ";
            }
        }
        try {
            data = programRankQueryDAO.query(areaType,biz,period,sectionName.hashCode()+"",whereStr,fieldsStr);
        } catch (Exception e) {
            data = new ArrayList();
        }
        result.put("chartData", data);
        result.put("distributionData", distributionData);
        return result;
    }
}
