package gags.service.entity;

import java.util.List;

/**
 * Created by zhangtao15 on 2017-06-26.
 */
public class AuthTree {
    private Long treeId;
    private Long parentId;
    private String key;
    private String title;
    private Integer level;
    private List<AuthTree> child;

    public Long getTreeId() {
        return treeId;
    }

    public void setTreeId(Long treeId) {
        this.treeId = treeId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public List<AuthTree> getChild() {
        return child;
    }

    public void setChild(List<AuthTree> child) {
        this.child = child;
    }

    @Override
    public String toString() {
        return "AuthTree{" +
                "treeId=" + treeId +
                ", parentId=" + parentId +
                ", key='" + key + '\'' +
                ", title='" + title + '\'' +
                ", level=" + level +
                ", child=" + child +
                '}';
    }
}
