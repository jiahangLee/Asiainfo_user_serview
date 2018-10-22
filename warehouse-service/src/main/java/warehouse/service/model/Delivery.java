package warehouse.service.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * Created by lyl on 2017/2/28.
 */
@Entity
@Table(name = "t_delivery")
public class Delivery {
    private int id;
    private Integer deliveryNo;
    private Integer batchStrorageNo;
    private Integer entrayNo;
    private Integer checkNo;
    private String deliveryDocno;
    private Integer orderId;
    private String contractNo;
    private String bookNo;
    private String airmaterialCode;
    private String airmaterialName;
    private String airmaterialType;
    private Integer producterNo;
    private String producterName;
    private Integer level;
    private String locationid;
    private BigDecimal deliveryPrice;
    private Integer informNum;
    private Integer actualNum;
    private Integer checkNum;
    private String partNo;
    private Date productDate;
    private Date sealDeadline;
    private Integer sealType;
    private Integer sealTimes;
    private Integer saveYears;
    private Integer managerNo;
    private String batchNo;
    private String unit;
    private Integer deliverStat;
    private Integer modifyNo;
    private String remark;
    private Integer returnId;

    @Id
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "DELIVERY_NO", nullable = true)
    public Integer getDeliveryNo() {
        return deliveryNo;
    }

    public void setDeliveryNo(Integer deliveryNo) {
        this.deliveryNo = deliveryNo;
    }

    @Basic
    @Column(name = "BATCH_STRORAGE_NO", nullable = true)
    public Integer getBatchStrorageNo() {
        return batchStrorageNo;
    }

    public void setBatchStrorageNo(Integer batchStrorageNo) {
        this.batchStrorageNo = batchStrorageNo;
    }

    @Basic
    @Column(name = "ENTRAY_NO", nullable = true)
    public Integer getEntrayNo() {
        return entrayNo;
    }

    public void setEntrayNo(Integer entrayNo) {
        this.entrayNo = entrayNo;
    }

    @Basic
    @Column(name = "CHECK_NO", nullable = true)
    public Integer getCheckNo() {
        return checkNo;
    }

    public void setCheckNo(Integer checkNo) {
        this.checkNo = checkNo;
    }

    @Basic
    @Column(name = "DELIVERY_DOCNO", nullable = true, length = 50)
    public String getDeliveryDocno() {
        return deliveryDocno;
    }

    public void setDeliveryDocno(String deliveryDocno) {
        this.deliveryDocno = deliveryDocno;
    }

    @Basic
    @Column(name = "ORDER_ID", nullable = true)
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    @Basic
    @Column(name = "CONTRACT_NO", nullable = true, length = 40)
    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    @Basic
    @Column(name = "BOOK_NO", nullable = true, length = 40)
    public String getBookNo() {
        return bookNo;
    }

    public void setBookNo(String bookNo) {
        this.bookNo = bookNo;
    }

    @Basic
    @Column(name = "AIRMATERIAL_CODE", nullable = true, length = 40)
    public String getAirmaterialCode() {
        return airmaterialCode;
    }

    public void setAirmaterialCode(String airmaterialCode) {
        this.airmaterialCode = airmaterialCode;
    }

    @Basic
    @Column(name = "AIRMATERIAL_NAME", nullable = true, length = 100)
    public String getAirmaterialName() {
        return airmaterialName;
    }

    public void setAirmaterialName(String airmaterialName) {
        this.airmaterialName = airmaterialName;
    }

    @Basic
    @Column(name = "AIRMATERIAL_TYPE", nullable = true, length = 50)
    public String getAirmaterialType() {
        return airmaterialType;
    }

    public void setAirmaterialType(String airmaterialType) {
        this.airmaterialType = airmaterialType;
    }

    @Basic
    @Column(name = "PRODUCTER_NO", nullable = true)
    public Integer getProducterNo() {
        return producterNo;
    }

    public void setProducterNo(Integer producterNo) {
        this.producterNo = producterNo;
    }

    @Basic
    @Column(name = "PRODUCTER_NAME", nullable = true, length = 100)
    public String getProducterName() {
        return producterName;
    }

    public void setProducterName(String producterName) {
        this.producterName = producterName;
    }

    @Basic
    @Column(name = "LEVEL", nullable = true)
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Basic
    @Column(name = "LOCATIONID", nullable = true, length = 50)
    public String getLocationid() {
        return locationid;
    }

    public void setLocationid(String locationid) {
        this.locationid = locationid;
    }

    @Basic
    @Column(name = "DELIVERY_PRICE", nullable = true, precision = 2)
    public BigDecimal getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(BigDecimal deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    @Basic
    @Column(name = "INFORM_NUM", nullable = true)
    public Integer getInformNum() {
        return informNum;
    }

    public void setInformNum(Integer informNum) {
        this.informNum = informNum;
    }

    @Basic
    @Column(name = "ACTUAL_NUM", nullable = true)
    public Integer getActualNum() {
        return actualNum;
    }

    public void setActualNum(Integer actualNum) {
        this.actualNum = actualNum;
    }

    @Basic
    @Column(name = "CHECK_NUM", nullable = true)
    public Integer getCheckNum() {
        return checkNum;
    }

    public void setCheckNum(Integer checkNum) {
        this.checkNum = checkNum;
    }

    @Basic
    @Column(name = "PART_NO", nullable = true, length = 50)
    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    @Basic
    @Column(name = "PRODUCT_DATE", nullable = true)
    public Date getProductDate() {
        return productDate;
    }

    public void setProductDate(Date productDate) {
        this.productDate = productDate;
    }

    @Basic
    @Column(name = "SEAL_DEADLINE", nullable = true)
    public Date getSealDeadline() {
        return sealDeadline;
    }

    public void setSealDeadline(Date sealDeadline) {
        this.sealDeadline = sealDeadline;
    }

    @Basic
    @Column(name = "SEAL_TYPE", nullable = true)
    public Integer getSealType() {
        return sealType;
    }

    public void setSealType(Integer sealType) {
        this.sealType = sealType;
    }

    @Basic
    @Column(name = "SEAL_TIMES", nullable = true)
    public Integer getSealTimes() {
        return sealTimes;
    }

    public void setSealTimes(Integer sealTimes) {
        this.sealTimes = sealTimes;
    }

    @Basic
    @Column(name = "SAVE_YEARS", nullable = true, precision = 0)
    public Integer getSaveYears() {
        return saveYears;
    }

    public void setSaveYears(Integer saveYears) {
        this.saveYears = saveYears;
    }

    @Basic
    @Column(name = "MANAGER_NO", nullable = true)
    public Integer getManagerNo() {
        return managerNo;
    }

    public void setManagerNo(Integer managerNo) {
        this.managerNo = managerNo;
    }

    @Basic
    @Column(name = "BATCH_NO", nullable = true, length = 40)
    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    @Basic
    @Column(name = "UNIT", nullable = true, length = 10)
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Basic
    @Column(name = "DELIVER_STAT", nullable = true)
    public Integer getDeliverStat() {
        return deliverStat;
    }

    public void setDeliverStat(Integer deliverStat) {
        this.deliverStat = deliverStat;
    }

    @Basic
    @Column(name = "MODIFY_NO", nullable = true)
    public Integer getModifyNo() {
        return modifyNo;
    }

    public void setModifyNo(Integer modifyNo) {
        this.modifyNo = modifyNo;
    }

    @Basic
    @Column(name = "REMARK", nullable = true, length = 100)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Basic
    @Column(name = "RETURN_ID", nullable = true)
    public Integer getReturnId() {
        return returnId;
    }

    public void setReturnId(Integer returnId) {
        this.returnId = returnId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Delivery delivery = (Delivery) o;

        if (id != delivery.id) return false;
        if (deliveryNo != null ? !deliveryNo.equals(delivery.deliveryNo) : delivery.deliveryNo != null) return false;
        if (batchStrorageNo != null ? !batchStrorageNo.equals(delivery.batchStrorageNo) : delivery.batchStrorageNo != null)
            return false;
        if (entrayNo != null ? !entrayNo.equals(delivery.entrayNo) : delivery.entrayNo != null) return false;
        if (checkNo != null ? !checkNo.equals(delivery.checkNo) : delivery.checkNo != null) return false;
        if (deliveryDocno != null ? !deliveryDocno.equals(delivery.deliveryDocno) : delivery.deliveryDocno != null)
            return false;
        if (orderId != null ? !orderId.equals(delivery.orderId) : delivery.orderId != null) return false;
        if (contractNo != null ? !contractNo.equals(delivery.contractNo) : delivery.contractNo != null) return false;
        if (bookNo != null ? !bookNo.equals(delivery.bookNo) : delivery.bookNo != null) return false;
        if (airmaterialCode != null ? !airmaterialCode.equals(delivery.airmaterialCode) : delivery.airmaterialCode != null)
            return false;
        if (airmaterialName != null ? !airmaterialName.equals(delivery.airmaterialName) : delivery.airmaterialName != null)
            return false;
        if (airmaterialType != null ? !airmaterialType.equals(delivery.airmaterialType) : delivery.airmaterialType != null)
            return false;
        if (producterNo != null ? !producterNo.equals(delivery.producterNo) : delivery.producterNo != null)
            return false;
        if (producterName != null ? !producterName.equals(delivery.producterName) : delivery.producterName != null)
            return false;
        if (level != null ? !level.equals(delivery.level) : delivery.level != null) return false;
        if (locationid != null ? !locationid.equals(delivery.locationid) : delivery.locationid != null) return false;
        if (deliveryPrice != null ? !deliveryPrice.equals(delivery.deliveryPrice) : delivery.deliveryPrice != null)
            return false;
        if (informNum != null ? !informNum.equals(delivery.informNum) : delivery.informNum != null) return false;
        if (actualNum != null ? !actualNum.equals(delivery.actualNum) : delivery.actualNum != null) return false;
        if (checkNum != null ? !checkNum.equals(delivery.checkNum) : delivery.checkNum != null) return false;
        if (partNo != null ? !partNo.equals(delivery.partNo) : delivery.partNo != null) return false;
        if (productDate != null ? !productDate.equals(delivery.productDate) : delivery.productDate != null)
            return false;
        if (sealDeadline != null ? !sealDeadline.equals(delivery.sealDeadline) : delivery.sealDeadline != null)
            return false;
        if (sealType != null ? !sealType.equals(delivery.sealType) : delivery.sealType != null) return false;
        if (sealTimes != null ? !sealTimes.equals(delivery.sealTimes) : delivery.sealTimes != null) return false;
        if (saveYears != null ? !saveYears.equals(delivery.saveYears) : delivery.saveYears != null) return false;
        if (managerNo != null ? !managerNo.equals(delivery.managerNo) : delivery.managerNo != null) return false;
        if (batchNo != null ? !batchNo.equals(delivery.batchNo) : delivery.batchNo != null) return false;
        if (unit != null ? !unit.equals(delivery.unit) : delivery.unit != null) return false;
        if (deliverStat != null ? !deliverStat.equals(delivery.deliverStat) : delivery.deliverStat != null)
            return false;
        if (modifyNo != null ? !modifyNo.equals(delivery.modifyNo) : delivery.modifyNo != null) return false;
        if (remark != null ? !remark.equals(delivery.remark) : delivery.remark != null) return false;
        if (returnId != null ? !returnId.equals(delivery.returnId) : delivery.returnId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (deliveryNo != null ? deliveryNo.hashCode() : 0);
        result = 31 * result + (batchStrorageNo != null ? batchStrorageNo.hashCode() : 0);
        result = 31 * result + (entrayNo != null ? entrayNo.hashCode() : 0);
        result = 31 * result + (checkNo != null ? checkNo.hashCode() : 0);
        result = 31 * result + (deliveryDocno != null ? deliveryDocno.hashCode() : 0);
        result = 31 * result + (orderId != null ? orderId.hashCode() : 0);
        result = 31 * result + (contractNo != null ? contractNo.hashCode() : 0);
        result = 31 * result + (bookNo != null ? bookNo.hashCode() : 0);
        result = 31 * result + (airmaterialCode != null ? airmaterialCode.hashCode() : 0);
        result = 31 * result + (airmaterialName != null ? airmaterialName.hashCode() : 0);
        result = 31 * result + (airmaterialType != null ? airmaterialType.hashCode() : 0);
        result = 31 * result + (producterNo != null ? producterNo.hashCode() : 0);
        result = 31 * result + (producterName != null ? producterName.hashCode() : 0);
        result = 31 * result + (level != null ? level.hashCode() : 0);
        result = 31 * result + (locationid != null ? locationid.hashCode() : 0);
        result = 31 * result + (deliveryPrice != null ? deliveryPrice.hashCode() : 0);
        result = 31 * result + (informNum != null ? informNum.hashCode() : 0);
        result = 31 * result + (actualNum != null ? actualNum.hashCode() : 0);
        result = 31 * result + (checkNum != null ? checkNum.hashCode() : 0);
        result = 31 * result + (partNo != null ? partNo.hashCode() : 0);
        result = 31 * result + (productDate != null ? productDate.hashCode() : 0);
        result = 31 * result + (sealDeadline != null ? sealDeadline.hashCode() : 0);
        result = 31 * result + (sealType != null ? sealType.hashCode() : 0);
        result = 31 * result + (sealTimes != null ? sealTimes.hashCode() : 0);
        result = 31 * result + (saveYears != null ? saveYears.hashCode() : 0);
        result = 31 * result + (managerNo != null ? managerNo.hashCode() : 0);
        result = 31 * result + (batchNo != null ? batchNo.hashCode() : 0);
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        result = 31 * result + (deliverStat != null ? deliverStat.hashCode() : 0);
        result = 31 * result + (modifyNo != null ? modifyNo.hashCode() : 0);
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        result = 31 * result + (returnId != null ? returnId.hashCode() : 0);
        return result;
    }
}
