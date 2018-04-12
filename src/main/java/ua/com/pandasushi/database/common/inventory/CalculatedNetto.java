package ua.com.pandasushi.database.common.inventory;

import java.io.Serializable;
import java.util.Calendar;

public class CalculatedNetto implements Serializable {
    private Calendar lastInventoryDate;
    private Integer lastInventoryNetto;
    private Integer productPurchaseNetto;
    private Integer shiftNetto;
    private Integer writeOffNetto;
    private Integer diffProcessing;
    private Integer consumptionNetto;
    private Integer calculatedNetto;

    public CalculatedNetto() {
    }

    public Calendar getLastInventoryDate() {
        return lastInventoryDate;
    }

    public void setLastInventoryDate(Calendar lastInventoryDate) {
        this.lastInventoryDate = lastInventoryDate;
    }

    public Integer getLastInventoryNetto() {
        return lastInventoryNetto;
    }

    public void setLastInventoryNetto(Integer lastInventoryNetto) {
        this.lastInventoryNetto = lastInventoryNetto;
    }

    public Integer getProductPurchaseNetto() {
        return productPurchaseNetto;
    }

    public void setProductPurchaseNetto(Integer productPurchaseNetto) {
        this.productPurchaseNetto = productPurchaseNetto;
    }

    public Integer getShiftNetto() {
        return shiftNetto;
    }

    public void setShiftNetto(Integer shiftNetto) {
        this.shiftNetto = shiftNetto;
    }

    public Integer getWriteOffNetto() {
        return writeOffNetto;
    }

    public void setWriteOffNetto(Integer writeOffNetto) {
        this.writeOffNetto = writeOffNetto;
    }

    public Integer getDiffProcessing() {
        return diffProcessing;
    }

    public void setDiffProcessing(Integer diffProcessing) {
        this.diffProcessing = diffProcessing;
    }

    public Integer getConsumptionNetto() {
        return consumptionNetto;
    }

    public void setConsumptionNetto(Integer consumptionNetto) {
        this.consumptionNetto = consumptionNetto;
    }

    public Integer getCalculatedNetto() {
        return calculatedNetto;
    }

    public void setCalculatedNetto(Integer calculatedNetto) {
        this.calculatedNetto = calculatedNetto;
    }
}