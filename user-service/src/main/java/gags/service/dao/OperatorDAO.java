package gags.service.dao;

import gags.service.entity.Menu;
import gags.service.entity.Operator;

import gags.service.util.BaseDataUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by jiahang Lee on 2017/6/16.
 */
@Component
public class OperatorDAO {

    private final SqlSession sqlSession;

    public OperatorDAO(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public List<Operator> queryOperator(Operator operator) {
        operator.setStatus(BaseDataUtil.STATUS_NORMAL);
        List<Operator> result = this.sqlSession.selectList("operator.queryOperator", operator);
        return result;
    }

    public List<Operator> list(Operator operator) {
        operator.setStatus(BaseDataUtil.STATUS_NORMAL);
        List<Operator> result = this.sqlSession.selectList("operator.query", operator);
        return result;
    }

    public Integer listCount(Operator operator){
        operator.setStatus(BaseDataUtil.STATUS_NORMAL);
        List<Integer> result = this.sqlSession.selectList("operator.queryCount", operator);
        return result.get(0);
    }

    public void insert(Operator operator) throws Exception {
        if (operator != null) {
            operator.setCreateDate(BaseDataUtil.getGlobalDate());
            operator.setStartDate(BaseDataUtil.getGlobalDate());
            operator.setStatus(BaseDataUtil.STATUS_NORMAL);
            this.sqlSession.insert("operator.insert", operator);
        }
    }

    public void update(Operator operator) throws Exception {
        if (operator != null && !"".equals(operator.getOperatorId())) {
            operator.setModifyDate(BaseDataUtil.getGlobalDate());
            this.sqlSession.update("operator.update", operator);
        }
    }

    public void delete(@Param("operatorId") Long operatorId) throws Exception {
        if (operatorId != null) {
            Operator operator = new Operator();
            operator.setStatus(BaseDataUtil.STATUS_INVALID);
            operator.setModifyDate(BaseDataUtil.getGlobalDate());
            operator.setOperatorId(operatorId);
            this.update(operator);
        }
    }
}
