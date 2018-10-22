package gags.service.controller;

import com.alibaba.fastjson.JSONObject;
import gags.service.dao.RelOperAuthObjDAO;
import gags.service.dao.RelOperAuthObjDAO;
import gags.service.entity.AuthTree;
import gags.service.entity.Operator;
import gags.service.entity.RelOperAuthObj;
import gags.service.entity.RelOperAuthObj;
import gags.service.util.BaseDataUtil;
import gags.service.util.CommonTreeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by jiahang Lee on 2017/6/28.
 */
@RestController
@RequestMapping("/relOperAuthObj")
public class RelOperAuthObjController {

    @Autowired
    private RelOperAuthObjDAO relOperAuthObjDAO;

    @RequestMapping(path = "/add")
    public Map add(@RequestParam Long operatorId,@RequestParam Long roleId,@RequestParam Integer status,@RequestParam Date createDate,@RequestParam Date modifyDate) {
        Map result = new HashMap();

        if (StringUtils.isEmpty(operatorId)) {
            result.put("result", BaseDataUtil.FAILD_VALUE);
            result.put("error", "菜单不能为空");
            return result;
        }
        try {
            RelOperAuthObj relOperAuthObj = new RelOperAuthObj();
            relOperAuthObj.setOperatorId(operatorId);
            relOperAuthObj.setRoleId(roleId);
            relOperAuthObj.setStatus(status);
            relOperAuthObj.setCreateDate(createDate);
            relOperAuthObj.setModifyDate(modifyDate);

            relOperAuthObjDAO.insert(relOperAuthObj);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", BaseDataUtil.FAILD_VALUE);
            result.put("error", e.getMessage());
            return result;
        } finally {
        }
        result.put("result", BaseDataUtil.SUCCESS_VALUE);
        return result;
    }
}


