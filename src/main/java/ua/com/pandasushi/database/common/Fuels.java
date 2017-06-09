package ua.com.pandasushi.database.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "Fuels")
public class Fuels implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "FUEL_ID")
	public Long fuelId;

	@Column(name = "STATIONFUEL")
	public String stationFuel;

	@Column(name = "PRICE")
	public Float price;

	public String getStationFuel() {
		return stationFuel;
	}

	public void setStationFuel(String stationFuel) {
		this.stationFuel = stationFuel;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Long getFuelId() {
		return fuelId;
	}

	public void setFuelId(Long fuelId) {
		this.fuelId = fuelId;
	}

}
