package ua.com.pandasushi.database.common.menu;


import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Тарас on 10.01.2017.
 */
@Entity
@Cacheable
@org.hibernate.annotations.Cache(include = "all", usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "LIB_PRODUCTS_INGREDIENTS")
public class PRODUCTS_INGREDIENTS implements Serializable {
    @Id
    @Column(name = "PROD_ING_ID")
    private Integer prodIngId;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "PRODUCT_ID")
    private Integer productId;

    @Column(name = "INGREDIENT_NAME")
    private String ingredientName;

    @Column(name = "INGREDIENT_ID")
    private Integer ingredientId;

    @Column(name = "UNITS")
    private String units;

    @Column(name = "LAST_CHECK")
    private Date lastCheck;

    @Column(name = "LAST_COEF")
    private Float lastCoef;

    @Column(name = "AVERAGE_COEF")
    private Float avgCoef;

    @Column(name = "AVERAGE_PRICE_FOR_UNIT")
    private Float avgPrice;

    @Column(name = "AUTO_ROZROBKA")
    private Boolean auto;

    public PRODUCTS_INGREDIENTS () {
        super();
    }

    public Integer getProdIngId() {
        return prodIngId;
    }

    public void setProdIngId(Integer prodIngId) {
        this.prodIngId = prodIngId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public Integer getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Integer ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public Date getLastCheck() {
        return lastCheck;
    }

    public void setLastCheck(Date lastCheck) {
        this.lastCheck = lastCheck;
    }

    public Float getLastCoef() {
        return lastCoef;
    }

    public void setLastCoef(Float lastCoef) {
        this.lastCoef = lastCoef;
    }

    public Float getAvgCoef() {
        return avgCoef;
    }

    public void setAvgCoef(Float avgCoef) {
        this.avgCoef = avgCoef;
    }

    public Float getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(Float avgPrice) {
        this.avgPrice = avgPrice;
    }

    public Boolean getAuto() {
        return auto;
    }

    public void setAuto(Boolean auto) {
        this.auto = auto;
    }

    @Override
    public String toString() {
        return "PRODUCTS_INGREDIENTS{" +
                "prodIngId=" + prodIngId +
                ", productName='" + productName + '\'' +
                ", productId=" + productId +
                ", ingredientName='" + ingredientName + '\'' +
                ", ingredientId=" + ingredientId +
                ", units='" + units + '\'' +
                ", lastCheck=" + lastCheck +
                ", lastCoef=" + lastCoef +
                ", avgCoef=" + avgCoef +
                ", avgPrice=" + avgPrice +
                ", auto=" + auto +
                '}';
    }
}
