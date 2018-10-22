package gags.service.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lyl on 2017/2/24.
 */

@Component
public class StatisticQueryDAO {

    private final SqlSession sqlSession;
    public StatisticQueryDAO(SqlSession sqlSession){
        this.sqlSession=sqlSession;
    }

    public List<Map<String,Object>> query(String tableName,String whereStr,String fields){
        Map<String,String> map=new HashMap();
        map.put("tableName", tableName);
        map.put("whereStr", whereStr);
        map.put("fields",fields);
        System.out.println("select "+fields+" from "+tableName+" where "+whereStr+" ORDER BY orderTime");
        /*
        if(pageNum!=null && pageSize!=null ){
            PageHelper.startPage(pageNum, pageSize);
        }
        */
        List<Map<String,Object>> result=  this.sqlSession.selectList("common.query", map);
       // PageInfo<List<Map<String,Object>>> info=new PageInfo(result);

        return result;
    }
    public List<Map<String,Object>> queryRank(String tableName,String whereStr,String fields){
        Map<String,String> map=new HashMap();
        map.put("tableName", tableName);
        map.put("whereStr", whereStr);
        map.put("fields",fields);
        /*
        if(pageNum!=null && pageSize!=null ){
            PageHelper.startPage(pageNum, pageSize);
        }
        */

        List<Map<String,Object>> result=  this.sqlSession.selectList("common.queryRank", map);
        // PageInfo<List<Map<String,Object>>> info=new PageInfo(result);

        return result;
    }

    /**
     *
     * @param tableName
     * @param whereStr
     * @param fields
     * @return
     */
    public List<Map<String,Object>> queryStock(String tableName,String whereStr,String fields){
        Map<String,String> map=new HashMap();
        map.put("tableName", tableName);
        map.put("whereStr", whereStr);
        map.put("fields",fields);

        System.out.println("select "+fields+" from "+tableName+" where "+whereStr);
        List<Map<String,Object>> result=  this.sqlSession.selectList("common.queryStock", map);

        return result;
    }

    /**
     *
     * @param tableName
     * @param whereStr
     * @param fields
     * @return 返回帮助检索指标释义、计算公式、粒度
     */
    public List<Map<String,Object>> queryHelpSearch(String tableName,String whereStr,String fields){
        Map<String,String> map=new HashMap();
        map.put("tableName", tableName);
        map.put("whereStr", whereStr);
        map.put("fields",fields);

        System.out.println("select "+fields+" from "+tableName+" where "+whereStr);
        List<Map<String,Object>> result=  this.sqlSession.selectList("common.queryHelpSearch", map);

        return result;
    }
}
