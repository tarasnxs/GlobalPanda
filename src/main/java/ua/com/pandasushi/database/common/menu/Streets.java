package ua.com.pandasushi.database.common.menu;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Cacheable
@org.hibernate.annotations.Cache(include = "all", usage = CacheConcurrencyStrategy.READ_WRITE)
@Table (name = "LIB_STREETS")
public class Streets implements Serializable {
	@Id
	@Column(name = "STREET_ID")
	private int id;
	
	@Column(name = "STREET")
	private String street;
	
	@Column(name = "REGION")
	private String region;
	
	@Column(name = "KITCHEN")
	private int kitchen;
	
	@Column(name = "MIN_ORDER")
	private int minOrder;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public int getKitchen() {
		return kitchen;
	}

	public void setKitchen(int kitchen) {
		this.kitchen = kitchen;
	}

	public int getMinOrder() {
		return minOrder;
	}

	public void setMinOrder(int minOrder) {
		this.minOrder = minOrder;
	}
	
	
}
