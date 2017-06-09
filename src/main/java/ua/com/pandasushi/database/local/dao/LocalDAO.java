package ua.com.pandasushi.database.local.dao;

import org.hibernate.SessionFactory;
import ua.com.pandasushi.database.common.DAO;
import ua.com.pandasushi.database.common.Employee;

import java.util.ArrayList;

/**
 * Created by Тарас on 24.10.2016.
 */
public class LocalDAO implements DAO {

    public LocalDAO () {
        super();

    }

    public ArrayList<Employee> getForLogin() {
        ArrayList<Employee> result = new ArrayList<>();
        return result;
    }


    @Override
    public void save(Object o) {

    }

    @Override
    public void update(Object o) {

    }


    @Override
    public SessionFactory getSessionFactory() {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public void begin() {

    }

    @Override
    public void commit() {

    }

    @Override
    public void rollback() {

    }
}
