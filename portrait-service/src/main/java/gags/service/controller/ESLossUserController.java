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
 * 画像流失用户
 * Created by zhangzc Lee on 2018/4/18
 */
@RestController
public class ESLossUserController {


    @RequestMapping(path = "/lossUser")
    public Map programRankQuery(@RequestParam String period, //1,5,15,30,60,day,week,weekday,month,halfmonth,year
                                @RequestParam String periodValue,
								@RequestParam(name = "business", required = false) String business,
                                @RequestParam(name = "area", required = false) String area){
        Map result = new HashMap();
        //图表/列表数据
        List<Map<String, Object>> data = new ArrayList<>();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("all","全业务");
        map.put("dvb","直播业务分析");
        map.put("ts","时移");
        map.put("replay","回看");
        map.put("vod","VOD点播");
        map.put("ott","OTT点播");
        map.put("game","游戏");
        map.put("appstore","应用商店");
        map.put("edu","教育");
        map.put("in_community","智慧社区");
        map.put("life_product","生活产品");
        map.put("edusiness","电商平台");


        List<String> newKpis = new ArrayList();
        newKpis.add("statistic_time");
        newKpis.add("area_name");
        newKpis.add("online_user");
        newKpis.add("new_user");

        String tableName = new StringBuffer("yaxin.lossUser")
//                .append(business).append("_")
//                .append("common")
                .append("_").append(period)
                .toString().toLowerCase();

        String kpiStartTime = getKpiTimeES(period, periodValue);
        String kpiEndTime = getKpiTimeES(period, periodValue);

        QueryBuilder dfqb = boolQuery()
                .must(StringUtils.isNotBlank(area) && !"0".equals(area) ? termQuery("projectno", area) : matchAllQuery())
                //.must(StringUtils.isNotBlank(bizCustoms) ? matchPhraseQuery("section_name", bizCustoms) : matchAllQuery())
                // .must(StringUtils.isNotBlank(bizCustom2)?termQuery("biz_custom2",bizCustom2):matchAllQuery())
                // .must(StringUtils.isNotBlank(bizCustom3)?termQuery("biz_custom3",bizCustom3):matchAllQuery())
                .filter(rangeQuery("statistic_time").from(kpiStartTime).to(kpiEndTime));

        String[] indies = DateUtil.convertESDate(kpiStartTime,kpiEndTime,period);


        ESDataUtil.makeESDataMap(indies,data, tableName.toLowerCase(), newKpis, dfqb, period);
        //System.err.println(data.get(0));

        if(StringUtils.isEmpty(business)||"".equals(business)||area == "-1"){
            result.put("lossUserData",data);
            return result;
        }
//        for(int i=0;i<data.size();i++){
//            data.get(i).put("business",map.get(business));
//        }
        result.put("lossUserData",data);
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
