package warehouse.service.model;

import java.math.BigDecimal;
import javax.persistence.*;
/**
 * Created by lyl on 2017/2/28.
 */
@Entity
@Table(name = "t_location")
public class Location {
    private String locationid;
    private Integer rfid;
    private Integer roomid;
    private Integer rack;
    private Integer floor;
    private Integer layer;
    private BigDecimal length;
    private BigDecimal width;
    private BigDecimal high;
    private BigDecimal bearing;

    @Id
    @Column(name = "LOCATIONID", nullable = false, length = 50)
    public String getLocationid() {
        return locationid;
    }

    public void setLocationid(String locationid) {
        this.locationid = locationid;
    }

    @Basic
    @Column(name = "RFID", nullable = true)
    public Integer getRfid() {
        return rfid;
    }

    public void setRfid(Integer rfid) {
        this.rfid = rfid;
    }

    @Basic
    @Column(name = "ROOMID", nullable = true)
    public Integer getRoomid() {
        return roomid;
    }

    public void setRoomid(Integer roomid) {
        this.roomid = roomid;
    }

    @Basic
    @Column(name = "RACK", nullable = true)
    public Integer getRack() {
        return rack;
    }

    public void setRack(Integer rack) {
        this.rack = rack;
    }

    @Basic
    @Column(name = "FLOOR", nullable = true)
    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    @Basic
    @Column(name = "LAYER", nullable = true)
    public Integer getLayer() {
        return layer;
    }

    public void setLayer(Integer layer) {
        this.layer = layer;
    }

    @Basic
    @Column(name = "LENGTH", nullable = true, precision = 2)
    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    @Basic
    @Column(name = "WIDTH", nullable = true, precision = 2)
    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    @Basic
    @Column(name = "HIGH", nullable = true, precision = 2)
    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    @Basic
    @Column(name = "BEARING", nullable = true, precision = 2)
    public BigDecimal getBearing() {
        return bearing;
    }

    public void setBearing(BigDecimal bearing) {
        this.bearing = bearing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (locationid != null ? !locationid.equals(location.locationid) : location.locationid != null) return false;
        if (rfid != null ? !rfid.equals(location.rfid) : location.rfid != null) return false;
        if (roomid != null ? !roomid.equals(location.roomid) : location.roomid != null) return false;
        if (rack != null ? !rack.equals(location.rack) : location.rack != null) return false;
        if (floor != null ? !floor.equals(location.floor) : location.floor != null) return false;
        if (layer != null ? !layer.equals(location.layer) : location.layer != null) return false;
        if (length != null ? !length.equals(location.length) : location.length != null) return false;
        if (width != null ? !width.equals(location.width) : location.width != null) return false;
        if (high != null ? !high.equals(location.high) : location.high != null) return false;
        if (bearing != null ? !bearing.equals(location.bearing) : location.bearing != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = locationid != null ? locationid.hashCode() : 0;
        result = 31 * result + (rfid != null ? rfid.hashCode() : 0);
        result = 31 * result + (roomid != null ? roomid.hashCode() : 0);
        result = 31 * result + (rack != null ? rack.hashCode() : 0);
        result = 31 * result + (floor != null ? floor.hashCode() : 0);
        result = 31 * result + (layer != null ? layer.hashCode() : 0);
        result = 31 * result + (length != null ? length.hashCode() : 0);
        result = 31 * result + (width != null ? width.hashCode() : 0);
        result = 31 * result + (high != null ? high.hashCode() : 0);
        result = 31 * result + (bearing != null ? bearing.hashCode() : 0);
        return result;
    }
}
