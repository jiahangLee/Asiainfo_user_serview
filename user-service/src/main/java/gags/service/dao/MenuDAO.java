package gags.service.dao;

import gags.service.entity.Menu;
import gags.service.util.BaseDataUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangtao15 on 2017/6/14.
 */
@Component
public class MenuDAO {
    private final SqlSession sqlSession;

    public MenuDAO(SqlSession sqlSession){
        this.sqlSession=sqlSession;
    }

    public List<Menu> query(Menu menu){
        menu.setStatus(BaseDataUtil.STATUS_NORMAL);
        List<Menu> result=  this.sqlSession.selectList("menu.queryMenus",menu);
        return result;
    }

    public List<Menu> list(Menu menu){
        menu.setStatus(BaseDataUtil.STATUS_NORMAL);
        List<Menu> result=  this.sqlSession.selectList("menu.query",menu);
        return result;
    }

    public void insert(Menu menu) throws Exception{
        if(menu != null) {
            menu.setCreateDate(BaseDataUtil.getGlobalDate());
            menu.setStatus(BaseDataUtil.STATUS_NORMAL);
            this.sqlSession.insert("menu.insert", menu);
        }
    }

    public void update(Menu menu) throws Exception{
        if(menu != null && ! "".equals(menu.getMenuId())) {
            menu.setModifyDate(BaseDataUtil.getGlobalDate());
            this.sqlSession.update("menu.update", menu);
        }
    }

    public void delete(@Param("menuId")Long menuId) throws Exception{
        if(menuId != null) {
            Menu menu = new Menu();
            menu.setStatus(BaseDataUtil.STATUS_INVALID);
            menu.setMenuId(menuId);
            this.update(menu);
        }
    }


}
