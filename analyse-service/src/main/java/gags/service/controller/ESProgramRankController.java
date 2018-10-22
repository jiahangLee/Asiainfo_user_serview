package gags.service.controller;

import gags.service.dao.ProgramRankQueryDAO;
import gags.service.dao.UserUseLogDAO;
import gags.service.entity.UserUseLog;
import gags.service.util.BaseDataUtil;
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
import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;

@RestController
public class ESProgramRankController {
    @Autowired
    private UserUseLogDAO userUseLogDAO;

    @RequestMapping(path = "/programRank")
    public Map programRankQuery(@RequestParam String areaType,
                                @RequestParam String biz, //dvb, ...
                                @RequestParam String bizSubtype, //common
                                @RequestParam String period, //1,5,15,30,60,day,week,weekday,month,halfmonth,year
                                @RequestParam String startTime,
                                @RequestParam(name = "sectionName", required = false) String sectionName,
                                @RequestParam(name = "fields", required = false) String fields, // kpi's englishName
                                @RequestParam(name = "areaName", required = false) String areaName,
                                @RequestParam(name = "bizCustom1", required = false) String bizCustom1,
                                @RequestParam(name = "bizCustom2", required = false) String bizCustom2,
                                @RequestParam(name = "bizCustom3", required = false) String bizCustom3,
                                @RequestParam(name = "topNumber", required = false) Integer topNumber,
                                @RequestParam(name = "userName", required = false) String userName){
        Map result = new HashMap();
        //图表/列表数据
        List<Map<String, Object>> data = new ArrayList<>();
        //统计分布数据（）
        List distributionData = new ArrayList();
        List<String> newKpis = new ArrayList();
        if (StringUtils.isBlank(sectionName) && !"sectionRank".equals(bizSubtype)) {
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

        String bizCustoms = "";

        if ("sectionRank".equals(bizSubtype)) {
            bizSubtype = "section";
        }else{
            bizSubtype = "program";
        }

        String tableName = new StringBuffer("T_").append(areaType).append("_")
                .append(biz).append("_")
                .append(bizSubtype).append("_")
                .append(period).toString().toLowerCase();

        if (StringUtils.isNotBlank(sectionName)) {
            bizCustoms = MD5Util.MD5(sectionName);
        }

        String kpiStartTime = getKpiTimeES(period, startTime);
        getAddMomAndYoy(period, fields, newKpis);

        QueryBuilder dfqb = boolQuery()
                .must(StringUtils.isNotBlank(areaName) && !"0".equals(areaName) ? termQuery("area_name", areaName) : matchAllQuery())
                .must(StringUtils.isNotBlank(bizCustoms) ? matchPhraseQuery("section_name", bizCustoms) : matchAllQuery())
                // .must(StringUtils.isNotBlank(bizCustom2)?termQuery("biz_custom2",bizCustom2):matchAllQuery())
                // .must(StringUtils.isNotBlank(bizCustom3)?termQuery("biz_custom3",bizCustom3):matchAllQuery())
                .filter(rangeQuery("statistic_time").from(kpiStartTime).to(kpiStartTime));

        String[] indies = DateUtil.convertESDate(kpiStartTime, null, period);

        ESDataUtil.makeESDataMap(indies, data, tableName.toLowerCase(), newKpis, dfqb, period);

        String sortField = fields.split(",")[0];
        data = BaseDataUtil.sortDataMap(data, sortField,topNumber);

        result.put("chartData", data);
        result.put("distributionData", distributionData);
        return result;
    }

    void getAddMomAndYoy(@RequestParam String period, @RequestParam(name = "fields", required = false) String
            fields, List<String> newKpis) {
        newKpis.clear();
        //拼接环比，同比
        for (String kpi : fields.split(",")) {
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

        newKpis.add("area_name");
        newKpis.add("statistic_time");
        newKpis.add("bizCustom1");
        newKpis.add("bizCustom2");
        newKpis.add("bizCustom3");
        newKpis.add("channel_group_label");
        newKpis.add("channel_label");
        newKpis.add("section_name");
        newKpis.add("program_name");
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
