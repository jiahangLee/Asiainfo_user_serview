package gags.service.dao;

import gags.service.entity.RelRoleMenu;
import gags.service.util.BaseDataUtil;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by jiahang Lee on 2017/6/28.
 */
@Component
public class RelRoleMenuDAO {

    private final SqlSession sqlSession;

    public RelRoleMenuDAO(SqlSession sqlSession){
        this.sqlSession=sqlSession;
    }

    public List<RelRoleMenu> list(RelRoleMenu relRoleMenu) {
        relRoleMenu.setStatus(BaseDataUtil.STATUS_NORMAL);
        List<RelRoleMenu> result=  this.sqlSession.selectList("relRoleMenu.query",relRoleMenu);
        return result;
    }

    public void update(RelRoleMenu relRoleMenu) throws Exception{
        if(!StringUtils.isEmpty(relRoleMenu.getRoleId())) {
            relRoleMenu.setModifyDate(BaseDataUtil.getGlobalDate());
            relRoleMenu.setStatus(BaseDataUtil.STATUS_INVALID);
            this.sqlSession.update("relRoleMenu.update", relRoleMenu);
        }
    }

    public void insert(RelRoleMenu relRoleMenu) throws Exception{
        if(relRoleMenu != null) {
            relRoleMenu.setCreateDate(BaseDataUtil.getGlobalDate());
            relRoleMenu.setStatus(BaseDataUtil.STATUS_NORMAL);
            this.sqlSession.insert("relRoleMenu.insert", relRoleMenu);
        }
    }
}
