package gags.service.dao;


import gags.service.entity.DataReportLog;
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
public class DataReportLogDAO {

    private final SqlSession sqlSession;

    public DataReportLogDAO(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }


    public List<DataReportLog> list(DataReportLog dataReportLog) {
        dataReportLog.setUpdateDate(new Date(DateUtil.getToday(new Date())*1000));
        List<DataReportLog> result = this.sqlSession.selectList("dataReportLog.queryDataReportLog", dataReportLog);
        return result;
    }

    public List<DataReportLog> query(String operatorId, String reportId) {
        DataReportLog dataReportLog = new DataReportLog();
        dataReportLog.setOperatorId(operatorId);
        dataReportLog.setReportId(reportId);
        dataReportLog.setUpdateDate(new Date(DateUtil.getToday(new Date())*1000));
        List<DataReportLog> result = this.sqlSession.selectList("dataReportLog.queryDataReportLog", dataReportLog);
        return result;
    }

    public void insert(DataReportLog dataReportLog) throws Exception {
        if (dataReportLog != null) {
            dataReportLog.setUpdateDate(BaseDataUtil.getGlobalDate());
            dataReportLog.setCreateDate(BaseDataUtil.getGlobalDate());
            dataReportLog.setLockStatus(BaseDataUtil.STATUS_NORMAL);
            this.sqlSession.insert("dataReportLog.insert", dataReportLog);
        }
    }

    public void update(DataReportLog dataReportLog) throws Exception {
        if (dataReportLog != null && !"".equals(dataReportLog.getId())) {
            dataReportLog.setUpdateDate(BaseDataUtil.getGlobalDate());
            this.sqlSession.update("dataReportLog.update", dataReportLog);
        }
    }

}
