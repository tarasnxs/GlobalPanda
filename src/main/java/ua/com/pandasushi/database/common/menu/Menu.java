package ua.com.pandasushi.database.common.menu;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Cacheable
@org.hibernate.annotations.Cache(include = "all", usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "LIB_MENU")
public class Menu implements Serializable {

	@Id
	@Column(name = "DISH_ID")
	private int code;

	@Column(name = "CHARCODE")
	private String sCode;

	@Column(name = "DISH_NAME")
	private String dish;

	@Column(name = "UNITS_CHECK")
	private String units;

	@Column(name = "UNITS_TEHCARD")
	private String unitsTehcard;

	@Column(name = "WEIGHT")
	private int weight;

	@Column(name = "PRICE")
	private int price;

	@Column(name = "GINGER")
	private int ginger;

	@Column(name = "COLDHOTADDS")
	private String coldHotAdds;

	@Column(name = "UNAGI")
	private int unagi;

	@Column(name = "PIZZASUSHIDRINKS")
	private String pizzaSushiDrinks;

	@Column(name = "DIRECTION")
	private String direction;

	@Column(name = "IMAGE_URL")
	private String imageUrl;

	@Column(name = "GROUP_NAME")
	private String groupName;

	@Column(name = "GROUP_ID")
	private Integer group_id;

	public Menu() {
		super();
	}
	
	public Menu(int code, String sCode, String dish, String units, int weight, int price, int ginger, String coldHotAdds,
			int unagi, String pizzaSushiDrinks) {
		super();
		this.code = code;
		this.sCode = sCode;
		this.dish = dish;
		this.units = units;
		this.weight = weight;
		this.price = price;
		this.ginger = ginger;
		this.coldHotAdds = coldHotAdds;
		this.unagi = unagi;
		this.pizzaSushiDrinks = pizzaSushiDrinks;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	public String getSCode() {
		return sCode;
	}

	public void setSCode(String sCode) {
		this.sCode = sCode;
	}

	public String getDish() {
		return dish;
	}

	public void setDish(String dish) {
		this.dish = dish;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getGinger() {
		return ginger;
	}

	public void setGinger(int ginger) {
		this.ginger = ginger;
	}

	public String getColdHotAdds() {
		return coldHotAdds;
	}

	public void setColdHotAdds(String coldHotAdds) {
		this.coldHotAdds = coldHotAdds;
	}

	public int getUnagi() {
		return unagi;
	}

	public void setUnagi(int unagi) {
		this.unagi = unagi;
	}

	public String getPizzaSushiDrinks() {
		return pizzaSushiDrinks;
	}

	public void setPizzaSushiDrinks(String pizzaSushiDrinks) {
		this.pizzaSushiDrinks = pizzaSushiDrinks;
	}

	public String getUnitsTehcard() {
		return unitsTehcard;
	}

	public void setUnitsTehcard(String unitsTehcard) {
		this.unitsTehcard = unitsTehcard;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Integer getGroup_id() {
		return group_id;
	}

	public void setGroup_id(Integer group_id) {
		this.group_id = group_id;
	}

	@Override
	public String toString() {
		return "Menu{" +
				"code=" + code +
				", sCode='" + sCode + '\'' +
				", dish='" + dish + '\'' +
				", units='" + units + '\'' +
				", unitsTehcard='" + unitsTehcard + '\'' +
				", weight=" + weight +
				", price=" + price +
				", ginger=" + ginger +
				", coldHotAdds='" + coldHotAdds + '\'' +
				", unagi=" + unagi +
				", pizzaSushiDrinks='" + pizzaSushiDrinks + '\'' +
				", direction='" + direction + '\'' +
				", imageUrl='" + imageUrl + '\'' +
				", groupName='" + groupName + '\'' +
				", group_id=" + group_id +
				'}';
	}
}
