package gags.service.util;

import gags.service.entity.AuthTree;
import gags.service.entity.MenuApp;
import gags.service.entity.MenuTree;
import gags.service.entity.Role;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangtao15 on 2017-06-27.
 */
public class CommonTreeUtil {

    public static List<AuthTree> getChilAuthTree(Long treeId,List<AuthTree> rootList) {
        // 子目录
        List<AuthTree> childList = new ArrayList<>();
        for (AuthTree authTree : rootList) {
            // 遍历所有节点，将父目录id与传过来的id比较
            if (authTree.getParentId() >= 0) {
                if (treeId.equals(authTree.getParentId())) {
                    childList.add(authTree);
                }
            }
        }
        // 递归循环子目录
        for (AuthTree authTree : childList) {
            // 递归
            authTree.setChild(getChilAuthTree(authTree.getTreeId(), rootList));
        }
        // 递归退出条件
        if (childList.size() == 0) {
            return null;
        }
        return childList;
    }

    public static List<MenuTree> getChilMenus(Long menuId, List<MenuTree> rootList) {
        // 子目录
        List<MenuTree> childList = new ArrayList<>();
        for (MenuTree mnu : rootList) {
            // 遍历所有节点，将父目录id与传过来的id比较
            if (mnu.getParentId() >= 0) {
                if (menuId.equals(mnu.getParentId())) {
                    childList.add(mnu);
                }
            }
        }
        // 递归循环子目录
        for (MenuTree mnu : childList) {
            // 递归
            mnu.setChildren(getChilMenus(mnu.getMenuId(), rootList));
        }
        // 递归退出条件
        if (childList.size() == 0) {
            return null;
        }
        return childList;
    }

    public static List<MenuApp> getChilMenusApp(Long menuId, List<MenuApp> rootList) {
        // 子目录
        List<MenuApp> childList = new ArrayList<>();
        for (MenuApp mnu : rootList) {
            // 遍历所有节点，将父目录id与传过来的id比较
            if (mnu.getParentId() >= 0) {
                if (menuId.equals(mnu.getParentId())) {
                    childList.add(mnu);
                }
            }
        }
        // 递归循环子目录
        for (MenuApp mnu : childList) {
            // 递归
            mnu.setMenus(getChilMenusApp(mnu.getMenuId(), rootList));
        }
        // 递归退出条件
        if (childList.size() == 0) {
            return null;
        }
        return childList;
    }

    public static List<Role> getChildRoles(Long roleId, List<Role> rootList) {
        // 子目录
        List<Role> childList = new ArrayList<>();
        for (Role role : rootList) {
            // 遍历所有节点，将父目录id与传过来的id比较
            if (roleId.equals(role.getParentId())) {
                childList.add(role);
            }
        }
        // 递归循环子目录
        for (Role role : childList) {
            // 递归
            role.setChildRoles(getChildRoles(role.getRoleId(), rootList));
        }
        // 递归退出条件
        if (childList.size() == 0) {
            return null;
        }
        return childList;
    }
}
