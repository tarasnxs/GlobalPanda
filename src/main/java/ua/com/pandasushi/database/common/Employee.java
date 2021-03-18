package ua.com.pandasushi.database.common;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Employee")
public class Employee implements Serializable {
	/*****
	 * * *** * *** * * * * * * * ***** * * ***** *** * * * * * * *
	 ***/
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "NAME")
	private String name;

	@Column(name = "POSITION")
	private String position;
	
	@Column(name = "MARK")
	private String mark;
	
	@Column(name = "KITCH_ID")
	private Integer kitch_id;

	@Column(name = "KITCHEN")
	private String kitchen;

	@Column(name = "HOURLY_PRICE")
	private float hourlyPrice;

	@Column(name = "FUEL_CONS")
	private float fuelCons;

	@Column(name = "FUELSTATION")
	private String fuelStation;

	@Column(name = "CHARCODE")
	private String charcode;
	
	@Column(name = "ACTIVE")
	private boolean active;
	
	@Column(name = "LOGIN")
	private String login;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "GP_ACCESS")
	private Boolean gpAccess;

	public Employee() {
		
	}

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                '}';
    }

    public boolean isActive() {
		return active;
	}
	
	public void setActive (boolean active) {
		this.active = active;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getKitchen() {
		return kitchen;
	}

	public void setKitchen(String kitchen) {
		this.kitchen = kitchen;
	}

	public float getHourlyPrice() {
		return hourlyPrice;
	}

	public void setHourlyPrice(float hourlyPrice) {
		this.hourlyPrice = hourlyPrice;
	}

	public float getFuelCons() {
		return fuelCons;
	}

	public void setFuelCons(float fuelCons) {
		this.fuelCons = fuelCons;
	}


	public String getFuelStation() {
		return fuelStation;
	}

	public void setFuelStation(String fuelStation) {
		this.fuelStation = fuelStation;
	}

	public String getCharcode() {
		return charcode;
	}

	public void setCharcode(String charcode) {
		this.charcode = charcode;
	}

	public int getId() {
		return id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isGpAccess() {
		return gpAccess;
	}

	public void setGpAccess(boolean gpAccess) {
		this.gpAccess = gpAccess;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public Integer getKitch_id() {
		return kitch_id;
	}

	public void setKitch_id(Integer kitch_id) {
		this.kitch_id = kitch_id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	
	
}
