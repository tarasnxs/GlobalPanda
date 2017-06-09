package ua.com.pandasushi.database.common.menu;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Тарас on 10.01.2017.
 */

@Entity
@Cacheable
@org.hibernate.annotations.Cache(include = "all", usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "LIB_PRODUCTS")
public class PRODUCTS implements Serializable {


    @Id
    @Column(name = "PRODUCT_ID")
    private Integer productId;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "FIRST_UNITS")
    private String firstUnits;

    @Column(name = "SECOND_UNITS")
    private String secondUnits;

    @Column(name = "UNITS_RELATION")
    private Integer unitsRelation;

    @Column(name = "EXPIRED_PERIOD")
    private Integer expiredPeriod;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "LAST_PRICE_CUR")
    private Float lastPriceCur;

    @Column(name = "AVG_PRICE_CUR")
    private Float avgPriceCur;

    @Column(name = "CUR_TO_UAH_TODAY")
    private Float curToUah;

    @Column(name = "LAST_PRICE_UAH")
    private Float lastPriceUah;

    @Column(name = "AVG_PRICE_UAH")
    private Float avgPriceUah;

    @Column(name = "CHECKED")
    private Boolean checked;

    public PRODUCTS () {
        super();
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getFirstUnits() {
        return firstUnits;
    }

    public void setFirstUnits(String firstUnits) {
        this.firstUnits = firstUnits;
    }

    public String getSecondUnits() {
        return secondUnits;
    }

    public void setSecondUnits(String secondUnits) {
        this.secondUnits = secondUnits;
    }

    public Integer getUnitsRelation() {
        return unitsRelation;
    }

    public void setUnitsRelation(Integer unitsRelation) {
        this.unitsRelation = unitsRelation;
    }

    public Integer getExpiredPeriod() {
        return expiredPeriod;
    }

    public void setExpiredPeriod(Integer expiredPeriod) {
        this.expiredPeriod = expiredPeriod;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Float getLastPriceCur() {
        return lastPriceCur;
    }

    public void setLastPriceCur(Float lastPriceCur) {
        this.lastPriceCur = lastPriceCur;
    }

    public Float getAvgPriceCur() {
        return avgPriceCur;
    }

    public void setAvgPriceCur(Float avgPriceCur) {
        this.avgPriceCur = avgPriceCur;
    }

    public Float getCurToUah() {
        return curToUah;
    }

    public void setCurToUah(Float curToUah) {
        this.curToUah = curToUah;
    }

    public Float getLastPriceUah() {
        return lastPriceUah;
    }

    public void setLastPriceUah(Float lastPriceUah) {
        this.lastPriceUah = lastPriceUah;
    }

    public Float getAvgPriceUah() {
        return avgPriceUah;
    }

    public void setAvgPriceUah(Float avgPriceUah) {
        this.avgPriceUah = avgPriceUah;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "PRODUCTS{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", firstUnits='" + firstUnits + '\'' +
                ", secondUnits='" + secondUnits + '\'' +
                ", unitsRelation=" + unitsRelation +
                ", expiredPeriod=" + expiredPeriod +
                ", currency='" + currency + '\'' +
                ", lastPriceCur=" + lastPriceCur +
                ", avgPriceCur=" + avgPriceCur +
                ", curToUah=" + curToUah +
                ", lastPriceUah=" + lastPriceUah +
                ", avgPriceUah=" + avgPriceUah +
                '}';
    }
}
