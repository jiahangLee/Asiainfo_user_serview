package gags.service.controller;

import gags.service.dao.RelRoleMenuDAO;
import gags.service.entity.RelRoleMenu;
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
public class RelRoleMenuController {

    @Autowired
    private RelRoleMenuDAO RelRoleMenuDAO;

    @RequestMapping(path = "/add")
    public Map add(@RequestParam Long menuId, @RequestParam Long roleId, @RequestParam Integer status, @RequestParam Date createDate, @RequestParam Date modifyDate) {
        Map result = new HashMap();

        if (StringUtils.isEmpty(menuId)) {
            result.put("result", BaseDataUtil.FAILD_VALUE);
            result.put("error", "操作员不能为空");
            return result;
        }
        try {
            RelRoleMenu relRoleMenu = new RelRoleMenu();
            relRoleMenu.setMenuId(menuId);
            relRoleMenu.setRoleId(roleId);
            relRoleMenu.setStatus(status);
            relRoleMenu.setCreateDate(createDate);
            relRoleMenu.setModifyDate(modifyDate);

            RelRoleMenuDAO.insert(relRoleMenu);
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
