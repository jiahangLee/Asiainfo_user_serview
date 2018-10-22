package gags.service.entity;

/**
 * Created by jiahang Lee on 2017/7/28.
 */
public class FlowTrendEntity {

    private String pageId;
    private String pageName;
    private String activeUser;
    private String activeUserRatio;
    private String useCount;
    private String timeInUse;
    private String insertDt;
    private String parentId;

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getActiveUser() {
        return activeUser;
    }

    public void setActiveUser(String activeUser) {
        this.activeUser = activeUser;
    }

    public String getActiveUserRatio() {
        return activeUserRatio;
    }

    public void setActiveUserRatio(String activeUserRatio) {
        this.activeUserRatio = activeUserRatio;
    }

    public String getUseCount() {
        return useCount;
    }

    public void setUseCount(String useCount) {
        this.useCount = useCount;
    }

    public String getTimeInUse() {
        return timeInUse;
    }

    public void setTimeInUse(String timeInUse) {
        this.timeInUse = timeInUse;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
