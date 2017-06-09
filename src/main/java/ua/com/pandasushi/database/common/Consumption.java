package ua.com.pandasushi.database.common;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@Entity
@Cacheable
@org.hibernate.annotations.Cache(include = "all", usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "Consumption")
public class Consumption implements Serializable {
	@Id
	@Column(name = "ID")
	private long id;
	
	@Column(name = "CHARCODE")
	private String charcode;
	
	@Column(name = "KITCHEN")
	private int kitchen;
	
	@Column(name = "DATE")
	private Date date;
	
	@Column(name = "CUSTOMER_ID")
	private long customerID;
	
	@Column(name = "ORDER_ID")
	private long orderID;
	
	@Column(name = "COUNT")
	private int count;
	
	@Column(name = "COMBO_NAME")
	private String comboName;
	
	@Column(name = "COMBO_ID")
	private int comboId;
	
	@Column(name = "DISH_NAME")
	private String dishName;
	
	@Column(name = "DISH_ID")
	private int dishId;
	
	@Column(name = "INGREDIENT_NAME")
	private String ingredientName;
	
	@Column(name = "INGREDIENT_ID")
	private int ingredientId;
	
	@Column(name = "WEIGHT")
	private float weight;
	
	@Column(name = "UNITS")
	private String units;
	
	public Consumption() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCharcode() {
		return charcode;
	}

	public void setCharcode(String charcode) {
		this.charcode = charcode;
	}

	public int getKitchen() {
		return kitchen;
	}

	public void setKitchen(int kitchen) {
		this.kitchen = kitchen;
	}

	public Date getDate() {
		return new Date(date.getTime());
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public long getCustomerID() {
		return customerID;
	}

	public void setCustomerID(long customerID) {
		this.customerID = customerID;
	}

	public long getOrderID() {
		return orderID;
	}

	public void setOrderID(long orderID) {
		this.orderID = orderID;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getComboName() {
		return comboName;
	}

	public void setComboName(String comboName) {
		this.comboName = comboName;
	}

	public int getComboId() {
		return comboId;
	}

	public void setComboId(int comboId) {
		this.comboId = comboId;
	}

	public String getDishName() {
		return dishName;
	}

	public void setDishName(String dishName) {
		this.dishName = dishName;
	}

	public int getDishId() {
		return dishId;
	}

	public void setDishId(int dishId) {
		this.dishId = dishId;
	}
	
	

	public String getIngredientName() {
		return ingredientName;
	}

	public void setIngredientName(String ingredientName) {
		this.ingredientName = ingredientName;
	}

	public int getIngredientId() {
		return ingredientId;
	}

	public void setIngredientId(int ingredientId) {
		this.ingredientId = ingredientId;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	@Override
	public String toString() {
		return "Consumption [id=" + id + ", charcode=" + charcode + ", kitchen=" + kitchen + ", date=" + date
				+ ", customerID=" + customerID + ", orderID=" + orderID + ", count=" + count + ", comboName="
				+ comboName + ", comboId=" + comboId + ", dishName=" + dishName + ", dishId=" + dishId
				+ ", ingredientName=" + ingredientName + ", ingredientId=" + ingredientId + ", weight=" + weight
				+ ", units=" + units + "]";
	}
	
	
	
}
