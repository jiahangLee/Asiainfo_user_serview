package gags.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by liekkas on 2017/3/1.
 */
@RestController
@RequestMapping(value = "/analyses2")
public class InsightAnalysis {
    private static final Logger log = LoggerFactory.getLogger(InsightAnalysis.class);
    private Random random = new Random();
    @RequestMapping(path = "/statistic")
    public List<Map<String,Object>> commonStatisticQuery(@RequestParam String areaType,
                                                         @RequestParam String biz, //dvb, ...
                                                         @RequestParam String bizSubtype, //common
                                                         @RequestParam String period, //1,5,15,30,60,day,week,weekday,month,halfmonth,year
                                                         @RequestParam String startTime,
                                                         @RequestParam String endTime,
                                                         @RequestParam(name = "fields", required = false) String fields, // kpi's englishName
                                                         @RequestParam(name="areaName",required = false) String areaName,
                                                         @RequestParam(name="bizCustom1",required = false) String bizCustom1,
                                                         @RequestParam(name="bizCustom2",required = false) String bizCustom2,
                                                         @RequestParam(name="bizCustom3",required = false) String bizCustom3
    ){
        log.info(">>> commonStatisticQuery: area:{} business:{} type:{} period:{} startTime:{} endTime:{} fields:{}", areaType, biz, bizSubtype, period, startTime, endTime, fields, areaName);
        List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
        String[] kpis = fields.split(",");

        if("common".equals(bizSubtype)) {
            for (int i = 0; i < 30; i++) {
                Map item = new HashMap();
                item.put("id", i);
                item.put("area_name", areaName);

                for (String kpi : kpis) {
                    item.put(kpi, random.nextInt(10000));
                    item.put(kpi + "HBRatio", String .format("%.2f",random.nextDouble()*100));
                    item.put(kpi + "TBRatio", String .format("%.2f",random.nextDouble()*100));
                }

                item.put("statistic_time", startTime+i);
                data.add(item);
            }
        } else if("channelGroup".equals(bizSubtype)) { //模拟频道组分析
            String[] groups = {"央视频道", "卫视频道", "省级卫视", "地方频道", "其它频道"};
            for (int i = 0; i < 30; i++) {
                for (String group : groups) {
                    Map item = new HashMap();
                    item.put("id", i);
                    item.put("area_name", areaName);
                    item.put("channel_type", group);

                    for (String kpi : kpis) {
                        item.put(kpi, random.nextInt(1000));
                        item.put(kpi + "HBRatio", String .format("%.2f",random.nextDouble()*100));
                        item.put(kpi + "TBRatio", String .format("%.2f",random.nextDouble()*100));
                    }

                    item.put("statistic_time", startTime+i);
                    data.add(item);
                }
            }
        }



        return data;
    }


    @RequestMapping(path = "/config/areas")
    public List<Map<String,Object>> areas(){
        log.info(">>> areas: ");
        List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
        Map provice = new HashMap();
        provice.put("id", 1);
        provice.put("name", "hunan");
        provice.put("parent_id", 0);
        provice.put("label", "湖南省");
        provice.put("type", "province");
        data.add(provice);

        Map city = new HashMap();
        city.put("id", 2);
        city.put("name", "cs");
        city.put("parent_id", 1);
        city.put("label", "长沙市");
        city.put("type", "city");
        data.add(city);

        Map project = new HashMap();
        project.put("id", 3);
        project.put("name", "proj");
        project.put("parent_id", 2);
        project.put("label", "长沙项目1");
        project.put("type", "project");
        data.add(project);

        return data;
    }
}
