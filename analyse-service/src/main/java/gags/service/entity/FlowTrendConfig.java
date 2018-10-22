package gags.service.entity;

/**
 * Created by zhangtao on 2017-08-10.
 */
public class FlowTrendConfig {
    private String name;
    private String value;
    private String symbolSize;
    private String category;
    private String source;
    private String target;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSymbolSize() {
        return symbolSize;
    }

    public void setSymbolSize(String symbolSize) {
        this.symbolSize = symbolSize;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public FlowTrendConfig(String name, String source, String target) {
        this.name = name;

        this.source = source;
        this.target = target;
    }

    public FlowTrendConfig() {
        super();
    }

    @Override
    public String toString() {
        return "FlowTrendConfig{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", symbolSize='" + symbolSize + '\'' +
                ", category='" + category + '\'' +
                ", source='" + source + '\'' +
                ", target='" + target + '\'' +
                '}';
    }
}
