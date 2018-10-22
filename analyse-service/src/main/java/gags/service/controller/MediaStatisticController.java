package gags.service.controller;

import gags.service.dao.UserUseLogDAO;
import gags.service.entity.UserUseLog;
import gags.service.util.DateUtil;
import gags.service.util.ESDataUtil;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Created by zhangtao on 2017/11/07.
 */
@RestController
public class MediaStatisticController {
    @Autowired
    private UserUseLogDAO userUseLogDAO;

    @RequestMapping(path = "/mediastatistic")
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

        if (StringUtils.isBlank(bizCustom1) || StringUtils.isBlank(bizCustom2)) {
            result.put("chartData", data);
            result.put("distributionData", distributionData);
            return result;
        }

        List<String> newKpis = new ArrayList();
        String tableName = "";

        String kpiStartTime = DateUtil.getKpiTimeES(period, startTime);
        String kpiEndTime = DateUtil.getKpiTimeES(period, endTime);

        String[] indies = DateUtil.convertESDate(kpiStartTime, kpiEndTime, period);

        tableName = new StringBuffer("T_").append(areaType).append("_")
                .append(biz).append("_")
                .append("media").append("_")
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

        String[] subtypeNames = {};
        String[] programNames = {"全部"};

        if(StringUtils.isNotBlank(bizCustom1)) {
            subtypeNames = bizCustom1.split(",");
            if(subtypeNames.length > 1){
                //programNames[0] = "全部";
            }else{
                programNames = bizCustom2.split(",");
            }
        }

        QueryBuilder clqb = boolQuery()
                .must(StringUtils.isNotBlank(areaName) && !"0".equals(areaName) ? termQuery("area_name", areaName) : matchAllQuery())
                .must(subtypeNames.length > 0 ? termsQuery("programtype", subtypeNames) : matchAllQuery())
                .must(programNames.length > 0 ? termsQuery("programtypename",programNames):matchAllQuery())
                .must(termQuery("status",bizCustom3))
                // .must(StringUtils.isNotBlank(bizCustom3)?termQuery("biz_custom3",bizCustom3):matchAllQuery())
                .filter(rangeQuery("statistic_time").from(kpiStartTime).to(kpiEndTime));

        ESDataUtil.makeESDataMap(indies, data, tableName, newKpis, clqb, period);

        result.put("chartData", data);
        result.put("distributionData", distributionData);

        return result;
    }

    @RequestMapping(path = "/advertstatistic")
    public Map commonStatisticQuery2(@RequestParam String areaType,
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

        if ((StringUtils.isBlank(bizCustom1) || StringUtils.isBlank(bizCustom2)) && !bizSubtype.equals("adBusinessRank")) {
            result.put("chartData", data);
            result.put("distributionData", distributionData);
            return result;
        }

        try {
            UserUseLog userUseLog = new UserUseLog(areaName,biz,bizSubtype,userName,0,1,0);
            userUseLogDAO.insert(userUseLog);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        List<String> newKpis = new ArrayList();
        String tableName = "";

        String kpiStartTime = DateUtil.getKpiTimeES(period, startTime);
        String kpiEndTime = DateUtil.getKpiTimeES(period, endTime);
        if(bizSubtype.equals("adBusinessRank")){
            kpiEndTime = kpiStartTime;
        }

        String[] indies = DateUtil.convertESDate(kpiStartTime, kpiEndTime, period);

        if("advert_three".equals(biz)){
            indies = new String[]{"threedata"};
            tableName = new StringBuffer("T_").append(areaType).append("_")
                    .append(biz.split("\\_")[0]).append("_")
                    .append("common").append("_")
                    .append(period).append("_three").toString().toLowerCase();
        }else {
            tableName = new StringBuffer("T_").append(areaType).append("_")
                    .append(biz).append("_")
                    .append("common").append("_")
                    .append(period).toString().toLowerCase();
        }

        if (fields != null) {
            getAddMomAndYoy(period, fields, bizCustom1, bizCustom2, bizCustom3, newKpis);
        }

        String[] subtypeNames = {};
        String[] programNames = {"全部"};

        if(StringUtils.isNotBlank(bizCustom1)) {
            subtypeNames = bizCustom1.split(",");
            if(subtypeNames.length > 1){
                programNames = new String[] {"全部"};
            }else{
                programNames = bizCustom2.split(",");
            }
        }

        if ("advert_three".equals(biz) && "全部".equals(bizCustom2) && !"全部".equals(bizCustom1)
                && subtypeNames.length<=1 && !bizCustom1.equals("开机")) {
            programNames = new String[]{};
        }

        QueryBuilder clqb = boolQuery()
                .must(StringUtils.isNotBlank(areaName) && !"0".equals(areaName) ? termQuery("area_name", areaName) : matchAllQuery())
                .must(subtypeNames.length > 0 ? termsQuery("adplacename", subtypeNames) : matchAllQuery())
                .must(programNames.length > 0 ? termsQuery("adtype",programNames):matchAllQuery())
                // .must(StringUtils.isNotBlank(bizCustom3)?termQuery("biz_custom3",bizCustom3):matchAllQuery())
                .filter(rangeQuery("statistic_time").from(kpiStartTime).to(kpiEndTime));

        ESDataUtil.makeESDataMap(indies, data, tableName, newKpis, clqb, period);

        if ("advert_three".equals(biz) && "全部".equals(bizCustom2) && !"全部".equals(bizCustom1)
                && subtypeNames.length<=1 && !bizCustom1.equals("开机")) {
            List<Map<String, Object>> newData = new ArrayList<>();
            for (Map<String, Object> obj : data) {
                if(!"全部".equals(obj.get("adtype")) && !"".equals(obj.get("adtype"))){
                    newData.add(obj);
                }
            }
            data.clear();
            data = newData;
        }

        result.put("chartData", data);
        result.put("distributionData", distributionData);

        return result;
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
        newKpis.add("programtype");
        newKpis.add("programtypename");
        newKpis.add("programstatus");
        newKpis.add("usetime");
        newKpis.add("program");
        newKpis.add("drama");
        newKpis.add("rate");
        newKpis.add("adplacename");
        newKpis.add("adtype");
        newKpis.add("deviceid");
        newKpis.add("distinct_deviceid");
        if (bizCustom1 != null) {
            newKpis.add("bizCustom1");
        }
        if (bizCustom2 != null) {
            newKpis.add("bizCustom2");
        }
        if (bizCustom3 != null) {
            newKpis.add("bizCustom3");
        }

    }


}
