package gags.service.entity;

import java.util.Date;
import java.util.List;

/**
 * Created by jiahang Lee on 2017/6/21.
 */
public class RelOrgAuthObj {

    private Long relId;
    private Long roleId;
    private String organizeId;
    private Integer status;
    private Date createDate;
    private Date modifyDate;
    private String remark;
    private Long opId;
    private Long orgId;
    private List<String>areaIds;

    public List<String> getAreaIds() {
        return areaIds;
    }

    public void setAreaIds(List<String> areaIds) {
        this.areaIds = areaIds;
    }

    public Long getRelId() {
        return relId;
    }

    public void setRelId(Long relId) {
        this.relId = relId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
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

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getOrganizeId() {
        return organizeId;
    }

    public void setOrganizeId(String organizeId) {
        this.organizeId = organizeId;
    }

    @Override
    public String toString() {
        return "RelOrgAuthObj{" +
                "relId=" + relId +
                ", roleId=" + roleId +
                ", organizeId=" + organizeId +
                ", status=" + status +
                ", createDate=" + createDate +
                ", modifyDate=" + modifyDate +
                ", remark='" + remark + '\'' +
                ", opId=" + opId +
                ", orgId=" + orgId +
                '}';
    }
}
