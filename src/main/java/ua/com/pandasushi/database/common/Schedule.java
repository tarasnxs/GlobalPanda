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
	private Long id;
	
	@Column(name = "DATE")
	private Date date;
	
	@Column(name = "KITCH_ID")
	private Integer kitchId;
	
	@Column(name = "EMPLOYEE_ID")
	private int employeeId;
	
	@Column(name = "EMPLOYEE_NAME")
	private String employeeName;
	
	@Column(name = "MARK")
	private String mark;
	
	@Column(name = "OPERATOR")
	private String operator;
	
	@Column(name = "START")
	private Date start;
	
	@Column(name = "START_CHANGE")
	private Date startChange;
	
	@Column(name = "END")
	private Date end;
	
	@Column(name = "END_CHANGE")
	private Date endChange;
	
	@Column(name = "PLAN")
	private boolean plan;
	
	@Column(name = "COMMENT")
	private String comment;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getStartChange() {
		return startChange;
	}

	public void setStartChange(Date startChange) {
		this.startChange = startChange;
	}

	public Date getEndChange() {
		return endChange;
	}

	public void setEndChange(Date endChange) {
		this.endChange = endChange;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
	
	
}
