package gags.service.entity;

import java.util.Date;
import java.util.List;

/**
 * Created by jiahang Lee on 2017/6/21.
 */
public class RelRoleMenu {

    private Long RelId;
    private Long RoleId;
    private Long MenuId;
    private Integer status;
    private Date createDate;
    private Date modifyDate;
    private String remark;
    private Long opId;
    private Long orgId;
    private List<Long> menuIds;

    public List<Long> getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(List<Long> menuIds) {
        this.menuIds = menuIds;
    }

    public Long getRelId() {
        return RelId;
    }

    public void setRelId(Long relId) {
        RelId = relId;
    }

    public Long getRoleId() {
        return RoleId;
    }

    public void setRoleId(Long roleId) {
        RoleId = roleId;
    }

    public Long getMenuId() {
        return MenuId;
    }

    public void setMenuId(Long menuId) {
        MenuId = menuId;
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
        return "RelRoleMenu{" +
                "RelId=" + RelId +
                ", RoleId=" + RoleId +
                ", MenuId=" + MenuId +
                ", status=" + status +
                ", createDate=" + createDate +
                ", modifyDate=" + modifyDate +
                ", remark='" + remark + '\'' +
                ", opId=" + opId +
                ", orgId=" + orgId +
                '}';
    }
}
