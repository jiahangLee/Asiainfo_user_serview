package gags.service.entity;


import java.util.Date;

/**
 * Created by zhangtao on 2018/5/17.
 */
public class UserUseLog {

    private Long id;
    private String areaCode;
    private String biz;
    private String bizSubtype;
    private String userName;
    private Integer loginCount;
    private Integer browsePageCount;
    private Integer dataDownCount;
    private Integer picDownCount;
    private Date createDate;
    private String ext1;
    private String ext2;
    private String ext3;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getBiz() {
        return biz;
    }

    public void setBiz(String biz) {
        this.biz = biz;
    }

    public String getBizSubtype() {
        return bizSubtype;
    }

    public void setBizSubtype(String bizSubtype) {
        this.bizSubtype = bizSubtype;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    public Integer getBrowsePageCount() {
        return browsePageCount;
    }

    public void setBrowsePageCount(Integer browsePageCount) {
        this.browsePageCount = browsePageCount;
    }

    public Integer getDataDownCount() {
        return dataDownCount;
    }

    public void setDataDownCount(Integer dataDownCount) {
        this.dataDownCount = dataDownCount;
    }

    public Integer getPicDownCount() {
        return picDownCount;
    }

    public void setPicDownCount(Integer picDownCount) {
        this.picDownCount = picDownCount;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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

    @Override
    public String toString() {
        return "UserUseLog{" +
                "id=" + id +
                ", areaCode='" + areaCode + '\'' +
                ", biz='" + biz + '\'' +
                ", bizSubtype='" + bizSubtype + '\'' +
                ", userName='" + userName + '\'' +
                ", loginCount=" + loginCount +
                ", browsePageCount=" + browsePageCount +
                ", dataDownCount=" + dataDownCount +
                ", picDownCount=" + picDownCount +
                ", createDate=" + createDate +
                ", ext1='" + ext1 + '\'' +
                ", ext2='" + ext2 + '\'' +
                ", ext3='" + ext3 + '\'' +
                '}';
    }

    public UserUseLog() {
        super();
    }

    public UserUseLog(String areaCode, String biz, String bizSubtype, String userName, Integer loginCount, Integer browsePageCount, Integer dataDownCount) {
        this.areaCode = areaCode;
        this.biz = biz;
        this.bizSubtype = bizSubtype;
        this.userName = userName;
        this.loginCount = loginCount;
        this.browsePageCount = browsePageCount;
        this.dataDownCount = dataDownCount;
    }
}
