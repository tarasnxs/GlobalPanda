package ua.com.pandasushi.database.common.inventory;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "INV_ING_SELECT")
public class InvSelect implements Serializable {
	
	public static final String TYPE_MANUAL = "manual";
	public static final String TYPE_MONTH = "month";
	public static final String TYPE_RED_ZONE = "red_zone";
	public static final String TYPE_RANDOM = "random";
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "ID")
	private long id;
	
	@Column(name = "DATE")
	private Date date;
	
	@Column(name = "CITY")
	private String city;
	
	@Column(name = "ING_ID")
	private Integer ingId;
	
	@Column(name = "TYPE")
	private String type;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getIngId() {
		return ingId;
	}

	public void setIngId(Integer ingId) {
		this.ingId = ingId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	

	
	
}
