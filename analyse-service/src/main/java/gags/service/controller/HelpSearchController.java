package gags.service.controller;

import gags.service.dao.StatisticQueryDAO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangzc on 2018/1/26.
 */
@RestController
public class HelpSearchController {

    @Autowired
    private StatisticQueryDAO helpSearchDAO;

    @RequestMapping(path = "/helpSearch")
    public Map commonStatisticQuery(
            @RequestParam(name = "targetName", required = false) String targetName) {
        Map result = new HashMap();
        //图表/列表数据
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();



        String tableName = new StringBuffer("help_search").toString().toUpperCase();
        String whereStr = "";


        if (StringUtils.isNotEmpty(targetName)) {
            whereStr += "where target_name  like '%" + targetName + "%'";
        } else {
            whereStr += "";
        }


        System.out.println(whereStr);
        String fieldsStr = "target_name as target_name,target_explain as target_explain,formula " +
                "as formula,period as period";


        try {
            data = helpSearchDAO.queryHelpSearch(tableName, whereStr, fieldsStr);
            if(data.size() < 1){
                result.put("helpSearch", data);
                return result;
            }
            result.put("helpSearch", data);
            return result;
        } catch (Exception e) {
            result.put("helpSearch", new ArrayList());
            e.printStackTrace();
        } finally {
            return result;
        }
    }
}
