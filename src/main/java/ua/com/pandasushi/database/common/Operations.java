package ua.com.pandasushi.database.common;

import org.hibernate.annotations.*;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Cacheable
@org.hibernate.annotations.Cache(include = "all", usage = CacheConcurrencyStrategy.READ_WRITE)
@Table (name = "Operations")
public class Operations implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int PRODUCT_PURCHASE = 400;
	public static final int DEBT_PURCHASE = 1000;
	public static final int PRODUCT_SHIFT = 500;
	public static final int CASH_SHIFT = 600;
	
	@Id
	@Column(name = "OPERATION_ID")
	private Integer operation_id;
	
	@Column(name = "KITCHEN")
	private Integer kitchen;
	
	@Column(name = "TYPE")
	private Integer type;
	
	@Column(name = "DATE")
	private Date date;
	
	@Column(name = "STARTPERIOD")
	private Date startPeriod;
	
	@Column(name = "ENDPERIOD")
	private Date endPeriod;
	
	@Column(name = "SUM")
	private Float sum;
	
	@Column(name = "CURRENCY")
	private String currency;

	@Column(name = "CUR_TO_UAH")
	private Float curToUah;
	
	@Column(name = "OPERATOR")
	private String operator;
	
	@Column(name = "CONTRAGENT")
	private String contrAgent;
	
	@Column(name = "DESCRIPTION1")
	private String description1;
	
	@Column(name = "DESCRIPTION2")
	private String description2;

	@Column(name = "DESCRIPTION3")
	private String description3;
	
	@Column(name = "INTPARAMETER1")
	private Float intparameter1;
	
	@Column(name = "INTPARAMETER2")
	private Float intparameter2;
	
	@Column(name = "INTPARAMETER3")
	private Float intparameter3;

	@Column(name = "INTPARAMETER4")
	private Float intparameter4;
	
	@Column(name = "FLOATPARAMETER1")
	private Float floatparameter1;
	
	@Column(name = "FLOATPARAMETER2")
	private Float floatparameter2;
	
	@Column(name = "FLOATPARAMETER3")
	private Float floatparameter3;

	@Column(name = "FLOATPARAMETER4")
	private Float floatparameter4;
	
	@Column(name = "BOOLPARAMETER1")
	private Boolean boolparameter1;
	
	@Column(name = "BOOLPARAMETER2")
	private Boolean boolparameter2;
	
	@Column(name = "BOOLPARAMETER3")
	private Boolean boolparameter3;

	@Column(name = "BOOLPARAMETER4")
	private Boolean boolparameter4;
	
	@Column(name = "STRPARAMETER1")
	private String strparameter1;
	
	@Column(name = "STRPARAMETER2")
	private String strparameter2;
	
	@Column(name = "STRPARAMETER3")
	private String strparameter3;

	@Column(name = "STRPARAMETER4")
	private String strparameter4;

	@Column(name = "CHECK_ID")
	private Integer checkId;

	public Integer getKitchen() {
		return kitchen;
	}

	public void setKitchen(Integer kitchen) {
		this.kitchen = kitchen;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getDate() {
		return new Date(date.getTime());
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getStartPeriod() {
		return new Date(startPeriod.getTime());
	}

	public void setStartPeriod(Date startPeriod) {
		this.startPeriod = startPeriod;
	}

	public Date getEndPeriod() {
		return new Date(endPeriod.getTime());
	}

	public void setEndPeriod(Date endPeriod) {
		this.endPeriod = endPeriod;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Float getCurToUah() {
		return curToUah;
	}

	public void setCurToUah(Float curToUah) {
		this.curToUah = curToUah;
	}

	public Float getSum() {
		return sum;
	}

	public void setSum(Float sum) {
		this.sum = sum;
	}

	public void addSum(Float sum) {
		this.sum += sum;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getContrAgent() {
		return contrAgent;
	}

	public void setContrAgent(String contrAgent) {
		this.contrAgent = contrAgent;
	}

	public String getDescription1() {
		return description1;
	}

	public void setDescription1(String description1) {
		this.description1 = description1;
	}

	public String getDescription2() {
		return description2;
	}

	public void setDescription2(String description2) {
		this.description2 = description2;
	}

	public Float getIntparameter1() {
		return intparameter1;
	}

	public void setIntparameter1(Float intparameter1) {
		this.intparameter1 = intparameter1;
	}

	public Float getIntparameter2() {
		return intparameter2;
	}

	public void setIntparameter2(Float intparameter2) {
		this.intparameter2 = intparameter2;
	}

	public Float getIntparameter3() {
		return intparameter3;
	}

	public void setIntparameter3(Float intparameter3) {
		this.intparameter3 = intparameter3;
	}

	public Float getFloatparameter1() {
		return floatparameter1;
	}

	public void setFloatparameter1(Float floatparameter1) {
		this.floatparameter1 = floatparameter1;
	}

	public Float getFloatparameter2() {
		return floatparameter2;
	}

	public void setFloatparameter2(Float floatparameter2) {
		this.floatparameter2 = floatparameter2;
	}

	public Float getFloatparameter3() {
		return floatparameter3;
	}

	public void setFloatparameter3(Float floatparameter3) {
		this.floatparameter3 = floatparameter3;
	}

	public Boolean getBoolparameter1() {
		return boolparameter1;
	}

	public void setBoolparameter1(Boolean boolparameter1) {
		this.boolparameter1 = boolparameter1;
	}

	public Boolean getBoolparameter2() {
		return boolparameter2;
	}

	public void setBoolparameter2(Boolean boolparameter2) {
		this.boolparameter2 = boolparameter2;
	}

	public Boolean getBoolparameter3() {
		return boolparameter3;
	}

	public void setBoolparameter3(Boolean boolparameter3) {
		this.boolparameter3 = boolparameter3;
	}

	public String getStrparameter1() {
		return strparameter1;
	}

	public void setStrparameter1(String strparameter1) {
		this.strparameter1 = strparameter1;
	}

	public String getStrparameter2() {
		return strparameter2;
	}

	public void setStrparameter2(String strparameter2) {
		this.strparameter2 = strparameter2;
	}

	public String getStrparameter3() {
		return strparameter3;
	}

	public void setStrparameter3(String strparameter3) {
		this.strparameter3 = strparameter3;
	}

	public Integer getOperation_id() {
		return operation_id;
	}

	public void setOperation_id(Integer operation_id) {
		this.operation_id = operation_id;
	}

	public String getDescription3() {
		return description3;
	}

	public void setDescription3(String description3) {
		this.description3 = description3;
	}

	public Float getIntparameter4() {
		return intparameter4;
	}

	public void setIntparameter4(Float intparameter4) {
		this.intparameter4 = intparameter4;
	}

	public Float getFloatparameter4() {
		return floatparameter4;
	}

	public void setFloatparameter4(Float floatparameter4) {
		this.floatparameter4 = floatparameter4;
	}

	public Boolean getBoolparameter4() {
		return boolparameter4;
	}

	public void setBoolparameter4(Boolean boolparameter4) {
		this.boolparameter4 = boolparameter4;
	}

	public String getStrparameter4() {
		return strparameter4;
	}

	public void setStrparameter4(String strparameter4) {
		this.strparameter4 = strparameter4;
	}

	public Integer getCheckId() {
		return checkId;
	}

	public void setCheckId(Integer checkId) {
		this.checkId = checkId;
	}

	@Override
	public String toString() {
		return "Operations{" +
				"operation_id=" + operation_id +
				", kitchen=" + kitchen +
				", type=" + type +
				", date=" + date +
				", startPeriod=" + startPeriod +
				", endPeriod=" + endPeriod +
				", sum=" + sum +
				", currency='" + currency + '\'' +
				", operator='" + operator + '\'' +
				", contrAgent='" + contrAgent + '\'' +
				", description1='" + description1 + '\'' +
				", description2='" + description2 + '\'' +
				", description3='" + description3 + '\'' +
				", intparameter1=" + intparameter1 +
				", intparameter2=" + intparameter2 +
				", intparameter3=" + intparameter3 +
				", intparameter4=" + intparameter4 +
				", floatparameter1=" + floatparameter1 +
				", floatparameter2=" + floatparameter2 +
				", floatparameter3=" + floatparameter3 +
				", floatparameter4=" + floatparameter4 +
				", boolparameter1=" + boolparameter1 +
				", boolparameter2=" + boolparameter2 +
				", boolparameter3=" + boolparameter3 +
				", boolparameter4=" + boolparameter4 +
				", strparameter1='" + strparameter1 + '\'' +
				", strparameter2='" + strparameter2 + '\'' +
				", strparameter3='" + strparameter3 + '\'' +
				", strparameter4='" + strparameter4 + '\'' +
				", checkId=" + checkId +
				'}';
	}
}
