package ua.com.pandasushi.database.common;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "INV_BALANCE")
public class InventoryBalance implements Serializable {
    public static final int ALL_LVIV = -1;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;
    
    @Column(name = "DATE")
    private Date date = new Date(0);

    @Column(name = "INGREDIENT")
    private Integer ingredient;

    @Column(name = "KITCHEN")
    private Integer kitchen;

    @Column(name = "ING_COST")
    private Float ingCost = 0.0f;

    @Column(name = "NETTO")
    private Integer netto = 0;

    @Column(name = "PURCHASE")
    private Integer purchase = 0;

    @Column(name = "PURCHASE_UAH")
    private Integer purchaseUah = 0;

    @Column(name = "SHIFT")
    private Integer shift = 0;

    @Column(name = "SHIFT_UAH")
    private Integer shiftUah = 0;

    @Column(name = "WRITE_OFF")
    private Integer writeOff = 0;

    @Column(name = "WRITE_OFF_UAH")
    private Integer writeOffUah = 0;

    @Column(name = "CONSUMPTION")
    private Integer consumption = 0;

    @Column(name = "CONSUMPTION_UAH")
    private Integer consumptionUah = 0;

    @Column(name = "INV_DIFF")
    private Integer invDiff = 0;

    @Column(name = "INV_DIFF_UAH")
    private Integer invDiffUah = 0;


    public InventoryBalance() {
    }

    public InventoryBalance(Integer ingId, Integer kitch) {
        this.ingredient = ingId;
        this.kitchen = kitch;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getIngredient() {
        return ingredient;
    }

    public void setIngredient(Integer ingredient) {
        this.ingredient = ingredient;
    }

    public Integer getKitchen() {
        return kitchen;
    }

    public void setKitchen(Integer kitchen) {
        this.kitchen = kitchen;
    }

    public Float getIngCost() {
        return ingCost;
    }

    public void setIngCost(Float ingCost) {
        this.ingCost = ingCost;
    }

    public Integer getNetto() {
        return netto;
    }

    public void setNetto(Integer netto) {
        this.netto = netto;
    }

    public Integer getPurchase() {
        return purchase;
    }

    public void setPurchase(Integer purchase) {
        this.purchase = purchase;
    }

    public Integer getPurchaseUah() {
        return purchaseUah;
    }

    public void setPurchaseUah(Integer purchaseUah) {
        this.purchaseUah = purchaseUah;
    }

    public Integer getShift() {
        return shift;
    }

    public void setShift(Integer shift) {
        this.shift = shift;
    }

    public Integer getShiftUah() {
        return shiftUah;
    }

    public void setShiftUah(Integer shiftUah) {
        this.shiftUah = shiftUah;
    }

    public Integer getWriteOff() {
        return writeOff;
    }

    public void setWriteOff(Integer writeOff) {
        this.writeOff = writeOff;
    }

    public Integer getWriteOffUah() {
        return writeOffUah;
    }

    public void setWriteOffUah(Integer writeOffUah) {
        this.writeOffUah = writeOffUah;
    }

    public Integer getConsumption() {
        return consumption;
    }

    public void setConsumption(Integer consumption) {
        this.consumption = consumption;
    }

    public Integer getConsumptionUah() {
        return consumptionUah;
    }

    public void setConsumptionUah(Integer consumptionUah) {
        this.consumptionUah = consumptionUah;
    }

    public Integer getInvDiff() {
        return invDiff;
    }

    public void setInvDiff(Integer invDiff) {
        this.invDiff = invDiff;
    }

    public Integer getInvDiffUah() {
        return invDiffUah;
    }

    public void setInvDiffUah(Integer invDiffUah) {
        this.invDiffUah = invDiffUah;
    }
}