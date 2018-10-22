package warehouse.service.model;

import java.math.BigDecimal;
import javax.persistence.*;
/**
 * Created by lyl on 2017/2/28.
 */
@Entity
@Table(name = "t_storageroom")
public class StorageRoom {
    private int roomid;
    private String roomtype;
    private String name;
    private Integer managerId;
    private Integer rackNum;
    private Integer locationNum;
    private Integer stackingNum;
    private BigDecimal areageSize;
    private BigDecimal highSize;
    private Integer rfid;

    @Id
    @Column(name = "ROOMID", nullable = false)
    public int getRoomid() {
        return roomid;
    }

    public void setRoomid(int roomid) {
        this.roomid = roomid;
    }

    @Basic
    @Column(name = "ROOMTYPE", nullable = true, length = 50)
    public String getRoomtype() {
        return roomtype;
    }

    public void setRoomtype(String roomtype) {
        this.roomtype = roomtype;
    }

    @Basic
    @Column(name = "NAME", nullable = true, length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "MANAGER_ID", nullable = true)
    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    @Basic
    @Column(name = "RACK_NUM", nullable = true)
    public Integer getRackNum() {
        return rackNum;
    }

    public void setRackNum(Integer rackNum) {
        this.rackNum = rackNum;
    }

    @Basic
    @Column(name = "LOCATION_NUM", nullable = true)
    public Integer getLocationNum() {
        return locationNum;
    }

    public void setLocationNum(Integer locationNum) {
        this.locationNum = locationNum;
    }

    @Basic
    @Column(name = "STACKING_NUM", nullable = true)
    public Integer getStackingNum() {
        return stackingNum;
    }

    public void setStackingNum(Integer stackingNum) {
        this.stackingNum = stackingNum;
    }

    @Basic
    @Column(name = "AREAGE_SIZE", nullable = true, precision = 2)
    public BigDecimal getAreageSize() {
        return areageSize;
    }

    public void setAreageSize(BigDecimal areageSize) {
        this.areageSize = areageSize;
    }

    @Basic
    @Column(name = "HIGH_SIZE", nullable = true, precision = 2)
    public BigDecimal getHighSize() {
        return highSize;
    }

    public void setHighSize(BigDecimal highSize) {
        this.highSize = highSize;
    }

    @Basic
    @Column(name = "RFID", nullable = true)
    public Integer getRfid() {
        return rfid;
    }

    public void setRfid(Integer rfid) {
        this.rfid = rfid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StorageRoom that = (StorageRoom) o;

        if (roomid != that.roomid) return false;
        if (roomtype != null ? !roomtype.equals(that.roomtype) : that.roomtype != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (managerId != null ? !managerId.equals(that.managerId) : that.managerId != null) return false;
        if (rackNum != null ? !rackNum.equals(that.rackNum) : that.rackNum != null) return false;
        if (locationNum != null ? !locationNum.equals(that.locationNum) : that.locationNum != null) return false;
        if (stackingNum != null ? !stackingNum.equals(that.stackingNum) : that.stackingNum != null) return false;
        if (areageSize != null ? !areageSize.equals(that.areageSize) : that.areageSize != null) return false;
        if (highSize != null ? !highSize.equals(that.highSize) : that.highSize != null) return false;
        if (rfid != null ? !rfid.equals(that.rfid) : that.rfid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = roomid;
        result = 31 * result + (roomtype != null ? roomtype.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (managerId != null ? managerId.hashCode() : 0);
        result = 31 * result + (rackNum != null ? rackNum.hashCode() : 0);
        result = 31 * result + (locationNum != null ? locationNum.hashCode() : 0);
        result = 31 * result + (stackingNum != null ? stackingNum.hashCode() : 0);
        result = 31 * result + (areageSize != null ? areageSize.hashCode() : 0);
        result = 31 * result + (highSize != null ? highSize.hashCode() : 0);
        result = 31 * result + (rfid != null ? rfid.hashCode() : 0);
        return result;
    }
}
