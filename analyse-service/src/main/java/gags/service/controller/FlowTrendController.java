package gags.service.controller;

import com.alibaba.fastjson.JSONObject;
import gags.service.dao.StatisticQueryDAO;
import gags.service.dao.UserUseLogDAO;
import gags.service.entity.FlowTrendConfig;
import gags.service.entity.UserUseLog;
import gags.service.util.DateUtil;
import gags.service.util.ESDataUtil;
import net.sf.jsqlparser.expression.DoubleValue;
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
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;


/**
 * Created by jiahang Lee on 2017/7/28.
 */
@RestController
public class FlowTrendController {

    @Autowired
    private StatisticQueryDAO statisticQueryDAO;
    @Autowired
    private UserUseLogDAO userUseLogDAO;
    private Random random = new Random();
    // 过滤的节点数据
    private List nodeNames = new ArrayList();
    {
        nodeNames.add("10000575-全部剧集");
       // nodeNames.add("10000615-全部剧集");
        nodeNames.add("10000574-全部剧集");
        nodeNames.add("10000587-热门车型");
        nodeNames.add("10000584-生活超市");
        nodeNames.add("10000576-环球购物");
        nodeNames.add("10000584-全部剧集");
        nodeNames.add("10000616-223");
        nodeNames.add("10000576-全部剧集");

        nodeNames.add("10000589-汽车益家");
        nodeNames.add("10000588-二手车");
        nodeNames.add("10000575-嘉丽购物");
        nodeNames.add("10000574-汽车平台");
        nodeNames.add("10000587-播放页");
        nodeNames.add("10000575-播放页");
        nodeNames.add("10000576-播放页");
        nodeNames.add("10000574-播放页");
        nodeNames.add("10000589-播放页");
        nodeNames.add("10000584-播放页");
    }


    @RequestMapping(path = "/flowTrend")
    public String  dataList(@RequestParam String areaType, //china,province,city,proje
                                   @RequestParam String biz, //dvb, ...
                                   @RequestParam String bizSubtype, //common
                                   @RequestParam String period, //1,5,15,30,60,day,week,weekday,month,halfmonth,year
                                   @RequestParam String startTime,
                                   @RequestParam String endTime,
                                   @RequestParam(name = "fields", required = false) String fields,
                                   @RequestParam(name = "areaName", required = false) String areaName,
                                   @RequestParam(name = "userName", required = false) String userName) {
        Map result = new HashMap();
        //图表/列表数据
        List<Map<String, Object>> data = new ArrayList();

        List<FlowTrendConfig> nodeList = new ArrayList();
        List<FlowTrendConfig> linkList = new ArrayList();
        List<FlowTrendConfig> categoryList = new ArrayList();

        Map resultData = new HashMap();

        List<String> newKpis = new ArrayList();
        String tableName = "";
        bizSubtype = "flowtrend";

        String kpiStartTime = DateUtil.getKpiTimeES(period, startTime);
        String kpiEndTime = DateUtil.getKpiTimeES(period, endTime);

        String[] indies = DateUtil.convertESDate(kpiStartTime, kpiStartTime, period);

        tableName = new StringBuffer("T_").append(areaType).append("_")
                .append(bizSubtype).append("_")
                .append(period).toString().toLowerCase();

        getAddFields(newKpis);

        try {
            UserUseLog userUseLog = new UserUseLog(areaName,biz,bizSubtype,userName,0,1,0);
            userUseLogDAO.insert(userUseLog);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        QueryBuilder clqb = boolQuery()
                .must(StringUtils.isNotBlank(areaName) && !"0".equals(areaName) ? termQuery("area_name", areaName) : matchAllQuery())
                // .must(StringUtils.isNotBlank(bizCustom2)?termQuery("biz_custom2",bizCustom2):matchAllQuery())
                // .must(StringUtils.isNotBlank(bizCustom3)?termQuery("biz_custom3",bizCustom3):matchAllQuery())
                .filter(rangeQuery("statistic_time").from(kpiStartTime).to(kpiStartTime));

        ESDataUtil.makeESDataMap(indies, data, tableName, newKpis, clqb, period);

        for (Map<String, Object> node : data) {

            FlowTrendConfig nodes = new FlowTrendConfig();
            FlowTrendConfig links = new FlowTrendConfig();
            FlowTrendConfig categories = new FlowTrendConfig();

            String name = (String)node.get("current_pagename");
            String parentName = (String)node.get("parent_pagename");
            String userCount = (String)node.get("user_count");
            String clickCount = (String)node.get("click_count");
            String pageLevel = (String)node.get("page_level");

            if ("天天健康".equals(name) && "长沙".equals(parentName)) {
                name = "天天健康1";
            }
            if (areaName.equals("0101")) {
                if(nodeNames.contains(name)) {
                    continue;
                }
                if(name.equals("4K体验")){
                    name = "4K专区";
                }
                if(name.equals("TV名师") && parentName.equals("教育")){
                    name = "TV名师1";
                }
                if(name.equals("TV名师") && parentName.equals("长沙")){
                    name = "TV名师2";
                }
                if(name.equals("TV名师") && parentName.equals("推荐")){
                    name = "TV名师3";
                }
                if(name.equals("入口1") || name.equals("入口2") ){
                    name = name + "-" + parentName;
                }
            }

//            nodes.put("name",name+"(用户数："+userCount+",点击次数："+clickCount+")");
//            nodes.setName(name+"(用户数："+userCount+",点击次数："+clickCount+")");
            String targetPageName = name + "(UV:" + userCount + ",PV:" + clickCount + ")";
            nodes.setName(targetPageName);
            nodes.setValue(userCount);
            if ("-1".equals(parentName)) {
                categories.setName(targetPageName);
                nodes.setCategory(targetPageName);
                nodes.setSymbolSize("28");
                links.setSource("用户");
                links.setTarget(targetPageName);
//                links.setValue(userCount);
            }else{
//                links.setSource(parentName+"(用户数："+userCount+",点击次数："+clickCount+")");
//                links.setTarget(name+"(用户数："+userCount+",点击次数："+clickCount+")");
                String sourceName = getParentPageName(data,parentName,areaName);
                categories.setName(targetPageName);
                if("2".equals(pageLevel)){
                    nodes.setSymbolSize("18");
                }else if ("3".equals(pageLevel)){
                    nodes.setSymbolSize("14");
                }else if ("4".equals(pageLevel)){
                    nodes.setSymbolSize("10");
                }else{
                    nodes.setSymbolSize("6");
                }
                nodes.setCategory(sourceName);
                links.setSource(sourceName);
                links.setTarget(targetPageName);

            }
            categoryList.add(categories);
            linkList.add(links);
            nodeList.add(nodes);
        }

        FlowTrendConfig nodes = new FlowTrendConfig();
        nodes.setName("用户");
        nodes.setSymbolSize("40");
        nodeList.add(nodes);

        System.out.println(nodeList.toString());
        int tag = 0;
        int index = 0;
        int flag = 0;
        List<FlowTrendConfig> nodeListData = new LinkedList<>();
        for(FlowTrendConfig dd :nodeList){
            nodeListData.add(dd);
            for(FlowTrendConfig tt :nodeList){
                if(dd.getName().equals(tt.getName())){
                    tag++;
                    if(tag>1){
                        System.out.println("-------------");
                        System.out.println(tt);
                        flag++;
                    }
                }
            }
            index++;
            tag = 0;
        }

        if(flag > 0){
            result.put("links", new ArrayList());
            result.put("nodes", new ArrayList());
            result.put("categories", new ArrayList());
            result.put("data", new ArrayList());
        }else {
            result.put("links", linkList);
            result.put("nodes", nodeListData);
            result.put("categories", categoryList);
            result.put("data", data);
        }

        resultData.put("chartData",result);

        return JSONObject.toJSONString(resultData);
    }

    private String getParentPageName(List<Map<String, Object>> data, String parentName,String areaName) {
        String parentPageName = "";
        for (Map<String, Object> node : data) {
            String name = (String) node.get("current_pagename");
            String userCount = (String) node.get("user_count");
            String clickCount = (String) node.get("click_count");

            if (areaName.equals("0101")) {
                if(name.equals("4K体验") && parentName.equals("4K专区")){
                    name = "4K专区";
                }
            }

            if (parentName.equals(name)) {
                parentPageName = name + "(UV:" + userCount + ",PV:" + clickCount + ")";
                break;
            }

        }
        return parentPageName;
    }

    void getAddFields(List<String> newKpis) {

        newKpis.add("area_name");
        newKpis.add("statistic_time");
        newKpis.add("current_pagename");
        newKpis.add("parent_pagename");
        newKpis.add("user_count");
        newKpis.add("page_level");
        newKpis.add("click_count");
        newKpis.add("l_date");

    }


    @RequestMapping(path = "/flowTrend2")
    public String  getOperatorList(@RequestParam String areaType, //china,province,city,proje
                               @RequestParam String biz, //dvb, ...
                               @RequestParam String bizSubtype, //common
                                  @RequestParam String period, //1,5,15,30,60,day,week,weekday,month,halfmonth,year
                                  @RequestParam String startTime,
                                  @RequestParam String endTime,
                                  @RequestParam(name = "fields", required = false) String fields,
                                  @RequestParam(name = "areaName", required = false) String areaName) {
        Map map = new HashMap();

        Map result = new HashMap();
        //图表/列表数据
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        //统计分布数据（）
        List distributionData = new ArrayList();
        String tableName = new StringBuffer("T_").append(areaType).append("_")
                .append("dvb").append("_")
                .append("page").append("_")
                .append(period).toString().toUpperCase();
        String whereStr = "";
        //TODO 确定时间格式
        String startTimeStr = "";
        String endTimeStr = "";
        String statistic_time_start = "";
        String statistic_time_end = "";
        try {
            if (NumberUtils.isNumber(period)) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                startTimeStr = dateFormat.parse(startTime).getTime() / 1000 + "";
                endTimeStr = dateFormat.parse(startTime).getTime() / 1000 + "";
                statistic_time_start = " statistic_time>=";
                statistic_time_end = " and statistic_time<";
            } else {
                startTimeStr = DateUtil.getTransdate(period, startTime);
                endTimeStr = DateUtil.getTransdate(period, endTime);
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
                whereStr += statistic_time_end + " '" + endTimeStr + "'";
            }
        }
        if (areaName != null && !"0".equals(areaName)) {
            whereStr += " and area_name='" + areaName + "'";
        }

        String fieldsStr = "*";
        if (fields != null) {
            fieldsStr = fields.toLowerCase();
            fieldsStr += ",area_name,page_name, DATE_FORMAT(FROM_UNIXTIME(statistic_time),'%Y-%c-%d')  AS \"statistic_time\" ,statistic_time as orderTime ";
        }
        try {
            data = statisticQueryDAO.query(tableName, whereStr, fieldsStr);
//            for(Map<String, Object> mapData : data ){
//
//            }
            Map<String, Object> flowTrendData = new HashMap<>();
            Map<String, Object> activeUserMap1 = new HashMap<>();
            Map<String, Object> activeUserMap2 = new HashMap<>();
            Map<String, Object> activeUserMap3 = new HashMap<>();
            Map<String, Object> activeUserMap4 = new HashMap<>();
            List activeUserData1 = new ArrayList();
            List activeUserLinks1 = new ArrayList();
            List activeUserCategories1 = new ArrayList();
            for(Map<String, Object> each:data)
            {

                FlowTrendConfig flowTrendConfig = new FlowTrendConfig();
            FlowTrendConfig flowTrendConfigLink = new FlowTrendConfig();
                FlowTrendConfig flowTrendConfigCategories = new FlowTrendConfig();
                flowTrendConfigCategories.setName(each.get("page_name").toString());
                activeUserCategories1.add(flowTrendConfigCategories);
            activeUserLinks1.add(flowTrendConfigLink);
                flowTrendConfig.setName(each.get("page_name").toString());
                flowTrendConfig.setValue(each.get("active_user").toString());
                flowTrendConfig.setCategory(each.get("page_name").toString());
                flowTrendConfig.setSymbolSize(each.get("active_user").toString().length()*10+20+"");
                activeUserData1.add(flowTrendConfig);
            }
            activeUserMap1.put("data",activeUserData1);
            activeUserMap1.put("links",activeUserLinks1);
            activeUserMap1.put("categories",activeUserCategories1);
            flowTrendData.put("activeUser",activeUserMap1);
            List activeUserData2 = new ArrayList();
            List activeUserLinks2 = new ArrayList();
            List activeUserCategories2 = new ArrayList();
            for(Map<String, Object> each:data)
            {

                FlowTrendConfig flowTrendConfig = new FlowTrendConfig();
                FlowTrendConfig flowTrendConfigLink = new FlowTrendConfig();
                FlowTrendConfig flowTrendConfigCategories = new FlowTrendConfig();
                flowTrendConfigCategories.setName(each.get("page_name").toString());
                activeUserCategories2.add(flowTrendConfigCategories);
                activeUserLinks2.add(flowTrendConfigLink);
                flowTrendConfig.setName(each.get("page_name").toString());
                flowTrendConfig.setValue(each.get("active_user_ratio").toString());
                flowTrendConfig.setCategory(each.get("page_name").toString());
                flowTrendConfig.setSymbolSize(each.get("active_user_ratio").toString().length()*10+20+"");
                activeUserData2.add(flowTrendConfig);
            }
            activeUserMap2.put("data",activeUserData2);
            activeUserMap2.put("links",activeUserLinks2);
            activeUserMap2.put("categories",activeUserCategories2);
            flowTrendData.put("activeUserRatio",activeUserMap2);
            List activeUserData3 = new ArrayList();
            List activeUserLinks3 = new ArrayList();
            List activeUserCategories3 = new ArrayList();

            for(Map<String, Object> each:data)
            {
                FlowTrendConfig flowTrendConfig = new FlowTrendConfig();
                FlowTrendConfig flowTrendConfigLink = new FlowTrendConfig();
                FlowTrendConfig flowTrendConfigCategories = new FlowTrendConfig();
                flowTrendConfigCategories.setName(each.get("page_name").toString());
                activeUserCategories3.add(flowTrendConfigCategories);
                activeUserLinks3.add(flowTrendConfigLink);
                flowTrendConfig.setName(each.get("page_name").toString());
                flowTrendConfig.setValue(each.get("use_count").toString());
                flowTrendConfig.setCategory(each.get("page_name").toString());
                flowTrendConfig.setSymbolSize(each.get("use_count").toString().length()*10+20+"");
                activeUserData3.add(flowTrendConfig);
            }
            activeUserMap3.put("data",activeUserData3);
            activeUserMap3.put("links",activeUserLinks3);
            activeUserMap3.put("categories",activeUserCategories3);
            flowTrendData.put("useCount",activeUserMap3);
            List activeUserData4 = new ArrayList();
            List activeUserLinks4 = new ArrayList();
            List activeUserCategories4 = new ArrayList();
            for(Map<String, Object> each:data)
            {

                FlowTrendConfig flowTrendConfig = new FlowTrendConfig();
                FlowTrendConfig flowTrendConfigLink = new FlowTrendConfig();
                FlowTrendConfig flowTrendConfigCategories = new FlowTrendConfig();
                flowTrendConfigCategories.setName(each.get("page_name").toString());
                activeUserCategories4.add(flowTrendConfigCategories);
                activeUserLinks4.add(flowTrendConfigLink);
                flowTrendConfig.setName(each.get("page_name").toString());
                flowTrendConfig.setValue(each.get("time_in_use").toString());
                flowTrendConfig.setCategory(each.get("page_name").toString());
                flowTrendConfig.setSymbolSize(each.get("time_in_use").toString().length()*10+20+"");
                activeUserData4.add(flowTrendConfig);
            }
            activeUserMap4.put("data",activeUserData4);
            activeUserMap4.put("links",activeUserLinks4);
            activeUserMap4.put("categories",activeUserCategories4);
            flowTrendData.put("timeInUse",activeUserMap4);
//            result.put("activeUser", activeUserMap);
//            List activeUserData = new ArrayList();
//            FlowTrendConfig flowTrendConfig = new FlowTrendConfig("首页 100",null,null);
//            activeUserData.add(flowTrendConfig);
//            FlowTrendConfig flowTrendConfig2 = new FlowTrendConfig("okList 100",null,null);
//            activeUserData.add(flowTrendConfig2);
//            FlowTrendConfig flowTrendConfig3 = new FlowTrendConfig("配置页 100",null,null);
//            activeUserData.add(flowTrendConfig3);

//            activeUserMap.put("data",activeUserData);


//            List activeUserLinks = new ArrayList();
//            FlowTrendConfig flowTrendConfigLink = new FlowTrendConfig(null,"首页 100","okList 100");
//            activeUserLinks.add(flowTrendConfigLink);
//            FlowTrendConfig flowTrendConfigLink2 = new FlowTrendConfig(null,"okList 100","配置页 100");
//            activeUserLinks.add(flowTrendConfigLink2);
//            FlowTrendConfig flowTrendConfigLink3 = new FlowTrendConfig(null,"配置页 100","首页 100");
//            activeUserLinks.add(flowTrendConfigLink3);

//            activeUserMap.put("links",activeUserLinks);
//
//
//            result.put("activeUser", activeUserMap);


            result.put("chartData", flowTrendData);
        } catch (Exception e) {
            result.put("chartData", new ArrayList());
            e.printStackTrace();
        } finally {
            return JSONObject.toJSONString(result);
        }
}}
