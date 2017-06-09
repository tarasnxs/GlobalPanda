package ua.com.pandasushi.database.common;



import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "OrdersSite")
public class OrdersSite implements Serializable {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1901916134467496291L;

	@Id
    @Column(name = "OSITE_ID")
    private Long site_id;

    @Column(name = "LOCAL_ID")
    private Integer local_id;

    @Column(name = "CODE")
    private Integer code;

    @Column(name = "COUNT")
    private Integer count;

    @Column(name = "DISCOUNT")
    private Integer discount;

    @Column(name = "DISH")
    private String dish;

    @Column(name = "PRICE")
    private Integer price;

    @Column(name = "COST")
    private Integer cost;

    @Column(name = "SUM")
    private Integer sum;

    @Column(name = "UNITS")
    private String units;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "SITE_ID")
    private CustomersSite customerSite;
    
    public OrdersSite() {
    	
    }

    public OrdersSite(OrdersSite order, CustomersSite cust) {
		super();
		this.site_id = order.site_id;
		this.local_id = order.local_id;
		this.code = order.code;
		this.count = order.count;
		this.discount = order.discount;
		this.dish = order.dish;
		this.price = order.price;
		this.cost = order.cost;
		this.sum = order.sum;
		this.units = order.units;
		this.customerSite = cust;
	}


	public static long getSerialversionuid() {
        return serialVersionUID;
    }


    public Long getSite_id() {
        return site_id;
    }

    public void setSite_id(Long site_id) {
        this.site_id = site_id;
    }

    public Integer getLocal_id() {
        return local_id;
    }

    public void setLocal_id(Integer local_id) {
        this.local_id = local_id;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public String getDish() {
        return dish;
    }

    public void setDish(String dish) {
        this.dish = dish;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getSum() {
        return sum;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public CustomersSite getCustomerSite() {
        return customerSite;
    }

    public void setCustomerSite(CustomersSite customerSite) {
        this.customerSite = customerSite;
    }

}
