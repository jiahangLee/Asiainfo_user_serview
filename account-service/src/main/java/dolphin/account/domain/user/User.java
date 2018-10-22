package dolphin.account.domain.user;


import dolphin.account.data.BaseEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class User extends BaseEntity {

    @Id
    private int id;


    @Column(unique = true, nullable = false)
    private String name;

    @Column
    private String label;

    //@Column
    private String password;
    @Column
    private String email;

    @Column
    private String phone;

    @Column
    private String photo;

    @Column
    private String workNum;

    @Column
    private boolean isAdmin;

    @Column
    private boolean enable;

    @Column(length = 255)
    private String remark;

    @Column
    private int orgId;



    //职位
    @Column
    private int duty;


    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Integer> roleIds = new HashSet<Integer>();

    public User() {
    }
    public User(User user){
        this.setAdmin(user.isAdmin());
        this.setName(user.getName());
        this.setDuty(user.getDuty());
        this.setEmail(user.getEmail());
        this.setEnable(user.isEnable());
        this.setId(user.getId());
        this.setLabel(user.getLabel());
        this.setOrgId(user.getOrgId());
        this.setPassword(user.getPassword());
        this.setPhone(user.getPhone());
        this.setRemark(user.getRemark());
        this.setRoleIds(user.getRoleIds());
        this.setRemark(user.getRemark());
        this.setCreatedAt(user.getCreatedAt());
        this.setLastModified(user.getLastModified());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getWorkNum() {
        return workNum;
    }

    public void setWorkNum(String workNum) {
        this.workNum = workNum;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public int getDuty() {
        return duty;
    }

    public void setDuty(int duty) {
        this.duty = duty;
    }

    public Set<Integer> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Set<Integer> roleIds) {
        this.roleIds = roleIds;
    }
}
