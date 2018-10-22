package gags.service.controller;

import gags.service.dao.StatisticQueryDAO;
import gags.service.dao.UserUseLogDAO;
import gags.service.entity.UserUseLog;
import gags.service.util.BaseDataUtil;
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
public class StatisticController {

    @Autowired
    private StatisticQueryDAO statisticQueryDAO;
    @Autowired
    private UserUseLogDAO userUseLogDAO;
    private Random random = new Random();

    @RequestMapping(path = "/statistic2")
    public Map commonStatisticQuery(@RequestParam String areaType, //china,province,city,projet
                                    @RequestParam String biz, //dvb, ...
                                    @RequestParam String bizSubtype, //common...
                                    @RequestParam String period, //1,5,15,30,60,day,week,weekday,month,halfmonth,year
                                    @RequestParam String startTime,
                                    @RequestParam String endTime,
                                    @RequestParam(name = "fields", required = false) String fields, // kpi's englishName
                                    @RequestParam(name = "areaName", required = false) String areaName,
                                    @RequestParam(name = "bizCustom1", required = false) String bizCustom1,
                                    @RequestParam(name = "bizCustom2", required = false) String bizCustom2,
                                    @RequestParam(name = "bizCustom3", required = false) String bizCustom3) {
        Map result = new HashMap();
        //图表/列表数据
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        //统计分布数据（）
        List distributionData = new ArrayList();
        String tableName = new StringBuffer("T_").append(areaType).append("_")
                .append(biz).append("_")
                .append(bizSubtype).append("_")
                .append(period).toString().toUpperCase();
        String whereStr = "";
        //TODO 确定时间格式
        String startTimeStr = "";
        String endTimeStr = "";
        try {
            if (NumberUtils.isNumber(period)) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                startTimeStr = dateFormat.parse(startTime).getTime() / 1000 + "";
                endTimeStr = dateFormat.parse(endTime).getTime() / 1000 + "";
            } else if ("day".equals(period)) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                startTimeStr = dateFormat.parse(startTime).getTime() / 1000 + "";
                endTimeStr = dateFormat.parse(endTime).getTime() / 1000 + 86400 + "";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (!startTime.equalsIgnoreCase("null")) {
            whereStr += " statistic_time>=" + startTimeStr;
            if (!endTime.equalsIgnoreCase("null")) {
                whereStr += " and statistic_time<" + endTimeStr;
            }
        } else {
            if (!endTime.equalsIgnoreCase("null")) {
                whereStr += "statistic_time<" + endTime;
            }
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

        String fieldsStr = "*";
        if (fields != null) {
            fieldsStr = fields.toLowerCase();
            fieldsStr += ",area_name,DATE_FORMAT(FROM_UNIXTIME(statistic_time),'%Y-%c-%d %H:%i:%s') AS \"statistic_time\"";
            if (bizCustom1 != null) {
                fieldsStr += ",bizCustom1";
            }
            if (bizCustom2 != null) {
                fieldsStr += ",bizCustom2";
            }
            if (bizCustom3 != null) {
                fieldsStr += ",bizCustom3";
            }
        }
        try {
            data = statisticQueryDAO.query(tableName, whereStr, fieldsStr);
            result.put("chartData", data);
            result.put("distributionData", distributionData);
            return result;
        } catch (Exception e) {
            result.put("chartData", new ArrayList());
            result.put("distributionData", new ArrayList());
            e.printStackTrace();
        } finally {
            return result;
        }


    }

    @RequestMapping(path = "/userradio")
    public Map userRatio(@RequestParam String areaType,
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
        Map result = new HashMap();
        //图表/列表数据
        List<Map<String, Object>> data = new ArrayList();
        //统计分布数据（）
        List distributionData = new ArrayList();

        String[] kpis = fields.split(",");
        List<String> newKpis = new ArrayList();
        String tableName = "";
        String bizType = null;

        String kpiStartTime = getKpiTimeES(period, startTime);
        String kpiEndTime = getKpiTimeES(period, endTime);

        String[] indies = DateUtil.convertESDate(kpiStartTime, kpiEndTime, period);
        String subtypeName = "detail_type";

        if(biz.equals("overview")) {
            tableName = new StringBuffer("T_").append(areaType).append("_all_all_")
                    .append(period).toString().toLowerCase();
        }else if(bizSubtype.equals("common_pie")){
            tableName = new StringBuffer("T_").append(areaType).append("_all_diff_")
                    .append(period).toString().toLowerCase();
            bizType = biz;
        }else{
            tableName = new StringBuffer("T_").append(areaType).append("_all_layer_")
                    .append(period).toString().toLowerCase();
            bizType = biz;
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

        String[] bizChannels = {};

        if (StringUtils.isNotBlank(bizCustom1)) {
            bizChannels = bizCustom1.split(",");
        }

        String detailType = "";
        if(bizSubtype.equals("channel_pie"))
            detailType = "channel";
        else if(bizSubtype.equals("apk_pie"))
            detailType = "apk";
        else if(bizSubtype.equals("area_pie"))
            detailType = "area";
        else if(bizSubtype.equals("single_pie"))
            detailType = "single";
        QueryBuilder clqb = boolQuery()
                .must(StringUtils.isNotBlank(areaName) && !"0".equals(areaName) ? termQuery("area_name", areaName) : matchAllQuery())
                .must(StringUtils.isNotBlank(bizType) ? termsQuery("business", bizType) : matchAllQuery())
                .must((!bizSubtype.equals("common_pie")) ? termsQuery(subtypeName, detailType) : matchAllQuery())
                 .must(StringUtils.isNotBlank(bizCustom1)?termQuery("subtype_name1",bizCustom1):matchAllQuery())
                // .must(StringUtils.isNotBlank(bizCustom3)?termQuery("biz_custom3",bizCustom3):matchAllQuery())
                .filter(rangeQuery("statistic_time").from(kpiStartTime).to(kpiStartTime));

        ESDataUtil.makeESDataMap(indies, data, tableName, newKpis, clqb, period);

        String []distributions = new String[]{"useTimeRadio","useCountRadio","useDayRadio"};
        getDistributionData(distributions, distributionData,data);

        result.put("chartData", data);
        result.put("distributionData", distributionData);

        return result;
    }

    @RequestMapping(path = "/statistic")
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
        List<Map<String, Object>> dataDis = new ArrayList();

        String[] kpis = fields.split(",");
        List<String> newKpis = new ArrayList();
        String tableName = "";
        String whereStr = "";
        //TODO 确定时间格式
        String startTimeStr = "";
        String endTimeStr = "";
        String fieldsStr = "*";
        String statistic_time_start = "";
        String statistic_time_end = "";

        String kpiStartTime = getKpiTimeES(period, startTime);
        String kpiEndTime = getKpiTimeES(period, endTime);

        String[] indies = DateUtil.convertESDate(kpiStartTime,kpiEndTime,period);
        String param = "";
        if(areaName.equals(BaseDataUtil.ZJ_ID)){
            param = "_zj";
        }

        try {
            UserUseLog userUseLog = new UserUseLog(areaName,biz,bizSubtype,userName,0,1,0);
            userUseLogDAO.insert(userUseLog);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        out:
        switch (biz) {
            case "overview": //电视概况
//                getChartData(startTime, areaName, data, kpis);
                switch (bizSubtype) {
//                    case "userActive":
//                        distributions = new String[]{"开机天数占比"};
//                        getDistributionData(distributions, distributionData);
//                        break;
//                    case "userUse":
//                        distributions = new String[]{"使用时长分布"};
//                        getDistributionData(distributions, distributionData);
//                        break;
                    default: {
                        tableName = new StringBuffer("T_").append(areaType).append("_")
                                .append(biz).append("_").append(bizSubtype).append("_")
                                .append(period).toString().toUpperCase();
                        whereStr = getStatistic(period, startTime, endTime, whereStr, startTimeStr, endTimeStr, statistic_time_start, statistic_time_end);
                        if (areaName != null && !"0".equals(areaName)) {
                            whereStr += " and area_name=" + areaName;
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
                        if (fields != null) {
                            fieldsStr = getAddMomAndYoy(period, fields, bizCustom1, bizCustom2, bizCustom3, newKpis);
                        }
//                        data = getMySqlData(tableName, whereStr, fieldsStr);


//                        tableName = new StringBuffer("T_").append(areaType).append("_")
//                                .append(biz).append("_")
//                                .append(bizSubtype).append("_")
//                                .append(period).toString().toLowerCase();
//
//                        if (fields != null) {
//                            //拼接环比，同比
//                            fieldsStr = getAddMomAndYoy(period, fields, bizCustom1, bizCustom2, bizCustom3, newKpis);
//                        }

                        QueryBuilder qb  =  boolQuery()
                                .must(StringUtils.isNotBlank(areaName)&& !"0".equals(areaName)?termQuery("area_name",areaName):matchAllQuery())
                              //  .must(StringUtils.isNotBlank(bizCustom1)?termsQuery("channel_group_label",bizChannelGroups):matchAllQuery())
                                //.must(StringUtils.isNotBlank(bizCustom2)?termQuery("biz_custom2",bizCustom2):matchAllQuery())
                                //.must(StringUtils.isNotBlank(bizCustom3)?termQuery("biz_custom3",bizCustom3):matchAllQuery())
                                .filter(rangeQuery("statistic_time").from(kpiStartTime).to(kpiEndTime));

                        ESDataUtil.makeESDataMap(indies,data,tableName, newKpis, qb,period);

//                        String disTableName = "t_project_all_all_day";
//                        QueryBuilder qbds  =  boolQuery()
//                                .must(StringUtils.isNotBlank(areaName)&& !"0".equals(areaName)?termQuery("area_name",areaName):matchAllQuery())
//                                .filter(rangeQuery("statistic_time").from(kpiStartTime).to(kpiStartTime));
//
//                        ESDataUtil.makeESDataMap(indies,dataDis,disTableName, newKpis, qbds,period);
//
//                        getDistributionData(distributions, distributionData,dataDis);

//                          String []distributions = new String[]{"使用时长分布"};
//                          getDistributionData(distributions, distributionData,new ArrayList<>());

                        break;
                    }

                }
                break;
            case "all": //全业务
//                String[] bizs = {"ts", "replay", "ott", "vod", "education", "dvb"};//, "游戏","电商平台", "广告", "APP"};
//                String[] compareBizs = {"直播", "回看", "时移", "VOD点播", "OTT点播", "教育"};
                if("useDays".equals(bizSubtype) && "day".equals(period)){
                    result.put("chartData", data);
                    result.put("distributionData", distributionData);
                    return result;
                }

                List<String> bizList = new ArrayList();
                bizList.add("replay");
                bizList.add("vod");
                bizList.add("dvb");
                if(!bizSubtype.equals("user_three")) {
                    bizList.add("ts");
                    bizList.add("ott");
                    bizList.add("education");
                    bizList.add("happy_fitness_group");
                    bizList.add("life_product");
                    if (!bizSubtype.equals("userTimeInUse")) {
                        bizList.add("in_community");
                    }
                }
                List<String> compareBizList = new ArrayList();
                compareBizList.add("直播");
                compareBizList.add("回看");
                compareBizList.add("VOD点播");
                if (!bizSubtype.equals("user_three")) {
                    compareBizList.add("时移");
                    compareBizList.add("OTT点播");
                    compareBizList.add("教育");
                    compareBizList.add("百灵K歌");
                    compareBizList.add("幸福健身团");
                    if (!bizSubtype.equals("userTimeInUse")) {
                        compareBizList.add("智慧社区");
                    }
                }


                if (!bizSubtype.equals("userTimeInUse") && !bizSubtype.equals("user_three")) {
                    bizList.add("appstore");
                    bizList.add("game_room");
                    compareBizList.add("应用商店");
                    compareBizList.add("游戏大厅");
                }
                String[] bizs = (String[]) bizList.toArray(new String[bizList.size()]);
                String[] compareBizs = (String[]) compareBizList.toArray(new String[compareBizList.size()]);

                if(bizSubtype.equals("user_three")){
                    param = "_three_zj";
                    indies = new String[]{"threedata"};
                }

                tableName = new StringBuffer("T_").append(areaType).append("_")
                        .append(biz).append("_common_")
                        .append(period).append(param).toString().toUpperCase();

                whereStr = getStatistic(period, startTime, endTime, whereStr, startTimeStr, endTimeStr, statistic_time_start, statistic_time_end);
                if (bizCustom1 != null) {
                    whereStr += " and bizCustom1='" + bizCustom1 + "'";
                }
                if (bizCustom2 != null) {
                    whereStr += " and bizCustom2='" + bizCustom2 + "'";
                }
                if (bizCustom3 != null) {
                    whereStr += " and bizCustom3='" + bizCustom3 + "'";
                }
                if (bizs != null && bizs.length > 0) {
                    whereStr += " and ( types = '" + bizs[0] + "'";
                    for (int i = 1; i < bizs.length; i++) {
                        whereStr += " or types = '" + bizs[i] + "' ";
                    }
                    whereStr += " ) ";
                }

                if (areaName != null && !"0".equals(areaName)) {
                    whereStr += " and area_name=" + areaName + "";
                }

                if (fields != null) {
                    //拼接环比，同比
                    for (String kpi : fields.split(",")) {
                        if (!kpi.equals("user_index")) {
                            newKpis.add(kpi);
                            newKpis.add(kpi + "_mom");
                            newKpis.add(kpi + "_yoy");
                        }
                    }
                    fieldsStr = StringUtils.join(newKpis.toArray(new String[0]), ",").toLowerCase();
                    if (!NumberUtils.isNumber(period)) {
                        fieldsStr += ",area_name, DATE_FORMAT(FROM_UNIXTIME(statistic_time),'%Y-%c-%d')  AS \"statistic_time\" ,statistic_time as orderTime ";
                    } else {
                        fieldsStr += ",area_name, DATE_FORMAT(FROM_UNIXTIME(statistic_time),'%Y-%c-%d %H:%i:%s')  AS \"statistic_time\" ,statistic_time as orderTime ";
                    }
                    fieldsStr += ",types_cn as compare_type";
                    if (bizCustom1 != null) {
                        fieldsStr += ",bizCustom1";
                    }
                    if (bizCustom2 != null) {
                        fieldsStr += ",bizCustom2";
                    }
                    if (bizCustom3 != null) {
                        fieldsStr += ",bizCustom3";
                    }
                }
//                data = getMySqlData(tableName, whereStr, fieldsStr);


                if (fields != null) {
                    //拼接环比，同比
                    fieldsStr = getAddMomAndYoy(period, fields, bizCustom1, bizCustom2, bizCustom3, newKpis);
                }

                QueryBuilder qball  =  boolQuery()
                        .must(StringUtils.isNotBlank(areaName)&& !"0".equals(areaName)?termQuery("area_name",areaName):matchAllQuery())
                        .must(termsQuery("types",bizs))
                        //.must(StringUtils.isNotBlank(bizCustom2)?termQuery("biz_custom2",bizCustom2):matchAllQuery())
                        //.must(StringUtils.isNotBlank(bizCustom3)?termQuery("biz_custom3",bizCustom3):matchAllQuery())
                        .filter(rangeQuery("statistic_time").from(kpiStartTime).to(kpiEndTime));

                ESDataUtil.makeESDataMap(indies,data,tableName, newKpis, qball,period);



                List<Map> statisticTimeList = new ArrayList<>();//同一时间不同业务的数据
                boolean noCompareType = true;//同一时间业务为空
                Map<String, Object> noCompareTypeMap = new HashMap();//添加业务为空的空数据
                List<Map<String, Object>> newData = new ArrayList(); //构建新的数据
                if (data != null && data.size() > 0) {
                    for (int y = 0; y < data.size(); y++) {
                        Map dataMap2 = data.get(y);
                        statisticTimeList.clear();
                        for (int j = 0; j < data.size(); j++) {
                            Map dataMap1 = data.get(j);
                            if (dataMap2.get("statistic_time") != null && dataMap2.get("statistic_time").equals(dataMap1.get("statistic_time"))) {
                                statisticTimeList.add(dataMap1);
                            }
                            if (statisticTimeList.size() == compareBizs.length) break;
                        }
                        for (int m = 1; m < statisticTimeList.size(); m++) {
                            data.remove(statisticTimeList.get(m));  //remove 对应的数据
                        }
                        for (String compareBiz : compareBizs) {
                            for (Map statisticTimeMap : statisticTimeList) {
                                if (compareBiz.equals(statisticTimeMap.get("compare_type"))) {
                                    noCompareType = false;
                                    newData.add(statisticTimeMap);
                                }
                                if (compareBiz.equals(statisticTimeMap.get("types_cn"))) {
                                    noCompareType = false;
                                    statisticTimeMap.put("compare_type",compareBiz);
                                    newData.add(statisticTimeMap);
                                }
                            }
                            if (noCompareType) {
                                noCompareTypeMap = new HashMap();
                                for (String field : newKpis) {
                                    noCompareTypeMap.put(field, "0");
                                }
                                noCompareTypeMap.put("compare_type", compareBiz);
                                noCompareTypeMap.put("statistic_time", dataMap2.get("statistic_time"));
                                noCompareTypeMap.put("area_name", dataMap2.get("area_name"));
                                newData.add(noCompareTypeMap);
                            }
                            noCompareType = true;
                        }
                    }
                }
                data = newData;//赋上补充0的结果
                break;

            case "game_visit": //游戏
                tableName = new StringBuffer("T_").append(areaType).append("_")
                        .append(biz).append("_").append(bizSubtype).append("_")
                        .append(period).toString().toUpperCase();

                String[] bizCustom1s = {};

                if(StringUtils.isNotBlank(bizCustom1)) {
                    bizCustom1s = bizCustom1.split(",");
                }

                if (fields != null) {
                    fieldsStr = getAddMomAndYoy(period, fields, bizCustom1, bizCustom2, bizCustom3, newKpis);
                }
                switch (bizSubtype) {
                    case "type1":
                        if(StringUtils.isBlank(bizCustom1)) {
                            break out;
                        }
                        if("全部".equals(bizCustom1)){
                            bizCustom1 = "";
                        }
                        QueryBuilder qb  =  boolQuery()
                                .must(StringUtils.isNotBlank(areaName)&& !"0".equals(areaName)?termQuery("area_name",areaName):matchAllQuery())
                                  .must(StringUtils.isNotBlank(bizCustom1)?termsQuery("game_visit_type_name",bizCustom1s):matchAllQuery())
                                //.must(StringUtils.isNotBlank(bizCustom2)?termQuery("biz_custom2",bizCustom2):matchAllQuery())
                                //.must(StringUtils.isNotBlank(bizCustom3)?termQuery("biz_custom3",bizCustom3):matchAllQuery())
                                .filter(rangeQuery("statistic_time").from(kpiStartTime).to(kpiEndTime));

                        ESDataUtil.makeESDataMap(indies, data, tableName, newKpis, qb, period);
                        break out;
                    case "type2":
                        if(StringUtils.isBlank(bizCustom1)) {
                            break out;
                        }
                        if("全部".equals(bizCustom1)){
                            bizCustom1 = "";
                        }
                        QueryBuilder qb2  =  boolQuery()
                                .must(StringUtils.isNotBlank(areaName)&& !"0".equals(areaName)?termQuery("area_name",areaName):matchAllQuery())
                                .must(StringUtils.isNotBlank(bizCustom1)?termsQuery("game_visit_type_name",bizCustom1s):matchAllQuery())
                                //.must(StringUtils.isNotBlank(bizCustom2)?termQuery("biz_custom2",bizCustom2):matchAllQuery())
                                //.must(StringUtils.isNotBlank(bizCustom3)?termQuery("biz_custom3",bizCustom3):matchAllQuery())
                                .filter(rangeQuery("statistic_time").from(kpiStartTime).to(kpiEndTime));

                        ESDataUtil.makeESDataMap(indies, data, tableName, newKpis, qb2, period);
                        break out;
                    case "single_game":
                        if(StringUtils.isBlank(bizCustom1)) {
                            break out;
                        }
                        if("全部".equals(bizCustom1)){
                            bizCustom1 = "";
                        }
                        QueryBuilder qb3  =  boolQuery()
                                .must(StringUtils.isNotBlank(areaName)&& !"0".equals(areaName)?termQuery("area_name",areaName):matchAllQuery())
                                .must(StringUtils.isNotBlank(bizCustom1)?termsQuery("game_visit_single_game_name",bizCustom1s):matchAllQuery())
                                //.must(StringUtils.isNotBlank(bizCustom2)?termQuery("biz_custom2",bizCustom2):matchAllQuery())
                                //.must(StringUtils.isNotBlank(bizCustom3)?termQuery("biz_custom3",bizCustom3):matchAllQuery())
                                .filter(rangeQuery("statistic_time").from(kpiStartTime).to(kpiEndTime));
                        ESDataUtil.makeESDataMap(indies, data, tableName, newKpis, qb3, period);
                        break out;
                }

            case "game_room": //游戏
                tableName = new StringBuffer("T_").append(areaType).append("_")
                        .append(biz).append("_").append(bizSubtype).append("_")
                        .append(period).toString().toUpperCase();

                String[] subtypeNames = {};

                if(StringUtils.isNotBlank(bizCustom1)) {
                    subtypeNames = bizCustom1.split(",");
                }

                if (fields != null) {
                    fieldsStr = getAddMomAndYoy(period, fields, bizCustom1, bizCustom2, bizCustom3, newKpis);
                }
                switch (bizSubtype) {
                    case "use_common":
                        QueryBuilder qb  =  boolQuery()
                                .must(StringUtils.isNotBlank(areaName)&& !"0".equals(areaName)?termQuery("area_name",areaName):matchAllQuery())
//                                .must(StringUtils.isNotBlank(bizCustom1)?termsQuery("game_visit_type_name",bizCustom1s):matchAllQuery())
                                //.must(StringUtils.isNotBlank(bizCustom2)?termQuery("biz_custom2",bizCustom2):matchAllQuery())
                                //.must(StringUtils.isNotBlank(bizCustom3)?termQuery("biz_custom3",bizCustom3):matchAllQuery())
                                .filter(rangeQuery("statistic_time").from(kpiStartTime).to(kpiEndTime));
                        ESDataUtil.makeESDataMap(indies,data,tableName, newKpis, qb,period);
                        break out;
                    case "use_type1":
                        if(StringUtils.isBlank(bizCustom1)) {
                            break out;
                        }
                        if("全部".equals(bizCustom1)){
                            bizCustom1 = "";
                        }
                        QueryBuilder qb2  =  boolQuery()
                                .must(StringUtils.isNotBlank(areaName)&& !"0".equals(areaName)?termQuery("area_name",areaName):matchAllQuery())
                                .must(StringUtils.isNotBlank(bizCustom1)?termsQuery("subtype_name",subtypeNames):matchAllQuery())
                                //.must(StringUtils.isNotBlank(bizCustom2)?termQuery("biz_custom2",bizCustom2):matchAllQuery())
                                //.must(StringUtils.isNotBlank(bizCustom3)?termQuery("biz_custom3",bizCustom3):matchAllQuery())
                                .filter(rangeQuery("statistic_time").from(kpiStartTime).to(kpiEndTime));

                        ESDataUtil.makeESDataMap(indies, data, tableName, newKpis, qb2, period);
                        break out;
                    case "use_type2":
                        if(StringUtils.isBlank(bizCustom1)) {
                            break out;
                        }
                        if("全部".equals(bizCustom1)){
                            bizCustom1 = "";
                        }
                        QueryBuilder qb3  =  boolQuery()
                                .must(StringUtils.isNotBlank(areaName)&& !"0".equals(areaName)?termQuery("area_name",areaName):matchAllQuery())
                                .must(StringUtils.isNotBlank(bizCustom1)?termsQuery("subtype_name",subtypeNames):matchAllQuery())
                                //.must(StringUtils.isNotBlank(bizCustom2)?termQuery("biz_custom2",bizCustom2):matchAllQuery())
                                //.must(StringUtils.isNotBlank(bizCustom3)?termQuery("biz_custom3",bizCustom3):matchAllQuery())
                                .filter(rangeQuery("statistic_time").from(kpiStartTime).to(kpiEndTime));

                        ESDataUtil.makeESDataMap(indies, data, tableName, newKpis, qb3, period);
                        break out;
                    case "use_single_game":
                        if(StringUtils.isBlank(bizCustom1)) {
                            break out;
                        }
                        if("全部".equals(bizCustom1)){
                            bizCustom1 = "";
                        }
                        QueryBuilder qb4  =  boolQuery()
                                .must(StringUtils.isNotBlank(areaName)&& !"0".equals(areaName)?termQuery("area_name",areaName):matchAllQuery())
                                .must(StringUtils.isNotBlank(bizCustom1)?termsQuery("subtype_name",subtypeNames):matchAllQuery())
                                //.must(StringUtils.isNotBlank(bizCustom2)?termQuery("biz_custom2",bizCustom2):matchAllQuery())
                                //.must(StringUtils.isNotBlank(bizCustom3)?termQuery("biz_custom3",bizCustom3):matchAllQuery())
                                .filter(rangeQuery("statistic_time").from(kpiStartTime).to(kpiEndTime));
                        ESDataUtil.makeESDataMap(indies, data, tableName, newKpis, qb4, period);
                        break out;
                }
            case "vod_three": //游戏
                switch (bizSubtype) {
                    case "section":
                        biz = "vod";
                        tableName = new StringBuffer("T_").append(areaType).append("_")
                                .append(biz).append("_").append(bizSubtype).append("_")
                                .append(period).append("_three_zj").toString().toUpperCase();

                        String[] vodSubtypeNames = {};

                        if (StringUtils.isNotBlank(bizCustom1)) {
                            vodSubtypeNames = bizCustom1.split(",");
                        }

                        if (fields != null) {
                            fieldsStr = getAddMomAndYoy(period, fields, bizCustom1, bizCustom2, bizCustom3, newKpis);
                        }
                        QueryBuilder qb2 = boolQuery()
                                .must(StringUtils.isNotBlank(areaName) && !"0".equals(areaName) ? termQuery("area_name", areaName) : matchAllQuery())
                                .must(StringUtils.isNotBlank(bizCustom1) ? termsQuery("subtype_name", vodSubtypeNames) : matchAllQuery())
                                //.must(StringUtils.isNotBlank(bizCustom2)?termQuery("biz_custom2",bizCustom2):matchAllQuery())
                                //.must(StringUtils.isNotBlank(bizCustom3)?termQuery("biz_custom3",bizCustom3):matchAllQuery())
                                .filter(rangeQuery("statistic_time").from(kpiStartTime).to(kpiEndTime));

                        if (StringUtils.isNotBlank(bizCustom1)) {
                            ESDataUtil.makeESDataMap(new String[]{"threedata"}, data, tableName, newKpis, qb2, period);
                        }
                        break out;
                    default:
                }
            case "ecom": //电商平台
                switch (bizSubtype) {
                    case "scan":
                        tableName = new StringBuffer("T_").append(areaType).append("_")
                                .append(biz).append("_").append(bizSubtype).append("_")
                                .append(period).toString().toLowerCase();

                        String[] shopNames = {};
                        String[] typeNames = {};
                        String[] goodsNames = {};

                        if (StringUtils.isNotBlank(bizCustom1) && !"全部".equals(bizCustom1)) {
                            shopNames = bizCustom1.split(",");
                        }
                        if (StringUtils.isNotBlank(bizCustom2) && !"全部".equals(bizCustom2)) {
                            typeNames = bizCustom2.split(",");
                        }
                        if (StringUtils.isNotBlank(bizCustom3) && !"全部".equals(bizCustom3)) {
                            goodsNames = bizCustom3.split(",");
                        }

                        if (fields != null) {
                            fieldsStr = getAddMomAndYoy(period, fields, bizCustom1, bizCustom2, bizCustom3, newKpis);
                        }
                        QueryBuilder qb = boolQuery()
                                .must(StringUtils.isNotBlank(areaName) && !"0".equals(areaName) ? termQuery("area_name", areaName) : matchAllQuery())
                                .must(shopNames.length > 0 ? termsQuery("shop_name", shopNames) : matchAllQuery())
                                .must(typeNames.length > 0 ? termsQuery("type_name", typeNames) : matchAllQuery())
                                .must(goodsNames.length > 0 ? termsQuery("goods_name", goodsNames) : matchAllQuery())
                                .filter(rangeQuery("statistic_time").from(kpiStartTime).to(kpiEndTime));

                        if (StringUtils.isNotBlank(bizCustom1)) {
                            ESDataUtil.makeESDataMap(indies, data, tableName, newKpis, qb, period);
                        }
                        break out;
                }

            default: //直播
                switch (bizSubtype) {
                    case "channel_group":
                     /*   String[] groups = {"中央台", "福建台", "福州台", "地方卫视", "上海文广","中数传媒","aaa"};
                        getChartDataMulti(startTime, areaName, data, kpis, groups);*/
                        tableName = new StringBuffer("T_").append(areaType).append("_")
                                .append(biz).append("_")
                                .append(bizSubtype).append("_")
                                .append(period).toString().toLowerCase();
                        /**
                        tableName += " LEFT JOIN t_config_channel_group t1  on channel_group_id = t1.id ";
                        whereStr = getStatistic(period, startTime, endTime, whereStr, startTimeStr, endTimeStr, statistic_time_start, statistic_time_end);
                        whereStr = getWereStr(areaName, bizCustom1, bizCustom2, bizCustom3, whereStr);
                         **/

                        if (fields != null) {
                            //拼接环比，同比
                            fieldsStr = getAddMomAndYoy(period, fields, bizCustom1, bizCustom2, bizCustom3, newKpis);
                        }

                        String[] bizChannelGroups = {};

                        if(StringUtils.isNotBlank(bizCustom1)) {
                            bizChannelGroups = bizCustom1.split(",");
                        }

                        QueryBuilder qb  =  boolQuery()
                                .must(StringUtils.isNotBlank(areaName)&& !"0".equals(areaName)?termQuery("area_name",areaName):matchAllQuery())
                                .must(StringUtils.isNotBlank(bizCustom1)?termsQuery("channel_group_label",bizChannelGroups):matchAllQuery())
                                //.must(StringUtils.isNotBlank(bizCustom2)?termQuery("biz_custom2",bizCustom2):matchAllQuery())
                                //.must(StringUtils.isNotBlank(bizCustom3)?termQuery("biz_custom3",bizCustom3):matchAllQuery())
                                .filter(rangeQuery("statistic_time").from(kpiStartTime).to(kpiEndTime));

                        ESDataUtil.makeESDataMap(indies,data,tableName, newKpis, qb,period);

                       // fieldsStr += ",label as compare_type";
                        //data = getMySqlData(tableName, whereStr, fieldsStr);
                        break;
                    case "channel":
                        tableName = new StringBuffer("T_").append(areaType).append("_")
                                .append(biz).append("_")
                                .append(bizSubtype).append("_")
                                .append(period).toString().toLowerCase();
                        /**
                        tableName += " LEFT JOIN t_config_channel t1  on channel_id = t1.id ";
                        whereStr = getStatistic(period, startTime, endTime, whereStr, startTimeStr, endTimeStr, statistic_time_start, statistic_time_end);
                        if (areaName != null && !"0".equals(areaName)) {
                            whereStr += " and area_name='" + areaName + "'";
                        }
                        if (bizCustom1 != null) {
                            whereStr += " and channel_id in (" + bizCustom1 + ")";
                        }
                         **/
                        if (fields != null) {
                            /**
                            //拼接环比，同比
                            for (String kpi : fields.split(",")) {
                                newKpis.add(kpi);
                                newKpis.add(kpi + "_mom");
                                newKpis.add(kpi + "_yoy");
                            }
                            fieldsStr = StringUtils.join(newKpis.toArray(new String[0]), ",").toLowerCase();
                            if (!NumberUtils.isNumber(period)) {
                                fieldsStr += ",area_name, DATE_FORMAT(FROM_UNIXTIME(statistic_time),'%Y-%c-%d')  AS \"statistic_time\" ,statistic_time as orderTime ";
                            } else {
                                fieldsStr += ",area_name, DATE_FORMAT(FROM_UNIXTIME(statistic_time),'%Y-%c-%d %H:%i:%s')  AS \"statistic_time\" ,statistic_time as orderTime ";
                            }
                            fieldsStr += ",label as compare_type";
                             **/
                            getAddMomAndYoy(period, fields, bizCustom1, bizCustom2, bizCustom3, newKpis);
                        }

                        String[] bizChannels = {};

                        if(StringUtils.isNotBlank(bizCustom1)) {
                            bizChannels = bizCustom1.split(",");
                        }else{
                            break;
                        }


                        QueryBuilder clqb  =  boolQuery()
                                .must(StringUtils.isNotBlank(areaName)&& !"0".equals(areaName)?termQuery("area_name",areaName):matchAllQuery())
                                .must(StringUtils.isNotBlank(bizCustom1)?termsQuery("channel_label",bizChannels):matchAllQuery())
                               // .must(StringUtils.isNotBlank(bizCustom2)?termQuery("biz_custom2",bizCustom2):matchAllQuery())
                               // .must(StringUtils.isNotBlank(bizCustom3)?termQuery("biz_custom3",bizCustom3):matchAllQuery())
                                .filter(rangeQuery("statistic_time").from(kpiStartTime).to(kpiEndTime));

                        ESDataUtil.makeESDataMap(indies,data,tableName, newKpis, clqb,period);

                        // data = getMySqlData(tableName, whereStr, fieldsStr);
                        break;
                    case "channel_three":
                        indies = new String[]{"threedata"};
                        tableName = new StringBuffer("T_").append(areaType).append("_")
                                .append(biz).append("_")
                                .append(bizSubtype.split("\\_")[0]).append("_")
                                .append(period).append("_three").append(param).toString().toLowerCase();
                        if (fields != null) {
                            getAddMomAndYoy(period, fields, bizCustom1, bizCustom2, bizCustom3, newKpis);
                        }

                        String[] bizThreeChannels = {};

                        if(StringUtils.isNotBlank(bizCustom1)) {
                            bizThreeChannels = bizCustom1.split(",");
                        }else{
                            break;
                        }


                        QueryBuilder threeclqb  =  boolQuery()
                                .must(StringUtils.isNotBlank(areaName)&& !"0".equals(areaName)?termQuery("area_name",areaName):matchAllQuery())
                                .must(StringUtils.isNotBlank(bizCustom1)?termsQuery("channel_label",bizThreeChannels):matchAllQuery())
                                // .must(StringUtils.isNotBlank(bizCustom2)?termQuery("biz_custom2",bizCustom2):matchAllQuery())
                                // .must(StringUtils.isNotBlank(bizCustom3)?termQuery("biz_custom3",bizCustom3):matchAllQuery())
                                .filter(rangeQuery("statistic_time").from(kpiStartTime).to(kpiEndTime));

                        ESDataUtil.makeESDataMap(indies,data,tableName, newKpis, threeclqb,period);

                        break;
                    case "dvbProgram":
                        tableName = new StringBuffer("T_").append(areaType).append("_")
                                .append(biz).append("_")
                                .append("program").append("_")
                                .append(period).toString().toLowerCase();
                        if (fields != null) {
                            getAddMomAndYoy(period, fields, bizCustom1, bizCustom2, bizCustom3, newKpis);
                        }

                        String[] dvbbizs = {};
                        String[] dvbChannel = {};
                        String[] dvbProgram = {};

                        if(StringUtils.isNotBlank(bizCustom1)) {
                            dvbbizs = bizCustom1.split("dvbProgram");
                            if(dvbbizs.length < 2){
                                break;
                            }
                            if(StringUtils.isNotBlank(dvbbizs[0])){
                                dvbChannel = dvbbizs[0].split(",");
                            }
                            if(StringUtils.isNotBlank(dvbbizs[1])){
                                dvbProgram = dvbbizs[1].split(",");
                            }
                        }else{
                            break;
                        }

                        QueryBuilder dvbclqb  =  boolQuery()
                                .must(StringUtils.isNotBlank(areaName)&& !"0".equals(areaName)?termQuery("area_name",areaName):matchAllQuery())
                                .must(dvbChannel.length>0?termsQuery("channel_label",dvbChannel):matchAllQuery())
                                .must(dvbProgram.length>0?termsQuery("program_label",dvbProgram):matchAllQuery())
                                // .must(StringUtils.isNotBlank(bizCustom2)?termQuery("biz_custom2",bizCustom2):matchAllQuery())
                                // .must(StringUtils.isNotBlank(bizCustom3)?termQuery("biz_custom3",bizCustom3):matchAllQuery())
                                .filter(rangeQuery("statistic_time").from(kpiStartTime).to(kpiStartTime));

                        ESDataUtil.makeESDataMap(indies,data,tableName, newKpis, dvbclqb,period);

                        break;
                    case "dvbProgram_three":
                        indies = new String[]{"threedata"};
                        tableName = new StringBuffer("T_").append(areaType).append("_")
                                .append(biz).append("_")
                                .append("program").append("_")
                                .append(period).append("_three").append(param).toString().toLowerCase();
                        if (fields != null) {
                            getAddMomAndYoy(period, fields, bizCustom1, bizCustom2, bizCustom3, newKpis);
                        }
                        String kpiEndTimeStr = kpiStartTime;
                        if(period.equals("60")){
                            kpiEndTimeStr = kpiEndTime;
                        }

                        String[] threedvbbizs = {};
                        String[] threedvbChannel = {};
                        String[] threedvbProgram = {};

                        if(StringUtils.isNotBlank(bizCustom1)) {
                            threedvbbizs = bizCustom1.split("dvbProgram");
                            if(threedvbbizs.length < 2){
                                break;
                            }
                            if(StringUtils.isNotBlank(threedvbbizs[0])){
                                threedvbChannel = threedvbbizs[0].split(",");
                            }
                            if(StringUtils.isNotBlank(threedvbbizs[1])){
                                threedvbProgram = threedvbbizs[1].split(",");
                            }
                        }else{
                            break;
                        }

                        QueryBuilder threedvbclqb  =  boolQuery()
                                .must(StringUtils.isNotBlank(areaName)&& !"0".equals(areaName)?termQuery("area_name",areaName):matchAllQuery())
                                .must(threedvbChannel.length>0?termsQuery("channel_label",threedvbChannel):matchAllQuery())
                                .must(threedvbProgram.length>0?termsQuery("program_label",threedvbProgram):matchAllQuery())
                                // .must(StringUtils.isNotBlank(bizCustom2)?termQuery("biz_custom2",bizCustom2):matchAllQuery())
                                // .must(StringUtils.isNotBlank(bizCustom3)?termQuery("biz_custom3",bizCustom3):matchAllQuery())
                                .filter(rangeQuery("statistic_time").from(kpiStartTime).to(kpiEndTimeStr));

                        ESDataUtil.makeESDataMap(indies,data,tableName, newKpis, threedvbclqb,period);

                        break;
                    case "replayProgram_three":
                        indies = new String[]{"threedata"};
                        tableName = new StringBuffer("T_").append(areaType).append("_")
                                .append(biz).append("_")
                                .append("program").append("_")
                                .append(period).append("_three").append("_zj").toString().toLowerCase();
                        if (fields != null) {
                            getAddMomAndYoy(period, fields, bizCustom1, bizCustom2, bizCustom3, newKpis);
                        }

                        String[] threereplaybizs = {};
                        String[] threereplayChannel = {};
                        String[] threereplayProgram = {};

                        if(StringUtils.isNotBlank(bizCustom1)) {
                            threereplaybizs = bizCustom1.split("dvbProgram");
                            if(threereplaybizs.length < 2){
                                break;
                            }
                            if(StringUtils.isNotBlank(threereplaybizs[0])){
                                threereplayChannel = threereplaybizs[0].split(",");
                            }
                            if(StringUtils.isNotBlank(threereplaybizs[1])){
                                threereplayProgram = threereplaybizs[1].split(",");
                            }
                        }else{
                            break;
                        }

                        QueryBuilder threereplayclqb  =  boolQuery()
                                .must(StringUtils.isNotBlank(areaName)&& !"0".equals(areaName)?termQuery("area_name",areaName):matchAllQuery())
                                .must(threereplayChannel.length>0?termsQuery("channel_label",threereplayChannel):matchAllQuery())
                                .must(threereplayProgram.length>0?termsQuery("program_label",threereplayProgram):matchAllQuery())
                                // .must(StringUtils.isNotBlank(bizCustom2)?termQuery("biz_custom2",bizCustom2):matchAllQuery())
                                // .must(StringUtils.isNotBlank(bizCustom3)?termQuery("biz_custom3",bizCustom3):matchAllQuery())
                                .filter(rangeQuery("statistic_time").from(kpiStartTime).to(kpiEndTime));

                        ESDataUtil.makeESDataMap(indies,data,tableName, newKpis, threereplayclqb,period);

                        break;
                    default:
//                        Map result = new HashMap();
                        //图表/列表数据
//                        List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
                        //统计分布数据（）
//                        List distributionData = new ArrayList();
                        if("programRank".equals(bizSubtype)){
                            bizSubtype ="program";
                        }

                        if("overview_three".equals(biz) || "vod_three".equals(biz) ||
                                "dvb_three".equals(biz) || "replay_three".equals(biz)){
                            indies = new String[]{"threedata"};

                            tableName = new StringBuffer("T_").append(areaType).append("_")
                                    .append(biz.split("\\_")[0]).append("_").append(bizSubtype).append("_")
                                    .append(period).append("_three").append(param).toString().toUpperCase();
                        }else if("program_three".equals(bizSubtype) || "series_three".equals(bizSubtype)){
                            indies = new String[]{"threedata"};

                            tableName = new StringBuffer("T_").append(areaType).append("_")
                                    .append(biz).append("_").append(bizSubtype.split("\\_")[0]).append("_")
                                    .append(period).append("_three").append(param).toString().toUpperCase();
                        }else {

                            tableName = new StringBuffer("T_").append(areaType).append("_")
                                    .append(biz).append("_")
                                    .append(bizSubtype).append("_")
                                    .append(period).toString().toUpperCase();
                        }

                        if ("section".equalsIgnoreCase(bizSubtype)
                                || "program".equalsIgnoreCase(bizSubtype)) {

                            String bizCustom = "section_name";
                            if("program".equalsIgnoreCase(bizSubtype)){
                                bizCustom = "program_name";
                            }
                            String[] bizCustoms = {};

                            if (StringUtils.isNotBlank(bizCustom1)) {
                                bizCustoms = bizCustom1.split(",");
                            } else if (!"common".equalsIgnoreCase(bizSubtype)) {
                                break;
                            }

                            if((bizSubtype.equals("section") && biz.equals("education")) && bizCustom1.equals("全部")){
                                bizCustom1 = "";
                            }

                            getAddMomAndYoy(period, fields, bizCustom1, bizCustom2, bizCustom3, newKpis);

                            QueryBuilder dfqb  =  boolQuery()
                                    .must(StringUtils.isNotBlank(areaName)&& !"0".equals(areaName)?termQuery("area_name",areaName):matchAllQuery())
                                    .must(StringUtils.isNotBlank(bizCustom1)?termsQuery(bizCustom,bizCustoms):matchAllQuery())
                                   // .must(StringUtils.isNotBlank(bizCustom2)?termQuery("biz_custom2",bizCustom2):matchAllQuery())
                                   // .must(StringUtils.isNotBlank(bizCustom3)?termQuery("biz_custom3",bizCustom3):matchAllQuery())
                                    .filter(rangeQuery("statistic_time").from(kpiStartTime).to(kpiEndTime));

                            ESDataUtil.makeESDataMap(indies,data,tableName.toLowerCase(), newKpis, dfqb,period);

                        }else {

                            if(biz.equals("app")){
                                if(bizSubtype.equals("app") || bizSubtype.equals("type1") || bizSubtype.equals("type2")) {
                                    if (StringUtils.isBlank(bizCustom1)) {
                                        result.put("chartData", data);
                                        result.put("distributionData", distributionData);
                                        return result;
                                    }else if(bizCustom1.equals("全部")){
                                        bizCustom1 = "";
                                    }
                                }
                            }

                            whereStr = getStatistic(period, startTime, endTime, whereStr, startTimeStr, endTimeStr, statistic_time_start, statistic_time_end);
                            if (bizCustom1 != null) {
                                String[] temps = bizCustom1.split(",");
                                whereStr += " and " + tableName + "." + bizSubtype + "_id in (";
                                for (int i = 0; i < temps.length; i++) {
                                    whereStr += i != temps.length - 1 ? "'" + temps[i].hashCode() + "'," : "'" + +temps[i].hashCode() + "')";
                                }
                            }
                            if ("section".equalsIgnoreCase(bizSubtype) || "program".equalsIgnoreCase(bizSubtype)) {
                                String sectionOrProgramTableName = "T_CONFIG_" + biz + "_" + bizSubtype;
                                whereStr += " and " + tableName + "." + bizSubtype + "_id=" + sectionOrProgramTableName + "." + bizSubtype + "_id";
                                tableName += "," + sectionOrProgramTableName;
                            }
                            if (areaName != null && !"0".equals(areaName)) {
                                whereStr += " and area_name='" + areaName + "'";
                            }

                            fieldsStr = "*";
                            if (fields != null) {
                                //拼接环比，同比
                                for (String kpi : fields.split(",")) {
                                    if(!(kpi.equals("new_visit_user") || kpi.equals("accumulated_visit_user")  ||
                                            kpi.equals("visit_user") || kpi.equals("market_share") ||
                                            kpi.equals("user_index") || kpi.equals("visit_rate"))) {
                                        newKpis.add(kpi);
                                        newKpis.add(kpi + "_mom");
                                        newKpis.add(kpi + "_yoy");
                                    }
                                }
                                fieldsStr = StringUtils.join(newKpis.toArray(new String[0]), ",").toLowerCase();
                                if (!NumberUtils.isNumber(period)) {
                                    fieldsStr += ",area_name, DATE_FORMAT(FROM_UNIXTIME(statistic_time),'%Y-%c-%d')  AS \"statistic_time\" ,statistic_time as orderTime ";
                                } else {
                                    fieldsStr += ",area_name, DATE_FORMAT(FROM_UNIXTIME(statistic_time),'%Y-%c-%d %H:%i:%s')  AS \"statistic_time\" ,statistic_time as orderTime ";
                                }
                                if ("section".equalsIgnoreCase(bizSubtype) || "program".equalsIgnoreCase(bizSubtype)) {
                                    fieldsStr += ",label";
                                }

                            }

                            if(!biz.equals("app")) {
//                                data = getMySqlData(tableName, whereStr, fieldsStr);
                            }

                            String bizCustom = "section_name";
                            if("program".equalsIgnoreCase(bizSubtype)){
                                bizCustom = "program_name";
                            }else if("program_three".equalsIgnoreCase(bizSubtype)
                                    || "series_three".equalsIgnoreCase(bizSubtype)){
                                bizCustom = "subtype_name";
                                if(StringUtils.isBlank(bizCustom1)){
                                    break out;
                                }
                            }
                            String[] bizCustoms = {};

                            if (StringUtils.isNotBlank(bizCustom1)) {
                                bizCustoms = bizCustom1.split(",");
                            }

                            getAddMomAndYoy(period, fields, bizCustom1, bizCustom2, bizCustom3, newKpis);

                            QueryBuilder dfqb  =  boolQuery()
                                    .must(StringUtils.isNotBlank(areaName)&& !"0".equals(areaName)?termQuery("area_name",areaName):matchAllQuery())
                                    .must(StringUtils.isNotBlank(bizCustom1)?termsQuery(bizCustom,bizCustoms):matchAllQuery())
                                    // .must(StringUtils.isNotBlank(bizCustom2)?termQuery("biz_custom2",bizCustom2):matchAllQuery())
                                    // .must(StringUtils.isNotBlank(bizCustom3)?termQuery("biz_custom3",bizCustom3):matchAllQuery())
                                    .filter(rangeQuery("statistic_time").from(kpiStartTime).to(kpiEndTime));

                            try {
                                ESDataUtil.makeESDataMap(indies,data,tableName.toLowerCase(), newKpis, dfqb,period);
                            } finally {

                            }
                        }
                        break;
                }
                break;
//            default:
//                getChartData(startTime, areaName, data, kpis);
//                break;
        }
//        getChartData(startTime, areaName, data, kpis);
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

    List<Map<String, Object>> getMySqlData(String tableName, String whereStr, String fieldsStr) {
        List<Map<String, Object>> data;
        try {
            data = statisticQueryDAO.query(tableName, whereStr, fieldsStr);
        } catch (Exception e) {
            data = new ArrayList();
        }
        return data;
    }

    String getWereStr(@RequestParam(name = "areaName", required = false) String
                              areaName, @RequestParam(name = "bizCustom1", required = false) String
                              bizCustom1, @RequestParam(name = "bizCustom2", required = false) String
                              bizCustom2, @RequestParam(name = "bizCustom3", required = false) String bizCustom3, String whereStr) {
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
        return whereStr;
    }

    String getAddMomAndYoy(@RequestParam String period, @RequestParam(name = "fields", required = false) String
            fields, @RequestParam(name = "bizCustom1", required = false) String
                                   bizCustom1, @RequestParam(name = "bizCustom2", required = false) String
                                   bizCustom2, @RequestParam(name = "bizCustom3", required = false) String bizCustom3, List<String> newKpis) {
        newKpis.clear();
        String fieldsStr;//拼接环比，同比
        for (String kpi : fields.split(",")) {
            if (!kpi.equals("loss_user")) {
                newKpis.add(kpi);
                if (!NumberUtils.isNumber(period)) {
                    if (!"day".equals(period)) {
                        newKpis.add(kpi + "_mom");
                    }
                    if ("month".equals(period) || "quarter".equals(period) || "year".equals(period)) {
                        newKpis.add(kpi + "_yoy");
                    }
                }
            }
        }
        fieldsStr = StringUtils.join(newKpis.toArray(new String[0]), ",").toLowerCase();
        if (!NumberUtils.isNumber(period)) {
            fieldsStr += ",area_name, DATE_FORMAT(FROM_UNIXTIME(statistic_time),'%Y-%c-%d')  AS \"statistic_time\" ,statistic_time as orderTime ";
            newKpis.add("area_name");
            newKpis.add("statistic_time");
        } else {
            fieldsStr += ",area_name, DATE_FORMAT(FROM_UNIXTIME(statistic_time),'%Y-%c-%d %H:%i:%s')  AS \"statistic_time\" ,statistic_time as orderTime ";
            newKpis.add("area_name");
            newKpis.add("statistic_time");
        }
        if (bizCustom1 != null) {
            fieldsStr += ",bizCustom1";
            newKpis.add("bizCustom1");
        }
        if (bizCustom2 != null) {
            fieldsStr += ",bizCustom2";
            newKpis.add("bizCustom2");
        }
        if (bizCustom3 != null) {
            fieldsStr += ",bizCustom3";
            newKpis.add("bizCustom3");
        }

        for (String kpi : fields.split(",")) {
            if (kpi.equals("loss_user")) {
                newKpis.add(kpi);
                if (!NumberUtils.isNumber(period)) {
                    if (!"day".equals(period)) {
                        newKpis.add(kpi + "_mom");
                    }
                    if ("month".equals(period) || "quarter".equals(period) || "year".equals(period)) {
                        newKpis.add(kpi + "_yoy");
                    }
                }
            }
        }
        newKpis.add("channel_group_label");
        newKpis.add("channel_label");
        newKpis.add("program_label");
        newKpis.add("section_name");
        newKpis.add("program_name");
        newKpis.add("types_cn");
        newKpis.add("types");
        newKpis.add("loss_user");
        newKpis.add("subtype_name");

        newKpis.add("level_type");
        newKpis.add("user_count");
        newKpis.add("user_level");
        newKpis.add("user_rate");
        newKpis.add("business");
        newKpis.add("subtype_name1");
        newKpis.add("game_visit_type_name");
        newKpis.add("game_visit_single_game_name");
        newKpis.add("shop_name");
        newKpis.add("type_name");
        newKpis.add("goods_name");
        return fieldsStr;
    }

    private String getStatistic(@RequestParam String period, @RequestParam String startTime, @RequestParam String endTime, String whereStr, String startTimeStr, String endTimeStr, String statistic_time_start, String statistic_time_end) {
        try {
            if (NumberUtils.isNumber(period)) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                startTimeStr = dateFormat.parse(startTime).getTime() / 1000 + "";
                endTimeStr = dateFormat.parse(endTime).getTime() / 1000 + "";
                statistic_time_start = " statistic_time>=";
                statistic_time_end = " and statistic_time<";
            } else {
                startTimeStr = DateUtil.getTransdate(period, startTime);
                endTimeStr = DateUtil.getTransdate(period, endTime);
                statistic_time_start = " REPLACE(STR_TO_DATE(FROM_UNIXTIME(statistic_time),'%Y-%m-%d'),'-','')>=";
                statistic_time_end = " and REPLACE(STR_TO_DATE(FROM_UNIXTIME(statistic_time),'%Y-%m-%d'),'-','')<";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (!startTime.equalsIgnoreCase("null")) {
            whereStr += statistic_time_start + " '" + startTimeStr + "'";
            if (!endTime.equalsIgnoreCase("null")) {
                whereStr += statistic_time_end + " '" + endTimeStr + "'";
            }
        } else {
            if (!endTime.equalsIgnoreCase("null")) {
                whereStr += "statistic_time<" + endTime;
            }
        }
        return whereStr;
    }

    private void getDistributionData(String[] distributions, List distributionData,List<Map<String, Object>> dataMap) {
//         dataMap = new ArrayList<>();
//        for(int i =0;i < 24;i++) {
//            Map<String, Object> dataObj = new HashMap<>();
//            dataObj.put("level_type", "use_time");
//            dataObj.put("user_level", i);
//            dataObj.put("user_count", Math.round(Math.random() * 1000));
//            dataMap.add(dataObj);
//        }

        for (String distribution : distributions) {
            Map map = new HashMap();
            List data = new ArrayList();
            for (int i = 1; i < 50; i++) {
                String userCount = "0";
                String userLevelAll = "";
                Boolean tag = false;
                for (Map<String, Object> mapDis : dataMap) {
                    int userLevel = 0;
                   if(!"".equals(mapDis.get("user_level")) && mapDis.get("user_level") != null )
                       userLevel = Integer.valueOf(mapDis.get("user_level").toString().split("\\[")[0]);
                       userLevelAll = mapDis.get("user_level").toString();

                    if(mapDis.get("level_type").equals("use_time") && distribution.equals("useTimeRadio") ){
                        if(userLevel == i){
                            userCount = mapDis.get("user_count").toString();
                            tag = true;
                            break;
                        }

                    }

                    if(mapDis.get("level_type").equals("use_count") && distribution.equals("useCountRadio") ){
                        if(userLevel == i){
                            userCount = mapDis.get("user_count").toString();
                            tag = true;
                            break;
                        }
                    }
                    if(mapDis.get("level_type").equals("use_day") && distribution.equals("useDayRadio") ){
                        if(userLevel == i){
                            userCount = mapDis.get("user_count").toString();
                            tag = true;
                            break;
                        }
                    }
                }
                if(tag) {
                    Map item = new HashMap();
                    item.put("name", userLevelAll);
                    item.put("value", userCount);
                    data.add(item);
                }
            }

            map.put("data", data);

            if(distribution.equals("useTimeRadio")){
                distribution = "使用时长档位";
            }else if(distribution.equals("useCountRadio")){
                distribution = "使用次数档位";
            }else if(distribution.equals("useDayRadio")){
                distribution = "使用天数档位";
            }
            map.put("name", distribution);

            distributionData.add(map);
        }
    }

    private void getChartDataMulti(String startTime, String areaName, List<Map<String, Object>> data, String[] kpis, String[] groups) {
        for (int i = 0; i < 30; i++) {
            for (String group : groups) {
                Map item = new HashMap();
                item.put("id", i);
                item.put("area_name", areaName);
                item.put("compare_type", group);

                for (String kpi : kpis) {
                    item.put(kpi, random.nextInt(1000));
                    item.put(kpi + "HBRatio", String.format("%.2f", random.nextDouble() * 100));
                    item.put(kpi + "TBRatio", String.format("%.2f", random.nextDouble() * 100));
                }

                item.put("statistic_time", startTime + i);
                data.add(item);
            }
        }
    }

    private void getChartData(String startTime, String areaName, List<Map<String, Object>> data, String[] kpis) {
        for (int i = 0; i < 30; i++) {
            Map item = new HashMap();
            item.put("id", i);
            item.put("area_name", areaName);

            for (String kpi : kpis) {
                item.put(kpi, random.nextInt(10000));
                item.put(kpi + "HBRatio", String.format("%.2f", random.nextDouble() * 100));
                item.put(kpi + "TBRatio", String.format("%.2f", random.nextDouble() * 100));
            }

            item.put("statistic_time", startTime + i);
            data.add(item);
        }
    }



    @RequestMapping(path = "/channelGroupsRank")
    public Map channelGroupsRankQuery(@RequestParam String areaType,
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
                                      @RequestParam(name = "sectionName", required = false) String sectionName,
                                      @RequestParam(name = "topNumber", required = false) Integer topNumber,
                                      @RequestParam(name = "userName", required = false) String userName
    ) {
        Map result = new HashMap();
        if (areaName == null || "-1".equals(areaName) || sectionName == null || "".equals(sectionName)) {
            result.put("chartData", new ArrayList());
            return result;
        }

        try {
            UserUseLog userUseLog = new UserUseLog(areaName,biz,bizSubtype,userName,0,1,0);
            userUseLogDAO.insert(userUseLog);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        String kpiStartTime = getKpiTimeES(period, startTime);

        List<String> newKpis = new ArrayList();
        String tableName = "";
        String whereStr = "";
        //TODO 确定时间格式
        String startTimeStr = "";
        String endTimeStr = "";
        String fieldsStr = "*";
        String statistic_time_start = "";
        String statistic_time_end = "";
        tableName = new StringBuffer("T_").append(areaType).append("_")
                .append(biz).append("_channel_")
                .append(period).toString().toLowerCase();
        /**
        tableName += " LEFT JOIN t_config_channel t1  on channel_id = t1.id ";
        try {
            if (NumberUtils.isNumber(period)) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                startTimeStr = dateFormat.parse(startTime).getTime() / 1000 + "";
                endTimeStr = dateFormat.parse(endTime).getTime() / 1000 + "";
                statistic_time_start = " statistic_time>=";
                statistic_time_end = " and statistic_time<=";
            } else {
                startTimeStr = getTransdate(period, startTime);
                endTimeStr = getTransdate(period, endTime);
                statistic_time_start = " REPLACE(STR_TO_DATE(FROM_UNIXTIME(statistic_time),'%Y-%m-%d'),'-','')>=";
                statistic_time_end = " and REPLACE(STR_TO_DATE(FROM_UNIXTIME(statistic_time),'%Y-%m-%d'),'-','')<=";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (!startTime.equalsIgnoreCase("null")) {
            whereStr += statistic_time_start + " '" + startTimeStr + "'";
            if (!endTime.equalsIgnoreCase("null")) {
                whereStr += statistic_time_end + " '" + endTimeStr + "'";
            }
        } else {
            if (!endTime.equalsIgnoreCase("null")) {
                whereStr += "statistic_time<=" + endTime;
            }
        }
        whereStr = getWereStr(areaName, bizCustom1, bizCustom2, bizCustom3, whereStr);
        if (sectionName != null && !"".equals(sectionName))
            whereStr += " and  channel_id in ( select t2.id from t_config_channel t2 where t2.channel_group_id in (" + sectionName + "))";
        if (fields != null) {
            //拼接环比，同比
            fieldsStr = getAddMomAndYoy(period, fields, bizCustom1, bizCustom2, bizCustom3, newKpis);
        }
        fieldsStr += ",label as compare_type";

        List<Map<String, Object>> data = statisticQueryDAO.queryRank(tableName, whereStr, fieldsStr);
         **/

        List<Map<String, Object>> data = new ArrayList<>();
        if (fields != null) {
            //拼接环比，同比
            fieldsStr = getAddMomAndYoy(period, fields, bizCustom1, bizCustom2, bizCustom3, newKpis);
        }

        QueryBuilder qb  =  boolQuery()
                .must(StringUtils.isNotBlank(areaName)&& !"0".equals(areaName)?termQuery("area_name",areaName):matchAllQuery())
                .must(StringUtils.isNotBlank(sectionName)?termsQuery("channel_group_label",sectionName.split(",")):matchAllQuery())
                //.must(StringUtils.isNotBlank(bizCustom1)?termQuery("biz_custom1",bizCustom1):matchAllQuery())
                //.must(StringUtils.isNotBlank(bizCustom2)?termQuery("biz_custom2",bizCustom2):matchAllQuery())
                //.must(StringUtils.isNotBlank(bizCustom3)?termQuery("biz_custom3",bizCustom3):matchAllQuery())
//                .must(termQuery("active_user", "17"))
                .filter(rangeQuery("statistic_time").from(kpiStartTime).to(kpiStartTime));

        String[] indies = DateUtil.convertESDate(kpiStartTime,null,period);

        ESDataUtil.makeESDataMap(indies,data,tableName, newKpis, qb,period);

        String sortField = fields.split(",")[0];
        data = BaseDataUtil.sortDataMap(data, sortField,topNumber);

        result.put("chartData", data);
        return result;
    }

    @RequestMapping(path = "/threeRank")
    public Map threeRank(@RequestParam String areaType,
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
                                      @RequestParam(name = "sectionName", required = false) String sectionName,
                                      @RequestParam(name = "userName", required = false) String userName
    ) {
        Map result = new HashMap();
        if (areaName == null || "-1".equals(areaName) ) {
            result.put("chartData", new ArrayList());
            return result;
        }

        String kpiStartTime = getKpiTimeES(period, startTime);

        List<String> newKpis = new ArrayList();
        String tableName = "";
        if("vod".equals(biz)){
            bizSubtype = "program";
        }else if("vod_series".equals(biz)){
            biz = "vod";
            bizSubtype = "series";
        }else{
            bizSubtype = "channel";
        }

        String param = "";
        if(areaName.equals(BaseDataUtil.ZJ_ID)){
            param = "_zj";
        }

        tableName = new StringBuffer("T_").append(areaType).append("_")
                .append(biz).append("_").append(bizSubtype).append("_")
                .append(period).append("_three").append(param).toString().toLowerCase();

        try {
            UserUseLog userUseLog = new UserUseLog(areaName,biz,tableName,userName,0,1,0);
            userUseLogDAO.insert(userUseLog);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        List<Map<String, Object>> data = new ArrayList<>();
        if (fields != null) {
            //拼接环比，同比
            getAddMomAndYoy(period, fields, bizCustom1, bizCustom2, bizCustom3, newKpis);
        }

        QueryBuilder qb  =  boolQuery()
                .must(StringUtils.isNotBlank(areaName)&& !"0".equals(areaName)?termQuery("area_name",areaName):matchAllQuery())
//                .must(StringUtils.isNotBlank(sectionName)?termsQuery("channel_group_label",sectionName.split(",")):matchAllQuery())
                //.must(StringUtils.isNotBlank(bizCustom1)?termQuery("biz_custom1",bizCustom1):matchAllQuery())
                //.must(StringUtils.isNotBlank(bizCustom2)?termQuery("biz_custom2",bizCustom2):matchAllQuery())
                //.must(StringUtils.isNotBlank(bizCustom3)?termQuery("biz_custom3",bizCustom3):matchAllQuery())
//                .must(termQuery("active_user", "17"))
                .filter(rangeQuery("statistic_time").from(kpiStartTime).to(kpiStartTime));

        String[] indies = new String[]{"threedata"};

        ESDataUtil.makeESDataMap(indies,data,tableName, newKpis, qb,period);

        result.put("distributionData", new ArrayList());
        result.put("chartData", data);
        return result;
    }

}
