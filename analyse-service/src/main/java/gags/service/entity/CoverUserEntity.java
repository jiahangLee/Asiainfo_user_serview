package gags.service.entity;

/**
 * Created by zhangzc on 2017/3/13.
 */
public class CoverUserEntity {
    private int cover_user;
    private String area_name;
    private String statistic_time;
    private String business_type;
    private int new_user;
    private String add_user;

    public int getCover_user() {
        return cover_user;
    }

    public void setCover_user(int cover_user) {
        this.cover_user = cover_user;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public String getStatistic_time() {
        return statistic_time;
    }

    public void setStatistic_time(String statistic_time) {
        this.statistic_time = statistic_time;
    }

    public String getBusiness_type() {
        return business_type;
    }

    public void setBusiness_type(String business_type) {
        this.business_type = business_type;
    }

    public int getNew_user() {
        return new_user;
    }

    public void setNew_user(int new_user) {
        this.new_user = new_user;
    }

    public String getAdd_user() {
        return add_user;
    }

    public void setAdd_user(String add_user) {
        this.add_user = add_user;
    }


    @Override
    public String toString() {
        return "CoverUserEntity{" +
                "cover_user=" + cover_user +
                ", area_name='" + area_name + '\'' +
                ", statistic_time='" + statistic_time + '\'' +
                ", business_type='" + business_type + '\'' +
                ", new_user=" + new_user +
                ", add_user='" + add_user + '\'' +
                '}';
    }
}
