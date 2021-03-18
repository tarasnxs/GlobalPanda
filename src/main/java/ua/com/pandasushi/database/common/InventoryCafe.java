package ua.com.pandasushi.database.common;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Тарас on 15.03.2017.
 */
@Entity
@Cacheable
@Table(name = "INVENTORY_CAFE")
public class InventoryCafe implements Serializable {

    @Id
    @Column( name = "INVENTORY_ID" )
    private Long inventoryId;

    @Column( name = "CHECK_ID" )
    private Integer checkId;

    @Column( name = "KITCHEN" )
    private Integer kitchen;

    @Column( name = "COOK" )
    private String cook;

    @Column( name = "PREVIOUS_DATE" )
    private Date previous;

    @Column( name = "TIME_START")
    private Date timeStart;

    @Column( name = "DATE_START" )
    private Date begin;

    @Column( name = "DATE_ATTEMPT" )
    private Date attemptTime;

    @Column( name = "DATE_END" )
    private Date end;

    @Column( name = "BASIC_ING" )
    private Integer basicIng;

    @Column( name = "PROD_ING_ID" )
    private Integer prodIngId;

    @Column( name = "PROD_ING_NAME" )
    private String prodIngName;

    @Column ( name = "UNIT1" )
    private String unit1;

    @Column ( name = "PROD_RELATION")
    private Integer prodRelation;

    @Column( name = "ATTEMPT_U1" )
    private Integer attemptU1;

    @Column( name = "ATTEMPT_U2" )
    private Integer attemptU2;

    @Column( name = "FACT_U1" )
    private Integer factU1;

    @Column( name = "FACT_U2" )
    private Integer factU2;

    @Column( name = "FIRST_ATTEMPT" )
    private Boolean firstAttempt;

    @Column( name = "PREVIOUS_NETTO" )
    private Integer previousNetto;

    @Column( name = "PRODUCT_PURCHASE")
    private Integer productPurchase;

    @Column( name = "PRODUCT_SHIFT" )
    private Integer productShift;

    @Column( name = "WRITE_OFF_NETTO" )
    private Integer writeOffNetto;

    @Column( name = "ROZROBKA_OUT" )
    private Integer rozrobkaOut;

    @Column( name = "ROZROBKA" )
    private Integer rozrobka;

    @Column( name = "ROZHID")
    private Integer rozhid;

    @Column( name = "CALCULATED_NETTO" )
    private Integer calculatedNetto;

    @Column( name = "INPUT_NETTO" )
    private Integer inputNetto;

    @Column( name = "CALCULATED_CONS")
    private Integer calculatedCons;

    @Column( name = "DIFF_NETTO" )
    private Integer diffNetto;

    @Column( name = "DIFF_PERCENT" )
    private Float diffPercent;

    @Column( name = "ING_PRICE")
    private Float ingPrice;

    @Column( name = "DIFF_UAH" )
    private Integer diffUah;

    @Column( name = "DIFF_COMPENSATION")
    private Integer diffCompensation;

    @Column( name = "COMMENT" )
    private String comment;
    
    @Column( name = "ADDED_IMPORTANCE" )
    private Integer addedImportance;
    
    @Column( name = "BALANCE_IMPORTANCE" )
    private Integer balanceImportance;
    

    public Integer getAddedImportance() {
		return addedImportance;
	}

	public void setAddedImportance(Integer addedImportance) {
		this.addedImportance = addedImportance;
	}

	public Integer getBalanceImportance() {
		return balanceImportance;
	}

	public void setBalanceImportance(Integer balanceImportance) {
		this.balanceImportance = balanceImportance;
	}

	public Long getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Long inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Integer getCheckId() {
        return checkId;
    }

    public void setCheckId(Integer checkId) {
        this.checkId = checkId;
    }

    public Integer getKitchen() {
        return kitchen;
    }

    public void setKitchen(Integer kitchen) {
        this.kitchen = kitchen;
    }

    public String getCook() {
        return cook;
    }

    public void setCook(String cook) {
        this.cook = cook;
    }

    public Date getPrevious() {
        return new Date(previous.getTime());
    }

    public void setPrevious(Date previous) {
        this.previous = previous;
    }

    public Date getBegin() {
        return new Date(begin.getTime());
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getTimeStart() {
        return new Date(timeStart.getTime());
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getAttemptTime() {
        return new Date(attemptTime.getTime());
    }

    public void setAttemptTime(Date attemptTime) {
        this.attemptTime = attemptTime;
    }

    public Date getEnd() {
        return new Date(end.getTime());
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Integer getBasicIng() {
        return basicIng;
    }

    public void setBasicIng(Integer basicIng) {
        this.basicIng = basicIng;
    }

    public Integer getProdIngId() {
        return prodIngId;
    }

    public void setProdIngId(Integer prodIngId) {
        this.prodIngId = prodIngId;
    }

    public String getProdIngName() {
        return prodIngName;
    }

    public void setProdIngName(String prodIngName) {
        this.prodIngName = prodIngName;
    }

    public Integer getAttemptU1() {
        return attemptU1;
    }

    public void setAttemptU1(Integer attemptU1) {
        this.attemptU1 = attemptU1;
    }

    public Integer getAttemptU2() {
        return attemptU2;
    }

    public void setAttemptU2(Integer attemptU2) {
        this.attemptU2 = attemptU2;
    }

    public Integer getFactU1() {
        return factU1;
    }

    public void setFactU1(Integer factU1) {
        this.factU1 = factU1;
    }

    public Integer getFactU2() {
        return factU2;
    }

    public void setFactU2(Integer factU2) {
        this.factU2 = factU2;
    }

    public Boolean getFirstAttempt() {
        return firstAttempt;
    }

    public void setFirstAttempt(Boolean firstAttempt) {
        this.firstAttempt = firstAttempt;
    }

    public Integer getProductPurchase() {
        return productPurchase;
    }

    public void setProductPurchase(Integer productPurchase) {
        this.productPurchase = productPurchase;
    }

    public Integer getProductShift() {
        return productShift;
    }

    public void setProductShift(Integer productShift) {
        this.productShift = productShift;
    }

    public Integer getRozrobka() {
        return rozrobka;
    }

    public void setRozrobka(Integer rozrobka) {
        this.rozrobka = rozrobka;
    }

    public Integer getRozhid() {
        return rozhid;
    }

    public void setRozhid(Integer rozhid) {
        this.rozhid = rozhid;
    }

    public Integer getCalculatedNetto() {
        return calculatedNetto;
    }

    public void setCalculatedNetto(Integer calculatedNetto) {
        this.calculatedNetto = calculatedNetto;
    }

    public Integer getCalculatedCons() {
        return calculatedCons;
    }

    public void setCalculatedCons(Integer calculatedCons) {
        this.calculatedCons = calculatedCons;
    }

    public Integer getDiffNetto() {
        return diffNetto;
    }

    public void setDiffNetto(Integer diffNetto) {
        this.diffNetto = diffNetto;
    }

    public Float getDiffPercent() {
        return diffPercent;
    }

    public void setDiffPercent(Float diffPercent) {
        this.diffPercent = diffPercent;
    }

    public Integer getDiffUah() {
        return diffUah;
    }

    public void setDiffUah(Integer diffUah) {
        this.diffUah = diffUah;
    }

    public Integer getDiffCompensation() {
        return diffCompensation;
    }

    public void setDiffCompensation(Integer diffCompensation) {
        this.diffCompensation = diffCompensation;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUnit1() {
        return unit1;
    }

    public void setUnit1(String unit1) {
        this.unit1 = unit1;
    }

    public Integer getProdRelation() {
        return prodRelation;
    }

    public void setProdRelation(Integer prodRelation) {
        this.prodRelation = prodRelation;
    }

    public Integer getRozrobkaOut() {
        return rozrobkaOut;
    }

    public void setRozrobkaOut(Integer rozrobkaOut) {
        this.rozrobkaOut = rozrobkaOut;
    }



    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
        String kitch;
        switch (getKitchen()) {
            case 0:
                kitch = "Сихів";
                break;

            case 1:
                kitch = "Варшавська";
                break;

            case 5:
                kitch = "Садова";
                break;

            default:
                kitch = "Введіть назву кухні";
                break;
        }
        return sdf.format(getBegin()) + " - " + kitch;
    }

    public Integer getPreviousNetto() {
        return previousNetto;
    }

    public void setPreviousNetto(Integer previousNetto) {
        this.previousNetto = previousNetto;
    }

    public Integer getWriteOffNetto() {
        return writeOffNetto;
    }

    public void setWriteOffNetto(Integer writeOffNetto) {
        this.writeOffNetto = writeOffNetto;
    }

    public Float getIngPrice() {
        return ingPrice;
    }

    public void setIngPrice(Float ingPrice) {
        this.ingPrice = ingPrice;
    }

    public Integer getInputNetto() {
        return inputNetto;
    }

    public void setInputNetto(Integer inputNetto) {
        this.inputNetto = inputNetto;
    }
}
