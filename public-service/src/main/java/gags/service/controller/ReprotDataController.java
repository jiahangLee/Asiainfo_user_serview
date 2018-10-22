package gags.service.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import gags.service.util.BaseDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangtao on 2017/9/9.
 */
@RestController
public class ReprotDataController {
    @Autowired
    RestTemplate restTemplate;

    @Value("${topNumber}")
    private Integer topNumber;


    @RequestMapping("/report")
    public Map sayHello2(@RequestParam String areaType,
                         @RequestParam String biz, //dvb, ...
                         @RequestParam String bizSubtype, //common
                         @RequestParam String period, //1,5,15,30,60,day,week,weekday,month,halfmonth,year
                         @RequestParam String startTime,
                         @RequestParam String endTime,
                         @RequestParam String areaName,
                         @RequestParam(name = "fields", required = false) String fields, // kpi's englishName
                         @RequestParam(name = "sectionName", required = false) String sectionName,
                         @RequestParam String loginName, @RequestParam String password) {

        Map<String, Object> resultData = new HashMap<>();

        if(!"quarter".equals(period)){
            startTime += "0";
            endTime += "0";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("areaType", areaType);
        map.put("biz", biz);
        map.put("bizSubtype", bizSubtype);
        map.put("period", period);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("areaName", areaName);
        map.put("fields", fields);
        if ("common".equals(bizSubtype)) {
            sectionName = "";
        }
        map.put("sectionName", sectionName);
        map.put("loginName", loginName);
        map.put("password", password);
        map.put("topNumber",topNumber);

        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(BaseDataUtil.USER_URL, String.class, map);
            responseEntity.getBody();

            JSONObject userObj = JSONObject.parseObject(responseEntity.getBody());
            resultData.put("result", userObj.get("result"));
            resultData.put("errorMsg", userObj.get("error"));
            if (BaseDataUtil.SUCCESS_VALUE.equals(userObj.get("result"))) {
                boolean flag = false;
                Object operatorId = JSONObject.parseObject(userObj.get("userMsg").toString()).get("operatorId");
                ResponseEntity<String> userAuthObj = restTemplate.getForEntity(BaseDataUtil.AREA_URL, String.class, operatorId);
                JSONArray jsonObject = JSONArray.parseArray(userAuthObj.getBody());
                for (Object json : jsonObject) {
                    JSONObject area = JSONObject.parseObject(json.toString());
                    if (area.get("id").equals(areaName)) {
                        flag = true;
                    }
                }

                if (!flag) {
                    resultData.put("result", BaseDataUtil.FAILD_VALUE);
                    resultData.put("errorMsg", BaseDataUtil.AUTH_ERROR_MSG);
                    return resultData;
                }
//                System.out.print(jsonObject);

            } else {
                return resultData;
            }

            String url = BaseDataUtil.ANALYSES_STATISTIC_URL;
            if (bizSubtype.equals("programRank")) {
                map.put("requestName",bizSubtype);
                url = BaseDataUtil.ANALYSES_RANK_URL;
            }
            if (bizSubtype.equals("channelGroupRank")) {
                map.put("requestName","channelGroupsRank");
                url = BaseDataUtil.ANALYSES_RANK_URL;
            }

            ResponseEntity<String> responseReport = restTemplate.getForEntity(url, String.class, map);

            JSONObject jsonObject = JSONObject.parseObject(responseReport.getBody());

            resultData.put("chartData", jsonObject.get("chartData"));
        } catch (Exception e) {
            e.printStackTrace();
            resultData.put("result", BaseDataUtil.FAILD_VALUE);
            resultData.put("errorMsg", "接口请求异常:"+e.getMessage());
            return resultData;
        } finally {
        }

        return resultData;
    }
}
