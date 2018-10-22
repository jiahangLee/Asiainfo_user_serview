package gags.service.controller;

import gags.service.dao.UserUseLogDAO;
import gags.service.entity.UserUseLog;
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
 * Created by lyl on 2017/2/23.
 */
@RestController
public class FlowStatisticController {
    @Autowired
    private UserUseLogDAO userUseLogDAO;

    @RequestMapping(path = "/flowstatistic")
    public Map commonStatisticQuery1(@RequestParam String areaType,
                                     @RequestParam String biz, //dvb, ...
                                     @RequestParam String bizSubtype, //common
                                     @RequestParam String period, //1,5,15,30,60,day,week,weekday,month,halfmonth,year
                                     @RequestParam String startTime,
                                     @RequestParam String endTime,
                                     @RequestParam(name = "fields", required = false) String fields, // kpi's englishName
                                     @RequestParam(name = "areaName", required = false) String areaName,
                                     @RequestParam(name = "bizCustom1", required = false) String bizCustom1,
                                     @RequestParam(name = "bizCustom2", required = false) String bizCustom2,
                                     @RequestParam(name = "bizCustom3", required = false) String bizCustom3,
                                     @RequestParam(name = "userName", required = false) String userName
    ) {
//        log.info(">>> commonStatisticQuery: area:{} business:{} type:{} period:{} startTime:{} endTime:{} fields:{}", areaType, biz, bizSubtype, period, startTime, endTime, fields, areaName);
        Map result = new HashMap();
        //图表/列表数据
        List<Map<String, Object>> data = new ArrayList();
        //统计分布数据（）
        List distributionData = new ArrayList();

        String[] kpis = fields.split(",");
        List<String> newKpis = new ArrayList();
        String tableName = "";

        String kpiStartTime = getKpiTimeES(period, startTime);
        String kpiEndTime = getKpiTimeES(period, endTime);

        String[] indies = DateUtil.convertESDate(kpiStartTime, kpiEndTime, period);
        String preType = "FLOW_";
        String subtypeName = "subtype_name";

        if(biz.equals("appstore") && bizSubtype.equals("appname")){
            preType = "T_";
            subtypeName = "appname";
            if(StringUtils.isBlank(bizCustom2)){
                result.put("chartData", data);
                result.put("distributionData", distributionData);
                return result;
            }
            if(StringUtils.isNotBlank(bizCustom2) && !"app".equals(bizCustom2)){
                bizSubtype =  bizCustom2;
            }

        }

        tableName = new StringBuffer(preType).append(areaType).append("_")
                .append(biz).append("_")
                .append(bizSubtype).append("_")
                .append(period).toString().toLowerCase();

        try {
            UserUseLog userUseLog = new UserUseLog(areaName,biz,tableName,userName,0,1,0);
            userUseLogDAO.insert(userUseLog);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        if (fields != null) {
            getAddMomAndYoy(period, fields, bizCustom1, bizCustom2, bizCustom3, newKpis);
        }

        String[] bizChannels = {};

        if (StringUtils.isNotBlank(bizCustom1)) {
            bizChannels = bizCustom1.split(",");
        }

        if((biz.equals("app") ||biz.equals("appstore")||biz.equals("education")) && bizCustom1.equals("全部")){
            bizCustom1 = "";
        }

        QueryBuilder clqb = boolQuery()
                .must(StringUtils.isNotBlank(areaName) && !"0".equals(areaName) ? termQuery("area_name", areaName) : matchAllQuery())
                .must(StringUtils.isNotBlank(bizCustom1) ? termsQuery(subtypeName, bizChannels) : matchAllQuery())
                // .must(StringUtils.isNotBlank(bizCustom2)?termQuery("biz_custom2",bizCustom2):matchAllQuery())
                // .must(StringUtils.isNotBlank(bizCustom3)?termQuery("biz_custom3",bizCustom3):matchAllQuery())
                .filter(rangeQuery("statistic_time").from(kpiStartTime).to(kpiEndTime));

        ESDataUtil.makeESDataMap(indies, data, tableName, newKpis, clqb, period);

        result.put("chartData", data);
        result.put("distributionData", distributionData);

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


    void getAddMomAndYoy(@RequestParam String period, @RequestParam(name = "fields", required = false) String
            fields, @RequestParam(name = "bizCustom1", required = false) String
                                 bizCustom1, @RequestParam(name = "bizCustom2", required = false) String
                                 bizCustom2, @RequestParam(name = "bizCustom3", required = false) String bizCustom3, List<String> newKpis) {

        for (String kpi : fields.split(",")) {
            newKpis.add(kpi);
        }

        newKpis.add("area_name");
        newKpis.add("statistic_time");
        if (bizCustom1 != null) {
            newKpis.add("bizCustom1");
        }
        if (bizCustom2 != null) {
            newKpis.add("bizCustom2");
        }
        if (bizCustom3 != null) {
            newKpis.add("bizCustom3");
        }
        newKpis.add("channel_group_label");
        newKpis.add("channel_label");
        newKpis.add("section_name");
        newKpis.add("program_name");
        newKpis.add("flag");
        newKpis.add("first_sort");
        newKpis.add("second_sort");
        newKpis.add("third_sort");
        newKpis.add("subtype_name");
        newKpis.add("business_name");
        newKpis.add("relation_business_name");
        newKpis.add("apptype1");
        newKpis.add("apptype2");
        newKpis.add("appname");

    }


}
