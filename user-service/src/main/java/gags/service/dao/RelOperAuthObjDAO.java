package gags.service.dao;

import gags.service.entity.Organize;
import gags.service.entity.RelOperAuthObj;
import gags.service.util.BaseDataUtil;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by jiahang Lee on 2017/6/28.
 */
@Component
public class RelOperAuthObjDAO {
    private final SqlSession sqlSession;

    public RelOperAuthObjDAO(SqlSession sqlSession){
        this.sqlSession=sqlSession;
    }

    public List<RelOperAuthObj> list(RelOperAuthObj relOperAuthObj) {
        relOperAuthObj.setStatus(BaseDataUtil.STATUS_NORMAL);
        List<RelOperAuthObj> result=  this.sqlSession.selectList("relOperAuthObj.query",relOperAuthObj);
        return result;
    }

    public void insert(RelOperAuthObj relOperAuthObj) throws Exception{
        if(relOperAuthObj != null) {
            relOperAuthObj.setCreateDate(BaseDataUtil.getGlobalDate());
            relOperAuthObj.setStatus(BaseDataUtil.STATUS_NORMAL);
            this.sqlSession.insert("relOperAuthObj.insert", relOperAuthObj);
        }
    }

    public void update(RelOperAuthObj relOperAuthObj) throws Exception{
        if(relOperAuthObj != null) {
            relOperAuthObj.setModifyDate(BaseDataUtil.getGlobalDate());
            relOperAuthObj.setStatus(BaseDataUtil.STATUS_INVALID);
            this.sqlSession.update("relOperAuthObj.update", relOperAuthObj);
        }
    }
}
