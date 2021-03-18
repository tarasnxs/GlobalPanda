package ua.com.pandasushi.database.common;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Cacheable
@Table(name = "ConsumptionCafe")
public class ConsumptionCafe implements Serializable {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "KITCHEN")
	private int kitchen;

	@Column(name = "DATE")
	private Date date;

	@Column(name = "COUNT")
	private int count;

	@Column(name = "DISH_ID")
    private Integer dishId;

	@Column(name = "DISH_NAME")
	private String dishName;

	@Column(name = "MODIFICATION_ID")
    private Integer modificationId;

	@Column(name = "MODIFICATION_NAME")
    private String modificationName;

    @Column(name = "INGREDIENT_ID")
    private int ingredientId;

	@Column(name = "INGREDIENT_NAME")
	private String ingredientName;

	@Column(name = "WEIGHT")
	private float weight;

	public ConsumptionCafe() {
		
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getKitchen() {
        return kitchen;
    }

    public void setKitchen(int kitchen) {
        this.kitchen = kitchen;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Integer getDishId() {
        return dishId;
    }

    public void setDishId(Integer dishId) {
        this.dishId = dishId;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public Integer getModificationId() {
        return modificationId;
    }

    public void setModificationId(Integer modificationId) {
        this.modificationId = modificationId;
    }

    public String getModificationName() {
        return modificationName;
    }

    public void setModificationName(String modificationName) {
        this.modificationName = modificationName;
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

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}
