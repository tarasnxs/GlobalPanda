package ua.com.pandasushi.database.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by Тарас on 27.10.2016.
 */
@Entity
@Table(name = "KITCHENS")
public class Kitchens implements Serializable {
    @Id
    @Column(name = "KITCH_ID")
    private Integer kitch_id;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "CITY")
    private String city;

    @Column(name = "NAME")
    private String name;

    public Integer getKitch_id() {
        return kitch_id;
    }

    public void setKitch_id(Integer kitch_id) {
        this.kitch_id = kitch_id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
