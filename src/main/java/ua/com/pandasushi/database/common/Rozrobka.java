package ua.com.pandasushi.database.common;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Тарас on 07.03.2017.
 */
@Entity
@Cacheable
@org.hibernate.annotations.Cache(include = "all", usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "ROZROBKA")
public class Rozrobka implements Serializable {
    @Id
    @Column(name = "ROZROBKA_ID")
    private long rozrobkaId;

    @Column(name = "DATE")
    private Date date;

    @Column(name = "KITCHEN")
    private int kitchen;

    @Column(name = "COOK")
    private String cook;

    @Column(name = "PRODUCT_ID")
    private int productId;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "COUNT_U1")
    private int countU1;

    @Column(name = "COUNT_U2")
    private int countU2;

    @Column(name = "INGREDIENT_ID")
    private int ingredientId;

    @Column(name = "INGREDIENT_NAME")
    private String ingredientName;

    @Column(name = "COUNT_ING")
    private int countIng;

    @Column(name = "COEF")
    private float coef;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "COMMENT")
    private String comment;

    public long getRozrobkaId() {
        return rozrobkaId;
    }

    public void setRozrobkaId(long rozrobkaId) {
        this.rozrobkaId = rozrobkaId;
    }

    public Date getDate() {
        return new Date(date.getTime());
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getKitchen() {
        return kitchen;
    }

    public void setKitchen(int kitchen) {
        this.kitchen = kitchen;
    }

    public String getCook() {
        return cook;
    }

    public void setCook(String cook) {
        this.cook = cook;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getCountU1() {
        return countU1;
    }

    public void setCountU1(int countU1) {
        this.countU1 = countU1;
    }

    public int getCountU2() {
        return countU2;
    }

    public void setCountU2(int countU2) {
        this.countU2 = countU2;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public int getCountIng() {
        return countIng;
    }

    public void setCountIng(int countIng) {
        this.countIng = countIng;
    }

    public float getCoef() {
        return coef;
    }

    public void setCoef(float coef) {
        this.coef = coef;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
