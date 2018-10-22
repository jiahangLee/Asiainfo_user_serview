package gags.service.entity;

/**
 * Created by zhangtao15 on 2017-07-20.
 */
public class CoordMapEntity {
    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public CoordMapEntity() {
        super();
    }

    public CoordMapEntity(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return "CoordMapEntity{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
