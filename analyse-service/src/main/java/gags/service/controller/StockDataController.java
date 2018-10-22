package gags.service.controller;

import gags.service.dao.StatisticQueryDAO;
import gags.service.util.DateUtil;
import gags.service.util.ESDataUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Created by zhangzc on 2018/1/26.
 */
@RestController
public class StockDataController {

    @Autowired
    private StatisticQueryDAO stockDataDAO;

    @RequestMapping(path = "/stockData")
    public Map commonStatisticQuery(@RequestParam String period, //1,5,15,30,60,day,week,weekday,month,halfmonth,year
                                    @RequestParam String startTime,
                                    @RequestParam String endTime,
                                    @RequestParam(name = "areaName", required = false) String areaName,
                                    @RequestParam(name = "areaType", required = false) String areaType,
                                    @RequestParam(name = "businessType", required = false) String businessType) {
        Map result = new HashMap();
        //图表/列表数据
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("ts", "时移业务分析");
        map.put("dvb", "直播业务分析");
        map.put("replay", "回看业务分析");
        map.put("education", "教育");
        map.put("ott", "OTT点播业务分析");
        map.put("vod", "VOD点播业务分析");
        map.put("appstore", "应用商店");
        map.put("life_product", "百灵K歌");
        map.put("happy_fitness_group", "幸福健身团");
        map.put("in_community", "智慧社区");
        map.put("game_visit", "游戏大厅");
        map.put("advert", "广告分析");

        Map<String, Object> mapNumber = new HashMap<>();
//        mapNumber.put("ts", "3");
//        mapNumber.put("dvb", "2");
//        mapNumber.put("replay", "4");
//        mapNumber.put("education", "10");
//        mapNumber.put("ott", "6");
//        mapNumber.put("vod", "5");
//        mapNumber.put("appstor", "9");
//        mapNumber.put("life_product", "12");
//        mapNumber.put("happy_fitness_group", "21");
//        mapNumber.put("in_community", "11");
//        mapNumber.put("game_visit", "20");
//        mapNumber.put("advert", "14");

        mapNumber.put("base"     ,"1" );
        mapNumber.put("live"     ,"2" );
        mapNumber.put("time"     ,"3" );
        mapNumber.put("look"     ,"4" );
        mapNumber.put("vod"      ,"5" );
        mapNumber.put("ott"      ,"6" );
        mapNumber.put("play"     ,"7" );
        mapNumber.put("app"      ,"8" );
        mapNumber.put("store"    ,"9" );
        mapNumber.put("edu"      ,"10");
        mapNumber.put("create"   ,"11");
        mapNumber.put("life"     ,"12");
        mapNumber.put("adv"      ,"14");
        mapNumber.put("ecom"     ,"15");
        mapNumber.put("fault"    ,"16");
        mapNumber.put("pay"      ,"17");
        mapNumber.put("other"    ,"18");
        mapNumber.put("gline"    ,"19");
        mapNumber.put("game"     ,"20");
        mapNumber.put("gmovie"   ,"22");
        mapNumber.put("gtheater" ,"23");
        mapNumber.put("4k"       ,"24");

        Map<String, Object> mapType = new HashMap<>();
        mapType.put("",  "base"    );
        mapType.put("dvb",  "live"    );
        mapType.put("ts",  "time"    );
        mapType.put("replay",  "look"    );
        mapType.put("vod",  "vod"     );
        mapType.put("ott",  "ott"     );
        mapType.put("",  "play"    );
        mapType.put("",  "app"     );
        mapType.put("appstore",  "store"   );
        mapType.put("education",  "edu"     );
        mapType.put("in_community",  "create"  );
        mapType.put("life_product",  "life"    );
        mapType.put("happy_fitness_group",  "life"    );
        mapType.put("",  "ecom"    );
        mapType.put("",  "fault"   );
        mapType.put("",  "pay"     );
        mapType.put("",  "other"   );
        mapType.put("",  "gline"   );
        mapType.put("game_visit",  "game"    );
        mapType.put("",  "gmovie"  );
        mapType.put("",  "gtheater");
        mapType.put("",  "4k"      );
        mapType.put("advert",  "adv"      );

        String tableName = new StringBuffer("t_analyse_")
                .append(mapType.get(businessType)).append("_")
                .append("common")
                .append("_").append(period)
                .toString().toLowerCase();

        String whereStr = "";
        //TODO 确定时间格式
        String startTimeStr = "";
        String endTimeStr = "";

        startTimeStr = DateUtil.getTransdate(period, startTime);
        endTimeStr = DateUtil.getTransdate(period, endTime);


        if (!startTimeStr.equalsIgnoreCase("null")) {
            whereStr += " l_date>=" + "'" +startTimeStr + "'";
            if (!endTimeStr.equalsIgnoreCase("null")) {
                whereStr += " and l_date<=" + "'" + endTimeStr + "'";
                if (areaName != null) {
                    whereStr += " and projectno='" + areaName + "'";
                }
                if (businessType != null) {
                    whereStr += " and systemtype='" + mapNumber.get(mapType.get(businessType)) + "'" + " group by projectno,l_date";
                }
            }
        } else {
            if (!endTime.equalsIgnoreCase("null")) {
                whereStr += "l_date<" + "'"  + endTimeStr  + "'";
                if (areaName != null) {
                    whereStr += " and projectno='" + areaName + "'";
                }
                if (businessType != null) {
                    whereStr += " and systemtype='" + mapNumber.get(mapType.get(businessType)) + "'";
                }
            }
        }


        System.out.println(whereStr);
        String fieldsStr = "l_date as statistic_time,max(projectno) as area_name," +
                "round(sum(history)/(1024*1024),4) as history_data,round(sum(increment)/(1024),4)" +
                " as increment_data";

        if (StringUtils.isEmpty(businessType) || "".equals(businessType) || areaName == "-1") {
            result.put("stockData", data);
            return result;
        }
        try {
            data = stockDataDAO.queryStock(tableName, whereStr, fieldsStr);
            for (int i = 0; i < data.size(); i++) {
                data.get(i).put("business_type", map.get(businessType));
            }
            result.put("stockData", data);
            return result;
        } catch (Exception e) {
            result.put("stockData", new ArrayList());
            e.printStackTrace();
        } finally {
            return result;
        }
    }

}
