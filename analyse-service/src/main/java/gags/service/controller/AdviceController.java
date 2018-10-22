package gags.service.controller;

import com.alibaba.fastjson.JSONObject;
import gags.service.dao.AdviceDAO;
import gags.service.entity.Advice;
import gags.service.util.BaseDataUtil;
import gags.service.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import gags.service.dao.RelOperAuthObjDAO;
//import gags.service.dao.RoleDAO;
//import gags.service.entity.RelOperAuthObj;
//import gags.service.util.AESUtil;
//import gags.service.util.BaseDataUtil;

/**
 * Created by zhangzc Lee on 2018/1/31.
 */
@RestController
@RequestMapping("/advice")
public class AdviceController {

    @Autowired
    private AdviceDAO adviceDAO;


    @RequestMapping(path = "/list")
    public String getAdviceList() {
        Map map = new HashMap();
        Advice advice = new Advice();
        List<Advice>list = adviceDAO.list(advice);
        return JSONObject.toJSONString(list);
    }

    @RequestMapping(value  = "/add", method=RequestMethod.POST)
    public Map add(@RequestBody Advice advice){
        Map result = new HashMap();
        try {
            adviceDAO.insert(advice);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", BaseDataUtil.FAILD_VALUE);
            result.put("error",e.getMessage());
            return result;
        } finally {
        }
        result.put("result",BaseDataUtil.SUCCESS_VALUE);
        return result;
    }

}