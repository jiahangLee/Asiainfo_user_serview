package gags.service.controller;

import gags.service.dao.MenuAppDAO;
import gags.service.dao.MenuDAO;
import gags.service.dao.RelRoleMenuDAO;
import gags.service.entity.*;
import gags.service.util.CommonTreeUtil;
import gags.service.util.BaseDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;

import java.util.*;

/**
 * Created by zhangtao15 on 2017-06-16.
 */
@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private MenuDAO menuDAO;
    @Autowired
    private MenuAppDAO menuAppDAO;
    @Autowired
    private RelRoleMenuDAO relRoleMenuDAO;

    @RequestMapping(path = "/list", method = RequestMethod.POST)
    public ResponseEntity<List<Menu>> getMenuList(@RequestParam(required = false) Long menuId,
                                                  @RequestParam(required = false) String menuName) {
       /* Menu menu = new Menu();
        menu.setMenuId(menuId);
        menu.setMenuName(menuName);
        return new ResponseEntity<List<Menu>>(menuDAO.list(menu), HttpStatus.OK);*/
       return null;
    }

    @RequestMapping(path = "/menuTree")
    public String getMenuSideNav(@RequestParam Long operatorId) {
        Map map = new HashMap();
        Menu menu = new Menu();

        List<Menu> list = null;
        if(BaseDataUtil.SUPER_USER_ID.equals(operatorId)){
            list = menuDAO.query(menu);
        }else{
            menu.setOperatorId(operatorId);
            list = initMenuData(menuDAO.list(menu));
        }

        map.put("rootMenu",this.getRootMenu(list));
        map.put("authUrls",this.getAuthUrl(list));
        map.put("authIds",this.getAuthId(list));
        List<MenuTree>rootTree = this.getRootMenu(list);
        for(MenuTree menuTree :rootTree){
            map.put(menuTree.getKey(),this.convertChildMenuTree(list,menuTree.getKey()));
        }
        //map.put("childMenu",this.convertChildMenuTree(list,menuCode));
        return JSONObject.toJSONString(map);
    }

    @RequestMapping(path = "/menus")
    public String getMenuSideNavApp(@RequestParam Long operatorId) {
        Map map = new HashMap();
        MenuApp menu = new MenuApp();

        List<MenuApp> list = null;
        if (BaseDataUtil.APP_SUPER_USER_ID.equals(operatorId)) {
            list = menuAppDAO.query(menu);
        } else {
            menu.setOperatorId(operatorId);
            list = initMenuAppData(menuAppDAO.list(menu));
        }

        map.put("menus", this.convertChildMenuTreeApp(list));

        return JSONObject.toJSONString(map);
    }

    /**
     * 初始化菜单数据
     * @param menuList
     * @return
     */
    private List<Menu> initMenuData(List<Menu> menuList) {
        List<Menu> funcData = new ArrayList<>();
        Menu menu = new Menu();
        List<Menu> list = menuDAO.query(menu);
        for (Menu mnu : menuList) {
            boolean flag = true;
            for (Menu func : funcData) {
                if (mnu.getMenuId().equals(func.getMenuId())) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                funcData.add(mnu);
                this.getParentMenus(mnu.getParentId(), list, funcData);
            }
        }
        return funcData;
    }

    /**
     * 初始化菜单数据app
     * @param menuList
     * @return
     */
    private List<MenuApp> initMenuAppData(List<MenuApp> menuList) {
        List<MenuApp> funcData = new ArrayList<>();
        MenuApp menu = new MenuApp();
        List<MenuApp> list = menuAppDAO.query(menu);
        for (MenuApp mnu : menuList) {
            boolean flag = true;
            for (MenuApp func : funcData) {
                if (mnu.getMenuId().equals(func.getMenuId())) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                funcData.add(mnu);
                this.getParentAppMenus(mnu.getParentId(), list, funcData);
            }
        }
        return funcData;
    }

    /**
     * 获取父类菜单app
     * @param mnuId
     * @param list
     * @param funcData
     */
    private void getParentAppMenus(Long mnuId, List<MenuApp> list, List<MenuApp> funcData){
        for(MenuApp mnu : list){
            if(mnu.getMenuId().equals(mnuId)){
                boolean flag = true;
                for(MenuApp func : funcData){
                    if(mnu.getMenuId().equals(func.getMenuId())){
                        flag = false;
                        break;
                    }
                }
                if(flag){
                    funcData.add(mnu);
                }
                if(mnu.getParentId() < 0 || !flag){
                    return ;
                }
                getParentAppMenus(mnu.getParentId(),list,funcData);
            }

        }
    }

    /**
     * 获取父类菜单
     * @param mnuId
     * @param list
     * @param funcData
     */
    private void getParentMenus(Long mnuId, List<Menu> list, List<Menu> funcData){
        for(Menu mnu : list){
            if(mnu.getMenuId().equals(mnuId)){
                boolean flag = true;
                for(Menu func : funcData){
                    if(mnu.getMenuId().equals(func.getMenuId())){
                        flag = false;
                        break;
                    }
                }
                if(flag){
                    funcData.add(mnu);
                }
                if(mnu.getParentId() < 0 || !flag){
                    return ;
                }
                getParentMenus(mnu.getParentId(),list,funcData);
            }

        }
    }


    @RequestMapping(path = "/funcTree")
    public String getFuncTree(@RequestParam Long operatorId) {
        //---------------load app funcTree------------------
        MenuApp menuApp = new MenuApp();
        List<MenuApp> menuAppList = null;

        if(operatorId == BaseDataUtil.APP_SUPER_USER_ID){
            menuAppList =  menuAppDAO.query(menuApp);
            return JSONObject.toJSONString(this.convertFuncTreeApp(menuAppList));
        }
        //----------------load pc funcTree-------------
        Menu menu = new Menu();

        List<Menu> list = null;
        if(BaseDataUtil.SUPER_USER_ID.equals(operatorId)){
            list = menuDAO.query(menu);
        }else{
            menu.setOperatorId(operatorId);
            list = initMenuData(menuDAO.list(menu));
        }

        return JSONObject.toJSONString(this.convertFuncTree(list));
    }

    @RequestMapping(path = "/funcIds")
    public String getFuncIds(@RequestParam Long roleId) {
        RelRoleMenu relRoleMenu = new RelRoleMenu();
        relRoleMenu.setRoleId(roleId);
        List list = relRoleMenuDAO.list(relRoleMenu);
        return JSONObject.toJSONString(convertFuncIds(list));
    }

    /**
     * 得到功能id
     * @param list
     * @return
     */
    private List<String> convertFuncIds(List<RelRoleMenu> list) {
        List<String> funcIds = new ArrayList<>();
        for (RelRoleMenu relRoleMenu : list) {
            funcIds.add("-"+relRoleMenu.getMenuId());
        }
        return funcIds;
    }

    /**
     * 获取允许的url地址
     * @param list
     * @return
     */
    private List<String> getAuthUrl(List<Menu> list){
        List<String> urls = new ArrayList<>();
        for(Menu menu : list){
            if(!StringUtils.isEmpty(menu.getMenuUrl())){
                urls.add(menu.getMenuUrl());
            }
        }
        return urls;
    }

    /**
     * 获取用户权限
     * @param list
     * @return
     */
    private List<Long> getAuthId(List<Menu> list){
        List<Long> ids = new ArrayList<>();
        for(Menu menu : list){
            if(BaseDataUtil.PARENTID_USERMANAGE.equals(menu.getParentId())
                    || BaseDataUtil.PARENTID_ROLEMANAGE.equals(menu.getParentId())){
                ids.add(menu.getMenuId());
            }
        }
        return ids;
    }

    /**
     * 查询导航菜单
     * @param list
     * @return
     */
    private List<MenuTree> getRootMenu(List<Menu> list){
        List<MenuTree> mtList = new ArrayList<>();
        if(list == null) return mtList;
        for(Menu mnu : list){
            if(mnu.getParentId().equals(BaseDataUtil.PARENTID_PAGEMANAGE)){
                MenuTree mt = new MenuTree();
                mt.setMenuId(mnu.getMenuId());
                mt.setKey(mnu.getMenuCode());
                mt.setName(mnu.getMenuName());
                mt.setPath(mnu.getMenuPath());
                for(Menu cmnu : list){
                    if(cmnu.getMenuUrl()!=null && cmnu.getMenuUrl().contains(mnu.getMenuPath())){
                        mt.setPath(cmnu.getMenuUrl());
                        break;
                    }
                }
                mtList.add(mt);
            }
        }
        if (mtList.size() > 0) {
            Collections.sort(mtList);
        }
        return mtList;
    }

    /**
     * 查询子菜单
     * @param list
     * @return
     */
    private List<MenuTree> convertChildMenuTree(List<Menu> list,String menuCode){
        List<MenuTree> rootList = new ArrayList<>();
        List<MenuTree> mtList = new ArrayList<>();
        Long parentId = -99L;
        if(list == null) return mtList;
        for(Menu mnu : list){
            MenuTree mt = new MenuTree();
            mt.setKey(mnu.getMenuCode());
            mt.setName(mnu.getMenuName());
            mt.setPath(mnu.getMenuPath());
            mt.setIcon(mnu.getMenuImage());
            mt.setMenuId(mnu.getMenuId());
            mt.setParentId(mnu.getParentId());
            if(mnu.getMenuCode().equals(menuCode)){
                parentId = mnu.getMenuId();
            }
            rootList.add(mt);
        }

        for(MenuTree menuTree : rootList){
            if(menuTree.getParentId().equals(parentId)){
                mtList.add(menuTree);
            }
        }
        // 递归查找设置子目录
        for(MenuTree menuTree : mtList){
            menuTree.setChildren(CommonTreeUtil.getChilMenus(menuTree.getMenuId(), rootList));
        }
        if (mtList.size() > 0) {
            Collections.sort(mtList);
        }
        return mtList;
    }

    /**
     * 查询子菜单app
     * @param list
     * @return
     */
    private List<MenuApp> convertChildMenuTreeApp(List<MenuApp> list){
        List<MenuApp> rootList = new ArrayList<>();
        List<MenuApp> mtList = new ArrayList<>();
        Long parentId = -1L;
        if(list == null) return mtList;

        for(MenuApp menuTree : list){
            if(menuTree.getParentId().equals(parentId)){
                mtList.add(menuTree);
            }
        }
        // 递归查找设置子目录
        for(MenuApp menuTree : mtList){
            menuTree.setMenus(CommonTreeUtil.getChilMenusApp(menuTree.getMenuId(), list));
        }
        if (mtList.size() > 0) {
            Collections.sort(mtList);
        }
        return mtList;
    }


    /**
     * 查询菜单权限树
     * @param list
     * @return
     */
    private List<AuthTree> convertFuncTree(List<Menu> list){
        List<AuthTree> rootList = new ArrayList<>();
        List<AuthTree> authList = new ArrayList<>();
        if(list==null) return authList;
        for(Menu mnu : list){
            AuthTree authTree = new AuthTree();
            authTree.setKey('-'+String.valueOf(mnu.getMenuId()));
            authTree.setTitle(mnu.getMenuName());
            authTree.setTreeId(mnu.getMenuId());
            authTree.setParentId(mnu.getParentId());
            rootList.add(authTree);
        }

        for(AuthTree authTree : rootList){
            if(authTree.getParentId().equals(BaseDataUtil.PARENT_ID)){
                authList.add(authTree);
            }
        }
        // 递归查找设置子目录
        for(AuthTree authTree : authList){
            authTree.setChild(CommonTreeUtil.getChilAuthTree(authTree.getTreeId(), rootList));
        }
        return authList;
    }

    /**
     * 查询菜单权限树app
     * @param list
     * @return
     */
    private List<AuthTree> convertFuncTreeApp(List<MenuApp> list){
        List<AuthTree> rootList = new ArrayList<>();
        List<AuthTree> authList = new ArrayList<>();
        if(list==null) return authList;
        for(MenuApp mnu : list){
            AuthTree authTree = new AuthTree();
            authTree.setKey('-'+String.valueOf(mnu.getMenuId()));
            authTree.setTitle(mnu.getMenuTitle());
            authTree.setTreeId(mnu.getMenuId());
            authTree.setParentId(mnu.getParentId());
            rootList.add(authTree);
        }

        for(AuthTree authTree : rootList){
            if(authTree.getParentId().equals(BaseDataUtil.PARENT_ID)){
                authList.add(authTree);
            }
        }
        // 递归查找设置子目录
        for(AuthTree authTree : authList){
            authTree.setChild(CommonTreeUtil.getChilAuthTree(authTree.getTreeId(), rootList));
        }
        return authList;
    }


    @RequestMapping(path = "/add")
    public Map add(@RequestParam String menuName,@RequestParam Long parentId){
        Map result = new HashMap();
        if(StringUtils.isEmpty(menuName)){
            result.put("result", BaseDataUtil.FAILD_VALUE);
            result.put("error","菜单名称不能为空");
            return result;
        }
        Menu menu = new Menu();
        menu.setMenuName(menuName);
        menu.setParentId(parentId);

        dealMenuDAOData(result,menu,"add");
        return result;
    }

    @RequestMapping(path = "/update")
    public Map update(@RequestParam Long menuId, @RequestParam String menuName,
                      @RequestParam(required = false) Long parentId){
        Map result = new HashMap();
        Menu menu = new Menu();
        if(StringUtils.isEmpty(menuName)){
            result.put("result", BaseDataUtil.FAILD_VALUE);
            result.put("error","菜单名称不能为空");
            return result;
        }
        if(menuId == null){
            result.put("result", BaseDataUtil.FAILD_VALUE);
            result.put("error","菜单标识不能为空");
            return result;
        }
        menu.setMenuId(menuId);
        menu.setMenuName(menuName);
        menu.setParentId(parentId);
        dealMenuDAOData(result,menu,"update");
        return result;
    }

    @RequestMapping(path = "/del")
    public Map del(@RequestParam Long menuId){
        Map result = new HashMap();
        Menu menu = new Menu();
        if(menuId == null){
            result.put("result", BaseDataUtil.FAILD_VALUE);
            result.put("error","菜单标识不能为空");
            return result;
        }
        menu.setMenuId(menuId);
        dealMenuDAOData(result,menu,"del");
        return result;
    }

    /**
     * 处理菜单相关业务数据
     * @param result 返回处理结果
     * @param menu 菜单对象
     * @param delType 处理类型 新增:add 修改:update 删除:del
     */
    private void dealMenuDAOData(Map result,Menu menu,String delType) {
        try {
            switch(delType){
                case "add" :
                    menuDAO.insert(menu);
                    break;
                case "update" :
                    menuDAO.update(menu);
                    break;
                case "del" :
                    menuDAO.delete(menu.getMenuId());
                    break;
                default:
                    System.out.println("未匹配到该"+delType+"类型操作!");
            }
            result.put("result",BaseDataUtil.FAILD_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", BaseDataUtil.FAILD_VALUE);
            result.put("error",e.getMessage());
        } finally {
        }
    }
}
