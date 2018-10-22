package gags.service.controller;

import com.alibaba.fastjson.JSONObject;
import gags.service.dao.OrganizeDAO;
import gags.service.dao.RelOrgAuthObjDAO;
import gags.service.entity.AreaConfig;
import gags.service.entity.AuthTree;
import gags.service.entity.Organize;
import gags.service.entity.RelOrgAuthObj;
import gags.service.util.CommonTreeUtil;
import gags.service.util.BaseDataUtil;
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
 * Created by jiahang Lee on 2017/6/16.
 */
@RestController
@RequestMapping("/organize")
public class OrganizeController {

    @Autowired
    private OrganizeDAO organizeDAO;
    @Autowired
    private RelOrgAuthObjDAO relOrgAuthObjDAO;

    @RequestMapping(path = "/areaList")
    public String getAreaList(@RequestParam Long operatorId) {
        AreaConfig areaConfig = new AreaConfig();
        List list = null;
        if(operatorId == BaseDataUtil.SUPER_USER_ID){
            list = organizeDAO.listAll(areaConfig);
        }else{
            areaConfig.setOperatorId(operatorId);
            list =  organizeDAO.listArea(areaConfig);
        }
        String areaStr = JSONObject.toJSONString(convertArea(list));
        areaStr = areaStr.replace("parentId","parent_id");
        return areaStr;
    }

    @RequestMapping(path = "/areaTree")
    public String getAreaTree(@RequestParam Long operatorId) {
        AreaConfig areaConfig = new AreaConfig();
        List list = null;
        if(operatorId == BaseDataUtil.SUPER_USER_ID){
            list = organizeDAO.listAll(areaConfig);
        }else{
            areaConfig.setOperatorId(operatorId);
            list =  initMenuData(organizeDAO.listArea(areaConfig));
        }

        return JSONObject.toJSONString(convertAreaTree2(list));
    }

    @RequestMapping(path = "/areaIds")
    public String getAreaIds(@RequestParam Long roleId) {
        RelOrgAuthObj relOrgAuthObj = new RelOrgAuthObj();
        relOrgAuthObj.setRoleId(roleId);
        List list = relOrgAuthObjDAO.list(relOrgAuthObj);
        return JSONObject.toJSONString(convertAreaIds(list));
    }

    /**
     * 得到区域id
     * @param list
     * @return
     */
    private List<String> convertAreaIds(List<RelOrgAuthObj> list) {
        List<String> areaIds = new ArrayList<>();
        for (RelOrgAuthObj relOrgAuthObj : list) {
            areaIds.add("-"+relOrgAuthObj.getOrganizeId());
        }
        return areaIds;
    }

    /**
     * 转换区域数据为树形结构
     * @param list
     * @return
     */
    private List convertArea(List<AreaConfig>list){
        List<AreaConfig> listArea = new ArrayList<>();
        boolean flag = true;
        for(AreaConfig areaConfig : list){
            for(AreaConfig area : list){
                if (!StringUtils.isEmpty(areaConfig.getParentId()) && areaConfig.getParentId().equals(area.getId())) {
                    flag = false;
                    break;
                }
            }
            if(flag){
                areaConfig.setParentId(String.valueOf(BaseDataUtil.AREA_ROOT_ID));
            }
            flag = true;
        }
        return list;

    }

    /**
     * 初始化区域数据
     * @param configList
     * @return
     */
    private List<AreaConfig> initMenuData(List<AreaConfig> configList) {
        List<AreaConfig> areaData = new ArrayList<>();
        AreaConfig areaConfig = new AreaConfig();
        List<AreaConfig> list = organizeDAO.listAll(areaConfig);
        for (AreaConfig area : configList) {
            boolean flag = true;
            for (AreaConfig areadata : areaData) {
                if (areadata.getId().equals(area.getParentId())) {
                    for (AreaConfig areadata2 : areaData) {
                        if(areadata2.getId().equals(area.getId())){
                            flag = false;
                        }
                    }
                    if(flag){
                        areaData.add(area);
                    }
                    flag = false;
                    break;
                }
            }
            if (flag) {
                areaData.add(area);
                this.getParentAreas(area.getParentId(), list, areaData);
            }
        }
        return areaData;
    }

    /**
     * 获取父类组织
     * @param areaId
     * @param list
     * @param areaList
     */
    private void getParentAreas(String areaId, List<AreaConfig> list, List<AreaConfig> areaList){
        for(AreaConfig areaConfig : list){
            if(areaConfig.getId().equals(areaId)){
                boolean flag = true;
                for(AreaConfig area : areaList){
                    if(areaConfig.getId().equals(area.getId())){
                        flag = false;
                        break;
                    }
                }
                if(flag){
                    areaList.add(areaConfig);
                }
                if(StringUtils.isEmpty(areaConfig.getParentId())){
                    return;
                }
                if (areaConfig.getParentId().equals("-1") || !flag) {
                    return;
                }
                getParentAreas(areaConfig.getParentId(),list,areaList);
            }

        }
    }

    /**
     * 转换区域数据为树形结构
     * @param list
     * @return
     */
    private List<AuthTree> convertAreaTree2(List<AreaConfig> list){
        List<AuthTree> rootList = new ArrayList<>();
        List<AuthTree> authList = new ArrayList<>();
        if(list == null) return authList;
        for(AreaConfig areaConfig : list){
            Long parentId = StringUtils.isEmpty(areaConfig.getParentId()) ? BaseDataUtil.AREA_ROOT_ID : Long.valueOf(areaConfig.getParentId());
            AuthTree authTree = new AuthTree();
            authTree.setKey('-'+String.valueOf(areaConfig.getId()));
            authTree.setTitle(areaConfig.getLabel());
            authTree.setTreeId(Long.valueOf(areaConfig.getId()));
            authTree.setParentId(parentId);
            rootList.add(authTree);
        }

        for(AuthTree authTree : rootList){
            if(authTree.getParentId().equals(BaseDataUtil.AREA_ROOT_ID)){
                authList.add(authTree);
            }
        }
        // 递归查找设置子目录
        for(AuthTree authTree : authList){
            authTree.setChild(CommonTreeUtil.getChilAuthTree(authTree.getTreeId(), rootList));
        }
        return authList;
    }

/*

    private List<AuthTree> convertAreaTree(List<Organize> list){
        List<AuthTree> rootList = new ArrayList<>();
        List<AuthTree> authList = new ArrayList<>();
        if(list == null) return authList;
        for(Organize organize : list){
            AuthTree authTree = new AuthTree();
            authTree.setKey('-'+String.valueOf(organize.getOrganizeId()));
            authTree.setTitle(organize.getOrganizeName());
            authTree.setTreeId(organize.getOrganizeId());
            authTree.setParentId(organize.getParentId());
            rootList.add(authTree);
        }

        for(AuthTree authTree : rootList){
            if(authTree.getParentId() == -1){
                authList.add(authTree);
            }
        }
        // 递归查找设置子目录
        for(AuthTree authTree : authList){
            authTree.setChild(CommonTreeUtil.getChilAuthTree(authTree.getTreeId(), rootList));
        }
        return authList;
    }
*/


    @RequestMapping(path = "/add")
    public Map add(@RequestParam String organizeName){
        Map result = new HashMap();
        if(StringUtils.isEmpty(organizeName)){
            result.put("result", BaseDataUtil.FAILD_VALUE);
            result.put("error","角色名称不能为空");
            return result;
        }
        try {
            Organize organize = new Organize();
            organize.setOrganizeName(organizeName);
            organizeDAO.insert(organize);
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

    @RequestMapping(path = "/update")
    public Map update(@RequestParam Long organizeId, @RequestParam String organizeName){
        Map result = new HashMap();
        Organize organize = new Organize();
        if(StringUtils.isEmpty(organizeName)){
            result.put("result", BaseDataUtil.FAILD_VALUE);
            result.put("error","角色名称不能为空");
            return result;
        }
        if(organizeId == null){
            result.put("result", BaseDataUtil.FAILD_VALUE);
            result.put("error","角色标识不能为空");
            return result;
        }
        organize.setOrganizeId(organizeId);
        organize.setOrganizeName(organizeName);
        try {
            organizeDAO.update(organize);
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

    @RequestMapping(path = "/del")
    public Map del(@RequestParam Long organizeId){
        Map result = new HashMap();
        if(organizeId == null){
            result.put("result", BaseDataUtil.FAILD_VALUE);
            result.put("error","角色标识不能为空");
            return result;
        }
        try {
            organizeDAO.delete(organizeId);
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
}
