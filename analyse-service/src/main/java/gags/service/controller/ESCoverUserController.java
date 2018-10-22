package gags.service.controller;

import gags.service.util.DateUtil;
import gags.service.util.ESDataUtil;
import gags.service.util.MD5Util;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.index.query.QueryBuilder;
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

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Created by zhangzc Lee on 2018/1/25
 */
@RestController
public class ESCoverUserController {


    @RequestMapping(path = "/coverUser")
    public Map programRankQuery(@RequestParam String period, //1,5,15,30,60,day,week,weekday,month,halfmonth,year
                                @RequestParam String startTime,
                                @RequestParam String endTime,
								@RequestParam(name = "businessType", required = false) String businessType,
                                @RequestParam(name = "areaName", required = false) String areaName){
        Map result = new HashMap();
        if("advert".equals(businessType)){
            return result;
        }
        //图表/列表数据
        List<Map<String, Object>> data = new ArrayList<>();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("ts","时移业务分析");
        map.put("dvb","直播业务分析");
        map.put("replay","回看业务分析");
        map.put("education","教育");
        map.put("ott","OTT点播业务分析");
        map.put("vod","VOD点播业务分析");
        map.put("appstore","应用商店");
        map.put("life_product","百灵K歌");
        map.put("happy_fitness_group","幸福健身团");
        map.put("in_community","智慧社区");
        map.put("game_visit","游戏大厅");
        map.put("advert","广告分析");
        map.put("overview","电视概况");

        List<String> newKpis = new ArrayList();
        newKpis.add("statistic_time");
        newKpis.add("area_name");
        newKpis.add("online_user");
        newKpis.add("new_user");

        String tableName = new StringBuffer("t_project_")
                .append(businessType).append("_")
                .append("common")
                .append("_").append(period)
                .toString().toLowerCase();

        String kpiStartTime = getKpiTimeES(period, startTime);
        String kpiEndTime = getKpiTimeES(period, endTime);

        QueryBuilder dfqb = boolQuery()
                .must(StringUtils.isNotBlank(areaName) && !"0".equals(areaName) ? termQuery("area_name", areaName) : matchAllQuery())
                //.must(StringUtils.isNotBlank(bizCustoms) ? matchPhraseQuery("section_name", bizCustoms) : matchAllQuery())
                // .must(StringUtils.isNotBlank(bizCustom2)?termQuery("biz_custom2",bizCustom2):matchAllQuery())
                // .must(StringUtils.isNotBlank(bizCustom3)?termQuery("biz_custom3",bizCustom3):matchAllQuery())
                .filter(rangeQuery("statistic_time").from(kpiStartTime).to(kpiEndTime));

        String[] indies = DateUtil.convertESDate(kpiStartTime,kpiEndTime,period);


        ESDataUtil.makeESDataMap(indies,data, tableName.toLowerCase(), newKpis, dfqb, period);
        //System.err.println(data.get(0));

        if(StringUtils.isEmpty(businessType)||"".equals(businessType)||areaName == "-1"){
            result.put("coverUserData",data);
            return result;
        }
        for(int i=0;i<data.size();i++){
            data.get(i).put("business_type",map.get(businessType));
//            data.get(i).put("add_user",data.get(i).get("new_user"));
//            data.get(i).remove("new_user");
//			data.get(i).put("cover_user",data.get(i).get("online_user"));
//			data.get(i).remove("online_user");
        }
        result.put("coverUserData",data);
        return result;
    }

    private String getKpiTimeES(@RequestParam String period, @RequestParam String kpiTime) {
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

}
