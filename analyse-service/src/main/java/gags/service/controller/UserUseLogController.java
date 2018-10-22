package gags.service.controller;


import gags.service.dao.UserUseLogDAO;
import gags.service.entity.UserUseLog;
import gags.service.util.BaseDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * Created by zhangtao on 2018/7/05.
 */
@RestController
@RequestMapping("/userUseLog")
public class UserUseLogController {

    @Autowired
    private UserUseLogDAO userUseLogDAO;

    @RequestMapping(value  = "/add")
    public void add(@RequestParam(name = "biz", required = false) String biz,
                    @RequestParam(name = "bizSubtype", required = false) String bizSubtype,
                    @RequestParam(name = "loginCount", required = false) Integer loginCount,
                    @RequestParam(name = "browsePageCount", required = false) Integer browsePageCount,
                    @RequestParam(name = "dataDownCount", required = false) Integer dataDownCount,
                    @RequestParam(name = "areaName", required = false) String areaName,
                    @RequestParam String userName){
        try {
            UserUseLog userUseLog = new UserUseLog(areaName,biz,bizSubtype,userName,loginCount,browsePageCount,dataDownCount);
            userUseLogDAO.insert(userUseLog);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

}