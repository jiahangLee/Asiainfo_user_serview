package gags.service.controller;

import gags.service.dao.RelOrgAuthObjDAO;

import gags.service.entity.RelOrgAuthObj;

import gags.service.util.BaseDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiahang Lee on 2017/6/28.
 */
public class RelOrgAuthObjController {

    @Autowired
    private RelOrgAuthObjDAO relOrgAuthObjDAO;

    @RequestMapping(path = "/add")
    public Map add(@RequestParam Long organizeId, @RequestParam Long roleId, @RequestParam Integer status, @RequestParam Date createDate, @RequestParam Date modifyDate) {
        Map result = new HashMap();

        if (StringUtils.isEmpty(organizeId)) {
            result.put("result", BaseDataUtil.FAILD_VALUE);
            result.put("error", "操作员不能为空");
            return result;
        }
        try {
            RelOrgAuthObj relOrgAuthObj = new RelOrgAuthObj();
            relOrgAuthObj.setOrganizeId(String.valueOf(organizeId));
            relOrgAuthObj.setRoleId(roleId);
            relOrgAuthObj.setStatus(status);
            relOrgAuthObj.setCreateDate(createDate);
            relOrgAuthObj.setModifyDate(modifyDate);

            relOrgAuthObjDAO.insert(relOrgAuthObj);
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
