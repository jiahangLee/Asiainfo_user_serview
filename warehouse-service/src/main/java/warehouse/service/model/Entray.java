package warehouse.service.model;

import java.math.BigDecimal;
import java.sql.Date;
import javax.persistence.*;
/**
 * Created by lyl on 2017/2/28.
 */
@Entity
@Table(name = "t_entray")
public class Entray {
    private int id;
    private Integer entrayNo;
    private Integer packingNo;
    private Integer checkNo;
    private String bookNo;
    private String airmaterialCode;
    private String airmaterialName;
    private String airmaterialType;
    private Integer producterNo;
    private String producterName;
    private Integer level;
    private String locationid;
    private Integer informNum;
    private Integer actualNum;
    private BigDecimal entryayPrice;
    private BigDecimal maintainPrice;
    private String partNo;
    private Date productDate;
    private Date sealDeadline;
    private Integer sealType;
    private Integer sealTimes;
    private Integer updownSet;
    private Integer hourSet;
    private Integer saveYears;
    private String unit;
    private Integer labNum;
    private Integer maintainType;
    private Date maintainDate;
    private Integer maintainUnit;
    private String contractNo;
    private Integer orderId;
    private Integer qualityStat;
    private String batchNo;
    private Integer batchSplit;
    private Integer modifyNo;
    private Integer managerNo;
    private Integer entrayStat;
    private String remark;

    @Id
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    @Column(name = "PACKING_NO", nullable = true)
    public Integer getPackingNo() {
        return packingNo;
    }

    public void setPackingNo(Integer packingNo) {
        this.packingNo = packingNo;
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
    @Column(name = "ENTRYAY_PRICE", nullable = true, precision = 2)
    public BigDecimal getEntryayPrice() {
        return entryayPrice;
    }

    public void setEntryayPrice(BigDecimal entryayPrice) {
        this.entryayPrice = entryayPrice;
    }

    @Basic
    @Column(name = "MAINTAIN_PRICE", nullable = true, precision = 2)
    public BigDecimal getMaintainPrice() {
        return maintainPrice;
    }

    public void setMaintainPrice(BigDecimal maintainPrice) {
        this.maintainPrice = maintainPrice;
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
    @Column(name = "UPDOWN_SET", nullable = true)
    public Integer getUpdownSet() {
        return updownSet;
    }

    public void setUpdownSet(Integer updownSet) {
        this.updownSet = updownSet;
    }

    @Basic
    @Column(name = "HOUR_SET", nullable = true, precision = 0)
    public Integer getHourSet() {
        return hourSet;
    }

    public void setHourSet(Integer hourSet) {
        this.hourSet = hourSet;
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
    @Column(name = "UNIT", nullable = true, length = 10)
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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
    @Column(name = "MAINTAIN_TYPE", nullable = true)
    public Integer getMaintainType() {
        return maintainType;
    }

    public void setMaintainType(Integer maintainType) {
        this.maintainType = maintainType;
    }

    @Basic
    @Column(name = "MAINTAIN_DATE", nullable = true)
    public Date getMaintainDate() {
        return maintainDate;
    }

    public void setMaintainDate(Date maintainDate) {
        this.maintainDate = maintainDate;
    }

    @Basic
    @Column(name = "MAINTAIN_UNIT", nullable = true)
    public Integer getMaintainUnit() {
        return maintainUnit;
    }

    public void setMaintainUnit(Integer maintainUnit) {
        this.maintainUnit = maintainUnit;
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
    @Column(name = "ORDER_ID", nullable = true)
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
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
    @Column(name = "BATCH_NO", nullable = true, length = 40)
    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    @Basic
    @Column(name = "BATCH_SPLIT", nullable = true)
    public Integer getBatchSplit() {
        return batchSplit;
    }

    public void setBatchSplit(Integer batchSplit) {
        this.batchSplit = batchSplit;
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
    @Column(name = "MANAGER_NO", nullable = true)
    public Integer getManagerNo() {
        return managerNo;
    }

    public void setManagerNo(Integer managerNo) {
        this.managerNo = managerNo;
    }

    @Basic
    @Column(name = "ENTRAY_STAT", nullable = true)
    public Integer getEntrayStat() {
        return entrayStat;
    }

    public void setEntrayStat(Integer entrayStat) {
        this.entrayStat = entrayStat;
    }

    @Basic
    @Column(name = "REMARK", nullable = true, length = 100)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entray entray = (Entray) o;

        if (id != entray.id) return false;
        if (entrayNo != null ? !entrayNo.equals(entray.entrayNo) : entray.entrayNo != null) return false;
        if (packingNo != null ? !packingNo.equals(entray.packingNo) : entray.packingNo != null) return false;
        if (checkNo != null ? !checkNo.equals(entray.checkNo) : entray.checkNo != null) return false;
        if (bookNo != null ? !bookNo.equals(entray.bookNo) : entray.bookNo != null) return false;
        if (airmaterialCode != null ? !airmaterialCode.equals(entray.airmaterialCode) : entray.airmaterialCode != null)
            return false;
        if (airmaterialName != null ? !airmaterialName.equals(entray.airmaterialName) : entray.airmaterialName != null)
            return false;
        if (airmaterialType != null ? !airmaterialType.equals(entray.airmaterialType) : entray.airmaterialType != null)
            return false;
        if (producterNo != null ? !producterNo.equals(entray.producterNo) : entray.producterNo != null) return false;
        if (producterName != null ? !producterName.equals(entray.producterName) : entray.producterName != null)
            return false;
        if (level != null ? !level.equals(entray.level) : entray.level != null) return false;
        if (locationid != null ? !locationid.equals(entray.locationid) : entray.locationid != null) return false;
        if (informNum != null ? !informNum.equals(entray.informNum) : entray.informNum != null) return false;
        if (actualNum != null ? !actualNum.equals(entray.actualNum) : entray.actualNum != null) return false;
        if (entryayPrice != null ? !entryayPrice.equals(entray.entryayPrice) : entray.entryayPrice != null)
            return false;
        if (maintainPrice != null ? !maintainPrice.equals(entray.maintainPrice) : entray.maintainPrice != null)
            return false;
        if (partNo != null ? !partNo.equals(entray.partNo) : entray.partNo != null) return false;
        if (productDate != null ? !productDate.equals(entray.productDate) : entray.productDate != null) return false;
        if (sealDeadline != null ? !sealDeadline.equals(entray.sealDeadline) : entray.sealDeadline != null)
            return false;
        if (sealType != null ? !sealType.equals(entray.sealType) : entray.sealType != null) return false;
        if (sealTimes != null ? !sealTimes.equals(entray.sealTimes) : entray.sealTimes != null) return false;
        if (updownSet != null ? !updownSet.equals(entray.updownSet) : entray.updownSet != null) return false;
        if (hourSet != null ? !hourSet.equals(entray.hourSet) : entray.hourSet != null) return false;
        if (saveYears != null ? !saveYears.equals(entray.saveYears) : entray.saveYears != null) return false;
        if (unit != null ? !unit.equals(entray.unit) : entray.unit != null) return false;
        if (labNum != null ? !labNum.equals(entray.labNum) : entray.labNum != null) return false;
        if (maintainType != null ? !maintainType.equals(entray.maintainType) : entray.maintainType != null)
            return false;
        if (maintainDate != null ? !maintainDate.equals(entray.maintainDate) : entray.maintainDate != null)
            return false;
        if (maintainUnit != null ? !maintainUnit.equals(entray.maintainUnit) : entray.maintainUnit != null)
            return false;
        if (contractNo != null ? !contractNo.equals(entray.contractNo) : entray.contractNo != null) return false;
        if (orderId != null ? !orderId.equals(entray.orderId) : entray.orderId != null) return false;
        if (qualityStat != null ? !qualityStat.equals(entray.qualityStat) : entray.qualityStat != null) return false;
        if (batchNo != null ? !batchNo.equals(entray.batchNo) : entray.batchNo != null) return false;
        if (batchSplit != null ? !batchSplit.equals(entray.batchSplit) : entray.batchSplit != null) return false;
        if (modifyNo != null ? !modifyNo.equals(entray.modifyNo) : entray.modifyNo != null) return false;
        if (managerNo != null ? !managerNo.equals(entray.managerNo) : entray.managerNo != null) return false;
        if (entrayStat != null ? !entrayStat.equals(entray.entrayStat) : entray.entrayStat != null) return false;
        if (remark != null ? !remark.equals(entray.remark) : entray.remark != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (entrayNo != null ? entrayNo.hashCode() : 0);
        result = 31 * result + (packingNo != null ? packingNo.hashCode() : 0);
        result = 31 * result + (checkNo != null ? checkNo.hashCode() : 0);
        result = 31 * result + (bookNo != null ? bookNo.hashCode() : 0);
        result = 31 * result + (airmaterialCode != null ? airmaterialCode.hashCode() : 0);
        result = 31 * result + (airmaterialName != null ? airmaterialName.hashCode() : 0);
        result = 31 * result + (airmaterialType != null ? airmaterialType.hashCode() : 0);
        result = 31 * result + (producterNo != null ? producterNo.hashCode() : 0);
        result = 31 * result + (producterName != null ? producterName.hashCode() : 0);
        result = 31 * result + (level != null ? level.hashCode() : 0);
        result = 31 * result + (locationid != null ? locationid.hashCode() : 0);
        result = 31 * result + (informNum != null ? informNum.hashCode() : 0);
        result = 31 * result + (actualNum != null ? actualNum.hashCode() : 0);
        result = 31 * result + (entryayPrice != null ? entryayPrice.hashCode() : 0);
        result = 31 * result + (maintainPrice != null ? maintainPrice.hashCode() : 0);
        result = 31 * result + (partNo != null ? partNo.hashCode() : 0);
        result = 31 * result + (productDate != null ? productDate.hashCode() : 0);
        result = 31 * result + (sealDeadline != null ? sealDeadline.hashCode() : 0);
        result = 31 * result + (sealType != null ? sealType.hashCode() : 0);
        result = 31 * result + (sealTimes != null ? sealTimes.hashCode() : 0);
        result = 31 * result + (updownSet != null ? updownSet.hashCode() : 0);
        result = 31 * result + (hourSet != null ? hourSet.hashCode() : 0);
        result = 31 * result + (saveYears != null ? saveYears.hashCode() : 0);
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        result = 31 * result + (labNum != null ? labNum.hashCode() : 0);
        result = 31 * result + (maintainType != null ? maintainType.hashCode() : 0);
        result = 31 * result + (maintainDate != null ? maintainDate.hashCode() : 0);
        result = 31 * result + (maintainUnit != null ? maintainUnit.hashCode() : 0);
        result = 31 * result + (contractNo != null ? contractNo.hashCode() : 0);
        result = 31 * result + (orderId != null ? orderId.hashCode() : 0);
        result = 31 * result + (qualityStat != null ? qualityStat.hashCode() : 0);
        result = 31 * result + (batchNo != null ? batchNo.hashCode() : 0);
        result = 31 * result + (batchSplit != null ? batchSplit.hashCode() : 0);
        result = 31 * result + (modifyNo != null ? modifyNo.hashCode() : 0);
        result = 31 * result + (managerNo != null ? managerNo.hashCode() : 0);
        result = 31 * result + (entrayStat != null ? entrayStat.hashCode() : 0);
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        return result;
    }
}
