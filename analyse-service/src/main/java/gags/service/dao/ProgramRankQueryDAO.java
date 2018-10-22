package gags.service.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProgramRankQueryDAO {
    private final SqlSession sqlSession;
    public ProgramRankQueryDAO(SqlSession sqlSession){this.sqlSession=sqlSession;}
    public List<Map<String,Object>> query(String areaType,String business,String period,String sectionId, String whereStr, String fields){
        Map<String,String> map=new HashMap();
        map.put("region", areaType);
        map.put("business", business);
        map.put("period", period);
        map.put("sectionId", sectionId);
        map.put("whereStr", whereStr);
        map.put("fields",fields);
        System.out.println("select "+fields+",label from "+"t_"+areaType+"_"+business+"_program_"+period+" a,t_config_"+business+"_program b,t_config_"+business+"_section_program c"+" where "+whereStr+"and a.program_id=c.program_id and a.program_id=b.program_id and c.section_id='"+sectionId+"' ORDER BY orderTime");
        List<Map<String,Object>> result=  this.sqlSession.selectList("programRank.query", map);
        return result;
    }
}
