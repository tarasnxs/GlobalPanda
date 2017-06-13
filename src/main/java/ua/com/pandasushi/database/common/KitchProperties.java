package ua.com.pandasushi.database.common;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "KitchProperties")
public class KitchProperties implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "DATE")
	private Date date;
	
	@Column(name = "WEEK_NUMBER")
	private Integer weekNumber;
	
	@Column(name = "KITCH_ID")
	private Integer kitchID;
	
	@Column(name = "OPERATORS")
	private Integer operators;
	
	@Column(name = "COOKS")
	private Integer cooks;
	
	@Column(name = "COURIERS")
	private Integer couriers;
	
	@Column(name = "START")
	private String start;
	
	@Column(name = "END")
	private String end;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getWeekNumber() {
		return weekNumber;
	}

	public void setWeekNumber(Integer weekNumber) {
		this.weekNumber = weekNumber;
	}

	public Integer getKitchID() {
		return kitchID;
	}

	public void setKitchID(Integer kitchID) {
		this.kitchID = kitchID;
	}

	public Integer getOperators() {
		return operators;
	}

	public void setOperators(Integer operators) {
		this.operators = operators;
	}

	public Integer getCooks() {
		return cooks;
	}

	public void setCooks(Integer cooks) {
		this.cooks = cooks;
	}

	public Integer getCouriers() {
		return couriers;
	}

	public void setCouriers(Integer couriers) {
		this.couriers = couriers;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public long getId() {
		return id;
	}
	
	
	
}
