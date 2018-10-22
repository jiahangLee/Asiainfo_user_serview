package gags.service.controller;

import com.alibaba.fastjson.JSONObject;
import gags.service.dao.MenuDAO;
import gags.service.dao.OperatorDAO;
import gags.service.dao.RelOperAuthObjDAO;
import gags.service.dao.RoleDAO;
import gags.service.entity.Menu;
import gags.service.entity.Operator;
import gags.service.entity.RelOperAuthObj;
import gags.service.entity.Role;
import gags.service.util.AESUtil;
import gags.service.util.BaseDataUtil;
import gags.service.util.CommonTreeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static gags.service.util.BaseDataUtil.DEFAULT_PWD;

/**
 * Created by jiahang Lee on 2017/6/16.
 */
@RestController
@RequestMapping("/operator")
public class OperatorController {

    @Autowired
    private OperatorDAO operatorDAO;
    @Autowired
    private RelOperAuthObjDAO relOperAuthObjDAO;
    @Autowired
    private RoleDAO roleDAO;
    @Autowired
    private MenuDAO menuDAO;


    @RequestMapping(path = "/login")
    public String login(@RequestParam String loginName,@RequestParam String password) {
        Map result = new HashMap();
        Operator operator = new Operator();
        operator.setOperatorCode(loginName);
        List<Operator> list = operatorDAO.queryOperator(operator);
//        System.out.print("==========================login:"+loginName+"=========AES==="+AESUtil.encrypt(password)+"========list:==="+list.get(0).getPassword());
//        System.out.print("==============================="+AESUtil.encrypt(password).equals(list.get(0).getPassword())+"========================");

        if(list != null && list.size() > 0 && AESUtil.encrypt(password).equals(list.get(0).getPassword())){
            Long operatorId = list.get(0).getOperatorId();
            String operatorName = list.get(0).getOperatorName();
            String operatorCode = list.get(0).getOperatorCode();
            String endDate = list.get(0).getEndDate();
            boolean isInvalid = false;
            if (endDate != null) {
                isInvalid = BaseDataUtil.parseDate(endDate,BaseDataUtil.DATE_TYPE_YYYY_MM_DD).before(new Date());
                if (isInvalid){
                    result.put("result", BaseDataUtil.FAILD_VALUE);
                    result.put("error","用户已失效");
                }
            }
            if(!isInvalid){
                Operator oper = new Operator();
                oper.setOperatorId(operatorId);
                oper.setOperatorName(operatorName);
                oper.setOperatorCode(operatorCode);
                result.put("result", BaseDataUtil.SUCCESS_VALUE);
                result.put("userMsg", oper);
                result.put("homeUrl", this.getHomeUrl(operatorId));
            }
        }else{
            result.put("result", BaseDataUtil.FAILD_VALUE);
            result.put("error","登录名或密码错误");
        }
        return JSONObject.toJSONString(result);
    }

    /**
     * 获取登录首页地址
     * @param operatorId
     * @return
     */
    private String getHomeUrl(Long operatorId){
        Menu menu = new Menu();

        List<Menu> list = null;
        if(BaseDataUtil.SUPER_USER_ID.equals(operatorId)){
            list = menuDAO.query(menu);
        }else{
            menu.setOperatorId(operatorId);
            list = menuDAO.list(menu);
        }
        // 获取一级菜单
        for (Menu mnu : list) {
            if (!StringUtils.isEmpty(mnu.getMenuUrl()) && BaseDataUtil.HOME_MENU_LEVEL == mnu.getMenuLevel()) {
                return mnu.getMenuUrl();
            }
        }
        // 获取二级级菜单
        for (Menu mnu : list) {
            if (!StringUtils.isEmpty(mnu.getMenuUrl()) && BaseDataUtil.IA_MENU_LEVEL == mnu.getMenuLevel()) {
                return mnu.getMenuUrl();
            }
        }
        // 其他
        for (Menu mnu : list) {
            if (!StringUtils.isEmpty(mnu.getMenuUrl())) {
                return mnu.getMenuUrl();
            }
        }
        return "/404";
    }

    @RequestMapping(path = "/checkUser")
    public String login(@RequestParam String loginName) {
        Map result = new HashMap();
        Operator operator = new Operator();
        operator.setOperatorCode(loginName);
        List<Operator> list = operatorDAO.queryOperator(operator);
        if(list != null && list.size() > 0){
            result.put("result", BaseDataUtil.FAILD_VALUE);
            result.put("error","用户名已存在");
        }else{
            result.put("result", BaseDataUtil.SUCCESS_VALUE);
        }
        return JSONObject.toJSONString(result);
    }
    @RequestMapping(path="/resetPwd")
    public Map resetPwd(@RequestParam Long operatorId)
    {
        Map result = new HashMap();
        try {
            Operator operator = new Operator();
            operator.setOperatorId(operatorId);
            operator.setPassword(AESUtil.encrypt(DEFAULT_PWD));
            operatorDAO.update(operator);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", BaseDataUtil.FAILD_VALUE);
            result.put("error","重置失败");
            result.put("result", BaseDataUtil.SUCCESS_VALUE);
        }
        return result;
    }
    @RequestMapping(path = "/checkPwd")
    public String checkPwd(@RequestParam String oldPwd,@RequestParam Long operatorId) {
        Map result = new HashMap();
        Operator operator = new Operator();
        operator.setOperatorId(operatorId);
        List<Operator> list = operatorDAO.queryOperator(operator);
        if(! list.get(0).getPassword().equals(AESUtil.encrypt(oldPwd)) ){
            result.put("result", BaseDataUtil.FAILD_VALUE);
            result.put("error","旧密码不正确");
        }else{
            result.put("result", BaseDataUtil.SUCCESS_VALUE);
        }
        return JSONObject.toJSONString(result);
    }
    @RequestMapping(value = "/modifyPwd")
    public Map update(@RequestParam String newPassword,@RequestParam long operatorId){
        Map result = new HashMap();
        try {
            Operator operator = new Operator();
            operator.setOperatorId(operatorId);
            operator.setPassword(AESUtil.encrypt(newPassword));
            operatorDAO.update(operator);
            result.put("result", BaseDataUtil.SUCCESS_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", BaseDataUtil.FAILD_VALUE);
            result.put("error","修改失败");
        }
        return result;
    }
    @RequestMapping(path = "/list")
    public String getOperatorList(@RequestParam(required = false) Long operatorId,
                                  @RequestParam(required = false) String operatorName,
                                  @RequestParam(required = false) Long beginRow,
                                  @RequestParam(required = false) Long pageSize) {
        Map map = new HashMap();
        Operator operator = new Operator();
        operator.setOperatorName(operatorName);
        operator.setBeginRow(beginRow);
        operator.setPageSize(pageSize);
        operator.setRoleId(getRoleIds(operatorId));
        operator.setExt1(BaseDataUtil.PC_TYPE);
        if(operatorId == BaseDataUtil.APP_SUPER_USER_ID){
            operator.setExt1(BaseDataUtil.APP_TYPE);
        }
        List<Operator>list = operatorDAO.list(operator);
        Integer listCount = operatorDAO.listCount(operator);
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

    @RequestMapping(value  = "/add", method=RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map add(@RequestBody  Operator operator){
        Map result = new HashMap();
        if(StringUtils.isEmpty(operator.getOperatorCode())){
            result.put("result", BaseDataUtil.FAILD_VALUE);
            result.put("error","用户名不能为空");
            return result;
        }
        if(StringUtils.isEmpty(operator.getRoleName())){
            result.put("result", BaseDataUtil.FAILD_VALUE);
            result.put("error","角色不能为空");
            return result;
        }
        try {
            operator.setPassword(AESUtil.encrypt(operator.getPassword()));
            operatorDAO.insert(operator);
            RelOperAuthObj relOperAuthObj = new RelOperAuthObj();
            relOperAuthObj.setRoleId(Long.valueOf(operator.getRoleName()));
            relOperAuthObj.setOperatorId(operator.getOperatorId());
            relOperAuthObjDAO.insert(relOperAuthObj);
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

    @RequestMapping(value = "/update", method=RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map update(@RequestBody Operator operator){
        Map result = new HashMap();
        if(operator.getOperatorId() == null){
            result.put("result", BaseDataUtil.FAILD_VALUE);
            result.put("error","用户标识不能为空");
            return result;
        }
        try {
            Long operatorId = operator.getOperatorId();
            // 修改用户权限
            if(!StringUtils.isEmpty(operator.getRoleName())) {
                Long roleId = Long.valueOf(operator.getRoleName());
                if(this.changeAuth(operatorId,roleId)) {
                    RelOperAuthObj relOperAuthObj = new RelOperAuthObj();
                    relOperAuthObj.setOperatorId(operatorId);
                    relOperAuthObj.setRoleId(Long.valueOf(roleId));
                    relOperAuthObjDAO.update(relOperAuthObj);
                    relOperAuthObjDAO.insert(relOperAuthObj);
                }
            }
            if(operator.getEndDate()!=null){
                operator.setEndDate(BaseDataUtil.parseDate(operator.getEndDate(), BaseDataUtil.DATE_TYPE_YYYY_MM_DD));
            }
            operatorDAO.update(operator);
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
     * 判断用户是否已存在该权限
     * @param operatorId
     * @param roleId
     * @return
     */
    private Boolean changeAuth(Long operatorId,Long roleId){
        RelOperAuthObj relOperAuthObj = new RelOperAuthObj();
        relOperAuthObj.setOperatorId(operatorId);
        // 得到当前用户的角色id
        List<RelOperAuthObj> operAuthObjList = relOperAuthObjDAO.list(relOperAuthObj);
        for (RelOperAuthObj operAuthObj : operAuthObjList) {
            if (operAuthObj.getRoleId().equals(roleId)) {
                return false;
            }
        }
        return true;
    }

    @RequestMapping(path = "/del")
    public Map del(@RequestParam Long operatorId){
        Map result = new HashMap();
        if(operatorId == null){
            result.put("result", BaseDataUtil.FAILD_VALUE);
            result.put("error","用户标识不能为空");
            return result;
        }
        try {
            RelOperAuthObj relOperAuthObj = new RelOperAuthObj();
            relOperAuthObj.setOperatorId(operatorId);
            relOperAuthObjDAO.update(relOperAuthObj);
            operatorDAO.delete(operatorId);
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