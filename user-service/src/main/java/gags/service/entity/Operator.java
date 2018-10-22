package gags.service.entity;

import gags.service.util.BaseDataUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by jiahang Lee on 2017/6/14.
 */
public class Operator {
    private Long operatorId;
    private String operatorName;
    private String operatorCode;
    private Integer operatorType;
    private String  password;
    private Integer status;
    private String email;
    private String phone;
    private String tel;
    private Integer sex;
    private Date birthday;
    private String company;
    private String department;
    private String jobPosition;
    private Integer failNum;
    private Integer lockStatus;
    private Integer pwdStatus;
    private String lockNote;
    private Date createDate;
    private Date modifyDate;
    private Date startDate;
    private Date endDate;
    private Integer downloads;
    private String remark;
    private String ext1;
    private String ext2;
    private String ext3;
    private Long beginRow;
    private Long pageSize;
    private String roleName;
    private List<Long> roleId;

    public List<Long> getRoleId() {
        return roleId;
    }

    public void setRoleId(List<Long> roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

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

    public Long getOperatorId() {
        return operatorId;
    }
    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }
    public String getOperatorName() {
        return operatorName;
    }
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }
    public String getOperatorCode() {
        return operatorCode;
    }
    public void setOperatorCode(String operatorCode) {
        this.operatorCode = operatorCode;
    }
    public Integer getOperatorType() {
        return operatorType;
    }
    public void setOperatorType(Integer operatorType) {
        this.operatorType = operatorType;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getTel() {
        return tel;
    }
    public void setTel(String tel) {
        this.tel = tel;
    }
    public Integer getSex() {
        return sex;
    }
    public void setSex(Integer sex) {
        this.sex = sex;
    }
    public Date getBirthday() {
        return birthday;
    }
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
    public String getCompany() {
        return company;
    }
    public void setCompany(String company) {
        this.company = company;
    }
    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getFailNum() {
        return failNum;
    }
    public void setFailNum(Integer failNum) {
        this.failNum = failNum;
    }
    public Integer getLockStatus() {
        return lockStatus;
    }
    public void setLockStatus(Integer lockStatus) {
        this.lockStatus = lockStatus;
    }
    public Integer getPwdStatus() {
        return pwdStatus;
    }
    public void setPwdStatus(Integer pwdStatus) {
        this.pwdStatus = pwdStatus;
    }

    public String getLockNote() {
        return lockNote;
    }

    public void setLockNote(String lockNote) {
        this.lockNote = lockNote;
    }

    public String getCreateDate() {
        if(createDate!=null) {
            return BaseDataUtil.formatDate(createDate,BaseDataUtil.DATE_TYPE_YYYY_MM_DD);
        }
        return null;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public Date getModifyDate() {
        return modifyDate;
    }
    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }
    public String getStartDate() {
        if(startDate!=null) {
            return BaseDataUtil.formatDate(startDate,BaseDataUtil.DATE_TYPE_YYYY_MM_DD);
        }
        return null;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public String getEndDate() {
        if(endDate!=null) {
            return BaseDataUtil.formatDate(endDate,BaseDataUtil.DATE_TYPE_YYYY_MM_DD);
        }
        return null;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public Integer getDownloads() {
        return downloads;
    }
    public void setDownloads(Integer downloads) {
        this.downloads = downloads;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getExt1() {
        return ext1;
    }
    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }
    public String getExt2() {
        return ext2;
    }
    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }
    public String getExt3() {
        return ext3;
    }
    public void setExt3(String ext3) {
        this.ext3 = ext3;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
    }

    @Override
    public String toString() {
        return "Operator{" +
                "operatorId=" + operatorId +
                ", operatorName='" + operatorName + '\'' +
                ", operatorCode='" + operatorCode + '\'' +
                ", operatorType=" + operatorType +
                ", password='" + password + '\'' +
                ", status=" + status +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", tel='" + tel + '\'' +
                ", sex=" + sex +
                ", birthday=" + birthday +
                ", company='" + company + '\'' +
                ", department='" + department + '\'' +
                ", jobPosition='" + jobPosition + '\'' +
                ", failNum=" + failNum +
                ", lockStatus=" + lockStatus +
                ", pwdStatus=" + pwdStatus +
                ", lockNote='" + lockNote + '\'' +
                ", createDate=" + createDate +
                ", modifyDate=" + modifyDate +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", downloads=" + downloads +
                ", remark='" + remark + '\'' +
                ", EXT1='" + ext1 + '\'' +
                ", EXT2='" + ext2 + '\'' +
                ", EXT3='" + ext3 + '\'' +
                '}';
    }
}