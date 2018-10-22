package dolphin.account.domain.dict;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by lyl on 16/5/30.
 */
@Entity
public class Dict implements Serializable{


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int groupId;
    private String groupName;
    private String groupLabel;
    private int itemId;
    private String itemName;
    private String itemLabel;
    private boolean fromExcel;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupLabel() {
        return groupLabel;
    }

    public void setGroupLabel(String groupLabel) {
        this.groupLabel = groupLabel;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemLabel() {
        return itemLabel;
    }

    public void setItemLabel(String itemLabel) {
        this.itemLabel = itemLabel;
    }
    public boolean isFromExcel() {
        return fromExcel;
    }

    public void setFromExcel(boolean fromExcel) {
        this.fromExcel = fromExcel;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dict dict = (Dict) o;

        if (id != dict.id) return false;
        if (groupId != dict.groupId) return false;
        if (itemId != dict.itemId) return false;
        if (groupName != null ? !groupName.equals(dict.groupName) : dict.groupName != null) return false;
        if (groupLabel != null ? !groupLabel.equals(dict.groupLabel) : dict.groupLabel != null) return false;
        if (itemName != null ? !itemName.equals(dict.itemName) : dict.itemName != null) return false;
        return itemLabel != null ? itemLabel.equals(dict.itemLabel) : dict.itemLabel == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + groupId;
        result = 31 * result + (groupName != null ? groupName.hashCode() : 0);
        result = 31 * result + (groupLabel != null ? groupLabel.hashCode() : 0);
        result = 31 * result + itemId;
        result = 31 * result + (itemName != null ? itemName.hashCode() : 0);
        result = 31 * result + (itemLabel != null ? itemLabel.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Dict{" +
                "id=" + id +
                ", groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                ", groupLabel='" + groupLabel + '\'' +
                ", itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", itemLabel='" + itemLabel + '\'' +
                '}';
    }
}
