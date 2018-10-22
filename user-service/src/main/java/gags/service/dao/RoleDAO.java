package gags.service.dao;

import gags.service.entity.Role;
import gags.service.util.BaseDataUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangtao15 on 2017-06-15.
 */
@Component
public class RoleDAO {
    private final SqlSession sqlSession;

    public RoleDAO(SqlSession sqlSession){
        this.sqlSession=sqlSession;
    }

    public List<Role>listAll(Role role) {
        role.setStatus(BaseDataUtil.STATUS_NORMAL);
        List<Role> result=  this.sqlSession.selectList("role.queryRoles",role);
        return result;
    }

    public List<Role>listAuth(Role role) {
        role.setStatus(BaseDataUtil.STATUS_NORMAL);
        List<Role> result=  this.sqlSession.selectList("role.queryAuth",role);
        return result;
    }

    public List<Role>list(Role role) {
        role.setStatus(BaseDataUtil.STATUS_NORMAL);
        List<Role> result=  this.sqlSession.selectList("role.query",role);
        return result;
    }

    public Integer listCount(Role role) {
        role.setStatus(BaseDataUtil.STATUS_NORMAL);
        List<Integer> result=  this.sqlSession.selectList("role.queryCount",role);
        return result.get(0);
    }

    public Integer insert(Role role) throws Exception{
        if(role != null) {
            role.setCreateDate(BaseDataUtil.getGlobalDate());
            role.setStatus(BaseDataUtil.STATUS_NORMAL);
            return this.sqlSession.insert("role.insert", role);
        }
        return null;
    }

    public void update(Role role) throws Exception{
        if(role != null && !"".equals(role.getRoleId())) {
            role.setModifyDate(BaseDataUtil.getGlobalDate());
            this.sqlSession.update("role.update", role);
        }
    }

    public void delete(@Param("roleId")Long roleId) throws Exception{
        if(roleId != null) {
            Role role = new Role();
            role.setStatus(BaseDataUtil.STATUS_INVALID);
            role.setRoleId(roleId);
            this.update(role);
        }
    }
    
}
