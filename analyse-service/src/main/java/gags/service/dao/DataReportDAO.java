package gags.service.dao;


import gags.service.entity.DataReport;
import gags.service.util.BaseDataUtil;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by zhangzc Lee on 2018/1/31.
 */
@Component
public class DataReportDAO {

    private final SqlSession sqlSession;

    public DataReportDAO(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }


    public List<DataReport> list(DataReport dataReport) {
        dataReport.setStatus(BaseDataUtil.STATUS_NORMAL);
        List<DataReport> result = this.sqlSession.selectList("dataReport.queryDataReport", dataReport);
        return result;
    }
    public DataReport getDataReportByReportId(Long reportId){
        DataReport dataReport = new DataReport();
        dataReport.setReport_id(reportId);
        List<DataReport> result  = this.list(dataReport);
        if(result != null && result.size() >0){
            return result.get(0);
        }
        return null;
    }
    public Integer listCount(DataReport dataReport){
        dataReport.setStatus(BaseDataUtil.STATUS_NORMAL);
        List<Integer> result = this.sqlSession.selectList("dataReport.queryCount", dataReport);
        return result.get(0);
    }
    public void insert(DataReport dataReport) throws Exception {
       if (dataReport != null) {
           dataReport.setUpload_date(BaseDataUtil.getGlobalDate());
           this.sqlSession.insert("dataReport.insert", dataReport);
       }
   }
    public void update(DataReport dataReport) throws Exception {
        if (dataReport != null && !"".equals(dataReport.getReport_id())) {
            this.sqlSession.update("dataReport.update", dataReport);
        }
    }

    public void delete( Long reportId) throws Exception {
        if (reportId != null) {
            this.sqlSession.update("dataReport.delete", reportId);
        }
    }
}
