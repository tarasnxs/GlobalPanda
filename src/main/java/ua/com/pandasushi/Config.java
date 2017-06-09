package ua.com.pandasushi;

import ua.com.pandasushi.database.common.Employee;
import ua.com.pandasushi.database.common.Kitchens;
import ua.com.pandasushi.database.site.dao.DAOSite;

import java.io.Serializable;

/**
 * Created by Тарас on 24.10.2016.
 */
public class Config implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -9158753816524149263L;
    private Kitchens kitchen;
    private Employee operator;


    Config() {
    }

    public Kitchens getKitchen() {
        return kitchen;
    }

    public void setKitchen(Kitchens kitchen) {
        this.kitchen = kitchen;
    }

    public Employee getOperator() {
        return operator;
    }

    public void setOperator(Employee operator) {
        this.operator = operator;
    }

}