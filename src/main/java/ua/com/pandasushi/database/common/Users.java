package ua.com.pandasushi.database.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "Users")
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "POSITION")
    private String position;

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

    @Column(name = "ADMIN")
    private Boolean admin;

    @Column(name = "ACCESS")
    private Integer[][] access;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "LASTLOGIN")
    private Date lastLogin;

    @Column(name = "LASTIP")
    private String lastIp;

    @Column(name = "MAC")
    private String mac;


    public Users() {

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

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public Integer[][] getAccess() {
        return access;
    }

    public void setAccess(Integer[][] access) {
        this.access = access;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getLastIp() {
        return lastIp;
    }

    public void setLastIp(String lastIp) {
        this.lastIp = lastIp;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}