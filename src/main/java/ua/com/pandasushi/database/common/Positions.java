package ua.com.pandasushi.database.common;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by Тарас on 27.10.2016.
 */
public class Positions implements Serializable {
    @Id
    @Column(name = "POS_ID")
    private Long pos_id;

    @Column(name = "POSITION")
    private String position;

    @Column(name = "DEFAULT_ACCESS")
    private Integer[][] defaultAccess;

    public Long getPos_id() {
        return pos_id;
    }

    public void setPos_id(Long pos_id) {
        this.pos_id = pos_id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer[][] getDefaultAccess() {
        return defaultAccess;
    }

    public void setDefaultAccess(Integer[][] defaultAccess) {
        this.defaultAccess = defaultAccess;
    }
}
