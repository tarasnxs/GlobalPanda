package ua.com.pandasushi.database.common;

import org.hibernate.SessionFactory;

/**
 * Created by Тарас on 26.10.2016.
 */
public interface DAO {
    void save(Object o);
    void update(Object o);
    SessionFactory getSessionFactory();
    void close();
    void begin();
    void commit();
    void rollback();
}
