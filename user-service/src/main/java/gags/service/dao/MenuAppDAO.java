package gags.service.dao;

import gags.service.entity.MenuApp;
import gags.service.util.BaseDataUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by zhangtao15 on 2017/6/14.
 */
@Component
public class MenuAppDAO {
    private final SqlSession sqlSession;

    public MenuAppDAO(SqlSession sqlSession){
        this.sqlSession=sqlSession;
    }

    public List<MenuApp> query(MenuApp menu){
        menu.setStatus(BaseDataUtil.STATUS_NORMAL);
        List<MenuApp> result=  this.sqlSession.selectList("menu_app.queryMenus",menu);
        return result;
    }

    public List<MenuApp> list(MenuApp menu){
        menu.setStatus(BaseDataUtil.STATUS_NORMAL);
        List<MenuApp> result=  this.sqlSession.selectList("menu_app.query",menu);
        return result;
    }

    public void insert(MenuApp menu) throws Exception{
        if(menu != null) {
            menu.setCreateDate(BaseDataUtil.getGlobalDate());
            menu.setStatus(BaseDataUtil.STATUS_NORMAL);
            this.sqlSession.insert("menu_app.insert", menu);
        }
    }

    public void update(MenuApp menu) throws Exception{
        if(menu != null && ! "".equals(menu.getMenuId())) {
            menu.setModifyDate(BaseDataUtil.getGlobalDate());
            this.sqlSession.update("menu_app.update", menu);
        }
    }

    public void delete(@Param("menuId")Long menuId) throws Exception{
        if(menuId != null) {
            MenuApp menu = new MenuApp();
            menu.setStatus(BaseDataUtil.STATUS_INVALID);
            menu.setMenuId(menuId);
            this.update(menu);
        }
    }


}
