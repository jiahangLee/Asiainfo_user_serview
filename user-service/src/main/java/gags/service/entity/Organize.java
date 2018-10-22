package gags.service.entity;

import java.util.Date;
/**
 * Created by jiahang Lee on 2017/6/14.
 */
public class Organize {
    private Long organizeId;
    private String organizeName;
    private String organizeCode;
    private Long parentId;
    private Integer organizeType;
    private String organizePath;
    private Integer status;
    private String remark;
    private Date createDate;
    private Date modifyDate;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    private Long opId;
    private Long orgId;
    private String EXT1;
    private String EXT2;
    private String EXT3;
    public Long getOrganizeId() {
        return organizeId;
    }
    public void setOrganizeId(Long organizeId) {
        this.organizeId = organizeId;
    }
    public String getOrganizeName() {
        return organizeName;
    }
    public void setOrganizeName(String organizeName) {
        this.organizeName = organizeName;
    }
    public String getOrganizeCode() {
        return organizeCode;
    }
    public void setOrganizeCode(String organizeCode) {
        this.organizeCode = organizeCode;
    }
    public Long getParentId() {
        return parentId;
    }
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    public Integer getOrganizeType() {
        return organizeType;
    }
    public void setOrganizeType(Integer organizeType) {
        this.organizeType = organizeType;
    }
    public String getOrganizePath() {
        return organizePath;
    }
    public void setOrganizePath(String organizePath) {
        this.organizePath = organizePath;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getOpId() {
        return opId;
    }
    public void setOpId(Long opId) {
        this.opId = opId;
    }
    public Long getOrgId() {
        return orgId;
    }
    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }
    public String getEXT1() {
        return EXT1;
    }
    public void setEXT1(String EXT1) {
        this.EXT1 = EXT1;
    }
    public String getEXT2() {
        return EXT2;
    }
    public void setEXT2(String EXT2) {
        this.EXT2 = EXT2;
    }
    public String getEXT3() {
        return EXT3;
    }
    public void setEXT3(String EXT3) {
        this.EXT3 = EXT3;
    }

    @Override
    public String toString() {
        return "Organize{" +
                "organizeId=" + organizeId +
                ", organizeName='" + organizeName + '\'' +
                ", organizeCode='" + organizeCode + '\'' +
                ", parentId=" + parentId +
                ", organizeType=" + organizeType +
                ", organizePath='" + organizePath + '\'' +
                ", status=" + status +
                ", remark='" + remark + '\'' +
                ", createDate=" + createDate +
                ", modifyDate=" + modifyDate +
                ", opId=" + opId +
                ", orgId=" + orgId +
                ", EXT1='" + EXT1 + '\'' +
                ", EXT2='" + EXT2 + '\'' +
                ", EXT3='" + EXT3 + '\'' +
                '}';
    }
}