package gags.service.entity;

import gags.service.util.BaseDataUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by zhangtao15 on 2017-06-14.
 */
public class Role {
    private Long roleId;
    private String roleName;
    private String roleCode;
    private Integer roleType;
    private Integer status;
    private Date createDate;
    private Date modifyDate;
    private Long opId;
    private Long orgId;
    private String remark;
    private String ext1;
    private String ext2;
    private String ext3;
    private Long operatorId;
    private Long parentId;
    private String parentName;
    private Long beginRow;
    private Long pageSize;
    private List<Role> childRoles;
    private List<Long>roleIds;

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

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public List<Role> getChildRoles() {
        return childRoles;
    }

    public void setChildRoles(List<Role> childRoles) {
        this.childRoles = childRoles;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getModifyDate() {
        if(modifyDate!=null) {
            return BaseDataUtil.formatDate(modifyDate,BaseDataUtil.DATE_TYPE_YYYY_MM_DD);
        }
        return null;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
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
}
