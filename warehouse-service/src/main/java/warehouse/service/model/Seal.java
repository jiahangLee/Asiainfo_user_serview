package warehouse.service.model;

import java.sql.Date;
import javax.persistence.*;
/**
 * Created by lyl on 2017/2/28.
 */
@Entity
@Table(name = "t_seal")
public class Seal {
    private int id;
    private Integer sealNo;
    private Integer batchStorageNo;
    private Integer entrayNo;
    private Integer checkNo;
    private Integer storageTypeNo;
    private String contractNo;
    private String bookNo;
    private String airmaterialCode;
    private String airmaterialName;
    private String airmaterialType;
    private Integer producterNo;
    private String producterName;
    private String partNo;
    private Integer level;
    private String locationid;
    private Integer sealType;
    private Date oldSealDeadline;
    private Date sealDeadline;
    private Integer batchStorage;
    private Integer planNum;
    private Integer recieveNum;
    private Integer sealNum;
    private Integer returnNum;
    private String batchNo;
    private String remark;
    private Date sealUpdate;
    private Integer sealWorker;
    private Integer sealRfid;
    private Integer recieveWorker;
    private Integer recieveRfid;
    private Date returnDate;
    private Date recieveDate;
    private Integer entrayStat;
    private Integer deliverStat;

    @Id
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "SEAL_NO", nullable = true)
    public Integer getSealNo() {
        return sealNo;
    }

    public void setSealNo(Integer sealNo) {
        this.sealNo = sealNo;
    }

    @Basic
    @Column(name = "BATCH_STORAGE_NO", nullable = true)
    public Integer getBatchStorageNo() {
        return batchStorageNo;
    }

    public void setBatchStorageNo(Integer batchStorageNo) {
        this.batchStorageNo = batchStorageNo;
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
    @Column(name = "STORAGE_TYPE_NO", nullable = true)
    public Integer getStorageTypeNo() {
        return storageTypeNo;
    }

    public void setStorageTypeNo(Integer storageTypeNo) {
        this.storageTypeNo = storageTypeNo;
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
    @Column(name = "PART_NO", nullable = true, length = 50)
    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
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
    @Column(name = "SEAL_TYPE", nullable = true)
    public Integer getSealType() {
        return sealType;
    }

    public void setSealType(Integer sealType) {
        this.sealType = sealType;
    }

    @Basic
    @Column(name = "OLD_SEAL_DEADLINE", nullable = true)
    public Date getOldSealDeadline() {
        return oldSealDeadline;
    }

    public void setOldSealDeadline(Date oldSealDeadline) {
        this.oldSealDeadline = oldSealDeadline;
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
    @Column(name = "BATCH_STORAGE", nullable = true, precision = 0)
    public Integer getBatchStorage() {
        return batchStorage;
    }

    public void setBatchStorage(Integer batchStorage) {
        this.batchStorage = batchStorage;
    }

    @Basic
    @Column(name = "PLAN_NUM", nullable = true, precision = 0)
    public Integer getPlanNum() {
        return planNum;
    }

    public void setPlanNum(Integer planNum) {
        this.planNum = planNum;
    }

    @Basic
    @Column(name = "RECIEVE_NUM", nullable = true, precision = 0)
    public Integer getRecieveNum() {
        return recieveNum;
    }

    public void setRecieveNum(Integer recieveNum) {
        this.recieveNum = recieveNum;
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
    @Column(name = "RETURN_NUM", nullable = true, precision = 0)
    public Integer getReturnNum() {
        return returnNum;
    }

    public void setReturnNum(Integer returnNum) {
        this.returnNum = returnNum;
    }

    @Basic
    @Column(name = "BATCH_NO", nullable = true, length = 50)
    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
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
    @Column(name = "SEAL_UPDATE", nullable = true)
    public Date getSealUpdate() {
        return sealUpdate;
    }

    public void setSealUpdate(Date sealUpdate) {
        this.sealUpdate = sealUpdate;
    }

    @Basic
    @Column(name = "SEAL_WORKER", nullable = true)
    public Integer getSealWorker() {
        return sealWorker;
    }

    public void setSealWorker(Integer sealWorker) {
        this.sealWorker = sealWorker;
    }

    @Basic
    @Column(name = "SEAL_RFID", nullable = true)
    public Integer getSealRfid() {
        return sealRfid;
    }

    public void setSealRfid(Integer sealRfid) {
        this.sealRfid = sealRfid;
    }

    @Basic
    @Column(name = "RECIEVE_WORKER", nullable = true)
    public Integer getRecieveWorker() {
        return recieveWorker;
    }

    public void setRecieveWorker(Integer recieveWorker) {
        this.recieveWorker = recieveWorker;
    }

    @Basic
    @Column(name = "RECIEVE_RFID", nullable = true)
    public Integer getRecieveRfid() {
        return recieveRfid;
    }

    public void setRecieveRfid(Integer recieveRfid) {
        this.recieveRfid = recieveRfid;
    }

    @Basic
    @Column(name = "RETURN_DATE", nullable = true)
    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    @Basic
    @Column(name = "RECIEVE_DATE", nullable = true)
    public Date getRecieveDate() {
        return recieveDate;
    }

    public void setRecieveDate(Date recieveDate) {
        this.recieveDate = recieveDate;
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
    @Column(name = "DELIVER_STAT", nullable = true)
    public Integer getDeliverStat() {
        return deliverStat;
    }

    public void setDeliverStat(Integer deliverStat) {
        this.deliverStat = deliverStat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Seal seal = (Seal) o;

        if (id != seal.id) return false;
        if (sealNo != null ? !sealNo.equals(seal.sealNo) : seal.sealNo != null) return false;
        if (batchStorageNo != null ? !batchStorageNo.equals(seal.batchStorageNo) : seal.batchStorageNo != null)
            return false;
        if (entrayNo != null ? !entrayNo.equals(seal.entrayNo) : seal.entrayNo != null) return false;
        if (checkNo != null ? !checkNo.equals(seal.checkNo) : seal.checkNo != null) return false;
        if (storageTypeNo != null ? !storageTypeNo.equals(seal.storageTypeNo) : seal.storageTypeNo != null)
            return false;
        if (contractNo != null ? !contractNo.equals(seal.contractNo) : seal.contractNo != null) return false;
        if (bookNo != null ? !bookNo.equals(seal.bookNo) : seal.bookNo != null) return false;
        if (airmaterialCode != null ? !airmaterialCode.equals(seal.airmaterialCode) : seal.airmaterialCode != null)
            return false;
        if (airmaterialName != null ? !airmaterialName.equals(seal.airmaterialName) : seal.airmaterialName != null)
            return false;
        if (airmaterialType != null ? !airmaterialType.equals(seal.airmaterialType) : seal.airmaterialType != null)
            return false;
        if (producterNo != null ? !producterNo.equals(seal.producterNo) : seal.producterNo != null) return false;
        if (producterName != null ? !producterName.equals(seal.producterName) : seal.producterName != null)
            return false;
        if (partNo != null ? !partNo.equals(seal.partNo) : seal.partNo != null) return false;
        if (level != null ? !level.equals(seal.level) : seal.level != null) return false;
        if (locationid != null ? !locationid.equals(seal.locationid) : seal.locationid != null) return false;
        if (sealType != null ? !sealType.equals(seal.sealType) : seal.sealType != null) return false;
        if (oldSealDeadline != null ? !oldSealDeadline.equals(seal.oldSealDeadline) : seal.oldSealDeadline != null)
            return false;
        if (sealDeadline != null ? !sealDeadline.equals(seal.sealDeadline) : seal.sealDeadline != null) return false;
        if (batchStorage != null ? !batchStorage.equals(seal.batchStorage) : seal.batchStorage != null) return false;
        if (planNum != null ? !planNum.equals(seal.planNum) : seal.planNum != null) return false;
        if (recieveNum != null ? !recieveNum.equals(seal.recieveNum) : seal.recieveNum != null) return false;
        if (sealNum != null ? !sealNum.equals(seal.sealNum) : seal.sealNum != null) return false;
        if (returnNum != null ? !returnNum.equals(seal.returnNum) : seal.returnNum != null) return false;
        if (batchNo != null ? !batchNo.equals(seal.batchNo) : seal.batchNo != null) return false;
        if (remark != null ? !remark.equals(seal.remark) : seal.remark != null) return false;
        if (sealUpdate != null ? !sealUpdate.equals(seal.sealUpdate) : seal.sealUpdate != null) return false;
        if (sealWorker != null ? !sealWorker.equals(seal.sealWorker) : seal.sealWorker != null) return false;
        if (sealRfid != null ? !sealRfid.equals(seal.sealRfid) : seal.sealRfid != null) return false;
        if (recieveWorker != null ? !recieveWorker.equals(seal.recieveWorker) : seal.recieveWorker != null)
            return false;
        if (recieveRfid != null ? !recieveRfid.equals(seal.recieveRfid) : seal.recieveRfid != null) return false;
        if (returnDate != null ? !returnDate.equals(seal.returnDate) : seal.returnDate != null) return false;
        if (recieveDate != null ? !recieveDate.equals(seal.recieveDate) : seal.recieveDate != null) return false;
        if (entrayStat != null ? !entrayStat.equals(seal.entrayStat) : seal.entrayStat != null) return false;
        if (deliverStat != null ? !deliverStat.equals(seal.deliverStat) : seal.deliverStat != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (sealNo != null ? sealNo.hashCode() : 0);
        result = 31 * result + (batchStorageNo != null ? batchStorageNo.hashCode() : 0);
        result = 31 * result + (entrayNo != null ? entrayNo.hashCode() : 0);
        result = 31 * result + (checkNo != null ? checkNo.hashCode() : 0);
        result = 31 * result + (storageTypeNo != null ? storageTypeNo.hashCode() : 0);
        result = 31 * result + (contractNo != null ? contractNo.hashCode() : 0);
        result = 31 * result + (bookNo != null ? bookNo.hashCode() : 0);
        result = 31 * result + (airmaterialCode != null ? airmaterialCode.hashCode() : 0);
        result = 31 * result + (airmaterialName != null ? airmaterialName.hashCode() : 0);
        result = 31 * result + (airmaterialType != null ? airmaterialType.hashCode() : 0);
        result = 31 * result + (producterNo != null ? producterNo.hashCode() : 0);
        result = 31 * result + (producterName != null ? producterName.hashCode() : 0);
        result = 31 * result + (partNo != null ? partNo.hashCode() : 0);
        result = 31 * result + (level != null ? level.hashCode() : 0);
        result = 31 * result + (locationid != null ? locationid.hashCode() : 0);
        result = 31 * result + (sealType != null ? sealType.hashCode() : 0);
        result = 31 * result + (oldSealDeadline != null ? oldSealDeadline.hashCode() : 0);
        result = 31 * result + (sealDeadline != null ? sealDeadline.hashCode() : 0);
        result = 31 * result + (batchStorage != null ? batchStorage.hashCode() : 0);
        result = 31 * result + (planNum != null ? planNum.hashCode() : 0);
        result = 31 * result + (recieveNum != null ? recieveNum.hashCode() : 0);
        result = 31 * result + (sealNum != null ? sealNum.hashCode() : 0);
        result = 31 * result + (returnNum != null ? returnNum.hashCode() : 0);
        result = 31 * result + (batchNo != null ? batchNo.hashCode() : 0);
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        result = 31 * result + (sealUpdate != null ? sealUpdate.hashCode() : 0);
        result = 31 * result + (sealWorker != null ? sealWorker.hashCode() : 0);
        result = 31 * result + (sealRfid != null ? sealRfid.hashCode() : 0);
        result = 31 * result + (recieveWorker != null ? recieveWorker.hashCode() : 0);
        result = 31 * result + (recieveRfid != null ? recieveRfid.hashCode() : 0);
        result = 31 * result + (returnDate != null ? returnDate.hashCode() : 0);
        result = 31 * result + (recieveDate != null ? recieveDate.hashCode() : 0);
        result = 31 * result + (entrayStat != null ? entrayStat.hashCode() : 0);
        result = 31 * result + (deliverStat != null ? deliverStat.hashCode() : 0);
        return result;
    }
}
