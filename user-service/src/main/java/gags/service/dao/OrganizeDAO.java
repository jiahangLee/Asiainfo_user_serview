package gags.service.dao;

import gags.service.entity.AreaConfig;
import gags.service.entity.Menu;
import gags.service.entity.Organize;
import gags.service.util.BaseDataUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by jiahang Lee on 2017/6/16.
 */
@Component
public class OrganizeDAO {

    private final SqlSession sqlSession;

    public OrganizeDAO(SqlSession sqlSession){
        this.sqlSession=sqlSession;
    }

    public List<AreaConfig>listArea(AreaConfig areaConfig) {
        areaConfig.setStatus(BaseDataUtil.STATUS_NORMAL);
        List<AreaConfig> result=  this.sqlSession.selectList("areaConfig.query",areaConfig);
        return result;
    }
    public List<AreaConfig>listAll(AreaConfig areaConfig) {
        areaConfig.setStatus(BaseDataUtil.STATUS_NORMAL);
        List<AreaConfig> result=  this.sqlSession.selectList("areaConfig.queryArea",areaConfig);
        return result;
    }

    public List<Organize>list(Organize organize) {
        organize.setStatus(BaseDataUtil.STATUS_NORMAL);
        List<Organize> result=  this.sqlSession.selectList("organize.query",organize);
        return result;
    }

    public void insert(Organize organize) throws Exception{
        if(organize != null) {
            organize.setCreateDate(BaseDataUtil.getGlobalDate());
            organize.setStatus(BaseDataUtil.STATUS_NORMAL);
            this.sqlSession.insert("organize.insert", organize);
        }
    }
    public void update(Organize organize) throws Exception{
        if(organize != null && !"".equals(organize.getOrganizeId())) {
            organize.setModifyDate(BaseDataUtil.getGlobalDate());
            this.sqlSession.update("organize.update", organize);
        }
    }
    public void delete(@Param("organizeId")Long organizeId) throws Exception{
        if(organizeId != null) {
            Organize organize = new Organize();
            organize.setStatus(BaseDataUtil.STATUS_INVALID);
            organize.setOrganizeId(organizeId);
            this.update(organize);
        }
    }
}
