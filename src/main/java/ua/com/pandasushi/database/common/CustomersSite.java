package ua.com.pandasushi.database.common;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Cacheable
@Table(name = "CustomersSite")
public class CustomersSite implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -5938508954437913485L;

	@Id
	@Column(name = "SITE_ID")
	private Long site_id;

	@Column(name = "CHARCODE")
	private String charCode;

	@Column(name = "DATE")
	private Date date;

	@Column(name = "KITCHEN")
	private int kitchen;

	@Column(name = "COOK")
	private String cook;

	@Column(name = "OPERATOR")
	private String operator;

	@Column(name = "PARTNER")
	private String partner;

	@Column(name = "PHONE")
	private String phone;

	@Column(name = "NAME")
	private String name;

	@Column(name = "EMAIL", updatable = false)
	private String email;

	@Column(name = "STREET")
	private String street;

	@Column(name = "HOUSE")
	private String house;

	@Column(name = "APARTAMENT")
	private String apartament;

	@Column(name = "PORCH")
	private String porch;

	@Column(name = "FLOOR")
	private String floor;

	@Column(name = "DOORCODE")
	private String doorcode;

	@Column(name = "REGION")
	private int region;

	@Column(name = "COMMENT")
	private String comment;

	@Column(name = "DISCOUNT")
	private int discount;

	@Column(name = "PEOPLE")
	private int people;

	@Column(name = "INFO")
	private String info;

	@Column(name = "ON_TIME")
	private boolean onTime;

	@Column(name = "DELIVERY_TIME")
	private Date deliveryTime;

	@Column(name = "COST")
	private int cost;

	@Column(name = "BONUS")
	private int bonus;

	@Column(name = "NEW_BONUS")
	private int newBonus;

	@Column(name = "FINAL_COST")
	private int fianlCost;

	@Column(name = "COURIER")
	private String courier;

	@Column(name = "SEND_TIME")
	private Date sendTime;

	@Column(name = "DELIVER_TIME")
	private Date deliverTime;

	@Column(name = "DELIVER_COST")
	private int deliverCost;

	@Column(name = "CANCEL_REASON")
	private String cancelReason;

	@Column(name = "CANCELLED")
	private boolean canceled;

	@Column(name = "DONE")
	private boolean done;

	@Column(name = "LASTCHANGE")
	private Date lastChange;
	
    @OneToMany(mappedBy = "customerSite", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrdersSite> ordersSite;
    
    

	public CustomersSite(CustomersSite cust) {
		super();
		this.site_id = cust.site_id;
		this.charCode = cust.charCode;
		this.date = cust.date;
		this.kitchen = cust.kitchen;
		this.cook = cust.cook;
		this.operator = cust.operator;
		this.partner = cust.partner;
		this.phone = cust.phone;
		this.name = cust.name;
		this.email = cust.email;
		this.street = cust.street;
		this.house = cust.house;
		this.apartament = cust.apartament;
		this.porch = cust.porch;
		this.floor = cust.floor;
		this.doorcode = cust.doorcode;
		this.region = cust.region;
		this.comment = cust.comment;
		this.discount = cust.discount;
		this.people = cust.people;
		this.info = cust.info;
		this.onTime = cust.onTime;
		this.deliveryTime = cust.deliveryTime;
		this.cost = cust.cost;
		this.bonus = cust.bonus;
		this.newBonus = cust.newBonus;
		this.fianlCost = cust.fianlCost;
		this.courier = cust.courier;
		this.sendTime = cust.sendTime;
		this.deliverTime = cust.deliverTime;
		this.deliverCost = cust.deliverCost;
		this.cancelReason = cust.cancelReason;
		this.canceled = cust.canceled;
		this.done = cust.done;
		this.lastChange = cust.lastChange;
		this.ordersSite = new ArrayList<>();
		for (OrdersSite os : cust.getOrdersSite()) 
			this.ordersSite.add(new OrdersSite(os, this));
	}

	public CustomersSite() {

	}

	public Long getSite_id() {
		return site_id;
	}

	public void setSite_id(Long site_id) {
		this.site_id = site_id;
	}

	public String getCharCode() {
		return charCode;
	}

	public void setCharCode(String charCode) {
		this.charCode = charCode;
	}

	public Date getDate() {
		return date;
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

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getHouse() {
		return house;
	}

	public void setHouse(String house) {
		this.house = house;
	}

	public String getApartament() {
		return apartament;
	}

	public void setApartament(String apartament) {
		this.apartament = apartament;
	}

	public String getPorch() {
		return porch;
	}

	public void setPorch(String porch) {
		this.porch = porch;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getDoorcode() {
		return doorcode;
	}

	public void setDoorcode(String doorcode) {
		this.doorcode = doorcode;
	}

	public int getRegion() {
		return region;
	}

	public void setRegion(int region) {
		this.region = region;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	public int getPeople() {
		return people;
	}

	public void setPeople(int people) {
		this.people = people;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public boolean isOnTime() {
		return onTime;
	}

	public void setOnTime(boolean onTime) {
		this.onTime = onTime;
	}

	public Date getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getBonus() {
		return bonus;
	}

	public void setBonus(int bonus) {
		this.bonus = bonus;
	}

	public int getNewBonus() {
		return newBonus;
	}

	public void setNewBonus(int newBonus) {
		this.newBonus = newBonus;
	}

	public int getFianlCost() {
		return fianlCost;
	}

	public void setFianlCost(int fianlCost) {
		this.fianlCost = fianlCost;
	}

	public String getCourier() {
		return courier;
	}

	public void setCourier(String courier) {
		this.courier = courier;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Date getDeliverTime() {
		return deliverTime;
	}

	public void setDeliverTime(Date deliverTime) {
		this.deliverTime = deliverTime;
	}

	public int getDeliverCost() {
		return deliverCost;
	}

	public void setDeliverCost(int deliverCost) {
		this.deliverCost = deliverCost;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	public boolean isCanceled() {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public Date getLastChange() {
		return lastChange;
	}

	public void setLastChange(Date lastChange) {
		this.lastChange = lastChange;
	}
	
	

	public List<OrdersSite> getOrdersSite() {
		return ordersSite;
	}

	public void setOrdersSite(List<OrdersSite> ordersSite) {
		this.ordersSite = ordersSite;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((apartament == null) ? 0 : apartament.hashCode());
		result = prime * result + bonus;
		result = prime * result + ((cancelReason == null) ? 0 : cancelReason.hashCode());
		result = prime * result + (canceled ? 1231 : 1237);
		result = prime * result + ((charCode == null) ? 0 : charCode.hashCode());
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + ((cook == null) ? 0 : cook.hashCode());
		result = prime * result + cost;
		result = prime * result + ((courier == null) ? 0 : courier.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + deliverCost;
		result = prime * result + ((deliverTime == null) ? 0 : deliverTime.hashCode());
		result = prime * result + ((deliveryTime == null) ? 0 : deliveryTime.hashCode());
		result = prime * result + discount;
		result = prime * result + (done ? 1231 : 1237);
		result = prime * result + ((doorcode == null) ? 0 : doorcode.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + fianlCost;
		result = prime * result + ((floor == null) ? 0 : floor.hashCode());
		result = prime * result + ((house == null) ? 0 : house.hashCode());
		result = prime * result + ((info == null) ? 0 : info.hashCode());
		result = prime * result + kitchen;
		result = prime * result + ((lastChange == null) ? 0 : lastChange.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + newBonus;
		result = prime * result + (onTime ? 1231 : 1237);
		result = prime * result + ((operator == null) ? 0 : operator.hashCode());
		result = prime * result + ((partner == null) ? 0 : partner.hashCode());
		result = prime * result + people;
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime * result + ((porch == null) ? 0 : porch.hashCode());
		result = prime * result + region;
		result = prime * result + ((sendTime == null) ? 0 : sendTime.hashCode());
		result = prime * result + ((site_id == null) ? 0 : site_id.hashCode());
		result = prime * result + ((street == null) ? 0 : street.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomersSite other = (CustomersSite) obj;
		if (apartament == null) {
			if (other.apartament != null)
				return false;
		} else if (!apartament.equals(other.apartament))
			return false;
		if (bonus != other.bonus)
			return false;
		if (cancelReason == null) {
			if (other.cancelReason != null)
				return false;
		} else if (!cancelReason.equals(other.cancelReason))
			return false;
		if (canceled != other.canceled)
			return false;
		if (charCode == null) {
			if (other.charCode != null)
				return false;
		} else if (!charCode.equals(other.charCode))
			return false;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (cook == null) {
			if (other.cook != null)
				return false;
		} else if (!cook.equals(other.cook))
			return false;
		if (cost != other.cost)
			return false;
		if (courier == null) {
			if (other.courier != null)
				return false;
		} else if (!courier.equals(other.courier))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (deliverCost != other.deliverCost)
			return false;
		if (deliverTime == null) {
			if (other.deliverTime != null)
				return false;
		} else if (!deliverTime.equals(other.deliverTime))
			return false;
		if (deliveryTime == null) {
			if (other.deliveryTime != null)
				return false;
		} else if (!deliveryTime.equals(other.deliveryTime))
			return false;
		if (discount != other.discount)
			return false;
		if (done != other.done)
			return false;
		if (doorcode == null) {
			if (other.doorcode != null)
				return false;
		} else if (!doorcode.equals(other.doorcode))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (fianlCost != other.fianlCost)
			return false;
		if (floor == null) {
			if (other.floor != null)
				return false;
		} else if (!floor.equals(other.floor))
			return false;
		if (house == null) {
			if (other.house != null)
				return false;
		} else if (!house.equals(other.house))
			return false;
		if (info == null) {
			if (other.info != null)
				return false;
		} else if (!info.equals(other.info))
			return false;
		if (kitchen != other.kitchen)
			return false;
		if (lastChange == null) {
			if (other.lastChange != null)
				return false;
		} else if (!lastChange.equals(other.lastChange))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (newBonus != other.newBonus)
			return false;
		if (onTime != other.onTime)
			return false;
		if (operator == null) {
			if (other.operator != null)
				return false;
		} else if (!operator.equals(other.operator))
			return false;
		if (partner == null) {
			if (other.partner != null)
				return false;
		} else if (!partner.equals(other.partner))
			return false;
		if (people != other.people)
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (porch == null) {
			if (other.porch != null)
				return false;
		} else if (!porch.equals(other.porch))
			return false;
		if (region != other.region)
			return false;
		if (sendTime == null) {
			if (other.sendTime != null)
				return false;
		} else if (!sendTime.equals(other.sendTime))
			return false;
		if (site_id == null) {
			if (other.site_id != null)
				return false;
		} else if (!site_id.equals(other.site_id))
			return false;
		if (street == null) {
			if (other.street != null)
				return false;
		} else if (!street.equals(other.street))
			return false;
		return true;
	}

	

}