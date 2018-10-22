package gags.service.entity;

import java.util.Date;

/**
 * Created by jiahang Lee on 2017/6/21.
 */
public class RelOperAuthObj {

    private Long relId;
    private Long operatorId;
    private Long roleId;
    private Integer status;
    private Date createDate;
    private Date modifyDate;
    private String remark;
    private Long opId;
    private Long orgId;

    public Long getRelId() {
        return relId;
    }

    public void setRelId(Long relId) {
        this.relId = relId;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
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

    @Override
    public String toString() {
        return "RelOperAuthObj{" +
                "relId=" + relId +
                ", operatorId=" + operatorId +
                ", roleId=" + roleId +
                ", status=" + status +
                ", createDate=" + createDate +
                ", modifyDate=" + modifyDate +
                ", remark='" + remark + '\'' +
                ", opId=" + opId +
                ", orgId=" + orgId +
                '}';
    }
}
