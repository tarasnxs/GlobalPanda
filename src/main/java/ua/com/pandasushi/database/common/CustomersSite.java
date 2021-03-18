package ua.com.pandasushi.database.common;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "CustomersSite")
public class CustomersSite implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "SITE_ID", columnDefinition = "BIGINT(20)")
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

    @Column(name = "SAVE_BONUS", nullable = false, columnDefinition = "int default 0")
    private int saveBonus = 0;

    @Column(name = "USED_BONUS", nullable = false, columnDefinition = "int default 0")
    private int usedBonus = 0;

    @Column(name = "START_BONUS", nullable = false, columnDefinition = "boolean not null default 0")
    private boolean startBonus;

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

    @Column(name = "MENU_ADDED")
    private Boolean menuAdded;

    @Column(name = "DELIVER_AT_LON")
    private Double deliveredLon;

    @Column(name = "DELIVER_AT_LAT")
    private Double deliveredLat;

    @Column(name = "TRACK_ID")
    private Long trackId;

    @Column(name = "PROMOCODE_ID")
    private Long promocodeId;

    @Column(name = "COMPENSATION_COUNT")
    private Integer compensationCount;

    @Column(name = "COMPENSATION_EXPIRED")
    private Date compensationExpired;

    @Column(name = "GREEN_ZONE")
    private Boolean greenZone;

    @Column(name = "ZONE")
    private String zone;

    @Column(name = "COMPENSATION_ADDED")
    private Boolean compensationAdded;

    @Column(name = "COMPENSATION_USED")
    private Boolean compensationUsed;

    @Column(name = "DAYS_AFTER_LAST_ORDER")
    private Integer daysAfterLastOrder;

    @Column(name = "PAYMENT_METHOD")
    private String paymentMethod;

    @Column(name = "ONLINE_PAYMENT_NUMBER")
    private Integer onlinePaymentNumber;

    @Column(name = "SHIFT_OPEN")
    private String shiftOpen;

    @Column(name = "SHIFT_CLOSE")
    private String shiftClose;

    @Column(name = "FINAL_PAYMENT_METHOD")
    private String finalPaymentMethod;

    @OneToMany(mappedBy = "customerSite", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @org.hibernate.annotations.ForeignKey(name = "none")
    private List<OrdersSite> ordersSite;

    public CustomersSite() {

    }

    public String getFinalPaymentMethod() {
        return finalPaymentMethod;
    }

    public void setFinalPaymentMethod(String finalPaymentMethod) {
        this.finalPaymentMethod = finalPaymentMethod;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }


    public Boolean getCompensationAdded() {
        return compensationAdded;
    }

    public void setCompensationAdded(Boolean compensationAdded) {
        this.compensationAdded = compensationAdded;
    }

    public Boolean getCompensationUsed() {
        return compensationUsed;
    }

    public void setCompensationUsed(Boolean compensationUsed) {
        this.compensationUsed = compensationUsed;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public Double getDeliveredLon() {
        return deliveredLon;
    }

    public void setDeliveredLon(Double deliveredLon) {
        this.deliveredLon = deliveredLon;
    }

    public Double getDeliveredLat() {
        return deliveredLat;
    }

    public void setDeliveredLat(Double deliveredLat) {
        this.deliveredLat = deliveredLat;
    }

    public Long getTrackId() {
        return trackId;
    }

    public void setTrackId(Long trackId) {
        this.trackId = trackId;
    }

    public boolean isStartBonus() {
        return startBonus;
    }

    public void setStartBonus(boolean startBonus) {
        this.startBonus = startBonus;
    }

    public Boolean isMenuAdded() {
        return menuAdded;
    }

    public void setMenuAdded(Boolean menuAdded) {
        this.menuAdded = menuAdded;
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

    public int getSaveBonus() {
        return saveBonus;
    }

    public void setSaveBonus(int saveBonus) {
        this.saveBonus = saveBonus;
    }

    public int getUsedBonus() {
        return usedBonus;
    }

    public void setUsedBonus(int usedBonus) {
        this.usedBonus = usedBonus;
    }

    public Long getPromocodeId() {
        return promocodeId;
    }

    public void setPromocodeId(Long promocodeId) {
        this.promocodeId = promocodeId;
    }

    public Integer getCompensationCount() {
        return compensationCount;
    }

    public void setCompensationCount(Integer compensationCount) {
        this.compensationCount = compensationCount;
    }

    public Date getCompensationExpired() {
        return compensationExpired;
    }

    public void setCompensationExpired(Date compensationExpired) {
        this.compensationExpired = compensationExpired;
    }

    public Boolean getGreenZone() {
        return greenZone;
    }

    public void setGreenZone(Boolean greenZone) {
        this.greenZone = greenZone;
    }

    public Integer getDaysAfterLastOrder() {
        return daysAfterLastOrder;
    }

    public void setDaysAfterLastOrder(Integer daysAfterLastOrder) {
        this.daysAfterLastOrder = daysAfterLastOrder;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Integer getOnlinePaymentNumber() {
        return onlinePaymentNumber;
    }

    public void setOnlinePaymentNumber(Integer onlinePaymentNumber) {
        this.onlinePaymentNumber = onlinePaymentNumber;
    }

    public String getShiftOpen() {
        return shiftOpen;
    }

    public void setShiftOpen(String shiftOpen) {
        this.shiftOpen = shiftOpen;
    }

    public String getShiftClose() {
        return shiftClose;
    }

    public void setShiftClose(String shiftClose) {
        this.shiftClose = shiftClose;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomersSite that = (CustomersSite) o;

        if (kitchen != that.kitchen) return false;
        if (region != that.region) return false;
        if (discount != that.discount) return false;
        if (people != that.people) return false;
        if (onTime != that.onTime) return false;
        if (cost != that.cost) return false;
        if (bonus != that.bonus) return false;
        if (newBonus != that.newBonus) return false;
        if (saveBonus != that.saveBonus) return false;
        if (usedBonus != that.usedBonus) return false;
        if (fianlCost != that.fianlCost) return false;
        if (deliverCost != that.deliverCost) return false;
        if (canceled != that.canceled) return false;
        if (done != that.done) return false;
        if (site_id != null ? !site_id.equals(that.site_id) : that.site_id != null) return false;
        if (charCode != null ? !charCode.equals(that.charCode) : that.charCode != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (cook != null ? !cook.equals(that.cook) : that.cook != null) return false;
        if (operator != null ? !operator.equals(that.operator) : that.operator != null) return false;
        if (partner != null ? !partner.equals(that.partner) : that.partner != null) return false;
        if (phone != null ? !phone.equals(that.phone) : that.phone != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (street != null ? !street.equals(that.street) : that.street != null) return false;
        if (house != null ? !house.equals(that.house) : that.house != null) return false;
        if (apartament != null ? !apartament.equals(that.apartament) : that.apartament != null) return false;
        if (porch != null ? !porch.equals(that.porch) : that.porch != null) return false;
        if (floor != null ? !floor.equals(that.floor) : that.floor != null) return false;
        if (doorcode != null ? !doorcode.equals(that.doorcode) : that.doorcode != null) return false;
        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;
        if (info != null ? !info.equals(that.info) : that.info != null) return false;
        if (deliveryTime != null ? !deliveryTime.equals(that.deliveryTime) : that.deliveryTime != null) return false;
        if (courier != null ? !courier.equals(that.courier) : that.courier != null) return false;
        if (sendTime != null ? !sendTime.equals(that.sendTime) : that.sendTime != null) return false;
        if (deliverTime != null ? !deliverTime.equals(that.deliverTime) : that.deliverTime != null) return false;
        if (cancelReason != null ? !cancelReason.equals(that.cancelReason) : that.cancelReason != null) return false;
        if (lastChange != null ? !lastChange.equals(that.lastChange) : that.lastChange != null) return false;
        if (menuAdded != null ? !menuAdded.equals(that.menuAdded) : that.menuAdded != null) return false;
        return ordersSite != null ? ordersSite.equals(that.ordersSite) : that.ordersSite == null;
    }

    @Override
    public int hashCode() {
        int result = site_id != null ? site_id.hashCode() : 0;
        result = 31 * result + (charCode != null ? charCode.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + kitchen;
        result = 31 * result + (cook != null ? cook.hashCode() : 0);
        result = 31 * result + (operator != null ? operator.hashCode() : 0);
        result = 31 * result + (partner != null ? partner.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (street != null ? street.hashCode() : 0);
        result = 31 * result + (house != null ? house.hashCode() : 0);
        result = 31 * result + (apartament != null ? apartament.hashCode() : 0);
        result = 31 * result + (porch != null ? porch.hashCode() : 0);
        result = 31 * result + (floor != null ? floor.hashCode() : 0);
        result = 31 * result + (doorcode != null ? doorcode.hashCode() : 0);
        result = 31 * result + region;
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + discount;
        result = 31 * result + people;
        result = 31 * result + (info != null ? info.hashCode() : 0);
        result = 31 * result + (onTime ? 1 : 0);
        result = 31 * result + (deliveryTime != null ? deliveryTime.hashCode() : 0);
        result = 31 * result + cost;
        result = 31 * result + bonus;
        result = 31 * result + newBonus;
        result = 31 * result + saveBonus;
        result = 31 * result + usedBonus;
        result = 31 * result + fianlCost;
        result = 31 * result + (courier != null ? courier.hashCode() : 0);
        result = 31 * result + (sendTime != null ? sendTime.hashCode() : 0);
        result = 31 * result + (deliverTime != null ? deliverTime.hashCode() : 0);
        result = 31 * result + deliverCost;
        result = 31 * result + (cancelReason != null ? cancelReason.hashCode() : 0);
        result = 31 * result + (canceled ? 1 : 0);
        result = 31 * result + (done ? 1 : 0);
        result = 31 * result + (lastChange != null ? lastChange.hashCode() : 0);
        result = 31 * result + (menuAdded != null ? menuAdded.hashCode() : 0);
        result = 31 * result + (ordersSite != null ? ordersSite.hashCode() : 0);
        return result;
    }
}