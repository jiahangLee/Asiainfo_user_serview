package gags.service.entity;

import java.util.List;

/**
 * Created by zhangtao15 on 2017-06-23.
 */
public class MenuTree implements Comparable<MenuTree>{
    private Long menuId;
    private Long parentId;
    private String key;
    private String name;
    private String icon;
    private String path;
    private List<MenuTree> children;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<MenuTree> getChildren() {
        return children;
    }

    public void setChildren(List<MenuTree> children) {
        this.children = children;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "MenuTree{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", path='" + path + '\'' +
                ", children=" + children +
                '}';
    }

    @Override
    public int compareTo(MenuTree menuTree) {
        return (this.menuId.intValue() - menuTree.getMenuId().intValue());
    }
}
