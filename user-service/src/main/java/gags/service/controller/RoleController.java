package gags.service.controller;

import com.alibaba.fastjson.JSONObject;
import gags.service.dao.*;
import gags.service.entity.*;
import gags.service.util.BaseDataUtil;
import gags.service.util.CommonTreeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangtao15 on 2017-06-16.
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleDAO roleDAO;
    @Autowired
    private RelOrgAuthObjDAO relOrgAuthObjDAO;
    @Autowired
    private RelRoleMenuDAO relRoleMenuDAO;
    @Autowired
    private RelOperAuthObjDAO relOperAuthObjDAO;
    @Autowired
    private MenuDAO menuDAO;


    @RequestMapping(path = "/listAuth")
    public String getRoleList(@RequestParam Long operatorId) {
        Role role = new Role();
        role.setOperatorId(operatorId);
        role.setRoleIds(this.getRoleIds(operatorId));
        List<Role> list = roleDAO.listAuth(role);
        return JSONObject.toJSONString(list);
    }

    @RequestMapping(path = "/list")
    public String getRoleList( @RequestParam(required = false) String roleName,
                                @RequestParam Long operatorId,
                                @RequestParam(required = false) Long beginRow,
                                @RequestParam(required = false) Long pageSize) {
        Map map = new HashMap();
        Role role = new Role();
        role.setRoleName(roleName);
        role.setOperatorId(operatorId);
        role.setBeginRow(beginRow);
        role.setPageSize(pageSize);
        role.setExt1(BaseDataUtil.PC_TYPE);
        if(operatorId == BaseDataUtil.APP_SUPER_USER_ID){
            role.setExt1(BaseDataUtil.APP_TYPE);
        }
        role.setRoleIds(this.getRoleIds(operatorId));
        List<Role>list = roleDAO.list(role);
        Integer listCount = roleDAO.listCount(role);
        map.put("data",list);
        map.put("total",listCount);
        return JSONObject.toJSONString(map);
    }

    /**
     * 获取隶属于当前用户的角色集合
     * @param operatorId
     * @return
     */
    private List<Long> getRoleIds(Long operatorId){
        List<Long> roleIds = null;
        RelOperAuthObj relOperAuthObj = new RelOperAuthObj();
        relOperAuthObj.setOperatorId(operatorId);
        // 得到当前用户的角色id
        List<RelOperAuthObj> operAuthObjList = relOperAuthObjDAO.list(relOperAuthObj);
        if(operAuthObjList.size()>0){
            roleIds =  new ArrayList<>();
        }
        // 得到所有角色id
        List<Role> roleList = roleDAO.listAll(new Role());
        // 查找当前用户角色的子集
        for (RelOperAuthObj operAuthObj : operAuthObjList){
            roleIds.add(operAuthObj.getRoleId());
            List<Role> childRoleIds = CommonTreeUtil.getChildRoles(operAuthObj.getRoleId(),roleList);
            if (childRoleIds != null) {
                for (Role role : childRoleIds) {
                    roleIds.add(role.getRoleId());
                }
            }
        }
        return roleIds;
    }

    @RequestMapping(path = "/add")
    public String add(@RequestParam String roleName,
                      @RequestParam Long operatorId,
                   @RequestParam(required = false) String areaList,
                   @RequestParam(required = false) String funcList){
        Map result = new HashMap();
        List<String> areaData = new ArrayList();
        List<Long> funData = new ArrayList();

        // 获取父类角色
        RelOperAuthObj operAuthObj = new RelOperAuthObj();
        operAuthObj.setOperatorId(operatorId);
        List<RelOperAuthObj> operAuthObjList = relOperAuthObjDAO.list(operAuthObj);
        Long parentRoleId = operAuthObjList.get(0).getRoleId();

        convertFuncAndAreaData(areaList, funcList, areaData, funData);

        if(StringUtils.isEmpty(roleName)){
            result.put("result", BaseDataUtil.FAILD_VALUE);
            result.put("error","角色名称不能为空");
            return JSONObject.toJSONString(result);
        }
        Long roleId = -1L;
        try {
            // 新增角色
            Role role = new Role();
            roleId = role.getRoleId();
            role.setRoleName(roleName);
            role.setParentId(parentRoleId);
            role.setExt1(BaseDataUtil.PC_TYPE);
            if(operatorId == BaseDataUtil.APP_SUPER_USER_ID){
                role.setExt1(BaseDataUtil.APP_TYPE);
            }
            roleDAO.insert(role);
            // 新增角色关联区域组织
            for(String areaId : areaData){
                RelOrgAuthObj relOrgAuthObj = new RelOrgAuthObj();
                relOrgAuthObj.setOrganizeId(areaId);
                relOrgAuthObj.setRoleId(role.getRoleId());
                relOrgAuthObjDAO.insert(relOrgAuthObj);
            }
            // 新增角色关联菜单
            for(Long funcId: funData){
                RelRoleMenu relRoleMenu = new RelRoleMenu();
                relRoleMenu.setRoleId(role.getRoleId());
                relRoleMenu.setMenuId(funcId);
                relRoleMenuDAO.insert(relRoleMenu);
            }
            // 新增角色用户关联
            RelOperAuthObj relOperAuthObj = new RelOperAuthObj();
            relOperAuthObj.setRoleId(role.getRoleId());
            relOperAuthObj.setOperatorId(operatorId);
            relOperAuthObjDAO.insert(relOperAuthObj);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                roleDAO.delete(roleId);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            result.put("result",BaseDataUtil.FAILD_VALUE);
            result.put("error",e.getMessage());
            return JSONObject.toJSONString(result);
        } finally {
        }
        result.put("result",BaseDataUtil.SUCCESS_VALUE);
        return JSONObject.toJSONString(result);
    }

    /**
     * 转换菜单和区域数据
     * @param areaList
     * @param funcList
     * @param areaData
     * @param funData
     */
    private void convertFuncAndAreaData(@RequestParam(required = false) String areaList, @RequestParam(required = false) String funcList, List<String> areaData, List<Long> funData) {
        // 区域数据集合
        if(!StringUtils.isEmpty(areaList)){
            String [] areaPre = areaList.split(",");
            for(String key : areaPre){
                areaData.add(key.split("-")[1]);
            }
        }
        // 菜单数据集合
        if(!StringUtils.isEmpty(funcList)){
            String [] funcPre = funcList.split(",");
            for(String key : funcPre){
                Long menuId = Long.valueOf(key.split("-")[1]);
                funData.add(menuId);
            }
        }
    }

    /**
     * 得到区域id
     * @param roleId
     * @return
     */
    private List<String> convertAreaIds(Long roleId) {
        RelOrgAuthObj relOrgAuthObj = new RelOrgAuthObj();
        relOrgAuthObj.setRoleId(roleId);
        List<RelOrgAuthObj> list = relOrgAuthObjDAO.list(relOrgAuthObj);
        List<String> areaIds = new ArrayList<>();
        for (RelOrgAuthObj orgAuthObj : list) {
            areaIds.add(orgAuthObj.getOrganizeId());
        }
        return areaIds;
    }

    /**
     * 得到功能id
     * @param roleId
     * @return
     */
    private List<Long> convertFuncIds(Long roleId) {
        RelRoleMenu relRoleMenu = new RelRoleMenu();
        relRoleMenu.setRoleId(roleId);
        List<RelRoleMenu> list = relRoleMenuDAO.list(relRoleMenu);
        List<Long> funcIds = new ArrayList<>();
        for (RelRoleMenu roleMenu : list) {
            funcIds.add(roleMenu.getMenuId());
        }
        return funcIds;
    }

    @RequestMapping(path = "/update")
    public Map update(@RequestParam Long roleId, @RequestParam String roleName,
                      @RequestParam(required = false) String areaList,
                      @RequestParam(required = false) String funcList) {
        Map result = new HashMap();

        Role role = new Role();
        if (StringUtils.isEmpty(roleName)) {
            result.put("result", BaseDataUtil.FAILD_VALUE);
            result.put("error", "角色名称不能为空");
            return result;
        }
        if (roleId == null) {
            result.put("result", BaseDataUtil.FAILD_VALUE);
            result.put("error", "角色标识不能为空");
            return result;
        }
        role.setRoleId(roleId);
        role.setRoleName(roleName);

        // 入参数据
        List<String> areaData = new ArrayList();
        List<Long> funcData = new ArrayList();
        List<String> newAreaIds = new ArrayList();
        List<Long> newFuncIds = new ArrayList();
        convertFuncAndAreaData(areaList, funcList, areaData, funcData);

        // 查找区域是否含有新增数据
        if(areaData.size()>0){
            boolean flag = true;
            List<String> roleIds = this.convertAreaIds(roleId);
            for(String newRoleId : areaData){
                for(String oldRoleId : roleIds){
                    if(newRoleId.equals(oldRoleId)){
                       flag = false;
                       break;
                    }
                }
                if(flag){
                    newAreaIds.add(newRoleId);
                }
                flag = true;
            }
        }
        // 查找菜单是否含有新增数据
        if(funcData.size()>0){
            boolean flag = true;
            List<Long> roleIds = this.convertFuncIds(roleId);
            for(Long newFuncId : funcData){
                for(Long oldFuncId : roleIds){
                    if(newFuncId.equals(oldFuncId)){
                        flag = false;
                        break;
                    }
                }
                if(flag) {
                    newFuncIds.add(newFuncId);
                }
                flag = true;
            }
        }
        try {
            // 修改角色
            roleDAO.update(role);
            // 新增角色关联区域组织
            for (String areaId : newAreaIds) {
                RelOrgAuthObj relOrgAuthObj = new RelOrgAuthObj();
                relOrgAuthObj.setOrganizeId(areaId);
                relOrgAuthObj.setRoleId(roleId);
                relOrgAuthObjDAO.insert(relOrgAuthObj);
            }
            // 新增角色关联菜单
            for (Long funcId : newFuncIds) {
                RelRoleMenu relRoleMenu = new RelRoleMenu();
                relRoleMenu.setRoleId(roleId);
                relRoleMenu.setMenuId(funcId);
                relRoleMenuDAO.insert(relRoleMenu);
            }
            // 修改角色关联区域数据
            if(areaData.size()>0) {
                RelOrgAuthObj relOrgAuthObj = new RelOrgAuthObj();
                relOrgAuthObj.setAreaIds(areaData);
                relOrgAuthObj.setRoleId(roleId);
                relOrgAuthObjDAO.update(relOrgAuthObj);
            }
            // 修改角色关联功能数据
            if(funcData.size()>0) {
                RelRoleMenu relRoleMenu = new RelRoleMenu();
                relRoleMenu.setMenuIds(funcData);
                relRoleMenu.setRoleId(roleId);
                relRoleMenuDAO.update(relRoleMenu);
            }

        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", BaseDataUtil.FAILD_VALUE);
            result.put("error", e.getMessage());
            return result;
        } finally {
        }
        result.put("result",BaseDataUtil.SUCCESS_VALUE);
        return result;
    }

    @RequestMapping(path = "/del")
    public Map del(@RequestParam Long roleId){
        Map result = new HashMap();
        if(roleId == null){
            result.put("result", BaseDataUtil.FAILD_VALUE);
            result.put("error","角色标识不能为空");
            return result;
        }
        try {
            roleDAO.delete(roleId);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result",BaseDataUtil.FAILD_VALUE);
            result.put("error",e.getMessage());
            return result;
        } finally {
        }
        result.put("result",BaseDataUtil.SUCCESS_VALUE);
        return result;
    }

    /**
     * 获取菜单id集合
     * @param mnuId
     * @param list
     * @param ids
     */
    private void getMenuIds(Long mnuId, List<Menu> list, List<Long> ids){
        for(Menu mnu : list){
            if(mnu.getMenuId().equals(mnuId)){
                if(ids.contains(mnu.getParentId()) || mnu.getParentId() < 0){
                    return ;
                }
                ids.add(mnu.getParentId());
                getMenuIds(mnu.getParentId(),list,ids);
            }

        }
    }
}
