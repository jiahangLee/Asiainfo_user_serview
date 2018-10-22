package gags.service.dao;

import gags.service.entity.Advice;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author <a href="mailto:wanggy6@asiainfo.com">wanggy6</a>
 * @version 1.0
 */
@Component
public class AdviceDAO {

    private final SqlSession sqlSession;

    public AdviceDAO(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public List<Advice> list(Advice advice) {
        List<Advice> result = this.sqlSession.selectList("advice.query", advice);
        return result;
    }
    public Integer listCount(Advice advice){
        List<Integer> result = this.sqlSession.selectList("advice.queryCount", advice);
        return result.get(0);
    }
    public void insert(Advice advice) throws Exception {
        if (advice != null) {
            this.sqlSession.insert("advice.insert", advice);
        }
    }
}
