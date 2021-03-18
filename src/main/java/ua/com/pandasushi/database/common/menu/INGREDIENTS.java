package ua.com.pandasushi.database.common.menu;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Тарас on 10.01.2017.
 */

@Entity
@Cacheable
@Table(name = "LIB_INGREDIENTS")
public class INGREDIENTS implements Serializable {

    @Id
    @Column(name = "INGREDIENT_ID")
    private Integer ingredientId;

    @Column(name = "INGREDIENT_NAME")
    private String ingredientName;

    @Column(name = "UNITS")
    private String units;

    @Column(name = "EXPIRED_PERIOD")
    private Integer expiredPeriod;

    @Column(name = "EXPIRED_PERIOD_MIN")
    private Integer expiredPeriodMin;

    @Column(name = "IMAGE_URL")
    private String imageUrl;

    @Column(name = "SYHIV_IMPORTANCE")
    private Integer syhivImportance;

    @Column(name = "VARSHAV_IMPORTANCE")
    private Integer varshavImportance;

    @Column(name = "IS_ON_INVENTORY")
    private Boolean onInventary;

    @Column(name = "IS_ON_CAFE_INVENTORY")
    private Boolean onCafeInventory;

    public INGREDIENTS() {
        super();
    }

    public Integer getExpiredPeriodMin() {
        return expiredPeriodMin;
    }

    public void setExpiredPeriodMin(Integer expiredPeriodMin) {
        this.expiredPeriodMin = expiredPeriodMin;
    }

    public Integer getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Integer ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public Integer getExpiredPeriod() {
        return expiredPeriod;
    }

    public void setExpiredPeriod(Integer expiredPeriod) {
        this.expiredPeriod = expiredPeriod;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getSyhivImportance() {
        return syhivImportance;
    }

    public void setSyhivImportance(Integer syhivImportance) {
        this.syhivImportance = syhivImportance;
    }

    public Integer getVarshavImportance() {
        return varshavImportance;
    }

    public void setVarshavImportance(Integer varshavImportance) {
        this.varshavImportance = varshavImportance;
    }

    public Boolean getOnInventary() {
        return onInventary;
    }

    public void setOnInventary(Boolean onInventary) {
        this.onInventary = onInventary;
    }

    public Boolean getOnCafeInventory() {
        return onCafeInventory;
    }

    public void setOnCafeInventory(Boolean onCafeInventory) {
        this.onCafeInventory = onCafeInventory;
    }

    @Override
    public String toString() {
        return "INGREDIENTS{" +
                "ingredientId=" + ingredientId +
                ", ingredientName='" + ingredientName + '\'' +
                ", units='" + units + '\'' +
                ", expiredPeriod=" + expiredPeriod +
                ", imageUrl='" + imageUrl + '\'' +
                ", syhivImportance=" + syhivImportance +
                ", varshavImportance=" + varshavImportance +
                '}';
    }
}
