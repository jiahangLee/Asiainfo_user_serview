package gags.service.dao;

import gags.service.entity.ConfigEntity;
import gags.service.entity.ConfigTreeEntity;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lyl on 2017/3/13.
 */

@Component
public class ConfigQueryDAO {
    private final SqlSession sqlSession;
    public ConfigQueryDAO(SqlSession sqlSession){
        this.sqlSession=sqlSession;
    }


    public List<ConfigTreeEntity> getAreaList(){
        List<ConfigTreeEntity> result=  this.sqlSession.selectList("config.areas");
        return result;
    }

    public List<ConfigEntity> getCommonConfig(String tableName){
        Map<String,String> map=new HashMap();
        map.put("tableName", tableName);
        List<ConfigEntity> result=  this.sqlSession.selectList("config.commonConfig", map);
        return result;
    }

    public List<String> getSectionList(String tableName){
        Map<String,String> map=new HashMap<>();
        map.put("tableName",tableName);
        List<String> result=sqlSession.selectList("config.sectionConfig",map);
        return result;
    }

    public List<String> getProgramList(String tableName){
        Map<String,String> map=new HashMap<>();
        map.put("tableName",tableName);
        List<String> result=sqlSession.selectList("config.programConfig",map);
        return result;
    }

    public List<String> getProgramsOfSection(String business,String sectionName){
        Map<String,String> map=new HashMap<>();
        map.put("business",business);
        map.put("sectionName",sectionName);
        List<String> result=sqlSession.selectList("config.programsOfSection",map);
        return result;
    }
}
