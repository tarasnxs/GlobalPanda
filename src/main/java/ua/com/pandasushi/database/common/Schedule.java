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
@Table(name = "Schedule")
public class Schedule implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "DATE")
	private Date date;
	
	@Column(name = "KITCH_ID")
	private Integer kitchId;
	
	@Column(name = "EMPLOYEE_ID")
	private int employeeId;
	
	@Column(name = "START")
	private Date start;
	
	@Column(name = "END")
	private Date end;
	
	@Column(name = "PLAN")
	private boolean plan;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getKitchId() {
		return kitchId;
	}

	public void setKitchId(Integer kitchId) {
		this.kitchId = kitchId;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public boolean isPlan() {
		return plan;
	}

	public void setPlan(boolean plan) {
		this.plan = plan;
	}

	public long getId() {
		return id;
	}
	
	
}
