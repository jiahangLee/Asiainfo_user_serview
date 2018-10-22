package gags.service.entity;


import gags.service.util.BaseDataUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangzc Lee on 2018/1/31.
 */
public class DataReport {

    private Long report_id;
    private String organize_id;
    private String report_name;
    private String key_word;
    private String report_date;
    private String report_type;
    private String remark;
    private Long operator_id;
    private Date upload_date;
    private String file_name;
    private String file_save_name;
    private Long beginRow;
    private Long pageSize;
    private String operator_name;


    public Long getOperator_id() {
        return operator_id;
    }

    public void setOperator_id(Long operator_id) {
        this.operator_id = operator_id;
    }

    public String getOperator_name() {
        return operator_name;
    }

    public void setOperator_name(String operator_name) {
        this.operator_name = operator_name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    private Integer status;

    public Long getBeginRow() {
        return beginRow;
    }

    public void setBeginRow(Long beginRow) {
        this.beginRow = beginRow;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public Long getReport_id() {
        return report_id;
    }

    public void setReport_id(Long report_id) {
        this.report_id = report_id;
    }

    public String getOrganize_id() {
        return organize_id;
    }

    public void setOrganize_id(String organize_id) {
        this.organize_id = organize_id;
    }


    public String getKey_word() {
        return key_word;
    }

    public void setKey_word(String key_word) {
        this.key_word = key_word;
    }

    public String getReport_date() {
        return report_date;
    }

    public void setReport_date(String report_date) {
        this.report_date = report_date;
    }

    public String getReport_name() {
        return report_name;
    }

    public void setReport_name(String report_name) {
        this.report_name = report_name;
    }

    public String getReport_type() {
        return report_type;
    }

    public void setReport_type(String report_type) {
        this.report_type = report_type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }



    public String getUpload_date() {
        if(this.upload_date!=null) {
            return BaseDataUtil.formatDate(this.upload_date,BaseDataUtil.DATE_TYPE_YYYY_MM_DD_HHMMSS);
        }
        return null;
    }

    public void setUpload_date(Date upload_date) {
        this.upload_date = upload_date;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_save_name() {
        return file_save_name;
    }

    public void setFile_save_name(String file_save_name) {
        this.file_save_name = file_save_name;
    }


}
