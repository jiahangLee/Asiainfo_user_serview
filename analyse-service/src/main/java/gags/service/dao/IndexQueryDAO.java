package gags.service.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiahang Lee on 2017/7/19.
 */
@Component
public class IndexQueryDAO {

    private final SqlSession sqlSession;
    public IndexQueryDAO(SqlSession sqlSession){
        this.sqlSession=sqlSession;
    }

    public List<Map<String,Object>> query(String tableName, String whereStr, String fields){
        Map<String,String> map=new HashMap();
        map.put("tableName", tableName);
        map.put("whereStr", whereStr);
        map.put("fields",fields);
        /*
        if(pageNum!=null && pageSize!=null ){
            PageHelper.startPage(pageNum, pageSize);
        }
        */

        List<Map<String,Object>> result=  this.sqlSession.selectList("common.query", map);
        // PageInfo<List<Map<String,Object>>> info=new PageInfo(result);

        return result;
    }
}
