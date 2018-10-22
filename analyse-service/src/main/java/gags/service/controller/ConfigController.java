package gags.service.controller;

import gags.service.dao.ChannelGroupQueryDAO;
import gags.service.dao.ChannelQueryDAO;
import gags.service.dao.ConfigQueryDAO;
import gags.service.entity.ChannelAndChannelGroupEntity;
import gags.service.entity.ConfigEntity;
import gags.service.entity.ConfigTreeEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lyl on 2017/3/13.
 *
 *
 *
 */



@RestController
@RequestMapping("/configs")
@Api(value = "基础配置信息API")
public class ConfigController {

    @Autowired
    private ConfigQueryDAO configQueryDAO;

    @Autowired
    private ChannelGroupQueryDAO channelGroupQueryDAO;

    @Autowired
    private ChannelQueryDAO channelQueryDAO;

    @GetMapping(path = "/areas")
    @ApiOperation(value = "获取所有区域信息")
    public ResponseEntity<List<ConfigTreeEntity>> getAllArea() {

        return new ResponseEntity<List<ConfigTreeEntity>>(configQueryDAO.getAreaList(), HttpStatus.OK);
    }
/*
    @GetMapping(path = "/channels")
    @ApiOperation(value = "获取所有channel信息")
    public ResponseEntity<List<ConfigEntity>> getChannels() {
        String tableName = "T_CONFIG_CHANNEL";
        return new ResponseEntity<List<ConfigEntity>>(configQueryDAO.getCommonConfig(tableName), HttpStatus.OK);
    }*/

    @GetMapping(path = "/sections")
    @ApiOperation(value = "获取所有栏目信息")
    public ResponseEntity<List<String>> getSections(@RequestParam String business){
        String tableName="T_CONFIG_"+business+"_SECTION";
        return new ResponseEntity<List<String>>(configQueryDAO.getSectionList(tableName),HttpStatus.OK);
    }

    @GetMapping(path = "/programs")
    @ApiOperation(value = "获取所有节目信息")
    public ResponseEntity<List<String>> getPrograms(@RequestParam String business){
        String tableName="T_CONFIG_"+business+"_PROGRAM";
        return new ResponseEntity<List<String>>(configQueryDAO.getProgramList(tableName),HttpStatus.OK);
    }

    @GetMapping(path = "/programsOfSection")
    @ApiOperation(value = "获取某个栏目下的所有节目")
    public ResponseEntity<List<String>> getProgramsOfSection(@RequestParam String business,@RequestParam String sectionName){
        return new ResponseEntity<List<String>>(configQueryDAO.getProgramsOfSection(business,sectionName),HttpStatus.OK);
    }

    @RequestMapping(path = "/areas2")
    public List<Map<String, Object>> areas() {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        Map provice = new HashMap();
        provice.put("id", 1);
        provice.put("name", "hunan");
        provice.put("parent_id", 0);
        provice.put("label", "湖南省");
        provice.put("type", "province");
        data.add(provice);

        Map city = new HashMap();
        city.put("id", 2);
        city.put("name", "cs");
        city.put("parent_id", 1);
        city.put("label", "长沙市");
        city.put("type", "city");
        data.add(city);

        Map project = new HashMap();
        project.put("id", 3);
        project.put("name", "proj");
        project.put("parent_id", 2);
        project.put("label", "长沙项目1");
        project.put("type", "project");
        data.add(project);

        return data;
    }

    @GetMapping(path = "/channelGroups")
    @ApiOperation(value = "获取频道组信息")
    public ResponseEntity<List<ChannelAndChannelGroupEntity>> getChannelGroup() {
        return new ResponseEntity(channelGroupQueryDAO.query(), HttpStatus.OK);
    }

    @GetMapping(path = "/channels")
    @ApiOperation(value = "获取频道信息")
    public ResponseEntity<List<ChannelAndChannelGroupEntity>> getChannel() {
        return new ResponseEntity(channelQueryDAO.query(), HttpStatus.OK);
    }
}
