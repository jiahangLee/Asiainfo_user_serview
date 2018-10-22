package dolphin.account.domain.permission;

import java.io.Serializable;
import javax.persistence.*;
/**
 * Created by lyl on 16/5/25.
 */

@Entity
public class Permission implements Serializable {

    public static String KEY_CHILDREN="children";
    public static String KEY_NAME ="name";
    public static String KEY_DESC="desc";


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;


    @Column(unique = true)
    private String uniqueId;

    private String name;
    private String label;
    private int parentId;


    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }


}
