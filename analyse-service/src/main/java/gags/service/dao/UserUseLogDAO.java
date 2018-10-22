package gags.service.dao;


import gags.service.entity. UserUseLog;
import gags.service.util.BaseDataUtil;
import gags.service.util.DateUtil;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by zhangtao on 2018/15/22.
 */
@Component
public class UserUseLogDAO {

    private final SqlSession sqlSession;

    public UserUseLogDAO(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }


    public List< UserUseLog> list( UserUseLog  userUseLog) {
        List< UserUseLog> result = this.sqlSession.selectList("userUseLog.queryUserUseLog",  userUseLog);
        return result;
    }

    public List< UserUseLog> query(String operatorId, String reportId) {
         UserUseLog  userUseLog = new  UserUseLog();
        List< UserUseLog> result = this.sqlSession.selectList("userUseLog.queryUserUseLog",  userUseLog);
        return result;
    }

    public void insert( UserUseLog  userUseLog) throws Exception {
        if ( userUseLog != null) {
//             userUseLog.setCreateDate(BaseDataUtil.getGlobalDate());
            this.sqlSession.insert("userUseLog.insert",  userUseLog);
        }
    }


}
