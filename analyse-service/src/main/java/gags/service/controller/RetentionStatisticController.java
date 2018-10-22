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
public class RetentionStatisticController {
    @Autowired
    private UserUseLogDAO userUseLogDAO;

    @RequestMapping(path = "/retentionstatistic")
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

        if (biz.equals("game_visit_rt")) {
            if (StringUtils.isBlank(bizCustom1)) {
                result.put("chartData", data);
                result.put("distributionData", distributionData);
                return result;
            }
            if ("全部".equals(bizCustom1)) {
                bizCustom1 = "";
            }
        }

        String[] kpis = fields.split(",");
        List<String> newKpis = new ArrayList();
        String tableName = "";

        String kpiStartTime = DateUtil.getKpiTimeES(period, startTime);
        String kpiEndTime = DateUtil.getKpiTimeES(period, endTime);

        String[] indies = DateUtil.convertESDate(kpiStartTime, kpiEndTime, period);

        if((biz.equals("life_product") || biz.equals("happy_fitness_group")) && bizSubtype.equals("second")){
            tableName = new StringBuffer("T_").append(areaType).append("_")
                    .append(biz).append("_")
                    .append("second").append("_")
                    .append(period).toString().toLowerCase();

        }else if(biz.equals("game_visit") || biz.equals("game_visit_rt")|| biz.equals("game_room")) {
            if(biz.equals("game_visit_rt")){
                biz = "game_visit";
            }
            tableName = new StringBuffer("DATA_").append(areaType).append("_")
                    .append(biz).append("_")
                    .append(bizSubtype).append("_")
                    .append(period).toString().toLowerCase();
        }else {
            tableName = new StringBuffer("DATA_").append(areaType).append("_")
                    .append(biz).append("_")
                    .append(bizSubtype).append("_")
                    .append(period).toString().toLowerCase();
        }

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

        if(StringUtils.isNotBlank(bizCustom1)) {
            subtypeNames = bizCustom1.split(",");
        }

        if (biz.equals("education")||biz.equals("app")) {
            if (bizSubtype.equals("section") || bizSubtype.equals("section2") || bizSubtype.equals("program") || bizSubtype.equals("app")) {
                if (StringUtils.isBlank(bizCustom1)) {
                    result.put("chartData", data);
                    result.put("distributionData", distributionData);
                    return result;
                }
            }
        }
        if((biz.equals("app")||biz.equals("education")) && "全部".equals(bizCustom1)){
            bizCustom1 = "";
        }

        QueryBuilder clqb = boolQuery()
                .must(StringUtils.isNotBlank(areaName) && !"0".equals(areaName) ? termQuery("area_name", areaName) : matchAllQuery())
                .must(StringUtils.isNotBlank(bizCustom1) ? termsQuery("subtype_name", subtypeNames) : matchAllQuery())
                // .must(StringUtils.isNotBlank(bizCustom2)?termQuery("biz_custom2",bizCustom2):matchAllQuery())
                // .must(StringUtils.isNotBlank(bizCustom3)?termQuery("biz_custom3",bizCustom3):matchAllQuery())
                .filter(rangeQuery("statistic_time").from(kpiStartTime).to(kpiEndTime));

        ESDataUtil.makeESDataMap(indies, data, tableName, newKpis, clqb, period);

        if(bizSubtype.equals("second")){
            result.put("chartData", data);
            result.put("distributionData", distributionData);
            return result;
        }

        List list = new ArrayList();
        for (Map<String, Object> obj : data) {
            Object subtypeName = obj.get("subtype_name");
            if (subtypeName != null && !"".equals(subtypeName) && !list.contains(subtypeName)) {
                list.add(subtypeName);
            }
        }

        long currentDate = DateUtil.getGlobalDate().getTime() / 1000;
        if (Long.valueOf(kpiEndTime) > currentDate) {
            kpiEndTime = currentDate + "";
        }
        long startDate = Long.valueOf(kpiStartTime);
        long endDate = Long.valueOf(kpiEndTime);
        List<Map<String, Object>> newData = new ArrayList();
        List<Map<String, Object>> dataTemp = new ArrayList();


        while (startDate <= endDate) {
            for (Object subtypeName : list) {
                String startDateStr = DateUtil.formatDate(new Date(startDate * 1000), DateUtil.DATE_TYPE_YYYY_MM_DD);
                for (Map<String, Object> obj : data) {
                    if (startDateStr.equals(obj.get("statistic_time")) && subtypeName.equals(obj.get("subtype_name"))) {
                        dataTemp.add(obj);
                    }
                }

                if (dataTemp.size() == 1) {
                    newData.add(dataTemp.get(0));
                    dataTemp.clear();
                    continue;
                }

                String bizType = "";
                switch (dataTemp.size()) {
                    case 6:
                        bizType = "retention_user_month";
                        break;
                    case 5:
                        bizType = "retention_user_15";
                        break;
                    case 4:
                        bizType = "retention_user_7";
                        break;
                    case 3:
                        bizType = "retention_user_5";
                        break;
                    case 2:
                        bizType = "retention_user_3";
                        break;
                    default:
                        bizType = "retention_user_next";
                }

                for (Map<String, Object> obj : dataTemp) {
                    if (obj.get(bizType) != null && !"".equals(obj.get(bizType))) {
                        newData.add(obj);
                        dataTemp.clear();
                        break;
                    }
                }
            }
            startDate += DateUtil.DATE_DAY_NUMBER;
        }

        result.put("chartData", newData);
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
        newKpis.add("subtype_name");
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
