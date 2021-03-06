package gags.service.dao;

import gags.service.entity.ChannelAndChannelGroupEntity;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuyj
 * @description
 * @create 2017/7/28
 */
@Component
public class ChannelGroupQueryDAO {
    public static final String TABLE_NAME = "t_config_channel_group";

    private final SqlSession sqlSession;

    public ChannelGroupQueryDAO(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public List<ChannelAndChannelGroupEntity> query() {
        Map<String, String> map = new HashMap();
        map.put("tableName", TABLE_NAME);
        List<ChannelAndChannelGroupEntity> result = this.sqlSession.selectList("config.queryChannelGroup", map);
        return result;
    }
}
