package gags.service.dao;

import gags.service.entity.RelOrgAuthObj;
import gags.service.util.BaseDataUtil;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by jiahang Lee on 2017/6/28.
 */
@Component
public class RelOrgAuthObjDAO {

    private final SqlSession sqlSession;

    public RelOrgAuthObjDAO(SqlSession sqlSession){
        this.sqlSession=sqlSession;
    }

    public List<RelOrgAuthObj> list(RelOrgAuthObj relOrgAuthObj) {
        relOrgAuthObj.setStatus(BaseDataUtil.STATUS_NORMAL);
        List<RelOrgAuthObj> result=  this.sqlSession.selectList("relOrgAuthObj.query",relOrgAuthObj);
        return result;
    }

    public void update(RelOrgAuthObj relOrgAuthObj) throws Exception{
        if(!StringUtils.isEmpty(relOrgAuthObj.getRoleId())) {
            relOrgAuthObj.setModifyDate(BaseDataUtil.getGlobalDate());
            relOrgAuthObj.setStatus(BaseDataUtil.STATUS_INVALID);
            this.sqlSession.update("relOrgAuthObj.update", relOrgAuthObj);
        }
    }

    public void insert(RelOrgAuthObj relOrgAuthObj) throws Exception{
        if(relOrgAuthObj != null) {
            relOrgAuthObj.setCreateDate(BaseDataUtil.getGlobalDate());
            relOrgAuthObj.setStatus(BaseDataUtil.STATUS_NORMAL);
            this.sqlSession.insert("relOrgAuthObj.insert", relOrgAuthObj);
        }
    }
}
