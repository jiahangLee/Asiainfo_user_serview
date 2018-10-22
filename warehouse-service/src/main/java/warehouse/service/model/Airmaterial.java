package warehouse.service.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * Created by lyl on 2017/2/28.
 */
@Entity
@Table(name = "t_airmaterial")
public class Airmaterial {
    private int id;
    private Integer rfid;
    private String batchNo;
    private Integer entrayNo;
    private Integer checkNo;
    private String contractNo;
    private Integer packingNo;
    private String airmaterialCode;
    private String airmaterialName;
    private String airmaterialType;
    private String unit;
    private Integer producterNo;
    private String producterName;
    private Integer level;
    private String locationid;
    private String stackingid;
    private Integer roomid;
    private String boxNo;
    private BigDecimal storagePrice;
    private BigDecimal movingPrice;
    private BigDecimal oriPrice;
    private Integer storageNum;
    private Integer availableNum;
    private Integer temporaryDispatch;
    private Integer sealNum;
    private Integer sealingNum;
    private Integer taskNum;
    private Integer checkingNum;
    private String partNo;
    private Date productDate;
    private Date storageDate;
    private Date discardDate;
    private Date sealDate;
    private Date sealDeadline;
    private Integer sealType;
    private Integer sealTimes;
    private Integer saveYears;
    private Integer labNum;
    private Integer storageType;
    private Integer billingType;
    private Integer orderId;
    private Integer taskId;
    private Integer qualityStat;
    private Integer maintainNum;
    private Integer modifyProgrameNo;
    private Integer modifyStat;
    private Integer usingStage;
    private String remarks;

    @Id
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    @Column(name = "BATCH_NO", nullable = true, length = 40)
    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
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
    @Column(name = "CONTRACT_NO", nullable = true, length = 40)
    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    @Basic
    @Column(name = "PACKING_NO", nullable = true)
    public Integer getPackingNo() {
        return packingNo;
    }

    public void setPackingNo(Integer packingNo) {
        this.packingNo = packingNo;
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
    @Column(name = "UNIT", nullable = true, length = 10)
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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
    @Column(name = "STACKINGID", nullable = true, length = 50)
    public String getStackingid() {
        return stackingid;
    }

    public void setStackingid(String stackingid) {
        this.stackingid = stackingid;
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
    @Column(name = "BOX_NO", nullable = true, length = 50)
    public String getBoxNo() {
        return boxNo;
    }

    public void setBoxNo(String boxNo) {
        this.boxNo = boxNo;
    }

    @Basic
    @Column(name = "STORAGE_PRICE", nullable = true, precision = 2)
    public BigDecimal getStoragePrice() {
        return storagePrice;
    }

    public void setStoragePrice(BigDecimal storagePrice) {
        this.storagePrice = storagePrice;
    }

    @Basic
    @Column(name = "MOVING_PRICE", nullable = true, precision = 2)
    public BigDecimal getMovingPrice() {
        return movingPrice;
    }

    public void setMovingPrice(BigDecimal movingPrice) {
        this.movingPrice = movingPrice;
    }

    @Basic
    @Column(name = "ORI_PRICE", nullable = true, precision = 2)
    public BigDecimal getOriPrice() {
        return oriPrice;
    }

    public void setOriPrice(BigDecimal oriPrice) {
        this.oriPrice = oriPrice;
    }

    @Basic
    @Column(name = "STORAGE_NUM", nullable = true, precision = 0)
    public Integer getStorageNum() {
        return storageNum;
    }

    public void setStorageNum(Integer storageNum) {
        this.storageNum = storageNum;
    }

    @Basic
    @Column(name = "AVAILABLE_NUM", nullable = true, precision = 0)
    public Integer getAvailableNum() {
        return availableNum;
    }

    public void setAvailableNum(Integer availableNum) {
        this.availableNum = availableNum;
    }

    @Basic
    @Column(name = "TEMPORARY_DISPATCH", nullable = true, precision = 0)
    public Integer getTemporaryDispatch() {
        return temporaryDispatch;
    }

    public void setTemporaryDispatch(Integer temporaryDispatch) {
        this.temporaryDispatch = temporaryDispatch;
    }

    @Basic
    @Column(name = "SEAL_NUM", nullable = true, precision = 0)
    public Integer getSealNum() {
        return sealNum;
    }

    public void setSealNum(Integer sealNum) {
        this.sealNum = sealNum;
    }

    @Basic
    @Column(name = "SEALING_NUM", nullable = true, precision = 0)
    public Integer getSealingNum() {
        return sealingNum;
    }

    public void setSealingNum(Integer sealingNum) {
        this.sealingNum = sealingNum;
    }

    @Basic
    @Column(name = "TASK_NUM", nullable = true, precision = 0)
    public Integer getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(Integer taskNum) {
        this.taskNum = taskNum;
    }

    @Basic
    @Column(name = "CHECKING_NUM", nullable = true, precision = 0)
    public Integer getCheckingNum() {
        return checkingNum;
    }

    public void setCheckingNum(Integer checkingNum) {
        this.checkingNum = checkingNum;
    }

    @Basic
    @Column(name = "PART_NO", nullable = true, length = 40)
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
    @Column(name = "STORAGE_DATE", nullable = true)
    public Date getStorageDate() {
        return storageDate;
    }

    public void setStorageDate(Date storageDate) {
        this.storageDate = storageDate;
    }

    @Basic
    @Column(name = "DISCARD_DATE", nullable = true)
    public Date getDiscardDate() {
        return discardDate;
    }

    public void setDiscardDate(Date discardDate) {
        this.discardDate = discardDate;
    }

    @Basic
    @Column(name = "SEAL_DATE", nullable = true)
    public Date getSealDate() {
        return sealDate;
    }

    public void setSealDate(Date sealDate) {
        this.sealDate = sealDate;
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
    @Column(name = "LAB_NUM", nullable = true)
    public Integer getLabNum() {
        return labNum;
    }

    public void setLabNum(Integer labNum) {
        this.labNum = labNum;
    }

    @Basic
    @Column(name = "STORAGE_TYPE", nullable = true)
    public Integer getStorageType() {
        return storageType;
    }

    public void setStorageType(Integer storageType) {
        this.storageType = storageType;
    }

    @Basic
    @Column(name = "BILLING_TYPE", nullable = true)
    public Integer getBillingType() {
        return billingType;
    }

    public void setBillingType(Integer billingType) {
        this.billingType = billingType;
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
    @Column(name = "TASK_ID", nullable = true)
    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    @Basic
    @Column(name = "QUALITY_STAT", nullable = true)
    public Integer getQualityStat() {
        return qualityStat;
    }

    public void setQualityStat(Integer qualityStat) {
        this.qualityStat = qualityStat;
    }

    @Basic
    @Column(name = "MAINTAIN_NUM", nullable = true)
    public Integer getMaintainNum() {
        return maintainNum;
    }

    public void setMaintainNum(Integer maintainNum) {
        this.maintainNum = maintainNum;
    }

    @Basic
    @Column(name = "MODIFY_PROGRAME_NO", nullable = true)
    public Integer getModifyProgrameNo() {
        return modifyProgrameNo;
    }

    public void setModifyProgrameNo(Integer modifyProgrameNo) {
        this.modifyProgrameNo = modifyProgrameNo;
    }

    @Basic
    @Column(name = "MODIFY_STAT", nullable = true)
    public Integer getModifyStat() {
        return modifyStat;
    }

    public void setModifyStat(Integer modifyStat) {
        this.modifyStat = modifyStat;
    }

    @Basic
    @Column(name = "USING_STAGE", nullable = true)
    public Integer getUsingStage() {
        return usingStage;
    }

    public void setUsingStage(Integer usingStage) {
        this.usingStage = usingStage;
    }

    @Basic
    @Column(name = "REMARKS", nullable = true, length = 100)
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Airmaterial that = (Airmaterial) o;

        if (id != that.id) return false;
        if (rfid != null ? !rfid.equals(that.rfid) : that.rfid != null) return false;
        if (batchNo != null ? !batchNo.equals(that.batchNo) : that.batchNo != null) return false;
        if (entrayNo != null ? !entrayNo.equals(that.entrayNo) : that.entrayNo != null) return false;
        if (checkNo != null ? !checkNo.equals(that.checkNo) : that.checkNo != null) return false;
        if (contractNo != null ? !contractNo.equals(that.contractNo) : that.contractNo != null) return false;
        if (packingNo != null ? !packingNo.equals(that.packingNo) : that.packingNo != null) return false;
        if (airmaterialCode != null ? !airmaterialCode.equals(that.airmaterialCode) : that.airmaterialCode != null)
            return false;
        if (airmaterialName != null ? !airmaterialName.equals(that.airmaterialName) : that.airmaterialName != null)
            return false;
        if (airmaterialType != null ? !airmaterialType.equals(that.airmaterialType) : that.airmaterialType != null)
            return false;
        if (unit != null ? !unit.equals(that.unit) : that.unit != null) return false;
        if (producterNo != null ? !producterNo.equals(that.producterNo) : that.producterNo != null) return false;
        if (producterName != null ? !producterName.equals(that.producterName) : that.producterName != null)
            return false;
        if (level != null ? !level.equals(that.level) : that.level != null) return false;
        if (locationid != null ? !locationid.equals(that.locationid) : that.locationid != null) return false;
        if (stackingid != null ? !stackingid.equals(that.stackingid) : that.stackingid != null) return false;
        if (roomid != null ? !roomid.equals(that.roomid) : that.roomid != null) return false;
        if (boxNo != null ? !boxNo.equals(that.boxNo) : that.boxNo != null) return false;
        if (storagePrice != null ? !storagePrice.equals(that.storagePrice) : that.storagePrice != null) return false;
        if (movingPrice != null ? !movingPrice.equals(that.movingPrice) : that.movingPrice != null) return false;
        if (oriPrice != null ? !oriPrice.equals(that.oriPrice) : that.oriPrice != null) return false;
        if (storageNum != null ? !storageNum.equals(that.storageNum) : that.storageNum != null) return false;
        if (availableNum != null ? !availableNum.equals(that.availableNum) : that.availableNum != null) return false;
        if (temporaryDispatch != null ? !temporaryDispatch.equals(that.temporaryDispatch) : that.temporaryDispatch != null)
            return false;
        if (sealNum != null ? !sealNum.equals(that.sealNum) : that.sealNum != null) return false;
        if (sealingNum != null ? !sealingNum.equals(that.sealingNum) : that.sealingNum != null) return false;
        if (taskNum != null ? !taskNum.equals(that.taskNum) : that.taskNum != null) return false;
        if (checkingNum != null ? !checkingNum.equals(that.checkingNum) : that.checkingNum != null) return false;
        if (partNo != null ? !partNo.equals(that.partNo) : that.partNo != null) return false;
        if (productDate != null ? !productDate.equals(that.productDate) : that.productDate != null) return false;
        if (storageDate != null ? !storageDate.equals(that.storageDate) : that.storageDate != null) return false;
        if (discardDate != null ? !discardDate.equals(that.discardDate) : that.discardDate != null) return false;
        if (sealDate != null ? !sealDate.equals(that.sealDate) : that.sealDate != null) return false;
        if (sealDeadline != null ? !sealDeadline.equals(that.sealDeadline) : that.sealDeadline != null) return false;
        if (sealType != null ? !sealType.equals(that.sealType) : that.sealType != null) return false;
        if (sealTimes != null ? !sealTimes.equals(that.sealTimes) : that.sealTimes != null) return false;
        if (saveYears != null ? !saveYears.equals(that.saveYears) : that.saveYears != null) return false;
        if (labNum != null ? !labNum.equals(that.labNum) : that.labNum != null) return false;
        if (storageType != null ? !storageType.equals(that.storageType) : that.storageType != null) return false;
        if (billingType != null ? !billingType.equals(that.billingType) : that.billingType != null) return false;
        if (orderId != null ? !orderId.equals(that.orderId) : that.orderId != null) return false;
        if (taskId != null ? !taskId.equals(that.taskId) : that.taskId != null) return false;
        if (qualityStat != null ? !qualityStat.equals(that.qualityStat) : that.qualityStat != null) return false;
        if (maintainNum != null ? !maintainNum.equals(that.maintainNum) : that.maintainNum != null) return false;
        if (modifyProgrameNo != null ? !modifyProgrameNo.equals(that.modifyProgrameNo) : that.modifyProgrameNo != null)
            return false;
        if (modifyStat != null ? !modifyStat.equals(that.modifyStat) : that.modifyStat != null) return false;
        if (usingStage != null ? !usingStage.equals(that.usingStage) : that.usingStage != null) return false;
        if (remarks != null ? !remarks.equals(that.remarks) : that.remarks != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (rfid != null ? rfid.hashCode() : 0);
        result = 31 * result + (batchNo != null ? batchNo.hashCode() : 0);
        result = 31 * result + (entrayNo != null ? entrayNo.hashCode() : 0);
        result = 31 * result + (checkNo != null ? checkNo.hashCode() : 0);
        result = 31 * result + (contractNo != null ? contractNo.hashCode() : 0);
        result = 31 * result + (packingNo != null ? packingNo.hashCode() : 0);
        result = 31 * result + (airmaterialCode != null ? airmaterialCode.hashCode() : 0);
        result = 31 * result + (airmaterialName != null ? airmaterialName.hashCode() : 0);
        result = 31 * result + (airmaterialType != null ? airmaterialType.hashCode() : 0);
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        result = 31 * result + (producterNo != null ? producterNo.hashCode() : 0);
        result = 31 * result + (producterName != null ? producterName.hashCode() : 0);
        result = 31 * result + (level != null ? level.hashCode() : 0);
        result = 31 * result + (locationid != null ? locationid.hashCode() : 0);
        result = 31 * result + (stackingid != null ? stackingid.hashCode() : 0);
        result = 31 * result + (roomid != null ? roomid.hashCode() : 0);
        result = 31 * result + (boxNo != null ? boxNo.hashCode() : 0);
        result = 31 * result + (storagePrice != null ? storagePrice.hashCode() : 0);
        result = 31 * result + (movingPrice != null ? movingPrice.hashCode() : 0);
        result = 31 * result + (oriPrice != null ? oriPrice.hashCode() : 0);
        result = 31 * result + (storageNum != null ? storageNum.hashCode() : 0);
        result = 31 * result + (availableNum != null ? availableNum.hashCode() : 0);
        result = 31 * result + (temporaryDispatch != null ? temporaryDispatch.hashCode() : 0);
        result = 31 * result + (sealNum != null ? sealNum.hashCode() : 0);
        result = 31 * result + (sealingNum != null ? sealingNum.hashCode() : 0);
        result = 31 * result + (taskNum != null ? taskNum.hashCode() : 0);
        result = 31 * result + (checkingNum != null ? checkingNum.hashCode() : 0);
        result = 31 * result + (partNo != null ? partNo.hashCode() : 0);
        result = 31 * result + (productDate != null ? productDate.hashCode() : 0);
        result = 31 * result + (storageDate != null ? storageDate.hashCode() : 0);
        result = 31 * result + (discardDate != null ? discardDate.hashCode() : 0);
        result = 31 * result + (sealDate != null ? sealDate.hashCode() : 0);
        result = 31 * result + (sealDeadline != null ? sealDeadline.hashCode() : 0);
        result = 31 * result + (sealType != null ? sealType.hashCode() : 0);
        result = 31 * result + (sealTimes != null ? sealTimes.hashCode() : 0);
        result = 31 * result + (saveYears != null ? saveYears.hashCode() : 0);
        result = 31 * result + (labNum != null ? labNum.hashCode() : 0);
        result = 31 * result + (storageType != null ? storageType.hashCode() : 0);
        result = 31 * result + (billingType != null ? billingType.hashCode() : 0);
        result = 31 * result + (orderId != null ? orderId.hashCode() : 0);
        result = 31 * result + (taskId != null ? taskId.hashCode() : 0);
        result = 31 * result + (qualityStat != null ? qualityStat.hashCode() : 0);
        result = 31 * result + (maintainNum != null ? maintainNum.hashCode() : 0);
        result = 31 * result + (modifyProgrameNo != null ? modifyProgrameNo.hashCode() : 0);
        result = 31 * result + (modifyStat != null ? modifyStat.hashCode() : 0);
        result = 31 * result + (usingStage != null ? usingStage.hashCode() : 0);
        result = 31 * result + (remarks != null ? remarks.hashCode() : 0);
        return result;
    }
}
