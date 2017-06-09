package ua.com.pandasushi.database.common.menu;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Тарас on 09.01.2017.
 */

@Entity
@Cacheable
@org.hibernate.annotations.Cache(include = "all", usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "LIB_TEHCARDS")
public class TEHCARDS implements Serializable {

    @Id
    @Column(name = "TEHCARD_ID")
    private Integer tehcard_id;

    @Column(name = "DISH_NAME")
    private String dishName;

    @Column(name = "DISH_ID")
    private Integer dishId;

    @Column(name = "INGREDIENT_NAME")
    private String ingredientName;

    @Column(name = "INGREDIENT_ID")
    private Integer ingredientId;

    @Column(name = "UNITS")
    private String units;

    @Column(name = "COUNT")
    private Float count;

    @Column(name = "USUSHKA")
    private Integer usushka;

    @Column(name = "FINAL_WEIGHT")
    private Integer finalWeight;

    public TEHCARDS() {

    }

    public Integer getTehcard_id() {
        return tehcard_id;
    }

    public void setTehcard_id(Integer tehcard_id) {
        this.tehcard_id = tehcard_id;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public Integer getDishId() {
        return dishId;
    }

    public void setDishId(Integer dishId) {
        this.dishId = dishId;
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

    public Float getCount() {
        return count;
    }

    public void setCount(Float count) {
        this.count = count;
    }

    public Integer getUsushka() {
        return usushka;
    }

    public void setUsushka(Integer usushka) {
        this.usushka = usushka;
    }

    public Integer getFinalWeight() {
        return finalWeight;
    }

    public void setFinalWeight(Integer finalWeight) {
        this.finalWeight = finalWeight;
    }

    @Override
    public String toString() {
        return "TehcardsDAO{" +
                "tehcard_id=" + tehcard_id +
                ", dishName='" + dishName + '\'' +
                ", dishId=" + dishId +
                ", ingredientName='" + ingredientName + '\'' +
                ", ingredientId=" + ingredientId +
                ", units='" + units + '\'' +
                ", count=" + count +
                ", usushka=" + usushka +
                ", finalWeight=" + finalWeight +
                '}';
    }
}
